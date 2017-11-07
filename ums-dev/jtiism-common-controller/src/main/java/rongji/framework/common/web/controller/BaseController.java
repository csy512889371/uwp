package rongji.framework.common.web.controller;

import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import rongji.framework.base.model.ResultModel;
import rongji.framework.common.web.editor.DateEditor;
import rongji.framework.util.SpringUtils;

/**
 * 基础controller
 *
 */
public class BaseController {
	

	public static final String FLASH_MESSAGE_ATTRIBUTE_NAME = "FlashMessageDirective.FLASH_MESSAGE";
	
	
	/** 错误消息 */
	protected static final ResultModel ERROR_RESULT_MESSAGE = ResultModel.error("admin.message.error");

	/** 成功消息 */
	protected static final ResultModel SUCCESS_RESULT_MODEL = ResultModel.success("admin.message.success");

	@InitBinder
	protected void initBinder(HttpServletRequest request,ServletRequestDataBinder binder){
		binder.registerCustomEditor(Date.class, new DateEditor());
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}
	
	
	/**
	 * 获取国际化消息
	 * 
	 * @param code
	 *            代码
	 * @param args
	 *            参数
	 * @return 国际化消息
	 */
	protected String message(String code, Object... args) {
		return SpringUtils.getMessage(code, args);
	}

	/**
	 * 添加瞬时消息
	 * 
	 * @param redirectAttributes
	 *            RedirectAttributes
	 * @param message
	 *            消息
	 */
	protected void addFlashMessage(RedirectAttributes redirectAttributes, ResultModel resultModel) {
		if (redirectAttributes != null && resultModel != null) {
			redirectAttributes.addFlashAttribute(FLASH_MESSAGE_ATTRIBUTE_NAME, resultModel);
		}
	}
	
	/**
	* @Title: solveRandomCode
	* @Description: (文件下载乱码处理)
	* @param realname
	* @param request
	* @param response
	* @throws Exception
	* @return void    返回类型
	* @throws
	* @author wqq 
	*/ 
	protected void solveRandomCode(String realname, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userAgent = request.getHeader("User-Agent");// 可以获得浏览器和操作系统信息
		if (userAgent != null) {
			if ((userAgent.toLowerCase().indexOf("firefox") > 0) || userAgent.toLowerCase().indexOf("mozilla") > 0) {
				byte[] bytes = userAgent.contains("MSIE") ? realname.getBytes() : realname.getBytes("UTF-8");
				realname = new String(bytes, "ISO-8859-1"); // 各浏览器基本都支持ISO编码
				response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", realname)); // 文件名外的双引号处理fireFox的空格截断问题
			} else {
				response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(realname, "UTF-8").replace("+", "%20"));
			}
		} else {
			response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(realname, "UTF-8").replace("+", "%20"));
		}

	}


}
