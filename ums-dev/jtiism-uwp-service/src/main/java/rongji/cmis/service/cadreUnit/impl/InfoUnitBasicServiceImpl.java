package rongji.cmis.service.cadreUnit.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import rongji.cmis.dao.cadreUnit.B01HiberDao;
import rongji.cmis.dao.cadreUnit.InfoUnitBasicDao;
import rongji.cmis.model.unit.B01;
import rongji.cmis.model.unit.B01Hiber;
import rongji.cmis.model.unit.UnitIndex;
import rongji.cmis.service.cadreUnit.InfoUnitBasicService;
import rongji.cmis.service.cadreUnit.UnitIndexService;
import rongji.framework.base.exception.ApplicationException;
import rongji.framework.base.pojo.Filter;
import rongji.framework.base.pojo.Filter.Operator;
import rongji.framework.base.service.impl.BaseServiceImpl;
import rongji.framework.util.LetterMatchUtil;
import rongji.framework.util.ParamRequest;
import rongji.framework.util.StringUtil;

@Service("infoUnitBasicServiceImpl")
public class InfoUnitBasicServiceImpl extends BaseServiceImpl<B01> implements InfoUnitBasicService {
	private static final Logger logger = LoggerFactory.getLogger(InfoUnitBasicServiceImpl.class);

	@Resource(name = "infoUnitBasicDaoImpl")
	InfoUnitBasicDao infoUnitBasicDao;

	@Resource(name = "b01HiberDaoImpl")
	B01HiberDao b01HiberDao;

	@Resource(name = "unitIndexServiceImpl")
	UnitIndexService unitIndexService;

	@Override
	public B01 queryUnitById(String id) {
		return infoUnitBasicDao.find(id);
	}

	/**
	 * 保存新增单位
	 */
	public void saveUnit(B01 b01, B01Hiber b01Hiber) {

		try {
			// 保存基本单位信息
			this.save(b01);
			// 保存b01Hiber
			B01 unit = infoUnitBasicDao.load(b01.getB00());
			b01Hiber.setUnit(unit);

			// 保存单位关系
			this.saveB01Hiber(b01Hiber);

			this.saveZb02(b01);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	/**
	 * 保存ZB02
	 * 
	 * @param b01
	 * @throws Exception
	 */
	public void saveZb02(B01 b01) throws Exception {

		UnitIndex zb02Table = new UnitIndex();

		zb02Table.setYesPrv(Short.valueOf("1"));
		zb02Table.setAttribute(7);
		zb02Table.setCodeName(b01.getB0101());

		String b0104 = b01.getB0104();
		if (StringUtils.isEmpty(b0104)) {
			b0104 = b01.getB0101();
		}
		zb02Table.setCodeAbr1(b0104);
		zb02Table.setCodeAbr2(b0104);
		zb02Table.setCodeAName(b0104);
		zb02Table.setCodeSpelling(LetterMatchUtil.getFirstSpell(b01.getB0101()));
		zb02Table.setInvalid("1");
		zb02Table.setInino(unitIndexService.getMaxInino());

		// 单位编码
		if (StringUtil.isNotEmpty(b01.getB0111())) {
			boolean isCodeExist = unitIndexService.isUnitCodeExist(b01.getB0111());
			if (isCodeExist) {
				throw new ApplicationException("zb02表已有记录，不用保存zb02！");
			}

			zb02Table.setUnitCode(b01.getB0111());
		}

		zb02Table.setCode(b01.getB00());
		unitIndexService.save(zb02Table);

	}

	/**
	 * 保存ZB02
	 * 
	 * @param b01
	 * @throws Exception
	 */
	public void saveOrUpdateZb02(B01 b01) {
		if (b01 == null) {
			return;
		}
		// 单位id
		if (StringUtil.isNotEmpty(b01.getB00())) {
			boolean isCodeExist = unitIndexService.isCodeExist(b01.getB00());
			if (isCodeExist) {
				return;
			}
		}

		UnitIndex zb02Table = new UnitIndex();
		zb02Table.setYesPrv(Short.valueOf("1"));
		zb02Table.setAttribute(7);
		zb02Table.setCodeName(b01.getB0101());

		String b0104 = b01.getB0104();
		if (StringUtils.isEmpty(b0104)) {
			b0104 = b01.getB0101();
		}
		zb02Table.setCodeAbr1(b0104);
		zb02Table.setCodeAbr2(b0104);
		zb02Table.setCodeAName(b0104);
		zb02Table.setCodeSpelling(LetterMatchUtil.getFirstSpell(b01.getB0101()));
		zb02Table.setInvalid("1");
		zb02Table.setInino(unitIndexService.getMaxInino());
		zb02Table.setUnitCode(b01.getB0111());
		zb02Table.setCode(b01.getB00());
		unitIndexService.save(zb02Table);
	}

	/**
	 * 保存b01Hiber,单位归口关系
	 * 
	 * @param b01Hiber
	 * @throws Exception
	 */
	public void saveB01Hiber(B01Hiber b01Hiber) throws Exception {
		B01Hiber unitHiber = new B01Hiber();
		unitHiber.setUnit(b01Hiber.getUnit());
		unitHiber.setIsHidden(b01Hiber.getIsHidden());
		// 设置parentB01Hiber,设置parentUnit
		B01Hiber parentB01Hiber = b01Hiber.getParentB01Hiber();
		if (parentB01Hiber == null) {
			setUnitGroupAndLib(b01Hiber, unitHiber);
		} else {
			String parentHiberId = b01Hiber.getParentB01Hiber().getId();
			if (parentHiberId == null) {
				setUnitGroupAndLib(b01Hiber, unitHiber);
			} else {
				setUnitGroupAndLib(parentHiberId, unitHiber);
			}
		}

		b01HiberDao.save(unitHiber);

	}

	private void setUnitGroupAndLib(String parentHiberId, B01Hiber unitHiber) {
		B01Hiber parentHiber = b01HiberDao.find(parentHiberId);
		if (parentHiber == null) {
			return;
		}
		unitHiber.setParentB01Hiber(parentHiber);
		unitHiber.setParentUnit(parentHiber.getUnit());
		// 父节点层级
		unitHiber.setUpLev(parentHiber.getUpLev() + 1);
		// 排序号
		Integer inino = b01HiberDao.getNewInino(parentHiber.getId());
		unitHiber.setInino(inino);

	}

	private void setUnitGroupAndLib(B01Hiber b01Hiber, B01Hiber unitHiber) {

		unitHiber.setParentB01Hiber(null);
		unitHiber.setParentUnit(null);
		unitHiber.setUpLev(0);
		Integer inino = b01HiberDao.getNewInino(0);
		unitHiber.setInino(inino);
	}

	@Override
	public List<B01> findAllUnitByParamRequest(ParamRequest paramRequest) {
		baseDao.getSession().clear();
		baseDao.findAllByParamRequest(paramRequest);
		return null;
	}

	@Override
	public List<Map<String, Object>> getHiberListByUnitGroupId(String dmcod) {
		return infoUnitBasicDao.getHiberListByUnitGroupId(dmcod);
	}

	@Override
	public Integer getCountUnitByInfrq(String libraryId, Integer infrq) {
		return infoUnitBasicDao.getCountUnitByInfrq(libraryId, infrq);
	}

	@Override
	public void updateUnit(B01 b01, B01Hiber b01Hiber, String b01HiberId) {

		B01 oldB01 = infoUnitBasicDao.find(b01.getB00());
		oldB01.setB0101(b01.getB0101());
		oldB01.setB0104(b01.getB0104());
		oldB01.setB0111(b01.getB0111());
//		oldB01.setEgxb0114(b01.getEgxb0114());
		oldB01.setB0131(b01.getB0131());
		oldB01.setB0131n(b01.getB0131n());
//		oldB01.setEgb0147(b01.getEgb0147());
		oldB01.setB0124(b01.getB0124());
		oldB01.setB0124n(b01.getB0124n());
//		oldB01.setEgb0121(b01.getEgb0121());
		oldB01.setB0127(b01.getB0127());
		oldB01.setB0127n(b01.getB0127n());
		oldB01.setB0120(b01.getB0120());
		oldB01.setB0120n(b01.getB0120n());

		String tempB01HiberId = null;
		if (StringUtil.isNotEmpty(b01HiberId)) {// 修改单位
			tempB01HiberId = b01HiberId;
		} else {
			tempB01HiberId = b01Hiber.getId();
		}
		B01Hiber oldB01Hiber = b01HiberDao.find(tempB01HiberId);
		B01Hiber tempParentB01Hiber = b01Hiber.getParentB01Hiber();// 父单位
		if (null != tempParentB01Hiber && StringUtil.isNotEmpty(tempParentB01Hiber.getId())) {
			tempParentB01Hiber = b01HiberDao.find(tempParentB01Hiber.getId());
			if (null != tempParentB01Hiber.getUpLev()) {// 父节点层级
				oldB01Hiber.setUpLev(tempParentB01Hiber.getUpLev() + 1);
			} else {
				oldB01Hiber.setUpLev(0);
			}
			oldB01Hiber.setParentUnit(tempParentB01Hiber.getUnit());
			oldB01Hiber.setParentB01Hiber(tempParentB01Hiber);
		} else {// 没有父节点
			oldB01Hiber.setUpLev(0);
			oldB01Hiber.setParentUnit(null);
			oldB01Hiber.setParentB01Hiber(null);
		}

		oldB01Hiber.setIsHidden(b01Hiber.getIsHidden());
		b01HiberDao.merge(oldB01Hiber);// 保存单位、归口、库关系

		B01 newB01 = oldB01;
		newB01 = (B01) this.merge(newB01);
		b01Hiber.setUnit(newB01);

		this.updateZb02(newB01, new ParamRequest());

	}

	/**
	 * @Title: UpdateZb02
	 * @Description: (更新ZB02表)
	 * @param b01
	 * @param paramRequest
	 * @return void 返回类型
	 * @throws
	 * @author hbh
	 */
	private void updateZb02(B01 b01, ParamRequest paramRequest) {
		paramRequest.addFilter(new Filter("CODE", Operator.eq, b01.getB00()));
		List<UnitIndex> zb02List = unitIndexService.findAllByParamRequest(paramRequest);
		if (zb02List.size() == 0) {
			return;
		}
		UnitIndex zb02Table = zb02List.get(0);
		zb02Table.setYesPrv(Short.valueOf("1"));
		zb02Table.setAttribute(7);
		zb02Table.setCodeName(b01.getB0101());

		String b0104 = b01.getB0104();
		if (StringUtils.isEmpty(b0104)) {
			b0104 = b01.getB0101();
		}
		zb02Table.setCodeAbr1(b0104);
		zb02Table.setCodeAbr2(b0104);
		zb02Table.setCodeAName(b0104);
		zb02Table.setCodeSpelling(LetterMatchUtil.getFirstSpell(b01.getB0101()));
		zb02Table.setInvalid("1");
		if (StringUtil.isNotEmpty(b01.getB0111())) {
			zb02Table.setUnitCode(b01.getB0111());
		}
		zb02Table = (UnitIndex) unitIndexService.merge(zb02Table);
	}

	@Override
	public List<Map<String, Object>> getHiberListByUnitId(String unitId, String libraryId) {
		return infoUnitBasicDao.getHiberListByUnitId(unitId, libraryId);
	}

	@Override
	public Integer getMaxPunitLevByDmcod(String dmcod) {
		return infoUnitBasicDao.getMaxPunitLevByDmcod(dmcod);
	}

	@Override
	public List<Map<String, Object>> getSupHiberListByUnitHiberId(String unitHiberId, String type) {
		return infoUnitBasicDao.getSupHiberListByUnitHiberId(unitHiberId, type);
	}

	@Override
	public List<Map<String, Object>> getAllHiberListByUnitGroupId() {
		return infoUnitBasicDao.getAllHiberListByUnitGroupId();
	}

	@Override
	public Integer getAllMaxPunitLevByDmcod(String dmcod) {
		return infoUnitBasicDao.getAllMaxPunitLevByDmcod(dmcod);
	}

	@Override
	public List<B01> getB01ListByRosterUnitId(String unitId) {

		return infoUnitBasicDao.getB01ListByRosterUnitId(unitId);
	}

	@Override
	public List<B01> findB01ByB0111(String b0111) {
		List<B01> b01List = new ArrayList<B01>();
		if (StringUtils.isNotEmpty(b0111)) {
			b01List = infoUnitBasicDao.findB01ByB0111(b0111);
		}
		return b01List;
	}

	@Override
	public Boolean isAuthInUnit(String auth) {
		return infoUnitBasicDao.isAuthInUnit(auth);
	}

}
