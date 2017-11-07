package rongji.cmis.dao.system.impl;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import rongji.cmis.dao.system.MenuOperDao;
import rongji.cmis.model.ums.CfgUmsMenuOper;
import rongji.framework.base.dao.impl.GenericDaoImpl;
import rongji.framework.util.SpringUtils;
import rongji.framework.util.StringUtil;

import java.util.*;

@Repository
public class MenuOperDaoImpl extends GenericDaoImpl<CfgUmsMenuOper> implements MenuOperDao {

    @Override
    public void deleteByMenuId(Integer menuId) {
        String hql = "delete from CfgUmsMenuOper where 1=1 and menuid=:menuId";
        Query query = this.getSession().createQuery(hql);
        query.setParameter("menuId", menuId);
        query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> queryPermissionsByIds(Set<Integer> operIds, String operAuthCode) {
        String sql = "select permission from CFG_UMS_MENU_OPER where id in (:operIds) and permission like :operAuthCode";
        Query query = this.getSession().createSQLQuery(sql).setParameterList("operIds", operIds).setString("operAuthCode", "%" + operAuthCode + "%");
        return query.list();
    }
    @Override
    public List<String> getUserEntMenuOperRight(String userId, String entId) {
        Map<String, Object> params =  new LinkedHashMap<String,Object>();
        String sql = this.findQueryByNamed("menuOper.getUserEntMenuOperRight", params);
        Query query = this.getSession().createSQLQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("entId", entId);
        return query.list();
    }

    @Override
    public List<String> getRoleEntMenuOperRight(String roleId, String entId) {
        Map<String, Object> params =  new LinkedHashMap<String,Object>();
        String sql = this.findQueryByNamed("menuOper.getRoleEntMenuOperRight", params);
        Query query = this.getSession().createSQLQuery(sql);
        query.setParameter("roleId", roleId);
        query.setParameter("entId", entId);
        return query.list();
    }
    
}
