package rongji.cmis.dao.cadreUnit.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import rongji.cmis.dao.cadreUnit.B01HiberDao;
import rongji.cmis.model.unit.B01Hiber;
import rongji.cmis.model.unit.B01.UnitType;
import rongji.framework.base.dao.impl.GenericDaoImpl;

/**
 * DEMO > 干部管理 > 单位管理
 * 
 * @author Administrator
 * 
 */
@Repository("b01HiberDaoImpl")
public class B01HiberDaoImpl extends GenericDaoImpl<B01Hiber> implements B01HiberDao {

	@Override
	public boolean isExistUnitInfo(String dmcod) {
		String hql = "select count(*) from  B01Hiber b where b.unitGroup.dmcod= :dmcod";
		Query query = this.getSession().createQuery(hql);
		query.setString("dmcod", dmcod);
		long count = ((Number) query.uniqueResult()).longValue();
		if (count > 0) {
			return true;
		}
		return false;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<B01Hiber> findB01HiberListByDmcod(String dmcod) {
		StringBuffer hql = new StringBuffer(" ");
		hql.append("SELECT b01Hiber ");
		hql.append(" FROM B01Hiber b01Hiber, UnitIndex unitIndex WHERE b01Hiber.unit.b00 = unitIndex.code ");
		hql.append(" AND b01Hiber.unitGroup.dmcod = :dmcod ");
		hql.append(" ORDER BY unitIndex.inino ASC ");
		Query query = this.getSession().createQuery(hql.toString()).setParameter("dmcod", dmcod);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<B01Hiber> findVulB01HiberListByDmcod(String dmcod) {
		StringBuffer hql = new StringBuffer(" ");
		hql.append(" SELECT b01Hiber ");
		hql.append(" FROM B01Hiber b01Hiber ");
		hql.append(" WHERE b01Hiber.unitGroup.dmcod = :dmcod AND b01Hiber.unit.unitType = 2 ");

		Query query = this.getSession().createQuery(hql.toString()).setParameter("dmcod", dmcod);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<B01Hiber> findB01HiberListByDmcodAndUnitType(String dmcod, UnitType unitType) {
		StringBuffer hql = new StringBuffer(" ");
		hql.append(" SELECT b01Hiber ");
		if (UnitType.UNIT.equals(unitType)) {
			hql.append(" FROM B01Hiber b01Hiber, UnitIndex unitIndex ");
			hql.append("WHERE b01Hiber.unit.b00 = unitIndex.code ");
			hql.append(" AND b01Hiber.unitGroup.dmcod = :dmcod AND b01Hiber.unit.unitType = :unitType");
			hql.append(" ORDER BY unitIndex.inino ASC ");
		}

		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("dmcod", dmcod);
		query.setParameter("unitType", unitType);

		return query.list();
	}

	@Override
	public void unitSortSave(Integer inino, String b01HiberId) {
		String hql = "update B01Hiber b01Hiber set b01Hiber.inino =:inino where b01Hiber.id =:b01HiberId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("inino", inino);
		query.setParameter("b01HiberId", b01HiberId);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<B01Hiber> findAllB01HiberList() {
		StringBuffer hql = new StringBuffer(" ");
		hql.append(" SELECT b01Hiber ");
		hql.append(" FROM B01Hiber b01Hiber ");
		hql.append(" ORDER BY b01Hiber.inino ASC");

		Query query = this.getSession().createQuery(hql.toString());
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public B01Hiber getB01HiberByB00(String b00) {
		StringBuffer hql = new StringBuffer(" ");
		hql.append(" SELECT b01Hiber ");
		hql.append(" FROM B01Hiber b01Hiber ");
		hql.append(" WHERE b01Hiber.unit.b00 = :b00");

		Query query = this.getSession().createQuery(hql.toString()).setParameter("b00", b00);
		List<B01Hiber> list = query.list();
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);

	}


	@SuppressWarnings("unchecked")
	@Override
	public List<B01Hiber> findAllB01HiberListByLibId(String libId) {
		StringBuffer hql = new StringBuffer(" ");
		hql.append("SELECT b01Hiber ");
		hql.append(" FROM B01Hiber b01Hiber, UnitIndex unitIndex WHERE b01Hiber.unit.b00 = unitIndex.code ");
		hql.append(" AND b01Hiber.unitLib.id = :libId ");
		hql.append(" ORDER BY unitIndex.inino ASC ");
		Query query = this.getSession().createQuery(hql.toString()).setParameter("libId", libId);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<B01Hiber> getB01HiberListByB00(String b00, String impLibId) {
		StringBuffer hql = new StringBuffer(" ");
		hql.append(" SELECT b01Hiber ");
		hql.append(" FROM B01Hiber b01Hiber ");
		if (StringUtils.isNotEmpty(impLibId)) {
			hql.append(" WHERE b01Hiber.unit.b00 = :b00 AND b01Hiber.unitLib.id = :impLibId");
		} else {
			hql.append(" WHERE b01Hiber.unit.b00 = :b00");
		}
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("b00", b00);
		if (StringUtils.isNotEmpty(impLibId)) {
			query.setParameter("impLibId", impLibId);
		}
		return query.list();
	}

	@Override
	public Integer getNewInino(String id) {
		String hql = "select max(b01Hiber.inino) from B01Hiber b01Hiber where b01Hiber.parentB01Hiber.id=:pId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("pId", id);
		Integer inino = (Integer) query.uniqueResult();
		if (inino == null) {
			inino = 1;
		} else {
			inino += 1;
		}
		return inino;
	}

	@Override
	public Integer getNewInino(Integer upLev) {
		String hql = "select max(b01Hiber.inino) from B01Hiber b01Hiber where b01Hiber.parentB01Hiber is null";
		Query query = this.getSession().createQuery(hql);
		Integer inino = (Integer) query.uniqueResult();
		if (inino == null) {
			inino = 1;
		} else {
			inino += 1;
		}
		return inino;
	}

	@Override
	public void delete_unit_index(String code) {
		String hql = "DELETE from UnitIndex where CODE=:code";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("code",code);
		query.executeUpdate();
	}
}
