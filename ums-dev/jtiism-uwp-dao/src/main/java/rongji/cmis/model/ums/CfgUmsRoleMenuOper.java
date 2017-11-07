package rongji.cmis.model.ums;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.fasterxml.jackson.annotation.JsonProperty;

import rongji.framework.base.dao.type.CollectionToStringUserType;
import rongji.framework.base.model.CfgBaseEntity;

/**
 * CfgUmsRoleMenuOper 角色对应的 菜单 和 操作 权限
 */
@TypeDef(
		name = "SetToStringUserType", 
		typeClass = CollectionToStringUserType.class, 
		parameters = { 
			@Parameter(name = "separator", value = ","),
			@Parameter(name = "collectionType", value = "java.util.HashSet"), 
			@Parameter(name = "elementType", value = "java.lang.Integer") 
			}
		)
@Entity
@Table(name = "CFG_UMS_ROLE_MENU_OPER")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "CFG_UMS_ROLE_MENU_SEQUENCE")
public class CfgUmsRoleMenuOper extends CfgBaseEntity {

	private static final long serialVersionUID = -6295260308983660372L;

	/**
	 * 所属角色
	 */
	private CfgUmsRole role;

	/**
	 * 菜单Id
	 */
	private Integer menuid;

	/**
	 * 操作集
	 */
	private Set<Integer> operIds;

	public CfgUmsRoleMenuOper() {
	}

	public CfgUmsRoleMenuOper(Integer menuid, Set<Integer> operIds) {
		super();
		this.menuid = menuid;
		this.operIds = operIds;
	}

	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	public CfgUmsRole getRole() {
		return role;
	}

	public void setRole(CfgUmsRole role) {
		this.role = role;
	}

	@JsonProperty
	@Column(name = "MENUID")
	public Integer getMenuid() {
		return this.menuid;
	}

	public void setMenuid(Integer menuid) {
		this.menuid = menuid;
	}

	@Column(name = "operation_ids")
	@Type(type = "SetToStringUserType")
	public Set<Integer> getOperIds() {
		return operIds;
	}

	public void setOperIds(Set<Integer> operIds) {
		this.operIds = operIds;
	}

}