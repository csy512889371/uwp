package rongji.cmis.dao.system;

import java.util.List;

import rongji.cmis.model.sys.ScriptVersion;
import rongji.framework.base.dao.GenericDao;

public interface ScriptVersionDao extends GenericDao<ScriptVersion> {

	List<ScriptVersion> getScriptVersionList(Integer pubType);

	List<ScriptVersion> getUpdateScriptVersionList(Integer pubType, Integer publicId);

	List<ScriptVersion> getScriptVerionBySelectedPublicId(Integer publicId);

	List<ScriptVersion> getScriptVerionBySelectPubIdAndChnType(Integer publicId, Integer chnType);

}
