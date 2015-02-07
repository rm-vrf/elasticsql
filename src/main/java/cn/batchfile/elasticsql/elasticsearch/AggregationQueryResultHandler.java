package cn.batchfile.elasticsql.elasticsearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.elasticsearch.action.search.SearchResponse;

public class AggregationQueryResultHandler implements ResultHandler {
	
	private JSONObject data;
	private List<Map<String, Object>> flattenBuckets;

	public AggregationQueryResultHandler(SearchResponse data) {
		this.data = JSONObject.fromObject(data.toString());
		this.flattenBuckets = getRows(null, this.data.getJSONObject("aggregations"), new HashMap<String, Object>());
	}
	
	private List<Map<String, Object>> getRows(String bucketName, JSONObject bucket, Map<String, Object> additionalColumns) {
		List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
		
		JSONArray subBuckets = getSubBuckets(bucket);
		if (subBuckets.size() > 0) {
			for (int i = 0; i < subBuckets.size(); i++) {
				String subBucketName = subBuckets.getJSONObject(i).getString("bucketName");
				JSONObject subBucket = subBuckets.getJSONObject(i).getJSONObject("bucket");
				
				Map<String, Object> newAdditionalColumns = new HashMap<String, Object>();
				// bucket without parents
				if (bucketName != null) {
					JSONObject newColumn = new JSONObject();
					newColumn.put(bucketName, bucket.get("key"));
					newAdditionalColumns = extend(newColumn, additionalColumns);
				}
				
				List<Map<String, Object>> newRows = getRows(subBucketName, subBucket, newAdditionalColumns);
				merge(rows, newRows);
			}
		} else {
			Map<String, Object> obj = extend(new JSONObject(), additionalColumns);
			if (bucketName != null) {
				obj.put(bucketName, bucket.get("key"));
			}
			
			for (Object field : bucket.keySet()) {
				Object val = bucket.get(field);
				if (val instanceof JSONObject && ((JSONObject)val).containsKey("value")) {
					obj.put(field.toString(), ((JSONObject)val).get("value"));
				} else {
					continue;
				}
			}
			rows.add(obj);
		}
		return rows;
	}
	
	private void merge(List<Map<String, Object>> array1, List<Map<String, Object>> array2) {
		for (int i = 0; i < array2.size(); i ++) {
			array1.add(array2.get(i));
		}
	}
	
	private Map<String, Object> extend(JSONObject obj1, Map<String, Object> obj2) {
		Map<String, Object> ret = new HashMap<String, Object>();
		for (Object key : obj1.keySet()) {
			ret.put(key.toString(), obj1.get(key));
		}
		for (Object key : obj2.keySet()) {
			ret.put(key.toString(), obj2.get(key));
		}
		return ret;
	}
	
	private JSONArray getSubBuckets(JSONObject bucket) {
		JSONArray subBuckets = new JSONArray();
		for (Object field : bucket.keySet()) {
			Object val = bucket.get(field);
			if (val instanceof JSONObject && ((JSONObject)val).containsKey("buckets")) {
				JSONArray buckets = ((JSONObject)val).getJSONArray("buckets");
				if (buckets != null) {
					for (int i = 0; i < buckets.size(); i ++) {
						JSONObject element = new JSONObject();
						element.put("bucketName", field);
						element.put("bucket", buckets.getJSONObject(i));
						subBuckets.add(element);
					}
				}
			}
		}
		return subBuckets;
	}

	public List<String> getHead() {
		List<String> head = new ArrayList<String>();
		for (int i = 0; i < flattenBuckets.size(); i ++) {
			for (Object key : flattenBuckets.get(i).keySet()) {
				if (!head.contains(key)) {
					head.add(key.toString());
				}
			}
		}
		return head;
	}

	public List<Map<String, Object>> getBody() {
		return flattenBuckets;
	}
	
}
