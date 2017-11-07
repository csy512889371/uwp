package rongji.framework.base.model;

import java.io.Serializable;

/**
 * 日志配置
 * 
 * @version 1.0
 */
public class LogConfig implements Serializable {

	private static final long serialVersionUID = -1108848647938408402L;

	/** 操作名称 */
	private String operateModule;

	/** 操作集 --表名 */
	private String operateSet;

	/**
	 * 获取操作名称
	 * 
	 * @return 操作名称
	 */
	public String getOperateModule() {
		return operateModule;
	}

	/**
	 * 设置操作名称
	 * 
	 */
	public void setOperateModule(String operateModule) {
		this.operateModule = operateModule;
	}

	/**
	 * 获取操作集 --表名
	 * 
	 * @return 操作集 --表名
	 */
	public String getOperateSet() {
		return operateSet;
	}

	/**
	 * 设置操作集 --表名
	 * 
	 */
	public void setOperateSet(String operateSet) {
		this.operateSet = operateSet;
	}

}