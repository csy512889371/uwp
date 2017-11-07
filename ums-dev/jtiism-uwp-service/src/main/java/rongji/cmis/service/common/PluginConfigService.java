package rongji.cmis.service.common;

import rongji.cmis.model.ums.CfgUmsPluginConfig;
import rongji.framework.base.service.BaseService;

/**
 * Service - 插件配置
 * 
 * @version 1.0
 */
public interface PluginConfigService extends BaseService<CfgUmsPluginConfig> {

	/**
	 * 判断插件ID是否存在
	 * 
	 * @param pluginId
	 *            插件ID
	 * @return 插件ID是否存在
	 */
	boolean pluginIdExists(String pluginId);

	/**
	 * 根据插件ID查找插件配置
	 * 
	 * @param pluginId
	 *            插件ID
	 * @return 插件配置，若不存在则返回null
	 */
	CfgUmsPluginConfig findByPluginId(String pluginId);

}