package rongji.redis.core.utils;

public abstract class KeyUtils {

    static final String DICT = "dataDict:";

    static final String ADMINUSER_ENT = "adminUserEnt:";

    static final String MEMBER_USER = "memberUser:";


    static final String ADMINUSER_RIGHT = "adminUserEnt:";

    static final String ADMINUSER_MENU_OPER_RIGHT = "adminUserEntMenuOperRight:";


    /**
     * 存 dataDict 对象的key
     */
    public static String dataDict(String codeTable, String code) {
        return DICT + codeTable + ":" + code;
    }

    /**
     * 存 dataDict code 的key
     */
    public static String dataDictCodes(String codeTable) {
        return "dataDictCode:" + codeTable + ":codes";
    }

    /**
     * dataDict 的前缀
     */
    public static String getDict() {
        return DICT;
    }


    /**
     * @param userId
     * @param entId
     * @return
     */
    public static String getAdminUserEtpKey(String userId,String fromType, String entId) {
        return ADMINUSER_ENT + entId + ":" + userId+fromType;
    }


    /**
     * @param userId
     * @return
     */
    public static String getMemberUserKey(String userId) {
        return MEMBER_USER + ":" + userId;
    }



    /**
     * 用户菜单权限 KEY
     * @param userId
     * @return
     */
    public static String getAdminUserRightKey(String userId) {
        return ADMINUSER_MENU_OPER_RIGHT + userId;
    }

    /**
     * 用户菜单操作项权限 KEY
     * @param userId
     * @param entId
     * @return
     */
    public static String getAdminUserEntMenuOperRightKey(String userId, String fromType,String entId) {
        return ADMINUSER_MENU_OPER_RIGHT + entId + ":" + userId+fromType;
    }

}