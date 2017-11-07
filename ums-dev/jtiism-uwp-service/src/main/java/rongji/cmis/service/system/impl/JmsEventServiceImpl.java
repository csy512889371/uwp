package rongji.cmis.service.system.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rongji.cmis.dao.system.JmsEventDao;
import rongji.cmis.model.sys.JmsEvent;
import rongji.cmis.service.system.JmsEventService;
import rongji.framework.base.service.impl.BaseServiceImpl;
import rongji.framework.util.Constant;
import rongji.framework.util.DateUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("jmsEventServiceImpl")
public class JmsEventServiceImpl extends BaseServiceImpl<JmsEvent> implements JmsEventService {
    private static final Logger logger = LoggerFactory.getLogger(JmsEventServiceImpl.class);


    @Resource
    private
    JmsEventDao jmsEventDao;




    /**
     * 按topic 查询待处理事件列表
     *
     * @param topic
     * @return
     */
    @Override
    public List<JmsEvent> findEventList(String topic) {
        Date nowDate = new Date();
        return jmsEventDao.findEventList(topic, DateUtil.getOffsetMinute(nowDate, -Constant.SEND_EVENT_MSG_INTERVAL_MINUTE), nowDate);
    }


}
