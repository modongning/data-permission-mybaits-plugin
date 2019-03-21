package com.x.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author modongning
 * @updateBy modongning
 * @updateBy 2018/7/7 下午5:45
 */
public class HttpUtils {

	public static HttpServletRequest getHttpServletRequest() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if(null == requestAttributes || null == requestAttributes.getRequest()){
			return null;
		}
		HttpServletRequest request = requestAttributes.getRequest();
		return request;
	}

	public static HttpSession getHttpSession(){
		HttpServletRequest httpServletRequest = getHttpServletRequest();
		if(null == httpServletRequest)
			return null;

		HttpSession session = httpServletRequest.getSession();
		return session;
	}

	public static String getRequestUrl(HttpServletRequest request){
		if(null == request)
			return null;
		return request.getRequestURL().toString();
	}

	/**
	 * @param request
	 * @return java.lang.String
	 * @author modongning
	 * @updateBy modongning
	 * @updateDate 2018/7/7 下午5:46
	 * 获取请求IP
	 */
	public static String getRequestIp(HttpServletRequest request) {
		if(null == request)
			return null;
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
			// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}
}
