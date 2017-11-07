package rongji.cmis.service.cadreUnit;

import java.util.List;

import rongji.cmis.model.ums.CfgUmsDataDict;
import rongji.cmis.model.unit.UnitIndex;
import rongji.framework.base.pojo.ZtreeDictNote;
import rongji.framework.base.service.BaseService;

public interface UnitIndexService extends BaseService<UnitIndex> {

	/**
	 * @Title: 获取单位排序号
	 * 
	 */
	Integer getMaxInino();

	/**
	 * @param curUnitId 
	 * @Title: isCodeExist
	 * @Description: 单位编码是否存在
	 * @return true 存在, false 不存在
	 */
	boolean isUnitCodeExist(String code);
	
	boolean isCodeExist(String code);

	List<ZtreeDictNote> getUnitIndexTreeList();

	List<ZtreeDictNote> buildUnitTreeNoteList();

	/**
	 * 查询单位 存在于ZB02 and 不存在于B01的数据
	 */
	List<CfgUmsDataDict> getListExcludeB01();

	void unitSortSave(Integer inino, String code);

	void unitsSortSave(String[] codeArr,String type,String unitHiberId);

	/**
	 * 
	 * @Title: sortUnitIndexAndB01Hiber
	 * @Description: 排序，ZB02，B01Hiber
	 * @param codeArr
	 * @return void 返回类型
	 * @throws
	 * @author LFG
	 */
	void sortUnitIndexAndB01Hiber(String[] codeArr,String unitHiberId,String type);
	
	List<CfgUmsDataDict> getUnitIndexByUnitCode(String unitCode);
	
	List<ZtreeDictNote> reserveUnitTreeNoteList();
}
