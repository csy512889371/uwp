package rongji.cmis.dao.system;

import rongji.cmis.model.ums.CfgTopmenuUser;
import rongji.cmis.model.ums.CfgUmsUser;
import rongji.framework.base.dao.GenericDao;

import java.util.List;

public interface TopMenuDao extends GenericDao<CfgTopmenuUser> {

	 void deleteByUser(CfgUmsUser cfgumsuser);

	 List<CfgTopmenuUser> findMenuByUser(CfgUmsUser cfgUmsUser);
}
