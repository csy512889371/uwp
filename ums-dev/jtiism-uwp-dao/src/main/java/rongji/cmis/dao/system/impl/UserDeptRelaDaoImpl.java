package rongji.cmis.dao.system.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import rongji.cmis.dao.system.UserDeptRelaDao;
import rongji.cmis.model.ums.UserDeptRela;
import rongji.framework.base.dao.impl.GenericDaoImpl;

@Repository("userDeptRelaDaoImpl")
public class UserDeptRelaDaoImpl extends GenericDaoImpl<UserDeptRela> implements UserDeptRelaDao {

	@Override
	public void deleteByUserId(Integer id) {
		String hql = "delete from UserDeptRela where cfgUmsUser.id = :id";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

}
