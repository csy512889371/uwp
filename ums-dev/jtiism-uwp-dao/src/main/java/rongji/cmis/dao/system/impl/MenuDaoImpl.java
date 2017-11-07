package rongji.cmis.dao.system.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import rongji.cmis.dao.system.MenuDao;
import rongji.cmis.model.ums.CfgUmsMenu;
import rongji.framework.base.dao.impl.GenericDaoImpl;

@Repository
public class MenuDaoImpl extends GenericDaoImpl<CfgUmsMenu> implements MenuDao {

	// @SuppressWarnings("unchecked")
	// @Override
	// public List<Map<String, Object>> findMenuMap() {
	// String hql =
	// "select new map(cfg.id as id,cfg.title as title,cfg.menutype as menutype,cfg.url as url,cfg.code as code,"
	// +
	// "cfg.state as state,cfg.sort as sort,cfg.remark as remark,cfg.parent.id as parentid) from CfgUmsMenu cfg";
	// return this.getSession().createQuery(hql).list();
	// }

	@SuppressWarnings("unchecked")
	@Override
	public List<CfgUmsMenu> findMenuByMenuIds(List<Integer> menuIds) {
		String hql = "from CfgUmsMenu where id in(:menuIds) and state=1 order by parentId , sort";
		Query query = this.getSession().createQuery(hql);
		query.setParameterList("menuIds", menuIds);
		List<CfgUmsMenu> menuList = (List<CfgUmsMenu>) query.list();
		return menuList;
	}

	@Override
	public CfgUmsMenu findMenuByCode(String menuCode) {
		String hql = "from CfgUmsMenu where code = :menuCode";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("menuCode", menuCode);
		return (CfgUmsMenu) query.list().get(0);
	}

}
