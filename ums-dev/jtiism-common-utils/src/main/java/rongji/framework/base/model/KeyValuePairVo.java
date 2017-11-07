package rongji.framework.base.model;

import java.io.Serializable;

/**
 * 简单的key value 类型对象
 * @author zhangzhiyi
 *
 */
public class KeyValuePairVo {

	private Serializable key;
	private Object value;
	
	public KeyValuePairVo() {
		super();
	}
	public KeyValuePairVo(Serializable key, Object value) {
		super();
		this.key = key;
		this.value = value;
	}
	public Serializable getKey() {
		return key;
	}
	public void setKey(Serializable key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "KeyValuePairVo [key=" + key + ", value=" + value + "]";
	}
	
}
