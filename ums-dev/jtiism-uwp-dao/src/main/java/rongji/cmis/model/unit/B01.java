package rongji.cmis.model.unit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * B01 entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "UNIT_INFO")
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE)
public class B01 implements java.io.Serializable {

	private static final long serialVersionUID = -2651829779538285168L;

	public enum UnitType {

		/** 单位 */
		UNIT,

		/** 分组 */
		SUBGROUP,

		/** 虚单位 */
		VUNIT
	}

	// Fields

	private String b00;
	/**
	 * 单位代码
	 */
	private String b0111;
	/**
	 * 全称
	 */
	private String b0101;
	/**
	 * 简称
	 */
	private String b0104;
	/**
	 * 单位隶属关系
	 */
	private String b0124;
	/**
	 *
	 */
	private String b0124n;
	/**
	 * 单位级别
	 */
	private String b0127;
	/**
	 * 单位性质类别
	 */
	private String b0131;
	/**
	 *
	 */
	private String b0127n;
	/**
	 *
	 */
	private String b0131n;
	/**
	 * 单位类型
	 */
	private UnitType unitType;

	/**
	 * 管理权限
	 */
	private String b0120;
	/**
	 *
	 */
	private String b0120n;


	// Constructors

	/** default constructor */
	public B01() {
	}

	@JsonProperty
	@GenericGenerator(name = "generator", strategy = "rongji.framework.base.dao.generater.UUIDKeyGen")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "B00", unique = true, nullable = false, length = 36)
	public String getB00() {
		return this.b00;
	}

	public void setB00(String b00) {
		this.b00 = b00;
	}

	@Column(name = "B0111", length = 150)
	@JsonProperty
	public String getB0111() {
		return this.b0111;
	}

	public void setB0111(String b0111) {
		this.b0111 = b0111;
	}

	@Column(name = "B0101", length = 120)
	@JsonProperty
	public String getB0101() {
		return this.b0101;
	}

	public void setB0101(String b0101) {
		this.b0101 = b0101;
	}

	@Column(name = "B0104", length = 20)
	@JsonProperty
	public String getB0104() {
		return this.b0104;
	}

	public void setB0104(String b0104) {
		this.b0104 = b0104;
	}


	@Column(name = "B0124", length = 2)
	public String getB0124() {
		return this.b0124;
	}

	public void setB0124(String b0124) {
		this.b0124 = b0124;
	}

	@Column(name = "B0127", length = 4)
	public String getB0127() {
		return this.b0127;
	}

	public void setB0127(String b0127) {
		this.b0127 = b0127;
	}

	@Column(name = "B0131", length = 3)
	public String getB0131() {
		return this.b0131;
	}

	public void setB0131(String b0131) {
		this.b0131 = b0131;
	}


	@Column(name = "B0127N", length = 100)
	public String getB0127n() {
		return this.b0127n;
	}

	public void setB0127n(String b0127n) {
		this.b0127n = b0127n;
	}

	@Column(name = "B0131N", length = 100)
	public String getB0131n() {
		return this.b0131n;
	}

	public void setB0131n(String b0131n) {
		this.b0131n = b0131n;
	}

	@Column(name = "B0124N", length = 100)
	public String getB0124n() {
		return this.b0124n;
	}

	public void setB0124n(String b0124n) {
		this.b0124n = b0124n;
	}

	@JsonProperty
	@Column(name = "UNITTYPE")
	public UnitType getUnitType() {
		return unitType;
	}

	public void setUnitType(UnitType unitType) {
		this.unitType = unitType;
	}

	@JsonProperty
	@Column(name = "B0120")
	public String getB0120() {
		return b0120;
	}

	public void setB0120(String b0120) {
		this.b0120 = b0120;
	}

	@JsonProperty
	@Column(name = "B0120N")
	public String getB0120n() {
		return b0120n;
	}

	public void setB0120n(String b0120n) {
		this.b0120n = b0120n;
	}
}