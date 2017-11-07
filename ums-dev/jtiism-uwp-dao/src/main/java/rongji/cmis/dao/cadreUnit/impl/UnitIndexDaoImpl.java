package rongji.cmis.dao.cadreUnit.impl;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import rongji.cmis.dao.cadreUnit.UnitIndexDao;
import rongji.cmis.model.ums.CfgUmsDataDict;
import rongji.cmis.model.unit.UnitIndex;
import rongji.framework.base.dao.impl.GenericDaoImpl;
import rongji.framework.base.pojo.ZtreeDictNote;
import rongji.framework.util.StringUtil;

import java.util.List;
import java.util.UUID;

@Repository("unitIndexDaoImpl")
public class UnitIndexDaoImpl extends GenericDaoImpl<UnitIndex> implements UnitIndexDao {

	@Override
	public void save(UnitIndex entity) {
		fillDefaultValue(entity);
		super.save(entity);
	}

	@Override
	public void saveOrUpdate(UnitIndex entity) {

		fillDefaultValue(entity);

		super.saveOrUpdate(entity);
	}

	@Override
	public Object merge(UnitIndex entity) {

		fillDefaultValue(entity);

		return super.merge(entity);
	}

	@Override
	public Integer getMaxInino() {
		SQLQuery sqlQuery = this.getSession().createSQLQuery("SELECT max(ININO) + 1 from UNIT_INDEX");
		Object inino = sqlQuery.uniqueResult();
		if (inino != null) {
			return Integer.parseInt(inino.toString());
		}
		return null;
	}

	private void fillDefaultValue(UnitIndex entity) {
		if (StringUtil.isEmpty(entity.getCode())) {
			entity.setCode(UUID.randomUUID().toString());
		}

		if (StringUtil.isEmpty(entity.getDmGrp())) {
			entity.setDmGrp("UNIT_INDEX");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ZtreeDictNote> getUnitIndexTreeList() {
		//fix: 简称存在取简称，简称不存在取全称
		StringBuffer sql = new StringBuffer(" ");
		sql.append("select unitIndex.CODE as code, unitIndex.CODE_NAME as codeName, unitIndex.CODE_SPELLING codeSpelling, ");
		sql.append(" unitIndex.CODE_ABR1 codeAbr1,  unitIndex.CODE_ABR2 codeAbr2, b01hiber.ININO inino, ");
		sql.append(" unitIndex.CODE_ANAME codeAName,  unitIndex.CODE_LEVEL codeLevel, unitIndex.DmGrp dmGrp, ");
		sql.append(" b01hiber.PUNIT_ID as supCode,unit.UNITTYPE as unitType");
		sql.append(" from UNIT_HIBER_RELA b01hiber ");
		sql.append("inner join UNIT_INFO unit on b01hiber.b00 = unit.b00 ");
		sql.append("inner join UNIT_INDEX unitIndex on b01hiber.b00 = unitIndex.CODE ORDER BY unitIndex.ININO asc");
		Query query = this.getSession().createSQLQuery(sql.toString()).setResultTransformer(Transformers.aliasToBean(ZtreeDictNote.class));
		return query.list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CfgUmsDataDict> getListExcludeB01() {
		//fix: 简称存在取简称，简称不存在取全称
		
		StringBuffer sql = new StringBuffer(" ");
		sql.append("select unitIndex.CODE as code, unitIndex.CODE_NAME as codeName, unitIndex.CODE_SPELLING codeSpelling, ");
		sql.append(" isnull(unitIndex.CODE_ABR1,unitIndex.CODE_NAME) codeAbr1,  isnull(unitIndex.CODE_ABR2,unitIndex.CODE_NAME) codeAbr2, unitIndex.ININO inino, ");
		sql.append(" unitIndex.CODE_ANAME codeAName, unitIndex.DmGrp dmGrp, unitIndex.UNIT_CODE as unitCode ");
		sql.append(" from ZB02 unitIndex where not exists(select unit.b00 from UNIT_INFO unit where unit.b00=unitIndex.CODE) ORDER BY unitIndex.ININO asc");
		Query query = this.getSession().createSQLQuery(sql.toString()).setResultTransformer(Transformers.aliasToBean(CfgUmsDataDict.class));
		return query.list();
	}

	@Override
	public void unitSortSave(Integer inino, String code) {
		String hql = "update UnitIndex unitIndex set unitIndex.inino =:inino where unitIndex.code =:code ";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("inino", inino);
		query.setParameter("code", code);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CfgUmsDataDict> getUnitIndexByUnitCode(String unitCode) {
		StringBuffer sql = new StringBuffer(" ");
		sql.append("select unitIndex.CODE as code, unitIndex.CODE_NAME as codeName, unitIndex.CODE_SPELLING codeSpelling, ");
		sql.append(" isnull(unitIndex.CODE_ABR1,unitIndex.CODE_NAME) codeAbr1,  isnull(unitIndex.CODE_ABR2,unitIndex.CODE_NAME) codeAbr2, unitIndex.ININO inino, ");
		sql.append(" unitIndex.CODE_ANAME codeAName, unitIndex.DmGrp dmGrp, unitIndex.UNIT_CODE as unitCode ");
		sql.append(" from ZB02 unitIndex where unitIndex.UNIT_CODE = :unitCode");
		Query query = this.getSession().createSQLQuery(sql.toString()).setResultTransformer(Transformers.aliasToBean(CfgUmsDataDict.class));
		query.setParameter("unitCode", unitCode);
		return query.list();
	}
	
	@Override
	public Integer getMaxPunitLev() {
		SQLQuery sqlQuery = this.getSession().createSQLQuery("SELECT max(punit_lev) from UNIT_HIBER_RELA");
		return (Integer) sqlQuery.uniqueResult();
	}


}
