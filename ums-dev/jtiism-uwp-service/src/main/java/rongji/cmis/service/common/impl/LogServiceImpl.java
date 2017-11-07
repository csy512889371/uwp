package rongji.cmis.service.common.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import rongji.cmis.dao.common.LogDao;
import rongji.cmis.model.ums.CfgUmsLog;
import rongji.cmis.service.common.LogService;
import rongji.framework.base.service.impl.BaseServiceImpl;

/**
 * Service - 日志
 * 
 * @version 1.0
 */
@Service("logServiceImpl")
public class LogServiceImpl extends BaseServiceImpl<CfgUmsLog> implements LogService {

	@Resource(name = "logDaoImpl")
	private LogDao logDao;

	public void clear() {
		logDao.removeAll();
	}

	@Override
	public void log(CfgUmsLog log) {
		this.save(log);
	}

}