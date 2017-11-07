package rongji.cmis.dao.system;

import java.util.List;

import rongji.cmis.model.ums.CfgUmsRole;
import rongji.framework.base.dao.GenericDao;

public interface RoleDao extends GenericDao<CfgUmsRole> {

	/**
	 * 
	 * @Title: findRoleByUser
	 * @Description: (根据用户信息查找角色)
	 * @param user
	 * @return
	 * @return List<CfgUmsRole> 返回类型
	 * @throws
	 * @author Administrator
	 */
	public List<CfgUmsRole> findRoleByUser(Integer userId);

}
