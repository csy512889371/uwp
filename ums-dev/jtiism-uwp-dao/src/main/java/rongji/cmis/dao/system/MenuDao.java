package rongji.cmis.dao.system;

import rongji.cmis.model.ums.CfgUmsMenu;
import rongji.framework.base.dao.GenericDao;

import java.util.List;

public interface MenuDao extends GenericDao<CfgUmsMenu> {

	/**
	 * 
	 * @Title: findMenuByMenuIds
	 * @Description: (这里用一句话描述这个方法的作用)
	 * @param menuIds
	 * @return
	 * @return List<CfgUmsMenu> 返回类型
	 * @throws
	 * @author spongebob
	 */
	 List<CfgUmsMenu> findMenuByMenuIds(List<Integer> menuIds);

	 CfgUmsMenu findMenuByCode(String menuCode);

	//  List<Map<String, Object>> findMenuMap();

}
