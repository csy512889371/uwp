package rongji.cmis.dao.system.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import rongji.cmis.dao.system.JmsEventDao;
import rongji.cmis.model.sys.JmsEvent;
import rongji.framework.base.dao.impl.GenericDaoImpl;

import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;

@Repository("jmsEventDaoImpl")
public class JmsEventDaoImpl extends GenericDaoImpl<JmsEvent> implements JmsEventDao {


    @Override
   /* public List findEventList(String topic, Date startDate, Date endDate) {
        if (topic == null) {
            return null;
        }
        try {
            String jpql = "select jmsEvent from JmsEvent jmsEvent where jmsEvent.topic=:topic and createTime  BETWEEN  :startDate AND   :endDate  ";
            Query query = this.getSession().createQuery(jpql);
            query.setParameter("topic", topic);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.list();
        } catch (NoResultException e) {
            return null;
        }
    }*/


    public List findEventList(String topic, Date startDate, Date endDate) {
        if (topic == null) {
            return null;
        }
        try {
            String jpql = "select jmsEvent from JmsEvent jmsEvent where jmsEvent.topic=:topic ";
            Query query = this.getSession().createQuery(jpql);
            query.setParameter("topic", topic);
            return query.list();
        } catch (NoResultException e) {
            return null;
        }
    }

}
