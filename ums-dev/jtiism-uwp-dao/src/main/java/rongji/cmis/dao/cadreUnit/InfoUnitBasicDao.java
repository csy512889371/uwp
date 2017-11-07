package rongji.cmis.dao.cadreUnit;

import rongji.cmis.model.unit.B01;
import rongji.framework.base.dao.GenericDao;

import java.util.List;
import java.util.Map;

public interface InfoUnitBasicDao extends GenericDao<B01> {

    List<Map<String, Object>> getHiberListByUnitGroupId(String dmcod);

    Integer getCountUnitByInfrq(String libraryId, Integer infrq);

    List<Map<String, Object>> getHiberListByUnitId(String unitId, String libraryId);

    Integer getMaxPunitLevByDmcod(String dmcod);

    List<Map<String, Object>> getSupHiberListByUnitHiberId(String unitId, String type);

    Integer getAllMaxPunitLevByDmcod(String dmcod);

    List<B01> getB01ListByRosterUnitId(String unitId);

    List<String> getSubB01HiberIdsByUnitId(String b01HiberId);

    List<String> getSubUnitIdsByUnitId(String tempB01HiberId);

    List<B01> findB01ByB0111(String b0111);

    List<Map<String, Object>> getAllHiberListByUnitGroupId();

    Boolean isAuthInUnit(String auth);
}
