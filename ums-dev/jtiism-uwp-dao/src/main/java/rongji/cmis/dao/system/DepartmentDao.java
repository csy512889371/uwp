package rongji.cmis.dao.system;

import rongji.cmis.model.unit.CmisDepartment;
import rongji.framework.base.dao.GenericDao;

public interface DepartmentDao extends GenericDao<CmisDepartment> {

	/**
	 * 
	 * @Title: saveDeptOrder
	 * @Description: 保存管理权限排序
	 * @param deptId
	 * @param orderNum
	 * @return void 返回类型
	 * @throws
	 * @author Administrator
	 */
	 void saveDeptOrder(String deptId, String orderNum);

	
	 long isCodeExists(String id);
	
	 void updateDepts(String code,String deptname,String description);
}
