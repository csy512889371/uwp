package rongji.cmis.controller.system;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import rongji.cmis.model.sys.MsgRemind;
import rongji.cmis.model.sys.MsgRemind.MsgType;
import rongji.cmis.model.ums.CfgUmsMenu;
import rongji.cmis.service.system.MenuService;
import rongji.cmis.service.system.MsgRemindService;
import rongji.cmis.service.system.TopMenuService;
import rongji.cmis.service.system.UserService;
import rongji.framework.base.model.ResultModel;
import rongji.framework.base.pojo.Filter;
import rongji.framework.base.pojo.Filter.Operator;
import rongji.framework.base.pojo.Order.Direction;
import rongji.framework.common.web.controller.BaseController;
import rongji.framework.util.DateUtil;
import rongji.framework.util.Page;
import rongji.framework.util.ParamRequest;
import rongji.framework.util.StringUtil;

@Controller("msgRemindController")
@RequestMapping("/sys/msgRemind")
public class MsgRemindController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(MsgRemindController.class);
	@Resource(name = "msgRemindServiceImpl")
	MsgRemindService msgRemindService;

	@Resource(name = "userServiceImpl")
	UserService userService;

	@Resource(name = "menuServiceImpl")
	MenuService menuService;

	@Resource(name = "topMenuServiceImpl")
	TopMenuService topMenuService;

	/**
	 * 
	 * @Title: loadMsgRemindIndex
	 * @Description: (消息管理页面)
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("loadMsgRemindIndex")
	public String loadMsgRemindIndex() {

		return "system/msg/msgRemindIndex";
	}

	/**
	 * 
	 * @Title: findMsgRemindList
	 * @Description: (消息查询列表)
	 * @param paramRequest
	 * @param request
	 * @return
	 * @return Page<MsgRemind> 返回类型
	 * @throws
	 * @author Administrator
	 */
	@ResponseBody
	@RequestMapping("findMsgRemindList")
	public Page<MsgRemind> findMsgRemindList(ParamRequest paramRequest, HttpServletRequest request) {

		// 类型
		String msgType = request.getParameter("msgType");

		// 标题
		String title = request.getParameter("title");

		// 开始时间
		String startTime = request.getParameter("startTime");

		// 结束时间
		String endTime = request.getParameter("endTime");

		// 消息状态
		String msgStatus = request.getParameter("msgStatus");

		// 当前登录用户
		Integer userId = userService.getCurrentUserId();

		paramRequest.addFilter(new Filter("cfgUmsUser.id", Operator.eq, userId));

		if (StringUtil.isNotEmpty(msgType)) {
			paramRequest.addFilter(new Filter("msgType", Operator.eq, MsgType.valueOf(msgType)));
		}

		if (StringUtil.isNotEmpty(title)) {
			paramRequest.addFilter(new Filter("title", Operator.like, title));
		}

		if (StringUtil.isNotEmpty(startTime)) {
			paramRequest.addFilter(new Filter("publictime", Operator.ge, DateUtil.formatStringToDateWithNull(startTime, "yyyy-MM-dd")));
		}
		if (StringUtil.isNotEmpty(endTime)) {
			paramRequest.addFilter(new Filter("publictime", Operator.le, DateUtil.formatStringToDateWithNull(endTime, "yyyy-MM-dd")));
		}

		if (StringUtil.isNotEmpty(msgStatus)) {
			paramRequest.addFilter(new Filter("msgStatus", Operator.eq, Integer.parseInt(msgStatus)));
		}

		paramRequest.setOrderProperty("msgStatus,publictime");
		paramRequest.setOrderDirection(Direction.desc);

		Page<MsgRemind> msgPage = msgRemindService.findAllForPage(paramRequest);

		return msgPage;
	}

	/**
	 * 
	 * @Title: signMsgStatus
	 * @Description: (标记消息为已读)
	 * @param msgId
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author Administrator
	 */
	@ResponseBody
	@RequestMapping("signMsgStatus")
	public ResultModel signMsgStatus(String msgIds) {
		Integer unReadMsgCount = 0;
		try {
			msgRemindService.updateMsgStatus(msgIds);
			// 当前登录用户
			Integer userId = userService.getCurrentUserId();
			//获取用户未读消息数量
			unReadMsgCount = msgRemindService.findMsgNumber(userId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("标记失败！");
		}
		return ResultModel.success("已标记为已读！",unReadMsgCount+"");
	}

	/**
	 * 
	 * @Title: findTop
	 * @Description: (查询前几条未读状态的信息)
	 * @param maxNum
	 * @param request
	 * @return
	 * @return List<MsgRemind> 返回类型
	 * @throws
	 * @author Administrator
	 */
	@ResponseBody
	@RequestMapping("findTop")
	public Map<String, Object> findTop(Integer maxNum, HttpServletRequest request) {
		if (maxNum == null) {
			maxNum = 5;
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<MsgRemind> msgList = msgRemindService.findTop(maxNum);
		paramMap.put("msgList", msgList);

		return paramMap;
	}

	/**
	 * 
	 * @Title: deleteMsg
	 * @Description: (删除消息)
	 * @param msgRemindIds
	 * @return
	 * @return ResultModel 返回类型
	 * @throws
	 * @author Administrator
	 */
	@ResponseBody
	@RequestMapping("deleteMsg")
	public ResultModel deleteMsg(String msgRemindIds) {
		try {
			msgRemindService.deleteMsg(msgRemindIds);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultModel.error("common.delete.fail");
		}

		return ResultModel.success("common.delete.success");
	}

	/**
	 * 
	 * @Title: msgIndex
	 * @Description: (消息链接跳转)
	 * @param request
	 * @param msgId
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	@RequestMapping("msgIndex")
	public String msgIndex(HttpServletRequest request, String msgId) {
		String url = "";
		Integer unReadMsgCount = 0;
		try {
			MsgRemind msgRemind = msgRemindService.find(msgId);
			if (msgRemind.getMsgType().equals(MsgType.job)) {
				// 将消息标记为已读
				msgRemindService.updateMsgStatus(msgId);
			}
			url = msgRemind.getSkipUrl();
			// 当前登录用户
			Integer userId = userService.getCurrentUserId();
			//获取用户未读消息数量
			unReadMsgCount = msgRemindService.findMsgNumber(userId);
			if (msgRemind.getParam() != null && !msgRemind.getParam().isEmpty()) {
				String param = URLEncoder.encode(msgRemind.getParam(), "GBK");
				//TODO 后期再修改未读条数实现方式
				url += "?param=" + param +"&unReadMsgCount=" + unReadMsgCount;
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return "redirect:" + url;
	}
	
	@RequestMapping("getMenuIdByMsg")
	@ResponseBody
	public Integer getMenuIdByMsgType(String msgId) {
		if (StringUtils.isEmpty(msgId)) {
			return null;
		}
		MsgRemind msgRemind = msgRemindService.load(msgId);
		String menuCode = msgRemind.getMsgType().getName();
		if (StringUtils.isEmpty(menuCode)) {
			return null;
		}
		CfgUmsMenu cfgUmsMenu = menuService.findMenuByCode(menuCode);
		
		return cfgUmsMenu.getId();
	}
}
