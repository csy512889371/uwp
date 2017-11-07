package rongji.cmis.model.sys;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import rongji.framework.base.model.SortEntity;

import javax.persistence.*;

/**
 * 配置 信息集 字段的显示
 */
@Entity
@Table(name = "SYS_COLUMN_SHOW")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "sys_column_show_sequence")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class SysColumnShow extends SortEntity {

	private static final long serialVersionUID = -736095388820732255L;

	public enum PropertyType {
		/** 固定于页面 */
		fixed,
		/** 数据库字段，动态展示 */
		dynamic,
		/** 额外字段 */
		extra
	}

	/**
	 * 值类型
	 */
	public enum Type {
		/** 文本 */
		text,

		/** 时间类型 */
		datetemp,

		/** 数据集 */
		dataList,

		/** 单选项 */
		select,

		/** 多选项 */
		checkbox,

		/** 只读 */
		readonly,
		/** 长文本 */
		textarea,
		/** 图片 **/
		singleimage,
		/** 单选框 **/
		radio,
		/** 单选框 **/
		multiFile

	}

	/**
	 * 所属信息集
	 */
	private String infoSet;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 是否启用
	 */
	private Boolean isEnabled;

	/**
	 * 是否必填
	 */
	private Boolean isRequired;

	/**
	 * 属性名称
	 */
	private String propertyName;
	/**
	 * 属性码
	 */
	private String propertyCode;
	/**
	 * 类型
	 */
	private Type type;

	/**
	 * 校验规则集合
	 */
	private String validateRule;

	/**
	 * 字段长度限制
	 */
	private String lengthLimit;
	
	private String rowHeight;

	private Boolean allowBatch;
	private PropertyType propertyType;
	private Integer columnPriv;
	
	private Boolean isGridItem;

	private String htmlStr;

	private String htmlType;

	private String dataFormat;

	private String comment;

	@JsonProperty
	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty
	@Column(name = "isEnabled")
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@JsonProperty
	@Column(name = "isRequired")
	public Boolean getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}

	@JsonProperty
	@Column(name = "propertyName")
	public String getPropertyName() {
		return propertyName;
	}

	@JsonProperty
	@Column(name = "propertyCode")
	public String getPropertyCode() {
		return propertyCode;
	}

	public void setPropertyCode(String propertyCode) {
		this.propertyCode = propertyCode;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	@JsonProperty
	@Column(name = "allowBatch")
	public Boolean getAllowBatch() {
		return allowBatch;
	}

	public void setAllowBatch(Boolean allowBatch) {
		this.allowBatch = allowBatch;
	}

	@JsonProperty
	@Column(name = "infoSet")
	public String getInfoSet() {
		return infoSet;
	}

	public void setInfoSet(String infoSet) {
		this.infoSet = infoSet;
	}

	@JsonProperty
	@Column(name = "validateRule")
	public String getValidateRule() {
		return validateRule;
	}

	public void setValidateRule(String validateRule) {
		this.validateRule = validateRule;
	}

	@JsonProperty
	@Column(name = "lengthLimit")
	public String getLengthLimit() {
		return lengthLimit;
	}

	public void setLengthLimit(String lengthLimit) {
		this.lengthLimit = lengthLimit;
	}

	@JsonProperty
	@Column(name = "type")
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@JsonProperty
	@Column(name = "propertyType")
	public PropertyType getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(PropertyType propertyType) {
		this.propertyType = propertyType;
	}

	@JsonProperty
	@Transient
	public Integer getColumnPriv() {
		return columnPriv;
	}

	public void setColumnPriv(Integer columnPriv) {
		this.columnPriv = columnPriv;
	}

	@JsonProperty
	@Column(name = "isGridItem")
	public Boolean getIsGridItem() {
		return isGridItem;
	}

	public void setIsGridItem(Boolean isGridItem) {
		this.isGridItem = isGridItem;
	}

	@JsonProperty
	@Column(name = "rowHeight")
	public String getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(String rowHeight) {
		this.rowHeight = rowHeight;
	}

	@Transient
	@JsonProperty
	public String getHtmlStr() {
		return htmlStr;
	}

	public void setHtmlStr(String htmlStr) {
		this.htmlStr = htmlStr;
	}

	@JsonProperty
	@Column(name = "htmlType")
	public String getHtmlType() {
		return htmlType;
	}

	public void setHtmlType(String htmlType) {
		this.htmlType = htmlType;
	}

	@JsonProperty
	@Column(name = "dataFormat")
	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	@Column(name = "comment")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
