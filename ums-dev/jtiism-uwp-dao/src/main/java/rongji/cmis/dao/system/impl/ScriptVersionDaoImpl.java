package rongji.cmis.dao.system.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import rongji.cmis.dao.system.ScriptVersionDao;
import rongji.cmis.model.sys.ScriptVersion;
import rongji.framework.base.dao.impl.GenericDaoImpl;

import java.util.List;

@Repository("scriptVersionDaoImpl")
public class ScriptVersionDaoImpl extends GenericDaoImpl<ScriptVersion> implements ScriptVersionDao {

    @SuppressWarnings("unchecked")
    @Override
    public List<ScriptVersion> getScriptVersionList(Integer pubType) {

        StringBuffer hql = new StringBuffer();
        hql.append("SELECT scriptVersion ");
        hql.append("FROM ScriptVersion scriptVersion ");
        hql.append("WHERE scriptVersion.seqno NOT IN ( ");
        hql.append("SELECT versionPublicDet.scriptVersion.seqno AS seqno FROM VersionPublicDet versionPublicDet ");
        hql.append("LEFT JOIN versionPublicDet.versionPublic versionPublic ");
        hql.append("WHERE versionPublic.pubType = " + pubType + " )");

        Query query = this.getSession().createQuery(hql.toString());
        List<ScriptVersion> list = query.list();

        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ScriptVersion> getUpdateScriptVersionList(Integer pubType, Integer publicId) {
        StringBuffer hql = new StringBuffer();
        hql.append("SELECT scriptVersion ");
        hql.append("FROM ScriptVersion scriptVersion ");
        hql.append("WHERE scriptVersion.seqno NOT IN ( ");
        hql.append("SELECT versionPublicDet.scriptVersion.seqno AS seqno FROM VersionPublicDet versionPublicDet ");
        hql.append("LEFT JOIN versionPublicDet.versionPublic versionPublic ");
        hql.append("WHERE versionPublic.pubType = " + pubType + " AND versionPublic.publicId <> " + publicId + ")");
        Query query = this.getSession().createQuery(hql.toString());
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ScriptVersion> getScriptVerionBySelectedPublicId(Integer publicId) {
        StringBuffer hql = new StringBuffer();
        hql.append("SELECT scriptVersion ");
        hql.append("FROM VersionPublicDet versionPublicDet ");
        hql.append("LEFT JOIN versionPublicDet.scriptVersion scriptVersion ");
        hql.append("WHERE versionPublicDet.versionPublic.publicId = :publicId ORDER BY scriptVersion.chnType ASC");

        Query query = this.getSession().createQuery(hql.toString());
        query.setParameter("publicId", publicId);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ScriptVersion> getScriptVerionBySelectPubIdAndChnType(Integer publicId, Integer chnType) {
        StringBuffer hql = new StringBuffer();
        hql.append("SELECT scriptVersion ");
        hql.append("FROM VersionPublicDet versionPublicDet ");
        hql.append("LEFT JOIN versionPublicDet.scriptVersion scriptVersion ");
        hql.append("WHERE versionPublicDet.versionPublic.publicId = :publicId AND scriptVersion.chnType = :chnType");

        Query query = this.getSession().createQuery(hql.toString());
        query.setParameter("publicId", publicId);
        query.setParameter("chnType", chnType);
        return query.list();
    }

}
