package rongji.cmis.service.cadreUnit;

import java.util.List;

import rongji.cmis.model.unit.B01Hiber;
import rongji.cmis.model.unit.B01.UnitType;
import rongji.framework.base.pojo.ZtreeUnitNote;
import rongji.framework.base.service.BaseService;

public interface B01HiberService extends BaseService<B01Hiber> {

	void deleteById(String unitId, String b01HiberId);

	/**
	 * @Title: deleteRecursiveById
	 * @Description: (根据给定的id递归删除单位信息)
	 * @param b01HiberId
	 * @return void 返回类型
	 * @throws
	 * @author wqq
	 */
	void deleteRecursiveById(String b01HiberId, boolean delRoot);

	/**
	 * @Title: isExistUnitInfo
	 * @Description: (归口下是否存在单位信息)
	 * @param dmcod
	 * @return boolean 返回类型
	 * @throws
	 * @author hbh
	 */
	boolean isExistUnitInfo(String dmcod);

	/**
	 * 
	 * @Title: findB01HiberListByDmcod
	 * @Description: 根据归口ID查询单位列表并根据单位排序(实单位)
	 * @param dmcod
	 * @return
	 * @return List<B01Hiber> 返回类型
	 * @throws
	 * @author LFG
	 */
	List<B01Hiber> findB01HiberListByDmcod(String dmcod);

	/**
	 * 
	 * @Title: findVulB01HiberListByDmcod
	 * @Description: 根据归口ID查询单位列表并根据单位排序(虚单位)
	 * @param dmcod
	 * @return
	 * @return List<B01Hiber> 返回类型
	 * @throws
	 * @author LFG
	 */
	List<B01Hiber> findVulB01HiberListByDmcod(String dmcod);

	ZtreeUnitNote getZtreeUnitNote(String b00);

	List<B01Hiber> findB01HiberListByDmcodAndUnitType(String dmcod, UnitType unit);

	/**
	 * 
	 * @Title: unitSortSave
	 * @Description: 排序
	 * @param inino
	 * @param b01HiberId
	 * @return void 返回类型
	 * @throws
	 * @author LFG
	 */
	void unitSortSave(Integer inino, String b01HiberId);

	List<B01Hiber> findAllB01HiberList();

	/**
	 * 
	 * @Title: getB01HiberByB00
	 * @Description: 根据机构ID获取单位与库数据
	 * @param b00
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author LFG
	 */
	B01Hiber getB01HiberByB00(String b00);
	
	
	/**
	* @Title: getUnitIdsRecursive
	* @Description: (根据指定的b01HiberId获取其子机构id列表)
	* @param b01HiberId
	* @param containRoot 是否返回结果包含b01HiberId参数指定的根节点
	* @return
	* @return List<String>    返回类型
	* @throws
	* @author wqq 
	*/ 
	List<String> getUnitIdsRecursive(B01Hiber hiber, boolean containRoot);
	
	/**
	* @Title: deleteB01Hiber
	* @Description: (递归删除中间关系b01Hiber)
	* @param hiber
	* @param delParent 是否删除hiber参数指定的节点
	* @return void    返回类型
	* @throws
	* @author wqq 
	*/ 
	void deleteB01Hiber(B01Hiber hiber, boolean delParent);
	
	/**
	 * @Title: getAllB01HiberListByLibId
	 * @Description: (根据库id查询单位列表并根据单位排序)
	 * @param libId
	 * @return HuJingqiang
	 */
	List<B01Hiber> findAllB01HiberListByLibId(String libId);

	List<B01Hiber> getB01HiberListByB00(String b00, String impLibId);
}
