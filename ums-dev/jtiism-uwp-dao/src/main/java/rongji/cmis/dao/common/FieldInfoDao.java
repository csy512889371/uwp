package rongji.cmis.dao.common;

import java.util.List;

import rongji.cmis.model.sys.SysFieldInfo;
import rongji.framework.base.dao.GenericDao;


/**
 * Dao - 表字段 对应的数据字典
 * 
 * @version 1.0
 */
public interface FieldInfoDao extends GenericDao<SysFieldInfo> {
	
	List<String> findCodeTableDist();
		
}