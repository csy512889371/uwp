package rongji.framework.util;

import java.util.List;

public class DataAccessUtil {
	@SuppressWarnings("rawtypes")
	public static Long longResult(List list){
		if(list!=null){
			return (Long)((Object[])list.get(0))[0];
		}else{
			return 0L;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static <T> T objectResult(List list,Class<T> requiredType){
		if(list!=null){
			try {
				Object[] objs = (Object[])list.get(0);
				return requiredType.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}else{
			return null;
		}
		return null;
	}
}
