package rongji.cmis.model.ums;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import rongji.framework.base.model.CfgBaseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CfgUmsMenuOper entity.
 */
@Entity
@Table(name = "CFG_UMS_MENU_OPER")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "CFG_UMS_MENU_OPER_SEQUENCE")
public class CfgUmsMenuOper extends CfgBaseEntity {

	private static final long serialVersionUID = -8726741539269381195L;

	/**
	 * 所属菜单Id
	 */
	private Integer menuid;

	/**
	 * 操作名称
	 */
	private String opername;

	/**
	 * 权限标识
	 */
	private String permission;

	public CfgUmsMenuOper() {
	}

	@JsonProperty
	@Column(name = "MENUID")
	public Integer getMenuid() {
		return this.menuid;
	}

	public void setMenuid(Integer menuid) {
		this.menuid = menuid;
	}

	@JsonProperty
	@Column(name = "OPERNAME", length = 20)
	public String getOpername() {
		return this.opername;
	}

	public void setOpername(String opername) {
		this.opername = opername;
	}

	@JsonProperty
	@Column(name = "PERMISSION")
	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

}