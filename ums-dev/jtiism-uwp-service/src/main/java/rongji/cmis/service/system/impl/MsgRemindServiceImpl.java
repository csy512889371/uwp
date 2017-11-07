package rongji.cmis.service.system.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import rongji.cmis.dao.system.MsgRemindDao;
import rongji.cmis.model.sys.MsgRemind;
import rongji.cmis.model.sys.MsgRemind.MsgType;
import rongji.cmis.model.ums.CfgUmsUser;
import rongji.cmis.service.system.MsgRemindService;
import rongji.cmis.service.system.UserService;
import rongji.framework.base.service.impl.BaseServiceImpl;
import rongji.framework.util.DateUtil;

@Service("msgRemindServiceImpl")
public class MsgRemindServiceImpl extends BaseServiceImpl<MsgRemind> implements MsgRemindService {

	@Resource(name = "msgRemindDaoImpl")
	MsgRemindDao msgRemindDao;

	@Resource(name = "userServiceImpl")
	UserService userService;

	@Override
	public void updateMsgStatus(String msgIds) {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes) ra).getRequest();
		String[] msgIdArr = msgIds.split(",");
		for (int i = 0; i < msgIdArr.length; i++) {
			MsgRemind msgRemind = msgRemindDao.find(msgIdArr[i]);
			if (msgRemind != null && msgRemind.getMsgStatus().equals(0)) {
				msgRemind.setMsgStatus(1);
				msgRemindDao.update(msgRemind);
				Integer msgNumber = (Integer) request.getSession().getAttribute("msgNumber");
				request.getSession().setAttribute("msgNumber", msgNumber - 1);
			}
		}

	}

	@Override
	public void updateMsgStatus(MsgType msgType, String param) {
		MsgRemind msgRemind = msgRemindDao.findByMsgTypeAndParam(msgType, param);
		if (msgRemind != null) {
			// TODO
		}

	}

	@Override
	public List<MsgRemind> findTop(Integer maxNum) {
		Integer userId = userService.getCurrentUserId();
		return msgRemindDao.findTop(maxNum, userId);
	}

	@Override
	public void putAlertMsg(String title, String content, MsgType msgType, Integer userId, String url, String param) {
		CfgUmsUser cfgUmsUser = userService.find(userId);
		Date date = DateUtil.getCurrentDate();
		MsgRemind msgRemind = new MsgRemind();
		msgRemind.setTitle(title);
		msgRemind.setContents(content);
		msgRemind.setMsgType(msgType);
		msgRemind.setMsgStatus(0);
		msgRemind.setPublictime(date);
		msgRemind.setCfgUmsUser(cfgUmsUser);
		msgRemind.setSkipUrl(url);
		msgRemind.setParam(param);

		this.save(msgRemind);

	}

	@Override
	public void deleteMsg(String msgRemindIds) {
		String[] msgRemindIdArr = msgRemindIds.split(",");
		for (int i = 0; i < msgRemindIdArr.length; i++) {
			msgRemindDao.delete(msgRemindIdArr[i]);
		}
	}

	@Override
	public Integer findMsgNumber(Integer userId) {
		return msgRemindDao.findMsgNumber(userId);
	}

	@Override
	public MsgRemind findByMsgType(MsgType msgType, Integer userId, String title) {
		return msgRemindDao.findByMsgType(msgType, userId, title);
	}

}
