package rongji.cmis.dao.common.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import rongji.cmis.dao.common.FieldInfoDao;
import rongji.cmis.model.sys.SysFieldInfo;
import rongji.framework.base.dao.impl.GenericDaoImpl;

/**
 * Dao - 表字段 对应的数据字典
 * 
 * @version 1.0
 */
@Repository("fieldInfoDaoImpl")
public class FieldInfoDaoImpl extends GenericDaoImpl<SysFieldInfo> implements FieldInfoDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<String> findCodeTableDist() {
		String sql = "select distinct CODETABLE FROM SYS_FIELDINFO order by CODETABLE ASC";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		return query.list();
	}

}