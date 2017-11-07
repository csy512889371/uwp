package rongji.cmis.model.ums;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "CFG_UMS_GROUP")
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE)
public class CfgUmsGroup implements Serializable {

	/**
	 * @Fields serialVersionUID : (用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 5564653759455222374L;

	private String id;
	private CfgUmsGroup parentGroup;
	private String groupName;
	private String groupDesc;
	private Integer seqno;

	private Set<CfgUmsGroup> childGroups = new LinkedHashSet<CfgUmsGroup>();
	private Set<CfgUmsUser> users = new LinkedHashSet<CfgUmsUser>();

	@JsonProperty
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "CFG_UMS_GROUP_ID", unique = true, nullable = false)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_GROUP_ID", nullable = true)
	public CfgUmsGroup getParentGroup() {
		return parentGroup;
	}

	public void setParentGroup(CfgUmsGroup parentGroup) {
		this.parentGroup = parentGroup;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentGroup")
	@OrderBy("seqno ASC")
	public Set<CfgUmsGroup> getChildGroups() {
		return childGroups;
	}

	public void setChildGroups(Set<CfgUmsGroup> childGroups) {
		this.childGroups = childGroups;
	}

	@JsonProperty
	@Column(name = "GROUP_NAME", length = 100)
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@JsonProperty
	@Column(name = "GROUP_DESC", length = 200)
	public String getGroupDesc() {
		return groupDesc;
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cfgUmsGroup")
	public Set<CfgUmsUser> getUsers() {
		return users;
	}

	public void setUsers(Set<CfgUmsUser> users) {
		this.users = users;
	}

	@JsonProperty
	@Column(name = "SEQNO")
	public Integer getSeqno() {
		return seqno;
	}

	public void setSeqno(Integer seqno) {
		this.seqno = seqno;
	}

}