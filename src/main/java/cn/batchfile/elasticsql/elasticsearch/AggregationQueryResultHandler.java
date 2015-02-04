package cn.batchfile.elasticsql.elasticsearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregations;

public class AggregationQueryResultHandler implements ResultHandler {
	
	private JSONObject data;
	private List<Map<String, Object>> flattenBuckets;

	public AggregationQueryResultHandler(SearchResponse data) {
		this.data = JSONObject.fromObject(data.toString());
		this.flattenBuckets = getRows(null, this.data.getJSONObject("aggregations"), new HashMap());
	}
	
	private List<Map<String, Object>> getRows(String bucketName, JSONObject bucket, Map additionalColumns) {
		List<Map<String, Object>> rows = new ArrayList<Map<String,Object>>();
		JSONArray subBuckets = getSubBuckets(bucket);
		if (subBuckets.size() > 0) {
			for (int i = 0; i < subBuckets.size(); i++) {
				String subBucketName = subBuckets.getJSONObject(i).getString("bucketName");
				JSONObject subBucket = subBuckets.getJSONObject(i).getJSONObject("bucket");
				
				Map newAdditionalColumns = new HashMap();
				if(bucketName != null) {
					Map newColumn = new HashMap();
					//newColumn.put(bucketName, bucket.);
				}
			}
		} else {
			
		}
		return rows;
	}
	
	private JSONArray getSubBuckets(JSONObject bucket) {
		JSONArray subBuckets = new JSONArray();
		for (Object field : bucket.keySet()) {
			JSONArray buckets = bucket.getJSONObject(field.toString()).getJSONArray("buckets");
			if (buckets != null) {
				for (int i = 0; i < buckets.size(); i ++) {
					JSONObject element = new JSONObject();
					element.put("bucketName", field);
					element.put("bucket", buckets.getJSONObject(i));
					subBuckets.add(element);
				}
			}
		}
		return subBuckets;
	}

	@Override
	public List<String> getHead() {
		List<String> head = new ArrayList<String>();
		for (Map<String, Object> bucket : flattenBuckets) {
			for (String key : bucket.keySet()) {
				if (!head.contains(key)) {
					head.add(key);
				}
			}
		}
		return head;
	}

	@Override
	public List<Map<String, Object>> getBody() {
		return flattenBuckets;
	}
	
}
