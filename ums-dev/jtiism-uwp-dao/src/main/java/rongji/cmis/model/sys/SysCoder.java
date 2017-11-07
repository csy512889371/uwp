package rongji.cmis.model.sys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import rongji.framework.base.model.BaseEntity;

/**
 * 系统数据字典
 * 
 * @version V1.0
 */
@Entity
@Table(name = "SHJT_SYS_CODER")
public class SysCoder extends BaseEntity {

	private static final long serialVersionUID = -6477623261063396688L;

	/**
	 * 表名
	 */
	private String tabName;

	/**
	 * 字段名
	 */
	private String colName;

	/**
	 * 字段描述
	 */
	private String colDesc;

	/**
	 * 值
	 */
	private String val;

	/**
	 * 值描述
	 */
	private String valDesc;

	/**
	 * 值简拼
	 */
	private String codeSpelling;

	/**
	 * 状态
	 */
	private Integer state;

	/**
	 * 状态变更时间
	 */
	private Date stateChntime;

	@JsonProperty
	@Column(name = "TAB_NAME", length = 20)
	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	@JsonProperty
	@Column(name = "COL_NAME", length = 20)
	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	@JsonProperty
	@Column(name = "COL_DESC", length = 20)
	public String getColDesc() {
		return colDesc;
	}

	public void setColDesc(String colDesc) {
		this.colDesc = colDesc;
	}

	@JsonProperty
	@Column(name = "VAL", length = 20)
	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	@JsonProperty
	@Column(name = "CODE_SPELLING", length = 20)
	public String getCodeSpelling() {
		return codeSpelling;
	}

	public void setCodeSpelling(String codeSpelling) {
		this.codeSpelling = codeSpelling;
	}

	@JsonProperty
	@Column(name = "VAL_DESC", length = 20)
	public String getValDesc() {
		return valDesc;
	}

	public void setValDesc(String valDesc) {
		this.valDesc = valDesc;
	}

	@JsonProperty
	@Column(name = "STATE", length = 20)
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@JsonProperty
	// 与前端表单中的日期格式一致
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:dd:ss")
	// DB 中的时间 转成 Json
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "STATE_CHNTIME")
	public Date getStateChntime() {
		return stateChntime;
	}

	public void setStateChntime(Date stateChntime) {
		this.stateChntime = stateChntime;
	}

}
