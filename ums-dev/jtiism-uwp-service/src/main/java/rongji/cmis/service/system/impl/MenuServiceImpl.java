package rongji.cmis.service.system.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import rongji.cmis.dao.system.MenuDao;
import rongji.cmis.dao.system.MenuOperDao;
import rongji.cmis.dao.system.RoleMenuOperDao;
import rongji.cmis.model.ums.CfgUmsMenu;
import rongji.cmis.model.ums.CfgUmsRole;
import rongji.cmis.model.ums.CfgUmsRoleMenuOper;
import rongji.cmis.service.system.MenuService;
import rongji.framework.base.dao.mapper.BulkMapper;
import rongji.framework.base.model.ResultModel;
import rongji.framework.base.service.impl.BaseServiceImpl;
import rongji.framework.base.service.utils.MulBulkMapperConverter;

import com.google.common.collect.Lists;

@Service("menuServiceImpl")
public class MenuServiceImpl extends BaseServiceImpl<CfgUmsMenu> implements MenuService {

	@Resource(name = "menuDaoImpl")
	MenuDao menuDao;

	@Resource(name = "roleMenuOperDaoImpl")
	RoleMenuOperDao roleMenuOperDao;

	@Resource(name = "menuOperDaoImpl")
	MenuOperDao menuOperDao;

	@Override
	public List<CfgUmsMenu> findMenuByRole(CfgUmsRole role) {
		List<CfgUmsRoleMenuOper> roleMenuOperList = role.getMenuOpers();
		List<Integer> menuIds = Lists.newArrayList();
		for (CfgUmsRoleMenuOper roleMenuOper : roleMenuOperList) {
			menuIds.add(roleMenuOper.getMenuid());
		}
		if(menuIds.isEmpty()){
			List<CfgUmsMenu> menuList = new ArrayList<>();
			return menuList;
		}
		return menuDao.findMenuByMenuIds(menuIds);
	}

	@Override
	public List<Map<String, Object>> findMenuMap() {
		List<CfgUmsMenu> menus = menuDao.findAll();
		BulkMapper<CfgUmsMenu> bulk = new BulkMapper<CfgUmsMenu>() {
			@Override
			public void mapBulk(CfgUmsMenu entity, Map<String, Object> paramMap, Map<String, Object> requestMap) {
				String parentId = (entity.getParent() != null) ? String.valueOf(entity.getParent().getId()) : "";
				paramMap.put("parentid", parentId);
			}
		};
		
		MulBulkMapperConverter<CfgUmsMenu> converter = new MulBulkMapperConverter<CfgUmsMenu>();
		return converter.convertListResultToMap(bulk, menus);
	}

	@Override
	public ResultModel deleteAfterChecked(Integer menuId) {
		List<CfgUmsRoleMenuOper> roleMenOpers = roleMenuOperDao.findByProperty("menuid", menuId);
		if (roleMenOpers != null && roleMenOpers.size() >= 1) {// 1.其它角色还拥有该菜单的授权，不可删除
			return ResultModel.error("不可删除，其他角色拥有该菜单的授权，请先取消授权");
		}
		List<CfgUmsMenu> menus = menuDao.findAll();
		for (CfgUmsMenu menu : menus) {
			CfgUmsMenu parent = menu.getParent();
			if (parent != null && menuId.equals(parent.getId())) {// 2.如果是某个菜单的父id,得先删除子菜单才可删除
				return ResultModel.error("不可删除！该菜单下还有其它子目录，请先删除子目录");
			}
		}
		// 可以删除了
		// 先删除菜单对应操作
		menuOperDao.deleteByMenuId(menuId);
		menuDao.delete(menuId);
		return ResultModel.success("删除成功");
	}

	@Override
	public CfgUmsMenu findMenuByCode(String menuCode) {
		return menuDao.findMenuByCode(menuCode);
	}
	
	/**
	 * 获取角色下的所有菜单
	 */
	@Override
	public List<CfgUmsMenu> findMenuByRoles(List<CfgUmsRole> roleList) {
		List<CfgUmsMenu> menuList = new ArrayList<CfgUmsMenu>();
		Set<Integer> menuIdSet = new HashSet<Integer>();
		for (CfgUmsRole cur : roleList) {
			List<CfgUmsRoleMenuOper> roleMenuOperList = cur.getMenuOpers();
			for (CfgUmsRoleMenuOper roleMenuOper : roleMenuOperList) {
				menuIdSet.add(roleMenuOper.getMenuid());
			}
		}
		
		if (!menuIdSet.isEmpty()) {
			menuList = menuDao.findMenuByMenuIds(new ArrayList<Integer>(menuIdSet));
		}
		
		return menuList;
	}

}
