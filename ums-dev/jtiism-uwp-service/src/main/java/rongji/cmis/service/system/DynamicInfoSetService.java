package rongji.cmis.service.system;

import rongji.cmis.model.sys.SysColumnShow;
import rongji.framework.util.Page;
import rongji.framework.util.ParamRequest;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author nick
 * @version V1.0
 * @Title: DynamicInfoSetService.java
 * @Package rongji.cmis.service.system
 * @Description: 动态信息集
 * @date 2016年8月8日 下午12:00:17
 */
public interface DynamicInfoSetService {

    /**
     * 创建动态信息集
     */
    void createInfoSetFKA01(String tableName);

    /**
     * 添加字段
     */
    void addColumn(SysColumnShow column);

    /**
     * 查找对象
     */
    Map<String, Object> findToMap(String infoSet, String id);

    String save(String infoSet, String enterpriseId, Map<String, String[]> parameterMap);

    /**
     * 查询全部数据
     *
     * @param fkVal
     * @throws ParseException
     */
    List<Map<String, String>> findAllToListMap(String infoSet, String fkVal, String type) throws ParseException;

    /**
     * 分页查询数据
     */
    Page<Object> findAllForPage(String infoSet, ParamRequest paramRequest);


    Boolean delete(String infoSet, String id,String enterpriseId);

    void deleteByFkEtp(String infoSet, String enterpriseId);

	Boolean deleteNotIn(String infoSet, String ids,String enterpriseId);

    Boolean update(String infoSet, String a0000, String keyValue, Map<String, String[]> parameterMap);

    Map<String, Object> findSingleMap(String a0000, String infoSet);

    /**
     * 获取动态表的主键名称
     *
     * @param infoSet 表名称
     * @return 主键名称
     */
    String getKeyName(String infoSet);

    List<Map<String, Object>> getRecentData(String infoSet, String columnOne, String columnTwo, String entId);

	void saveByMap(String infoSet,String enterpriseId,Map<String ,Object> paramMap);

	Boolean updateByMap(String infoSet, String enterpriseId, String keyValue, Map<String ,Object> paramMap);


}
