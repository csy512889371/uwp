package rongji.cmis.service.system.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import rongji.cmis.dao.system.DepartmentDao;
import rongji.cmis.dao.system.UserDao;
import rongji.cmis.dao.system.UserDeptRelaDao;
import rongji.cmis.model.ums.CfgUmsUser;
import rongji.cmis.model.ums.UserDeptRela;
import rongji.cmis.model.unit.CmisDepartment;
import rongji.cmis.service.system.UserDeptRelaService;
import rongji.framework.base.service.impl.BaseServiceImpl;

@Service("userDeptRelaServiceImpl")
public class UserDeptRelaServiceImpl extends BaseServiceImpl<UserDeptRela> implements UserDeptRelaService {

	@Resource(name = "userDeptRelaDaoImpl")
	UserDeptRelaDao userDeptRelaDao;

	@Resource(name = "userDaoImpl")
	UserDao userDao;

	@Resource(name = "departmentDaoImpl")
	DepartmentDao departmentDao;

	@Override
	public void saveDeptRela(String deptId, Integer userId) {
		UserDeptRela userDeptRela = new UserDeptRela();
		CfgUmsUser cfgUmsUser = userDao.find(userId);
		CmisDepartment cmisDepartment = departmentDao.find(deptId);
		List<UserDeptRela> userDeptRelaList = userDeptRelaDao.findByProperty("cfgUmsUser.id", userId);
		if (userDeptRelaList.size() > 0) {
			userDeptRela = userDeptRelaList.get(0);
			userDeptRela.setCfgUmsUser(cfgUmsUser);
			userDeptRela.setDeptId(cmisDepartment);
		} else {
			userDeptRela.setCfgUmsUser(cfgUmsUser);
			userDeptRela.setDeptId(cmisDepartment);
		}

		userDeptRela = (UserDeptRela) userDeptRelaDao.merge(userDeptRela);
	}

	@Override
	public CmisDepartment findDeptByUserId(Integer userId) {
		CmisDepartment cmisDepartment = new CmisDepartment();
		List<UserDeptRela> userDeptRelaList = userDeptRelaDao.findByProperty("cfgUmsUser.id", userId);
		UserDeptRela userDeptRela = new UserDeptRela();
		if (userDeptRelaList.size() > 0) {
			userDeptRela = userDeptRelaList.get(0);
			cmisDepartment = userDeptRela.getDeptId();
		}
		return cmisDepartment;
	}
}
