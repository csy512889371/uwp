package rongji.cmis.dao.system;

import java.util.List;
import java.util.Map;

import rongji.cmis.model.ums.CfgUmsCodeAttribute;
import rongji.framework.base.dao.GenericDao;

public interface CfgUmsCodeAttributeDao extends GenericDao<CfgUmsCodeAttribute> {

	List<CfgUmsCodeAttribute> findAllRequest();

	void updateCodeAttr(CfgUmsCodeAttribute cfgUmsCodeAttribute);

	Integer findLastSeq(String codeId, String parentId);

	List<Map<String, Object>> findAttributeINSession(String operateSet);

	void addCodeAttribute(CfgUmsCodeAttribute cfgUmsCodeAttribute);

    Boolean isTableExists(String tableName);

	List<CfgUmsCodeAttribute> getTableChineseName(String tableName);

	List<CfgUmsCodeAttribute> findScriptNoNull();
}
