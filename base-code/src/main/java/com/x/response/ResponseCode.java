package com.x.response;

/**
 * @author hzh 2018/7/4 下午10:44
 */
public enum ResponseCode {
	OK(10001, "ok"),
	BUSINESS_ERROR(10002, "fail"),
	SESSION_TIME(10003, "登录超时"),
	PARAM_ERROR(20001, "参数异常"),
	AUTHENTICATION_ERROR(40001, "认证失败"),
	AUTHORIZATION_ERROR(40002, "授权失败"),
	ACCOUNT_OR_PASSWORD_ERROR(41001, "账号或密码错误"),
	UNKNOWN_ERROR(99999, "服务器异常")
	;

	private int code;
	private String msg;

	ResponseCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}
