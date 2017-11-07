package rongji.cmis.service.common.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rongji.cmis.dao.common.PluginConfigDao;
import rongji.cmis.model.ums.CfgUmsPluginConfig;
import rongji.cmis.service.common.PluginConfigService;
import rongji.framework.base.service.impl.BaseServiceImpl;

/**
 * Service - 插件配置
 * 
 * @version 1.0
 */
@Service("pluginConfigServiceImpl")
public class PluginConfigServiceImpl extends BaseServiceImpl<CfgUmsPluginConfig> implements PluginConfigService {

	@Resource(name = "pluginConfigDaoImpl")
	private PluginConfigDao pluginConfigDao;

	@Transactional(readOnly = true)
	public boolean pluginIdExists(String pluginId) {
		return pluginConfigDao.pluginIdExists(pluginId);
	}

	@Transactional(readOnly = true)
	public CfgUmsPluginConfig findByPluginId(String pluginId) {
		return pluginConfigDao.findByPluginId(pluginId);
	}

}