package rongji.cmis.dao.system;

import java.util.List;
import java.util.Map;

import rongji.cmis.model.ums.CfgUmsInfoSet;
import rongji.framework.base.dao.GenericDao;
import rongji.framework.util.Page;
import rongji.framework.util.ParamRequest;

public interface DynamicInfoSetDao  extends GenericDao<CfgUmsInfoSet> {

	void createInfoSetFKA01(String tableName);

	void addIndexCode(String tableName);

	void addColumn(final String sql);

	Map<String, Object> findToMap(String infoSet, String selectV, String priKey);

	List<Map<String, Object>> findAllToListMap(String infoSet);

	Page<Object> findAllForPage(String infoSet, ParamRequest paramRequest);

	void save(String sql);

	Integer update(String sql);

	List<Map<String, Object>> findAllToListMap(String infoSet, String fkVal, String type, String selectV);

	List<Map<String, Object>> getRecentData(String infoSet, String columnOne, String columnTwo,String entId);
}
