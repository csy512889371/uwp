package rongji.cmis.dao.common.impl;

import org.springframework.stereotype.Repository;

import rongji.cmis.dao.common.LogComputeRecDao;
import rongji.cmis.model.sys.LogComputeRec;
import rongji.framework.base.dao.impl.GenericDaoImpl;

@Repository("logComputeRecDaoImpl")
public class LogComputeRecDaoImpl extends GenericDaoImpl<LogComputeRec> implements LogComputeRecDao {

}