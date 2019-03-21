package com.x.base;

/**
 * @Author modongning
 * @updateBy modongning
 * @updateBy 2018/7/7 下午5:55
 * 常量池
 */
public class GlobalCode {
	//请求信息线程变量存储的KEY名称
	public final static String REQUEST_INFO_THREAD_LOCAL_KEY = "RequestInfo";

	//密码加密次数
	public final static int PASSWORD_HASH_TIMES = 3;

	//可用、有效
	public final static int VALID = 1;
	//不可用、无效
	public final static int INVALID = 0;
}
