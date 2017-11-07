package rongji.cmis.dao.cadreUnit.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import rongji.cmis.dao.cadreUnit.InfoUnitBasicDao;
import rongji.cmis.model.unit.B01;
import rongji.framework.base.dao.impl.GenericDaoImpl;
import rongji.framework.util.StringUtil;

@Repository("infoUnitBasicDaoImpl")
public class InfoUnitBasicDaoImpl extends GenericDaoImpl<B01> implements InfoUnitBasicDao {
    @Override
    public void save(B01 entity) {

        if (StringUtil.isEmpty(entity.getB00())) {
            entity.setB00(UUID.randomUUID().toString());
        }

        super.save(entity);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> getHiberListByUnitGroupId(String dmcod) {
        StringBuffer sql = new StringBuffer(" ");

        sql.append("SELECT b01.B00, b01.B0101, b01.B0111, b01.unitType, b01Hiber.DMCOD ,zb02.ININO ,zb02.CODE ,b01Hiber.PUNIT_LEV,b01Hiber.PUNIT_ID FROM UNIT_INFO as b01 ");
        sql.append("INNER JOIN UNIT_HIBER_RELA as b01Hiber ON b01.b00 = b01Hiber.b00 ");
        sql.append("INNER JOIN UNIT_INDEX AS zb02 ON b01.B00 = zb02.CODE ");
        sql.append("WHERE b01Hiber.DMCOD = :dmcod ");
        sql.append("ORDER BY zb02.ININO ASC");
        Query query = this.getSession().createSQLQuery(sql.toString());
        query.setParameter("dmcod", dmcod);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @Override
    public Integer getCountUnitByInfrq(String libraryId, Integer infrq) {

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("libraryId", libraryId);
        paramMap.put("infrq", infrq);
        String sql = this.findQueryByNamed("unitInfo.getCountUnitByInfrq", paramMap);
        Query query = this.createSQLQuery(sql, paramMap);

        return (Integer) query.list().get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> getHiberListByUnitId(String unitId, String libraryId) {
        StringBuffer sql = new StringBuffer();
        Query query = null;

        sql.append(" SELECT b01.B00, b01.B0101, b01.B0111, b01.unitType, b01Hiber.DMCOD ,zb02.ININO ,zb02.CODE FROM UNIT_INFO as b01 ");
        sql.append(" INNER JOIN UNIT_HIBER_RELA as b01Hiber ON b01.b00 = b01Hiber.b00 ");
        sql.append(" INNER JOIN UNIT_INDEX AS zb02 ON b01.B00 = zb02.CODE ");

        if (StringUtil.isEmpty(unitId)) {// 通过归口查询
            sql.append(" WHERE b01Hiber.DMCOD = :dmcod ");
            sql.append(" AND b01Hiber.PUNIT_LEV = 0 ");
            sql.append(" ORDER BY zb02.ININO ASC");
            query = this.getSession().createSQLQuery(sql.toString());
            query.setParameter("dmcod", libraryId);
            query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        } else {
            sql.append(" WHERE b01Hiber.PUNIT_ID = :unitId ");
            sql.append(" ORDER BY zb02.ININO ASC");
            query = this.getSession().createSQLQuery(sql.toString());
            query.setParameter("unitId", unitId);
            query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        }
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Integer getMaxPunitLevByDmcod(String dmcod) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT MAX(b01Hiber.PUNIT_LEV) AS maxPunitLev FROM UNIT_INFO as b01 ");
        sql.append("INNER JOIN UNIT_HIBER_RELA as b01Hiber ON b01.b00 = b01Hiber.b00 ");
        sql.append("INNER JOIN UNIT_INDEX AS zb02 ON b01.B00 = zb02.CODE ");
        sql.append("WHERE b01Hiber.DMCOD = :dmcod ");
        Query query = this.getSession().createSQLQuery(sql.toString());
        query.setParameter("dmcod", dmcod);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return (Integer) ((Map<String, Object>) query.list().get(0)).get("maxPunitLev");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> getSupHiberListByUnitHiberId(String unitHiberId, String type) {
        StringBuffer sql = new StringBuffer();
        Query query = null;

        sql.append(" SELECT b01.B00, b01.B0101, b01.B0111, b01.unitType, b01Hiber.DMCOD ,b01Hiber.ININO AS ININO,b01Hiber.B00 AS CODE,b01Hiber.B01_UNIT_LIBRARY_RELA_ID AS b01HiberId FROM UNIT_INFO as b01 ");
        sql.append(" INNER JOIN UNIT_HIBER_RELA as b01Hiber ON b01.b00 = b01Hiber.b00 ");

        sql.append(" WHERE b01Hiber.P_B01_UNIT_LIBRARY_RELA_ID = :unitHiberId ");
        sql.append(" ORDER BY b01Hiber.ININO ASC");
        query = this.getSession().createSQLQuery(sql.toString());
        query.setParameter("unitHiberId", unitHiberId);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Map<String, Object>> getAllHiberListByUnitGroupId() {
        StringBuffer sql = new StringBuffer(" ");

        sql.append("SELECT b01.B00, b01.B0101, b01.B0111, b01.unitType, b01Hiber.DMCOD ,b01Hiber.ININO ,b01Hiber.B00 AS CODE,b01Hiber.PUNIT_LEV,b01Hiber.PUNIT_ID,b01Hiber.B01_UNIT_LIBRARY_RELA_ID AS b01HiberId,P_B01_UNIT_LIBRARY_RELA_ID AS parentB01HiberId FROM UNIT_INFO as b01 ");
        sql.append("INNER JOIN UNIT_HIBER_RELA as b01Hiber ON b01.b00 = b01Hiber.b00 ");
        sql.append("ORDER BY b01Hiber.ININO ASC");
        Query query = this.getSession().createSQLQuery(sql.toString());
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Boolean isAuthInUnit(String auth) {
        String sql = "select count(*) from UNIT_INFO where B0120=:auth";
        SQLQuery query = this.getSession().createSQLQuery(sql);
        query.setParameter("auth", auth);
        int count = Integer.parseInt(query.list().get(0).toString());
        return count > 0 ? false : true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Integer getAllMaxPunitLevByDmcod(String dmcod) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT MAX(b01Hiber.PUNIT_LEV) AS maxPunitLev FROM B01 as b01 ");
        sql.append("INNER JOIN B01_UNIT_LIBRARY_RELA as b01Hiber ON b01.b00 = b01Hiber.b00 ");
        // sql.append("INNER JOIN ZB02 AS zb02 ON b01.B00 = zb02.CODE ");
        sql.append("WHERE b01Hiber.DMCOD = :dmcod ");
        // sql.append("AND b01.unitType <> :unitType ");
        Query query = this.getSession().createSQLQuery(sql.toString());
        query.setParameter("dmcod", dmcod);
        // query.setParameter("unitType", UnitType.VUNIT);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return (Integer) ((Map<String, Object>) query.list().get(0)).get("maxPunitLev");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<B01> getB01ListByRosterUnitId(String unitId) {

        String hql = "SELECT b01 FROM B01RosterUnitReal b01RosterUnitReal LEFT JOIN b01RosterUnitReal.b01 b01 ";
        hql += "WHERE b01RosterUnitReal.rosterUnit.id = :unitId";
        Query query = this.getSession().createQuery(hql);
        query.setParameter("unitId", unitId);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getSubB01HiberIdsByUnitId(String b01HiberId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("b01HiberId", b01HiberId);
        String sql = this.findQueryByNamed("unitInfo.getSubB01HiberIdsById", paramMap);
        Query query = this.getSession().createSQLQuery(sql);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getSubUnitIdsByUnitId(String b01HiberId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("b01HiberId", b01HiberId);
        String sql = this.findQueryByNamed("unitInfo.getSubUnitIdsById", paramMap);
        Query query = this.getSession().createSQLQuery(sql);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<B01> findB01ByB0111(String b0111) {
        String hql = "SELECT b01 FROM B01 AS b01 WHERE b01.b0111 = :b0111 ";
        Query query = this.getSession().createQuery(hql);
        query.setParameter("b0111", b0111);
        return query.list();
    }

}
