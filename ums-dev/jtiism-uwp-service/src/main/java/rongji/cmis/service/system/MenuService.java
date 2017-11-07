package rongji.cmis.service.system;

import java.util.List;
import java.util.Map;

import rongji.cmis.model.ums.CfgUmsMenu;
import rongji.cmis.model.ums.CfgUmsRole;
import rongji.framework.base.model.ResultModel;
import rongji.framework.base.service.BaseService;

public interface MenuService extends BaseService<CfgUmsMenu> {


	/**
	 * 
	 * @Title: findMenuByRole
	 * @Description: (根据角色信息查找菜单)
	 * @param role
	 * @return
	 * @return List<CfgUmsMenu> 返回类型
	 * @throws
	 * @author Administrator
	 */
	public List<CfgUmsMenu> findMenuByRole(CfgUmsRole role);

	/**
	 * 
	 * @Title: findMenuMap
	 * @Description: (这里用一句话描述这个方法的作用)
	 * @return
	 * @return List<Map<String,Object>> 返回类型
	 * @throws
	 * @author spongebob
	 */
	public List<Map<String, Object>> findMenuMap();

	/**
	 * 
	* @Title: deleteAfterChecked
	* @Description: (先确定该菜单不被其他角色授权及菜单下没有其它子目录方可删除)
	* @param menuId
	* @return void    返回类型
	* @throws
	* @author spongebob
	 */
	public ResultModel deleteAfterChecked(Integer menuId);

	public CfgUmsMenu findMenuByCode(String menuCode);
	
	List<CfgUmsMenu> findMenuByRoles(List<CfgUmsRole> roleList);

}
