package com.x.response;

/**
 * 统一数据返回模型
 *
 * @author hzh 2018/7/4 下午10:01
 */
public class Response {

	private int code;
	private String msg;
	private Object data;
	private String transId;
	private String requestIp;
	private String requestUrl;
	private String transTime;

	public Response() {
		this.code = ResponseCode.OK.getCode();
		this.msg = ResponseCode.OK.getMsg();
	}

	public Response(Object data) {
		this.code = ResponseCode.OK.getCode();
		this.msg = ResponseCode.OK.getMsg();
		this.data = data;
	}

	private Response(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static Response ok() {
		return new Response();
	}

	public static Response ok(Object data) {
		return new Response(data);
	}

	public static Response error(String errMsg) {
		return error(ResponseCode.BUSINESS_ERROR, errMsg);
	}

	public static Response error(ResponseCode responseCode) {
		return new Response(responseCode.getCode(), responseCode.getMsg());
	}

	public static Response error(ResponseCode responseCode, String errMsg) {
		return new Response(responseCode.getCode(), errMsg);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}
}
