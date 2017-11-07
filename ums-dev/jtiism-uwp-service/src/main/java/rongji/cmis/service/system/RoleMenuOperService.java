package rongji.cmis.service.system;

import java.util.List;
import java.util.Map;

import rongji.cmis.model.ums.CfgUmsRoleMenuOper;
import rongji.framework.base.service.BaseService;

public interface RoleMenuOperService extends BaseService<CfgUmsRoleMenuOper> {

	/**
	 * 
	 * @Title: deleteRoleMenuByRoleId
	 * @Description: (根据角色ID删除已经分配的菜单)
	 * @param roleId
	 * @return void 返回类型
	 * @throws
	 * @author Administrator
	 */
	public void deleteByRoleId(Integer roleId);

	/**
	 * 
	 * @Title: grantRoleMenu
	 * @Description: (为角色分配菜单项)
	 * @param roleId
	 * @param menuIds
	 * @return void 返回类型
	 * @throws
	 * @author spongebob
	 */
	public void grantRoleMenu(Integer roleId, Integer[] menuIds);

	/**
	 * 
	 * @Title: grantRoleMenuOper
	 * @Description: (为角色分配菜单操作项)
	 * @param roleId
	 * @param menuId
	 * @param checkOperId
	 * @return void 返回类型
	 * @throws
	 * @author spongebob
	 */
	public void grantRoleMenuOper(Integer roleId, Integer menuId, Integer[] checkOperIds);

	/**
	 * 
	 * @Title: deleteByMenuId
	 * @Description: (将所有菜单id为menuid的项删除)
	 * @param menuId
	 * @return void 返回类型
	 * @throws
	 * @author spongebob
	 */
	public void deleteByMenuId(Integer menuId);

	/**
	 * 
	 * @Title: findByMenuId
	 * @Description: (这里用一句话描述这个方法的作用)
	 * @param menuId
	 * @return
	 * @return List<CfgUmsRoleMenuOper> 返回类型
	 * @throws
	 * @author spongebob
	 */
	public List<CfgUmsRoleMenuOper> findByMenuId(Integer menuId);

	/**
	 * 
	 * @Title: findByRoleId
	 * @Description: (这里用一句话描述这个方法的作用)
	 * @param roleId
	 * @return
	 * @return List<Map<String,Object>> 返回类型
	 * @throws
	 * @author spongebob
	 */
	public List<Map<String, Object>> findByRoleId(Integer roleId);

}
