package rongji.cmis.service.system.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rongji.cmis.service.system.DynamicInfoSetAosService;
import rongji.cmis.service.system.DynamicInfoSetService;
import rongji.cmis.service.system.DynamicInfoSetYosService;
import rongji.framework.util.DateUtil;
import rongji.framework.util.StatisticUtil;
import rongji.framework.util.StringUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service("dynamicInfoSetAosServiceImpl")
public class DynamicInfoSetAosServiceImpl implements DynamicInfoSetAosService {

    private static final Logger logger = LoggerFactory.getLogger(DynamicInfoSetAosServiceImpl.class);
    @Resource(name = "dynamicInfoSetServiceImpl")
    private
    DynamicInfoSetService dynamicInfoSetService;


    @Resource(name = "dynamicInfoSetYosServiceImpl")
    private
    DynamicInfoSetYosService dynamicInfoSetYosService;


    @Override
    public void genAos(String enterpriseId) {
        //企业的  经营记录列表
        List<Map<String, String>> yosInfoSetList = null;
        try {
            yosInfoSetList = dynamicInfoSetService.findAllToListMap("SHJT_ENT_YOS", enterpriseId, null);
            List<StatisticUtil.YearSeason> yearSeasonList004 = new ArrayList<>();
            List<StatisticUtil.YearSeason> yearSeasonList005 = new ArrayList<>();
            List<StatisticUtil.YearSeason> yearSeasonList006 = new ArrayList<>();
            List<StatisticUtil.YearSeason> yearSeasonList007 = new ArrayList<>();
            List<StatisticUtil.YearSeason> yearSeasonList014 = new ArrayList<>();//固定资产投资额（万元）
            List<StatisticUtil.YearSeason> yearSeasonList017 = new ArrayList<>();//增值税（万元）
            List<StatisticUtil.YearSeason> yearSeasonList018 = new ArrayList<>();//企业所得税（万元）
            List<StatisticUtil.YearSeason> yearSeasonList019 = new ArrayList<>();//个人所得税（万元）


            //构建统计用的list
            for (Map map : yosInfoSetList) {
                //年份 季度 不能为空
                Object entyos003 = map.get("ENTYOS003");
                Object entyos002 = map.get("ENTYOS002");
                if (entyos003 != null && entyos002 != null) {
                    Integer _season = Integer.valueOf(entyos003.toString());//季度
                    Integer _year = Integer.valueOf(DateUtil.formatDateToStringWithNull(DateUtil.getDate(entyos002.toString()), "yyyy"));
                    Object entyos004 = map.get("ENTYOS004");
                    Object entyos005 = map.get("ENTYOS005");
                    Object entyos006 = map.get("ENTYOS006");
                    Object entyos007 = map.get("ENTYOS007");//产值
                    Object entyos014 = map.get("ENTYOS014");//增值税（万元）
                    Object entyos017 = map.get("ENTYOS017");//增值税（万元）
                    Object entyos018 = map.get("ENTYOS018");//企业所得税（万元）
                    Object entyos019 = map.get("ENTYOS019");//个人所得税（万元）
                    BigDecimal ENTYOS004 = BigDecimal.valueOf(Float.valueOf(entyos004 == null ? "0" : entyos004.toString()));//销售（营业）收入
                    BigDecimal ENTYOS005 = BigDecimal.valueOf(Float.valueOf(entyos005 == null ? "0" : entyos005.toString()));//利润总额
                    BigDecimal ENTYOS006 = BigDecimal.valueOf(Float.valueOf(entyos006 == null ? "0" : entyos006.toString()));//税收总额
                    BigDecimal ENTYOS007 = BigDecimal.valueOf(Float.valueOf(entyos007 == null ? "0" : entyos007.toString()));//税收总额
                    BigDecimal ENTYOS014 = BigDecimal.valueOf(Float.valueOf(entyos014 == null ? "0" : entyos014.toString()));//税收总额
                    BigDecimal ENTYOS017 = BigDecimal.valueOf(Float.valueOf(entyos017 == null ? "0" : entyos017.toString()));//税收总额
                    BigDecimal ENTYOS018 = BigDecimal.valueOf(Float.valueOf(entyos018 == null ? "0" : entyos018.toString()));//税收总额
                    BigDecimal ENTYOS019 = BigDecimal.valueOf(Float.valueOf(entyos019 == null ? "0" : entyos019.toString()));//税收总额
                    yearSeasonList004.add(new StatisticUtil.YearSeason(_year, _season, ENTYOS004));
                    yearSeasonList005.add(new StatisticUtil.YearSeason(_year, _season, ENTYOS005));
                    yearSeasonList006.add(new StatisticUtil.YearSeason(_year, _season, ENTYOS006));
                    yearSeasonList007.add(new StatisticUtil.YearSeason(_year, _season, ENTYOS007));
                    yearSeasonList014.add(new StatisticUtil.YearSeason(_year, _season, ENTYOS014));
                    yearSeasonList017.add(new StatisticUtil.YearSeason(_year, _season, ENTYOS017));
                    yearSeasonList018.add(new StatisticUtil.YearSeason(_year, _season, ENTYOS018));
                    yearSeasonList019.add(new StatisticUtil.YearSeason(_year, _season, ENTYOS019));
                }
            }
            //按年度去重
            Set<String> yearSet = dynamicInfoSetYosService.getDistinctYear(yosInfoSetList);//去重后的年度列表
            //生成各年份的报表
            List<Map<String, Object>> resultList = new ArrayList<>();
            for (String countYear : yearSet) {
                //销售（营业）收入（万元）
                BigDecimal ENTAOS004 = StatisticUtil.getYearSum(Integer.valueOf(countYear), yearSeasonList004);
                //利润总额（万元）
                BigDecimal ENTAOS005 = StatisticUtil.getYearSum(Integer.valueOf(countYear), yearSeasonList005);
                //税收总额（万元）
                BigDecimal ENTAOS006 = StatisticUtil.getYearSum(Integer.valueOf(countYear), yearSeasonList006);
                BigDecimal ENTAOS007 = StatisticUtil.getYearSum(Integer.valueOf(countYear), yearSeasonList007);
                BigDecimal ENTAOS014 = StatisticUtil.getYearSum(Integer.valueOf(countYear), yearSeasonList014);
                BigDecimal ENTAOS017 = StatisticUtil.getYearSum(Integer.valueOf(countYear), yearSeasonList017);
                BigDecimal ENTAOS018 = StatisticUtil.getYearSum(Integer.valueOf(countYear), yearSeasonList018);
                BigDecimal ENTAOS019 = StatisticUtil.getYearSum(Integer.valueOf(countYear), yearSeasonList019);
                Map<String, Object> aosDynaMap = new HashMap<String, Object>();
                aosDynaMap.put("ENTAOS001", countYear);
                aosDynaMap.put("ENTAOS004", ENTAOS004);
                aosDynaMap.put("ENTAOS005", ENTAOS005);
                aosDynaMap.put("ENTAOS006", ENTAOS006);
                aosDynaMap.put("ENTAOS007", ENTAOS007);
                aosDynaMap.put("ENTAOS014", ENTAOS014);
                aosDynaMap.put("ENTAOS017", ENTAOS017);
                aosDynaMap.put("ENTAOS018", ENTAOS018);
                aosDynaMap.put("ENTAOS019", ENTAOS019);
                //构造更新年份经营信息集MAP
                resultList.add(aosDynaMap);
            }

            //查询年度表 已存在该年度记录？ 更新：新增
            for (Map<String, Object> resultMap : resultList) {
                String ENTAOS000 = "";
                boolean updateFlag = false;
                String yearStr = (String) resultMap.get("ENTAOS001");
                List<Map<String, String>> aosInfoSetList = dynamicInfoSetService.findAllToListMap("SHJT_ENT_AOS", enterpriseId, null);
                for (Map<String, String> aos : aosInfoSetList) {
                    String entaos001 = aos.get("ENTAOS001");
                    String _yearStr = StringUtil.isNotEmpty(entaos001) && entaos001.length() >= 4 ? entaos001.substring(0, 4) : "";
                    ENTAOS000 = aos.get("ENTAOS000");
                    if (yearStr.equals(_yearStr)) {
                        updateFlag = true;
                        resultMap.put("ENTAOS000", ENTAOS000);
                        break;
                    }
                }
                logger.info("-------updateFlag" + updateFlag);
                if (updateFlag) {
                    dynamicInfoSetService.updateByMap("SHJT_ENT_AOS", enterpriseId, ENTAOS000, resultMap);
                } else {
                    dynamicInfoSetService.saveByMap("SHJT_ENT_AOS", enterpriseId, resultMap);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}