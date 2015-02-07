package cn.batchfile.elasticsql.util;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import net.sf.json.JSONObject;

public class Rest {
	
	public JSONObject get(String url) {
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				throw new RuntimeException(String.format("Error when get url: %s", url));
			}
			String json = getMethod.getResponseBodyAsString();
			return JSONObject.fromObject(json);
		} catch (HttpException e) {
			throw new RuntimeException(String.format("Error when get url: %s", url), e);
		} catch (IOException e) {
			throw new RuntimeException(String.format("Error when get url: %s", url), e);
		} finally {
			getMethod.releaseConnection();
		}
	}
	
	public JSONObject put(String url, JSONObject data) {
		throw new RuntimeException("TODO");
	}
	
	public JSONObject delete(String url) {
		throw new RuntimeException("TODO");
	}
	
	public JSONObject post(String url, JSONObject data) {
		throw new RuntimeException("TODO");
	}
}
