package rongji.cmis.service.system;

import rongji.cmis.model.ums.RoleInfosetPri;
import rongji.framework.base.service.BaseService;

import java.util.List;

public interface RoleInfosetPriService extends BaseService<RoleInfosetPri> {
	/**
	 * 
	 * @Title: grantRoleInfoCadreSet
	 * @Description: 角色分配信息集/信息项权限
	 * @param roleId
	 *            角色ID
	 * @param privCodes
	 *            信息集/信息项数组
	 * @param privs
	 *            权限数组 0：只读，1：可写
	 * @param infoType
	 *            数据类型 0:信息集,1:信息项
	 * @return void 返回类型
	 * @throws
	 * @author LFG
	 */
	void grantRoleInfoCadreSet(Integer roleId, String[] privCodes, Integer[] privs, Integer infoType);

	/**
	 * 
	 * @Title: delRoleCadreByRoleIdAndInfoType
	 * @Description: 删除该角色对应的信息集/信息项已分配的角色
	 * @param roleId
	 *            角色ID
	 * @param infoType
	 *            类型0：信息集 ，1：信息项
	 * @return void 返回类型
	 * @throws
	 * @author LFG
	 */
	public void delRoleCadreByRoleIdAndInfoType(Integer roleId, Integer infoType);

	/**
	 * 
	 * @Title: getRoleInfosetListByRoleAndPrivCode
	 * @Description: 根据角色ID或权限编码获取 角色信息集信息项权限列表
	 * @param roleid
	 *            权限ID
	 * @param privCode
	 *            权限编码
	 * @return
	 * @return List<RoleInfosetPri> 返回类型
	 * @throws
	 * @author LFG
	 */
	List<RoleInfosetPri> getRoleInfosetListByRoleAndPrivCode(Integer roleid, String privCode);

	/**
	 * 
	 * @Title: getInfoSetPrivList
	 * @Description: 查看当前用户对应的信息集和信息项权限（最高）
	 * @param privCode
	 *            信息集
	 * @return
	 * @return List<RoleInfosetPri> 返回类型
	 * @throws
	 * @author LFG
	 */
	public List<RoleInfosetPri> getInfoSetPrivList(String privCode);

	RoleInfosetPri getInfoSetPriv(String infoSetCode, String privCode,String fromType);

	void delRoleInfosetPriByRoleId(Integer roleId);

	/**
	 * 
	 * @Title: getInfoSetPriv
	 * @Description: 信息集权限
	 * @param infoSetCode
	 *            信息集
	 * @return
	 * @return Boolean 返回类型
	 * @throws
	 * @author Administrator
	 */
	RoleInfosetPri getInfoSetPriv(String infoSetCode,String fromType);

//	RoleInfosetPri getInfoSetPrivByEntId(String entId, String infoSetCode);

	RoleInfosetPri getInfoSetPrivByEntId(String entId, String infoSetCode, String fromType,Integer userId,String aprStatus);

	Boolean getInfoSetPriv2(String infoSetCode, String privCode, String autoCadreId);

	List<RoleInfosetPri> getRoleInfosetPriListByRoleArr(Integer[] roleIdArr);
	
	/**
	 * 企业的操作权限（角色-处室  匹配 干部-处室）
	 */
	Boolean isHasCadreMenuOperAuth(String cadreId, String operAuthCode);

}
