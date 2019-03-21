package com.x.exception;

/**
 * session time out。<br>
 * 运行时异常，由Handler统一处理。<br>
 */
public class SessionTimeOutException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SessionTimeOutException(String message) {
		super(message);
	}
}
