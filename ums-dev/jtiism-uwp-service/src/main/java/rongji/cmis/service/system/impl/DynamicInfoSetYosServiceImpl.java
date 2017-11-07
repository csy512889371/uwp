package rongji.cmis.service.system.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rongji.cmis.service.system.DynamicInfoSetYosService;
import rongji.framework.util.DateUtil;

import java.util.*;

@Service("dynamicInfoSetYosServiceImpl")
public class DynamicInfoSetYosServiceImpl implements DynamicInfoSetYosService {

    private static final Logger logger = LoggerFactory.getLogger(DynamicInfoSetYosServiceImpl.class);

    /**
     * 去重经营情况季度表中的年份
     * @param yosInfoSetList
     * @return
     */
    @Override
    public Set<String> getDistinctYear(List<Map<String, String>> yosInfoSetList) {
        //按年度去重
        List<String> yearList = new ArrayList<>();
        for (Map map : yosInfoSetList) {
            String yearStr = null;
            Object entyos0021 = map.get("ENTYOS002");
            if (entyos0021 != null) {
                yearStr = DateUtil.formatDateToStringWithNull(DateUtil.getDate(entyos0021.toString()), "yyyy");
                yearList.add(yearStr);
            }
        }
        Set<String> yearSet = new HashSet<String>(yearList);//去重后的年度列表
        return yearSet;
    }

}