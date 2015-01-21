package cn.batchfile.elasticsql;

import java.io.IOException;

import com.github.mpjct.jmpjct.JMP;
import com.github.mpjct.jmpjct.JMP_Thread;

public class Main {
    public static void main(String[] args) throws IOException {
    	
    	JMP.config.put("ports", "3306");
    	//JMP.config.put("logConf", "conf/log.conf");
    	JMP.config.put("plugins", "com.github.mpjct.jmpjct.plugin.proxy.Proxy, cn.batchfile.elasticsql.plugin.ElasticsearchProxy, com.github.mpjct.jmpjct.plugin.debug.Debug");
    	JMP.config.put("proxyHosts", "3306:127.0.0.1:5050");
    	JMP.config.put("ehcacheConf", "conf/ehcache.xml");
    	JMP.config.put("ehcacheCacheName", "MySQL");
    	
    	JMP.config.put("schemaUrl", "jdbc:mysql://127.0.0.1:5050/elasticsql_schema?useUnicode=true&characterEncoding=utf-8");
    	JMP.config.put("schemaUsername", "root");
    	JMP.config.put("schemaPassword", "bitnami");
    	
    	JMP.config.put("elasticsearchHosts", "127.0.0.1:9200,localhost:9200");
        
        //Logger logger = Logger.getLogger("JMP");
        //PropertyConfigurator.configure(JMP.config.getProperty("logConf").trim());
        
        String[] ports = JMP.config.getProperty("ports").split(",");
        for (String port: ports) {
            new JMP_Thread(Integer.parseInt(port.trim())).run();
        }
    }

}
