package rongji.cmis.service.system.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import rongji.cmis.dao.system.OutputConfigDao;
import rongji.cmis.model.sys.SysOutputConfig;
import rongji.cmis.service.system.OutputConfigService;
import rongji.framework.base.service.impl.BaseServiceImpl;

@Service("outputConfigServiceImpl")
public class OutputConfigServiceImpl extends BaseServiceImpl<SysOutputConfig> implements OutputConfigService{

	@Resource(name="outputConfigDaoImpl")
	OutputConfigDao outputConfigDao;
	
}
