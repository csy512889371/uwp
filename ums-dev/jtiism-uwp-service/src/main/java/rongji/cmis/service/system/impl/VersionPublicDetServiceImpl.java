package rongji.cmis.service.system.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import rongji.cmis.dao.system.VersionPublicDetDao;
import rongji.cmis.model.sys.VersionPublicDet;
import rongji.cmis.service.system.VersionPublicDetService;
import rongji.framework.base.service.impl.BaseServiceImpl;

@Service("versionPublicDetServiceImpl")
public class VersionPublicDetServiceImpl extends BaseServiceImpl<VersionPublicDet> implements VersionPublicDetService {

	@Resource(name = "versionPublicDetDaoImpl")
	private VersionPublicDetDao versionPublicDetDao;

	@Override
	public List<Integer> getSeqnoByPublicId(Integer publicId) {
		return versionPublicDetDao.getSeqnoByPublicId(publicId);
	}
}
