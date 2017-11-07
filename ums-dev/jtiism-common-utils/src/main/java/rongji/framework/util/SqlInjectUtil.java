package rongji.framework.util;


/**
 * 
* @Title: SqlInjectUtil.java
* @Package rongji.framework.util
* @Description: 控制sql语句注入
* @author LFG
* @date 2016-4-26 下午5:33:07
* @version V1.0
 */
public class SqlInjectUtil {
	/**
	 * 
	 * @Title: sqlInject
	 * @Description: 判断参数是否包含注入语句
	 * @param str
	 * @return
	 * @return boolean true:包含;false:不包含
	 * @throws
	 * @author LFG
	 */
	public static boolean sqlInject(String str) {
		if(StringUtil.isEmpty(str)){
			return false;
		}
		String injectStr = "'|and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+|,";
		String[] injectArray = injectStr.split("\\|");
		for (int i = 0; i < injectArray.length; i++) {
			if (str.indexOf(injectArray[i]) >= 0) {
				return true;
			}
		}
		return false;
	}
}