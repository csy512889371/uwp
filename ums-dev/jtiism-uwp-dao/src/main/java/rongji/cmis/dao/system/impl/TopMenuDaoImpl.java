package rongji.cmis.dao.system.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import rongji.cmis.dao.system.TopMenuDao;
import rongji.cmis.model.ums.CfgTopmenuUser;
import rongji.cmis.model.ums.CfgUmsUser;
import rongji.framework.base.dao.impl.GenericDaoImpl;

@Repository
public class TopMenuDaoImpl extends GenericDaoImpl<CfgTopmenuUser> implements TopMenuDao {

	/**
	 * 根据用户删除CFG_TOPMENU_USER表中的数据
	 */
	@Override
	public void deleteByUser(CfgUmsUser cfgumsuser) {
		List<CfgTopmenuUser> topMenuList = this.findByProperty("USER_ID", cfgumsuser);
		for (CfgTopmenuUser cfgTopmenuUser : topMenuList) {
			this.delete(cfgTopmenuUser);
		}
	}

	@Override
	public List<CfgTopmenuUser> findMenuByUser(CfgUmsUser cfgUmsUser) {
		return this.findByProperty("USER_ID", cfgUmsUser);
	}

}
