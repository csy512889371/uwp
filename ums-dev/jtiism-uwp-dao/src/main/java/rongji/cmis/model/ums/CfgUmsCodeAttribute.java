package rongji.cmis.model.ums;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import rongji.framework.base.model.SortEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CFG_UMS_CODE_ATTRIBUTE（代码属性表） 指标代码实体类
 * 
 * @author Administrator
 * 
 */
@Entity
@Table(name = "CFG_UMS_CODE_ATTRIBUTE")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "CFG_UMS_CODE_ATTRIBUTE")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class CfgUmsCodeAttribute extends SortEntity {

	private static final long serialVersionUID = 3178024052522715349L;

	/**
	 * 
	 * 代码表
	 */
	private CfgUmsCode cfgUmsCode;
	/**
	 * 属性代码
	 */
	private String attrCode;
	/**
	 * 属性名称
	 */
	private String attrName;
	/**
	 * 父类Id
	 */
	private String parentId;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 
	 */
	private String url;
	/**
	 * 是用于批处理
	 */
	private Boolean isBatch;
	/**
	 * 是基础信息集
	 */
	private Boolean isBasic;
	/**
	 * 是常用信息集
	 */
	private Boolean isCommon;

	/**
	 * 记录类型 singleRecord:单记录类型 multiRecord:多记录类型
	 */
	private String recordType;

	/**
	 * 信息集类型
	 */
	private String infoSetType;

	/**
	 * 脚本
	 */
	private String script;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODE_ID")
	public CfgUmsCode getCfgUmsCode() {
		return cfgUmsCode;
	}

	public void setCfgUmsCode(CfgUmsCode cfgUmsCode) {
		this.cfgUmsCode = cfgUmsCode;
	}

	@JsonProperty
	@Column(name = "ATTRCODE", length = 10)
	public String getAttrCode() {
		return attrCode;
	}

	public void setAttrCode(String attrCode) {
		this.attrCode = attrCode;
	}

	@JsonProperty
	@Column(name = "ATTRNAME", length = 20)
	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	@JsonProperty
	@Column(name = "PARENTID", length = 36)
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@JsonProperty
	@Column(name = "STATUS", length = 1)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@JsonProperty
	@Column(name = "URL", length = 500)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@JsonProperty
	@Column(name = "isBatch")
	public Boolean getIsBatch() {
		return isBatch;
	}

	public void setIsBatch(Boolean isBatch) {
		this.isBatch = isBatch;
	}

	@JsonProperty
	@Column(name = "isBasic")
	public Boolean getIsBasic() {
		return isBasic;
	}

	public void setIsBasic(Boolean isBasic) {
		this.isBasic = isBasic;
	}

	@JsonProperty
	@Column(name = "isCommon")
	public Boolean getIsCommon() {
		return isCommon;
	}

	public void setIsCommon(Boolean isCommon) {
		this.isCommon = isCommon;
	}

	@JsonProperty
	@Column(name = "recordType")
	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	@JsonProperty
	@Column(name = "infoSetType")
	public String getInfoSetType() {
		return infoSetType;
	}

	public void setInfoSetType(String infoSetType) {
		this.infoSetType = infoSetType;
	}

	@Column(name = "script")
	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
}
