package rongji.cmis.service.system.impl;

import org.springframework.stereotype.Service;
import rongji.cmis.dao.system.RoleDao;
import rongji.cmis.model.ums.CfgUmsRole;
import rongji.cmis.model.ums.CfgUmsRoleUser;
import rongji.cmis.model.ums.RoleInfosetPri;
import rongji.cmis.service.system.*;
import rongji.framework.base.pojo.Filter;
import rongji.framework.base.service.impl.BaseServiceImpl;
import rongji.framework.util.Constant;
import rongji.framework.util.ParamRequest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("roleServiceImpl")
public class RoleServiceImpl extends BaseServiceImpl<CfgUmsRole> implements RoleService {

	@Resource(name = "roleDaoImpl")
	RoleDao roleDao;
	
	@Resource(name = "userServiceImpl")
	UserService userService;
	
	@Resource(name = "roleUserServiceImpl")
	RoleUserService roleUserService;
	
	@Resource(name = "roleInfosetPriServiceImpl")
	RoleInfosetPriService roleInfosetPriService;
	
	@Resource(name = "roleDeptServiceImpl")
	RoleDeptService roleDeptService;
	
	@Override
	public List<CfgUmsRole> findRoleByUser(Integer userId) {
		return roleDao.findRoleByUser(userId);
	}

	/**
	 * 删除角色
	 */
	@Override
	public void deleteByRoleId(Integer roleId) {
		
		roleUserService.deleteRoleUserByRoleId(roleId);//删除角色与用户关系表数据
		roleDeptService.deleteRoleDeptByRoleId(roleId.toString());//删除角色与管理权限关系关系表数据
		roleInfosetPriService.delRoleInfosetPriByRoleId(roleId);//删除角色与角色信息集信息项权限表数据
		this.delete(roleId);
	}

	@Override
	public Map<String, Object> getRoleCadreInfoByUserRole(Integer infoType,String fromType) {
		Map<String,Object> infoSetPrivMap = new HashMap<String, Object>();
		if(Constant.ADMIN_FROMTYPE.equals(fromType)) {
			// 1.获取当前用户id(userId)
			Integer userId = userService.getCurrentUserId();// 获取当前用户id
			// 2.根据userId获取对应的roleId
			List<CfgUmsRoleUser> cfgUmsRoleUserList = roleUserService.findByUserId(userId);
			for (CfgUmsRoleUser curu : cfgUmsRoleUserList) {
				ParamRequest param = new ParamRequest();
				param.addFilter(Filter.eq("cfgUmsRole.id", curu.getRoleid()));
				param.addFilter(Filter.eq("infoType", infoType));
				List<RoleInfosetPri> roleInfosetPriList = roleInfosetPriService.findAllByParamRequest(param);
				for (RoleInfosetPri rip : roleInfosetPriList) {
					infoSetPrivMap.put(rip.getPrivCode(), rip.getPriv());
				}
			}
		}else if(Constant.MEMBER_FROMTYPE.equals(fromType)){
			ParamRequest param = new ParamRequest();
			param.addFilter(Filter.eq("cfgUmsRole.id", 2));
			param.addFilter(Filter.eq("infoType", infoType));
			List<RoleInfosetPri> roleInfosetPriList = roleInfosetPriService.findAllByParamRequest(param);
			for (RoleInfosetPri rip : roleInfosetPriList) {
				infoSetPrivMap.put(rip.getPrivCode(), rip.getPriv());
			}
		}
		return infoSetPrivMap;
	}

}
