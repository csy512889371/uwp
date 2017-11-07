package rongji.framework.base;

/**
 * 
 * <p>Title:      </p>
 * <p>Description:  DAO异常类    </p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: RongJi</p>
 * 
 * @author Anna 
 * @create in 2009-2-23
 * @version 2.0
 */
public class DaoException extends RuntimeException {

	
	public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
