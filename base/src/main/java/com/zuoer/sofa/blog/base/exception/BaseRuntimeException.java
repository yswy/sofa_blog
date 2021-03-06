package com.zuoer.sofa.blog.base.exception;

import com.zuoer.sofa.blog.base.error.ErrorCode;

/**
 * @author chenbug
 * @version $Id: BaseRuntimeException.java,v 0.1 2009-5-21 上午01:15:32 chenbug Exp $
 */
public class BaseRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 75922815058256083L;
	
	protected ErrorCode errorCode;

	/**
	 * 空构造器。
	 */
	public BaseRuntimeException() {
		super();
	}

	public BaseRuntimeException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public BaseRuntimeException(ErrorCode errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public BaseRuntimeException(ErrorCode errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}

	public BaseRuntimeException(ErrorCode errorCode, String message, Throwable throwable) {
		super(message, throwable);
		this.errorCode = errorCode;
	}

	public <E extends BaseRuntimeException> BaseRuntimeException(E exception) {
		super(exception);
		this.errorCode = exception.getErrorCode();
	}

	public <E extends BaseRuntimeException> BaseRuntimeException(String message, E exception) {
		super(message, exception);
		this.errorCode = exception.getErrorCode();
	}

	/**
	 * @see java.lang.Throwable#toString()
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("errorCode=").append(errorCode).append(",").append(super.toString());
		return buf.toString();
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}


}
