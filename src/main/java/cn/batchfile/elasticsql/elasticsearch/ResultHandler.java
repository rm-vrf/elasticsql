package cn.batchfile.elasticsql.elasticsearch;

import java.util.List;
import java.util.Map;

public interface ResultHandler {

	List<String> getHead();
	
	List<Map<String, Object>> getBody();
}
