package rongji.cmis.dao.common;

import rongji.cmis.model.ums.CfgUmsLog;
import rongji.framework.base.dao.GenericDao;


/**
 * Dao - 日志
 * 
 * @version 1.0
 */
public interface LogDao extends GenericDao<CfgUmsLog> {

	/**
	 * 删除所有日志
	 */
	void removeAll();

}