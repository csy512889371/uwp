package rongji.cmis.dao.cadreUnit;

import java.util.List;

import rongji.cmis.model.unit.B01Hiber;
import rongji.cmis.model.unit.B01.UnitType;
import rongji.framework.base.dao.GenericDao;

public interface B01HiberDao extends GenericDao<B01Hiber> {
	/**
	 * @Title: isExistUnitInfo
	 * @Description: (归口下是否存在单位信息)
	 * @param dmcod
	 * @return boolean 返回类型
	 * @throws
	 * @author hbh
	 */
	boolean isExistUnitInfo(String dmcod);

	List<B01Hiber> findB01HiberListByDmcod(String dmcod);

	List<B01Hiber> findVulB01HiberListByDmcod(String dmcod);

	List<B01Hiber> findB01HiberListByDmcodAndUnitType(String dmcod, UnitType unit);

	void unitSortSave(Integer inino, String b01HiberId);

	//TODO
	List<B01Hiber> findAllB01HiberList();

	B01Hiber getB01HiberByB00(String b00);
	
	List<B01Hiber> findAllB01HiberListByLibId(String libId);
	
	List<B01Hiber> getB01HiberListByB00(String b00, String impLibId);


	Integer getNewInino(String id);

	Integer getNewInino(Integer i);

	void delete_unit_index(String code); 
}
