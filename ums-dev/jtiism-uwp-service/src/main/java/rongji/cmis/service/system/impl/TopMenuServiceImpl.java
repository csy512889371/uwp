package rongji.cmis.service.system.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import rongji.cmis.dao.system.TopMenuDao;
import rongji.cmis.model.ums.CfgTopmenuUser;
import rongji.cmis.model.ums.CfgUmsUser;
import rongji.cmis.service.system.TopMenuService;
import rongji.framework.base.service.impl.BaseServiceImpl;

@Service("topMenuServiceImpl")
public class TopMenuServiceImpl extends BaseServiceImpl<CfgTopmenuUser> implements TopMenuService {

	@Resource(name = "topMenuDaoImpl")
	TopMenuDao topMenuDao;

	/**
	 * 根据当前用户删除CFG_TOPMENU_USER表中的数据
	 */
	@Override
	public void deleteByUser(CfgUmsUser cfgUmsUser) {
		topMenuDao.deleteByUser(cfgUmsUser);
	}

	/**
	 * 根据当前用户查找CFG_TOPMENU_USER表中的数据
	 */
	@Override
	public List<CfgTopmenuUser> findMenuByUser(CfgUmsUser cfgUmsUser) {
		return topMenuDao.findMenuByUser(cfgUmsUser);
	}
}
