package rongji.cmis.service.common;

import rongji.cmis.model.ums.CfgUmsLog;
import rongji.framework.base.service.BaseService;

/**
 * Service - 日志
 * 
 * @version 1.0
 */
public interface LogService extends BaseService<CfgUmsLog> {

	/**
	 * 清空日志
	 */
	void clear();
	
	void log(CfgUmsLog log);

}