package rongji.cmis.service.system.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import rongji.cmis.dao.system.GroupDao;
import rongji.cmis.dao.system.UserDao;
import rongji.cmis.model.ums.CfgUmsGroup;
import rongji.cmis.service.system.GroupService;
import rongji.framework.base.service.impl.BaseServiceImpl;

@Service("groupServiceImpl")
public class GroupServiceImpl extends BaseServiceImpl<CfgUmsGroup> implements GroupService {

	@Resource(name = "userDaoImpl")
	UserDao userDao;

	@Resource(name = "groupDaoImpl")
	GroupDao groupDao;

	@Override
	public void saveGroupSort(String groupIds) {
		String[] groupIdArr = groupIds.split(",");
		int i = 1;
		for (String groupId : groupIdArr) {
			CfgUmsGroup group = groupDao.load(groupId);
			if (group != null) {
				group.setSeqno(i);
				groupDao.merge(group);
				i++;
			}
		}
	}

	@Override
	public Integer getLastSeqno() {
		return groupDao.getLastSeqno();
	}

	@Override
	public void saveAndSeqno(CfgUmsGroup group) {
		if (group.getParentGroup() == null) {
			Integer seqno = groupDao.getLastSeqno();
			group.setSeqno(seqno);
		} else {
			CfgUmsGroup parGroup = groupDao.load(group.getParentGroup().getId());
			group.setSeqno(parGroup.getSeqno() + 1);
			group.setParentGroup(parGroup);
		}
		groupDao.save(group);
	}

}
