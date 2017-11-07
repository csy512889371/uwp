package rongji.cmis.model.ums;

import java.io.Serializable;

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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@Entity
@Table(name = "USER_DEPT_RELA")
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE)
public class UserDeptRela implements Serializable {
	/**
	* @Fields serialVersionUID : (用一句话描述这个变量表示什么)
	*/ 
	private static final long serialVersionUID = 2999021929605294412L;
	private String userDeptRelaId;
	private CmisDepartment deptId;
	private CfgUmsUser cfgUmsUser;
	
	@JsonProperty
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "USER_DEPT_RELA_ID", unique = true, nullable = false)
	public String getUserDeptRelaId() {
		return userDeptRelaId;
	}
	public void setUserDeptRelaId(String userDeptRelaId) {
		this.userDeptRelaId = userDeptRelaId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEPT_ID")
	public CmisDepartment getDeptId() {
		return deptId;
	}
	public void setDeptId(CmisDepartment deptId) {
		this.deptId = deptId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	public CfgUmsUser getCfgUmsUser() {
		return cfgUmsUser;
	}
	public void setCfgUmsUser(CfgUmsUser cfgUmsUser) {
		this.cfgUmsUser = cfgUmsUser;
	}
	
	
}
