package rongji.framework.base.pojo;

import java.io.Serializable;

/**
 * unit ztree 节点数据
 */
public class ZtreeUnitNote implements Serializable {

	public enum Type {

		// 单位
		unit
	}

	private static final long serialVersionUID = 3668689627402827158L;

	// Type=unitGroup时,表示归口id, Type=unit时,表示hiber id
	private String id;

	// Type=unitGroup时,pid=null, Type=unit时,表示parent hiber id
	private String pid;

	// 节点名称: 单位/归口名称
	private String name;

	// 节点名称: 单位/归口全称
	private String fullName;

	// 节点类型
	private Type type;


	// Type=unit时, 表示单位id
	private String unitId;

	// Type=unit时, 表示单位名称
	private String unitName;

	// 归口ID
	private String dmcod;
	// 父节点层级
	private Integer punitLev;
	
	public ZtreeUnitNote() {
	}

	public ZtreeUnitNote(String id, String name, String fullName, Type type) {
		this.id = id;
		this.name = name;
		this.fullName = fullName;
		this.type = type;
	}

	public ZtreeUnitNote(String id, String pid, String name, String fullName, Type type) {
		this.id = id;
		this.pid = pid;
		this.name = name;
		this.fullName = fullName;
		this.type = type;
	}

	public ZtreeUnitNote(String id, String pid, String name, String fullName) {
		super();
		this.id = id;
		this.pid = pid;
		this.name = name;
		this.fullName = fullName;
	}

	public ZtreeUnitNote(String id, String pid, String name, String fullName, Type type, String unitId, String unitName) {
		super();
		this.id = id;
		this.pid = pid;
		this.name = name;
		this.fullName = fullName;
		this.type = type;
		this.unitId = unitId;
		this.unitName = unitName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getDmcod() {
		return dmcod;
	}

	public void setDmcod(String dmcod) {
		this.dmcod = dmcod;
	}

	public Integer getPunitLev() {
		return punitLev;
	}

	public void setPunitLev(Integer punitLev) {
		this.punitLev = punitLev;
	}

}
