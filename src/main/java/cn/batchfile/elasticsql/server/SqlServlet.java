package cn.batchfile.elasticsql.server;

import java.io.IOException;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.github.mpjct.jmpjct.JMP;
import com.github.mpjct.jmpjct.mysql.proto.Column;
import com.github.mpjct.jmpjct.mysql.proto.Row;

import cn.batchfile.elasticsql.elasticsearch.Result;
import cn.batchfile.elasticsql.elasticsearch.StatementExecutor;
import cn.batchfile.elasticsql.exceptions.ExecuteException;

public class SqlServlet implements Servlet {
	
	private StatementExecutor statementExecutor;

	public void init(ServletConfig config) throws ServletException {
		statementExecutor = new StatementExecutor();
		String http_address = JMP.config.getProperty("elasticsearch.http");
		String transport_address = JMP.config.getProperty("elasticsearch.transport");
		statementExecutor.connect(http_address, transport_address);
	}

	public ServletConfig getServletConfig() {
		return null;
	}

	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		String sql = request.getParameter("sql");
		
		JSONObject r = new JSONObject();
		r.put("resultSet", null);
		r.put("ok", null);
		r.put("error", null);
		
		try {
			Result result = statementExecutor.execute(sql);
			if (result.resultSet != null) {
				JSONArray rs = new JSONArray();
				for (Row row : result.resultSet.rows) {
					JSONObject object = new JSONObject();
					List<Column> columns = result.resultSet.columns;
					for (int i = 0; i < columns.size(); i ++) {
						Column column = columns.get(i);
						object.put(column.name, get(row.data, i));
					}
					rs.add(object);
				}
				r.put("resultSet", rs);
			} else {
				JSONObject ok = new JSONObject();
				ok.put("affectedRows", result.affectedRows);
				ok.put("lastInsertId", result.lastInsertId);
				ok.put("warnings", result.warnings);
				r.put("ok", ok);
			}
		} catch (ExecuteException e) {
			JSONObject err = new JSONObject();
			err.put("errorCode", e.getCode());
			err.put("errorMessage", e.getMessage());
			err.put("sqlState", e.getSqlState());
			r.put("error", err);
		} catch (Exception e) {
			JSONObject err = new JSONObject();
			err.put("errorCode", 1500);
			err.put("errorMessage", e.getMessage());
			err.put("sqlState", "HY000");
			r.put("error", err);
		}
		
		responseJson(response, r);
	}
	
	private Object get(List<Object> list, int index) {
		return list.size() > index ? list.get(index) : null;
	}
	
	private void responseJson(ServletResponse response, JSON json) throws IOException {
		response.setContentType("application/json");
		response.getWriter().print(json.toString());
	}

	public String getServletInfo() {
		return null;
	}

	public void destroy() {
		statementExecutor = null;
	}

}
