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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CfgTopMenuUser entity.
 */
@Entity
@Table(name = "CFG_TOPMENU_USER")
public class CfgTopmenuUser implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7732394339861821511L;
	// Fields

	private String cfgTopmenuUserId;
	private CfgUmsUser cfgUmsUser;
	private CfgUmsMenu cfgUmsMenu;

	// Constructors

	/** default constructor */
	public CfgTopmenuUser() {
	}

	/** full constructor */
	public CfgTopmenuUser(CfgUmsUser cfgUmsUser, CfgUmsMenu cfgUmsMenu) {
		this.cfgUmsUser = cfgUmsUser;
		this.cfgUmsMenu = cfgUmsMenu;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@JsonProperty
	@GeneratedValue(generator = "generator")
	@Column(name = "CFG_TOPMENU_USER_ID", unique = true, nullable = false, length = 36)
	public String getCfgTopmenuUserId() {
		return this.cfgTopmenuUserId;
	}

	public void setCfgTopmenuUserId(String cfgTopmenuUserId) {
		this.cfgTopmenuUserId = cfgTopmenuUserId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	public CfgUmsUser getCfgUmsUser() {
		return this.cfgUmsUser;
	}

	public void setCfgUmsUser(CfgUmsUser cfgUmsUser) {
		this.cfgUmsUser = cfgUmsUser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MENU_ID")
	public CfgUmsMenu getCfgUmsMenu() {
		return this.cfgUmsMenu;
	}

	public void setCfgUmsMenu(CfgUmsMenu cfgUmsMenu) {
		this.cfgUmsMenu = cfgUmsMenu;
	}

}
