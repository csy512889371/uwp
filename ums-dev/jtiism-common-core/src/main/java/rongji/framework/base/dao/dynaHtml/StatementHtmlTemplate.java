package rongji.framework.base.dao.dynaHtml;

import freemarker.template.Template;


public class StatementHtmlTemplate {


    public StatementHtmlTemplate() {
    }

    public StatementHtmlTemplate(Template template) {
        this.template = template;
    }


    private Template template;


    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

}
