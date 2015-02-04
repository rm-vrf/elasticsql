package cn.batchfile.elasticsql.elasticsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

public class DefaultQueryResultHandler implements ResultHandler {
	
	private List<String> head;
	private SearchResponse data;

	public DefaultQueryResultHandler(SearchResponse data) {
		this.data = data;
		this.head = createSchema(data.getHits());
	}

	public List<String> getHead() {
		return head;
	}

	public List<Map<String, Object>> getBody() {
		SearchHit[] hits = data.getHits().getHits();
		List<Map<String, Object>> body = new ArrayList<Map<String,Object>>();
		for (SearchHit hit : hits) {
			body.add(hit.getSource());
		}
		return body;
	}

	private List<String> createSchema(SearchHits data) {
		SearchHit[] hits = data.getHits();
		List<String> schema = new ArrayList<String>();
		for (SearchHit hit : hits) {
			for (String key : hit.getSource().keySet()) {
				if (!schema.contains(key)) {
					schema.add(key);
				}
			}
		}
		return schema;
	}
}
