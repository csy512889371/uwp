package rongji.cmis.dao.system.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import rongji.cmis.dao.cadreUnit.InfoCadreBasicAttributeDao;
import rongji.cmis.dao.system.DynamicInfoSetDao;
import rongji.cmis.model.ums.CfgUmsInfoSet;
import rongji.framework.base.dao.impl.GenericDaoImpl;
import rongji.framework.util.Constant;
import rongji.framework.util.Page;
import rongji.framework.util.ParamRequest;

@Repository("dynamicInfoSetDaoImpl")
public class DynamicInfoSetDaoImpl extends GenericDaoImpl<CfgUmsInfoSet> implements DynamicInfoSetDao {

	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

	@Resource(name = "infoCadreBasicAttributeDaoImpl")
	InfoCadreBasicAttributeDao infoCadreBasicAttributeDao;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public void createInfoSetFKA01(String tableName) {
		String pk = "PK_" + tableName;
		String fk = "FK_" + tableName + "_UNITINFO";
		Map<String, Object> parameters = new LinkedHashMap<String, Object>();
		parameters.put("infoSet", tableName);
		parameters.put("keyName", Constant.getKeyName(tableName));
		parameters.put("primaryKeyN", pk);
		parameters.put("foreignKeyN", fk);
		String createSqls = infoCadreBasicAttributeDao.findQueryByNamed("infoSet.createTableFKUnitInfo", parameters);
		SQLQuery qurey = this.getSession().createSQLQuery(createSqls);
		qurey.setFlushMode(FlushMode.COMMIT).executeUpdate();

	}

	@Override
	public void addIndexCode(String tableName) {
		Map<String, Object> parameters = new LinkedHashMap<String, Object>();
		parameters.put("tableName", tableName);
		String createSqls = infoCadreBasicAttributeDao.findQueryByNamed("uwp-indexManager.CreateIndexCode",parameters);
		SQLQuery qurey = this.getSession().createSQLQuery(createSqls);
		qurey.setFlushMode(FlushMode.COMMIT).executeUpdate();

	}

	@Override
	public void addColumn(String sql) {
		SQLQuery qurey = this.getSession().createSQLQuery(sql);
		qurey.setFlushMode(FlushMode.COMMIT).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> findToMap(String infoSet, String selectV, String key) {
		String sql = "select " + selectV + " from " + infoSet + " where " + Constant.getKeyName(infoSet) + "=:key ";
		SQLQuery query = (SQLQuery) this.getSession().createSQLQuery(sql).setParameter("key", key).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return (Map<String, Object>) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> findAllToListMap(String infoSet) {

		StringBuffer findSql = new StringBuffer();
		findSql.append("SELECT * FROM ").append(infoSet);
		SQLQuery query = (SQLQuery) this.getSession().createSQLQuery(findSql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return (List<Map<String, Object>>) query.list();
	}

	@Override
	public Page<Object> findAllForPage(String infoSet, ParamRequest paramRequest) {
		// TODO
		return null;
	}

	@Override
	public void save(String sql) {
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.executeUpdate();
	}

	@Override
	public Integer update(String sql) {
		SQLQuery query = this.getSession().createSQLQuery(sql);
		return query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> findAllToListMap(String infoSet, String fkVal, String type, String selectV) {
		//String conditionSqlKey = ;
		Map<String, Object> params =  new LinkedHashMap<String,Object>();
		params.put("infoSet",infoSet);
		params.put("FKKEY",fkVal);
		params.put("type",type);
		String conditionSql = this.findQueryByNamed("infoSet.gridQuery", params);
		String sql = "SELECT " + selectV + " from " + infoSet + " where " + Constant.getFkkey() + " =:FKKEY ";
		sql+=conditionSql;
		Query query = this.getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setParameter("FKKEY", fkVal);
		return (List<Map<String, Object>>) query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getRecentData(String infoSet, String columnOne, String columnTwo,String entId) {
		String sql = "select * from "+infoSet+"  where " + Constant.getFkkey() + " =:FKKEY order by "+columnOne+" desc,"+columnTwo+" desc LIMIT 1";
		Query query = this.getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setParameter("FKKEY", entId);
		return (List<Map<String, Object>>) query.list();
	}

}
