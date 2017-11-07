package rongji.framework.web.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rongji.cmis.model.sys.SysCoder;
import rongji.cmis.service.common.CoderService;
import rongji.framework.util.SpringUtils;

public class OutSelectOptionByCodeTag extends SimpleTagSupport {

	private static final Logger logger = LoggerFactory.getLogger(OutSelectOptionByCodeTag.class);

	private String tabName;// 原表名

	private String colName; // 字段名

	private String value;// 默认值

	public void doTag() throws JspException, IOException {
		PageContext ctx = (PageContext) getJspContext();
		JspWriter out = ctx.getOut();
		if (StringUtils.isBlank(this.colName)) {
			out.print("");
		} else {
			CoderService coderService = SpringUtils.getBean("coderServiceImpl", CoderService.class);
			List<SysCoder> coderList = coderService.findListByFieldInfo(tabName, colName);
			if (coderList == null || coderList.isEmpty()) {
				out.print("");
				return;
			}

			logger.info("the size of codeValueList" + coderList.size());
            for (SysCoder coder : coderList) {
                String val = coder.getVal();
                String retStr = "<option value=\"" + coder.getVal() + "\"";
                if (!StringUtils.isEmpty(val) && val.equals(this.getValue())) {
                    retStr += " selected=\"selected\"";
                }
                retStr += ">" + coder.getValDesc() + "</option>";
                out.print(retStr);
            }
		}
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

}