package cn.batchfile.elasticsql.schema;

public class Schema {

	public String name;
	public int number_of_shards = 5;
	public int number_of_replicas = 2;
	public boolean ttl_enabled = false;
	public String ttl_default;

}
