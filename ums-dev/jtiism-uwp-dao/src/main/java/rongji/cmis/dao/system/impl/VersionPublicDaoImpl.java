package rongji.cmis.dao.system.impl;

import org.springframework.stereotype.Repository;

import rongji.cmis.dao.system.VersionPublicDao;
import rongji.cmis.model.sys.VersionPublic;
import rongji.framework.base.dao.impl.GenericDaoImpl;

@Repository("versionPublicDaoImpl")
public class VersionPublicDaoImpl extends GenericDaoImpl<VersionPublic> implements VersionPublicDao {

}
