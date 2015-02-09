package cn.batchfile.elasticsql.server;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class HttpServer {

	private static final Logger logger = Logger.getLogger("HttpServer");
	private static String DEFAULT_WEBAPP = "src/main/webapp";
	
	public void start(int port, String webapp) throws Exception {
		Server server = new Server(port);
		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setContextPath("/");
		webAppContext.setWar(StringUtils.isEmpty(webapp) ? DEFAULT_WEBAPP : webapp);
		webAppContext.setServer(server);
		
		HashLoginService loginService = new HashLoginService("ESQL-SECURITY-REALM");
		webAppContext.getSecurityHandler().setLoginService(loginService);
		
		server.setHandler(webAppContext);
		server.start();

		logger.info(String.format("Http server started on %d", port));
	}
}
