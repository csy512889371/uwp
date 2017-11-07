package rongji.cmis.controller.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import rongji.cmis.model.ums.CfgUmsLog;
import rongji.cmis.service.common.LogService;
import rongji.framework.common.web.controller.BaseController;
import rongji.framework.util.DocResourceUtils;
import rongji.redis.core.impl.RedisClient;

import javax.annotation.Resource;

@Controller("demoController")
@RequestMapping("sys/demo")
public class DemoController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

	@Resource(name = "freeMarkerConfigurer")
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Resource(name = "logServiceImpl")
	private LogService logService;

	@Autowired
	private RedisClient redisClient;


	@RequestMapping("log")
	@ResponseBody
	public String log() {
		String picDir = DocResourceUtils.getResourceFilePath("/resource/pic/");
		logger.error(picDir);

		logger.info(System.getProperty("catalina.home"));
		CfgUmsLog log = logService.find("2c9205815a3656b4015a366a7b130003");
		logger.info("test");
		if (log != null) {
			logger.info(log.getContent());
		}
		return "success";
	}

	@RequestMapping("webOfficeIndex")
	public String webOfficeIndex() {
		return "common/demo/webOfficeIndex";
	}

	@RequestMapping("testRedis")
	@ResponseBody
	public String testRedis() {

		/*

		 * redisClient.opsForHash().hset(KeyUtils.getAdminUserEtpKey("1",
		 * "023f6d38-3007-464e-98ef-5abcedb200d3"), "SHJT_ENT_INF", "1"); String
		 * menuVal =
		 * redisClient.opsForHash().hget(KeyUtils.getAdminUserEtpKey("1",
		 * "023f6d38-3007-464e-98ef-5abcedb200d3"), "SHJT_ENT_INF");
		 * 
		 * System.out.println(menuVal);
		 */

		return "success";
	}



	
	@RequestMapping("toPage")
	public String toDemoPage(String page) {
		return "page/demo/" + page;
	}

}