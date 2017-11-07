package rongji.cmis.controller.system;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

import rongji.cmis.model.ums.CfgTopmenuUser;
import rongji.cmis.model.ums.CfgUmsMenu;
import rongji.cmis.model.ums.CfgUmsMenuOper;
import rongji.cmis.model.ums.CfgUmsUser;
import rongji.cmis.service.system.MenuOperService;
import rongji.cmis.service.system.MenuService;
import rongji.cmis.service.system.RoleMenuOperService;
import rongji.cmis.service.system.TopMenuService;
import rongji.cmis.service.system.UserService;
import rongji.framework.base.model.ResultModel;
import rongji.framework.base.pojo.Order.Direction;
import rongji.framework.common.web.controller.BaseController;
import rongji.framework.util.Constant;
import rongji.framework.util.Page;
import rongji.framework.util.ParamRequest;

@Controller("menuController")
@RequestMapping("/sys/menu")
public class MenuController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

	@Resource(name = "menuServiceImpl")
	MenuService menuService;

	@Resource(name = "menuOperServiceImpl")
	MenuOperService menuOperService;

	@Resource(name = "roleMenuOperServiceImpl")
	RoleMenuOperService roleMenuOperService;
	
	@Resource(name = "topMenuServiceImpl")
	TopMenuService topMenuService;
	
	@Resource(name = "userServiceImpl")
	UserService userService;

	/**
	 * 
	 * @Title: searchMenu
	 * @Description: (跳转到菜单管理页面)
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("searchMenu")
	public String searchMenu() {
		return "system/menu/menuList";
	}

	/**
	 * 
	 * @Title: menuList
	 * @Description: 模糊查询菜单信息
	 * @param title
	 * @param paramRequest
	 * @param request
	 * @param response
	 * @return
	 * @return Page<CfgUmsMenu> 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("menuList")
	@ResponseBody
	public Page<CfgUmsMenu> menuList(String title, ParamRequest paramRequest, HttpServletRequest request, HttpServletResponse response) {
		if (StringUtils.isNotBlank(title)) {
			Map<String, String> likeFilters = new LinkedHashMap<String, String>();
			likeFilters.put("title", title);
			paramRequest.setLikeFilters(likeFilters);
		}
		String sortColumns = "sort";
		paramRequest.setOrderProperty(sortColumns);
		paramRequest.setOrderDirection(Direction.asc);
		paramRequest.setPageSize(500);
		Page<CfgUmsMenu> menuList = menuService.findAllForPage(paramRequest);
		return menuList;
	}

	/**
	 * 
	 * @Title: addChildrenMenu
	 * @Description: (跳转到下级菜单添加页面)
	 * @param parentId
	 *            菜单父ID
	 * @param model
	 *            返回消息
	 * @param request
	 * @param response
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("addChildrenMenu")
	public String addChildrenMenu(Integer parentId, Model model, HttpServletRequest request, HttpServletResponse response) {
		CfgUmsMenu cfgUmsMenu = menuService.find(parentId);
		model.addAttribute("cfgUmsMenu", cfgUmsMenu);
		return "system/menu/addChildrenMenu";
	}

	/**
	 * 
	 * @Title: menuAdd
	 * @Description: (跳转到菜单添加页面)
	 * @param parentId
	 *            菜单父ID
	 * @param model
	 *            返回消息
	 * @param request
	 * @param response
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("menuAdd")
	public String menuAdd(Integer parentId, Model model, HttpServletRequest request, HttpServletResponse response) {
		CfgUmsMenu cfgUmsMenu = new CfgUmsMenu();
		if (parentId != null) {
			cfgUmsMenu = menuService.find(parentId);
		}
		model.addAttribute("cfgUmsMenu", cfgUmsMenu);
		return "system/menu/menuAdd";
	}

	/**
	 * 
	 * @Title: menuUpdate
	 * @Description: (跳转到菜单更新页面)
	 * @param menuId
	 *            菜单ID
	 * @param model
	 *            返回消息
	 * @param request
	 * @param response
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("menuUpdate")
	public String menuUpdate(Integer menuId, Model model, HttpServletRequest request, HttpServletResponse response) {
		CfgUmsMenu cfgUmsMenu = menuService.find(menuId);
		model.addAttribute("cfgUmsMenu", cfgUmsMenu);
		return "system/menu/menuUpdate";
	}

	/**
	 * 
	 * @Title: saveNewMenu
	 * @Description: (保存新增菜单信息)
	 * @param cfgUmsMenu
	 *            实体信息
	 * @param request
	 * @param response
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("saveNewMenu")
	@ResponseBody
	public ResultModel saveNewMenu(CfgUmsMenu cfgUmsMenu, HttpServletRequest request, HttpServletResponse response) {
		try {
			if (cfgUmsMenu.getMenutype() == 1) {// 如果是系统菜单，无论有无选中父类，先清空父类，直接保存
				cfgUmsMenu.setParent(null);
			} else {// 如果是二级，三级菜单，需校验与父类的层级关系是否正确
				CfgUmsMenu parent = menuService.find(cfgUmsMenu.getParent().getId());
				if (cfgUmsMenu.getMenutype() - parent.getMenutype() != 1) {
					return ResultModel.error("失败，菜单等级与父类等级不对应");
				}
			}
			menuService.save(cfgUmsMenu);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}

		return ResultModel.success("common.save.success");
	}

	/**
	 * 
	 * @Title: deleteMenu
	 * @Description: (删除菜单)
	 * @param menuId
	 *            菜单ID
	 * @param request
	 * @param response
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("deleteMenu")
	@ResponseBody
	public ResultModel deleteMenu(Integer menuId, HttpServletRequest request, HttpServletResponse response) {
		try {
			return menuService.deleteAfterChecked(menuId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("删除失败，请联系管理员");
		}
	}

	/**
	 * 
	 * @Title: updateMenu
	 * @Description: (更新菜单信息)
	 * @param cfgUmsMenu
	 *            实体信息
	 * @param request
	 * @param response
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("updateMenu")
	@ResponseBody
	public ResultModel updateMenu(CfgUmsMenu cfgUmsMenu, HttpServletRequest request, HttpServletResponse response) {

		try {
			if (cfgUmsMenu.getParent().getId() == null) {
				cfgUmsMenu.setParent(null);
			}
			cfgUmsMenu = (CfgUmsMenu)menuService.merge(cfgUmsMenu);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}

		return ResultModel.success("common.save.success");
	}

	/**
	 * 
	 * @Title: loadMenu
	 * @Description: (加载系统下的菜单信息)
	 * @param request
	 *            菜单ID
	 * @return
	 * @return Map<String,List<CfgUmsMenu>> 返回类型
	 * @throws
	 * @author Administrator
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("loadMenu")
	@ResponseBody
	public Map<String, List<CfgUmsMenu>> loadMenu(HttpServletRequest request) {
		List<CfgUmsMenu> list = (List<CfgUmsMenu>) request.getSession().getAttribute(Constant.USERMENU);
		// 同一父类的菜单放在一起
		Map<String, List<CfgUmsMenu>> menuMap = new HashMap<>();
		for (CfgUmsMenu m : list) {
			if (m.getParent() == null) {// 系统菜单不用
				continue;
			}
			if (menuMap.get(m.getParent().getId().toString()) == null) {
				menuMap.put(m.getParent().getId().toString(), new ArrayList<CfgUmsMenu>());
			}
			menuMap.get(m.getParent().getId().toString()).add(m);
		}
		return menuMap;
	}

	/**
	 * 
	 * @Title: menuPower
	 * @Description: (跳转到菜单权限操作页面)
	 * @param menuId
	 *            菜单ID
	 * @param request
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("menuOperList")
	public String menuOperList(String menuId, HttpServletRequest request) {
		request.setAttribute("menuId", menuId);
		return "system/menu/menuOperList";
	}

	/**
	 * 
	 * @Title: getMenuOperList
	 * @Description: (根据menuId返回对应得操作项)
	 * @param menuId
	 * @return
	 * @return Page<CfgUmsMenuOper> 返回类型
	 * @throws
	 * @author spongebob
	 */
	@RequestMapping("getMenuOperList")
	@ResponseBody
	public Page<CfgUmsMenuOper> getMenuOperList(String menuId) {
		ParamRequest pageRequest = new ParamRequest();
		Map<String, Object> equalFilters = new LinkedHashMap<String, Object>();
		equalFilters.put("menuid", Integer.parseInt(menuId));
		pageRequest.setEqualFilters(equalFilters);
		pageRequest.setPageSize(20);
		Page<CfgUmsMenuOper> pages = menuOperService.findAllForPage(pageRequest);
		return pages;
	}

	/**
	 * 
	 * @Title: saveOrUpdateMenuOper
	 * @Description: (修改与更新菜单操作)
	 * @param menuOper
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author spongebob
	 */
	@RequestMapping("saveOrUpdateMenuOper")
	@ResponseBody
	public ResultModel saveOrUpdateMenuOper(CfgUmsMenuOper menuOper) {
		try {
			menuOperService.saveOrUpdateMenuOper(menuOper);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.success("保存失败");
		}
		return ResultModel.success("common.save.success");
	}

	/**
	 * 
	 * @Title: deleteMenuOper
	 * @Description: (删除指定的菜单操作)
	 * @param menuOperId
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author spongebob
	 */
	@RequestMapping("deleteMenuOper")
	@ResponseBody
	public ResultModel deleteMenuOper(String menuOperId) {
		try {
			menuOperService.delete(Integer.parseInt(menuOperId));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.success("删除失败");
		}
		return ResultModel.success("删除成功");
	}

	/**
	 * 
	 * @Title: quickEntry
	 * @Description: (跳转到自定义快捷入口页面)
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author HuJingqiang
	 */
	@RequestMapping("addQuickEntry")
	public String addQuickEntry(HttpServletRequest request) {
		return "system/menu/addQuickEntry";
	}
	
	/**
	 * 
	 * @Title: saveQuickEntry
	 * @Description: (保存快捷入口信息)
	 * @param quickMenuIds
	 *            实体信息
	 * @param request
	 * @param response
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author HuJingqiang
	 */
	@RequestMapping("saveQuickEntry")
	@ResponseBody
	public ResultModel saveQuickEntry(Integer[] quickMenuIds, HttpServletRequest request, HttpServletResponse response) {
		try {
			//先清空用户在表中的数据
			topMenuService.deleteByUser(userService.getCurrent());
			//遍历添加用户对应的快捷入口菜单
			for (Integer id : quickMenuIds) {
				CfgUmsUser currentUser = userService.getCurrent();
				CfgTopmenuUser cfgTopMenuUser = new CfgTopmenuUser();
				CfgUmsMenu cfgUmsMenu = new CfgUmsMenu();
				cfgUmsMenu.setId(id);
				cfgTopMenuUser.setCfgUmsUser(currentUser);
				cfgTopMenuUser.setCfgUmsMenu(cfgUmsMenu);
				topMenuService.save(cfgTopMenuUser);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.save.fail");
		}

		return ResultModel.success("common.save.success");
	}
	
	/**
	 * 选择图标
	 * @return
	 */
	@RequestMapping("chooseIcon")
	public String chooseIcon(HttpServletRequest request, String icon){
		String filePath = request.getSession().getServletContext().getRealPath("resources/admin/img/menuicon");
		File file = new File(filePath);
		String[] fileList = file.list();
		List<String> menuList = new ArrayList<String>();
		List<String> topList = new ArrayList<String>();
		if(icon==null) icon="";
		for(int i=0; i<fileList.length; i++){
			if(icon.equals(fileList[i])){
				request.setAttribute("icon", icon.replace(".png", ""));
			}
			if(fileList[i].indexOf("ico_m_")>-1||fileList[i].indexOf("ico2_m_")>-1){
				menuList.add(fileList[i].replace(".png",""));
			}
			if(fileList[i].indexOf("nav_")>-1){
				topList.add(fileList[i].replace(".png",""));
			}
		}
		request.setAttribute("menuList", menuList);
		request.setAttribute("topList", topList);
		return "system/menu/iconList";
	}
}
