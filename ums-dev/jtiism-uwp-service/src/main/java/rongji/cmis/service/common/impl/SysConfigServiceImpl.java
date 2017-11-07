package rongji.cmis.service.common.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import rongji.cmis.dao.common.SysConfigDao;
import rongji.cmis.model.sys.SysConfig;
import rongji.cmis.service.common.SysConfigService;
import rongji.framework.base.pojo.Setting;
import rongji.framework.base.service.impl.BaseServiceImpl;

@Service("sysConfigServiceImpl")
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfig> implements SysConfigService {
	
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(SysConfigServiceImpl.class);
	
	@Resource(name="sysConfigDaoImpl")
	SysConfigDao sysConfigDao;
	
	@Resource(name="sysConfigServiceImpl")
	SysConfigService sysConfigService;

	@Override
	public void saveSysConfigs(List<SysConfig> configs) {
		for(SysConfig config:configs){
			List<SysConfig> oldConfigs = sysConfigDao.findByProperty("name", config.getName());
			if(!oldConfigs.isEmpty()){
				SysConfig oldConfig=oldConfigs.get(0);
				oldConfig.setValue(config.getValue());
				sysConfigDao.merge(oldConfig);
			}
		}
	}

	/**
	 * 更新系统参数中指定设置项
	 *
	 * @param setting
	 */
	@Override
	public void saveSysConfigsBySetting(Setting setting) {
		
		List<SysConfig> sysConfigList = sysConfigService.findAll();
		
		for (SysConfig sysConfig : sysConfigList) {
			String configName = sysConfig.getName();
			if (StringUtils.isEmpty(configName)) {
				continue;
			}
			
			try {
				Object val = PropertyUtils.getProperty(setting, configName);
				if (val == null) {
					continue;
				}
				sysConfig.setValue(val.toString());
				sysConfigDao.save(sysConfig);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		
	}

}