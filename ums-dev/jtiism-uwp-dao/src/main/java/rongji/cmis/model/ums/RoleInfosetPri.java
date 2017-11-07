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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@Entity
@Table(name = "ROLE_INFOSET_PRI")
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE)
public class RoleInfosetPri implements java.io.Serializable{
	private static final long serialVersionUID = -6477623261063396688L;
	/**
	 * 角色信息集信息项权限标识
	 */
	private String roleInfosetPriId;
	/**
	 * 角色
	 */
	private CfgUmsRole cfgUmsRole;
	/**
	 * 0：信息集 1：信息项
	 */
	private Integer infoType;
	/**
	 * 权限编码
	 */
	private String privCode;
	/**
	 * 权限 0：只读 1：可编辑
	 */
	private Integer priv;

	public RoleInfosetPri() {
	}

	public RoleInfosetPri(String roleInfosetPriId, CfgUmsRole cfgUmsRole, Integer infoType, String privCode, Integer priv) {
		this.roleInfosetPriId = roleInfosetPriId;
		this.cfgUmsRole = cfgUmsRole;
		this.infoType = infoType;
		this.privCode = privCode;
		this.priv = priv;
	}

	public void setRoleInfosetPriId(String _roleInfosetPriId) {
		this.roleInfosetPriId = _roleInfosetPriId;
	}

	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ROLE_INFOSET_PRI_ID", unique = true, nullable = false, length = 36)
	public String getRoleInfosetPriId() {
		return this.roleInfosetPriId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_ID")
	public CfgUmsRole getCfgUmsRole() {
		return cfgUmsRole;
	}

	public void setCfgUmsRole(CfgUmsRole cfgUmsRole) {
		this.cfgUmsRole = cfgUmsRole;
	}

	public void setInfoType(Integer infoType) {
		this.infoType = infoType;
	}

	@Column(name = "INFO_TYPE")
	public Integer getInfoType() {
		return this.infoType;
	}

	public void setPrivCode(String privCode) {
		this.privCode = privCode;
	}

	@Column(name = "PRIV_CODE", length = 50)
	public String getPrivCode() {
		return this.privCode;
	}

	public void setPriv(Integer priv) {
		this.priv = priv;
	}

	@Column(name = "PRIV")
	public Integer getPriv() {
		return this.priv;
	}
}
