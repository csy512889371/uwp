package rongji.cmis.dao.common.impl;

import javax.persistence.NoResultException;

import org.hibernate.FlushMode;
import org.springframework.stereotype.Repository;

import rongji.cmis.dao.common.PluginConfigDao;
import rongji.cmis.model.ums.CfgUmsPluginConfig;
import rongji.framework.base.dao.impl.GenericDaoImpl;

/**
 * Dao - 插件配置
 * 
 * @version 1.0
 */
@Repository("pluginConfigDaoImpl")
public class PluginConfigDaoImpl extends GenericDaoImpl<CfgUmsPluginConfig> implements PluginConfigDao {

	public boolean pluginIdExists(String pluginId) {
		if (pluginId == null) {
			return false;
		}
		String jpql = "select count(*) from CfgUmsPluginConfig pluginConfig where pluginConfig.pluginId = :pluginId";

		Long count = (Long)this.getSession().createQuery(jpql).setFlushMode(FlushMode.COMMIT).setParameter("pluginId",pluginId).uniqueResult();
		
		return count > 0;
	}

	public CfgUmsPluginConfig findByPluginId(String pluginId) {
		
		if (pluginId == null) {
			return null;
		}
		try {
			String jpql = "select pluginConfig from CfgUmsPluginConfig pluginConfig where pluginConfig.pluginId = :pluginId";
			return (CfgUmsPluginConfig)this.getSession().createQuery(jpql).setFlushMode(FlushMode.COMMIT).setParameter("pluginId", pluginId).uniqueResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}