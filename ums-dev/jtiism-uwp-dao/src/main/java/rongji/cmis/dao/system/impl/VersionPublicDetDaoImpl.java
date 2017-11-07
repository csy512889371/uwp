package rongji.cmis.dao.system.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import rongji.cmis.dao.system.VersionPublicDetDao;
import rongji.cmis.model.sys.VersionPublicDet;
import rongji.framework.base.dao.impl.GenericDaoImpl;

@Repository("versionPublicDetDaoImpl")
public class VersionPublicDetDaoImpl extends GenericDaoImpl<VersionPublicDet> implements VersionPublicDetDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getSeqnoByPublicId(Integer publicId) {
		String hql = "SELECT versionPublicDet FROM VersionPublicDet versionPublicDet WHERE versionPublicDet.versionPublic.publicId = " + publicId;

		Query query = this.getSession().createQuery(hql);
		List<VersionPublicDet> list = query.list();
		List<Integer> seqnoList = new ArrayList<Integer>();
		for (VersionPublicDet vpd : list) {
			seqnoList.add(vpd.getScriptVersion().getSeqno());
		}
		return seqnoList;
	}

	@Override
	public void deleteByPublicId(Integer publicId) {
		
		String hql = "DELETE FROM VersionPublicDet versionPublicDet WHERE versionPublicDet.versionPublic.publicId = " + publicId;

		Query query = this.getSession().createQuery(hql);
		query.executeUpdate();
	}

}
