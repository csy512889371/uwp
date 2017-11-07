package rongji.cmis.dao.system.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import rongji.cmis.dao.system.RoleUserDao;
import rongji.cmis.model.ums.CfgUmsRoleUser;
import rongji.framework.base.dao.impl.GenericDaoImpl;

@Repository
public class RoleUserDaoImpl extends GenericDaoImpl<CfgUmsRoleUser> implements RoleUserDao {

	@Override
	public List<CfgUmsRoleUser> findByUserId(Integer userId) {
		return this.findByProperty("USERID", userId);
	}

	@Override
	public void deleteRoleUserByUserId(Integer userId) {
		List<CfgUmsRoleUser> roleUserList = this.findByProperty("userid", userId);
		for (CfgUmsRoleUser cfgUmsRoleUser : roleUserList) {
			this.delete(cfgUmsRoleUser.getId());
		}

	}

	@Override
	public void deleteRoleUserByRoleId(Integer roleId) {
		List<CfgUmsRoleUser> roleUserList = this.findByProperty("roleid", roleId);
		for (CfgUmsRoleUser cfgUmsRoleUser : roleUserList) {
			this.delete(cfgUmsRoleUser.getId());
		}

	}

	@Override
	public List<CfgUmsRoleUser> findRoleByUserId(Integer userId) {
		return this.findByProperty("userid", userId);
	}

	/**
	 * @Description: (根据角色ID查询其配置的用户)
	 * @Title: findUserByRoleId
	 * @param roleId
	 * @return
	 * @return List<CfgUmsRoleUser>    返回类型
	 * @throws
	 * @author LinJH 2016-4-19
	 */
	@Override
	public List<CfgUmsRoleUser> findUserByRoleId(Integer roleId) {
		return this.findByProperty("roleid", roleId);
	}

}
