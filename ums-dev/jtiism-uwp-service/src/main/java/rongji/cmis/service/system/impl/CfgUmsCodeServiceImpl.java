package rongji.cmis.service.system.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import rongji.cmis.dao.system.CfgUmsCodeDao;
import rongji.cmis.model.ums.CfgUmsCode;
import rongji.cmis.service.system.CfgUmsCodeService;
import rongji.framework.base.service.impl.BaseServiceImpl;

@Service("cfgUmsCodeServiceImpl")
public class CfgUmsCodeServiceImpl extends BaseServiceImpl<CfgUmsCode> implements CfgUmsCodeService {

	@Resource(name = "cfgUmsCodeDaoImpl")
	CfgUmsCodeDao cfgUmsCodeDao;

}
