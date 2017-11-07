package rongji.cmis.service.system;

import java.util.List;

import rongji.cmis.model.sys.VersionPublicDet;
import rongji.framework.base.service.BaseService;

public interface VersionPublicDetService extends BaseService<VersionPublicDet> {
	/**
	 * 
	 * @Title: getSeqnoByPublicId
	 * @Description: 根据版本号查找脚本号列表
	 * @param publicId
	 * @return
	 * @return List<Integer> 返回类型
	 * @throws
	 * @author LFG
	 */
	List<Integer> getSeqnoByPublicId(Integer publicId);

}
