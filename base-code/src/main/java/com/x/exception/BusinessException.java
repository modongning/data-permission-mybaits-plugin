package com.x.exception;

/**
 * 业务异常
 * @author modongning
 */
public class BusinessException extends RuntimeException{
	private static final long serialVersionUID = -7546598998309530025L;

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(String message) {
		super(message);
	}
	
	
}	
