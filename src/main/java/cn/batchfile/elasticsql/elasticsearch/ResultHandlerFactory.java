package cn.batchfile.elasticsql.elasticsearch;

import org.elasticsearch.action.search.SearchResponse;

public class ResultHandlerFactory {
	
	public static ResultHandler create(SearchResponse response) {
		if (response.getAggregations() == null) {
			return new DefaultQueryResultHandler(response);
		} else {
			return new AggregationQueryResultHandler(response);
		}
	}
}
