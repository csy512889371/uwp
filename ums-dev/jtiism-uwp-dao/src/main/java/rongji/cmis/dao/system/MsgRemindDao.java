package rongji.cmis.dao.system;

import java.util.List;

import rongji.cmis.model.sys.MsgRemind;
import rongji.cmis.model.sys.MsgRemind.MsgType;
import rongji.framework.base.dao.GenericDao;

public interface MsgRemindDao extends GenericDao<MsgRemind> {

	MsgRemind findByMsgTypeAndParam(MsgType msgType, String param);

	/**
	 * 
	 * @Title: findTop
	 * @Description: (查询前几条未读状态的消息)
	 * @param maxNum
	 * @param userId
	 * @return
	 * @return List<MsgRemind> 返回类型
	 * @throws
	 * @author Administrator
	 */
	List<MsgRemind> findTop(Integer maxNum, Integer userId);

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
	Integer findMsgNumber(Integer userId);

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
	MsgRemind findByMsgType(MsgType msgType, Integer userId, String title);

	/**
	 * 
	 * @Title: deleteMsgRemindByUserId
	 * @Description: 删除企业信息
	 * @param userId
	 * @return void 返回类型
	 * @throws
	 * @author LFG
	 */
	void deleteMsgRemindByUserId(Integer userId);

}
