package rongji.cmis.service.common.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import rongji.cmis.dao.common.LogComputeRecDao;
import rongji.cmis.dao.common.LogDao;
import rongji.cmis.model.sys.LogComputeRec;
import rongji.cmis.model.ums.CfgUmsLog;
import rongji.cmis.service.common.LogComputeRecService;
import rongji.framework.base.service.impl.BaseServiceImpl;

/**
 * Service - 日志
 * 
 * @version 1.0
 */
@Service("logComputeRecServiceImpl")
public class LogComputeRecServiceImpl extends BaseServiceImpl<LogComputeRec> implements LogComputeRecService {

	@Resource(name = "logComputeRecDaoImpl")
	private LogComputeRecDao logComputeRecDao;

	@Resource(name = "logDaoImpl")
	private LogDao logDao;

	@Override
	public void insertLogRc(LogComputeRec logRc) {
		logComputeRecDao.save(logRc);
	}

	@Override
	public void changeToLog(List<CfgUmsLog> logLists, List<LogComputeRec> logRcList) {
		for (CfgUmsLog log : logLists) {
			logDao.save(log);
		}
		for (LogComputeRec logRc : logRcList) {
			logComputeRecDao.delete(logRc);
		}
	}

}