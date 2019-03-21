package com.x.exception;

/**
 * 账号无效异常
 */
public class AccountInvalidException extends RuntimeException {

	private static final long serialVersionUID = 7285467635831994822L;

	/**
	 * Constructs a new runtime exception with the specified detail message.
	 * The cause is not initialized, and may subsequently be initialized by a
	 * call to {@link #initCause}.
	 *
	 * @param message the detail message. The detail message is saved for
	 *                later retrieval by the {@link #getMessage()} method.
	 */
	public AccountInvalidException(String message) {
		super(message);
	}
}
