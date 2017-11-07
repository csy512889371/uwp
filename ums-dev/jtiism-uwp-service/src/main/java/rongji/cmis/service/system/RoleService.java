package rongji.cmis.service.system;

import java.util.List;
import java.util.Map;

import rongji.cmis.model.ums.CfgUmsRole;
import rongji.framework.base.service.BaseService;

public interface RoleService extends BaseService<CfgUmsRole> {

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

	/**
	 * @Description: (删除角色)
	 * @Title: deleteByRoleId
	 * @param roleId
	 * @return void    返回类型
	 * @throws
	 * @author LinJH 2016-4-19
	 */
	public void deleteByRoleId(Integer roleId);

	public Map<String, Object> getRoleCadreInfoByUserRole(Integer infoType,String fromType);


}
