package rongji.cmis.dao.cadreUnit.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import rongji.cmis.dao.cadreUnit.InfoCadreBasicAttributeDao;
import rongji.cmis.model.sys.SysColumnShow;
import rongji.framework.base.dao.impl.GenericDaoImpl;

@Repository("infoCadreBasicAttributeDaoImpl")
public class InfoCadreBasicAttributeDaoImpl extends GenericDaoImpl<SysColumnShow> implements InfoCadreBasicAttributeDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> findColumnInfoINSession(String infoSet) {
		String sql = "select * from SYS_COLUMN_SHOW where infoSet=:infoSet";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setParameter("infoSet", infoSet.toUpperCase());
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return (List<Map<String, Object>>) query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SysColumnShow> findColumnInfo(String infoSet) {
		String hql = "select show from SysColumnShow show where infoSet=:infoSet";
		Query query= this.getSession().createQuery(hql);
		query.setString("infoSet", infoSet);
		List<SysColumnShow> list = query.list();
		if (list == null ) {
			list = new ArrayList<SysColumnShow>();
		} 
		return list;
	}

}
