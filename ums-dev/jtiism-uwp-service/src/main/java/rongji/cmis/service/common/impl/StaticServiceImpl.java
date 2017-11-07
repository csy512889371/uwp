package rongji.cmis.service.common.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import rongji.cmis.model.ums.CfgUmsCodeAttribute;
import rongji.cmis.service.common.StaticService;
import rongji.cmis.service.common.TemplateService;
import rongji.cmis.service.system.CfgUmsCodeAttributeService;
import rongji.framework.base.pojo.Setting;
import rongji.framework.base.pojo.Template;
import rongji.framework.util.CreateAndWriteFile;
import rongji.framework.util.SettingUtils;

/**
 * Service - 静态化
 *
 * @version 3.0
 */
@Service("staticServiceImpl")
public class StaticServiceImpl implements StaticService, ServletContextAware {

    private static final Logger logger = LoggerFactory.getLogger(StaticServiceImpl.class);

    /**
     * servletContext
     */
    private ServletContext servletContext;

    @Resource(name = "freeMarkerConfigurer")
    private FreeMarkerConfigurer freeMarkerConfigurer;


    @Resource(name = "templateServiceImpl")
    private TemplateService templateService;

    @Resource(name = "cfgUmsCodeAttributeServiceImpl")
    private CfgUmsCodeAttributeService cfgUmsCodeAttributeService;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Transactional(readOnly = true)
    public int build(String templatePath, String staticPath, Map<String, Object> model) {
        Assert.hasText(templatePath);
        Assert.hasText(staticPath);

        FileOutputStream fileOutputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        Writer writer = null;

        try {
            freemarker.template.Template template = freeMarkerConfigurer.getConfiguration().getTemplate(templatePath);
            File staticFile = new File(servletContext.getRealPath(staticPath));
            File staticDirectory = staticFile.getParentFile();
            if (!staticDirectory.exists()) {
                staticDirectory.mkdirs();
            }
            fileOutputStream = new FileOutputStream(staticFile);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
            writer = new BufferedWriter(outputStreamWriter);
            template.process(model, writer);
            writer.flush();
            return 1;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(outputStreamWriter);
            IOUtils.closeQuietly(fileOutputStream);
        }
        return 0;
    }

    @Transactional(readOnly = true)
    public int build(String templatePath, String staticPath) {
        return build(templatePath, staticPath, null);
    }

    @Transactional(readOnly = true)
    public int buildOther() {
        int buildCount = 0;
        Template adminCommonJsTemplate = templateService.get("adminCommonJs");
        buildCount += build(adminCommonJsTemplate.getTemplatePath(), adminCommonJsTemplate.getStaticPath());
        return buildCount;
    }

    @Transactional(readOnly = true)
    public int buildDynaJs() {
        int buildCount = 0;
        try {
            List<CfgUmsCodeAttribute> cfgUmsCodeAttributeList = cfgUmsCodeAttributeService.findScriptNoNull();
            Setting setting = SettingUtils.get();
            String realPath = servletContext.getRealPath(setting.getDynajsPath());
            for (CfgUmsCodeAttribute cfgUmsCodeAttribute : cfgUmsCodeAttributeList) {
                String fileName = "dynajs_" + cfgUmsCodeAttribute.getAttrCode().toLowerCase();
                CreateAndWriteFile.createFile(fileName, cfgUmsCodeAttribute.getScript(), realPath, "js");
                buildCount++;
            }
            return buildCount;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return 0;
    }

    @Transactional(readOnly = true)
    public int buildAll() {
        int buildCount = 0;
        buildOther();
        return buildCount;
    }

    @Transactional(readOnly = true)
    public int delete(String staticPath) {
        Assert.hasText(staticPath);

        File staticFile = new File(servletContext.getRealPath(staticPath));
        if (staticFile.exists()) {
            staticFile.delete();
            return 1;
        }
        return 0;
    }

    @Transactional(readOnly = true)
    public int deleteOther() {
        int deleteCount = 0;
        Template shopCommonJsTemplate = templateService.get("shopCommonJs");
        Template adminCommonJsTemplate = templateService.get("adminCommonJs");
        deleteCount += delete(shopCommonJsTemplate.getStaticPath());
        deleteCount += delete(adminCommonJsTemplate.getStaticPath());
        return deleteCount;
    }

}