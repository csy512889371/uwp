package rongji.framework.web.init;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import rongji.cmis.service.common.StaticService;
import rongji.framework.base.CommonAttributes;
import rongji.framework.base.ContextBeanFactory;
import rongji.framework.base.pojo.Setting;
import rongji.framework.util.ClassUtils;
import rongji.framework.util.FileUtil;
import rongji.framework.util.SettingUtils;
import rongji.framework.util.SpringUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title:   监听器   </p>
 * <p>Company: RongJi</p>
 *
 * @version 3.0
 */
public class InitListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(InitListener.class);


    public void contextDestroyed(ServletContextEvent event) {

        logger.info("clear system start >>>>>");

        logger.info("<<<<< clear system end");
    }

    public void contextInitialized(ServletContextEvent event) {

        logger.info("InitListener start >>>>>");
        logger.info(" 这里是监听器 ");

        try {
            ClassUtils.initClassProperty("rongji");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        TaskExecutor taskExecutor = (TaskExecutor) ContextBeanFactory.getApplicationContextBean(event, "taskExecutor");
        String pageSize = event.getServletContext().getInitParameter("pageSize");

        CommonAttributes.DEFAULT_PAGE_LIST = event.getServletContext().getInitParameter("pageList");
        CommonAttributes.DEFAULT_PAGE_SIZE = pageSize;

        taskExecutor.execute(() -> {
            /*FieldInfoService fieldInfoService = (FieldInfoService)SpringUtils.getBean("fieldInfoServiceImpl");
            DataDictService dataDictService = (DataDictService)SpringUtils.getBean("dataDictServiceImpl");
            List<String> codeTables = fieldInfoService.findCodeTableDist();
            String message = dataDictService.refreshCaches(codeTables);
            logger.info("below codeIndex can't cache into redis:" + message);*/

            //根据系统参数设置中的路径，生成文件夹
            Setting settingBean = SettingUtils.get();
            String resourcePath = settingBean.getResourcePath();
            List<String> proList =new ArrayList<String>();
            proList.add("licensePicPath");
            proList.add("licenseThumbPath");
            proList.add("publicPicPath");
            proList.add("publicThumbPath");
            proList.add("dynaReportTempDoc");
            proList.add("fileUploadPath");
            proList.add("licensePicPath");
            proList.add("dynaImgPath");
            proList.add("dynaThumbPath");
            for(String proName : proList){
                try {
                    String _path = (String) PropertyUtils.getProperty(settingBean, proName);
                    FileUtil.mkDir(resourcePath, _path);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

            }

            StaticService staticService = (StaticService) SpringUtils.getBean("staticServiceImpl");
            staticService.buildOther();
            staticService.buildDynaJs();

            ///T
        });

        logger.info(" <<<<< InitListener end");
    }

}
