package rongji.cmis.service.system;

import java.util.List;

import rongji.cmis.model.ums.CfgTopmenuUser;
import rongji.cmis.model.ums.CfgUmsUser;
import rongji.framework.base.service.BaseService;

public interface TopMenuService extends BaseService<CfgTopmenuUser> {

	public void deleteByUser(CfgUmsUser cfgUmsUser);

	public List<CfgTopmenuUser> findMenuByUser(CfgUmsUser cfgUmsUser);
}
