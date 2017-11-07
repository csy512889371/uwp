package rongji.cmis.model.unit;

import java.util.Set;

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
 * CmisDepartment  管理权限表
 */
@Entity
@Table(name = "CMIS_DEPARTMENT")
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE)
public class CmisDepartment implements java.io.Serializable {

	private static final long serialVersionUID = -4282329730945197561L;
	// Fields

	/**
	 * ID
	 */
	private String code;

	/**
	 * 管理权限名称
	 */
	private String deptname;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 排序
	 */
	private Integer inino;
	/**
	 * 管理权限名称拼音
	 */
	private String depSpelling;
	/**
	 * 管理权限名称简拼
	 */
	private String depBspelling;

	/**
	 * 管理权限名称简拼2
	 */
	private String codeAName;	


	/** default constructor */
	public CmisDepartment() {
	}

	/** full constructor */
	public CmisDepartment(CmisDepartment cmisDepartment, String deptname, String description, Integer inino, Integer status, String depSpelling, String depBspelling,
			Set<CmisDepartment> cmisDepartments) {
		this.deptname = deptname;
		this.description = description;
		this.inino = inino;
		this.depSpelling = depSpelling;
		this.depBspelling = depBspelling;
	}

	@GenericGenerator(name = "generator", strategy = "rongji.framework.base.dao.generater.UUIDKeyGen")
	@Id
	@GeneratedValue(generator = "generator")
	@JsonProperty
	@Column(name = "CODE", unique = true, nullable = false, length = 36)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@JsonProperty
	@Column(name = "CODE_NAME", length = 120)
	public String getDeptname() {
		return this.deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	@JsonProperty
	@Column(name = "DESCRIPTION", length = 200)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty
	@Column(name = "ININO")
	public Integer getInino() {
		return this.inino;
	}

	public void setInino(Integer inino) {
		this.inino = inino;
	}

	@JsonProperty
	@Column(name = "CODE_SPELLING", length = 100)
	public String getDepSpelling() {
		return this.depSpelling;
	}

	public void setDepSpelling(String depSpelling) {
		this.depSpelling = depSpelling;
	}

	@JsonProperty
	@Column(name = "CODE_ABR1", length = 50)
	public String getDepBspelling() {
		return this.depBspelling;
	}

	public void setDepBspelling(String depBspelling) {
		this.depBspelling = depBspelling;
	}

	@JsonProperty
	@Column(name = "CODE_ABR2", length = 50)
	public String getDepBspelling2() {
		return this.depBspelling;
	}

	public void setDepBspelling2(String depBspelling2) {
		this.depBspelling = depBspelling2;
	}

	@JsonProperty
	@Column(name = "CODE_ANAME", length = 50)
	public String getCodeAName() {
		return codeAName;
	}

	public void setCodeAName(String codeAName) {
		this.codeAName = codeAName;
	}

/*	@JsonProperty
	@Column(name = "DmGrp", length = 20)
	public String getdmGrp() {
		return dmGrp;
	}

	public void setdmGrp(String dmGrp) {
		this.dmGrp = dmGrp;
	}*/

}