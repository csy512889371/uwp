package rongji.framework.base.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @Title: UnitNode.java
 * @Package rongji.framework.base.model
 * @Description: 单位节点实现类
 * @author LFG
 * @date 2016年6月14日 下午7:15:00
 * @version V1.0
 */
public class UnitNode implements Comparable<UnitNode> {

	private Integer maxInino;
	private Integer inino;
	private String unitId;
	private String unitName;
	private List<UnitNode> childrenNodes;
	private UnitNode parentNode;
	private Integer punitLev;
	private Integer unitType;
	private String b01HiberId;

	public boolean isEndNode() {
		if (childrenNodes == null || childrenNodes.isEmpty()) {
			return true;
		}
		return false;
	}

	public int compareTo(UnitNode o) {
		return this.getInino().compareTo(o.getInino());
	}

	public Integer getInino() {
		return inino;
	}

	public void setInino(Integer inino) {
		this.inino = inino;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public List<UnitNode> getChildren() {
		return childrenNodes;
	}

	public void setChildrenNodes(List<UnitNode> childrenNodes) {
		this.childrenNodes = childrenNodes;
	}

	public UnitNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(UnitNode parentNode) {
		this.parentNode = parentNode;
	}

	public Integer getPunitLev() {
		return punitLev;
	}

	public void setPunitLev(Integer punitLev) {
		this.punitLev = punitLev;
	}

	public List<UnitNode> getChildrenNodes() {
		if (this.childrenNodes == null) {
			childrenNodes = new ArrayList<UnitNode>();
		}
		return childrenNodes;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((unitId == null) ? 0 : unitId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnitNode other = (UnitNode) obj;
		if (unitId == null) {
			if (other.unitId != null)
				return false;
		} else if (!unitId.equals(other.unitId))
			return false;
		return true;
	}

	public Integer getMaxInino() {
		return maxInino;
	}

	public void setMaxInino(Integer maxInino) {
		this.maxInino = maxInino;
	}

	public Integer getUnitType() {
		return unitType;
	}

	public void setUnitType(Integer unitType) {
		this.unitType = unitType;
	}

	public String getB01HiberId() {
		return b01HiberId;
	}

	public void setB01HiberId(String b01HiberId) {
		this.b01HiberId = b01HiberId;
	}

}
