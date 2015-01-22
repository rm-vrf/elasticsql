package cn.batchfile.elasticsql.elasticsearch;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.github.mpjct.jmpjct.mysql.proto.Column;
import com.github.mpjct.jmpjct.mysql.proto.ERR;
import com.github.mpjct.jmpjct.mysql.proto.OK;
import com.github.mpjct.jmpjct.mysql.proto.ResultSet;
import com.github.mpjct.jmpjct.mysql.proto.Row;

import cn.batchfile.elasticsql.schema.SchemaManager;

public class StatementExecutor {
	private static final Logger logger = Logger.getLogger("StatementExecutor");
	private SchemaManager schemaManager;
	private List<String> hosts = new ArrayList<String>();

	public void setSchemaManager(SchemaManager schemaManager) {
		this.schemaManager = schemaManager;
	}
	
	public void setHosts(String hosts) {
		String[] ary = hosts.split(",");
		for (String s : ary) {
			if (!s.trim().isEmpty()) {
				this.hosts.add(s.trim());
			}
		}
	}
	
	public ResultSet execute(String sql) throws SQLException {
		if (StringUtils.containsIgnoreCase(sql, "SHOW VARIABLES")
				|| StringUtils.containsIgnoreCase(sql, "SELECT @@session.auto_increment_increment")
				|| StringUtils.containsIgnoreCase(sql, "show databases")
				|| StringUtils.containsIgnoreCase(sql, "SELECT database()")
				|| StringUtils.containsIgnoreCase(sql, "FROM INFORMATION_SCHEMA.STATISTICS.")
				|| StringUtils.containsIgnoreCase(sql, "from mysql.")) {
			
			return schemaManager.query(sql);
		} else if (StringUtils.equalsIgnoreCase(sql, "SELECT * FROM persons")) {
            ResultSet rs = new ResultSet();
            
            Column col = new Column("Fake Data");
            rs.addColumn(col);
            
            Column col2 = new Column("Fake Date");
            rs.addColumn(col2);
            
            Row row = new Row();
            row.addData("1");
            row.addData(new Date().getTime());
            
            rs.addRow(row); 
            return rs;
    	}
		return null;
	}
}
