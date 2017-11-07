package rongji.cmis.service.cadreUnit.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import rongji.cmis.dao.cadreUnit.UnitIndexDao;
import rongji.cmis.model.ums.CfgUmsDataDict;
import rongji.cmis.model.unit.UnitIndex;
import rongji.cmis.service.cadreUnit.B01HiberService;
import rongji.cmis.service.cadreUnit.InfoUnitBasicService;
import rongji.cmis.service.cadreUnit.UnitIndexService;
import rongji.framework.base.model.UnitNode;
import rongji.framework.base.pojo.Filter;
import rongji.framework.base.pojo.Filter.Logic;
import rongji.framework.base.pojo.Filter.Operator;
import rongji.framework.base.pojo.ZtreeDictNote;
import rongji.framework.base.service.impl.BaseServiceImpl;
import rongji.framework.util.ParamRequest;

/**
 * 单位指标 service
 */
@Service("unitIndexServiceImpl")
public class UnitIndexServiceImpl extends BaseServiceImpl<UnitIndex> implements UnitIndexService {

	@Resource(name = "unitIndexDaoImpl")
	UnitIndexDao unitIndexDao;

	@Resource(name = "unitIndexServiceImpl")
	UnitIndexService unitIndexService;

	@Resource(name = "infoUnitBasicServiceImpl")
	InfoUnitBasicService infoUnitBasicService;

	@Resource(name = "b01HiberServiceImpl")
	B01HiberService b01HiberService;


	@Override
	public Integer getMaxInino() {
		return unitIndexDao.getMaxInino();
	}

	@Override
	public boolean isUnitCodeExist(String code) {
		ParamRequest paramRequest = new ParamRequest();
		paramRequest.addFilter(new Filter(Logic.and, "unitCode", Operator.eq, code));
		List<UnitIndex> zb02List = unitIndexDao.findAllByParamRequest(paramRequest);
		if (zb02List == null || zb02List.isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isCodeExist(String code) {
		ParamRequest paramRequest = new ParamRequest();
		paramRequest.addFilter(new Filter(Logic.and, "code", Operator.eq, code));
		List<UnitIndex> zb02List = unitIndexDao.findAllByParamRequest(paramRequest);
		if (zb02List == null || zb02List.isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public List<ZtreeDictNote> getUnitIndexTreeList() {
		return unitIndexDao.getUnitIndexTreeList();
	}

	@Override
	public List<ZtreeDictNote> buildUnitTreeNoteList() {
		List<ZtreeDictNote> treeNoteList = this.getUnitIndexTreeList();
		
		List<ZtreeDictNote> treeNoteListCopy = new ArrayList<ZtreeDictNote>(Arrays.asList(new ZtreeDictNote[treeNoteList.size()]));
		Collections.copy(treeNoteListCopy, treeNoteList);
		for (ZtreeDictNote treeNote : treeNoteList) {
			updateIsParent(treeNoteListCopy, treeNote.getSupCode());

			String abr1 = treeNote.getCodeAbr1();
			if (StringUtils.isEmpty(abr1)) {
				treeNote.setCodeAbr1(treeNote.getCodeName());
			}

			String abr2 = treeNote.getCodeAbr2();
			if (StringUtils.isEmpty(abr2)) {
				treeNote.setCodeAbr2(treeNote.getCodeName());
			}
		}
		
		return treeNoteList;
	}
	
	private void updateIsParent(List<ZtreeDictNote> treeNoteListCopy, String supCode) {
		if (StringUtils.isEmpty(supCode)) {
			return;
		}

		for (ZtreeDictNote treeNote : treeNoteListCopy) {
			if (supCode.equals(treeNote.getCode())) {
				treeNote.setIsParent(true);
				treeNoteListCopy.remove(treeNote);
				break;
			}
		}
	}

	@Override
	public List<CfgUmsDataDict> getListExcludeB01() {
		return unitIndexDao.getListExcludeB01();
	}

	@Override
	public void unitSortSave(Integer inino, String code) {
		unitIndexDao.unitSortSave(inino, code);
	}

	@Override
	public void unitsSortSave(String[] codeArr,String unitHiberId,String type) {

		Integer preCount = 0;
		List<Map<String, Object>> unitList = infoUnitBasicService.getAllHiberListByUnitGroupId();// 根据归口查询所有机构
		Integer maxPunitLev = unitIndexDao.getMaxPunitLev();// 当前最大层级
		UnitNode root = new UnitNode();
		root.setMaxInino(preCount);
		List<UnitNode> unitNodeList = new ArrayList<UnitNode>();
		if (maxPunitLev != null) {
			for (Integer unitLevel = 0; unitLevel <= maxPunitLev; unitLevel++) {
				for (Map<String, Object> unit : unitList) {
					String b01HiberId = (String) unit.get("b01HiberId");
					Integer level = (Integer) unit.get("PUNIT_LEV");
					// String parentId = (String) unit.get("PUNIT_ID");
					String parentB01HiberId = (String) unit.get("parentB01HiberId");
					UnitNode parentNote = findCurrentNode(unitNodeList, parentB01HiberId);
					if (parentNote == null) {
						parentNote = root;
					}
					if (unitLevel != level) {
						continue;
					}
					UnitNode node = new UnitNode();
					node.setUnitId((String) unit.get("B00"));
					node.setInino((Integer) unit.get("ININO"));
					node.setPunitLev((Integer) unit.get("PUNIT_LEV"));
					node.setUnitName((String) unit.get("B0101"));
					node.setParentNode(parentNote);
					node.setUnitType((Integer) unit.get("unitType"));
					node.setB01HiberId(b01HiberId);
					parentNote.getChildrenNodes().add(node);
					unitNodeList.add(node);
				}
			}
		}
		UnitNode selParentUnit = null;
		if (codeArr != null && codeArr.length > 0) {
			for (int i = 0; i < codeArr.length; i++) {
				String selUnitId = codeArr[i];
				UnitNode currentNode = findCurrentNode(unitNodeList, selUnitId);
				if (currentNode == null) {
					continue;
				}
				currentNode.setInino(i + 1);
				if (selParentUnit == null) {
					selParentUnit = currentNode.getParentNode();
				}
			}
		}
		if (selParentUnit != null && !selParentUnit.getChildrenNodes().isEmpty()) {
			Collections.sort(selParentUnit.getChildrenNodes());
		}
		this.iteratorUnit(root, root);
	}

	/**
	 * 
	 * @Title: iteratorUnit
	 * @Description: 迭代树排序
	 * @param currentNode
	 * @param root
	 * @return void 返回类型
	 * @throws
	 * @author LFG
	 */
	private void iteratorUnit(UnitNode currentNode, UnitNode root) {
		if (StringUtils.isNotEmpty(currentNode.getB01HiberId())) {
			Integer dmcodIndex = root.getMaxInino();
			root.setMaxInino(++dmcodIndex);
			currentNode.setInino(dmcodIndex);
			b01HiberService.unitSortSave(currentNode.getInino(), currentNode.getB01HiberId());
			if (StringUtils.isNotEmpty(currentNode.getUnitId())) {
				unitIndexDao.unitSortSave(currentNode.getInino(), currentNode.getUnitId());
			}

		}
		if (!currentNode.isEndNode()) {
			List<UnitNode> list = currentNode.getChildrenNodes();
			if (list != null && list.size() > 0) {
				for (UnitNode nodeEle : list) {
					iteratorUnit(nodeEle, root);
				}
			}
		}
	}

	private UnitNode findCurrentNode(List<UnitNode> unitNodeList, String b01HiberId) {
		if (StringUtils.isEmpty(b01HiberId) || unitNodeList == null || unitNodeList.isEmpty()) {
			return null;
		}

		for (UnitNode unitNode : unitNodeList) {
			if (b01HiberId.equals(unitNode.getB01HiberId())) {
				return unitNode;
			}
		}

		return null;
	}

	@Override
	public void sortUnitIndexAndB01Hiber(String[] codeArr,String unitHiberId,String type) {
		unitIndexService.unitsSortSave(codeArr,unitHiberId,type);
	}

	@Override
	public List<CfgUmsDataDict> getUnitIndexByUnitCode(String unitCode) {
		return unitIndexDao.getUnitIndexByUnitCode(unitCode);
	}

	@Override
	public List<ZtreeDictNote> reserveUnitTreeNoteList() {
		// TODO Auto-generated method stub
		return null;
	}
}
