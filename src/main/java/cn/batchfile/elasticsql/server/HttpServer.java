package cn.batchfile.elasticsql.server;

import javax.servlet.Servlet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import com.github.mpjct.jmpjct.JMP;

public class HttpServer {

	private static final Logger logger = Logger.getLogger("HttpServer");
	private static String DEFAULT_WEBAPP = "src/main/webapp";
	
	public void start() throws Exception {
		int port = Integer.parseInt(JMP.config.getProperty("http.port"));
        String webapp = JMP.config.getProperty("webapp");
		
		Server server = new Server(port);
		WebAppContext context = new WebAppContext();
		context.setContextPath("/");
		context.setWar(StringUtils.isEmpty(webapp) ? DEFAULT_WEBAPP : webapp);
		context.setServer(server);
		
		HashLoginService loginService = new HashLoginService("ESQL-SECURITY-REALM");
		context.getSecurityHandler().setLoginService(loginService);
		
		//set sql executor
		Servlet sqlServlet = new SqlServlet();
		context.addServlet(new ServletHolder(sqlServlet), "/sql");
		
		Servlet explainServlet = new ExplainServlet();
		context.addServlet(new ServletHolder(explainServlet), "/explain");
		
		server.setHandler(context);
		server.start();

		logger.info(String.format("Http server started on %d", port));
	}
}
