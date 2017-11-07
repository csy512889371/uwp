package rongji.cmis.service.system;

import rongji.cmis.model.ums.CfgUmsUser;
import rongji.framework.base.pojo.Principal;
import rongji.framework.base.service.BaseService;

import java.util.List;

public interface UserService extends BaseService<CfgUmsUser> {

	/**
	 * 
	 * @Title: checkLoginUser
	 * @Description: (校验登录用户)
	 * @param user
	 * @return
	 * @return List<CfgUmsUser> 返回类型
	 * @throws
	 * @author Administrator
	 */
	public List<CfgUmsUser> checkLoginUser(CfgUmsUser user);

	/**
	 * 根据用户名查找管理员
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 管理员，若不存在则返回null
	 */
	CfgUmsUser findByUsername(String username);

	/**
	 * 获取当前登录管理员
	 * 
	 * @return 当前登录管理员,若不存在则返回null
	 */
	CfgUmsUser getCurrent();

	/**
	 * 获取当前登录管理员
	 *
	 * @return 当前登录管理员,若不存在则返回null
	 */
	Principal getCurrentPrincipal();

	/**
	 * 获取当前登录用户名
	 * 
	 * @return 当前登录用户名,若不存在则返回null
	 */
	String getCurrentUsername();

	/**
	 * 获取当前登录用户名id
	 * 
	 * @return 当前登录用户名,若不存在则返回null
	 */
	Integer getCurrentUserId();

	/**
	 * 
	 * @Title: getDeptIdsByUserId
	 * @Description: (取得当前登录用户的管理权限权限ID数组)
	 * @param userId
	 *            如果userId为空则取当前登录的用户ID
	 * @return
	 * @return String[] 返回类型
	 * @throws
	 * @author Administrator
	 */
	String[] getDeptIdsByUserId(Integer userId);

	public void saveUserAndDept(CfgUmsUser cfgUmsUser, String deptId);

	public void mergeUserAndDept(CfgUmsUser cfgUmsUser, String deptId);

	/**
	 * 删除企业
	 * 
	 * @Title: deleteUser
	 * @Description: (这里用一句话描述这个方法的作用)
	 * @param userId
	 * @return void 返回类型
	 * @throws
	 * @author LFG
	 */
	public void deleteUser(Integer userId);


}
