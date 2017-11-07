package rongji.report.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @Title: 报表数据-层级关系
 */
public class DataEntry {

	/**
	 * 层级
	 */
	private int level = 0;

	/**
	 * 节点名称
	 */
	private String name = null;

	/**
	 * 父节点
	 */
	private DataEntry parent = null;

	/**
	 * 本节点数据
	 */
	private Map<String, Object> dataInfo = null;

	/**
	 * 子节点
	 */
	private List<DataEntry> childs = null;

	public DataEntry() {
		super();
	}

	public DataEntry(String name) {
		super();
		this.name = name;
	}

	public DataEntry(Map<String, Object> dataInfo) {
		super();
		this.dataInfo = dataInfo;
	}

	public void addChild(DataEntry child) {
		if (child == null) {
			return;
		}

		child.setLevel(level + 1);
		child.setParent(this);
		getChilds().add(child);
	}

	/**
	 * 是否有节点数据： 有：true 没有： false
	 */
	public boolean isHasData() {
		if (dataInfo == null || dataInfo.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * 下级节点——总数据
	 */
	public List<Map<String, Object>> datas() {

		if (isLeaf()) {
			return null;
		}

		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		for (DataEntry dataEntry : childs) {
			if (dataEntry == null || !dataEntry.isHasData()) {
				continue;
			}

			datas.add(dataEntry.getDataInfo());
		}

		return datas;
	}

	/**
	 * 是否叶子节点
	 */
	public boolean isLeaf() {
		if (this.childs == null || this.childs.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * 层级
	 */
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * 节点名称
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getDataInfo() {
		return dataInfo;
	}

	public void setDataInfo(Map<String, Object> dataInfo) {
		this.dataInfo = dataInfo;
	}

	public DataEntry getParent() {
		return parent;
	}

	public void setParent(DataEntry parent) {
		this.parent = parent;
	}

	public List<DataEntry> getChilds() {
		if (childs == null) {
			childs = new ArrayList<DataEntry>();
		}
		return childs;
	}

	public void setChilds(List<DataEntry> childs) {
		this.childs = childs;
	}

	public static void main(String[] args) {
		DataEntry root = new DataEntry("root");
		for (int i = 0; i < 10; i++) {
			DataEntry unitGroup = new DataEntry("归口" + i);
			root.addChild(unitGroup);
			for (int j = 0; j < 10; j++) {
				DataEntry unit = new DataEntry("单位" + i + "_" + j);
				unitGroup.addChild(unit);

				for (int k = 0; k < 10; k++) {
					DataEntry cadre = new DataEntry("企业" + i + "_" + j + "_" + k);
					unit.addChild(cadre);
				}
			}
		}

		iterator(root, root);
		
		
		//
	}

	private static void iterator(DataEntry currentNode, DataEntry root) {
		
		if (currentNode.parent == null) {
			//ROOT
		}
		
		if (currentNode.parent != null) {
			int level = currentNode.getLevel();
			// TODO preDeal()

			// TODO script
			
			System.out.println(currentNode.getName() + "_level_" + level);
			// TODO doneDeal()
		}
		
		//递归
		if (!currentNode.isLeaf()) {
			List<DataEntry> list = currentNode.getChilds();
			if (list != null && list.size() > 0) {
				for (DataEntry nodeEle : list) {
					iterator(nodeEle, root);
				}
			}
		}
	}

}
