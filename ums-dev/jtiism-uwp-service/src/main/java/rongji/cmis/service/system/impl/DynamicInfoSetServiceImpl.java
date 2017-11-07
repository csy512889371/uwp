package rongji.cmis.service.system.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rongji.cmis.annotations.JsmQueueSender;
import rongji.cmis.dao.cadreUnit.InfoCadreBasicAttributeDao;
import rongji.cmis.dao.system.DynamicInfoSetDao;
import rongji.cmis.model.sys.SysColumnShow;
import rongji.cmis.model.sys.SysColumnShow.Type;
import rongji.cmis.model.ums.CfgUmsRoleUser;
import rongji.cmis.model.ums.CfgUmsUser;
import rongji.cmis.service.cadreUnit.InfoCadreBasicAttributeService;
import rongji.cmis.service.common.DataDictService;
import rongji.cmis.service.system.*;
import rongji.framework.base.exception.ApplicationException;
import rongji.framework.base.pojo.Filter;
import rongji.framework.base.pojo.Order.Direction;
import rongji.framework.base.pojo.ZtreeDictNote;
import rongji.framework.util.*;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

@Service("dynamicInfoSetServiceImpl")
public class DynamicInfoSetServiceImpl implements DynamicInfoSetService {

    private static final Logger logger = LoggerFactory.getLogger(DynamicInfoSetServiceImpl.class);

    @Resource(name = "dynamicInfoSetDaoImpl")
    DynamicInfoSetDao dynamicInfoSetDao;

    @Resource(name = "infoCadreBasicAttributeDaoImpl")
    InfoCadreBasicAttributeDao infoCadreBasicAttributeDao;

    @Resource(name = "infoCadreBasicAttributeServiceImpl")
    InfoCadreBasicAttributeService infoCadreBasicAttributeService;

    @Resource(name = "cfgUmsCodeAttributeServiceImpl")
    CfgUmsCodeAttributeService cfgUmsCodeAttributeService;

    @Resource(name = "dataDictServiceImpl")
    DataDictService dataDictService;
    /**
     * 用户service
     */
    @Resource(name = "userServiceImpl")
    private UserService userService;

    @Resource(name = "roleUserServiceImpl")
    RoleUserService roleUserService;



    @Override
    public void createInfoSetFKA01(String tableName) {
        try {
            dynamicInfoSetDao.createInfoSetFKA01(tableName);
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException("create infoset error!", e);
        }

    }

    @Override
    public void addColumn(SysColumnShow column) {
        try {
            Map<String, Object> parameters = new LinkedHashMap<String, Object>();
            parameters.put("infoSet", column.getInfoSet());
            if (Type.dataList.equals(column.getType())) {
                parameters.put("columnName", column.getPropertyCode());
            } else {
                parameters.put("columnName", column.getPropertyName());
            }
            if (StringUtil.isNotEmpty(column.getLengthLimit())) {
                parameters.put("lengthLimit", column.getLengthLimit());
            } else {
                parameters.put("lengthLimit", 0);
            }
            if (column.getType().equals(Type.datetemp)) {
                parameters.put("paramType", "datetime");
            } else if (column.getType().equals(Type.multiFile) || column.getType().equals(Type.textarea)) {
                parameters.put("paramType", "text");
            } else {
                parameters.put("paramType", "varchar");
            }
            String alterSql = infoCadreBasicAttributeDao.findQueryByNamed("infoSet.addColumn", parameters);
            dynamicInfoSetDao.addColumn(alterSql);
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new ApplicationException("add column error!", e);
        }
    }

    @Override
    public Map<String, Object> findToMap(String infoSet, String id) {
        ParamRequest paramRequest = new ParamRequest();
        paramRequest.addFilter(Filter.eq("infoSet", infoSet));
        paramRequest.setOrderProperty("seq");
        paramRequest.setOrderDirection(Direction.asc);
        List<SysColumnShow> columns = infoCadreBasicAttributeDao.findAllByParamRequest(paramRequest);
        String selectV = " " + getKeyName(infoSet);
        if (!infoSet.equals("SHJT_ENT_INF")) {
            selectV += ("," + Constant.getFkkey());
        }
        for (SysColumnShow column : columns) {
            if (Type.dataList.equals(column.getType()) || Type.select.equals(column.getType())) {
                selectV += "," + column.getPropertyCode();
            } else {
                selectV += "," + column.getPropertyName();
            }
        }
        return dynamicInfoSetDao.findToMap(infoSet, selectV, id);
    }

    @Override
    public Page<Object> findAllForPage(String infoSet, ParamRequest paramRequest) {
        return dynamicInfoSetDao.findAllForPage(infoSet, paramRequest);
    }

    @Override
    public String save(String infoSet, String enterpriseId, Map<String, String[]> parameterMap) {

        // 1.getColumnInfo from infoSet
        List<SysColumnShow> columnList = infoCadreBasicAttributeService.findExtraEnabledAttrListByInfoSet(infoSet);
        if (columnList == null || columnList.isEmpty()) {
            return "";
        }
        Map<String, String> columnMap = new LinkedHashMap<String, String>();
        String id = UUID.randomUUID().toString();
        columnMap.put(getKeyName(infoSet), id);
        columnMap.put(Constant.getFkkey(), enterpriseId);
        /*if(infoSet.equals("SHJT_ENT_SSM")){
            columnMap.put("ENTSSM016", "1");
		}*/
        for (SysColumnShow column : columnList) {
            String pName = "";
            if (Type.dataList.equals(column.getType()) || Type.select.equals(column.getType())) {
                pName = column.getPropertyCode();
            } else {
                pName = column.getPropertyName();
            }


            String[] strings = parameterMap.get(pName);
            String columnValue = (strings == null || strings.length == 0) ? null : strings[0];
//			String columnValue = request.getParameter(pName);
            if (columnValue != null) {
                if (Type.datetemp.equals(column.getType()) && columnValue != "" && columnValue.length() <= 4) {
                    columnValue += "-01-01";
                }
                columnMap.put(pName, columnValue);
            }
            if (Type.multiFile.equals(column.getType())) {
                String uploadFileStr = parameterMap.get("uploadFileStr")[0];
                String deleteFileStr = parameterMap.get("deleteFileStr")[0];
                String fileStr = parameterMap.get(pName)[0];
                if (StringUtil.isNotEmpty(fileStr) && StringUtil.isNotEmpty(deleteFileStr)) {
                    String[] fileArr = fileStr.split(",");
                    List<String> fileList = Arrays.asList(fileArr);
                    List<String> deleteList = Arrays.asList(deleteFileStr.split(","));
                    List<String> fileList2 = new ArrayList<>();
                    for (String file : fileArr) {
                        if (!deleteList.contains(file)) {
                            fileList2.add(file);
                        }
                    }
                    fileStr = "";
                    int size = fileList2.size();
                    int i = 0;
                    for (String fileName : fileList2) {
                        fileStr += fileName;
                        if (i++ < size) fileStr += ",";
                    }
                    columnMap.put(pName, fileStr);
                }
                if (StringUtil.isNotEmpty(uploadFileStr)) {
                    columnMap.put(pName, uploadFileStr);
                }
            }
        }
        Map<String, Object> parameters = new LinkedHashMap<String, Object>();
        parameters.put("infoSet", infoSet);
        parameters.put("columnMap", columnMap);

        String insertSql = infoCadreBasicAttributeDao.findQueryByNamed("infoSet.insertData", parameters);
        dynamicInfoSetDao.save(insertSql);

        //管理员权限
        Boolean flag = false;
        CfgUmsUser cfgUmsUser = userService.getCurrent();
        if (cfgUmsUser != null) {
            List<CfgUmsRoleUser> roleUserList = roleUserService.findRoleByUserId(cfgUmsUser.getId());
            for (CfgUmsRoleUser roleUser : roleUserList) {
                if (roleUser.getRoleid().equals("1")) {
                    flag = true;
                }
            }
        }
        if (flag && infoSet.equals("SHJT_ENT_SSM")) {
            Map<String, String> columnupDateMap = new LinkedHashMap<String, String>();
            columnupDateMap.put("ENTSSM016", "1");
            Map<String, Object> params = new LinkedHashMap<String, Object>();
            params.put("infoSet", infoSet);
            params.put("columnMap", columnupDateMap);
            params.put("keyName", getKeyName(infoSet));
            params.put("keyValue", id);
            String updateSql = infoCadreBasicAttributeDao.findQueryByNamed("infoSet.updateData", params);
            Integer tg = dynamicInfoSetDao.update(updateSql);
        }

        return getKeyName(infoSet) + "," + id;

    }


    @Override
    public List<Map<String, String>> findAllToListMap(String infoSet, String fkVal, String type) throws ParseException {
        ParamRequest paramRequest = new ParamRequest();
        paramRequest.addFilter(Filter.eq("infoSet", infoSet));
        paramRequest.setOrderProperty("seq");
        paramRequest.setOrderDirection(Direction.asc);
        List<SysColumnShow> columns = infoCadreBasicAttributeDao.findAllByParamRequest(paramRequest);
        String selectV = " " + getKeyName(infoSet);
        if (!infoSet.equals("SHJT_ENT_INF")) {
            selectV += ("," + Constant.getFkkey());
        }
        for (SysColumnShow column : columns) {
            if (Type.dataList.equals(column.getType()) || Type.select.equals(column.getType())) {
                selectV += "," + column.getPropertyCode();
            } else {
                selectV += "," + column.getPropertyName();
            }
        }
        List<Map<String, Object>> returnDataList = dynamicInfoSetDao.findAllToListMap(infoSet, fkVal, type, selectV);
        returnDataList.forEach(inforset -> {
            Map<String, Object> newColumn = new HashMap<>();
            inforset.forEach((k, v) -> {
                columns.forEach(c -> {
                    if (c.getPropertyCode().equals(k) && c.getType().equals(Type.dataList)) {
                        if (v != null && v != "") {
                            String newColumnName = k + "_desc";
                            ZtreeDictNote note = dataDictService.findByFieldInfo(infoSet, c.getPropertyCode(), v.toString());
                            String newColumnValue = note.getCodeName();
                            newColumn.put(newColumnName, newColumnValue);
                        }
                    }
                    if (c.getPropertyName().equals(k) && c.getType().equals(Type.datetemp)) {
                        Date date = new Date();
                        try {
                            date = (Timestamp) v;
                            newColumn.put(k, date);
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                });
            });
            newColumn.forEach((key, val) -> {
                inforset.put(key, val);
            });
        });

        List<Map<String, String>> resultData = ListUtil.ListChange(returnDataList);
        return resultData;
    }

    @Override
    public Boolean delete(String infoSet, String id, String enterpriseId) {
        Map<String, Object> parameters = new LinkedHashMap<String, Object>();
        parameters.put("infoSet", infoSet);
        parameters.put("keyName", getKeyName(infoSet));
        parameters.put("keyValue", id);
        String delSql = infoCadreBasicAttributeDao.findQueryByNamed("infoSet.deleteData", parameters);
        Integer size = dynamicInfoSetDao.update(delSql);
        if (size == 1) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteNotIn(String infoSet, String ids, String enterpriseId) {
        Map<String, Object> parameters = new LinkedHashMap<String, Object>();
        parameters.put("infoSet", infoSet);
        parameters.put("keyName", getKeyName(infoSet));
        parameters.put("keyValue", ids);
        parameters.put("foreignKey", Constant.getFkkey());
        parameters.put("foreignKeyValue", enterpriseId);
        String delSql = "";
        if (StringUtil.isNotEmpty(ids)) {
            delSql = infoCadreBasicAttributeDao.findQueryByNamed("infoSet.deleteDataNotIn", parameters);
        } else {
            delSql = infoCadreBasicAttributeDao.findQueryByNamed("infoSet.deleteDataAllByEntId", parameters);
        }
        Integer size = dynamicInfoSetDao.update(delSql);
        if (size == 1) {
            return true;
        }
        return false;
    }

    @Override
    @JsmQueueSender(infoSet = "#infoSet", entId = "#enterpriseId")
    public void deleteByFkEtp(String infoSet, String enterpriseId) {
        Map<String, Object> parameters = new LinkedHashMap<String, Object>();
        parameters.put("infoSet", infoSet);
        String fkkey = Constant.getFkkey();
        if (StringUtil.isNotEmpty(fkkey)) {
            //获取该表所有的字段
            List<String> keyList = new ArrayList<>();
            List<SysColumnShow> attrs = infoCadreBasicAttributeService.findDynEnabledAttrListByInfoSet(infoSet);
            for (SysColumnShow attr : attrs) {
                keyList.add(attr.getPropertyName());
            }
            //若包含外键则根据外键删除
            if (keyList.contains(fkkey)) {
                parameters.put("keyName", fkkey);
                parameters.put("keyValue", enterpriseId);
                String delSql = infoCadreBasicAttributeDao.findQueryByNamed("infoSet.deleteData", parameters);
                dynamicInfoSetDao.update(delSql);
            }
        }
    }

    @Override
    public Boolean update(String infoSet, String enterpriseId, String keyValue, Map<String, String[]> parameterMap) {
        List<SysColumnShow> columnList = infoCadreBasicAttributeService.findExtraEnabledAttrListByInfoSet(infoSet);
        if (columnList == null || columnList.isEmpty()) {
            return false;
        }
        Map<String, String> columnMap = new LinkedHashMap<String, String>();
        for (SysColumnShow column : columnList) {
            String pName = "";
            if (Type.dataList.equals(column.getType()) || Type.select.equals(column.getType())) {
                pName = column.getPropertyCode();
            } else {
                pName = column.getPropertyName();
            }
            String[] strings = parameterMap.get(pName);
            String columnValue = (strings == null || strings.length == 0) ? null : strings[0];
            if (columnValue != null) {
                if (Type.datetemp.equals(column.getType()) && columnValue != "" && columnValue.length() <= 4) {
                    columnValue += "-01-01";
                }
                columnMap.put(pName, columnValue);
            }
            if (Type.multiFile.equals(column.getType())) {
                String uploadFileStr = parameterMap.get("uploadFileStr")[0];
                String deleteFileStr = parameterMap.get("deleteFileStr")[0];
                String fileStr = parameterMap.get(pName)[0];
                if (StringUtil.isNotEmpty(fileStr) && StringUtil.isNotEmpty(deleteFileStr)) {
                    String[] fileArr = fileStr.split(",");
                    List<String> fileList = Arrays.asList(fileArr);
                    List<String> deleteList = Arrays.asList(deleteFileStr.split(","));
                    List<String> fileList2 = new ArrayList<>();
                    for (String file : fileArr) {
                        if (!deleteList.contains(file)) {
                            fileList2.add(file);
                        }
                    }
                    fileStr = "";
                    int size = fileList2.size();
                    int i = 0;
                    for (String fileName : fileList2) {
                        fileStr += fileName;
                        if (i++ < size) fileStr += ",";
                    }
                    columnMap.put(pName, fileStr);
                }
                if (StringUtil.isNotEmpty(uploadFileStr)) {
                    columnMap.put(pName, uploadFileStr);
                }
            }
        }
        Map<String, Object> parameters = new LinkedHashMap<String, Object>();
        parameters.put("infoSet", infoSet);
        parameters.put("columnMap", columnMap);
        parameters.put("keyName", getKeyName(infoSet));
        parameters.put("keyValue", keyValue);
        String insertSql = infoCadreBasicAttributeDao.findQueryByNamed("infoSet.updateData", parameters);
        Integer tg = dynamicInfoSetDao.update(insertSql);
        if (tg >= 1) {
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> findSingleMap(String enterpriseId, String infoSet) {
        if (StringUtils.isEmpty(infoSet) || StringUtils.isEmpty(enterpriseId)) {
            return new HashMap<String, Object>();
        }


        ParamRequest paramRequest = new ParamRequest();
        paramRequest.addFilter(Filter.eq("infoSet", infoSet));
        paramRequest.setOrderProperty("seq");
        paramRequest.setOrderDirection(Direction.asc);
        List<SysColumnShow> colShowInfoList = infoCadreBasicAttributeDao.findAllByParamRequest(paramRequest);


        String selectV = " " + getKeyName(infoSet);
        if (!infoSet.equals("SHJT_ENT_INF")) {
            selectV += ("," + Constant.getFkkey());
        }
        for (SysColumnShow column : colShowInfoList) {
            if (Type.dataList.equals(column.getType()) || Type.select.equals(column.getType())) {
                selectV += "," + column.getPropertyCode();
            } else {
                selectV += "," + column.getPropertyName();
            }
        }
        List<Map<String, Object>> dataList = dynamicInfoSetDao.findAllToListMap(infoSet, enterpriseId, null, selectV);

        dataList.forEach(data -> {
            Map<String, Object> newColumn = new HashMap<>();
            data.forEach((colName, colVal) -> {
                colShowInfoList.forEach(colShow -> {
                    if (colShow.getPropertyCode().equals(colName) && colShow.getType().equals(Type.dataList)) {
                        if (colVal != null && colVal != "") {
                            String newColumnName = colName + "_desc";
                            ZtreeDictNote note = dataDictService.findByFieldInfo(infoSet, colShow.getPropertyCode(), colVal.toString());
                            String newColumnValue = note.getCodeName();
                            newColumn.put(newColumnName, newColumnValue);
                        }
                    }
                    if (colShow.getPropertyName().equals(colName) && colShow.getType().equals(Type.datetemp)) {
                        Date date = new Date();
                        try {
                            date = (Timestamp) colVal;
                            newColumn.put(colName, date);
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                });
            });
            newColumn.forEach((k, v) -> {
                data.put(k, v);
            });
        });


        //List<Map<String, String>> resultData = ListUtil.ListChange(dataList);
        if (dataList.size() > 0) {
            return dataList.get(0);
        }
        return new HashMap<String, Object>();
    }


    /*
     * 主键名称
     */
    @Override
    public String getKeyName(String infoSet) {
        return Constant.getKeyName(infoSet);
    }

    @Override
    public List<Map<String, Object>> getRecentData(String infoSet, String columnOne, String columnTwo, String entId) {
        return dynamicInfoSetDao.getRecentData(infoSet, columnOne, columnTwo, entId);
    }

    @Override
    public void saveByMap(String infoSet, String enterpriseId, Map<String, Object> paramMap) {
        List<SysColumnShow> columnList = infoCadreBasicAttributeService.findExtraEnabledAttrListByInfoSet(infoSet);
        if (columnList == null || columnList.isEmpty()) {
            return;
        }
        Map<String, String> columnMap = new LinkedHashMap<String, String>();
        String id = UUID.randomUUID().toString();
        columnMap.put(getKeyName(infoSet), id);
        columnMap.put(Constant.getFkkey(), enterpriseId);
        for (SysColumnShow column : columnList) {
            String pName = "";
            if (SysColumnShow.Type.dataList.equals(column.getType()) || SysColumnShow.Type.select.equals(column.getType())) {
                pName = column.getPropertyCode();
            } else {
                pName = column.getPropertyName();
            }
            Object vlaue = paramMap.get(pName);
            String columnValue = "";
            if (vlaue == null) {
                columnValue = null;
            } else {
                columnValue = vlaue.toString();
            }
            if (columnValue != null) {
                if (SysColumnShow.Type.datetemp.equals(column.getType()) && columnValue != "" && columnValue.length() <= 4) {
                    columnValue += "-01-01";
                }
                columnMap.put(pName, columnValue);
            }
        }
        Map<String, Object> parameters = new LinkedHashMap<String, Object>();
        parameters.put("infoSet", infoSet);
        parameters.put("columnMap", columnMap);

        String insertSql = infoCadreBasicAttributeDao.findQueryByNamed("infoSet.insertData", parameters);
        dynamicInfoSetDao.save(insertSql);
    }

    @Override
    public Boolean updateByMap(String infoSet, String enterpriseId, String keyValue, Map<String, Object> paramMap) {
        List<SysColumnShow> columnList = infoCadreBasicAttributeService.findExtraEnabledAttrListByInfoSet(infoSet);
        if (columnList == null || columnList.isEmpty()) {
            return false;
        }
        Map<String, String> columnMap = new LinkedHashMap<String, String>();
        for (SysColumnShow column : columnList) {
            String pName = "";
            if (Type.dataList.equals(column.getType()) || Type.select.equals(column.getType())) {
                pName = column.getPropertyCode();
            } else {
                pName = column.getPropertyName();
            }
            Object vlaue = paramMap.get(pName);
            String columnValue = "";
            if (vlaue == null) {
                columnValue = null;
            } else {
                columnValue = vlaue.toString();
            }
            if (Type.datetemp.equals(column.getType()) && columnValue != "" && columnValue.length() <= 4) {
                columnValue += "-01-01";
            }
            columnMap.put(pName, columnValue);
        }
        Map<String, Object> parameters = new LinkedHashMap<String, Object>();
        parameters.put("infoSet", infoSet);
        parameters.put("columnMap", columnMap);
        parameters.put("keyName", getKeyName(infoSet));
        parameters.put("keyValue", keyValue);
        String insertSql = infoCadreBasicAttributeDao.findQueryByNamed("infoSet.updateData", parameters);
        Integer tg = dynamicInfoSetDao.update(insertSql);
        if (tg >= 1) {
            return true;
        }
        return false;
    }


}
