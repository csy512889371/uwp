package rongji.cmis.model.ums;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import rongji.framework.base.model.CfgBaseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CfgUmsUser entity.
 */
@Entity
@Table(name = "CFG_UMS_USER")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "CFG_UMS_USER_SEQUENCE")
public class CfgUmsUser extends CfgBaseEntity {

	/** "用户名"Cookie名称 */
	public static final String USERNAME_COOKIE_NAME = "username";

	private static final long serialVersionUID = -2728689659288305217L;

	private String username;
	private String password;
	private Boolean state;
	private Short sort;
	private String remark;
	private CfgUmsGroup cfgUmsGroup;
	private List<CfgTopmenuUser> cfgTopmenuUsers = new ArrayList<CfgTopmenuUser>(0);
	private List<UserDeptRela> userDeptRelas = new ArrayList<UserDeptRela>(0);

	// Constructors

	/** default constructor */
	public CfgUmsUser() {
	}

	@JsonProperty
	@Column(name = "USERNAME", length = 50)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "PASSWORD", length = 60)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CFG_UMS_GROUP_ID", nullable = true)
	public CfgUmsGroup getCfgUmsGroup() {
		return cfgUmsGroup;
	}

	public void setCfgUmsGroup(CfgUmsGroup cfgUmsGroup) {
		this.cfgUmsGroup = cfgUmsGroup;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cfgUmsUser")
	public List<CfgTopmenuUser> getCfgTopmenuUsers() {
		return cfgTopmenuUsers;
	}

	public void setCfgTopmenuUsers(List<CfgTopmenuUser> cfgTopmenuUsers) {
		this.cfgTopmenuUsers = cfgTopmenuUsers;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cfgUmsUser")
	public List<UserDeptRela> getUserDeptRelas() {
		return userDeptRelas;
	}

	public void setUserDeptRelas(List<UserDeptRela> userDeptRelas) {
		this.userDeptRelas = userDeptRelas;
	}

}