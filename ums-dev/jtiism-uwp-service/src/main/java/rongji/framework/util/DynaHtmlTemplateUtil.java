package rongji.framework.util;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import rongji.framework.base.dao.dynaHtml.DefaultDynaHtmlStatementBuilder;
import rongji.framework.base.dao.dynaHtml.StatementHtmlTemplate;
import rongji.framework.base.exception.ApplicationException;

import javax.annotation.Resource;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Component("dynaHtmlTemplateUtil")
public class DynaHtmlTemplateUtil implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(DynaHtmlTemplateUtil.class);
    /**
     * 模板缓存
     */
    protected Map<String, StatementHtmlTemplate> templateCache;

    @Resource(name = "htmlGenerateStatementBuilder")
    protected DefaultDynaHtmlStatementBuilder htmlGenerateStatementBuilder;

    @Resource(name = "freeMarkerConfigurer")
    private FreeMarkerConfigurer freeMarkerConfigurer;


    @Override
    public void afterPropertiesSet() throws Exception {
        templateCache = new HashMap();
        htmlGenerateStatementBuilder.init();
        Map<String, String> namedHtmlTemplate =  htmlGenerateStatementBuilder.getHtmlTemplates();

        StringTemplateLoader stringLoader = new StringTemplateLoader();
        for (Map.Entry<String, String> entry : namedHtmlTemplate.entrySet()) {
            stringLoader.putTemplate(entry.getKey(), entry.getValue());
            templateCache.put(entry.getKey(), new StatementHtmlTemplate(new Template(entry.getKey(), new StringReader(entry.getValue()), freeMarkerConfigurer.getConfiguration())));
        }

    }

    protected String processTemplate(StatementHtmlTemplate statementTemplate, Map<String, ?> parameters) {
        StringWriter stringWriter = new StringWriter();
        try {
            statementTemplate.getTemplate().process(parameters, stringWriter);
        } catch (Exception e) {
            logger.error("处理generate/html 查询参数模板时发生错误：{}", e.toString());
            throw new ApplicationException(e);
        }
        return stringWriter.toString();
    }

    public String findDynaHtmlByNamed(final String queryName, final Map<String, ?> parameters) {
        StatementHtmlTemplate statementTemplate = templateCache.get(queryName);
        String statement = processTemplate(statementTemplate, parameters);
        return statement;
    }


}
