package rongji.cmis.dao.system;

import rongji.cmis.model.ums.RoleDeptReal;
import rongji.framework.base.dao.GenericDao;

public interface RoleDeptDao extends GenericDao<RoleDeptReal> {
	/**
	 * 
	 * @Title: deleteRoleDeptByRoleId
	 * @Description: 根据角色ID删除角色管理权限中间表数据
	 * @param roleId
	 * @return void 返回类型
	 * @throws
	 * @author LFG
	 */
	void deleteRoleDeptByRoleId(String roleId);
	
	/**
	 * 
	 * @Title: findDeptByRoleIds
	 * @Description: (根据角色ID查询其拥有的管理权限)
	 * @param roleIdArr
	 * @return
	 * @return Integer[] 返回类型
	 * @throws
	 * @author Administrator
	 */
	String[] findDeptByRoleIds(Integer[] roleIdArr);

	String[] findDeptByRoleId(Integer roleId);

}
