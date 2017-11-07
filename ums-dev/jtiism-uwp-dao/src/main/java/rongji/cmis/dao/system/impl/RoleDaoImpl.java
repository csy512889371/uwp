package rongji.cmis.dao.system.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import rongji.cmis.dao.system.RoleDao;
import rongji.cmis.model.ums.CfgUmsRole;
import rongji.framework.base.dao.impl.GenericDaoImpl;

@Repository
public class RoleDaoImpl extends GenericDaoImpl<CfgUmsRole> implements RoleDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<CfgUmsRole> findRoleByUser(Integer userId) {
		String hql = "from CfgUmsRole where id in(select roleid from CfgUmsRoleUser where userId=:userId)";
		Query query = this.getSession().createQuery(hql);
		query.setInteger("userId", userId);
		List<CfgUmsRole> roleList = query.list();
		return roleList;
	}
}
