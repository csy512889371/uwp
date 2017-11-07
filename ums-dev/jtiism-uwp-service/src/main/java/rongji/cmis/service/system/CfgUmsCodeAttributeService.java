package rongji.cmis.service.system;

import java.util.List;
import java.util.Map;

import rongji.cmis.model.ums.CfgUmsCodeAttribute;
import rongji.framework.base.service.BaseService;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统后台管理-->信息构建：指标代码维护
 * 
 * @author Administrator
 *
 */
public interface CfgUmsCodeAttributeService extends BaseService<CfgUmsCodeAttribute> {

	List<CfgUmsCodeAttribute> findAllRequest();

	void updateCodeAttr(CfgUmsCodeAttribute cfgUmsCodeAttribute);

	void saveCadreListCodeSort(String groupId, String[] infoSetIds);

	void addANewDynaInfoSet(CfgUmsCodeAttribute attr, HttpServletRequest request);

	void updateCodeAttribute(CfgUmsCodeAttribute codeAttr, HttpServletRequest request);

	void deleteDynaInfoSet(CfgUmsCodeAttribute attr);
	/**
	 * 创建动态信息集
	 */
	void createIndexCode(String tableName);

	void addCodeAttribute(CfgUmsCodeAttribute cfgUmsCodeAttribute);

	Boolean isTableExists(String tableName);

	List<CfgUmsCodeAttribute> findByAttrCode(String attrCode);

	List<CfgUmsCodeAttribute> findScriptNoNull();
}
