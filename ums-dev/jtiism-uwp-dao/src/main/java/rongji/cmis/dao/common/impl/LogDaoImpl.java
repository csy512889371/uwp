package rongji.cmis.dao.common.impl;

import org.hibernate.FlushMode;
import org.springframework.stereotype.Repository;

import rongji.cmis.dao.common.LogDao;
import rongji.cmis.model.ums.CfgUmsLog;
import rongji.framework.base.dao.impl.GenericDaoImpl;

/**
 * Dao - 日志
 * 
 * @version 1.0
 */
@Repository("logDaoImpl")
public class LogDaoImpl extends GenericDaoImpl<CfgUmsLog> implements LogDao {

	public void removeAll() {
		String jpql = "delete from Log log";
		this.getSession().createQuery(jpql).setFlushMode(FlushMode.COMMIT).executeUpdate();
	}
	
}