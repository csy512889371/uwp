package rongji.cmis.dao.system;

import rongji.cmis.model.ums.CfgUmsMenuOper;
import rongji.framework.base.dao.GenericDao;

import java.util.List;
import java.util.Set;

 public interface MenuOperDao extends GenericDao<CfgUmsMenuOper> {

	/**
	 * 
	 * @Title: delteByMenuId
	 * @Description: (删除menuId的所有操作)
	 * @param menuId
	 * @return void 返回类型
	 * @throws
	 * @author Administrator
	 */
	 void deleteByMenuId(Integer menuId);
	
	
	/**
	 * 根据id获取 权限字符串
	 */
	List<String> queryPermissionsByIds(Set<Integer> operIds, String operAuthCode);

	List getUserEntMenuOperRight(String userId, String entId);

    List getRoleEntMenuOperRight(String roleId, String entId);
}
