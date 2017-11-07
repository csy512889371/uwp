package rongji.cmis.service.system;

import rongji.cmis.model.ums.RoleDeptReal;
import rongji.framework.base.service.BaseService;
import rongji.redis.core.annotations.RedisCacheEvict;

public interface RoleDeptService extends BaseService<RoleDeptReal> {

	@RedisCacheEvict(key="adminUserEnt" ,entId="")
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
	public String[] findDeptByRoleIds(Integer[] roleIdArr);

	public String[] findDeptByRoleId(Integer roleId);

}
