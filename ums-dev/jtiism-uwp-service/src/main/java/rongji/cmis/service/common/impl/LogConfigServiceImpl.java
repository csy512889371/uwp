package rongji.cmis.service.common.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import rongji.cmis.service.common.LogConfigService;
import rongji.framework.base.CommonAttributes;
import rongji.framework.base.model.LogConfig;

/**
 * Service - 日志配置
 * 
 * @version 1.0
 */
@Service("logConfigServiceImpl")
public class LogConfigServiceImpl implements LogConfigService {
	
	private static final Logger logger = LoggerFactory.getLogger(LogConfigServiceImpl.class);

	@SuppressWarnings("unchecked")
	@Cacheable("logConfig")
	public List<LogConfig> getAll() {
		try {
			File cmisXmlFile = new ClassPathResource(CommonAttributes.CMIS_XML_PATH).getFile();
			Document document = new SAXReader().read(cmisXmlFile);
			List<Element> elements = document.selectNodes("/cmis/logConfig");
			List<LogConfig> logConfigs = new ArrayList<LogConfig>();
			for (Element element : elements) {
				String operateModule = element.attributeValue("operateModule");
				String operateSet = element.attributeValue("operateSet");
				LogConfig logConfig = new LogConfig();
				logConfig.setOperateModule(operateModule);
				logConfig.setOperateSet(operateSet);
				logConfigs.add(logConfig);
			}
			return logConfigs;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

}