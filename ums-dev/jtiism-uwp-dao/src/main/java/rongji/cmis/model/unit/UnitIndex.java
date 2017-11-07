package rongji.cmis.model.unit;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 单位指标数据
 * @Title: UnitIndex.java
 * @Package rongji.cmis.model
 * @version V1.0
 */
@Entity
@Table(name = "UNIT_INDEX")
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE)
public class UnitIndex implements Serializable {

	private static final long serialVersionUID = -958254880278863008L;

	/** code */
	private String code;

	/** 单位编码 */
	private String unitCode;

	/** 名称 */
	private String codeName;

	/** 名称简写一 */
	private String codeAbr1;

	/** 名称简写二 */
	private String codeAbr2;

	/** 上级代码 */
	private String supCode;

	/** 首字母拼音 */
	private String codeSpelling;

	/** 排序号 */
	private Integer inino;

	/** 别名 */
	private String codeAName;

	/** 所在级别 */
	private Double codeLevel;

	/** 是否有叶子 */
	private String codeLeaf;

	/** 助记码 */
	private String codeAssist;

	/** 是否有效 */
	private String invalid;

	/** 起始时间 */
	private String startDate;

	/** 结束时间 */
	private String stopDate;

	/** 代码层级 */
	private String dmLevCod;

	/** 指标表名 */
	private String dmGrp;

	/** 是否显示 */
	private Short yesPrv;

	private Integer attribute;

	@JsonProperty
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@Column(name = "CODE", unique = true, nullable = false, length = 36)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name = "UNIT_CODE")
	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
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

	@Column(name = "DmLevCod")
	public String getDmLevCod() {
		return dmLevCod;
	}

	public void setDmLevCod(String dmLevCod) {
		this.dmLevCod = dmLevCod;
	}

	@Column(name = "DmGrp")
	public String getDmGrp() {
		return dmGrp;
	}

	public void setDmGrp(String dmGrp) {
		this.dmGrp = dmGrp;
	}

	@Column(name = "YesPrv")
	public Short getYesPrv() {
		return yesPrv;
	}

	public void setYesPrv(Short yesPrv) {
		this.yesPrv = yesPrv;
	}

	@Column(name = "Attribute")
	public Integer getAttribute() {
		return attribute;
	}

	public void setAttribute(Integer attribute) {
		this.attribute = attribute;
	}

}
