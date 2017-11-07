package rongji.framework.base;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * <p>Title:      </p>
 * <p>Description:      </p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: RongJi</p>
 * 
 * @author Anna 
 * @create in 2013-8-15
 * @version 3.0 
 */
public class ContextBeanFactory{

	
	/**
	 * 根据Bean名称获取对象实例
	 * @param event
	 * @param beanName
	 * @return
	 */
	public static Object getApplicationContextBean(ServletContextEvent event,String beanName){
		
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		
		return webApplicationContext.getBean(beanName);
		
	}
	
	
	
}
