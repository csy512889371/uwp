package rongji.cmis.service.system;

import java.util.List;

import rongji.cmis.model.sys.MsgRemind;
import rongji.cmis.model.sys.MsgRemind.MsgType;
import rongji.framework.base.service.BaseService;

public interface MsgRemindService extends BaseService<MsgRemind> {

	/**
	 * 
	 * @Title: findTop
	 * @Description: (查询前几条未读状态的消息)
	 * @param maxNum
	 * @return
	 * @return List<MsgRemind> 返回类型
	 * @throws
	 * @author Administrator
	 */
	public List<MsgRemind> findTop(Integer maxNum);

	/**
	 * 
	 * @Title: updateMsgStatus
	 * @Description: (根据消息Id修改消息状态)
	 * @param msgIds
	 * @return void 返回类型
	 * @throws
	 * @author Administrator
	 */
	public void updateMsgStatus(String msgIds);

	/**
	 * 
	 * @Title: updateMsgStatus
	 * @Description: (根据消息类型和参数修改消息状态)
	 * @param msgType
	 * @param param
	 * @return void 返回类型
	 * @throws
	 * @author Administrator
	 */
	public void updateMsgStatus(MsgType msgType, String param);

	/**
	 * 
	 * @Title: putAlertMsg
	 * @Description: (添加消息提醒)
	 * @param title
	 * @param content
	 * @param msgType
	 * @param userId
	 * @param url
	 * @param param
	 * @return void 返回类型
	 * @throws
	 * @author Administrator
	 */
	public void putAlertMsg(String title, String content, MsgType msgType, Integer userId, String url, String param);

	/**
	 * 
	 * @Title: deleteMsg
	 * @Description: (根据消息ID删除消息)
	 * @param msgRemindIds
	 * @return void 返回类型
	 * @throws
	 * @author Administrator
	 */
	public void deleteMsg(String msgRemindIds);

	/**
	 * 
	 * @Title: findMsgNumber
	 * @Description: (未读消息条数)
	 * @param userId
	 * @return
	 * @return Integer 返回类型
	 * @throws
	 * @author Administrator
	 */
	public Integer findMsgNumber(Integer userId);

	/**
	 * 
	 * @Title: findByMsgType
	 * @Description: (根据类型查找消息)
	 * @param msgType
	 * @param userId
	 * @param title
	 * @return
	 * @return MsgRemind 返回类型
	 * @throws
	 * @author Administrator
	 */
	public MsgRemind findByMsgType(MsgType msgType, Integer userId, String title);

}
