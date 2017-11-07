package rongji.cmis.dao.system;

import rongji.cmis.model.ums.RoleInfosetPri;
import rongji.framework.base.dao.GenericDao;

import java.util.List;
import java.util.Map;

public interface RoleInfosetPriDao extends GenericDao<RoleInfosetPri> {

    void delRoleCadreByRoleIdAndInfoType(Integer roleId, Integer infoType);

    List<RoleInfosetPri> getRoleInfosetListByRoleAndPrivCode(Integer roleId, String privCode);

    void delRoleInfosetPriByRoleId(Integer roleId);

    List<RoleInfosetPri> getRoleInfosetPriListByRoleArr(Integer[] roleIdArr);

    Integer getInfosetPriByUserId(int userId, String infoSet);

    Integer getInfosetPriByRoleId(int roleId, String infoSet);

    /**
     * 根据用户据ID 企业ID 获取该用户 该企业的 信息集权限
     * @param userId
     * @param entId
     * @param infoSet
     * @return
     */
    Integer getInfosetPriByUserIdAndEntId(int userId, String entId, String infoSet);


    /**
     * 根据用户据ID 企业ID 获取该角色 该企业的 信息集权限
     * @param roleId
     * @param entId
     * @param infoSet
     * @return
     */
    Integer getInfosetPriByRoleIdAndEntId(int roleId, String entId, String infoSet);

    /**
     * 根据用户ID 企业ID 获取该用户 该企业的 信息集权限集合
     *
     * @param userId
     * @param entId
     * @return
     */
    List<Map<String, Object>> getInfosetPriListByUserIdAndEntId(int userId, String entId);
    /**
     * 根据用户ID 企业ID 获取该用户 该企业的 信息集权限集合
     *
     * @param roleId
     * @param entId
     * @return
     */
    List<Map<String,Object>> getInfosetPriListByRoleIdAndEntId(int roleId, String entId);
}
