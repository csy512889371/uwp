package rongji.cmis.model.ums;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * 
 * @Title: 对应中组织部颁标准代码
 * @version V1.0
 */
@Entity(name = "CFG_UMS_DATADICT")
public class CfgUmsDataDict implements Serializable {

	private static final long serialVersionUID = -3794225734003011464L;

	public CfgUmsDataDict() {
	}

	/**
	 * 代码
	 */
	private String code;

	/**
	 * 名称
	 */
	private String codeName;

	/**
	 * 名称简写
	 */
	private String codeAbr1;

	/**
	 * 名称简写二
	 */
	private String codeAbr2;

	/**
	 * 上级代码
	 */
	private String supCode;

	/**
	 * 首字母拼音
	 */
	private String codeSpelling;

	/**
	 * 排序号
	 */
	private Integer inino;

	/**
	 * 别名
	 */
	private String codeAName;

	/**
	 * 所在级别
	 */
	private Double codeLevel;

	/**
	 * 是否有叶子
	 */
	private String codeLeaf;

	/**
	 * 助记码
	 */
	private String codeAssist;

	/**
	 * 是否有效
	 */
	private String invalid;

	/**
	 * 起始时间
	 */
	private String startDate;

	/**
	 * 结束时间
	 */
	private String stopDate;

	/**
	 * 代码层级
	 */
	private String dmLevCod;

	/**
	 * 指标表名
	 */
	private String dmGrp;

	/**
	 * 是否显示
	 */
	private Short yesPrv;

	/**
	 * 
	 */
	private Integer attribute;

	/**
	 * 
	 */
	private String unitCode;
	/**
	 * 是否常用代码
	 */
	private Short isCommon;
	
	/**
	 * 是否标准指标 0：标准， 1：扩展
	 */
	private Short isStand;
	
	// code name 后缀，用于指标显示是的后缀显示。
	private String codeNameSuffix;

	@Id
	@Column(name = "CODE", unique = true, nullable = false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "CODE_NAME", length = 80)
	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	@Column(name = "CODE_ABR1", length = 60)
	public String getCodeAbr1() {
		return codeAbr1;
	}

	public void setCodeAbr1(String codeAbr1) {
		this.codeAbr1 = codeAbr1;
	}

	@Column(name = "CODE_ABR2", length = 60)
	public String getCodeAbr2() {
		return codeAbr2;
	}

	public void setCodeAbr2(String codeAbr2) {
		this.codeAbr2 = codeAbr2;
	}

	@Column(name = "SUP_CODE", length = 8)
	public String getSupCode() {
		return supCode;
	}

	public void setSupCode(String supCode) {
		this.supCode = supCode;
	}

	@Column(name = "CODE_SPELLING", length = 100)
	public String getCodeSpelling() {
		return codeSpelling;
	}

	public void setCodeSpelling(String codeSpelling) {
		this.codeSpelling = codeSpelling;
	}

	@Column(name = "ININO")
	public Integer getInino() {
		return inino;
	}

	public void setInino(Integer inino) {
		this.inino = inino;
	}

	@Column(name = "CODE_ANAME")
	public String getCodeAName() {
		return codeAName;
	}

	public void setCodeAName(String codeAName) {
		this.codeAName = codeAName;
	}

	@Column(name = "CODE_LEVEL")
	public Double getCodeLevel() {
		return codeLevel;
	}

	public void setCodeLevel(Double codeLevel) {
		this.codeLevel = codeLevel;
	}

	@Column(name = "CODE_LEAF")
	public String getCodeLeaf() {
		return codeLeaf;
	}

	public void setCodeLeaf(String codeLeaf) {
		this.codeLeaf = codeLeaf;
	}

	@Column(name = "CODE_ASSIST")
	public String getCodeAssist() {
		return codeAssist;
	}

	public void setCodeAssist(String codeAssist) {
		this.codeAssist = codeAssist;
	}

	@Column(name = "INVALID")
	public String getInvalid() {
		return invalid;
	}

	public void setInvalid(String invalid) {
		this.invalid = invalid;
	}

	@Column(name = "START_DATE")
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	@Column(name = "STOP_DATE")
	public String getStopDate() {
		return stopDate;
	}

	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
	}

	@Column(name = "IS_COMMON")
	public Short getIsCommon() {
		return isCommon;
	}

	public void setIsCommon(Short isCommon) {
		this.isCommon = isCommon;
	}
	
	@Column(name = "IS_STAND")
	public Short getIsStand() {
		return isStand;
	}

	public void setIsStand(Short isStand) {
		this.isStand = isStand;
	}

	public String getDmLevCod() {
		return dmLevCod;
	}

	public void setDmLevCod(String dmLevCod) {
		this.dmLevCod = dmLevCod;
	}

	public String getDmGrp() {
		return dmGrp;
	}

	public void setDmGrp(String dmGrp) {
		this.dmGrp = dmGrp;
	}

	public Short getYesPrv() {
		return yesPrv;
	}

	public void setYesPrv(Short yesPrv) {
		this.yesPrv = yesPrv;
	}

	public Integer getAttribute() {
		return attribute;
	}

	public void setAttribute(Integer attribute) {
		this.attribute = attribute;
	}

	@Transient
	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((codeAName == null) ? 0 : codeAName.hashCode());
		result = prime * result + ((codeName == null) ? 0 : codeName.hashCode());
		result = prime * result + ((codeSpelling == null) ? 0 : codeSpelling.hashCode());
		result = prime * result + ((dmGrp == null) ? 0 : dmGrp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CfgUmsDataDict other = (CfgUmsDataDict) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (codeAName == null) {
			if (other.codeAName != null)
				return false;
		} else if (!codeAName.equals(other.codeAName))
			return false;
		if (codeName == null) {
			if (other.codeName != null)
				return false;
		} else if (!codeName.equals(other.codeName))
			return false;
		if (codeSpelling == null) {
			if (other.codeSpelling != null)
				return false;
		} else if (!codeSpelling.equals(other.codeSpelling))
			return false;
		if (dmGrp == null) {
			if (other.dmGrp != null)
				return false;
		} else if (!dmGrp.equals(other.dmGrp))
			return false;
		return true;
	}

	@Transient
	public String getCodeNameSuffix() {
		return codeNameSuffix;
	}

	public void setCodeNameSuffix(String codeNameSuffix) {
		this.codeNameSuffix = codeNameSuffix;
	}
	
	

}
