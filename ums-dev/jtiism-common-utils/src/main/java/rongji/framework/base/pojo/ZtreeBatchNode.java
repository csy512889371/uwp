package rongji.framework.base.pojo;

public class ZtreeBatchNode {
	private String id;
	private String pId;
	private String name;
	private String nodeType;
	private boolean hasChild;
	private boolean checked;
	private boolean nocheck;
	
	public ZtreeBatchNode(String id, String pId, String name, String nodeType, boolean hasChild, boolean nocheck, boolean checked) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.nodeType = nodeType;
		this.hasChild = hasChild;
		this.checked = checked;
		this.nocheck = nocheck;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public boolean isHasChild() {
		return hasChild;
	}
	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}
	public boolean getChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public boolean isNocheck() {
		return nocheck;
	}
	public void setNocheck(boolean nocheck) {
		this.nocheck = nocheck;
	}
	
	
}
