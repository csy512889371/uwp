package rongji.cmis.model.ums;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.MapKeyColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import rongji.framework.base.model.SortEntity;

/**
 * Entity - 插件配置
 * 
 * @version 1.0
 */
@Entity
@Table(name = "CFG_UMS_PLUGIN_CONFIG")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "CFG_UMS_PLUGIN_CONFIG_SEQUENCE")
public class CfgUmsPluginConfig extends SortEntity {

	private static final long serialVersionUID = 2967873491285939196L;

	/** 插件ID */
	private String pluginId;

	/** 是否启用 */
	private Boolean isEnabled;

	/** 属性 */
	private Map<String, String> attributes = new HashMap<String, String>();

	/**
	 * 获取插件ID
	 * 
	 * @return 插件ID
	 */
	@Column(nullable = false, updatable = false, unique = true, length = 100)
	public String getPluginId() {
		return pluginId;
	}

	/**
	 * 设置插件ID
	 * 
	 * @param pluginId
	 *            插件ID
	 */
	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	/**
	 * 获取是否启用
	 * 
	 * @return 是否启用
	 */
	@Column(nullable = false, columnDefinition="varchar(2)")
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	/**
	 * 设置是否启用
	 * 
	 * @param isEnabled
	 *            是否启用
	 */
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * 获取属性
	 * 
	 * @return 属性
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "CFG_UMS_PLUGIN_CONFIG_ATTRIBUTE")
	@MapKeyColumn(name = "name", length = 100)
	public Map<String, String> getAttributes() {
		return attributes;
	}

	/**
	 * 设置属性
	 * 
	 * @param attributes
	 *            属性
	 */
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	/**
	 * 获取属性值
	 * 
	 * @param name
	 *            属性名称
	 * @return 属性值
	 */
	@Transient
	public String getAttribute(String name) {
		if (getAttributes() != null && name != null) {
			return getAttributes().get(name);
		} else {
			return null;
		}
	}

	/**
	 * 设置属性值
	 * 
	 * @param name
	 *            属性名称
	 * @param value
	 *            属性值
	 */
	@Transient
	public void setAttribute(String name, String value) {
		if (getAttributes() != null && name != null) {
			getAttributes().put(name, value);
		}
	}

}