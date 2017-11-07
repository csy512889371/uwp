package rongji.cmis.controller.common;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import rongji.cmis.model.sys.SysAnnounce;
import rongji.cmis.model.ums.CfgTopmenuUser;
import rongji.cmis.model.ums.CfgUmsMenu;
import rongji.cmis.model.ums.CfgUmsRole;
import rongji.cmis.service.system.*;
import rongji.framework.common.web.controller.BaseController;
import rongji.framework.util.Constant;
import rongji.framework.util.ParamRequest;
import rongji.framework.util.SettingUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("indexController")
@RequestMapping("/index")
public class IndexController extends BaseController {

	@Resource
	UserService userService;

	@Resource
	RoleService roleService;

	@Resource(name = "topMenuServiceImpl")
	TopMenuService topMenuService;

	@Resource(name = "menuServiceImpl")
	MenuService menuService;

	@Resource(name = "msgRemindServiceImpl")
	MsgRemindService msgRemindService;

	@Resource(name = "sysAnnounceServiceImpl")
	SysAnnounceService sysAnnounceService;

	/**
	 * 系统首页
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/sysIndex")
	public String sysIndex(HttpServletRequest request) {
		List<CfgUmsMenu> menuList = (List<CfgUmsMenu>) request.getSession().getAttribute(Constant.USERMENU);
		request.setAttribute("allMenu", new Gson().toJson(menuList));
		request.setAttribute("appAdminUrl", SettingUtils.get().getAppAdminUrl());
		return "sysIndex";
	}

	@SuppressWarnings("unchecked")
	/**
	 * 跳转登录界面
	 * TODO delete
	 * @return
	 */
	@RequestMapping("index")
	public String index(HttpServletRequest request) {

		// 取出所有 菜单 页面上用 权限标签判断
		Integer userId = userService.getCurrentUserId();

		if (userId == null) {
			return "redirect:/";
		}

		// 获取角色信息 查询页面
		List<CfgUmsMenu> menuList = new ArrayList<>();
		List<CfgUmsRole> roleList = roleService.findRoleByUser(userId);
		if (roleList != null && roleList.size() > 0) {
			// 获取菜单权限
			menuList = menuService.findMenuByRoles(roleList);
			request.getSession().setAttribute(Constant.USERMENU, menuList);
		}
		request.setAttribute("allMenu", new Gson().toJson(menuList));

		// 获取当前用户的快捷入口菜单id
		List<CfgTopmenuUser> cfgTopmenuUserList = topMenuService.findMenuByUser(userService.getCurrent());
		List<Integer> topMenuIdList = new ArrayList<>();
		for (CfgTopmenuUser aCfgTopmenuUserList : cfgTopmenuUserList) {
			topMenuIdList.add(aCfgTopmenuUserList.getCfgUmsMenu().getId());
		}
		//获取系统公告
		HashMap<String,Object> param = new HashMap<>();
		ParamRequest paramRequest = new ParamRequest();
		//只获取已启用记录
		param.put("saninf002","1");
		paramRequest.setInFilters(param);
		List<SysAnnounce> sysAnnounces = sysAnnounceService.findAllByParamRequest(paramRequest);

		Integer msgNumber = msgRemindService.findMsgNumber(userId);
		request.setAttribute("topMenuIdList", new Gson().toJson(topMenuIdList));
		request.getSession().setAttribute("msgNumber", msgNumber);
		request.setAttribute("firstLogin", "true");
		request.setAttribute("sysAnnounces", sysAnnounces);
		request.setAttribute("appAdminUrl", SettingUtils.get().getAppAdminUrl());
		return "admin/sysIndex";
	}

	/**
	 * 子系统首页
	 * 
	 * @param id
	 *            菜单ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/subIndex")
	public String subIndex(HttpServletRequest request, Integer id, String type, String url) {
		CfgUmsMenu menuTO = menuService.load(id);
		// 获取用户拥有权限的菜单集
		List<CfgUmsMenu> menuList = (List<CfgUmsMenu>) request.getSession().getAttribute(Constant.USERMENU);
		List<CfgUmsMenu> firstLevelMenus = new ArrayList<CfgUmsMenu>();
		List<CfgUmsMenu> secondLevelMenus = new ArrayList<CfgUmsMenu>();
		List<CfgUmsMenu> thirdLevelMenus = new ArrayList<CfgUmsMenu>();
		List<CfgUmsMenu> sysMenuList = new ArrayList<CfgUmsMenu>();
		CfgUmsMenu currentSystem = null;// 点击按钮所属的一级菜单
		CfgUmsMenu currentMenu = null;
		for (CfgUmsMenu menu : menuList) {
			if ((menu.getId().toString()).equals(id.toString())) {
				if(menu.getType().equals("1")) {
					currentMenu = menu;
				}
			}
			if(menu.getType().equals("1")){
				if (menu.getMenutype() == 1) {
					firstLevelMenus.add(menu);
				} else if (menu.getMenutype() == 2) {
					secondLevelMenus.add(menu);
				} else {
					thirdLevelMenus.add(menu);
				}
			}
		}

		if (currentMenu != null) {
			currentSystem = getCurrentSys(currentMenu);
			if (StringUtils.isEmpty(url)) {
				request.setAttribute("url", currentMenu.getUrl());
			} else {
				request.setAttribute("url", url);
			}

			if (currentMenu.getParent() == null) {
				request.setAttribute("url", null);
			}
		}

		// 获取当前用户的快捷入口菜单id
		List<CfgTopmenuUser> cfgTopmenuUserList = topMenuService.findMenuByUser(userService.getCurrent());
		List<Integer> topMenuIdList = new ArrayList<Integer>();
		for (int i = 0; i < cfgTopmenuUserList.size(); i++) {
			topMenuIdList.add(cfgTopmenuUserList.get(i).getCfgUmsMenu().getId());
		}

		// 用户没有当前菜单的权限
		if (currentSystem == null) {
			currentMenu = menuTO;
			currentSystem = getCurrentSys(menuTO);
			request.setAttribute("url", "index/noPermissions.do");
			sysMenuList.add(currentSystem);
		}

		sysMenuList.addAll(firstLevelMenus);

		request.setAttribute("topMenuIdList", new Gson().toJson(topMenuIdList));
		request.setAttribute("menuOne", new Gson().toJson(sortSysMenu(sysMenuList, currentSystem)));
		request.setAttribute("menuTwo", new Gson().toJson(secondLevelMenus));
		request.setAttribute("menuThree", new Gson().toJson(thirdLevelMenus));
		request.setAttribute("currentMenu", new Gson().toJson(currentMenu));
		request.setAttribute("appAdminUrl", SettingUtils.get().getAppAdminUrl());



		return "admin/subIndex";
	}

	/**
	 * 系统排序：当前系统排在最前面
	 */
	private List<CfgUmsMenu> sortSysMenu(List<CfgUmsMenu> sysMenuList, CfgUmsMenu currentSystem) {
		List<CfgUmsMenu> sortMenuList = new ArrayList<CfgUmsMenu>();
		sortMenuList.add(currentSystem);
		for (CfgUmsMenu menu : sysMenuList) {
			if (!currentSystem.getId().toString().equals(menu.getId().toString())) {
				sortMenuList.add(menu);
			}
		}

		return sortMenuList;
	}

	/**
	 * 
	 * @Title: noPermissions
	 * @Description: 跳转到无权限页面
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author LFG
	 */
	@RequestMapping("noPermissions")
	public String noPermissions() {
		return "noPermissions";
	}

	private CfgUmsMenu getCurrentSys(CfgUmsMenu menu) {
		if (menu.getParent() != null) {
			return getCurrentSys(menu.getParent());
		} else {
			return menu;
		}
	}

	@RequestMapping("valueChoosePage")
	public String valueChoosePage(String title, String[] values, String[] text, HttpServletRequest request) {
		if (StringUtils.isNotEmpty(title) && !title.trim().endsWith("：")) {
			title += "：";
		}
		List<Map<String, String>> select = new ArrayList<Map<String, String>>();
		for (int i = 0; i < values.length; i++) {
			Map<String, String> item = new HashMap<String, String>();
			item.put("val", values[i]);
			item.put("text", text[i]);
			select.add(item);
		}
		request.setAttribute("items", select);
		request.setAttribute("title", title);
		return "common/valueChoosePage";
	}

}
