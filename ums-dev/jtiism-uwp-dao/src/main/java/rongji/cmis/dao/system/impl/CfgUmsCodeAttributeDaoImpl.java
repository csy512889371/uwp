package rongji.cmis.dao.system.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import rongji.cmis.dao.system.CfgUmsCodeAttributeDao;
import rongji.cmis.model.ums.CfgUmsCodeAttribute;
import rongji.framework.base.dao.impl.GenericDaoImpl;
import rongji.framework.util.StringUtil;

@Repository("cfgUmsCodeAttributeDaoImpl")
public class CfgUmsCodeAttributeDaoImpl extends GenericDaoImpl<CfgUmsCodeAttribute> implements CfgUmsCodeAttributeDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<CfgUmsCodeAttribute> findAllRequest() {
		String hql="from CfgUmsCodeAttribute";
		List<CfgUmsCodeAttribute> codeList=this.getSession().createQuery(hql).setFlushMode(FlushMode.COMMIT).list();
		return codeList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer findLastSeq(String codeId,String parentId) {
		String hql="select max(seq) from CfgUmsCodeAttribute as codeAttr where codeAttr.cfgUmsCode.id=:codeId ";
		if(StringUtil.isNotEmpty(parentId)){
			hql+="and codeAttr.parentId=:parentId ";
		}
		Query query = this.getSession().createQuery(hql);
		query.setParameter("codeId", codeId);
		if(StringUtil.isNotEmpty(parentId)){
			query.setParameter("parentId", parentId);
		}
		List<Integer> list=(List<Integer>) query.list();
		if(list.size()>0){
			return list.get(0);
		}
		return 0;
	}
	
	/**
	 * 后台管理系统 > 信息构建 > 指标代码维护:左边代码编辑修改
	 */
	@Override
	public void updateCodeAttr(CfgUmsCodeAttribute cfgUmsCodeAttribute) {		
		String sql = " update CFG_UMS_CODE_ATTRIBUTE set ATTRCODE = :attrCode,ATTRNAME=:attrName where ID = :id";
		SQLQuery query = this.getSession().createSQLQuery(sql);		
		query.setParameter("attrCode",cfgUmsCodeAttribute.getAttrCode());
		query.setParameter("attrName", cfgUmsCodeAttribute.getAttrName());
		query.setParameter("id", cfgUmsCodeAttribute.getId());
		query.executeUpdate();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> findAttributeINSession(String operateSet) {
		String sql = "select * from CFG_UMS_CODE_ATTRIBUTE where code_id='3' and attrcode=:attrcode";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setParameter("attrcode", operateSet.toUpperCase());
		return (List<Map<String, Object>>) query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addCodeAttribute(CfgUmsCodeAttribute cfgUmsCodeAttribute) {
		String sql ="insert into CFG_UMS_CODE_ATTRIBUTE (ID,ATTRCODE,ATTRNAME,PARENTID,STATUS,seq,CODE_ID) values (:id,:attrcode,:attrname,:parentid,:status,:seq,1)";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setParameter("id",cfgUmsCodeAttribute.getId());
		query.setParameter("attrcode",cfgUmsCodeAttribute.getAttrCode());
		query.setParameter("attrname",cfgUmsCodeAttribute.getAttrName());
		query.setParameter("parentid",cfgUmsCodeAttribute.getParentId());
		query.setParameter("status",cfgUmsCodeAttribute.getStatus());
		query.setParameter("seq",cfgUmsCodeAttribute.getSeq());
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Boolean isTableExists(String tableName) {
		String sql = "select count(*) from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='jtiismv1' and TABLE_NAME=:tableName";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setParameter("tableName",tableName);
		int count=Integer.parseInt(query.list().get(0).toString());
		return count>0?false:true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CfgUmsCodeAttribute> getTableChineseName(String tableName){
		String sql = "select * from CFG_UMS_CODE_ATTRIBUTE where ATTRCODE=:tableName";
		SQLQuery query = this.getSession().createSQLQuery(sql).addEntity(CfgUmsCodeAttribute.class);
		query.setParameter("tableName",tableName);
		return (List<CfgUmsCodeAttribute>)query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CfgUmsCodeAttribute> findScriptNoNull() {
		String sql = "select * FROM CFG_UMS_CODE_ATTRIBUTE WHERE CODE_ID='3' and script is not NULL and script!=''";
		SQLQuery query = this.getSession().createSQLQuery(sql).addEntity(CfgUmsCodeAttribute.class);
		return (List<CfgUmsCodeAttribute>) query.list();
	}
}
