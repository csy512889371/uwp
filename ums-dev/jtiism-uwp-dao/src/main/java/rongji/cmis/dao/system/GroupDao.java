package rongji.cmis.dao.system;

import rongji.cmis.model.ums.CfgUmsGroup;
import rongji.framework.base.dao.GenericDao;

public interface GroupDao extends GenericDao<CfgUmsGroup> {

	Integer getLastSeqno();

}
