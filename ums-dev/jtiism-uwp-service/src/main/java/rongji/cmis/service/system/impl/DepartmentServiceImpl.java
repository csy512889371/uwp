package rongji.cmis.service.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import rongji.cmis.dao.system.DepartmentDao;
import rongji.cmis.dao.system.RoleDeptDao;
import rongji.cmis.model.ums.CfgUmsRoleUser;
import rongji.cmis.model.ums.RoleDeptReal;
import rongji.cmis.model.unit.CmisDepartment;
import rongji.cmis.service.system.DepartmentService;
import rongji.cmis.service.system.RoleUserService;
import rongji.cmis.service.system.UserService;
import rongji.framework.base.pojo.Order.Direction;
import rongji.framework.base.service.impl.BaseServiceImpl;
import rongji.framework.util.ParamRequest;

@Service("departmentServiceImpl")
public class DepartmentServiceImpl extends BaseServiceImpl<CmisDepartment> implements DepartmentService {

	@Resource(name = "departmentDaoImpl")
	DepartmentDao departmentDao;

	@Resource(name = "roleDeptDaoImpl")
	RoleDeptDao roleDeptDao;

	@Resource(name = "userServiceImpl")
	UserService userService;

	@Resource(name = "roleUserServiceImpl")
	RoleUserService roleUserService;

	@Override
	public void saveDeptOrder(String deptId, String orderNum) {
		departmentDao.saveDeptOrder(deptId, orderNum);

	}


	@Override
	public Map<String, Object> getCadreDeptOperMap() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Integer userId = userService.getCurrentUserId();
		// 角色list
		List<CfgUmsRoleUser> roleUserList = roleUserService.findRoleByUserId(userId);
		// 当前用户 -> 用户角色 -> 角色管理权限权限
		List<RoleDeptReal> allRoleDeptList = new ArrayList<RoleDeptReal>();
		List<RoleDeptReal> roleDeptList = null;
		for (CfgUmsRoleUser cfgUmsRoleUser : roleUserList) {
			roleDeptList = roleDeptDao.findByPropertys(new String[] { "ROLE_ID" }, new Integer[] { cfgUmsRoleUser.getRoleid() });
			if (!roleDeptList.isEmpty() && roleDeptList.size() > 0) {
				allRoleDeptList.addAll(roleDeptList);
			}
		}
		ParamRequest paramRequest = new ParamRequest();
		paramRequest.setOrderDirection(Direction.asc);
		paramRequest.setOrderProperty("orderno");
		List<CmisDepartment> cmisDepartmentList = departmentDao.findAllByParamRequest(paramRequest);
		for (CmisDepartment department : cmisDepartmentList) {
			for (RoleDeptReal deptReal : allRoleDeptList) {// 若是已分配用户则选中
				if (department.getCode().equals(deptReal.getCmisDepartment().getCode())) {
					returnMap.put(department.getCode(),"0");
					break;
				}
			}
		}
		return returnMap;
	}

	@Override
	public boolean isCodeExists(String id) {
		return departmentDao.isCodeExists(id) == 0;
	}

	@Override
	public void updateDepts(String code, String deptname, String description) {
		departmentDao.updateDepts(code, deptname, description);
	}
}
