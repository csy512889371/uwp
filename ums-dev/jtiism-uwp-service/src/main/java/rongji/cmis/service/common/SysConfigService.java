package rongji.cmis.service.common;

import java.util.List;

import rongji.cmis.model.sys.SysConfig;
import rongji.framework.base.pojo.Setting;
import rongji.framework.base.service.BaseService;


public interface SysConfigService extends BaseService<SysConfig> {

	void saveSysConfigs(List<SysConfig> configs);

	void saveSysConfigsBySetting(Setting setting);
}