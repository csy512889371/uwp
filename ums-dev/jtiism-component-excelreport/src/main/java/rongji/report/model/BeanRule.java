package rongji.report.model;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @Title: Scrpit.java
 * @Package rongji.report.utils
 * @Description: 单条脚本
 * @author liuzhen
 * @date 2017年3月1日 上午8:40:27
 * @version V1.0
 */
public class BeanRule {

	public enum CommondType {
		assign, codeBlock
	}

	CommondType commendType;
	String target;
	String commond;

	public static BeanRule createBeanRule(String info) throws Exception {
		if (StringUtils.isEmpty(info)) {
			return null;
		}
		return new BeanRule(info);
	}

	public BeanRule(String info) throws Exception {
		try {
			Integer sginIndex = -1;
			if (info.startsWith(">>")) {
				commendType = CommondType.codeBlock;
				this.commond = info.substring(2);
			} else if ((sginIndex = info.indexOf(">")) > 0) {
				commendType = CommondType.assign;
				target = info.substring(0, sginIndex);
				this.commond = addAssignFunc(info.substring(sginIndex + 1));
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new Exception("未识别语法:" + info);
		}
	}

	private String addAssignFunc(String commond) throws Exception {
		Integer index1 = commond.indexOf(".setObject(");
		Integer index2 = commond.indexOf(".fill(");
		Integer index3 = commond.indexOf(".fillEach(");
		Integer index4 = commond.indexOf(".setPicture(");
		Integer num = 0;
		if (index1 > 0) {
			num++;
		}
		if (index2 > 0) {
			if (num > 0) {
				throw new Exception("不允许存在多个赋值函数");
			}
			num++;
		}
		if (index3 > 0) {
			if (num > 0) {
				throw new Exception("不允许存在多个赋值函数");
			}
			num++;
		}
		if (index4 > 0) {
			if (num > 0) {
				throw new Exception("不允许存在多个赋值函数");
			}
			num++;
		}
		if (num == 0) {
			commond = commond + ".setObject()";
		}
		return commond;
	}

	public CommondType getCommendType() {
		return commendType;
	}

	public void setCommendType(CommondType commendType) {
		this.commendType = commendType;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getCommond() {
		return commond;
	}

	public void setCommond(String commond) {
		this.commond = commond;
	}
}
