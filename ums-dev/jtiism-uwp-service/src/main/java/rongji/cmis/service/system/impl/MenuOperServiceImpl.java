package rongji.cmis.service.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rongji.cmis.dao.system.MenuOperDao;
import rongji.cmis.model.ums.CfgUmsMenuOper;
import rongji.cmis.service.system.MenuOperService;
import rongji.cmis.service.system.UserService;
import rongji.framework.base.service.impl.BaseServiceImpl;
import rongji.framework.base.service.utils.MulBulkMapperConverter;
import rongji.framework.util.Constant;
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

@Service("menuOperServiceImpl")
public class MenuOperServiceImpl extends BaseServiceImpl<CfgUmsMenuOper> implements MenuOperService {

    @Resource(name = "menuOperDaoImpl")
    MenuOperDao menuOperDao;

    @Resource(name = "userServiceImpl")
    UserService userService;

    @Autowired
    private RedisClient redisClient;

    @Override
    @RedisCacheEvict(key = "adminUserEnt", entId = "")
    public void deleteByMenuId(Integer menuId) {
        menuOperDao.deleteByMenuId(menuId);

    }

    @Override
    @RedisCacheEvict(key = "adminUserEnt", entId = "")
    public void saveOrUpdateMenuOper(CfgUmsMenuOper menuOper) {

        if (menuOper.getId() == null || menuOperDao.find(menuOper.getId()) == null) {// 新增
            menuOperDao.save(menuOper);
        } else {// 修改
            CfgUmsMenuOper menuOperForData = menuOperDao.find(menuOper.getId());
            menuOperForData.setOpername(menuOper.getOpername());
            menuOperForData.setPermission(menuOper.getPermission());
            menuOperForData = (CfgUmsMenuOper) menuOperDao.merge(menuOperForData);
        }
    }

    @Override
    public List<Map<String, Object>> findByMenuId(Integer menuId) {
        List<CfgUmsMenuOper> menuOpers = menuOperDao.findByProperty("menuid", menuId);

        MulBulkMapperConverter<CfgUmsMenuOper> converter = new MulBulkMapperConverter<CfgUmsMenuOper>();
        return converter.convertListResultToMap(null, menuOpers);
    }

    @Override
    public List<String> getCurrentUserMenuOperRight(String entId, String fromType) {
        if (Constant.ADMIN_FROMTYPE.equals(fromType)) {
            List<String> rights = new ArrayList<String>();
            // 1.获取当前用户id(userId)
            Integer userId = userService.getCurrentUserId();// 获取当前用户id
            if (StringUtil.isEmpty(entId) || userId == null) {
                return rights;
            }
            try {
                String redisKey = KeyUtils.getAdminUserEntMenuOperRightKey(userId + "", fromType, entId);
                if (redisClient.canNotUse(SettingUtils.get().isNeedRedis())) {
                    rights = menuOperDao.getUserEntMenuOperRight(userId + "", entId);
                } else {
                    if (redisClient.exists(redisKey.getBytes())) {
                        rights = redisClient.opsForHash().hvals(redisKey);
                        if (rights.isEmpty()) {
                            rights = menuOperDao.getUserEntMenuOperRight(userId + "", entId);
                        }
                    } else {
                        rights = menuOperDao.getUserEntMenuOperRight(userId + "", entId);
                    }
                    if (!rights.isEmpty()) {
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
//			logger.error(e.getMessage(), e);
                return rights;
            }
        } else {

            List<String> rights = new ArrayList<String>();
            // 1.获取当前用户id(userId)
            Integer userId = userService.getCurrentUserId();// 获取当前用户id
            if (StringUtil.isEmpty(entId) || userId == null) {
                return rights;
            }
            try {
                String redisKey = KeyUtils.getAdminUserEntMenuOperRightKey(userId + "", fromType, entId);
                if (redisClient.canNotUse(SettingUtils.get().isNeedRedis())) {
                    rights = menuOperDao.getUserEntMenuOperRight(userId + "", entId);
                } else {
                    if (redisClient.exists(redisKey.getBytes())) {
                        rights = redisClient.opsForHash().hvals(redisKey);
                        if (rights.isEmpty()) {
                            rights = menuOperDao.getUserEntMenuOperRight(userId + "", entId);
                        }
                    } else {
                        rights = menuOperDao.getUserEntMenuOperRight(userId + "", entId);
                    }
                    if (!rights.isEmpty()) {
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
//			logger.error(e.getMessage(), e);
                return rights;
            }
        }

    }
}
