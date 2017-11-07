package rongji.cmis.dao.system.impl;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import rongji.cmis.dao.system.RoleInfosetPriDao;
import rongji.cmis.model.ums.RoleInfosetPri;
import rongji.framework.base.dao.impl.GenericDaoImpl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RoleInfosetPriDaoImpl extends GenericDaoImpl<RoleInfosetPri> implements RoleInfosetPriDao {

	@Override
	public void delRoleCadreByRoleIdAndInfoType(Integer roleId, Integer infoType) {
		String sql = "delete from ROLE_INFOSET_PRI where ROLE_ID = :roleId and INFO_TYPE = :infoType";
		Query query = this.getSession().createSQLQuery(sql);
		query.setParameter("roleId", roleId);
		query.setParameter("infoType", infoType);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleInfosetPri> getRoleInfosetListByRoleAndPrivCode(Integer roleId, String privCode) {
		String hql = "from RoleInfosetPri rip where rip.cfgUmsRole.id = :roleId and privCode LIKE :privCode ORDER BY priv ASC";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roleId", roleId);
		query.setParameter("privCode", "%" + privCode + "%");
		return query.list();
	}

	@Override
	public void delRoleInfosetPriByRoleId(Integer roleId) {
		List<RoleInfosetPri> roleInfosetPri = this.findByProperty("ROLE_ID", roleId);
		for (RoleInfosetPri rip : roleInfosetPri) {
			this.delete(rip.getRoleInfosetPriId());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleInfosetPri> getRoleInfosetPriListByRoleArr(Integer[] roleIdArr) {
		String hql = "select rip from RoleInfosetPri as rip where rip.cfgUmsRole.id in(:roleId)";
		Query query = this.getSession().createQuery(hql);
		query.setParameterList("roleId", roleIdArr);
		if (roleIdArr.length < 1) {
			return null;
		}
		return query.list();
	}

	@Override
	public Integer getInfosetPriByUserId(int userId, String infoSet) {
		Map<String, Object> parames = new HashMap<String, Object>();
		parames.put("userId", userId);
		parames.put("privKey", infoSet);
		String sql = findQueryByNamed("infoSetPri.getInfosetPriByUserId", parames);
		Query query = this.createSQLQuery(sql, parames);
		return (Integer)query.uniqueResult();
	}

	@Override
	public Integer getInfosetPriByRoleId(int roleId, String infoSet) {
		Map<String, Object> parames = new HashMap<String, Object>();
		parames.put("roleId", roleId);
		parames.put("privKey", infoSet);
		String sql = findQueryByNamed("infoSetPri.getInfosetPriByRoleId", parames);
		Query query = this.createSQLQuery(sql, parames);
		return (Integer)query.uniqueResult();
	}

	@Override
	public Integer getInfosetPriByUserIdAndEntId(int userId, String entId, String infoSet) {
		Map<String, Object> parames = new HashMap<String, Object>();
		parames.put("userId", userId);
		parames.put("entId", entId);
		parames.put("privKey", infoSet);
		String sql = findQueryByNamed("infoSetPri.getInfosetPriByUserIdAndEntId", parames);
		Query query = this.createSQLQuery(sql, parames);
		return (Integer)query.uniqueResult();
	}


	@Override
	public Integer getInfosetPriByRoleIdAndEntId(int roleId, String entId, String infoSet) {
		Map<String, Object> parames = new HashMap<String, Object>();
		parames.put("roleId", roleId);
		parames.put("entId", entId);
		parames.put("privKey", infoSet);
		String sql = findQueryByNamed("infoSetPri.getInfosetPriByRoleIdAndEntId", parames);
		Query query = this.createSQLQuery(sql, parames);
		return (Integer)query.uniqueResult();
	}


	@Override
	public List<Map<String,Object>> getInfosetPriListByUserIdAndEntId(int userId,String entId) {
		Map<String, Object> params =  new LinkedHashMap<String,Object>();
		params.put("userId", userId);
		params.put("entId", entId);
		String sql = this.findQueryByNamed("infoSetPri.getInfosetPriListByUserIdAndEntId", params);
		Query query = this.getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setParameter("userId", userId);
		query.setParameter("entId", entId);

		return (List<Map<String, Object>>) query.list();
	}


	@Override
	public List<Map<String,Object>> getInfosetPriListByRoleIdAndEntId(int roleId, String entId) {
		Map<String, Object> params =  new LinkedHashMap<String,Object>();
		params.put("roleId", roleId);
		params.put("entId", entId);
		String sql = this.findQueryByNamed("infoSetPri.getInfosetPriListByRoleIdAndEntId", params);
		Query query = this.getSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setParameter("roleId", roleId);
		query.setParameter("entId", entId);
		return (List<Map<String, Object>>) query.list();
	}
}
