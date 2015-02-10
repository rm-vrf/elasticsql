package cn.batchfile.elasticsql.server;

import java.io.IOException;
import java.sql.SQLFeatureNotSupportedException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.nlpcn.es4sql.exception.SqlParseException;

import com.github.mpjct.jmpjct.JMP;

import cn.batchfile.elasticsql.elasticsearch.StatementExecutor;

public class ExplainServlet implements Servlet {

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
		try {
			SearchRequestBuilder query = statementExecutor.explain(sql);
			response.setContentType("application/json");
			response.getWriter().print(query.toString());
		} catch (SQLFeatureNotSupportedException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (SqlParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public String getServletInfo() {
		return null;
	}

	public void destroy() {
		statementExecutor = null;
	}

}
