package rongji.cmis.service.system;

import rongji.cmis.model.ums.CfgUmsGroup;
import rongji.framework.base.service.BaseService;

public interface GroupService extends BaseService<CfgUmsGroup> {

	void saveGroupSort(String groupIds);

	Integer getLastSeqno();

	void saveAndSeqno(CfgUmsGroup group);

}
