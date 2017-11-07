package rongji.cmis.service.cadreUnit;

import java.util.List;
import java.util.Map;

import rongji.cmis.model.unit.B01;
import rongji.cmis.model.unit.B01Hiber;
import rongji.framework.base.service.BaseService;
import rongji.framework.util.ParamRequest;

public interface InfoUnitBasicService extends BaseService<B01> {

	/**
	 * 
	 * @Title: queryUnitById
	 * @Description: (根据编号查询单位bean)
	 * @return
	 * @return B01 返回类型
	 * @throws
	 * @author Administrator
	 */
	public B01 queryUnitById(String id);

	public void saveUnit(B01 b01, B01Hiber b01Hiber);

	/**
	 * @Title: findAllUnitByParamRequest
	 * @Description: (hibernate session clear后查询)
	 * @param paramRequest
	 * @return
	 * @return List<B01> 返回类型
	 * @throws
	 * @author Administrator
	 */
	public List<B01> findAllUnitByParamRequest(ParamRequest paramRequest);

	/**
	 * 
	 * @Title: getHiberListByUnitGroupId
	 * @Description: 根据归口ID获取所有单位信息
	 * @param dmcod
	 *            归口ID
	 * @return
	 * @return List<B01Hiber> 返回类型
	 * @throws
	 * @author LFG
	 */
	List<Map<String, Object>> getHiberListByUnitGroupId(String dmcod);
	
	void saveOrUpdateZb02(B01 b01);
	
	/**
	 * 
	 * @Title: getHiberListByUnitId
	 * @Description: 根据归口ID获取所有单位信息
	 * @param unitId
	 *            单位ID
	 * @return
	 * @return List<B01Hiber> 返回类型
	 * @throws
	 * @author LFG
	 */
	List<Map<String, Object>> getHiberListByUnitId(String unitId, String libraryId);

	/**
	 * 
	 * @Title: getPreCountUnitByGroupId
	 * @Description: 查询所在归口之前所有归口的单位总和
	 * @param infrq
	 *            归口排序号
	 * @return
	 * @return Integer 返回类型
	 * @throws
	 * @author LFG
	 */
	public Integer getCountUnitByInfrq(String libraryId, Integer infrq);

	public void updateUnit(B01 b01, B01Hiber b01Hiber, String b01HiberId);

	public Integer getMaxPunitLevByDmcod(String dmcod);

	public List<Map<String, Object>> getSupHiberListByUnitHiberId(String unitHiberId, String type);

	public List<Map<String, Object>> getAllHiberListByUnitGroupId();

	public Integer getAllMaxPunitLevByDmcod(String dmcod);

	public List<B01> getB01ListByRosterUnitId(String unitId);
	
	public List<B01> findB01ByB0111(String b0111);

	Boolean isAuthInUnit(String auth);

}
