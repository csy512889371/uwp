package rongji.cmis.service.system.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import rongji.cmis.dao.system.ScriptVersionDao;
import rongji.cmis.model.sys.ScriptVersion;
import rongji.cmis.service.system.ScriptVersionService;
import rongji.framework.base.service.impl.BaseServiceImpl;
import rongji.framework.util.StringUtil;

@Service("scriptVersionServiceImpl")
public class ScriptVersionServiceImpl extends BaseServiceImpl<ScriptVersion> implements ScriptVersionService {

	@Resource(name = "scriptVersionDaoImpl")
	private ScriptVersionDao scriptVersionDao;

	@Override
	public String getChnScript(Integer seqno, Integer scriptType) {
		ScriptVersion scriptVersion = scriptVersionDao.find(seqno);
		if (scriptVersion != null) {
			if (1 == scriptType && StringUtil.isNotEmpty(scriptVersion.getChnScriptAccess())) {
				return scriptVersion.getChnScriptAccess();
			} else {
				return scriptVersion.getChnScript();
			}
		} else {
			return "";
		}
	}

	@Override
	public List<ScriptVersion> getScriptVersionList(Integer pubType) {
		return scriptVersionDao.getScriptVersionList(pubType);
	}

	@Override
	public List<ScriptVersion> getUpdateScriptVersionList(Integer pubType, Integer publicId) {
		return scriptVersionDao.getUpdateScriptVersionList(pubType, publicId);
	}

	@Override
	public List<ScriptVersion> getScriptVerionBySelectedPublicIds(Integer[] publicIds) {
		List<ScriptVersion> scriptVersionList = new ArrayList<ScriptVersion>();
		for (Integer publicId : publicIds) {
			List<ScriptVersion> list = scriptVersionDao.getScriptVerionBySelectedPublicId(publicId);
			if (!list.isEmpty() && list.size() > 0) {
				scriptVersionList.addAll(list);
			}
		}
		return scriptVersionList;
	}
}
