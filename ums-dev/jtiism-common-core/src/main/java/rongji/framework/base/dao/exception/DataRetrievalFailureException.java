package rongji.framework.base.dao.exception;

public class DataRetrievalFailureException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public DataRetrievalFailureException(String message) {
        super(message);
    }

    public DataRetrievalFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
