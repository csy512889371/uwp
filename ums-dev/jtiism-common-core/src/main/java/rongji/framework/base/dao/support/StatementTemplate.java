package rongji.framework.base.dao.support;

import freemarker.template.Template;


public class StatementTemplate {

	public enum TYPE {

		/** hql 查询 */
		HQL,

		/** sql 查询 */
		SQL
	}

	public StatementTemplate() {
	}

	public StatementTemplate(TYPE type, Template template) {
		this.type = type;
		this.template = template;
	}

	private TYPE type;

	private Template template;

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

}
