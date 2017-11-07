package rongji.framework.base.model;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 基础的model 便于系统model的统一管理
 * 
 * @author zhangzhiyi
 * 
 */
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE)
@MappedSuperclass
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = -3088050743330642186L;

	/** ID */
	private String id;

	/** "ID"属性名称 */
	public static final String ID_PROPERTY_NAME = "id";

	/**
	 * 获取ID
	 * 
	 * @return ID
	 */
	@JsonProperty
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "ID", unique = true, nullable = false)
	public String getId() {
		return id;
	}

	/**
	 * 设置ID
	 * 
	 * @param id
	 *            ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 重写equals方法
	 * 
	 * @param obj
	 *            对象
	 * @return 是否相等
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (!BaseEntity.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		BaseEntity other = (BaseEntity) obj;
		return getId() != null ? getId().equals(other.getId()) : false;
	}

	/**
	 * 重写hashCode方法
	 * 
	 * @return hashCode
	 */
	@Override
	public int hashCode() {
		int hashCode = 17;
		hashCode += null == getId() ? 0 : getId().hashCode() * 31;
		return hashCode;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName() + " [");

		for (Field f : this.getClass().getDeclaredFields()) {

			try {
				if (f.getType() == String.class || f.getType() == Short.class || f.getType() == Integer.class || f.getType() == Long.class) {
					PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(this, f.getName());
					if (descriptor == null) {
						continue;
					}
					Method method = descriptor.getReadMethod();
					Object tempValue = method.invoke(this);
					if (tempValue == null) {
						continue;
					}
					sb.append(f.getName() + ":" + tempValue + ",");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (sb.indexOf(",") != -1) {
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		sb.append("]");
		return sb.toString();
	}

}
