package rongji.cmis.model.ums;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import rongji.cmis.model.unit.CmisDepartment;

@Entity
@Table(name = "ROLE_DEPT_RELA")
public class RoleDeptReal {

	private String roleDeptRealId;// 角色与管理权限关系标识
	private CmisDepartment cmisDepartment;// 管理权限标识
	private CfgUmsRole cfgUmsRole;// 角色标识

	public RoleDeptReal() {
	}

	public RoleDeptReal(String roleDeptRealId, CmisDepartment cmisDepartment, CfgUmsRole cfgUmsRole) {
		super();
		this.roleDeptRealId = roleDeptRealId;
		this.cmisDepartment = cmisDepartment;
		this.cfgUmsRole = cfgUmsRole;
	}

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ROLE_DEPT_RELA_ID", unique = true, nullable = false, length = 36)
	public String getRoleDeptRealId() {
		return roleDeptRealId;
	}

	public void setRoleDeptRealId(String roleDeptRealId) {
		this.roleDeptRealId = roleDeptRealId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEPT_ID", nullable = false)
	public CmisDepartment getCmisDepartment() {
		return cmisDepartment;
	}

	public void setCmisDepartment(CmisDepartment cmisDepartment) {
		this.cmisDepartment = cmisDepartment;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_ID", nullable = false)
	public CfgUmsRole getCfgUmsRole() {
		return cfgUmsRole;
	}

	public void setCfgUmsRole(CfgUmsRole cfgUmsRole) {
		this.cfgUmsRole = cfgUmsRole;
	}

}
