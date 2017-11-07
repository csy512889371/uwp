package rongji.cmis.dao.cadreUnit;

import java.util.List;

import rongji.cmis.model.ums.CfgUmsDataDict;
import rongji.cmis.model.unit.UnitIndex;
import rongji.framework.base.dao.GenericDao;
import rongji.framework.base.pojo.ZtreeDictNote;

/**
 * 单位指标 Dao
 */
public interface UnitIndexDao extends GenericDao<UnitIndex> {

	Integer getMaxInino();

	List<ZtreeDictNote> getUnitIndexTreeList();
	
	/**
	 * 查询单位 存在于ZB02 and 不存在于B01的数据
	 */
	List<CfgUmsDataDict> getListExcludeB01();

	void unitSortSave(Integer inino, String code);
	
	List<CfgUmsDataDict> getUnitIndexByUnitCode(String unitCode);
	
	Integer getMaxPunitLev();

}
