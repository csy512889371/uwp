package rongji.framework.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import rongji.framework.util.StringUtil;

/**
 * <p>Title:      </p>
 * <p>Description:      </p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: RongJi</p>
 * 
 * @author Anna 
 * @create in 2013-7-24
 * @version 3.0 
 */
public class ContextHolderUtils {
	
//	private static HttpServletRequest request;
//	private static HttpServletResponse response;
//	
//
//	public static void setRequest(HttpServletRequest request_) {
//		if(request==null){
//			request = request_;
//		}
//	}
//	public static void setResponse(HttpServletResponse response_) {
//		if(response==null){
//			response = response_;
//		}
//	}
//	
//	public static void clearRequest(){
//		request=null;
//	}
//	
//	public static void clearResponse(){
//		response=null;
//	}
//	
	
	
	/**
	 * SpringMvc下获取request
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//		if(request==null){
//			request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//		}
		return request;
	}
	/**
	 * SpringMvc下获取response
	 * @return
	 */
	public static HttpServletResponse getResponse(){
		HttpServletResponse response = ((ServletWebRequest)RequestContextHolder.getRequestAttributes()).getResponse();
//		if(response==null){
//			response = ((ServletWebRequest)RequestContextHolder.getRequestAttributes()).getResponse();
//		}
		return response;
	}
	/**
	 * SpringMvc下获取session
	 * 
	 * @return
	 */
	public static HttpSession getSession() {
		HttpSession session = getRequest().getSession();
		return session;
	}
	
	/**
	 * 获得请求路径
	 * @param request
	 * @return
	 */
	public static String getRequestPath(HttpServletRequest request) {
	    String requestPath = null;
		
	    if(request.getQueryString()==null){
		requestPath = request.getRequestURI() ;
	    }else{
		requestPath = request.getRequestURI() + "?" + request.getQueryString();
	    }
		if (requestPath.indexOf("&") > -1) {// 去掉其他参数
			requestPath = requestPath.substring(0, requestPath.indexOf("&"));
		}
		if (requestPath.indexOf("=") > -1) {
		    requestPath = requestPath.substring(0,requestPath.indexOf("?"));
		}
		requestPath = requestPath.substring(request.getContextPath().length() + 1);// 去掉项目路径
		return requestPath;
	}
	
	/**
	 * 获得请求参数 （过滤请求地址 xxx.do?xxx）
	 * @param request
	 * @return
	 */
	public static String getQueryStr(HttpServletRequest request){
		String queryStr = request.getQueryString();
		if(!StringUtil.isEmpty(queryStr)&&queryStr.indexOf("&") > -1){
			queryStr = queryStr.substring(queryStr.indexOf("&"));
			return queryStr;
		}
		return "";
	}
	
}
