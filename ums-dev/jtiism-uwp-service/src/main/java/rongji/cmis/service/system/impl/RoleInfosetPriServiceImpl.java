package rongji.cmis.service.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import rongji.cmis.dao.system.RoleDao;
import rongji.cmis.dao.system.RoleInfosetPriDao;
import rongji.cmis.model.ums.CfgUmsRole;
import rongji.cmis.model.ums.CfgUmsRoleUser;
import rongji.cmis.model.ums.RoleInfosetPri;
import rongji.cmis.service.system.*;
import rongji.framework.base.pojo.Filter;
import rongji.framework.base.pojo.Filter.Operator;
import rongji.framework.base.pojo.Order.Direction;
import rongji.framework.base.service.impl.BaseServiceImpl;
import rongji.framework.util.Constant;
import rongji.framework.util.ParamRequest;
import rongji.framework.util.SettingUtils;
import rongji.redis.core.annotations.RedisCacheEvict;
import rongji.redis.core.impl.RedisClient;
import rongji.redis.core.utils.KeyUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("roleInfosetPriServiceImpl")
public class RoleInfosetPriServiceImpl extends BaseServiceImpl<RoleInfosetPri> implements RoleInfosetPriService {

    @Resource(name = "roleInfosetPriDaoImpl")
    RoleInfosetPriDao roleInfosetPriDao;

    @Resource(name = "roleDaoImpl")
    RoleDao roleDao;

    @Resource(name = "userServiceImpl")
    UserService userService;

    @Resource(name = "roleUserServiceImpl")
    RoleUserService roleUserService;

    @Resource(name = "roleInfosetPriServiceImpl")
    RoleInfosetPriService roleInfosetPriService;

    @Resource(name = "departmentServiceImpl")
    DepartmentService departmentService;

    @Resource(name = "roleDeptServiceImpl")
    RoleDeptService roleDeptService;

    @Resource(name = "userAuthServiceImpl")
    private UserAuthService userAuthService;


    @Autowired
    private RedisClient redisClient;


    @Override
    @RedisCacheEvict(key = "adminUserEnt", entId = "")
    public void grantRoleInfoCadreSet(Integer roleId, String[] privCodes, Integer[] privs, Integer infoType) {
        // 分配单位权限之前先删除该角色已分配的信息集/信息项权限
        delRoleCadreByRoleIdAndInfoType(roleId, infoType);
        CfgUmsRole role = roleDao.find(roleId);
        for (int i = 0; i < privCodes.length; i++) {
            RoleInfosetPri rip = new RoleInfosetPri();
            rip.setCfgUmsRole(role);
            rip.setInfoType(infoType);
            rip.setPriv(privs[i]);
            rip.setPrivCode(privCodes[i]);

            roleInfosetPriDao.save(rip);
        }
    }

    @Override
    @RedisCacheEvict(key = "adminUserEnt", entId = "")
    public void delRoleCadreByRoleIdAndInfoType(Integer roleId, Integer infoType) {
        roleInfosetPriDao.delRoleCadreByRoleIdAndInfoType(roleId, infoType);
    }

    @Override
    public List<RoleInfosetPri> getRoleInfosetListByRoleAndPrivCode(Integer roleId, String privCode) {
        return roleInfosetPriDao.getRoleInfosetListByRoleAndPrivCode(roleId, privCode);
    }

    @Override
    public List<RoleInfosetPri> getInfoSetPrivList(String privCode) {

        // 1.获取当前用户id(userId)
        Integer userId = userService.getCurrentUserId();// 获取当前用户id
        // 2.根据userId获取对应的roleId
        List<CfgUmsRoleUser> cfgUmsRoleUserList = roleUserService.findByUserId(userId);
        Integer infoSetPriv = null;// 信息集权限
        Integer maxPrivRoleId = null;
        ParamRequest paramRequest = new ParamRequest();
        List<RoleInfosetPri> roleInfoList = null;// 信息集
        List<Integer> roleIds = new ArrayList<Integer>();
        for (CfgUmsRoleUser curu : cfgUmsRoleUserList) {
            if (null != curu.getRoleid()) {
                roleIds.add(curu.getRoleid());
            }
        }
        paramRequest.addFilter(Filter.in("cfgUmsRole.id", roleIds));
        paramRequest.addFilter(Filter.eq("privCode", privCode));

        List<RoleInfosetPri> roleInfosetPriList = roleInfosetPriService.findAllByParamRequest(paramRequest);
        for (RoleInfosetPri rip : roleInfosetPriList) {
            if (infoSetPriv == null || infoSetPriv <= rip.getPriv()) {
                infoSetPriv = rip.getPriv();
                maxPrivRoleId = rip.getCfgUmsRole().getId();
            }
        }
        if (null != maxPrivRoleId) {
            paramRequest = new ParamRequest();
            paramRequest.addFilter(Filter.eq("cfgUmsRole.id", maxPrivRoleId));
            paramRequest.addFilter(Filter.like("privCode", "%" + privCode + "_%"));// 过滤信息集

            roleInfoList = roleInfosetPriService.findAllByParamRequest(paramRequest);
        }

        return roleInfoList;
    }

    @Override
    public RoleInfosetPri getInfoSetPriv(String infoSetCode, String privCode, String fromType) {
        Integer infoSetPriv = null;// 信息集权限
        Integer maxPrivRoleId = null;// 最高权限角色ID
        Integer maxPriv = null;// 信息项最高权限
        ParamRequest paramRequest = new ParamRequest();
        RoleInfosetPri roleInfosetPri = null;// 信息集
        List<Integer> roleIds = new ArrayList<Integer>();
        if (Constant.ADMIN_FROMTYPE.equals(fromType)) {
            // 1.获取当前用户id(userId)
            Integer userId = userService.getCurrentUserId();// 获取当前用户id
            // 2.根据userId获取对应的roleId
            List<CfgUmsRoleUser> cfgUmsRoleUserList = roleUserService.findByUserId(userId);
            for (CfgUmsRoleUser curu : cfgUmsRoleUserList) {
                if (null != curu.getRoleid()) {
                    roleIds.add(curu.getRoleid());
                }
            }
        } else if (Constant.MEMBER_FROMTYPE.equals(fromType)) {
            roleIds.add(2);
        }
        paramRequest.addFilter(Filter.in("cfgUmsRole.id", roleIds));
        paramRequest.addFilter(Filter.eq("privCode", infoSetCode));

        List<RoleInfosetPri> roleInfosetPriList = roleInfosetPriService.findAllByParamRequest(paramRequest);
        for (RoleInfosetPri rip : roleInfosetPriList) {
            if (infoSetPriv == null || infoSetPriv <= rip.getPriv()) {
                infoSetPriv = rip.getPriv();
                maxPrivRoleId = rip.getCfgUmsRole().getId();
                maxPriv = rip.getPriv();
            }
        }
        if (null != maxPrivRoleId) {
            paramRequest = new ParamRequest();
            paramRequest.addFilter(Filter.eq("cfgUmsRole.id", maxPrivRoleId));
            paramRequest.addFilter(Filter.eq("privCode", privCode));// 过滤信息集
            List<RoleInfosetPri> roleInfoList = roleInfosetPriService.findAllByParamRequest(paramRequest);
            if (roleInfoList.size() > 0) {
                roleInfosetPri = roleInfoList.get(0);
                if (maxPriv < roleInfosetPri.getPriv()) {
                    roleInfosetPri.setPriv(maxPriv);
                }
            }
        }

        return roleInfosetPri;
    }

    @Override
    @RedisCacheEvict(key = "adminUserEnt", entId = "")
    public void delRoleInfosetPriByRoleId(Integer roleId) {
        roleInfosetPriDao.delRoleInfosetPriByRoleId(roleId);
    }

    @Override
    public RoleInfosetPri getInfoSetPriv(String infoSetCode, String fromType) {
        RoleInfosetPri roleInfosetPri = null;
        Integer maxPri = 0;
        if (Constant.ADMIN_FROMTYPE.equals(fromType)) {
            // 1.获取当前用户id(userId)
            Integer userId = userService.getCurrentUserId();// 获取当前用户id

		/*// 2.根据userId获取对应的roleId
        List<CfgUmsRoleUser> cfgUmsRoleUserList = roleUserService.findByUserId(userId);
		Integer infoSetPriv = null;// 信息集权限
		Integer maxPrivRoleId = null;// 最高权限角色ID
		Integer maxPriv = null;// 信息项最高权限
		ParamRequest paramRequest = new ParamRequest();
		RoleInfosetPri roleInfosetPri = null;// 信息集
		List<Integer> roleIds = new ArrayList<Integer>();
		for (CfgUmsRoleUser curu : cfgUmsRoleUserList) {
			if (null != curu.getRoleid()) {
				roleIds.add(curu.getRoleid());
			}
		}
		paramRequest.addFilter(Filter.in("cfgUmsRole.id", roleIds));
		paramRequest.addFilter(Filter.eq("privCode", infoSetCode));

		List<RoleInfosetPri> roleInfosetPriList = roleInfosetPriService.findAllByParamRequest(paramRequest);
		for (RoleInfosetPri rip : roleInfosetPriList) {
			if (infoSetPriv == null || infoSetPriv <= rip.getPriv()) {
				infoSetPriv = rip.getPriv();
				maxPrivRoleId = rip.getCfgUmsRole().getId();
				maxPriv = rip.getPriv();
			}
		}
		if (null != maxPrivRoleId) {
			paramRequest = new ParamRequest();
			paramRequest.addFilter(Filter.eq("cfgUmsRole.id", maxPrivRoleId));
			paramRequest.addFilter(Filter.eq("privCode", infoSetCode));// 信息集
			List<RoleInfosetPri> roleInfoList = roleInfosetPriService.findAllByParamRequest(paramRequest);
			if (roleInfoList.size() > 0) {
				roleInfosetPri = roleInfoList.get(0);
				if (maxPriv < roleInfosetPri.getPriv()) {
					roleInfosetPri.setPriv(maxPriv);
				}
			}
		}*/
            maxPri = roleInfosetPriDao.getInfosetPriByUserId(userId, infoSetCode);
        } else {
            maxPri = roleInfosetPriDao.getInfosetPriByRoleId(2, infoSetCode);
        }
        if (maxPri != null) {
            roleInfosetPri = new RoleInfosetPri();
            roleInfosetPri.setPriv(maxPri);
        }
        return roleInfosetPri;
    }


    /**
     * 根据企业ID 信息集代码 获取改企业的信息集权限
     *
     * @param entId
     * @param infoSetCode
     * @return
     */
    @Override
    public RoleInfosetPri getInfoSetPrivByEntId(String entId, String infoSetCode, String fromType, Integer memberUserId,String aprStatus) {
        if (Constant.ADMIN_FROMTYPE.equals(fromType)) {
            // 1.获取当前用户id(userId)
           Integer  userId = userService.getCurrentUserId();// 获取当前用户id
            // 获取 企业ID 用户ID 对应的RedisKey
            String redisKey = KeyUtils.getAdminUserEtpKey(userId + "", entId, fromType);
            Integer maxPri = null;
            String maxPriStr = null;
            RoleInfosetPri roleInfosetPri = null;
            if (redisClient.canNotUse(SettingUtils.get().isNeedRedis())) {
                maxPri = roleInfosetPriDao.getInfosetPriByUserIdAndEntId(userId, entId, infoSetCode);
            } else {
                try {
                    if (redisClient.exists(redisKey.getBytes())) {//如果存在这个KEY
                        if (redisClient.opsForHash().hexists(redisKey, infoSetCode)) {
                            maxPriStr = redisClient.opsForHash().hget(redisKey, infoSetCode);
                            maxPri = Integer.valueOf(maxPriStr);
                        } else {
                            maxPri = roleInfosetPriDao.getInfosetPriByUserIdAndEntId(userId, entId, infoSetCode);
                            if (maxPri != null) {
                                redisClient.opsForHash().hset(redisKey, infoSetCode, maxPri + "", 3600);
                            } else {
                                redisClient.opsForHash().hset(redisKey, infoSetCode, null, 3600);
                            }
                        }
                    } else {//如果不存在这个KEY //则塞入该KEY所有的的缓存
                        List<Map<String, Object>> userEntInfoSetList = roleInfosetPriDao.getInfosetPriListByUserIdAndEntId(userId, entId);
                        for (Map<String, Object> infoSetMap : userEntInfoSetList) {
                            String _entId = (String) infoSetMap.get("ENTINF000");
                            Integer _maxPri = (Integer) infoSetMap.get("PRIV");
                            String _infoSetCode = (String) infoSetMap.get("PRIV_CODE");
                            String _redisKey = KeyUtils.getAdminUserEtpKey(userId + "", _entId, fromType);
                            redisClient.opsForHash().hset(_redisKey, _infoSetCode, _maxPri + "", 3600);
                        }
                    }
                } catch (Exception e) {
                    maxPri = roleInfosetPriDao.getInfosetPriByUserIdAndEntId(userId, entId, infoSetCode);
                }
            }
            if (maxPri != null) {
                roleInfosetPri = new RoleInfosetPri();
                roleInfosetPri.setPriv(maxPri);
            }
            return roleInfosetPri;
        } else {

            // 获取 企业ID 用户ID 对应的RedisKey
            String redisKey = KeyUtils.getMemberUserKey(String.valueOf(memberUserId));

            Integer maxPri = null;
            String maxPriStr = null;
            RoleInfosetPri roleInfosetPri = null;
            Integer roleId = SettingUtils.get().getMemberAccountRoleId();
            if("1".equals(aprStatus) || "2".equals(aprStatus)){
                roleId = SettingUtils.get().getMemberAccountNoAprRoleId();
            }
            if (redisClient.canNotUse(SettingUtils.get().isNeedRedis())) {
                maxPri = roleInfosetPriDao.getInfosetPriByRoleId( roleId, infoSetCode);
            } else {
                try {
                    if (redisClient.exists(redisKey.getBytes())) {//如果存在这个KEY
                        if (redisClient.opsForHash().hexists(redisKey, infoSetCode)) {
                            maxPriStr = redisClient.opsForHash().hget(redisKey, infoSetCode);
                            maxPri = Integer.valueOf(maxPriStr);
                        } else {
                            maxPri = roleInfosetPriDao.getInfosetPriByRoleId(roleId, infoSetCode);
                            if (maxPri != null) {
                                redisClient.opsForHash().hset(redisKey, infoSetCode, maxPri + "", 3600);
                            } else {
                                redisClient.opsForHash().hset(redisKey, infoSetCode, null, 3600);
                            }
                        }
                    } else {//如果不存在这个KEY //则塞入该KEY所有的的缓存
                        Integer _maxPri = roleInfosetPriDao.getInfosetPriByRoleId(roleId, infoSetCode);
                        redisClient.opsForHash().hset(redisKey, infoSetCode, _maxPri + "", 3600);
                    }
                } catch (Exception e) {
                    maxPri = roleInfosetPriDao.getInfosetPriByRoleId(roleId, infoSetCode);
                }
            }
            if (maxPri != null) {
                roleInfosetPri = new RoleInfosetPri();
                roleInfosetPri.setPriv(maxPri);
            }
            return roleInfosetPri;
        }
    }


    @Override
    public Boolean getInfoSetPriv2(String infoSetCode, String privCode, String cadreId) {
        Boolean hasPriv = false;
        Map<String, Object> deptWriteMap = new HashMap<String, Object>();// 处室写权限
        ParamRequest paramRequest = null;
        // paramRequest = new ParamRequest();
        // paramRequest.setOrderProperty("orderno");
        // paramRequest.setOrderDirection(Direction.asc);
        // List<CmisDepartment> cmisDepartmentList =
        // departmentService.findAllByParamRequest(paramRequest);
        paramRequest = new ParamRequest();
        paramRequest.addFilter(new Filter("a0000", Operator.eq, cadreId));
        paramRequest.setOrderDirection(Direction.desc);
        paramRequest.setOrderProperty("readonly");

        //TODO
/*		List<DepartmentA01Rela> cmisCaderDeptRelaList = cadreDeptRelaService.findAllByParamRequest(paramRequest);
        for (DepartmentA01Rela cadredept : cmisCaderDeptRelaList) {
			if ("0".equals(cadredept.getReadonly())) {// 拥有处室写权限
				deptWriteMap.put(cadredept.getDepartment().getId(), cadredept.getReadonly());
			}
		}*/
        // /////角色处室信息集权限
        Map<String, Object> roleDeptWriteMap = new HashMap<String, Object>();// 信息集写权限
        // 1.获取当前用户id(userId)
        Integer userId = userService.getCurrentUserId();// 获取当前用户id
        // 2.根据userId获取对应的roleId
        List<CfgUmsRoleUser> cfgUmsRoleUserList = roleUserService.findByUserId(userId);
        paramRequest = new ParamRequest();
        List<Integer> roleIds = new ArrayList<Integer>();// 角色Id
        for (CfgUmsRoleUser curu : cfgUmsRoleUserList) {
            if (null != curu.getRoleid()) {
                roleIds.add(curu.getRoleid());
            }
        }
        for (Integer roleId : roleIds) {
            paramRequest = new ParamRequest();
            paramRequest.addFilter(Filter.eq("cfgUmsRole.id", roleId));
            paramRequest.addFilter(Filter.eq("privCode", infoSetCode));// 信息集
            List<RoleInfosetPri> roleInfoList = roleInfosetPriService.findAllByParamRequest(paramRequest);
            RoleInfosetPri roleInfosetPri = null;
            if (roleInfoList.size() > 0) {
                roleInfosetPri = roleInfoList.get(0);
                String[] deptIdArr = roleDeptService.findDeptByRoleId(roleId);
                for (String tempDeptId : deptIdArr) {
                    Integer priv = (Integer) roleDeptWriteMap.get(tempDeptId);
                    if (null != priv && 1 == priv) {// 原来处室信息集是写
                        continue;
                    } else {
                        if (null != roleInfosetPri.getPriv() && 1 == roleInfosetPri.getPriv()) {
                            roleDeptWriteMap.put(tempDeptId, roleInfosetPri.getPriv());
                        }
                    }
                }
            }
        }
        for (Map.Entry<String, Object> entryDept : deptWriteMap.entrySet()) {
            String deptId = (String) entryDept.getKey();// 处室Id
            for (Map.Entry<String, Object> entryRoleDept : roleDeptWriteMap.entrySet()) {
                String roleDeptId = (String) entryRoleDept.getKey();// 角色处室ID
                if (deptId.equals(roleDeptId)) {
                    hasPriv = true;
                    break;
                }
            }
        }
        return hasPriv;
    }

    @Override
    public List<RoleInfosetPri> getRoleInfosetPriListByRoleArr(Integer[] roleIdArr) {
        return roleInfosetPriDao.getRoleInfosetPriListByRoleArr(roleIdArr);
    }

    /**
     * 企业的操作权限（角色-处室 匹配 干部-处室）
     */
    @Override
    public Boolean isHasCadreMenuOperAuth(String cadreId, String operAuthCode) {
        if (StringUtils.isEmpty(cadreId) || StringUtils.isEmpty(operAuthCode)) {
            return false;
        }

        // 1. 通过当前用户角色 匹配菜单操作与处室的关系
        Integer userId = userService.getCurrentUserId();
        if (userId == null) {
            return false;
        }

/*		// 2. 获取<处室id,操作项权限>
        List<Tuple2<String, String>> departMentPerms = userAuthService.findDepartmentPermissions(userId, operAuthCode);
		if (departMentPerms == null || departMentPerms.isEmpty()) {
			return false;
		}

		// 3. 获取干部处事权限
		List<String> deptList = cadreDeptRelaService.findUnReadOnlyDmcod(cadreId);
		if (deptList == null || deptList.isEmpty()) {
			return false;
		}

		// 4. 判断权限
		for (Tuple2<String, String> depPerm : departMentPerms) {
			if (operAuthCode.equals(depPerm._2()) && isExistInCadreDept(deptList, depPerm._1())) {
				return true;
			}
		}*/

        //return false;

        return true;
    }

    private boolean isExistInCadreDept(List<String> deptList, String deptId) {

        if (StringUtils.isEmpty(deptId)) {
            return false;
        }
        for (String dept : deptList) {
            if (deptId.equals(dept)) {
                return true;
            }
        }
        return false;
    }

}
