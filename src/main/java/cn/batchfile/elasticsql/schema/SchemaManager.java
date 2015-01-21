package cn.batchfile.elasticsql.schema;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

public class SchemaManager {
	private static final Logger logger = Logger.getLogger("SchemaManager");
	private DataSource ds;
	
	public void connect(String url, String username, String password) {
		BasicDataSource bds = new BasicDataSource();
		bds.setDriverClassName("com.mysql.jdbc.Driver");
		bds.setUrl(url);
		bds.setUsername(username);
		bds.setPassword(password);
		bds.setMaxActive(200);
		bds.setValidationQuery("SELECT 1");
		bds.setTestWhileIdle(true);
		bds.setTimeBetweenEvictionRunsMillis(3600000);
		bds.setMinEvictableIdleTimeMillis(1800000);
		bds.setTestOnBorrow(true);
		ds = bds;
	}

	public List<Schema> get_databases() {
		return null;
	}
	
	public List<Table> get_tables(String schema) {
		return null;
	}
	
	public List<Column> get_columns(String schema, String table) {
		return null;
	}
	
	public void create_database(Schema schema) {
	}
	
	public void drop_database(String schema) {
	}
	
	public void create_table(String schema, Table table, List<Column> columns) {
	}
	
	public void drop_table(String schema, String table) {
	}
	
	public void add_column(String schema, String table, Column column) {
	}
	
	public void drop_column(String schema, String table, String column) {
	}
}
