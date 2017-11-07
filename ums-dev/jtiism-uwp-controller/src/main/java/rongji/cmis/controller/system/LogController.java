package rongji.cmis.controller.system;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import rongji.cmis.model.ums.CfgUmsLog;
import rongji.cmis.model.ums.CfgUmsLog.OperateType;
import rongji.cmis.service.common.LogService;
import rongji.framework.base.model.ResultModel;
import rongji.framework.base.pojo.Filter;
import rongji.framework.base.pojo.Filter.Operator;
import rongji.framework.base.pojo.Order.Direction;
import rongji.framework.common.web.controller.BaseController;
import rongji.framework.util.Page;
import rongji.framework.util.ParamRequest;
import rongji.framework.util.StringUtil;

/**
 * Controller - 管理日志
 * 
 * @version 1.0
 */
@Controller("adminLogController")
@RequestMapping("/sys/log")
public class LogController extends BaseController {

	@Resource(name = "logServiceImpl")
	private LogService logService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "logList")
	public String list() {
		return "system/log/logList";
	}

	/**
	 * 日志查询
	 * 
	 * @param topic
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getlogList")
	public Page<CfgUmsLog> getConverseList(String sort, String serachName, String serachValue, String serachOperaTypeValue, ParamRequest paramRequest) {
		// 操作类型查询
		if (StringUtil.isNotEmpty(serachOperaTypeValue)) {
			if (OperateType.insert.toString().equals(serachOperaTypeValue)) {
				paramRequest.addFilter(new Filter("operateType", Operator.eq, OperateType.insert));
			} else if (OperateType.update.toString().equals(serachOperaTypeValue)) {
				paramRequest.addFilter(new Filter("operateType", Operator.eq, OperateType.update));
			} else if (OperateType.delete.toString().equals(serachOperaTypeValue)) {
				paramRequest.addFilter(new Filter("operateType", Operator.eq, OperateType.delete));
			}
		}
		if (!StringUtils.isEmpty(serachValue) && !StringUtils.isEmpty(serachName)) {
			if ("operateObj".equals(serachName)) {
				paramRequest.addFilter(Filter.eq(serachName, serachValue));
			} else {
				paramRequest.addFilter(Filter.like(serachName, serachValue));
			}
		}

		if (!StringUtils.isEmpty(sort)) {
			String[] params = sort.split("\\.");
			paramRequest.setOrderProperty(params[0]);
			paramRequest.setOrderDirection(params.length == 2 ? Direction.fromString(params[1]) : Direction.asc);
		}

		Page<CfgUmsLog> logs = logService.findAllForPage(paramRequest);

		return logs;
	}

	/**
	 * 查看
	 */
	@RequestMapping(value = "logView")
	public String view(String logId, ModelMap model) {
		model.addAttribute("log", logService.find(logId));
		return "system/log/logView";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "deleteLog", method = RequestMethod.POST)
	public @ResponseBody ResultModel delete(String logIds) {
		try {
			String[] logIdArr = logIds.split(",");
			for (int i = 0; i < logIdArr.length; i++) {
				logService.delete(logIdArr[i]);
			}
		} catch (Exception e) {
			return ERROR_RESULT_MESSAGE;
		}
		return SUCCESS_RESULT_MODEL;
	}

	/**
	 * 清空
	 */
	@RequestMapping(value = "clear", method = RequestMethod.POST)
	public @ResponseBody ResultModel clear() {
		logService.clear();
		return SUCCESS_RESULT_MODEL;
	}

}