package rongji.framework.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class InforItemAuthTag extends BodyTagSupport {
	private static final long serialVersionUID = -1385010302157701800L;
	/** 
     *  
     */
	private int maxLength; // 限定的最长字数
	private String suffix; // 后缀
	private String value; // 要处理的值

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int doAfterBody() throws JspException {
		if (null == value) {
			value = bodyContent.getString();
		}

		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
		if (null == value) {
			value = "";
		}
		// JspWriter out = super.getBodyContent().getEnclosingWriter();
		JspWriter out = pageContext.getOut();
		try {
			if (value.length() > maxLength) {
				value = value.substring(0, maxLength);
				if (suffix != null && !"".equals(suffix)) {
					value += suffix;
				}
			}
			out.print(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
}
