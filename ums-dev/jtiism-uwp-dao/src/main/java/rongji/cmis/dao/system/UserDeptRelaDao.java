package rongji.cmis.dao.system;

import rongji.cmis.model.ums.UserDeptRela;
import rongji.framework.base.dao.GenericDao;

public interface UserDeptRelaDao extends GenericDao<UserDeptRela> {

	void deleteByUserId(Integer id);

}
