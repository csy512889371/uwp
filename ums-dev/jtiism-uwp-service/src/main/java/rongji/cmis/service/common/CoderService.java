package rongji.cmis.service.common;

import java.util.List;
import java.util.Map;

import rongji.cmis.model.sys.SysCoder;
import rongji.framework.base.service.BaseService;

/**
 * 
 * @Title: 系统数据字典 service
 * @version V1.0
 */
public interface CoderService extends BaseService<SysCoder> {

	List<SysCoder> findListByFieldInfo(String tabName, String colName);

	Map<String, SysCoder> findMapByFieldInfo(String tabName, String colName);

}
