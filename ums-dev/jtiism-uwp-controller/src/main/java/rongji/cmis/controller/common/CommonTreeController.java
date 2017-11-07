package rongji.cmis.controller.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import rongji.cmis.model.ums.CfgUmsMenu;
import rongji.cmis.model.ums.CfgUmsRole;
import rongji.cmis.service.system.DepartmentService;
import rongji.cmis.service.system.MenuService;
import rongji.cmis.service.system.RoleMenuOperService;
import rongji.cmis.service.system.RoleService;
import rongji.cmis.service.system.UserService;
import rongji.framework.base.model.ResultModel;
import rongji.framework.common.web.controller.BaseController;
import rongji.framework.util.Constant;
import rongji.framework.util.FastjsonUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成各类型树
 *
 * @author kemiaoxin
 */
@Controller("commonTreeController")
@RequestMapping("commonTree")
public class CommonTreeController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(CommonTreeController.class);
    @Resource(name = "menuServiceImpl")
    MenuService menuService;

    @Resource(name = "roleMenuOperServiceImpl")
    RoleMenuOperService roleMenuOperService;

    @Resource(name = "roleServiceImpl")
    RoleService roleService;

    @Resource(name = "userServiceImpl")
    UserService userService;

    @Resource(name = "departmentServiceImpl")
    DepartmentService departmentService;

    /**
     * 获取角色菜单信息
     *
     * @param roleId  角色ID
     * @param request
     * @return
     */
    @RequestMapping("getMultyMenuTree")
    public String getMultyMenuTree(Integer roleId, HttpServletRequest request) {
        List<Map<String, Object>> menuList = menuService.findMenuMap();
        List<Map<String, Object>> roleMenuList = roleMenuOperService.findByRoleId(roleId);
        Map<String, Object> map = new HashMap<>();
        for (Map<String, Object> roleMenu : roleMenuList) {
            Object menuId = roleMenu.get("menuid");
            map.put(String.valueOf(menuId), menuId);
        }
        for (Map<String, Object> menu : menuList) {
            String id = String.valueOf(menu.get("id"));
            if (map.get(id) != null) {
                menu.put("checked", "true");
            }
        }
        request.setAttribute("menuList", FastjsonUtils.toJson(menuList));
        request.setAttribute("roleId", roleId);
        return "system/role/roleMenuOper";
    }

    /**
     * @param roleId
     * @param menuIds
     * @param request
     * @param response
     * @return ResultModel 返回类型
     * @throws
     * @Title: grantRoleMenu
     * @Description: (为角色分配菜单项)
     * @author spongebob
     */
    @ResponseBody
    @RequestMapping("grantRoleMenu")
    public ResultModel grantRoleMenu(Integer roleId, Integer[] menuIds, HttpServletRequest request, HttpServletResponse response) {
        try {
            roleMenuOperService.grantRoleMenu(roleId, menuIds);
            Integer userId = userService.getCurrentUserId();

            // 获取角色信息 查询页面
            List<CfgUmsMenu> menuList = new ArrayList<CfgUmsMenu>();
            List<CfgUmsRole> roleList = roleService.findRoleByUser(userId);
            if (roleList != null && roleList.size() > 0) {
                // 获取菜单权限
                menuList = menuService.findMenuByRoles(roleList);
                request.getSession().setAttribute(Constant.USERMENU, menuList);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultModel.error("分配失败！请与管理员联系！");
        }
        return ResultModel.success("分配成功！");
    }


    @RequestMapping("getAllTreePage")
    public String getAllTreePage(int type, HttpServletRequest request) {

        if (type == 1) {// 单选树
            List<Map<String, Object>> list = menuService.findMenuMap();
            request.setAttribute("menuList", FastjsonUtils.toJson(list));
            return "common/menuSingleTree";

        } else if (type == 2) {// 多选树
            List<Map<String, Object>> list = menuService.findMenuMap();
            request.setAttribute("menuList", FastjsonUtils.toJson(list));
            return "common/menuMultyTree";
        }
        return "";
    }
}
