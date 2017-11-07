package rongji.cmis.dao.system;

import rongji.cmis.model.ums.CfgUmsRoleUser;
import rongji.framework.base.dao.GenericDao;

import java.util.List;

public interface RoleUserDao extends GenericDao<CfgUmsRoleUser> {

	/**
	 *
	 * @Title: findByUserId
	 * @Description: (根据用户ID查询是否分配角色)
	 * @param userId
	 * @return
	 * @return CfgUmsRoleUser 返回类型
	 * @throws
	 * @author Administrator
	 */
	 List<CfgUmsRoleUser> findByUserId(Integer userId);

	/**
	 *
	 * @Title: deleteRoleUserByUserId
	 * @Description: (通过用户ID删除分配角色信息)
	 * @param userId
	 * @return void 返回类型
	 * @throws
	 * @author Administrator
	 */
	public void deleteRoleUserByUserId(Integer userId);

	/**
	 *
	 * @Title: deleteRoleUserByRoleId
	 * @Description: (通过角色ID删除分配用户信息)
	 * @param roleId
	 * @return void 返回类型
	 * @throws
	 * @author Administrator
	 */
	public void deleteRoleUserByRoleId(Integer roleId);

	/**
	 *
	 * @Title: findRoleByUserId
	 * @Description: (根据用户ID查询其拥有的角色)
	 * @param userId
	 * @return
	 * @return List<CfgUmsRole> 返回类型
	 * @throws
	 * @author Administrator
	 */
	public List<CfgUmsRoleUser> findRoleByUserId(Integer userId);

	/**
	 * @Description: (根据角色ID查询其配置的用户)
	 * @Title: findUserByRoleId
	 * @param roleId
	 * @return
	 * @return List<CfgUmsRoleUser>    返回类型
	 * @throws
	 * @author LinJH 2016-4-19
	 */
	public List<CfgUmsRoleUser> findUserByRoleId(Integer roleId);
}
