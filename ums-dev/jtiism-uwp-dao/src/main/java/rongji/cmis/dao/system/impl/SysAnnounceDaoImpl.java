package rongji.cmis.dao.system.impl;

import org.springframework.stereotype.Repository;
import rongji.cmis.dao.system.SysAnnounceDao;
import rongji.cmis.model.sys.SysAnnounce;
import rongji.framework.base.dao.impl.GenericDaoImpl;

@Repository("sysAnnounceDaoImpl")
public class SysAnnounceDaoImpl extends GenericDaoImpl<SysAnnounce> implements SysAnnounceDao {
}
