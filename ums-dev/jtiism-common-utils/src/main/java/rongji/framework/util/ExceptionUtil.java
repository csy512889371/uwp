package rongji.framework.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <p>Title:      </p>
 * <p>Description:      </p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: RongJi</p>
 * 
 * @author Anna 
 * @create in 2013-7-24
 * @version 3.0 
 */
public class ExceptionUtil {

	/**
	 * 返回错误信息字符串
	 * @param ex
	 * @return
	 */
	public static String getExceptionMessage(Exception ex) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		return sw.toString();
	}
	
}
