package rongji.cmis.service.common;

import java.util.List;

import rongji.framework.base.model.LogConfig;


/**
 * Service - 日志配置
 * 
 * @version 1.0
 */
public interface LogConfigService {

	/**
	 * 获取所有日志配置
	 * 
	 * @return 所有日志配置
	 */
	List<LogConfig> getAll();

}