package com.x.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

public class LocalHttpClient {
	private static Logger log = LoggerFactory.getLogger(LocalHttpClient.class);
	
	private static LocalHttpClient localHttpClient = null;
	private HttpClient httpClient;

	private static int maxTotal = 200;
	private static String keystoryPath;
	private static String keystoryPassword;

	private LocalHttpClient() {
		
	};

	public void init(int maxTotal, String keystoryPath, String keystoryPassword) {
		LocalHttpClient.maxTotal = maxTotal;
		LocalHttpClient.keystoryPath = keystoryPath;
		LocalHttpClient.keystoryPassword = keystoryPassword;
	}

	public static LocalHttpClient getInstance() {
		if (null != localHttpClient) {
			return localHttpClient;
		}
		localHttpClient = new LocalHttpClient();
		if (null != keystoryPath && "".equals(keystoryPath)) {
			localHttpClient.httpClient = HttpClientFactory.createHttpClient(keystoryPath, keystoryPassword);
		} else if (maxTotal > 0) {
			localHttpClient.httpClient = HttpClientFactory.createHttpClient(maxTotal);
		} else {
			localHttpClient.httpClient = HttpClientFactory.createHttpClient();
		}
		return localHttpClient;
	}

	public <T> T execute(HttpUriRequest request,ResponseHandler<T> responseHandler){
		try {
			return httpClient.execute(request, responseHandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public HttpResponse execute(HttpUriRequest request) {
		try {
			return httpClient.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public HttpClient getHttpClient() {
		return httpClient;
	}
	
	public String exe(HttpUriRequest request) {
		HttpResponse response = null;
		try {
			response = httpClient.execute(request);
			int status = response.getStatusLine().getStatusCode();
			if (status != 200) {
				release(response);
				return null;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				return EntityUtils.toString(entity);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				release(response);
			}
		}
		return null;
	}

	public String exe(HttpPost httppost) {
		HttpResponse response = null;
		try {
			response = httpClient.execute(httppost, new BasicHttpContext());
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				return EntityUtils.toString(entity);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				release(response);
			}
		}
		return null;
	}

	private void release(HttpResponse response) {
		try {
			// 自动释放连接
			EntityUtils.consume(response.getEntity());
		} catch (IOException e) {
			log.error("-------释放链接错误:" + new Date() + "------");
			e.printStackTrace();
		}
	}
}
