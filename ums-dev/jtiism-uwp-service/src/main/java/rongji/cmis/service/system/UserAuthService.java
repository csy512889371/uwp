package rongji.cmis.service.system;

import java.util.List;
import java.util.Set;

import rongji.cmis.model.ums.CfgUmsRole;
import rongji.cmis.model.ums.CfgUmsUser;

/**
 * 
* @Title: UserAuthService.java
* @Package rongji.cmis.service.system
* @Description: 用户认证 service
 */
public interface UserAuthService {
	
	List<CfgUmsRole> findRoles(Integer userId);
	
	Set<String> findStringRoles(CfgUmsUser user);
	
	Set<String> findStringPermissions(CfgUmsUser user);
	
}
