package rongji.cmis.service.system;

import rongji.cmis.model.sys.JmsEvent;
import rongji.framework.base.service.BaseService;

import java.util.List;

public interface JmsEventService extends BaseService<JmsEvent> {

    List<JmsEvent> findEventList(String topic);
}
