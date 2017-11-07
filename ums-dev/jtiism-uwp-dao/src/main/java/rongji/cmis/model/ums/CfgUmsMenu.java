package rongji.cmis.model.ums;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import rongji.framework.base.model.CfgBaseEntity;

import javax.persistence.*;

/**
 * CfgUmsMenu entity.
 */
@Entity
@Table(name = "CFG_UMS_MENU")
public class CfgUmsMenu extends CfgBaseEntity {

	private static final long serialVersionUID = -7479276853683910640L;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 菜单类型
	 */
	private Short menutype;

	/**
	 * 点击后前往的地址 菜单才有
	 */
	private String url;

	/**
	 * 父Id
	 */
	private CfgUmsMenu parent;

	/**
	 * 菜单标识符 用于权限匹配的 如sys:resource
	 */
	private String code;

	/**
	 * 是否可用
	 */
	private Boolean state;

	private Short sort;

	private String remark;
	
	private String bigicon;
	
	private String smallicon;

	private String type;
	
	// Constructors

	/** default constructor */
	public CfgUmsMenu() {
	}

	public CfgUmsMenu(Integer id) {
		super.setId(id);
	}

	@JsonProperty
	@Column(name = "TITLE", length = 30)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty
	@Column(name = "MENUTYPE")
	public Short getMenutype() {
		return this.menutype;
	}

	public void setMenutype(Short menutype) {
		this.menutype = menutype;
	}

	@JsonProperty
	@Column(name = "URL", length = 100)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@JsonProperty
	@Column(name = "CODE", length = 100)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
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

	@JsonProperty
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	@JoinColumn(name = "PARENTID")
	public CfgUmsMenu getParent() {
		return parent;
	}

	public void setParent(CfgUmsMenu parent) {
		this.parent = parent;
	}

	@JsonProperty
	@Column(name = "BIG_ICON", length = 20)
	public String getBigicon() {
		return this.bigicon;
	}

	public void setBigicon(String bigicon) {
		this.bigicon = bigicon;
	}
	
	@JsonProperty
	@Column(name = "SMALL_ICON", length = 20)
	public String getSmallicon() {
		return this.smallicon;
	}

	public void setSmallicon(String smallicon) {
		this.smallicon = smallicon;
	}

	@JsonProperty
	@Column(name = "TYPE", length = 11)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}