package rongji.framework.base.model;

import rongji.framework.util.SpringUtils;

public class ResultModel {

	/**
	 * 类型
	 */
	public enum Type {

		/** 成功 */
		success,

		/** 警告 */
		warn,

		/** 错误 */
		error
	}

	/** 类型 */
	private Type type;

	/** 内容 */
	private String content;
	
	/**
	 * 参数
	 */
	private String params;

	/**
	 * 初始化一个新创建的 ResultModel 对象，使其表示一个空消息。
	 */
	public ResultModel() {

	}

	/**
	 * 初始化一个新创建的 ResultModel 对象
	 * 
	 * @param type
	 *            类型
	 * @param content
	 *            内容
	 */
	public ResultModel(Type type, String content) {
		this.type = type;
		this.content = SpringUtils.getMessage(content);
	}
	
	/**
	 * 初始化一个新创建的 ResultModel 对象
	 * 
	 * @param type
	 *            类型
	 * @param content
	 *            内容
	 * @param params
	 *            参数
	 */
	public ResultModel(Type type, String params, String content) {
		this.type = type;
		this.content = SpringUtils.getMessage(content);
		this.params = params;
	}
	
	/**
	 * @param type
	 *            类型
	 * @param content
	 *            内容
	 * @param params
	 *            页面参数
	 * @param args
	 *            国际化参数
	 */
	public ResultModel(Type type, String content, String params, Object... args) {
		this.type = type;
		this.params = params;
		this.content = SpringUtils.getMessage(content, args);
	}
	

	/**
	 * @param type
	 *            类型
	 * @param content
	 *            内容
	 * @param args
	 *            参数
	 */
	public ResultModel(Type type, String content, Object... args) {
		this.type = type;
		this.content = SpringUtils.getMessage(content, args);
	}

	/**
	 * 返回成功消息
	 * 
	 * @param content
	 *            内容
	 * @param args
	 *            参数
	 * @return 成功消息
	 */
	public static ResultModel success(String content, Object... args) {
		return new ResultModel(Type.success, content, args);
	}
	
	/**
	 * 返回成功消息
	 * 
	 * @param content
	 *            内容
	 * @param args
	 *            国际化参数
	 * @param params
	 *            页面参数
	 * @return 成功消息
	 */
	public static ResultModel success(String content, String params, Object... args) {
		return new ResultModel(Type.success, content, params, args);
	}

	/**
	 * 返回警告消息
	 * 
	 * @param content
	 *            内容
	 * @param args
	 *            参数
	 * @return 警告消息
	 */
	public static ResultModel warn(String content, Object... args) {
		return new ResultModel(Type.warn, content, args);
	}

	/**
	 * 返回错误消息
	 * 
	 * @param content
	 *            内容
	 * @param args
	 *            参数
	 * @return 错误消息
	 */
	public static ResultModel error(String content, Object... args) {
		return new ResultModel(Type.error, content, args);
	}

	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * 获取内容
	 * 
	 * @return 内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置内容
	 * 
	 * @param content
	 *            内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return SpringUtils.getMessage(content);
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
	
}
