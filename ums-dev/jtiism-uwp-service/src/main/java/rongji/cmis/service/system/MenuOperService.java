package rongji.cmis.service.system;

import rongji.cmis.model.ums.CfgUmsMenuOper;
import rongji.framework.base.service.BaseService;

import java.util.List;
import java.util.Map;

public interface MenuOperService extends BaseService<CfgUmsMenuOper> {

	/**
	 * 
	 * @Title: delteByMenuId
	 * @Description: (删除menuId的所有权限)
	 * @param menuId
	 * @return void 返回类型
	 * @throws
	 * @author Administrator
	 */
	public void deleteByMenuId(Integer menuId);


	/**
	 * 
	 * @Title: saveOrUpdateMenuOper
	 * @Description: (这里用一句话描述这个方法的作用)
	 * @param menuOper
	 * @return void 返回类型
	 * @throws
	 * @author spongebob
	 */
	public void saveOrUpdateMenuOper(CfgUmsMenuOper menuOper);

	/**
	 * 
	 * @Title: findByMenuId
	 * @Description: (这里用一句话描述这个方法的作用)
	 * @param menuId
	 * @return
	 * @return List<Map<String,Object>> 返回类型
	 * @throws
	 * @author spongebob
	 */
	public List<Map<String, Object>> findByMenuId(Integer menuId);

	/**
	 * @param entId
	 * @return
	 */
	List<String> getCurrentUserMenuOperRight(String entId,String fromType);
}
