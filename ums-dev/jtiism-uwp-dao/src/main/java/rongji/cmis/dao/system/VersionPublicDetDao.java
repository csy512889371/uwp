package rongji.cmis.dao.system;

import java.util.List;

import rongji.cmis.model.sys.VersionPublicDet;
import rongji.framework.base.dao.GenericDao;

public interface VersionPublicDetDao extends GenericDao<VersionPublicDet> {

	List<Integer> getSeqnoByPublicId(Integer publicId);

	void deleteByPublicId(Integer publicId);

}
