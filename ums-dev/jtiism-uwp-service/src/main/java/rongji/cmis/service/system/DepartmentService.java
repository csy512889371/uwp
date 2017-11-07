package rongji.cmis.service.system;

import java.util.Map;

import rongji.cmis.model.unit.CmisDepartment;
import rongji.framework.base.service.BaseService;

public interface DepartmentService extends BaseService<CmisDepartment> {

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
	public void saveDeptOrder(String deptId, String orderNum);


	public Map<String, Object> getCadreDeptOperMap();
	
	public boolean isCodeExists(String code);
	
	public void updateDepts(String code,String deptname,String description);
	
}
