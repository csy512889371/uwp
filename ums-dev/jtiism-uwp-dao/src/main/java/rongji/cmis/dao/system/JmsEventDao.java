package rongji.cmis.dao.system;

import rongji.cmis.model.sys.JmsEvent;
import rongji.framework.base.dao.GenericDao;

import java.util.Date;
import java.util.List;

public interface JmsEventDao extends GenericDao<JmsEvent> {

    List findEventList(String topic, Date startDate, Date endDate);

}
