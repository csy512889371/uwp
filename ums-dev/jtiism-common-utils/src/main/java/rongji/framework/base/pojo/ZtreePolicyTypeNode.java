package rongji.framework.base.pojo;

import java.io.Serializable;

/**
 * unit ztree 节点数据
 */
public class ZtreePolicyTypeNode implements Serializable {


	private String id;

	private String pid;

	private String name;

	private String fullName;

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


	public ZtreePolicyTypeNode(String id, String pid, String name, String fullName) {
		this.id = id;
		this.pid = pid;
		this.name = name;
		this.fullName = fullName;
	}
}
