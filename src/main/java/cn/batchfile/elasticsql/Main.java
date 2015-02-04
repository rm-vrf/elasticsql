package cn.batchfile.elasticsql;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.github.mpjct.jmpjct.JMP;
import com.github.mpjct.jmpjct.JMP_Thread;

public class Main {
    public static void main(String[] args) throws IOException {
    	
    	//JMP.config.put("logConf", "conf/log.conf");
    	//JMP.config.put("plugins", "com.github.mpjct.jmpjct.plugin.proxy.Proxy, cn.batchfile.elasticsql.plugin.Dump");
    	//JMP.config.put("proxyHosts", "3306:127.0.0.1:5050");
    	//JMP.config.put("ehcacheConf", "conf/ehcache.xml");
    	//JMP.config.put("ehcacheCacheName", "MySQL");
    	
    	//JMP.config.put("schemaUrl", "jdbc:mysql://127.0.0.1:5050/elasticsql_schema?useUnicode=true&characterEncoding=utf-8");
    	//JMP.config.put("schemaUsername", "root");
    	//JMP.config.put("schemaPassword", "bitnami");
    	
        
        //Logger logger = Logger.getLogger("JMP");
        //PropertyConfigurator.configure(JMP.config.getProperty("logConf").trim());
        
    	//JMP.config.put("plugins", "cn.batchfile.elasticsql.plugin.ElasticsearchProxy, cn.batchfile.elasticsql.plugin.Dump");
    	JMP.config.put("plugins", "cn.batchfile.elasticsql.plugin.ElasticsearchProxy");
    	
    	JMP.config.put("ports", "3306");
    	JMP.config.put("cluster_name", "lu_20150202");
    	JMP.config.put("transport_address", "localhost:9300");
    	JMP.config.put("http_address", "localhost:9200");
    	
        String[] ports = JMP.config.getProperty("ports").split(",");
        for (String port: ports) {
        	if (StringUtils.isBlank(port)) {
        		continue;
        	}
            new JMP_Thread(Integer.parseInt(port.trim())).run();
        }
    }
}
