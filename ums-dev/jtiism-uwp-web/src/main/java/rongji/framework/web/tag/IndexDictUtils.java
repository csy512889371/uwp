package rongji.framework.web.tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rongji.cmis.service.common.DataDictService;
import rongji.framework.base.pojo.ZtreeDictNote;
import rongji.framework.util.ParamRequest;
import rongji.framework.util.SpringUtils;
import rongji.framework.util.StringUtil;

import java.util.Map;

/**
 * Utils - 字典 名称
 *
 * @version 1.0
 */
public final class IndexDictUtils {

    private static final Logger logger = LoggerFactory.getLogger(IndexDictUtils.class);

    /**
     * 不可实例化
     */
    private IndexDictUtils() {
    }

    public static String getCodeName(String srcTable, String srcField, String code) {
        String codeName = "";
        try {
            DataDictService dataDictService = (DataDictService) SpringUtils.getBean("dataDictServiceImpl");
            if (dataDictService != null) {
                ZtreeDictNote dataDict = dataDictService.findByFieldInfo(srcTable, srcField, code);
                if (dataDict != null) {
                    codeName = dataDict.getCodeAbr1();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return codeName;
    }


    /**
     * 获取逗号分隔的字典名称
     *
     * @param srcTable
     * @param srcField
     * @param code
     * @return
     */
    public static String getSplitCodeName(String srcTable, String srcField, String code) {
        StringBuilder codeName = new StringBuilder();
        try {
            String[] arr = code.split(",");
            for (int i = 0; i < arr.length; i++) {
                String _code = arr[i];
                DataDictService dataDictService = (DataDictService) SpringUtils.getBean("dataDictServiceImpl");
                if (dataDictService != null) {
                    ZtreeDictNote dataDict = dataDictService.findByFieldInfo(srcTable, srcField, _code);
                    if (dataDict != null) {
                        codeName.append(dataDict.getCodeAbr1());
                        if (i < arr.length - 1) {
                            codeName.append(",");
                        }else{
                            codeName.append(";");
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return codeName.toString();
    }


    public static String getFullCodeName(String srcTable, String srcField, String code) {
        String codeName = "";
        try {
            DataDictService dataDictService = (DataDictService) SpringUtils.getBean("dataDictServiceImpl");
            if (dataDictService != null) {
                ZtreeDictNote dataDict = dataDictService.findByFieldInfo(srcTable, srcField, code);
                if (dataDict != null) {
                    codeName = dataDict.getCodeName();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return codeName;
    }

    public static String getCsmsUnitCodeName(String code) {
        String codeName = code;
        try {
            DataDictService dataDictService = (DataDictService) SpringUtils.getBean("dataDictServiceImpl");
            if (dataDictService != null) {
                Map<String, Object> map = dataDictService.findUnitsByCode(code, new ParamRequest());
                if (null != map) {
                    String name = (String) map.get("name");
                    if (StringUtil.isNotEmpty(name)) {
                        codeName = name;
                    }
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return codeName;
    }

}