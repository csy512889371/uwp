package rongji.cmis.model.unit;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@Entity
@Table(name = "UNIT_HIBER_RELA")
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE)
public class B01Hiber implements java.io.Serializable {

	private static final long serialVersionUID = -2586988033686126555L;

	private String id;

	/**
	 * 父节点
	 */
	private B01Hiber parentB01Hiber;

	/**
	 * B00 单位
	 */
	private B01 unit;

	/**
	 * 父单位
	 */
	private B01 parentUnit;

	/**
	 * 父节点层级
	 */
	private Integer upLev;
	

	private Set<B01Hiber> childB01Hibers;
	/**
	 * 排序号
	 */
	private Integer inino;

	/**
	 * true:隐藏，不显示.false:显示
	 */
	private Boolean isHidden;

	/** default constructor */
	public B01Hiber() {
		super();
	}

	@JsonProperty
	@GenericGenerator(name = "generator", strategy = "rongji.framework.base.dao.generater.UUIDKeyGen")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "B01_UNIT_LIBRARY_RELA_ID", unique = true, nullable = false, length = 36)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "B00", nullable = false)
	public B01 getUnit() {
		return unit;
	}

	public void setUnit(B01 unit) {
		this.unit = unit;
	}

	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PUNIT_ID", nullable = true)
	public B01 getParentUnit() {
		return parentUnit;
	}

	public void setParentUnit(B01 parentUnit) {
		this.parentUnit = parentUnit;
	}

	@JsonProperty
	@Column(name = "PUNIT_LEV", length = 11)
	public Integer getUpLev() {
		return upLev;
	}

	public void setUpLev(Integer upLev) {
		this.upLev = upLev;
	}

	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "P_B01_UNIT_LIBRARY_RELA_ID", nullable = true)
	public B01Hiber getParentB01Hiber() {
		return parentB01Hiber;
	}

	public void setParentB01Hiber(B01Hiber parentB01Hiber) {
		this.parentB01Hiber = parentB01Hiber;
	}

	@JsonProperty
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentB01Hiber")
	public Set<B01Hiber> getChildB01Hibers() {
		return childB01Hibers;
	}

	public void setChildB01Hibers(Set<B01Hiber> childB01Hibers) {
		this.childB01Hibers = childB01Hibers;
	}

	@Column(name = "ININO")
	public Integer getInino() {
		return inino;
	}

	public void setInino(Integer inino) {
		this.inino = inino;
	}

	@JsonProperty
	@Column(name = "isHidden")
	public Boolean getIsHidden() {
		return isHidden;
	}

	public void setIsHidden(Boolean isHidden) {
		this.isHidden = isHidden;
	}
	
	@Transient
	public boolean isHasParent() {
		if (this.parentB01Hiber != null && StringUtils.isNotEmpty(this.parentB01Hiber.getId())) {
			return true;
		}
		return false;
	}
}
