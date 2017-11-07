package rongji.framework.base.model;

import java.io.Serializable;

/**
 * 
 * @Title: IntegrateConfig.java
 * @Package rongji.framework.base.model
 * @Description: 综合查询配置
 * @author LFG
 * @date 2016年7月14日 下午7:45:26
 * @version V1.0
 */
public class IntegrateConfig implements Serializable {
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
	 * @param operation
	 *            操作名称
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
	 * @param urlPattern
	 *            操作集 --表名
	 */
	public void setOperateSet(String operateSet) {
		this.operateSet = operateSet;
	}
}
