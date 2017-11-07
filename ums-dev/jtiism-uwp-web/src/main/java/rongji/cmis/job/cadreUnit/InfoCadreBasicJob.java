package rongji.cmis.job.cadreUnit;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import rongji.cmis.service.system.MsgRemindService;

import javax.annotation.Resource;

/**
 * 
 * 干部基本信息定时器
 */
@Component("infoCadreBasicJob")
@Lazy(false)
public class InfoCadreBasicJob {

	@Resource(name = "msgRemindServiceImpl")
	MsgRemindService msgRemindService;


}
