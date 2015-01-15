package com.github.mpjct.jmpjct;

/*
 * Java Mysql Proxy
 * Main binary. Just listen for connections and pass them over
 * to the proxy module
 */

import java.io.IOException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.util.Properties;
import java.io.FileInputStream;

public class JMP {
    public static Properties config = new Properties();
    
    public static void main(String[] args) throws IOException {
        //FileInputStream config = new FileInputStream("conf/jmp.properties");
        //JMP.config.load(config);
        //config.close();
    	JMP.config.put("ports", "5050");
    	//JMP.config.put("logConf", "conf/log.conf");
    	JMP.config.put("plugins", "com.github.mpjct.jmpjct.plugin.proxy.Proxy");
    	JMP.config.put("proxyHosts", "5050:127.0.0.1:3306");
    	JMP.config.put("ehcacheConf", "conf/ehcache.xml");
    	JMP.config.put("ehcacheCacheName", "MySQL");
        
        Logger logger = Logger.getLogger("JMP");
        //PropertyConfigurator.configure(JMP.config.getProperty("logConf").trim());
        
        String[] ports = JMP.config.getProperty("ports").split(",");
        for (String port: ports) {
            new JMP_Thread(Integer.parseInt(port.trim())).run();
        }
    }
}
