package rongji.cmis.model.ums;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import rongji.framework.base.model.CfgBaseEntity;

/**
 * CfgUmsRole entity.
 */
@Entity
@Table(name = "CFG_UMS_ROLE")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "CFG_UMS_ROLE_SEQUENCE")
public class CfgUmsRole extends CfgBaseEntity {

	private static final long serialVersionUID = 6735890290375436822L;

	/**
	 * 名称
	 */
	private String rolename;

	/**
	 * 状态 false:弃用 true启用
	 */
	private Boolean state;

	// TODO　delete
	private Short sort;

	/**
	 * 描述
	 */
	private String remark;

	/**
	 * 角色标识
	 */
	private String code;

	/**
	 * 用户 组织机构 工作职务关联表
	 */
	private List<CfgUmsRoleMenuOper> menuOpers;

	public CfgUmsRole() {
	}

	@JsonProperty
	@Column(name = "ROLENAME", length = 60)
	public String getRolename() {
		return this.rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	@JsonProperty
	@Column(name = "STATE")
	public Boolean getState() {
		return this.state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

	@JsonProperty
	@Column(name = "SORT")
	public Short getSort() {
		return this.sort;
	}

	public void setSort(Short sort) {
		this.sort = sort;
	}

	@JsonProperty
	@Column(name = "REMARK", length = 300)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = CfgUmsRoleMenuOper.class, mappedBy = "role", orphanRemoval = true)
	@Fetch(FetchMode.SELECT)
	@Basic(optional = true, fetch = FetchType.EAGER)
	@OrderBy
	public List<CfgUmsRoleMenuOper> getMenuOpers() {
		if (menuOpers == null) {
			menuOpers = Lists.newArrayList();
		}
		return menuOpers;
	}

	public void setMenuOpers(List<CfgUmsRoleMenuOper> menuOpers) {
		this.menuOpers = menuOpers;
	}
	
	public void addMenuOpers(CfgUmsRoleMenuOper menuOper) {
		menuOper.setRole(this);
		this.getMenuOpers().add(menuOper);
	}

	@JsonProperty
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}