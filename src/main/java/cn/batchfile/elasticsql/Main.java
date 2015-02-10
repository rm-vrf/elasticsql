package cn.batchfile.elasticsql;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.PropertyConfigurator;

import cn.batchfile.elasticsql.server.HttpServer;

import com.github.mpjct.jmpjct.JMP;
import com.github.mpjct.jmpjct.JMP_Thread;

public class Main {
    public static void main(String[] args) throws NumberFormatException, Exception {
    	
    	// 从配置文件中加载设置
    	loadConfig(JMP.config);
    	
    	// 从命令行中加载设置
    	loadArguments(JMP.config, args);
    	
    	// 设置插件
    	JMP.config.put("plugins", "cn.batchfile.elasticsql.plugin.ElasticsearchProxy");
    	
    	// 加载日志设置
    	if (JMP.config.containsKey("log.conf")) {
    		PropertyConfigurator.configure(JMP.config.getProperty("log.conf").trim());
    	}
    	
    	// 启动socket监听端口，提供mysql服务
        String[] ports = JMP.config.getProperty("socket.port").split(",");
        for (String port: ports) {
        	if (StringUtils.isBlank(port)) {
        		continue;
        	}
        	new Thread(new JMP_Thread(Integer.parseInt(port.trim()))).start();
        }
        
        // 启动web服务
        new HttpServer().start();
    }
    
    private static void loadArguments(Properties properties, String[] args) {
    	for (String arg : args) {
    		if (StringUtils.contains(arg, "=") && StringUtils.startsWith(arg, "--")) {
    			String name = StringUtils.substringBetween(arg, "--", "=");
    			String value = StringUtils.substringAfterLast(arg, "=");
    			properties.put(name, value);
    		}
    	}
	}

	private static void loadConfig(Properties properties) throws IOException {
    	FileInputStream config = null;
    	try {
	    	File file = new File("conf/elasticsql.properties");
	    	if (file.exists()) {
	        	config = new FileInputStream(file);
	        	properties.load(config);
	    	}
    	} finally {
    		IOUtils.closeQuietly(config);
    	}
    }
}
