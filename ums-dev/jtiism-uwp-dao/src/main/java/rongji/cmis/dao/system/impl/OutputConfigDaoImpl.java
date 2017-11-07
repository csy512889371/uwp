package rongji.cmis.dao.system.impl;

import org.springframework.stereotype.Repository;

import rongji.cmis.dao.system.OutputConfigDao;
import rongji.cmis.model.sys.SysOutputConfig;
import rongji.framework.base.dao.impl.GenericDaoImpl;

@Repository("outputConfigDaoImpl")
public class OutputConfigDaoImpl extends GenericDaoImpl<SysOutputConfig> implements OutputConfigDao{

}
