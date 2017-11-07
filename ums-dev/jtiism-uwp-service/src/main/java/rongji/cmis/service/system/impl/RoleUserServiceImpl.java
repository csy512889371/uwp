package rongji.cmis.service.system.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rongji.cmis.dao.system.MenuDao;
import rongji.cmis.dao.system.MenuOperDao;
import rongji.cmis.dao.system.RoleMenuOperDao;
import rongji.cmis.dao.system.RoleUserDao;
import rongji.cmis.model.ums.CfgUmsMenu;
import rongji.cmis.model.ums.CfgUmsMenuOper;
import rongji.cmis.model.ums.CfgUmsRoleMenuOper;
import rongji.cmis.model.ums.CfgUmsRoleUser;
import rongji.cmis.service.system.RoleUserService;
import rongji.cmis.service.system.UserService;
import rongji.framework.base.service.impl.BaseServiceImpl;
import rongji.framework.util.SettingUtils;
import rongji.framework.util.StringUtil;
import rongji.redis.core.annotations.RedisCacheEvict;
import rongji.redis.core.impl.RedisClient;
import rongji.redis.core.utils.KeyUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("roleUserServiceImpl")
public class RoleUserServiceImpl extends BaseServiceImpl<CfgUmsRoleUser> implements RoleUserService {

	private static final Logger logger = LoggerFactory.getLogger(RoleUserServiceImpl.class);
	@Resource(name = "userServiceImpl")
	UserService userService;

	@Resource(name = "roleUserDaoImpl")
	RoleUserDao roleUserDao;

	@Resource(name = "roleMenuOperDaoImpl")
	RoleMenuOperDao roleMenuOperDao;

	@Resource(name = "menuOperDaoImpl")
	MenuOperDao menuOperDao;

	@Resource(name = "menuDaoImpl")
	MenuDao menuDao;

    @Autowired
    private RedisClient redisClient;

	@Override
	public List<CfgUmsRoleUser> findByUserId(Integer userId) {
		return roleUserDao.findByUserId(userId);
	}

	@Override
	public List<String> getCurrentUserRight() {
		
		List<CfgUmsRoleMenuOper> roleMenuOpers = null;//单个角色权限
		List<CfgUmsRoleMenuOper> roleMenuOperList = new ArrayList<CfgUmsRoleMenuOper>();//用户所有角色权限
		
		try {
			List<String> rights = new ArrayList<String>();
			// 1.获取当前用户id(userId)
			Integer userId = userService.getCurrentUserId();// 获取当前用户id
			// 2.根据userId获取对应的roleId
			List<CfgUmsRoleUser> cfgUmsRoleUserList = this.findByUserId(userId);
			// 3.根据角色（roleId）获取对应的有操作权限菜单
			for (CfgUmsRoleUser curu : cfgUmsRoleUserList) {
				roleMenuOpers = roleMenuOperDao.findByProperty("role_id", curu.getRoleid());
				roleMenuOperList.addAll(roleMenuOpers);
			}

			// 4.设置codeMenu
			List<CfgUmsMenu> menus = menuDao.findAll();
			Map<Integer, String> codeMenuMap = new HashMap<Integer, String>();//
			// codeMenu以menuId为key存储code
			for (CfgUmsMenu menu : menus) {
				codeMenuMap.put(menu.getId(), menu.getCode());
			}
			// 5.设置menuOper
			List<CfgUmsMenuOper> menOper = menuOperDao.findAll();
			// codeOperMap 以CfgUmsMenuOper.id为key permission为value
			Map<Integer, String> codeOperMap = new HashMap<Integer, String>();
			for (CfgUmsMenuOper menuOper : menOper) {
				codeOperMap.put(menuOper.getId(), menuOper.getPermission());
			}
			// 6.整理数据
			for (CfgUmsRoleMenuOper roleMenuOper : roleMenuOperList) {
				if (codeMenuMap.containsKey(roleMenuOper.getMenuid())) {// 确定menuId存在
					for (Integer operId : roleMenuOper.getOperIds()) {// 遍历被选中的operId
						if (StringUtil.isNotEmpty(codeOperMap.get(operId))) {
							rights.add(codeOperMap.get(operId));
						}
					}
				}
			}
			return rights;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<String>();
		}

	}

    /**
     * 获取当前用户权限并存入redis
     * @return
     */
    @Override
    public List<String> getCurrentUserRightWithRedis() {
        try {
            List<String> rights = new ArrayList<String>();
            // 1.获取当前用户id(userId)
            Integer userId = userService.getCurrentUserId();// 获取当前用户id
            String redisKey = KeyUtils.getAdminUserRightKey(userId + "");
			if (redisClient.canNotUse(SettingUtils.get().isNeedRedis())) {
				rights = getCurrentUserRight();
            } else { 
            	rights = redisClient.opsForHash().hvals(redisKey);
                if (!rights.isEmpty()) {
					rights = redisClient.opsForHash().hvals(redisKey);
                } else {
					rights = getCurrentUserRight();
                    //构造权限Map
                    Map<Integer, String> rightsMap = new HashMap<>();
                    int i = 0;
                    for (String right : rights) {
                        rightsMap.put(i++, right);
                    }
                    redisClient.opsForHash().hmset(redisKey, rightsMap, 3600);
                }
            }
            return rights;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ArrayList<String>();
        }
    }

	@Override
	@RedisCacheEvict(key="adminUserEnt" ,entId="")
	public void deleteRoleUserByUserId(Integer userId) {
		roleUserDao.deleteRoleUserByUserId(userId);

	}

	@Override
	@RedisCacheEvict(key="adminUserEnt" ,entId="")
	public void deleteRoleUserByRoleId(Integer roleId) {
		roleUserDao.deleteRoleUserByRoleId(roleId);

	}

	@Override
	public List<CfgUmsRoleUser> findRoleByUserId(Integer userId) {
		return roleUserDao.findRoleByUserId(userId);
	}

	/**
	 * @Description: (根据角色ID查询其配置的用户)
	 * @Title: findUserByRoleId
	 * @param roleId
	 * @return
	 * @return List<CfgUmsRoleUser>    返回类型
	 * @throws
	 * @author LinJH 2016-4-19
	 */
	@Override
	public List<CfgUmsRoleUser> findUserByRoleId(Integer roleId) {
		return roleUserDao.findUserByRoleId(roleId);
	}

	@Override
	@RedisCacheEvict(key="adminUserEnt" ,entId="")
	public void save(CfgUmsRoleUser entity){
		roleUserDao.save(entity);
	}

}
