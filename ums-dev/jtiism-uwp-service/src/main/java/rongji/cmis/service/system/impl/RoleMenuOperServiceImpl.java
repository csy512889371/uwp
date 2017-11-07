package rongji.cmis.service.system.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import rongji.cmis.dao.system.RoleDao;
import rongji.cmis.dao.system.RoleMenuOperDao;
import rongji.cmis.model.ums.CfgUmsRole;
import rongji.cmis.model.ums.CfgUmsRoleMenuOper;
import rongji.cmis.service.system.RoleMenuOperService;
import rongji.framework.base.dao.mapper.BulkMapper;
import rongji.framework.base.service.impl.BaseServiceImpl;
import rongji.framework.base.service.utils.MulBulkMapperConverter;

@Service("roleMenuOperServiceImpl")
public class RoleMenuOperServiceImpl extends BaseServiceImpl<CfgUmsRoleMenuOper> implements RoleMenuOperService {

	@Resource(name = "roleMenuOperDaoImpl")
	RoleMenuOperDao roleMenuOperDao;

	@Resource(name = "roleDaoImpl")
	RoleDao roleDao;

	@Override
	public void deleteByRoleId(Integer roleId) {
		roleMenuOperDao.deleteByRoleId(roleId);
	}

	/**
	 * 
	 * @Title: grantRoleMenu
	 * @Description: (为角色分配菜单项)
	 * @param roleId
	 * @param menuIds
	 * @return void 返回类型
	 * @throws
	 * @author spongebob
	 */
	@Override
	public void grantRoleMenu(Integer roleId, Integer[] menuIds) {
		try {
			CfgUmsRole role = roleDao.find(roleId);
			List<CfgUmsRoleMenuOper> roleMenuOperList = role.getMenuOpers();// 获取role的原始菜单数据
			Set<Integer> menuIdSet = new HashSet<Integer>(Arrays.asList(menuIds));// 将role新选定的菜单id放入menuIdSet中

			Iterator<CfgUmsRoleMenuOper> iterator = roleMenuOperList.iterator();
			while (iterator.hasNext()) {
				CfgUmsRoleMenuOper roleMenuOpered = iterator.next();
				if (menuIdSet.contains(roleMenuOpered.getMenuid())) {
					menuIdSet.remove(roleMenuOpered.getMenuid());
					continue;
				}
				iterator.remove();
			}
			for (Integer menuId : menuIdSet) {
				role.addMenuOpers(new CfgUmsRoleMenuOper(menuId, null));
			}
			role = (CfgUmsRole) roleDao.merge(role);
		} catch (Exception e) {

		}

	}

	/**
	 * 
	 * @Title: grantRoleMenuOper
	 * @Description: (为角色分配菜单操作项)
	 * @param roleId
	 * @param menuId
	 * @param checkOperId
	 * @return void 返回类型
	 * @throws
	 * @author spongebob
	 */
	@Override
	public void grantRoleMenuOper(Integer roleId, Integer menuId, Integer[] checkOperIds) {
		List<CfgUmsRoleMenuOper> roleMenuOpers = roleMenuOperDao.findByPropertys(new String[] { "role_id", "menuid" }, new Integer[] { roleId, menuId });
		CfgUmsRoleMenuOper cfgUmsRoleMenu = roleMenuOpers.get(0);
		Set<Integer> operIdList = new HashSet<Integer>(Arrays.asList(checkOperIds));
		cfgUmsRoleMenu.setOperIds(operIdList);
		roleMenuOperDao.merge(cfgUmsRoleMenu);
	}

	@Override
	public void deleteByMenuId(Integer menuId) {
		roleMenuOperDao.deleteByMenuId(menuId);
	}

	@Override
	public List<CfgUmsRoleMenuOper> findByMenuId(Integer menuId) {
		return roleMenuOperDao.findByProperty("menuid", menuId);
	}

	@Override
	public List<Map<String, Object>> findByRoleId(Integer roleId) {
		List<CfgUmsRoleMenuOper> roleMenuOper = roleMenuOperDao.findByProperty("role_id", roleId);
		BulkMapper<CfgUmsRoleMenuOper> bulk = new BulkMapper<CfgUmsRoleMenuOper>() {
			@Override
			public void mapBulk(CfgUmsRoleMenuOper entity, Map<String, Object> paramMap, Map<String, Object> requestMap) {
				String role_id = (entity.getRole() != null) ? String.valueOf(entity.getRole().getId()) : "";
				paramMap.put("role_id", role_id);
			}
		};

		MulBulkMapperConverter<CfgUmsRoleMenuOper> converter = new MulBulkMapperConverter<CfgUmsRoleMenuOper>();
		return converter.convertListResultToMap(bulk, roleMenuOper);

	}

}
