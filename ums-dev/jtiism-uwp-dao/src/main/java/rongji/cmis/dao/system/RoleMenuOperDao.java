package rongji.cmis.dao.system;

import rongji.cmis.model.ums.CfgUmsRoleMenuOper;
import rongji.framework.base.dao.GenericDao;

public interface RoleMenuOperDao extends GenericDao<CfgUmsRoleMenuOper> {

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
	 * @Title: deleteByMenuId
	 * @Description: (将所有菜单id为menuid的项删除)
	 * @param menuId
	 * @return void 返回类型
	 * @throws
	 * @author spongebob
	 */
	public void deleteByMenuId(Integer menuId);

	// public List<CfgUmsRoleMenuOper> findByRoleId(Integer roleId);

}
