package rongji.cmis.service.common;

import java.util.List;

import rongji.cmis.model.sys.LogComputeRec;
import rongji.cmis.model.ums.CfgUmsLog;
import rongji.framework.base.service.BaseService;

/**
 * Service - 日志
 * 
 * @version 1.0
 */
public interface LogComputeRecService extends BaseService<LogComputeRec> {

	void insertLogRc(LogComputeRec logRc);

	void changeToLog(List<CfgUmsLog> logLists, List<LogComputeRec> logRcList);

}