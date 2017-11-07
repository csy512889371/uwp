package rongji.framework.base.exception;

import rongji.framework.util.SpringUtils;

/**
 * 应用异常,定义从该类派生的异常为应用异常,应用程序的所有异常应衍生自该类,需要明确捕获异常,并记录异常信息.
 * 该异常继承自RuntimeException.
 * 
 * @author chenshiying
 */
@SuppressWarnings("serial")
public class ApplicationException extends RuntimeException {

	private String message;

	private Object[] args;

	private Throwable cause;

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
		message = SpringUtils.getMessage(message);
		this.message = message;
		this.cause = cause;
	}

	public ApplicationException(String message) {
		super(SpringUtils.getMessage(message));

		message = SpringUtils.getMessage(message);
		this.message = message;
	}

	public ApplicationException(String message, Object[] args) {
		super(SpringUtils.getMessage(message, args));
		this.message = message;
		this.args = args;
	}

	public ApplicationException(Throwable cause) {
		super(cause);

		this.cause = cause;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public String getMessageInfo() {
		return SpringUtils.getMessage(message, args);
	}

	public Throwable getCause() {
		return cause;
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
	}

}
