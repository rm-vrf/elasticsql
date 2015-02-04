package cn.batchfile.elasticsql.elasticsearch;

import com.github.mpjct.jmpjct.mysql.proto.ResultSet;

public class Result {
	public ResultSet resultSet;
    public long affectedRows = 0;
    public long lastInsertId = 0;
    public long warnings = 0;
}
