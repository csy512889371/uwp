package rongji.cmis.service.system;

import javax.servlet.http.HttpServletRequest;

import rongji.cmis.model.sys.VersionPublic;
import rongji.framework.base.service.BaseService;

public interface VersionPublicService extends BaseService<VersionPublic> {
	/**
	 * 
	 * @Title: saveVersionPublic
	 * @Description: 保存版本发布
	 * @param versionPublic
	 * @param selectedSeqnos
	 * @return VersionPublic 返回类型
	 * @throws
	 * @author LFG
	 */
	VersionPublic saveVersionPublic(VersionPublic versionPublic, String selectedSeqnos);

	/**
	 * 
	 * @Title: updateVersionPublic
	 * @Description: 修改版本发布
	 * @param versionPublic
	 * @param selectedSeqnos
	 * @return VersionPublic 返回类型
	 * @throws
	 * @author LFG
	 */
	VersionPublic updateVersionPublic(VersionPublic versionPublic, String selectedSeqnos);

	/**
	 * 
	 * @Title: delVersionPublic
	 * @Description: 删除版本发布
	 * @param publicId
	 * @return void 返回类型
	 * @throws
	 * @author LFG
	 */
	void delVersionPublic(String publicIds);

	String expVersionPublic(String publicIds, HttpServletRequest request);

}
