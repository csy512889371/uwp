package rongji.cmis.service.system.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import rongji.cmis.dao.system.RoleDeptDao;
import rongji.cmis.model.ums.RoleDeptReal;
import rongji.cmis.service.system.RoleDeptService;
import rongji.framework.base.service.impl.BaseServiceImpl;

@Service("roleDeptServiceImpl")
public class RoleDeptServiceImpl extends BaseServiceImpl<RoleDeptReal> implements RoleDeptService {

	@Resource(name = "roleDeptDaoImpl")
	RoleDeptDao roleDeptDao;

	@Override
	public void deleteRoleDeptByRoleId(String roleId) {
		roleDeptDao.deleteRoleDeptByRoleId(roleId);

	}

	@Override
	public String[] findDeptByRoleIds(Integer[] roleIdArr) {
		return roleDeptDao.findDeptByRoleIds(roleIdArr);
	}

	@Override
	public String[] findDeptByRoleId(Integer roleId) {
		return roleDeptDao.findDeptByRoleId(roleId);
	}

}
