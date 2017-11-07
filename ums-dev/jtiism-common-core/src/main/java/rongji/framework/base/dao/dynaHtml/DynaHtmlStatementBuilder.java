package rongji.framework.base.dao.dynaHtml;

import java.io.IOException;
import java.util.Map;

/**
 * 动态html/generate语句组装器
 *
 */
public interface DynaHtmlStatementBuilder {

	/**
	 * htmlTemplate
	 * 
	 * @return
	 */
	public Map<String, String> getHtmlTemplates();

	/**
	 * 初始化
	 * 
	 * @throws IOException
	 */
	public void init() throws IOException;
}