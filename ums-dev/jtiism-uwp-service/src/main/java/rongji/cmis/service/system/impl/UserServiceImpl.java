package rongji.cmis.service.system.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import rongji.cmis.dao.system.MsgRemindDao;
import rongji.cmis.dao.system.UserDao;
import rongji.cmis.dao.system.UserDeptRelaDao;
import rongji.cmis.model.ums.CfgUmsRoleUser;
import rongji.cmis.model.ums.CfgUmsUser;
import rongji.cmis.model.ums.UserDeptRela;
import rongji.cmis.model.unit.CmisDepartment;
import rongji.cmis.service.system.DepartmentService;
import rongji.cmis.service.system.RoleDeptService;
import rongji.cmis.service.system.RoleUserService;
import rongji.cmis.service.system.UserService;
import rongji.framework.base.pojo.Principal;
import rongji.framework.base.service.impl.BaseServiceImpl;
import rongji.framework.util.Constant;

@Service("userServiceImpl")
public class UserServiceImpl extends BaseServiceImpl<CfgUmsUser> implements UserService {

	@Resource(name = "userDaoImpl")
	UserDao userDao;

	@Resource(name = "userDeptRelaDaoImpl")
	UserDeptRelaDao userDeptRelaDao;

	@Resource(name = "roleUserServiceImpl")
	private RoleUserService roleUserService;

	@Resource(name = "roleDeptServiceImpl")
	private RoleDeptService roleDeptService;

	@Resource(name = "departmentServiceImpl")
	private DepartmentService departmentService;

	@Resource(name = "msgRemindDaoImpl")
	MsgRemindDao msgRemindDao;

	@Override
	public List<CfgUmsUser> checkLoginUser(CfgUmsUser user) {
		return userDao.checkLoginUser(user);
	}

	@Override
	public CfgUmsUser findByUsername(String username) {
		return userDao.findByUsername(username);
	}

	@Override
	public CfgUmsUser getCurrent() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Principal principal = (Principal) request.getSession().getAttribute(Constant.CURRENTUSER);
		if (principal != null) {
			return userDao.find(principal.getId());
		}
		return null;
	}


	@Override
	public Principal getCurrentPrincipal() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Principal principal = (Principal) request.getSession().getAttribute(Constant.CURRENTUSER);
		return principal;
	}


	@Override
	public String getCurrentUsername() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Principal principal = (Principal) request.getSession().getAttribute(Constant.CURRENTUSER);
		if (principal == null) {
			return null;
		}
		return principal.getUsername();
	}

	@Override
	public Integer getCurrentUserId() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Principal principal = (Principal) request.getSession().getAttribute(Constant.CURRENTUSER);
		if (principal == null) {
			return null;
		}
		return principal.getId();
	}

	public CmisDepartment getCurrUserDept() {
		CfgUmsUser currentUser = getCurrent();
		List<UserDeptRela> list = currentUser.getUserDeptRelas();
		if (list != null && list.size() > 0) {
			UserDeptRela udr = list.get(0);
			CmisDepartment cd = udr.getDeptId();
			return cd;
		} else {
			return null;
		}
	}

	@Override
	public String[] getDeptIdsByUserId(Integer userId) {
		// 当前登录用户id
		if (userId == null) {
			userId = this.getCurrentUserId();
		}
		// 角色list
		List<CfgUmsRoleUser> roleUserList = roleUserService.findRoleByUserId(userId);
		String[] deptIdArr = null;
		if (roleUserList != null) {
			Integer[] arr = new Integer[roleUserList.size()];
			for (int i = 0; i < roleUserList.size(); i++) {
				arr[i] = roleUserList.get(i).getRoleid();
			}

			// 当前用户拥有的管理权限
			deptIdArr = roleDeptService.findDeptByRoleIds(arr);
		}
		if (deptIdArr.length <= 0) {
			return new String[] { "" };
		}
		return deptIdArr;
	}

	@Override
	public void saveUserAndDept(CfgUmsUser cfgUmsUser, String deptId) {
		userDao.save(cfgUmsUser);
		UserDeptRela userDeptReal = new UserDeptRela();
		if (StringUtils.isNotEmpty(deptId)) {
			userDeptReal.setCfgUmsUser(cfgUmsUser);
			userDeptReal.setDeptId(departmentService.find(deptId));
			userDeptRelaDao.save(userDeptReal);
		}
	}

	@Override
	public void mergeUserAndDept(CfgUmsUser cfgUmsUser, String deptId) {
		if (cfgUmsUser != null) {
			CfgUmsUser user = userDao.load(cfgUmsUser.getId());
			user.setRemark(cfgUmsUser.getRemark());
			user.setSort(cfgUmsUser.getSort());
			user.setState(cfgUmsUser.getState());
			user.setUsername(cfgUmsUser.getUsername());
			user.setCfgUmsGroup(cfgUmsUser.getCfgUmsGroup());
			userDao.merge(user);
			userDeptRelaDao.deleteByUserId(user.getId());
			UserDeptRela userDeptReal = new UserDeptRela();
			if (StringUtils.isNotEmpty(deptId)) {
				userDeptReal.setCfgUmsUser(cfgUmsUser);
				userDeptReal.setDeptId(departmentService.find(deptId));
				userDeptRelaDao.save(userDeptReal);
			}
		}
	}

	@Override
	public void deleteUser(Integer userId) {
		// 删除角色和企业关系表数据
		roleUserService.deleteRoleUserByUserId(userId);
		// 删除该企业消息
		msgRemindDao.deleteMsgRemindByUserId(userId);
		userDao.delete(userId);
	}

}
