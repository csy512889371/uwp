package rongji.cmis.service.system;

import rongji.cmis.model.ums.UserDeptRela;
import rongji.cmis.model.unit.CmisDepartment;
import rongji.framework.base.service.BaseService;

public interface UserDeptRelaService extends BaseService<UserDeptRela> {

	/**
	 * 
	 * @Title: saveDeptRela
	 * @Description: (保存分配管理权限)
	 * @param deptId
	 * @param userId
	 * @return void 返回类型
	 * @throws
	 * @author Administrator
	 */
	void saveDeptRela(String deptId, Integer userId);

	public CmisDepartment findDeptByUserId(Integer userId);
}
