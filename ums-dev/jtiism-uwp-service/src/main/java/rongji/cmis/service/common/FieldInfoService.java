package rongji.cmis.service.common;

import java.util.List;

import rongji.cmis.model.sys.SysFieldInfo;
import rongji.framework.base.service.BaseService;

/**
 * Service - 表字段 对应的数据字典 codeTable
 * 
 * @version 1.0
 */
public interface FieldInfoService extends BaseService<SysFieldInfo> {

	/**
	 * 
	 * @Description: 根据codeTable和code 获取对应的FieldInfo
	 * 
	 * @param codeTable
	 * 
	 * @param code
	 * 
	 * @return
	 */
	SysFieldInfo findFieldInfo(String srcTable, String srcField);

	/**
	 * @Description: 获取 CodeTable的distinct后的唯一值
	 * @return
	 */
	List<String> findCodeTableDist();
	

	SysFieldInfo findFieldInfoBySrcField(String desField);

}