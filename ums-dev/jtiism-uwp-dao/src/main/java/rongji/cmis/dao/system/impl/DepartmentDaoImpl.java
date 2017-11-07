package rongji.cmis.dao.system.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import rongji.cmis.dao.system.DepartmentDao;
import rongji.cmis.model.unit.CmisDepartment;
import rongji.framework.base.dao.impl.GenericDaoImpl;

@Repository("departmentDaoImpl")
public class DepartmentDaoImpl extends GenericDaoImpl<CmisDepartment> implements DepartmentDao {

	@Override
	public void saveDeptOrder(String deptId, String orderNum) {
		String hql = "update CmisDepartment set orderno =:orderNum where 1 = 1 and id =:deptId";
		Query query = this.getSession().createQuery(hql);
		query.setInteger("orderNum", Integer.parseInt(orderNum));
		query.setString("deptId", deptId);
		query.executeUpdate();
	}


	@Override
	public long isCodeExists(String id) {
		String hql="SELECT COUNT(CODE)as idCount from CmisDepartment where CODE=:id";
		Query query = this.getSession().createQuery(hql);
		query.setString("id", id);
		return (long) query.list().get(0);
	}

	@Override
	public void updateDepts(String code, String deptname, String description) {
		String hql="UPDATE CmisDepartment set CODE_NAME=:deptname,DESCRIPTION=:description where CODE=:code";
		Query query = this.getSession().createQuery(hql);
		query.setString("code", code);
		query.setString("deptname", deptname);
		query.setString("description", description);
		query.executeUpdate();
	}
}
