package rongji.cmis.model.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import rongji.framework.base.model.BaseEntity;

/**
 * 表字段 对应的数据字典
 * @version V1.0
 */
@Entity
@Table(name = "SYS_FIELDINFO")
public class SysFieldInfo extends BaseEntity {

	private static final long serialVersionUID = -3726027905695329871L;

	/**
	 * 原表名
	 */
	private String srcTable;

	/**
	 * 原这段
	 */
	private String srcField;
	

	private String desField;

	/**
	 * 对应的 字典表
	 */
	private String codeTable;
	


	@JsonProperty
	@Column(name = "SRCTABLE", length = 100)
	public String getSrcTable() {
		return srcTable;
	}

	public void setSrcTable(String srcTable) {
		this.srcTable = srcTable;
	}

	@JsonProperty
	@Column(name = "SRCFIELD", length = 100)
	public String getSrcField() {
		return srcField;
	}

	public void setSrcField(String srcField) {
		this.srcField = srcField;
	}

	@JsonProperty
	@Column(name = "CODETABLE", length = 100)
	public String getCodeTable() {
		return codeTable;
	}

	public void setCodeTable(String codeTable) {
		this.codeTable = codeTable;
	}

	@JsonProperty
	@Column(name = "DESFIELD", length = 100)
	public String getDesField() {
		return desField;
	}

	public void setDesField(String desField) {
		this.desField = desField;
	}

}