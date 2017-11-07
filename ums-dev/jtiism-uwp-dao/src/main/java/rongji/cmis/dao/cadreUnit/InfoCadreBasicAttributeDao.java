package rongji.cmis.dao.cadreUnit;

import java.util.List;
import java.util.Map;

import rongji.cmis.model.sys.SysColumnShow;
import rongji.framework.base.dao.GenericDao;

public interface InfoCadreBasicAttributeDao extends GenericDao<SysColumnShow> {

	List<Map<String, Object>> findColumnInfoINSession(String infoSet);

	List<SysColumnShow> findColumnInfo(String infoSet);

}
