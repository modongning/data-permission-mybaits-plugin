package com.x.utils;

import javax.servlet.http.HttpSession;

/**
 * @Author modongning
 * @updateBy modongning
 * @updateBy 2018/9/12 上午11:34
 */
public class SessionUtils {

	public static <T>T getAttr(String key){
		HttpSession httpSession = HttpUtils.getHttpSession();
		if(null == httpSession)
			return null;
		return (T)httpSession.getAttribute(key);
	}

	public static void setAttr(String key,Object value){
		HttpSession httpSession = HttpUtils.getHttpSession();
		if(null == httpSession)
			throw new IllegalStateException("没有session,不能设置attribute");

		httpSession.setAttribute(key,value);
	}
}
