package rongji.cmis.service.cadreUnit.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import rongji.cmis.dao.cadreUnit.B01HiberDao;
import rongji.cmis.dao.cadreUnit.InfoUnitBasicDao;
import rongji.cmis.model.unit.B01Hiber;
import rongji.cmis.model.unit.UnitIndex;
import rongji.cmis.model.unit.B01.UnitType;
import rongji.cmis.service.cadreUnit.B01HiberService;
import rongji.cmis.service.cadreUnit.UnitIndexService;
import rongji.framework.base.pojo.Filter;
import rongji.framework.base.pojo.Filter.Logic;
import rongji.framework.base.pojo.Filter.Operator;
import rongji.framework.base.pojo.ZtreeUnitNote;
import rongji.framework.base.service.impl.BaseServiceImpl;
import rongji.framework.util.ParamRequest;

/**
 * DEMO > 干部管理 > 单位管理
 * 
 * @author Administrator
 *
 */
@Service("b01HiberServiceImpl")
public class B01HiberServiceImpl extends BaseServiceImpl<B01Hiber> implements B01HiberService {

	@Resource(name = "infoUnitBasicDaoImpl")
	InfoUnitBasicDao infoUnitBasicDao;

	@Resource(name = "b01HiberDaoImpl")
	B01HiberDao b01HiberDao;

	@Resource(name = "unitIndexServiceImpl")
	UnitIndexService unitIndexService;
	
	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	public void deleteById(String unitId, String b01HiberId) {
		ParamRequest paramRequest = new ParamRequest();
		paramRequest.addFilter(new Filter(Logic.and, "B00", Operator.eq, unitId));
		List<B01Hiber> b01HiberList = b01HiberDao.findAllByParamRequest(paramRequest);
		if (b01HiberList.size() < 2) {
			b01HiberDao.delete(b01HiberId);
			infoUnitBasicDao.delete(unitId);
		} else {
			b01HiberDao.delete(b01HiberId);
		}
		b01HiberDao.delete_unit_index(unitId);
		//this.deleteZb02(unitId);
	}
	
	/**
	 * 递归获取单位id列表
	 */
	public List<String> getUnitIdsRecursive(B01Hiber hiber, boolean containRoot) {
		List<String> units = new ArrayList<String>();
		if(hiber == null){
			return units;
		}
		recursiveSubUnits(hiber, units);
		
		if(containRoot) {
			units.add(hiber.getUnit().getB00());
		}
		return units;
	}
	
	/**
	 * 递归删除单位信息
	 */
	public void deleteRecursiveById(String b01HiberId, boolean delRoot) {
		B01Hiber hiber = b01HiberDao.find(b01HiberId);
		if (hiber == null) {
			return;
		}
		List<String> unitIds = new ArrayList<String>();

		// 递归机构树，获取所有机构树节点单位id
		recursiveSubUnits(hiber, unitIds);
		
		if(CollectionUtils.isNotEmpty(unitIds)) {
			
			//递归删除, 删除中间表B01Hiber，delRoot标志是否删除所选节点中间关系
			deleteB01Hiber(hiber, delRoot);
			
			if(delRoot) {	//如果删除根节点
				unitIds.add(hiber.getUnit().getB00());
			}
			
			//删除机构
			for(String unitId : unitIds) {
				// 3. 删除b01
				infoUnitBasicDao.delete(unitId);
			}
			
		}
	}

	private void recursiveSubUnits(B01Hiber hiber, List<String> unitIds) {
		Set<B01Hiber> hiberSet = hiber.getChildB01Hibers();
		if (hiberSet.size() > 0) {
			for (B01Hiber bh : hiberSet) {
				recursiveSubUnits(bh, unitIds);
				unitIds.add(bh.getUnit().getB00());
			}
		}
	}

	public void deleteB01Hiber(B01Hiber hiber, boolean delParent) {
		//利用Hibernate级联删除进行递归删除
		if(delParent) {
			b01HiberDao.delete(hiber);
		} else {
			Set<B01Hiber> hiberSet = hiber.getChildB01Hibers();
			
			if (hiberSet.size() > 0) {
				for (B01Hiber bh : hiberSet) {
					b01HiberDao.delete(bh);
				}
			}
			hiber.setChildB01Hibers(null);
		}
	}

	/**
	 * 删除ZB02
	 * 
	 * @param b01
	 * @throws Exception
	 */
	public void deleteZb02(String unitId) {
		UnitIndex zb02Table = unitIndexService.find(unitId);
		if (zb02Table != null) {
			unitIndexService.delete(unitId);
		}
	}

	@Override
	public boolean isExistUnitInfo(String dmcod) {
		return b01HiberDao.isExistUnitInfo(dmcod);
	}

	@Override
	public List<B01Hiber> findB01HiberListByDmcod(String dmcod) {
		return b01HiberDao.findB01HiberListByDmcod(dmcod);
	}

	@Override
	public List<B01Hiber> findVulB01HiberListByDmcod(String dmcod) {
		return b01HiberDao.findVulB01HiberListByDmcod(dmcod);
	}

	@Override
	public List<B01Hiber> findB01HiberListByDmcodAndUnitType(String dmcod, UnitType unit) {
		return b01HiberDao.findB01HiberListByDmcodAndUnitType(dmcod, unit);
	}

	@Override
	public ZtreeUnitNote getZtreeUnitNote(String b00) {
		ParamRequest paramRequest = new ParamRequest();
		paramRequest.addFilter(new Filter("unit.b00", Operator.eq, b00));
		List<B01Hiber> b01Hibers = b01HiberDao.findAllByParamRequest(paramRequest);
		B01Hiber unitHiber = b01Hibers.get(0);
		
		String pid = "";
		if (unitHiber != null && unitHiber.isHasParent()) {
			pid = unitHiber.getParentB01Hiber().getId();
		}
		
		ZtreeUnitNote unitNote = new ZtreeUnitNote(unitHiber.getId(), pid, unitHiber.getUnit().getB0101(), unitHiber.getUnit().getB0101(), ZtreeUnitNote.Type.unit, unitHiber.getUnit().getB00(), unitHiber.getUnit().getB0101());
		return unitNote;
	}

	@Override
	public void unitSortSave(Integer inino, String b01HiberId) {
		b01HiberDao.flush();
		b01HiberDao.unitSortSave(inino, b01HiberId);
	}

	@Override
	public List<B01Hiber> findAllB01HiberList() {
		return b01HiberDao.findAllB01HiberList();
	}

	@Override
	public B01Hiber getB01HiberByB00(String b00) {
		return b01HiberDao.getB01HiberByB00(b00);
	}

	@Override
	public List<B01Hiber> findAllB01HiberListByLibId(String libId) {
		return b01HiberDao.findAllB01HiberListByLibId(libId);
	}

	@Override
	public List<B01Hiber> getB01HiberListByB00(String b00, String impLibId) {
		return b01HiberDao.getB01HiberListByB00(b00, impLibId);
	}

}
