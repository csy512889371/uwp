package rongji.cmis.service.system;

import java.util.List;

import rongji.cmis.model.sys.ScriptVersion;
import rongji.framework.base.service.BaseService;

public interface ScriptVersionService extends BaseService<ScriptVersion> {
	/**
	 * 
	 * @Title: getChnScript
	 * @Description: 变更脚本信息
	 * @param seqno
	 * @param scriptType
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author LFG
	 */
	String getChnScript(Integer seqno, Integer scriptType);

	/**
	 * 
	 * @Title: getScriptVersionList
	 * @Description: 新增脚本列表
	 * @param pubType
	 * @return
	 * @return List<ScriptVersion> 返回类型
	 * @throws
	 * @author LFG
	 */
	List<ScriptVersion> getScriptVersionList(Integer pubType);

	/**
	 * 
	 * @Title: getUpdateScriptVersionList
	 * @Description: 修改脚本列表
	 * @param pubType
	 * @param publicId
	 * @return
	 * @return List<ScriptVersion> 返回类型
	 * @throws
	 * @author LFG
	 */
	List<ScriptVersion> getUpdateScriptVersionList(Integer pubType, Integer publicId);

	/**
	 * 
	 * @Title: getScriptVerionBySelectedPublicIds
	 * @Description: 根据版本号读取脚本脚本列表
	 * @param publicIds
	 * @return
	 * @return List<ScriptVersion> 返回类型
	 * @throws
	 * @author LFG
	 */
	List<ScriptVersion> getScriptVerionBySelectedPublicIds(Integer[] publicIds);

}
