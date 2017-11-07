package rongji.cmis.controller.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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

import rongji.cmis.model.sys.SysColumnShow;
import rongji.cmis.model.ums.CfgUmsCode;
import rongji.cmis.model.ums.CfgUmsCodeAttribute;
import rongji.cmis.model.ums.CfgUmsRole;
import rongji.cmis.model.ums.CfgUmsRoleMenuOper;
import rongji.cmis.model.ums.CfgUmsRoleUser;
import rongji.cmis.model.ums.CfgUmsUser;
import rongji.cmis.model.ums.RoleDeptReal;
import rongji.cmis.model.ums.RoleInfosetPri;
import rongji.cmis.model.unit.CmisDepartment;
import rongji.cmis.service.cadreUnit.InfoCadreBasicAttributeService;
import rongji.cmis.service.system.CfgUmsCodeService;
import rongji.cmis.service.system.DepartmentService;
import rongji.cmis.service.system.MenuOperService;
import rongji.cmis.service.system.MenuService;
import rongji.cmis.service.system.RoleDeptService;
import rongji.cmis.service.system.RoleInfosetPriService;
import rongji.cmis.service.system.RoleMenuOperService;
import rongji.cmis.service.system.RoleService;
import rongji.cmis.service.system.RoleUserService;
import rongji.cmis.service.system.UserDeptRelaService;
import rongji.cmis.service.system.UserService;
import rongji.framework.base.model.ResultModel;
import rongji.framework.base.pojo.Filter;
import rongji.framework.base.pojo.Order.Direction;
import rongji.framework.common.web.controller.BaseController;
import rongji.framework.util.FastjsonUtils;
import rongji.framework.util.Page;
import rongji.framework.util.ParamRequest;
import rongji.framework.util.StringUtil;

@Controller("roleController")
@RequestMapping("/sys/role")
public class RoleController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

	@Resource(name = "roleServiceImpl")
	RoleService roleService;

	@Resource(name = "roleMenuOperServiceImpl")
	RoleMenuOperService roleMenuOperService;

	@Resource(name = "menuOperServiceImpl")
	MenuOperService menuOperService;

	@Resource(name = "roleUserServiceImpl")
	RoleUserService roleUserService;

	@Resource(name = "userServiceImpl")
	UserService userService;

	@Resource(name = "cfgUmsCodeServiceImpl")
	CfgUmsCodeService cfgUmsCodeService;

	@Resource(name = "departmentServiceImpl")
	DepartmentService departmentService;

	@Resource(name = "roleDeptServiceImpl")
	RoleDeptService roleDeptService;

	@Resource(name = "menuServiceImpl")
	MenuService menuService;

	@Resource(name = "infoCadreBasicAttributeServiceImpl")
	InfoCadreBasicAttributeService infoCadreBasicAttributeService;

	@Resource(name = "roleInfosetPriServiceImpl")
	RoleInfosetPriService roleInfosetPriService;

	@Resource(name = "userDeptRelaServiceImpl")
	UserDeptRelaService userDeptRelaService;


	/**
	 * 
	 * @Title: searchRole
	 * @Description: (跳转到角色管理页面)
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("searchRole")
	public String searchRole() {
		return "system/role/roleList";
	}

	/**
	 * 
	 * @Title: roleList
	 * @Description: 模糊查询角色信息
	 * @param roleName
	 * @param paramRequest
	 * @param request
	 * @param response
	 * @return
	 * @return Page<CfgUmsRole> 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("roleList")
	@ResponseBody
	public Page<CfgUmsRole> roleList(String roleName, ParamRequest paramRequest, HttpServletRequest request, HttpServletResponse response) {
		if (StringUtils.isNotBlank(roleName)) {
			Map<String, String> likeFilters = new LinkedHashMap<String, String>();
			likeFilters.put("rolename", roleName);
			paramRequest.setLikeFilters(likeFilters);
		}
		paramRequest.setOrderDirection(Direction.asc);
		paramRequest.setOrderProperty("sort");
		Page<CfgUmsRole> roleList = roleService.findAllForPage(paramRequest);
		return roleList;
	}

	/**
	 * 
	 * @Title: roleAdd
	 * @Description: (跳转到角色添加页面)
	 * @param request
	 * @param response
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("roleAdd")
	public String roleAdd(HttpServletRequest request, HttpServletResponse response) {
		return "system/role/roleAdd";
	}

	/**
	 * 
	 * @Title: roleUpdate
	 * @Description: (跳转到角色更新页面)
	 * @param roleId
	 *            角色ID
	 * @param model
	 *            返回消息
	 * @param request
	 * @param response
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("roleUpdate")
	public String roleUpdate(Integer roleId, Model model, HttpServletRequest request, HttpServletResponse response) {
		CfgUmsRole cfgUmsRole = new CfgUmsRole();
		cfgUmsRole = roleService.find(roleId);
		model.addAttribute("cfgUmsRole", cfgUmsRole);
		return "system/role/roleUpdate";
	}

	/**
	 * 
	 * @Title: saveNewRole
	 * @Description: (保存新增角色信息)
	 * @param cfgUmsRole
	 *            实体信息
	 * @param request
	 * @param response
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("saveNewRole")
	@ResponseBody
	public ResultModel saveNewRole(CfgUmsRole cfgUmsRole, HttpServletRequest request, HttpServletResponse response) {

		try {
			if (StringUtils.isNotBlank(cfgUmsRole.getRolename())) {
				roleService.save(cfgUmsRole);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}

		return ResultModel.success("common.save.success");

	}

	/**
	 * @Description: (判断角色是否关联用户)
	 * @Title: findUsersByRoleId
	 * @param roleId
	 * @param request
	 * @param response
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author LinJH 2016-4-19
	 */
	@RequestMapping("findUsersByRoleId")
	@ResponseBody
	public ResultModel findUsersByRoleId(Integer roleId, HttpServletRequest request, HttpServletResponse response) {

		List<CfgUmsRoleUser> list = this.roleUserService.findUserByRoleId(roleId);
		if (list.isEmpty()) {
			return ResultModel.error("common.delete.fail");
		} else {
			return ResultModel.success("common.delete.success");
		}
	}

	/**
	 * 
	 * @Title: deleteRole
	 * @Description: (删除角色)
	 * @param roleId
	 *            角色ID
	 * @param request
	 * @param response
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("deleteRole")
	@ResponseBody
	public ResultModel deleteRole(Integer roleId, HttpServletRequest request, HttpServletResponse response) {

		CfgUmsRole cfgUmsRole = new CfgUmsRole();
		try {
			cfgUmsRole = roleService.load(roleId);
			if (cfgUmsRole != null) {
				roleService.deleteByRoleId(roleId);
				// roleService.delete(roleId);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.delete.fail");
		}

		return ResultModel.success("common.delete.success");
	}

	/**
	 * 
	 * @Title: updateRole
	 * @Description: (更新角色信息)
	 * @param cfgUmsRole
	 *            实体信息
	 * @param request
	 * @param response
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("updateRole")
	@ResponseBody
	public ResultModel updateRole(CfgUmsRole cfgUmsRole, HttpServletRequest request, HttpServletResponse response) {
		try {
			if (cfgUmsRole != null) {
				CfgUmsRole oldRole = roleService.load(cfgUmsRole.getId());
				// 加载关联数据，避免关联ID对应不上而删除关联表数据
				cfgUmsRole.setMenuOpers(oldRole.getMenuOpers());
				cfgUmsRole = (CfgUmsRole) roleService.merge(cfgUmsRole);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}

		return ResultModel.success("common.save.success");
	}

	/**
	 * 
	 * @Title: getMenuOper
	 * @Description: (获取菜单的操作)
	 * @param roleId
	 *            角色ID
	 * @param menuId
	 *            菜单ID
	 * @param menuCheck
	 *            是否已分配菜单
	 * @param request
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("getMenuOper")
	public String getMenuOper(Integer roleId, Integer menuId, boolean menuCheck, HttpServletRequest request) {
		// 已经拥有的菜单操作权限 OperIds
		List<CfgUmsRoleMenuOper> roleMenuOpers = roleMenuOperService.findByPropertys(new String[] { "role_id", "menuid" }, new Integer[] { roleId, menuId });
		List<Map<String, Object>> menuOperList = menuOperService.findByMenuId(menuId);// 菜单操作列表
		if (roleMenuOpers.size() > 0) {
			CfgUmsRoleMenuOper roleMenuOper = roleMenuOpers.get(0);
			if (roleMenuOper != null) {
				Set<Integer> operIds = roleMenuOper.getOperIds();
				for (Map<String, Object> menuOper : menuOperList) {
					Integer operId = Integer.parseInt(menuOper.get("id").toString());
					if (operIds.contains(operId)) {// 已经拥有的操作
						menuOper.put("checked", true);
					}
				}
			}
		}
		if (menuOperList == null || menuOperList.size() < 1) {// 无操作的菜单
			request.setAttribute("operListNull", true);
			request.setAttribute("message", "暂无操作！");
		} else {// 有操作的菜单
			request.setAttribute("operListNull", false);
			request.setAttribute("operList", FastjsonUtils.toJson(menuOperList));
		}
		request.setAttribute("menuCheck", menuCheck);// 是否已经分配菜单
		request.setAttribute("menuId", menuId);
		request.setAttribute("roleId", roleId);
		return "system/role/operList";
	}

	/**
	 * 
	 * @Title: grantMenuOper
	 * @Description: (分配菜单操作)
	 * @param roleId
	 *            角色ID
	 * @param menuId
	 *            菜单ID
	 * @param checkOperId
	 *            选中的操作ID
	 * @param request
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author Administrator
	 */
	@ResponseBody
	@RequestMapping("grantMenuOper")
	public ResultModel grantMenuOper(Integer roleId, Integer menuId, Integer[] checkOperIds, HttpServletRequest request) {
		try {
			roleMenuOperService.grantRoleMenuOper(roleId, menuId, checkOperIds);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("分配失败！请与管理员联系!");
		}
		return ResultModel.success("分配成功！");
	}

	/**
	 * 
	 * @Title: getRoleUserList
	 * @Description: 跳转到分配用户页面
	 * @param roleId
	 * @param request
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("getRoleUserList")
	public String getRoleUserList(Integer roleId, HttpServletRequest request) {
		request.setAttribute("roleId", roleId);
		return "system/role/roleUser";
	}

	/**
	 * 
	 * @Title: getRoleUser
	 * @Description: 获取用户列表
	 * @param roleId
	 * @param request
	 * @return
	 * @return List<Map<String,Object>> 返回类型
	 * @throws
	 * @author Administrator
	 */
	@ResponseBody
	@RequestMapping("getRoleUser")
	public List<Map<String, Object>> getRoleUser(Integer roleId, HttpServletRequest request) {
		List<CfgUmsRoleUser> roleUserList = roleUserService.findByPropertys(new String[] { "roleid" }, new Integer[] { roleId });
		List<CfgUmsUser> intList = userService.findAll();
		Map<String, Object> map = null;
		List<Map<String, Object>> retuList = new ArrayList<Map<String, Object>>();
		for (CfgUmsUser user : intList) {
			map = new HashMap<String, Object>();
			map.put("USERNAME", user.getUsername());
			map.put("REMARK", user.getRemark());
			for (CfgUmsRoleUser roleUser : roleUserList) {// 若是已分配用户则选中
				if (user.getId().equals(roleUser.getUserid())) {
					map.put("USERROLEID", user.getId());
				}
				CmisDepartment department = userDeptRelaService.findDeptByUserId(user.getId());
				map.put("DEPTNAME", department.getDeptname());
			}
			map.put("ID", user.getId());
			retuList.add(map);
		}
		Collections.sort(retuList, new Comparator<Map<String, Object>>() {
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				// o1，o2是list中的Map，可以在其内取得值，按其排序，此例为降序，s1和s2是排序字段值
				String s1 = (String) o1.get("DEPTNAME");
				String s2 = (String) o2.get("DEPTNAME");
				s1 = (s1 == null) ? "" : s1;
				s2 = (s2 == null) ? "" : s2;
				return -s1.compareTo(s2);
			}
		});
		return retuList;
	}

	/**
	 * 
	 * @Title: grantUser
	 * @Description: 分配用户
	 * @param roleId
	 * @param userIdStr
	 * @param request
	 * @param response
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author Administrator
	 */
	@ResponseBody
	@RequestMapping("grantUser")
	public ResultModel grantUser(Integer roleId, String userIdStr, HttpServletRequest request, HttpServletResponse response) {

		try {
			// 分配角色之前先删除已分配用户
			roleUserService.deleteRoleUserByRoleId(roleId);

			if (StringUtil.isEmpty(userIdStr)) {
				return ResultModel.success("分配成功！");
			}
			String[] userIdArr = userIdStr.split(",");
			for (int i = 0; i < userIdArr.length; i++) {
				Integer userId = Integer.parseInt(userIdArr[i]);

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
	 * @Title: dataPrivilegePage
	 * @Description: (跳转到数据权限页面)
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author LFG
	 */
	@RequestMapping("dataPrivilegePage")
	public String dataPrivilegePage(Model model, String roleId, HttpServletRequest request) {

		ParamRequest param = new ParamRequest();
		param.addFilter(Filter.eq("code", "dataPrivilegeCode"));
		List<CfgUmsCode> codes = cfgUmsCodeService.findAllByParamRequest(param);
		if (codes.size() > 0) {
			Set<CfgUmsCodeAttribute> codeAttrs = codes.get(0).getCfgUmsCodeAttribute();
			model.addAttribute("CODEATTRS", FastjsonUtils.toJson(codeAttrs));
		}
		model.addAttribute("roleId", roleId);
		return "system/role/dataPrivilege";
	}

	/**
	 * 
	 * @Title: roleDeptList
	 * @Description: 访问角色管理权限页面
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author LFG
	 */
	@RequestMapping("roleDeptList")
	public String roleDeptList() {
		return "system/role/roleDeptList";
	}

	/**
	 * 
	 * @Title: roleCadretypeList
	 * @Description: 访问角色管理权限页面
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author LFG
	 */
	@RequestMapping("roleCadretypeList")
	public String roleCadretypeList() {
		return "system/role/roleCadretypeList";
	}

	/**
	 * 
	 * @Title: deptList
	 * @Description: 加载管理权限列表并将角色所分配的管理权限选中
	 * @param roleId
	 *            角色ID
	 * @return
	 * @return List<Map<String,Object>> 返回类型
	 * @throws
	 * @author LFG
	 */
	@ResponseBody
	@RequestMapping("deptList")
	public List<Map<String, Object>> deptList(Integer roleId) {

		List<RoleDeptReal> roleDeptList = roleDeptService.findByPropertys(new String[] { "ROLE_ID" }, new Integer[] { roleId });
		ParamRequest paramRequest = new ParamRequest();
		paramRequest.setOrderDirection(Direction.asc);
		paramRequest.setOrderProperty("inino");
		List<CmisDepartment> cmisDepartmentList = departmentService.findAllByParamRequest(paramRequest);
		Map<String, Object> map = null;
		List<Map<String, Object>> retuList = new ArrayList<Map<String, Object>>();
		for (CmisDepartment department : cmisDepartmentList) {
			map = new HashMap<String, Object>();
			map.put("DEPARTMENT_ID", department.getCode());
			map.put("DEPARTMENT_NAME", department.getDeptname());

			for (RoleDeptReal deptReal : roleDeptList) {// 若是已分配用户则选中
				if (department.getCode().equals(deptReal.getCmisDepartment().getCode())) {
					map.put("ROLE_DEPT_RELA_ID", deptReal.getRoleDeptRealId());
					break;
				}
			}
			retuList.add(map);
		}
		return retuList;
	}


	/**
	 * 
	 * @Title: saveRoleDept
	 * @Description: 修改角色管理权限角色表数据
	 * @param roleId
	 *            角色ID
	 * @param deptIdStr
	 *            管理权限ID串
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author LFG
	 */
	@ResponseBody
	@RequestMapping("saveRoleDept")
	public ResultModel saveRoleDept(String roleId, String deptIdStr) {
		String[] deptIdArr = null;
		try {
			// 删除该角色管理权限角色表数据
			roleDeptService.deleteRoleDeptByRoleId(roleId);
			if (StringUtil.isNotEmpty(deptIdStr)) {
				deptIdArr = deptIdStr.split(",");
			} else {
				return ResultModel.success("common.save.success");
			}
			for (int i = 0; i < deptIdArr.length; i++) {
				RoleDeptReal entity = new RoleDeptReal();
				CfgUmsRole cfgUmsRole = roleService.find(Integer.parseInt(roleId));
				CmisDepartment cmisDepartment = departmentService.find(deptIdArr[i]);
				entity.setCfgUmsRole(cfgUmsRole);
				entity.setCmisDepartment(cmisDepartment);
				roleDeptService.save(entity);// 添加该角色管理权限角色表数据
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}
		return ResultModel.success("common.save.success");
	}


	/**
	 * 
	 * @Title: infoCadreSetTemplate
	 * @Description: 访问信息集模板页面
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author LFG
	 */
	@RequestMapping("infoCadreSetTemplate")
	public String infoCadreSetTemplate(Integer roleId, HttpServletRequest request) {

		ParamRequest param = new ParamRequest();
		param.addFilter(Filter.eq("code", "cadreListCode"));
		List<CfgUmsCode> codes = cfgUmsCodeService.findAllByParamRequest(param);

		param = new ParamRequest();
		Map<String, Object> equalFilters = new LinkedHashMap<String, Object>();
		equalFilters.put("cfgUmsRole.id", roleId);
		param.setEqualFilters(equalFilters);
		param.addFilter(Filter.eq("infoType", 0));// 0:信息集
		List<RoleInfosetPri> roleInfosetPriList = roleInfosetPriService.findAllByParamRequest(param);
		List<Map<String, Object>> infoSetList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> ripList = new ArrayList<Map<String, Object>>();

		Set<CfgUmsCodeAttribute> codeAttrs = codes.get(0).getCfgUmsCodeAttribute();

		for (CfgUmsCodeAttribute cfgUmsCodeAttribute : codeAttrs) {
			//禁用的信息集，不显示
			if (0 == cfgUmsCodeAttribute.getStatus()) {
				continue;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", cfgUmsCodeAttribute.getId());
			String attrCode = "";
			if (StringUtil.isNotEmpty(cfgUmsCodeAttribute.getAttrCode())) {
				attrCode = cfgUmsCodeAttribute.getAttrCode();
			}
			map.put("attrCode", attrCode);
			map.put("attrName", cfgUmsCodeAttribute.getAttrName());
			map.put("parentId", cfgUmsCodeAttribute.getParentId());
			infoSetList.add(map);
		}

		for (RoleInfosetPri rip : roleInfosetPriList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("roleId", rip.getCfgUmsRole().getId());
			map.put("privCode", rip.getPrivCode());
			map.put("priv", rip.getPriv());
			ripList.add(map);
		}
		request.setAttribute("ripList", FastjsonUtils.toJson(ripList));
		request.setAttribute("roleId", roleId);
		request.setAttribute("infoSetList", FastjsonUtils.toJson(infoSetList));

		return "system/role/infoSetTemplateList";
	}

	/**
	 * 
	 * @Title: grantRoleInfoCadre
	 * @Description: 角色设置信息集/信息项权限
	 * @param roleId
	 *            角色ID
	 * @param privCodes
	 *            权限编码
	 * @param privs
	 *            权限 0:只读，1:可写
	 * @param infoType
	 *            数据类型 0:信息集,1:信息项
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author LFG
	 */
	@ResponseBody
	@RequestMapping("grantRoleInfoCadre")
	public ResultModel grantRoleInfoCadre(Integer roleId, String[] privCodes, Integer[] privs, Integer infoType) {
		try {
			roleInfosetPriService.grantRoleInfoCadreSet(roleId, privCodes, privs, infoType);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}
		return ResultModel.success("common.save.success");
	}

	/**
	 * 
	 * @Title: infoCadreTemplate
	 * @Description: 访问信息项模板页面
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author LFG
	 */
	@RequestMapping("infoCadreTemplate")
	public String infoCadreTemplate(Integer roleId, HttpServletRequest request) {

		ParamRequest param = new ParamRequest();
		param.addFilter(Filter.eq("code", "cadreListCode"));
		List<CfgUmsCode> codes = cfgUmsCodeService.findAllByParamRequest(param);

		param = new ParamRequest();
		Map<String, Object> equalFilters = new LinkedHashMap<String, Object>();
		equalFilters.put("cfgUmsRole.id", roleId);
		param.setEqualFilters(equalFilters);
		param.addFilter(Filter.eq("infoType", 1));// 1:信息项
		List<RoleInfosetPri> roleInfosetPriList = roleInfosetPriService.findAllByParamRequest(param);
		List<Map<String, Object>> ripList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> infoList = new ArrayList<Map<String, Object>>();
		List<CfgUmsCodeAttribute> allCodeAttrList = new ArrayList<CfgUmsCodeAttribute>();
		Set<CfgUmsCodeAttribute> codeAttrs = codes.get(0).getCfgUmsCodeAttribute();
		for (CfgUmsCodeAttribute cuca : codeAttrs) {
		    //禁用的信息集 不显示
		    if (0 == cuca.getStatus()) {
		        continue;
            }
			if (StringUtil.isNotEmpty(cuca.getParentId()) && (cuca.getParentId().equals("23") || cuca.getParentId().equals("39") || cuca.getParentId().equals("53") || cuca.getParentId().equals("54") || cuca.getParentId().equals("55") || cuca.getParentId().equals("56") || cuca.getParentId().equals("57") || cuca.getParentId().equals("58"))) {// 23代表常用信息，39代表其它信息
				cuca.setStatus(-2);// 只用于标识是否要展示后面的单选框
			}
			allCodeAttrList.add(cuca);
		}
		for (CfgUmsCodeAttribute cfgUmsCodeAttribute : codeAttrs) {
            //禁用的信息集 不显示
            if (0 == cfgUmsCodeAttribute.getStatus()) {
                continue;
            }

			if (StringUtil.isNotEmpty(cfgUmsCodeAttribute.getAttrCode())) {
				List<SysColumnShow> sysColumnShows = infoCadreBasicAttributeService.findSysColumnShowListByInfoSet(cfgUmsCodeAttribute.getAttrCode());
				for (SysColumnShow scs : sysColumnShows) {
				    //未启用的 信息项 不显示
				    if (false == scs.getIsEnabled()) {
				        continue;
                    }
					CfgUmsCodeAttribute cuca = new CfgUmsCodeAttribute();
					if (StringUtil.isNotEmpty(scs.getPropertyName())) {
						cuca.setAttrCode(scs.getInfoSet() + "_" + scs.getPropertyName());
					} else {
						cuca.setAttrCode(scs.getInfoSet() + "_" + scs.getPropertyCode());
					}
					cuca.setAttrName(scs.getName());
					cuca.setId(scs.getId());
					cuca.setParentId(cfgUmsCodeAttribute.getId());
					cuca.setStatus(-1);// 只用于标识是否要展示后面的单选框
					allCodeAttrList.add(cuca);
				}
			}
		}
		for (CfgUmsCodeAttribute cuca : allCodeAttrList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("attrCode", cuca.getAttrCode());
			map.put("attrName", cuca.getAttrName());
			map.put("id", cuca.getId());
			map.put("parentId", cuca.getParentId());
			map.put("status", cuca.getStatus());
			infoList.add(map);
		}

		for (RoleInfosetPri rip : roleInfosetPriList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("roleId", rip.getCfgUmsRole().getId());
			map.put("privCode", rip.getPrivCode());
			map.put("priv", rip.getPriv());
			ripList.add(map);
		}

		request.setAttribute("ripList", FastjsonUtils.toJson(ripList));
		request.setAttribute("infoList", FastjsonUtils.toJson(infoList));
		request.setAttribute("roleId", roleId);
		return "system/role/infoTemplateList";
	}


}
