package rongji.framework.common.web.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import rongji.framework.base.PageContextHolder;
import rongji.framework.util.Page;
import rongji.framework.util.StringUtil;

public class PageInterceptor implements HandlerInterceptor{

	@SuppressWarnings("rawtypes")
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if(StringUtil.isNotEmpty(request.getParameter("pageSize"))&&StringUtil.isNotEmpty(request.getParameter("pn"))){
			Page page = new Page();
			page.setPageSize(Integer.valueOf(request.getParameter("pageSize")));
			page.setFirstResult(Integer.valueOf(request.getParameter("pageSize"))*(Integer.valueOf(request.getParameter("pn"))-1));
			PageContextHolder.setPageParam(page);
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if(StringUtil.isNotEmpty(request.getParameter("pageSize"))&&StringUtil.isNotEmpty(request.getParameter("pn"))){
			PageContextHolder.clearPageParam();
		}
	}

}