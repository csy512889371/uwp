package rongji.cmis.dao.system.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import rongji.cmis.dao.system.MsgRemindDao;
import rongji.cmis.model.sys.MsgRemind;
import rongji.cmis.model.sys.MsgRemind.MsgType;
import rongji.framework.base.dao.impl.GenericDaoImpl;

@Repository("msgRemindDaoImpl")
public class MsgRemindDaoImpl extends GenericDaoImpl<MsgRemind> implements MsgRemindDao {

	@SuppressWarnings("unchecked")
	@Override
	public MsgRemind findByMsgTypeAndParam(MsgType msgType, String param) {
		String hql = " FROM MsgRemind MSG WHERE MSG.msgType =:msgType AND MSG.msgRemindId =:param ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("msgType", msgType);
		query.setParameter("param", param);
		List<MsgRemind> list = query.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MsgRemind> findTop(Integer maxNum, Integer userId) {
		String hql = " FROM MsgRemind MSG WHERE MSG.msgStatus = 0 AND MSG.cfgUmsUser.id =:userId ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("userId", userId);
		query.setFirstResult(0);
		query.setMaxResults(maxNum);
		return query.list();
	}

	@Override
	public Integer findMsgNumber(Integer userId) {
		String hql = "SELECT COUNT(*) FROM MsgRemind MSG WHERE MSG.msgStatus = 0 AND MSG.cfgUmsUser.id =:userId ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("userId", userId);
		return Integer.parseInt(query.list().get(0).toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public MsgRemind findByMsgType(MsgType msgType, Integer userId, String title) {
		String hql = " FROM MsgRemind MSG WHERE MSG.msgType =:msgType AND MSG.cfgUmsUser.id =:userId AND MSG.title =:title ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("msgType", msgType);
		query.setParameter("userId", userId);
		query.setParameter("title", title);
		List<MsgRemind> list = query.list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		
		return null;
	}

	@Override
	public void deleteMsgRemindByUserId(Integer userId) {
		String hql = "DELETE FROM MsgRemind AS msgRemind WHERE msgRemind.cfgUmsUser.id = :userId";
		Query query = this.getSession().createQuery(hql);
		query.setInteger("userId", userId);
		query.executeUpdate();
	}

}
