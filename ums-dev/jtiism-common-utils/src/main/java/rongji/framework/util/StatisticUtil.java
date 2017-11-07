package rongji.framework.util;

import java.math.BigDecimal;
import java.util.List;

/**
 * 统计工具类
 */
public class StatisticUtil {

    private StatisticUtil() {
    }


    /**
     * 同环比区别
     * 本期统计数据与上期比较，例如2014年7月份与2014年6月份相比较，叫环比。
     * 与历史同时期比较，例如2014年7月份与2013年7月份相比，叫同比。
     * 1）环比增长率=（本期数－上期数）/上期数×100%。
     * 2）同比增长率=（本期数－同期数）/同期数×100%。
     */


    /**
     * 获取同比率（季度范围）
     *
     * @param countYear
     * @param seasonFrom
     * @param seasonTo
     * @param list
     * @return
     */
    public static BigDecimal getYearOnYearRate(Integer countYear, Integer seasonFrom, Integer seasonTo, List<YearSeason> list) {
        BigDecimal rate = BigDecimal.valueOf(0);
        BigDecimal countYearSum = getSeasonRangeSum(countYear, seasonFrom, seasonTo, list, true);//本期数
        BigDecimal lastYearSum = getSeasonRangeSum(countYear - 1, seasonFrom, seasonTo, list, true);//同期数
        if (lastYearSum.floatValue() > 0 && countYearSum.floatValue() > 0) {
            //（本期数－同期数）/同期数×100%。
            rate = BigDecimal.valueOf((countYearSum.floatValue() - lastYearSum.floatValue()) / lastYearSum.floatValue());
        }
        return rate;
    }


    /**
     * 获取环比率（季度）
     *
     * @param countYear
     * @param countSeason
     * @param list
     * @return
     */
    public static BigDecimal getSeasonOnSeasonRate(Integer countYear, Integer countSeason, List<YearSeason> list) {
        BigDecimal rate = BigDecimal.valueOf(0);
        BigDecimal countSeanSum = getSeasonRangeSum(countYear, countSeason, countSeason, list, true);//本期数
        Integer _countYear = (countSeason - 1 <= 0) ? countYear - 1 : countYear;
        Integer lastSeason = (countSeason - 1 <= 0) ? 4 : countSeason - 1;
        BigDecimal lastSeasonSum = getSeasonRangeSum(_countYear, lastSeason, lastSeason, list, true);//同期数
        if (lastSeasonSum.floatValue() > 0 && countSeanSum.floatValue() > 0) {
            //环比增长率=（本期数－上期数）/上期数×100%
            rate = BigDecimal.valueOf((countSeanSum.floatValue() - lastSeasonSum.floatValue()) / lastSeasonSum.floatValue());
        }
        return rate;
    }


    /**
     * 统计季度范围内的值总和
     *
     * @param countYear
     * @param seasonFrom
     * @param seasonTo
     * @param list
     * @param DEAK_LOST_SEASON_DATA
     * @return
     */
    public static BigDecimal getSeasonRangeSum(Integer countYear, Integer seasonFrom, Integer seasonTo, List<YearSeason> list, boolean DEAK_LOST_SEASON_DATA) {
        float sum = 0f;
        Integer seasonNum = seasonTo - seasonFrom + 1;
        Integer count_seasonNum = 0;
        for (YearSeason ym : list) {
            Integer _season = ym.getRecordSeason();
            Integer _year = ym.getRecordYear();
            BigDecimal _value = ym.getRecordValue();
            if (_year.intValue() == countYear.intValue() && isValueBetween(_season, seasonFrom, seasonTo)) {
                sum += _value.floatValue();
                count_seasonNum++;
                if (seasonFrom == seasonTo) {//如果季度从 等于 季度到 则直接返回
                    return BigDecimal.valueOf(sum);
                }
            }
        }
        if (DEAK_LOST_SEASON_DATA) {
            return count_seasonNum == seasonNum ? BigDecimal.valueOf(sum) : BigDecimal.valueOf(0);
        } else {
            return BigDecimal.valueOf(sum);
        }
    }


    /**
     * 统计季度范围内的值总和
     *
     * @param countYear
     * @param list
     * @param DEAK_LOST_SEASON_DATA
     * @return
     */
    public static BigDecimal getYearSum(Integer countYear, List<YearSeason> list, boolean DEAK_LOST_SEASON_DATA) {
        return getSeasonRangeSum(countYear, 1, 4, list, DEAK_LOST_SEASON_DATA);
    }



    /**
     * 统计季度范围内的值总和( 处理空值为0)
     *
     * @param countYear
     * @param list
     * @return
     */
    public static BigDecimal getYearSumDealNull(Integer countYear, List<YearSeason> list) {
        return getSeasonRangeSum(countYear, 1, 4, list, true);
    }


    /**
     * 获取年度记录统计总额
     *
     * @param countYear
     * @param list
     * @return
     */
    public static BigDecimal getYearSum(Integer countYear, List<YearSeason> list) {
        float result = 0f;
        for (YearSeason ym : list) {
            Integer _year = ym.getRecordYear();
            BigDecimal _value = ym.getRecordValue();
            if (_year.intValue() == countYear.intValue()) {
                result += _value.floatValue();
            }
        }
        return BigDecimal.valueOf(result);
    }


    /**
     * 判断一个值是大小否介于两者之间
     *
     * @param value
     * @param a
     * @param b
     * @return
     */
    private static boolean isValueBetween(Integer value, Integer a, Integer b) {
        if (value.intValue() >= a.intValue() && value.intValue() <= b) {
            return true;
        }
        return false;
    }


    public static class YearSeason {
        private Integer recordYear;
        private Integer recordSeason;
        private BigDecimal recordValue;

        public YearSeason(Integer recordYear, Integer recordSeason, BigDecimal recordValue) {
            this.recordYear = recordYear;
            this.recordSeason = recordSeason;
            this.recordValue = recordValue;
        }

        Integer getRecordYear() {
            return recordYear;
        }

        public void setRecordYear(Integer recordYear) {
            this.recordYear = recordYear;
        }

        Integer getRecordSeason() {
            return recordSeason;
        }

        public void setRecordSeason(Integer recordSeason) {
            this.recordSeason = recordSeason;
        }

        BigDecimal getRecordValue() {
            return recordValue;
        }

        public void setRecordValue(BigDecimal recordValue) {
            this.recordValue = recordValue;
        }
    }


}
