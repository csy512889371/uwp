package rongji.cmis.controller.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import rongji.cmis.model.ums.CfgUmsGroup;
import rongji.cmis.model.ums.CfgUmsRole;
import rongji.cmis.model.ums.CfgUmsRoleUser;
import rongji.cmis.model.ums.CfgUmsUser;
import rongji.cmis.model.ums.UserDeptRela;
import rongji.cmis.model.unit.CmisDepartment;
import rongji.cmis.service.system.DepartmentService;
import rongji.cmis.service.system.GroupService;
import rongji.cmis.service.system.RoleService;
import rongji.cmis.service.system.RoleUserService;
import rongji.cmis.service.system.UserDeptRelaService;
import rongji.cmis.service.system.UserService;
import rongji.framework.base.model.ResultModel;
import rongji.framework.base.pojo.Filter;
import rongji.framework.base.pojo.Order.Direction;
import rongji.framework.common.web.controller.BaseController;
import rongji.framework.util.FastjsonUtils;
import rongji.framework.util.MD5;
import rongji.framework.util.Page;
import rongji.framework.util.ParamRequest;
import rongji.framework.util.SettingUtils;
import rongji.framework.util.StringUtil;

@Controller("userController")
@RequestMapping("/sys/user")
public class UserController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Resource(name = "userServiceImpl")
	UserService userService;

	@Resource(name = "roleServiceImpl")
	RoleService roleService;

	@Resource(name = "roleUserServiceImpl")
	RoleUserService roleUserService;

	@Resource(name = "departmentServiceImpl")
	DepartmentService departmentService;

	@Resource(name = "userDeptRelaServiceImpl")
	UserDeptRelaService userDeptRelaService;

	@Resource(name = "groupServiceImpl")
	GroupService groupService;

	/**
	 * 
	 * @Title: searchUser
	 * @Description: (跳转到用户管理页面)
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("searchUser")
	public String searchUser() {
		return "system/user/userList";
	}

	/**
	 * 
	 * @Title: userList
	 * @Description: 条件模糊查询用户信息
	 * @param userName
	 * @param paramRequest
	 * @param request
	 * @param response
	 * @return
	 * @return Page<CfgUmsUser> 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("userList")
	@ResponseBody
	public Page<CfgUmsUser> userList(String userName, String type, String groupId, ParamRequest paramRequest, HttpServletRequest request, HttpServletResponse response) {

		if ("group".equals(type)) {
			paramRequest.addFilter(Filter.eq("cfgUmsGroup.id", groupId == null ? "" : groupId));
		} else {
			if (StringUtils.isNotBlank(userName)) {
				paramRequest.addFilter(Filter.like("username", userName));
			}
		}
		Page<CfgUmsUser> userList = userService.findAllForPage(paramRequest);
		List<CfgUmsUser> resultList = userList.getResult();
		List<Map<String, Object>> resultMapList = new ArrayList<Map<String, Object>>();
		for (CfgUmsUser user : resultList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", user.getId());
			map.put("username", user.getUsername());
			map.put("state", user.getState());
			map.put("remark", user.getRemark());
			CmisDepartment department = userDeptRelaService.findDeptByUserId(user.getId());
			map.put("deptName", department.getDeptname());
			List<CfgUmsRoleUser> roleUserList = roleUserService.findByUserId(user.getId());
			String rolesName = "";
			for (CfgUmsRoleUser roleUser : roleUserList) {
				Integer roleId = roleUser.getRoleid();
				CfgUmsRole role = roleService.find(roleId);
				if (role != null) {
					rolesName += role.getRolename() + ",";
				}
			}
			if (StringUtils.isNotEmpty(rolesName)) {
				rolesName = rolesName.substring(0, rolesName.length() - 1);
			}
			map.put("rolesName", rolesName);
			resultMapList.add(map);
		}
		userList.setResultMapList(resultMapList);
		return userList;
	}

	/**
	 * 
	 * @Title: userAdd
	 * @Description: (跳转到用户添加页面)
	 * @param request
	 * @param response
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("userAdd")
	public String userAdd(HttpServletRequest request, HttpServletResponse response) {
		ParamRequest paramRequest = new ParamRequest();
		paramRequest.setOrderProperty("inino");
		paramRequest.setOrderDirection(Direction.asc);
		List<CmisDepartment> cmisDepartmentList = departmentService.findAllByParamRequest(paramRequest);
		request.setAttribute("deptList", cmisDepartmentList);
		ParamRequest paramRequest2 = new ParamRequest();
		paramRequest2.setOrderProperty("seqno");
		paramRequest2.setOrderDirection(Direction.asc);
		List<CfgUmsGroup> groupList = groupService.findAllByParamRequest(paramRequest2);
		request.setAttribute("groupList", groupList);
		return "system/user/userAdd";
	}

	/**
	 * 
	 * @Title: userUpdate
	 * @Description: (跳转到更新用户信息页面)
	 * @param userId
	 *            用户ID
	 * @param model
	 *            返回消息
	 * @param request
	 * @param response
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("userUpdate")
	public String userUpdate(Integer userId, Model model, HttpServletRequest request, HttpServletResponse response) {
		CfgUmsUser cfgUmsUser = new CfgUmsUser();
		cfgUmsUser = userService.find(userId);
		model.addAttribute("cfgUmsUser", cfgUmsUser);
		ParamRequest paramRequest = new ParamRequest();
		paramRequest.setOrderProperty("inino");
		paramRequest.setOrderDirection(Direction.asc);
		List<CmisDepartment> cmisDepartmentList = departmentService.findAllByParamRequest(paramRequest);
		model.addAttribute("deptList", cmisDepartmentList);
		if (cfgUmsUser.getUserDeptRelas() != null && !cfgUmsUser.getUserDeptRelas().isEmpty()) {
			model.addAttribute("selectDeptId", cfgUmsUser.getUserDeptRelas().get(0).getDeptId().getCode());
		}

		return "system/user/userUpdate";
	}

	/**
	 * 
	 * @Title: saveNewUser
	 * @Description: (保存新增用户信息)
	 * @param cfgUmsUser
	 *            实体信息
	 * @param request
	 * @param response
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author Administrator
	 */
	@ResponseBody
	@RequestMapping("saveNewUser")
	public ResultModel saveNewUser(CfgUmsUser cfgUmsUser, String deptId, String groupId, HttpServletRequest request, HttpServletResponse response) {

		String password = MD5.MD5Encode("1234");
		cfgUmsUser.setPassword(password);

		try {
			if (cfgUmsUser != null) {
				// 保存到数据库
				cfgUmsUser.setCfgUmsGroup(groupService.load(groupId));
				userService.save(cfgUmsUser);
				userService.saveUserAndDept(cfgUmsUser, deptId);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}

		return ResultModel.success("common.save.success");

	}

	/**
	 * 
	 * @Title: deleteUser
	 * @Description: (删除用户)
	 * @param userId
	 *            用户ID
	 * @param request
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("deleteUser")
	@ResponseBody
	public ResultModel deleteUser(Integer userId, HttpServletRequest request) {
		try {
			userService.deleteUser(userId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.delete.fail");
		}
		return ResultModel.success("common.delete.success");
	}

	/**
	 * 
	 * @Title: updateUser
	 * @Description: (更新用户信息)
	 * @param cfgUmsUser
	 *            实体信息
	 * @param request
	 * @param response
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author Administrator
	 */
	@ResponseBody
	@RequestMapping("updateUser")
	public ResultModel updateUser(CfgUmsUser cfgUmsUser, String deptId, String groupId, HttpServletRequest request, HttpServletResponse response) {

		try {
			if (StringUtil.isEmpty(groupId)) {
				return ResultModel.error("common.save.fail");
			}
			cfgUmsUser.setCfgUmsGroup(groupService.load(groupId));
			userService.mergeUserAndDept(cfgUmsUser, deptId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}

		return ResultModel.success("common.save.success");
	}

	/**
	 * 
	 * @Title: resetPassword
	 * @Description: (恢复密码)
	 * @param userId
	 *            用户ID
	 * @param request
	 * @param response
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author Administrator
	 */
	@ResponseBody
	@RequestMapping("resetPassword")
	public ResultModel resetPassword(Integer userId, HttpServletRequest request, HttpServletResponse response) {
		CfgUmsUser cfgUmsUser = userService.find(userId);

		if (cfgUmsUser != null) {
			cfgUmsUser.setPassword(MD5.MD5Encode("1234"));
		}
		try {
			cfgUmsUser = (CfgUmsUser) userService.merge(cfgUmsUser);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			ResultModel.error("密码重置失败！请与管理员联系！");
		}

		return ResultModel.success("密码重置成功！");
	}

	/**
	 * 
	 * @Title: getRoleList
	 * @Description: (获取用户角色信息)
	 * @param userId
	 *            用户ID
	 * @param request
	 * @param response
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("getRoleList")
	public String getRoleList(Integer userId, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("userId", userId);
		return "system/user/userRole";
	}

	/**
	 * 
	 * @param userId
	 * @param request
	 * @return
	 */
	@RequestMapping("getBranchPage")
	public String getBranchPage(Integer userId, Model model) {
		model.addAttribute("userId", userId);
		/*
		 * List<UserBranchRela> userBranchRelas =
		 * userService.find(userId).getUserBranchRelas(); List<String> branchIds
		 * = new ArrayList<String>(); for (UserBranchRela userBranchRela :
		 * userBranchRelas) {
		 * branchIds.add(userBranchRela.getBranchInfo().getBranchInfoId()); }
		 * model.addAttribute("barnchInfoIds", FastjsonUtils.toJson(branchIds));
		 */
		return "system/user/userBranch";
	}

	/**
	 * 
	 * @Title: getUserRole
	 * @Description: (获取角色列表)
	 * @param userId
	 *            用户ID
	 * @param request
	 * @param response
	 * @return
	 * @return List<Map<String,Object>> 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("getUserRole")
	@ResponseBody
	public List<Map<String, Object>> getUserRole(Integer userId, HttpServletRequest request, HttpServletResponse response) {
		List<CfgUmsRoleUser> umsRoleUsers = roleUserService.findByPropertys(new String[] { "userid" }, new Integer[] { userId });
		List<CfgUmsRole> intList = roleService.findAll();
		Map<String, Object> map = null;
		List<Map<String, Object>> retuList = new ArrayList<Map<String, Object>>();
		for (CfgUmsRole role : intList) {
			map = new HashMap<String, Object>();
			map.put("ROLENAME", role.getRolename());
			map.put("REMARK", role.getRemark());

			for (CfgUmsRoleUser roleUser : umsRoleUsers) {// 若是已分配角色则选中
				if (role.getId().equals(roleUser.getRoleid())) {
					map.put("ROLEUSERID", role.getId());
				}
			}
			map.put("ID", role.getId());
			retuList.add(map);
		}
		return retuList;
	}

	/**
	 * 
	 * @Title: fenpeiRole
	 * @Description: (为用户分配角色)
	 * @param userId
	 *            用户ID
	 * @param roleIdStr
	 *            角色ID
	 * @param request
	 * @param response
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author Administrator
	 */
	@ResponseBody
	@RequestMapping("grantRole")
	public ResultModel grantRole(Integer userId, String roleIdStr, HttpServletRequest request, HttpServletResponse response) {

		try {
			// 分配角色之前先删除已分配角色
			roleUserService.deleteRoleUserByUserId(userId);

			// 给用户分配角色时，无角色也可以保存，需要删除已分配的角色 linjh 2016-04-19
			if (StringUtil.isEmpty(roleIdStr)) {
				return ResultModel.success("分配成功！");
			}

			String[] roleIdArr = roleIdStr.split(",");

			for (int i = 0; i < roleIdArr.length; i++) {
				Integer roleId = Integer.parseInt(roleIdArr[i]);

				CfgUmsRoleUser cfgUmsRoleUser = new CfgUmsRoleUser();
				cfgUmsRoleUser.setUserid(userId);
				cfgUmsRoleUser.setRoleid(roleId);
				
				roleUserService.save(cfgUmsRoleUser);

			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("分配失败！请与管理员联系！");
		}
		return ResultModel.success("分配成功！");
	}

	/**
	 * 
	 * @Title: currentUserInfo
	 * @Description: (跳转到当前用户信息页面)
	 * @param request
	 * @param response
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author HuJingqiang
	 */
	@RequestMapping("currentUserInfo")
	public String currentUserInfo(HttpServletRequest request) {
		CfgUmsUser currentUser = userService.getCurrent();
		Integer userId = currentUser.getId();
		// 获取当前用户角色
		List<CfgUmsRole> roleList = roleService.findRoleByUser(userId);
		request.setAttribute("roleList", roleList);

		// 获取当前用户状态
		if (currentUser.getState()) {
			request.setAttribute("state", "有效");
		} else {
			request.setAttribute("state", "无效");
		}
		// 获取当前用户备注
		String remark = currentUser.getRemark();
		request.setAttribute("remark", remark);
		return "system/user/currentUserInfo";
	}

	/**
	 * @Title: isExistPassword
	 * 
	 * @Description: (判断表单输入的旧密码是否与数据库中的密码相同)
	 * @param request
	 * @param paramRequest
	 * @return
	 * @throws IOException
	 * @author HuJingqiang
	 */
	@ResponseBody
	@RequestMapping("/isExistPassword")
	public boolean isExistPassword(HttpServletRequest request) throws IOException {
		CfgUmsUser cfgUmsUser = userService.getCurrent();
		String oldPassword = MD5.MD5Encode(request.getParameter("oldPassword"));
		String sqlPassword = cfgUmsUser.getPassword();
		if (oldPassword != null && oldPassword.equals(sqlPassword)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @Title: updatePassword
	 * 
	 * @Description: (修改密码)
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author HuJingqiang
	 */
	@ResponseBody
	@RequestMapping("updatePassword")
	public ResultModel updatePassword(String password, HttpServletRequest request, HttpServletResponse response) {
		try {
			CfgUmsUser cfgUmsUser = userService.getCurrent();
			if (cfgUmsUser != null) {
				cfgUmsUser.setPassword(MD5.MD5Encode(password));
			}

			cfgUmsUser = (CfgUmsUser) userService.merge(cfgUmsUser);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("密码修改失败！请与管理员联系！");
		}
		return ResultModel.success("密码修改成功！");
	}

	/**
	 * 
	 * @Title: getUserDeptList
	 * @Description: (获取管理权限列表)
	 * @param paramRequest
	 * @return
	 * @return Page<CmisDepartment> 返回类型
	 * @throws
	 * @author Administrator
	 */
	@ResponseBody
	@RequestMapping("getUserDeptList")
	public Page<CmisDepartment> getUserDeptList(ParamRequest paramRequest) {
		paramRequest.setOrderProperty("orderno");
		paramRequest.setOrderDirection(Direction.asc);
		return departmentService.findAllForPage(paramRequest);
	}

	/**
	 * 
	 * @Title: getOfficesPage
	 * @Description: (跳转到管理权限分配页面)
	 * @param userId
	 * @param model
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("getOfficesPage")
	public String getOfficesPage(Integer userId, Model model) {
		model.addAttribute("userId", userId);
		List<UserDeptRela> userDeptRelas = userService.find(userId).getUserDeptRelas();
		List<String> deptList = new ArrayList<String>();
		for (UserDeptRela userDeptRela : userDeptRelas) {
			deptList.add(userDeptRela.getDeptId().getCode());
		}
		model.addAttribute("deptList", FastjsonUtils.toJson(deptList));
		return "system/user/userOffices";
	}

	/**
	 * 
	 * @Title: grantUserOfficesRela
	 * @Description: (分配管理权限)
	 * @param deptId
	 * @param userId
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author Administrator
	 */
	@ResponseBody
	@RequestMapping("grantUserOfficesRela")
	public ResultModel grantUserOfficesRela(String deptId, Integer userId) {
		try {
			userDeptRelaService.saveDeptRela(deptId, userId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}
		return ResultModel.success("common.save.success");
	}

	@RequestMapping("searchGroupIndex")
	public String searchGroupIndex() {
		return "system/group/groupIndex";
	}

	@ResponseBody
	@RequestMapping("groupList")
	public List<CfgUmsGroup> groupList(ParamRequest paramRequest) {
		CfgUmsGroup parGroup = new CfgUmsGroup();
		parGroup.setGroupName(SettingUtils.get().getUserGroupRootName());
		parGroup.setId("0");

		paramRequest.setOrderDirection(Direction.asc);
		paramRequest.setOrderProperty("seqno");
		List<CfgUmsGroup> groups = groupService.findAllByParamRequest(paramRequest);
		for (CfgUmsGroup group : groups) {
			if (group.getParentGroup() == null || StringUtil.isEmpty(group.getParentGroup().getId())) {
				group.setParentGroup(parGroup);
			}
		}
		return groups;
	}

	@ResponseBody
	@RequestMapping("getGroupList")
	public List<Map<String, Object>> getGroupList(ParamRequest paramRequest, String filterIds) {
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		String userGRName = SettingUtils.get().getUserGroupRootName();
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("id", "0");
		root.put("pId", "");
		root.put("groupName", userGRName);
		results.add(root);

		paramRequest.setOrderDirection(Direction.asc);
		paramRequest.setOrderProperty("seqno");
		List<CfgUmsGroup> groups = groupService.findAllByParamRequest(paramRequest);

		for (CfgUmsGroup group : groups) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", group.getId());
			String pId = "0";
			if (group.getParentGroup() != null) {
				pId = group.getParentGroup().getId();
			}
			map.put("pId", pId);
			map.put("groupName", group.getGroupName());
			results.add(map);
		}
		return results;
	}

	@ResponseBody
	@RequestMapping("deleteGroup")
	public ResultModel deleteGroup(String id) {
		try {
			CfgUmsGroup group = groupService.find(id);
			if (group != null) {
				Set<CfgUmsGroup> groups = group.getChildGroups();
				Set<CfgUmsUser> users = group.getUsers();
				if (!groups.isEmpty()) {
					return ResultModel.error("该分组下有子分组，请先解除分组关系再删除");
				} else if (!users.isEmpty()) {
					return ResultModel.error("该分组下挂有用户，请先解除用户关系再删除");
				}
				groupService.delete(group);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.delete.fail");
		}
		return ResultModel.success("common.delete.success");
	}

	@ResponseBody
	@RequestMapping("saveNewGroup")
	public ResultModel saveNewGroup(CfgUmsGroup group) {
		try {
			if (group.getParentGroup() != null) {
				if ("0".equals(group.getParentGroup().getId())) {
					// 根节点
					group.setParentGroup(null);
				}
			}
			groupService.saveAndSeqno(group);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}
		return ResultModel.success("common.save.success");
	}

	@ResponseBody
	@RequestMapping("updateGroup")
	public ResultModel updateGroup(CfgUmsGroup group) {
		try {
			if (group.getParentGroup() != null) {
				if ("0".equals(group.getParentGroup().getId())) {
					// 根节点
					group.setParentGroup(null);
				} else {
					group.setParentGroup(groupService.load(group.getParentGroup().getId()));
				}
			}
			groupService.update(group);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}
		return ResultModel.success("common.save.success");
	}

	@RequestMapping("groupAdd")
	public String groupAdd() {
		return "system/group/groupAdd";
	}

	@RequestMapping("groupUpdate")
	public String groupUpdate(String groupId, Model model) {
		CfgUmsGroup group = groupService.load(groupId);
		if (group.getParentGroup() == null || StringUtil.isEmpty(group.getParentGroup().getId())) {
			CfgUmsGroup parGroup = new CfgUmsGroup();
			parGroup.setGroupName(SettingUtils.get().getUserGroupRootName());
			parGroup.setId("0");
			group.setParentGroup(parGroup);
		}
		model.addAttribute("group", group);
		return "system/group/groupUpdate";
	}

	@RequestMapping("selectGroup")
	public String selectGroup(String inputCodeId, String inputShowId, String filterIds, Boolean rootSelected, Model model) {
		model.addAttribute("inputCodeId", inputCodeId);
		model.addAttribute("inputShowId", inputShowId);
		model.addAttribute("filterIds", filterIds);
		model.addAttribute("rootSelected", rootSelected);
		return "system/group/selectGroup";
	}

	@ResponseBody
	@RequestMapping("saveGroupSort")
	public ResultModel saveGroupSort(String groupIds) {
		try {
			groupService.saveGroupSort(groupIds);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("保存排序失败");
		}
		return ResultModel.success("保存排序成功");
	}
}
