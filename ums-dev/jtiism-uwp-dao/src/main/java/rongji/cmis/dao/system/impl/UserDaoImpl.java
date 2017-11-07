package rongji.cmis.dao.system.impl;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.FlushMode;
import org.springframework.stereotype.Repository;

import rongji.cmis.dao.system.UserDao;
import rongji.cmis.model.ums.CfgUmsUser;
import rongji.framework.base.dao.impl.GenericDaoImpl;

@Repository
public class UserDaoImpl extends GenericDaoImpl<CfgUmsUser> implements UserDao {


	@Override
	public List<CfgUmsUser> checkLoginUser(CfgUmsUser user) {
		String hql = "from CfgUmsUser where username='" + user.getUsername() + "' and password='" + user.getPassword() + "'";
		List<CfgUmsUser> userList = this.findByHQL(hql);
		return userList;
	}

	@Override
	public CfgUmsUser findByUsername(String username) {
		if (username == null) {
			return null;
		}
		try {
			String jpql = "select user from CfgUmsUser user where lower(user.username) = lower(:username)";
			return (CfgUmsUser)this.getSession().createQuery(jpql).setFlushMode(FlushMode.COMMIT).setParameter("username", username).uniqueResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
