package rongji.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 定义系统常量
 * @author kemiaoxin
 *
 */
public class Constant {
	
	/**当前用户 返回CfgUmsUser*/
	public final static String CURRENTUSER = "current_user";

	/**当前用户 返回CfgUmsUser*/
	public final static String CURRENTMEMBER = "current_member";

	/**当前登录用户的菜单权限 返回List<CfgUmsMenu>*/
	public final static String USERMENU = "user_menu";
	
	/**当前选择的系统 即一级菜单*/
	public final static String CURRENTSYSTEM = "current_system";


	/**当前选择的系统 即一级菜单*/
	public final static String DEF_LICENCEPATH = "/assets/pic/license-default.png";//经营执照默认路径
	
	/**自定义条件查询*/
	public final static String SEPARATOR = "|SEP|";


	/**企业信息修改MQ队列名*/
	public final static String ENTQUEUENAME = "jjms.entQueuelt";

	/**发送MQ定时任务执行时间间隔(分钟)*/
	public final static Integer SEND_EVENT_MSG_INTERVAL_MINUTE = 2;

	/**
	 * 管理端
	 */
	public final static String ADMIN_FROMTYPE = "admin";

	/**
	 * 门户端
	 */
	public final static String MEMBER_FROMTYPE = "member";

	/**
	 * 登录ticket
	 */
	public final static String LOGINTICKETS = "loginTickets";

	/**
	 * 获取自定义表主键名称
	 * @param infoSet
	 * @return
	 */
	public static String getKeyName(String infoSet) {
		if (StringUtils.isEmpty(infoSet)) {
			return "";
		}
		
		String priveKeyName = infoSet.replaceAll("SHJT", "").replace("_", "") + "000";
		return priveKeyName;
	}
	
	public static String getFkkey() {
		return "ENTINF000";
	}

}
