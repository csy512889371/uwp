package rongji.framework.base;

import rongji.framework.util.Page;
@SuppressWarnings("rawtypes")
public class PageContextHolder {
	//利用ThreadLocal解决线程安全问题
	private static final ThreadLocal<Page> contextHolder = new ThreadLocal<Page>();

	//设置分页
	public static void setPageParam(Page page) {
		contextHolder.set(page);
	}
	
	//获取分页
	public static Page getPageParam() {
		return contextHolder.get();
	}

	//清空
	public static void clearPageParam() {
		contextHolder.remove();
	}
}
