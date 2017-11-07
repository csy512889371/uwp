package rongji.framework.base.pojo;

import java.io.Serializable;

/**
 * 
 * ztreeDict 节点数据
 *
 */
public class ZtreeDictNote implements Serializable {

	public enum Type {

		// 库
		library,

		// 归口
		unitGroup,

		// 单位
		unit,

		// 分组
		subGroup
	}

	private static final long serialVersionUID = -1870871099345195724L;

	// 代码
	private String code;

	// 名称
	private String codeName;

	// 名称简写一
	private String codeAbr1;

	// 名称简写二
	private String codeAbr2;

	// 上级代码
	private String supCode;

	// 首字母拼音
	private String codeSpelling;

	// 排序号
	private Integer inino;

	// 别名
	private String codeAName;

	// 所在级别
	private Double codeLevel;

	// 指标表名
	private String dmGrp;

	private Type noteType;

	private String unitCode;

	private String unitHiberId;

	// 单位类型 0:单位 1:分组
	private Integer unitType;

	// 是否常用代码
	private Short isCommon;

	private Boolean isHidden;

	// code name 后缀，用于指标显示是的后缀显示。
	private String codeNameSuffix;

	private Boolean isParent = false;

	public ZtreeDictNote() {
		super();
	}

	public ZtreeDictNote(String code, String codeName, String codeAbr1) {
		super();
		this.code = code;
		this.codeName = codeName;
		this.codeAbr1 = codeAbr1;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getCodeAbr1() {
		return codeAbr1;
	}

	public void setCodeAbr1(String codeAbr1) {
		this.codeAbr1 = codeAbr1;
	}

	public String getCodeAbr2() {
		return codeAbr2;
	}

	public void setCodeAbr2(String codeAbr2) {
		this.codeAbr2 = codeAbr2;
	}

	public String getSupCode() {
		return supCode;
	}

	public void setSupCode(String supCode) {
		this.supCode = supCode;
	}

	public String getCodeSpelling() {
		return codeSpelling;
	}

	public void setCodeSpelling(String codeSpelling) {
		this.codeSpelling = codeSpelling;
	}

	public Integer getInino() {
		return inino;
	}

	public void setInino(Integer inino) {
		this.inino = inino;
	}

	public String getCodeAName() {
		return codeAName;
	}

	public void setCodeAName(String codeAName) {
		this.codeAName = codeAName;
	}

	public Double getCodeLevel() {
		return codeLevel;
	}

	public void setCodeLevel(Double codeLevel) {
		this.codeLevel = codeLevel;
	}

	public String getDmGrp() {
		return dmGrp;
	}

	public void setDmGrp(String dmGrp) {
		this.dmGrp = dmGrp;
	}

	public Type getNoteType() {
		return noteType;
	}

	public void setNoteType(Type noteType) {
		this.noteType = noteType;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public Integer getUnitType() {
		return unitType;
	}

	public void setUnitType(Integer unitType) {
		this.unitType = unitType;
	}

	public Short getIsCommon() {
		return isCommon;
	}

	public void setIsCommon(Short isCommon) {
		this.isCommon = isCommon;
	}

	public Boolean getIsHidden() {
		return isHidden;
	}

	public void setIsHidden(Boolean isHidden) {
		this.isHidden = isHidden;
	}

	public String getCodeNameSuffix() {
		return codeNameSuffix;
	}

	public void setCodeNameSuffix(String codeNameSuffix) {
		this.codeNameSuffix = codeNameSuffix;
	}

	public String getUnitHiberId() {
		return unitHiberId;
	}

	public void setUnitHiberId(String unitHiberId) {
		this.unitHiberId = unitHiberId;
	}

	public Boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}

}
