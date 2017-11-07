package rongji.cmis.model.ums;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import rongji.framework.base.model.CfgBaseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CfgUmsRoleUser entity.
 */
@Entity
@Table(name = "CFG_UMS_ROLE_USER")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "CFG_UMS_ROLE_USER_SEQUENCE")
public class CfgUmsRoleUser extends CfgBaseEntity {
 
	private static final long serialVersionUID = 3598357104615002957L;

	// Fields
	private Integer roleid;
	
	private Integer userid;

	// Constructors

	/** default constructor */
	public CfgUmsRoleUser() {
	}

	@Column(name = "ROLEID")
	@JsonProperty
	public Integer getRoleid() {
		return this.roleid;
	}

	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}

	@Column(name = "USERID")
	@JsonProperty
	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

}