package rongji.framework.base.pojo;

import java.io.Serializable;

public class ZtreeAdjustPlanNote implements Serializable {

	public enum Type {
		// 管理权限
		department,

		// 调配方案
		adjustPlan,
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7720667689934814925L;

	// Type=department时,表示管理权限id,Type=adjustPlan时,表示调配方案id
	private String id;

	// Type=department时,pid=null,Type=adjustPlan时,表示管理权限id
	private String pid;

	// 节点名称: 管理权限/调配方案名称
	private String name;

	// 节点类型
	private Type type;

	// Type=department时,表示管理权限id,Type=adjustPlan时,表示调配方案所属管理权限id
	private String departmentId;

	// Type=department时,表示管理权限名称,Type=adjustPlan时,表示调配方案所属管理权限名称
	private String departmentName;

	// Type=adjustPlan时,表示调配方案id
	private String adjustPlanId;

	// Type=adjustPlanName时,表示调配方案名称
	private String adjustPlanName;

	// 父节点层级
	private Integer pAdjustPlanLev;

	public ZtreeAdjustPlanNote() {
	}
	
	public ZtreeAdjustPlanNote(String id, String name, Type type) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
	}

	public ZtreeAdjustPlanNote(String id, String pid, String name, Type type, String departmentId, String departmentName, String adjustPlanId, String adjustPlanName) {
		super();
		this.id = id;
		this.pid = pid;
		this.name = name;
		this.type = type;
		this.departmentId = departmentId;
		this.departmentName = departmentName;
		this.adjustPlanId = adjustPlanId;
		this.adjustPlanName = adjustPlanName;
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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getAdjustPlanId() {
		return adjustPlanId;
	}

	public void setAdjustPlanId(String adjustPlanId) {
		this.adjustPlanId = adjustPlanId;
	}

	public String getAdjustPlanName() {
		return adjustPlanName;
	}

	public void setAdjustPlanName(String adjustPlanName) {
		this.adjustPlanName = adjustPlanName;
	}

	public Integer getpAdjustPlanLev() {
		return pAdjustPlanLev;
	}

	public void setpAdjustPlanLev(Integer pAdjustPlanLev) {
		this.pAdjustPlanLev = pAdjustPlanLev;
	}

}
