package rongji.cmis.service.system.impl;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import rongji.cmis.model.ums.CfgUmsMenu;
import rongji.cmis.model.ums.CfgUmsMenuOper;
import rongji.cmis.model.ums.CfgUmsRole;
import rongji.cmis.model.ums.CfgUmsRoleMenuOper;
import rongji.cmis.model.ums.CfgUmsUser;
import rongji.cmis.service.system.MenuOperService;
import rongji.cmis.service.system.MenuService;
import rongji.cmis.service.system.RoleService;
import rongji.cmis.service.system.UserAuthService;
import rongji.cmis.service.system.UserService;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;

@Service("userAuthServiceImpl")
public class UserAuthServiceImpl implements UserAuthService {

	@Resource(name = "userServiceImpl")
	UserService userService;

	@Resource(name = "roleServiceImpl")
	RoleService roleService;

	@Resource(name = "menuServiceImpl")
	MenuService menuService;

	@Resource(name = "menuOperServiceImpl")
	MenuOperService menuOperService;

	@Override
	public Set<String> findStringRoles(CfgUmsUser user) {
		List<CfgUmsRole> roles = findRoles(user.getId());
		return Sets.newHashSet(Collections2.transform(roles, new Function<CfgUmsRole, String>() {
			@Override
			public String apply(CfgUmsRole input) {
				return input.getCode();
			}
		}));

	}

	/**
	 * 第一部分是域）, 第二部分是操作
	 * 
	 */
	@Override
	public Set<String> findStringPermissions(CfgUmsUser user) {
		Set<String> permissions = Sets.newHashSet();
		
		List<CfgUmsRole> roles = findRoles(user.getId());
		for (CfgUmsRole role : roles) {
			for (CfgUmsRoleMenuOper rmo : role.getMenuOpers()) {

				CfgUmsMenu menu = menuService.find(rmo.getMenuid());
				// 获取 权限标识
				String actualResourceIdentity = menu.getCode();
				// 不可用 即没查到 或者标识字符串不存在
				if (menu == null || StringUtils.isEmpty(actualResourceIdentity) || Boolean.FALSE.equals(menu.getState())) {
					continue;
				}
				
				Set<Integer> operIds = rmo.getOperIds();
				
				if (operIds != null && !operIds.isEmpty()) {
					for (Integer operid : rmo.getOperIds()) {
						CfgUmsMenuOper menuOper = menuOperService.find(operid);

						if (menuOper == null) {
							continue;
						}
						permissions.add(actualResourceIdentity + ":" + menuOper.getPermission());
					}
				} else {
					permissions.add(actualResourceIdentity + ":*");
				}
			}
		}

		return permissions;
	}

	@Override
	public List<CfgUmsRole> findRoles(Integer userId) {
		return roleService.findRoleByUser(userId);
	}

}
