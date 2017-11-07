package rongji.framework.base;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.util.StringUtils;

import rongji.framework.util.StringUtil;

/**
 * <p>Title:      </p>
 * <p>Description:      </p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: RongJi</p>
 * 
 * @author Anna 
 * @create in 2013-7-26
 * @version 3.0 
 */
public class ConvertEditor extends PropertyEditorSupport {
	private SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//19
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//8-10
	private SimpleDateFormat cstDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'UTC 0800' yyyy",Locale.US);
	
	/**
	 * 字符串转Date
	 */
	public void setAsText(String text) throws IllegalArgumentException {
		if (!StringUtil.isEmpty(text)) {
			text = text.trim();
			try {
				if (text.indexOf(":") == -1 && text.length()>= 8 &&text.length() <= 10) {
					setValue(this.dateFormat.parse(text));
					
				} else if (text.indexOf(":") > 0 && text.length() >= 14 && text.length() <= 19 ) {
					setValue(this.datetimeFormat.parse(text));
					
				} else if (text.indexOf(":") > 0 && text.length() == 21) {
					
					text = text.replace(".0", "");
					setValue(this.datetimeFormat.parse(text));
					
				} else if (text.indexOf(":") > 0 && text.indexOf("UTC") > 0){
					setValue(cstDateFormat.parse(text));
					
				} else {
					throw new IllegalArgumentException("Could not parse date: "+text);
				}
				
			} catch (ParseException ex) {
				IllegalArgumentException iae = new IllegalArgumentException("Could not parse date: " + ex.getMessage());
				iae.initCause(ex);
				throw iae;
			}
		} else {
			setValue(null);
		}
	}
}
