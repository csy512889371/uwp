package rongji.cmis.dao.system.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import rongji.cmis.dao.system.RoleMenuOperDao;
import rongji.cmis.model.ums.CfgUmsRoleMenuOper;
import rongji.framework.base.dao.impl.GenericDaoImpl;

@Repository
public class RoleMenuOperDaoImpl extends GenericDaoImpl<CfgUmsRoleMenuOper> implements RoleMenuOperDao {

	// @Override
	// public List<CfgUmsRoleMenuOper> findByRoleId(Integer roleId) {
	// String hql =
	// "select * from CfgUmsRoleMenuOper rmo where  1=1 and rmo.role.id="+
	// roleId;
	// return this.find(hql, null, null);
	//
	// }

	@Override
	public void deleteByRoleId(Integer roleId) {
		String hql = "delete from CfgUmsRoleMenuOper rmo where 1=1 and rmo.role.id=:roleId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roleId", roleId);
		query.executeUpdate();
	}

	@Override
	public void deleteByMenuId(Integer menuId) {
		String hql = "delete from CfgUmsRoleMenuOper where 1=1 and menuid=" + menuId;
		Query query = this.getSession().createQuery(hql);
		query.executeUpdate();
	}

}
