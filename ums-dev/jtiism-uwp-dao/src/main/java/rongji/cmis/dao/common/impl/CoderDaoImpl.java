package rongji.cmis.dao.common.impl;

import org.springframework.stereotype.Repository;

import rongji.cmis.dao.common.CoderDao;
import rongji.cmis.model.sys.SysCoder;
import rongji.framework.base.dao.impl.GenericDaoImpl;

@Repository("coderDaoImpl")
public class CoderDaoImpl extends GenericDaoImpl<SysCoder> implements CoderDao {

}
