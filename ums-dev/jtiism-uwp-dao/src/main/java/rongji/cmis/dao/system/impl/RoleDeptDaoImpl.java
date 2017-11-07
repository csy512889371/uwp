package rongji.cmis.dao.system.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import rongji.cmis.dao.system.RoleDeptDao;
import rongji.cmis.model.ums.RoleDeptReal;
import rongji.framework.base.dao.impl.GenericDaoImpl;

@Repository
public class RoleDeptDaoImpl extends GenericDaoImpl<RoleDeptReal> implements RoleDeptDao {

	@Override
	public void deleteRoleDeptByRoleId(String roleId) {
		List<RoleDeptReal> roleDeptList = this.findByProperty("ROLE_ID", roleId);
		for (RoleDeptReal roleDeptReal : roleDeptList) {
			this.delete(roleDeptReal.getRoleDeptRealId());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String[] findDeptByRoleIds(Integer[] roleIdArr) {
		String hql = "select distinct rdr.cmisDepartment.id from RoleDeptReal as rdr where rdr.cfgUmsRole.id in(:roleId)";
		Query query = this.getSession().createQuery(hql);
		query.setParameterList("roleId", roleIdArr);
		String[] deptArr = new String[] { "" };
		if (roleIdArr.length < 1) {
			return deptArr;
		}
		List<String> list = query.list();
		if (list != null) {
			deptArr = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				deptArr[i] = list.get(i);
			}
			return deptArr;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String[] findDeptByRoleId(Integer roleId) {
		String hql = "select distinct rdr.cmisDepartment.id from RoleDeptReal as rdr where rdr.cfgUmsRole.id = :roleId";
		Query query = this.getSession().createQuery(hql);
		query.setParameter("roleId", roleId);
		String[] deptArr = new String[] { "" };
		List<String> list = query.list();
		if (list != null) {
			deptArr = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				deptArr[i] = list.get(i);
			}
			return deptArr;
		}
		return null;
	}

}
