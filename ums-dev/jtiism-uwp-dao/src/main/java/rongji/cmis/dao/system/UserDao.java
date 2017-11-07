package rongji.cmis.dao.system;

import rongji.cmis.model.ums.CfgUmsUser;
import rongji.framework.base.dao.GenericDao;

import java.util.List;

public interface UserDao extends GenericDao<CfgUmsUser> {


    /**
     * @param user
     * @return List<CfgUmsUser> 返回类型
     * @throws
     * @Title: checkLoginUser
     * @Description: (校验登录用户)
     * @author Administrator
     */
    List<CfgUmsUser> checkLoginUser(CfgUmsUser user);

    /**
     * 根据用户名查找管理员
     *
     * @param username 用户名(忽略大小写)
     * @return 管理员，若不存在则返回null
     */
    CfgUmsUser findByUsername(String username);

}
