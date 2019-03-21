package com.x.exception;

/**
 * 接口参数异常
 */
public class AbnormalParameterException extends RuntimeException {

	private static final long serialVersionUID = 7184460302113472755L;

	/**
	 * Constructs a new runtime exception with the specified detail message.
	 * The cause is not initialized, and may subsequently be initialized by a
	 * call to {@link #initCause}.
	 *
	 * @param message the detail message. The detail message is saved for
	 *                later retrieval by the {@link #getMessage()} method.
	 */
	public AbnormalParameterException(String message) {
		super(message);
	}
}
