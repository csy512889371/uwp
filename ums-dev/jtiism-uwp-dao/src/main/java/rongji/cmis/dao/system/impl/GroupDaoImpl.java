package rongji.cmis.dao.system.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import rongji.cmis.dao.system.GroupDao;
import rongji.cmis.model.ums.CfgUmsGroup;
import rongji.framework.base.dao.impl.GenericDaoImpl;

@Repository
public class GroupDaoImpl extends GenericDaoImpl<CfgUmsGroup> implements GroupDao {

	@Override
	public Integer getLastSeqno() {
		String hql = "select max(seqno) from CfgUmsGroup ";
		Query query = this.getSession().createQuery(hql);
		Integer seqno = (Integer) query.uniqueResult();
		if (seqno == null) {
			seqno = 1;
		}
		return seqno + 1;
	}

}
