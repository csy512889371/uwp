package rongji.framework.util;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期类。
 */
public class DateUtil implements Serializable {

	public static Logger logger = LoggerFactory.getLogger(DateUtil.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DateUtil() {

	}

	public static Date getDate(String dateStr) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return formatter.parse(dateStr);
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public static final Date getDate(String dateTimeString, String dateSimpleFormat) {

		if (dateTimeString == null || "".equals(dateTimeString)) {
			return null;
		}

		SimpleDateFormat formattxt = new SimpleDateFormat(dateSimpleFormat);
		Date date = null;
		try {
			date = formattxt.parse(dateTimeString);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			date = new Date();
		}
		return date;
	}

	/**
	 * 将Date转换成字符串.
	 * 
	 * @param date
	 *            时间
	 * @param pattern
	 *            日期字符串匹配模式 例:yyyy-MM-dd HH:mm:ss
	 */
	public static final String getDateString(Date date, String dateSimpleFormat) {
		SimpleDateFormat formattxt = new SimpleDateFormat(dateSimpleFormat);
		return formattxt.format(date);
	}

	/**
	 * 获取当前时间及时间
	 * 
	 * @return 当前日期
	 */
	public static String getDateTimeStr() {
		return getDateTimeStr("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获取当前日期
	 * 
	 * @param dataFormat
	 *            日期格式
	 * @return 当前日期
	 */
	public static String getCurrentDateStr(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String mDateTime = formatter.format(cal.getTime());
		return mDateTime;
	}

	/**
	 * 获取当前日期
	 * 
	 * @return 当前日期
	 */
	public static Date getCurrentDate() {
		return getCurrentDate("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获取当前日期
	 * 
	 * @param dateFormat
	 *            日期格式
	 * @return 当前日期
	 */
	public static Date getCurrentDate(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String mDateTime = formatter.format(cal.getTime());
		return getDate(mDateTime, dateFormat);
	}

	public static Timestamp getCurrentDateTime() {
		Date d = new Date();
		Timestamp t = new Timestamp(d.getTime());
		return t;
	}

	/**
	 * 获取当前日期
	 * 
	 * @return 当前日期
	 */
	public static String getCurrentDateStr() {
		return getCurrentDateStr("yyyy-MM-dd");
	}

	/**
	 * 获取当前年份
	 * 
	 * @return 当前年份
	 */
	public static String getCurrentYearStr() {
		return getCurrentDateStr("yyyy");
	}

	/**
	 * 获取当前时间及时间
	 * 
	 * @param dateFormat
	 *            日期格式
	 * @return 指定格式的日期
	 */
	public static String getDateTimeStr(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		String mDateTime = formatter.format(cal.getTime());
		return (mDateTime);
	}

	public static final Date getYesterdayDate(Date date) {
		Calendar yesterday = Calendar.getInstance();
		yesterday.setTime(date);
		yesterday.add(11, -24);
		return yesterday.getTime();
	}


	/**
	 * 获取当前年的字符串
	 * @param date
	 * @return
	 */
	public static final String getNowYearStr(Date date) {
		return getDateString(date, "yyyy");
	}

	/**
	 * 获取上一年的字符串
	 * @param date
	 * @return
	 */
	public static final String getLastYearStr(Date date) {
		return getDateString(getLastYearDate(date), "yyyy");
	}


	public static final Date getLastMonthDate(Date date) {
		Calendar lastMonth = Calendar.getInstance();
		lastMonth.setTime(date);
		lastMonth.add(2, -1);
		return lastMonth.getTime();
	}


	public static final Date getLastYearDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, -1);
		return c.getTime();
	}



	public static final Date getNextMonthString(Date date) {
		Calendar nextMonth = Calendar.getInstance();
		nextMonth.setTime(date);
		nextMonth.add(2, 1);
		return nextMonth.getTime();
	}

	public static Date getMonthFirstDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(5, calendar.getActualMinimum(5));
		return calendar.getTime();
	}

	public static String getMonthFirstDay(String dateTimeString, String dateSimpleFormat) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getDate(dateTimeString, dateSimpleFormat));
		calendar.set(5, calendar.getActualMinimum(5));
		return getDateString(calendar.getTime(), dateSimpleFormat);
	}

	public static int getLastYear(String dateTimeString, String dateSimpleFormat) {
		return Integer.parseInt(getDateString(getLastMonthDate(getDate(dateTimeString, dateSimpleFormat)), "yyyy"));
	}

	public static int getLastMonth(String dateTimeString, String dateSimpleFormat) {
		return Integer.parseInt(getDateString(getLastMonthDate(getDate(dateTimeString, dateSimpleFormat)), "MM"));
	}

	public static int betweenMonth(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);
		boolean flag = true;
		int temp = 0;
		while (flag) {
			temp++;
			c1.add(2, 1);
			if (c1.before(c2))
				flag = true;
			else
				flag = false;
		}
		return temp;
	}

	public static int betweenHour(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);
		boolean flag = true;
		int temp = 0;
		while (flag) {
			temp++;
			c1.add(10, 1);
			if (c1.before(c2))
				flag = true;
			else
				flag = false;
		}
		return temp;
	}

	/**
	 * 获取最近一周的日期
	 * 
	 * @return String
	 */
	public static String PreviousWeekToDate() {
		Calendar date = Calendar.getInstance();
		date.add(Calendar.DATE, -7);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date.getTime());
	}

	public static String getBeforeMonthToStr() {
		Calendar date = Calendar.getInstance();
		date.add(Calendar.MONTH, -1);
		date.add(Calendar.DATE, -1);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date.getTime());
	}

	public static String getBeforeMonthToNumber() {
		Calendar date = Calendar.getInstance();
		date.add(Calendar.MONTH, -1);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
		return formatter.format(date.getTime());
	}

	public static String getBeforeDateToStr() {
		Calendar date = Calendar.getInstance();
		date.add(Calendar.DATE, -1);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date.getTime());
	}

	public static String getMonth(int num) {
		Calendar date = Calendar.getInstance();
		date.add(Calendar.MONTH, num);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date.getTime());
	}

	/**
	 * 
	 * @Title: getDateStr
	 * @Description: (获取当天日期：yyyy-MM-dd)
	 * @author chenshiying
	 */
	public static String getDateStr() {
		Calendar date = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date.getTime());
	}

	/**
	 * 获取一天的开始
	 * 
	 * @Title: getDateBegin
	 * @Description:
	 * @param @param date
	 * @param @return
	 * @return Date
	 */
	public static Date getDateBegin(Date date) {
		String dateStr = getDateString(date, "yyyy-MM-dd");
		String start = dateStr + " 00:00:00";

		Date beginDate = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			beginDate = format.parse(start);
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		return beginDate;
	}

	/**
	 * 获取一天结束的日期
	 * 
	 * @Title: getDateEnd
	 * @Description:
	 * @param @param date
	 * @param @return
	 * @return Date
	 */
	public static Date getDateEnd(Date date) {
		String dateStr = getDateString(date, "yyyy-MM-dd");
		String end = dateStr + " 23:59:59";

		Date endDate = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			endDate = format.parse(end);
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		return endDate;
	}

	/**
	 * 将时间加上指定分钟数后的时间
	 * 
	 * @param date
	 * @param min
	 * @return
	 */
	public static Date getDateAdd(Date date, int min) {
		if (null == date) {
			date = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, min);
		return c.getTime();
	}

	/**
	 * 获取日期间间隔秒数 add by sumfing 2010-07-23
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Long getDateIntervalSecond(Date startDate, Date endDate) {
		Long sec = new Long("0");
		if (startDate != null) {
			if (endDate == null) {
				endDate = getCurrentDate();
			}
			sec = (Math.abs(startDate.getTime() - endDate.getTime())) / 1000;
		}
		return sec;
	}

	/**
	 * 获取日期间间隔分钟数 add by sumfing 2010-07-23
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Long getDateIntervalMinute(Date startDate, Date endDate) {
		Long sec = getDateIntervalSecond(startDate, endDate);
		Long minute = sec / 60;
		return minute;
	}

	/**
	 * 获取日期间间隔小时 add by sumfing 2010-07-23
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Long getDateIntervalHour(Date startDate, Date endDate) {
		Long sec = getDateIntervalSecond(startDate, endDate);
		Long hour = sec / 3600;
		return hour;
	}

	/**
	 * 获取日期间间隔天数 add by sumfing 2010-07-23
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Long getDateIntervalDay(Date startDate, Date endDate) {
		Long sec = getDateIntervalSecond(startDate, endDate);
		Long day = sec / (24 * 3600);
		return day;
	}

	/**
	 * 获取两个日期间隔
	 * 
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return 日期间隔
	 */
	public static long getDateInterval(String startDate, String endDate) {
		Date d1 = null;
		Date d2 = null;
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			d1 = dateFormatter.parse(startDate);
			d2 = dateFormatter.parse(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return getDateInterval(d1, d2);
	}

	/**
	 * 获取两个日期间隔（负数）
	 * 
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return 日期间隔
	 */
	public static long getDateInterval(Date startDate, Date endDate) {
		long day = (startDate.getTime() - endDate.getTime()) / (1000 * 60 * 60 * 24);
		return day;
	}

	/**
	 * 获取两个日期间隔天数（正数）
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long getDateIntervalDays(Date startDate, Date endDate) {
		Date begin, end;
		if (startDate.getTime() > endDate.getTime()) {
			begin = endDate;
			end = startDate;
		} else {
			begin = startDate;
			end = endDate;
		}
		long day = (end.getTime() - begin.getTime()) / (1000 * 60 * 60 * 24);
		return day;
	}

	/**
	 * 获取当前日期是星期几
	 * 
	 * @param value
	 *            日期
	 * @return 星期几
	 */
	public static int getWeek(Date date) {
		SimpleDateFormat weekFormat = new SimpleDateFormat("E");
		String returnValue = weekFormat.format(date);
		return Convert.toInteger(returnValue);
	}

	public static Date formatStringToDateWithNull(String string, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		if (pattern != null) {
			dateFormat.applyPattern(pattern);
		} else {
			dateFormat.applyPattern("yyyy-MM-dd");
		}
		if (StringUtil.isEmpty(string)) {
			return null;
		} else {
			try {
				return dateFormat.parse(string);
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
				return null;
			}
		}

	}

	/**
	 * 依据特定格式要求，将日期类转换日期字符串，供项目组成员调用,允许传入的时间对象为空 formatDateToString
	 * 
	 * @param date
	 *            Date 日期类
	 * @param pattern
	 *            String 格式要求，具体信息见JDK中java.text.SimpleDateFormat类文档
	 * @return String 日期字符串
	 */
	public static String formatDateToStringWithNull(Date date, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		if (pattern != null) {
			dateFormat.applyPattern(pattern);
		} else {
			dateFormat.applyPattern("yyyy-MM-dd");
		}
		if (date == null) {
			return null;
		} else {
			return dateFormat.format(date);
		}

	}

	public static String formatDateToStringWithSpace(Date date, String pattern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		if (pattern != null) {
			dateFormat.applyPattern(pattern);
		} else {
			dateFormat.applyPattern("yyyy-MM-dd");
		}
		if (date == null) {
			return "";
		} else {
			return dateFormat.format(date);
		}

	}

	/**
	 * 时间格式转换yyyy-mm-dd-->yyyymmdd
	 * 
	 * @param dateStr
	 *            String
	 * @return String
	 */
	public static String formatDateStr(String dateStr) {
		String tmp1 = dateStr.substring(0, 4);
		String tmp2 = dateStr.substring(5, 7);
		String tmp3 = dateStr.substring(8, 10);
		String tmp = tmp1 + tmp2 + tmp3;
		return tmp;
	}

	/**
	 * 检验输入是否为正确的日期格式,默认格式:yyyy-MM-dd
	 * 
	 * @param sourceDate
	 * @return
	 */
	public static boolean checkDateValidation(String sourceDate, String format) {
		if (sourceDate == null) {
			return false;
		}
		try {
			if (StringUtil.isEmpty(format)) {
				format = "yyyy-MM-dd";
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			dateFormat.setLenient(false);
			dateFormat.parse(sourceDate);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 获取某年的第一天
	 * 
	 * @param date
	 * @return Date
	 */
	public static Date getFirstDayByYear(Date date) {
		if (null == date) {
			date = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_YEAR, 1);
		return c.getTime();
	}

	/**
	 * 获取某月的第一天
	 * 
	 * @param date
	 * @return Date
	 */
	public static Date getFirstDayByMonth(Date date) {
		if (null == date) {
			date = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}

	/**
	 * 获取本年的第一天
	 */
	public static Date getFirstDayByYear() {
		return getFirstDayByYear(new Date());
	}

	/**
	 * 获取本月的第一天
	 */
	public static Date getFirstDayByMonth() {
		return getFirstDayByMonth(new Date());
	}

	/**
	 * 获取某年的最后一天
	 * 
	 * @param date
	 * @return Date
	 */
	public static Date getLastDayByYear(Date date) {
		if (null == date) {
			date = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		c.set(Calendar.YEAR, year + 1);// 将年份设置为明年
		c.set(Calendar.DAY_OF_YEAR, 0);// 前一年的最后一天
		return c.getTime();
	}

	/**
	 * 获取某月的最后一天
	 * 
	 * @param date
	 * @return Date
	 */
	public static Date getLastDayByMonth(Date date) {
		if (null == date) {
			date = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		c.set(Calendar.MONTH, month + 1);// 将月份设置为下个月
		c.set(Calendar.DAY_OF_MONTH, 0);// 上个月的最后一天
		return c.getTime();
	}

	/**
	 * 获取本年的最后一天
	 */
	public static Date getLastDayByYear() {
		return getLastDayByYear(new Date());
	}

	/**
	 * 获取本月的最后一天
	 */
	public static Date getLastDayByMonth() {
		return getLastDayByMonth(new Date());
	}

	/**
	 * 取前一天的日期
	 * 
	 * @param date
	 *            为空则默认取昨天
	 * @return Date
	 */
	public static Date getYesterday(Date date) {
		String yy = getYesterdayStr(date);
		Date yester = formatStringToDateWithNull(yy, "yyyy-MM-dd HH:mm:ss");
		return yester;
	}

	/**
	 * 取前一天的日期
	 * 
	 * @param date
	 *            为空则默认取昨天
	 * @return str yyyy-MM-dd HH:mm:ss
	 */
	public static String getYesterdayStr(Date date) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		} else {
			cal.setTime(new Date());
		}
		cal.add(Calendar.DATE, -1);
		String yy = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
		return yy;
	}

	/**
	 * 指定时间的某num天（正数为时间向前进，负数为时间向后退） num为1,是指定时间的明天，num为-1,是指定时间的昨天
	 * 
	 */
	public static Date getDateOfDateNum(Date date, int num) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		} else {
			cal.setTime(new Date());
		}
		cal.add(Calendar.DATE, num);
		return cal.getTime();

	}

	/**
	 * 指定时间的某num月的天（正数为时间向前进，负数为时间向后退） num为1,是指定时间的下一个月，num为-1,是指定时间的上一个月
	 */
	public static Date getDateOfMonthNum(Date date, int num) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		} else {
			cal.setTime(new Date());
		}
		cal.add(Calendar.MONTH, num);
		return cal.getTime();
	}

	/**
	 * 获取指定时间段内的时间区间名称
	 * 
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @author：redlwb add 2012-3-16 edit 2012-5-22
	 */
	public static String getTimeInterZone(String beginTime, String endTime) {
		StringBuffer result = new StringBuffer("");

		if (beginTime != null && !"".equals(beginTime.trim()) && endTime != null && !"".equals(endTime.trim())) {// 如果开始日期和结束日期都不为空
			int beginY, beginM, beginD, endY, endM, endD;
			String[] beginTimes = beginTime.split("-");
			String[] endTimes = endTime.split("-");
			beginY = Integer.parseInt(beginTimes[0]);
			beginM = Integer.parseInt(beginTimes[1]);
			beginD = Integer.parseInt(beginTimes[2]);
			endY = Integer.parseInt(endTimes[0]);
			endM = Integer.parseInt(endTimes[1]);
			endD = Integer.parseInt(endTimes[2]);
			if (beginY == endY) {// 年份相同
				if (beginM == endM) {// 月份相同
					if (beginM == 2) {// 2月
						if ((beginY % 4 == 0 && beginY % 100 != 0) || beginY % 400 == 0) {
							if (beginD == 1 && endD == 29) {
								result.append(beginY + "年" + beginM + "月");
							} else {
								result.append(beginY + "年" + beginM + "月" + beginD + "日" + "至" + endY + "年" + endM + "月" + endD + "日");
							}
						} else {
							if (beginD == 1 && endD == 28) {
								result.append(beginY + "年" + beginM + "月");
							} else {
								result.append(beginY + "年" + beginM + "月" + beginD + "日" + "至" + endY + "年" + endM + "月" + endD + "日");
							}
						}
					} else if (beginM == 4 || beginM == 6 || beginM == 9 || beginM == 11) {// 小月
						if (beginD == 1 && endD == 30) {
							result.append(beginY + "年" + beginM + "月");
						} else {
							result.append(beginY + "年" + beginM + "月" + beginD + "日" + "至" + endY + "年" + endM + "月" + endD + "日");
						}
					} else {// 大月
						if (beginD == 1 && endD == 31) {
							result.append(beginY + "年" + beginM + "月");
						} else {
							result.append(beginY + "年" + beginM + "月" + beginD + "日" + "至" + endY + "年" + endM + "月" + endD + "日");
						}
					}
				} else if (endM - beginM == 2) {// 季度
					if (beginM == 1 && beginD == 1 && endM == 3 && endD == 31) {
						result.append(beginY + "年" + "第一季度");
					} else if (beginM == 4 && beginD == 1 && endM == 6 && endD == 30) {
						result.append(beginY + "年" + "第二季度");
					} else if (beginM == 7 && beginD == 1 && endM == 9 && endD == 30) {
						result.append(beginY + "年" + "第三季度");
					} else if (beginM == 10 && beginD == 1 && endM == 12 && endD == 31) {
						result.append(beginY + "年" + "第四季度");
					} else {
						result.append(beginY + "年" + beginM + "月" + beginD + "日" + "至" + endY + "年" + endM + "月" + endD + "日");
					}

				} else if (endM - beginM == 5) {// 半年
					if (beginM == 1 && beginD == 1 && endM == 6 && endD == 30) {
						result.append(beginY + "上半年");
					} else if (beginM == 7 && beginD == 1 && endM == 9 && endD == 30) {
						result.append(beginY + "下半年");
					} else {
						result.append(beginY + "年" + beginM + "月" + beginD + "日" + "至" + endY + "年" + endM + "月" + endD + "日");
					}
				} else if (endM - beginM == 11) {// 全年
					if (beginM == 1 && beginD == 1 && endM == 12 && endD == 31) {
						result.append(beginY + "年");
					} else {
						result.append(beginY + "年" + beginM + "月" + beginD + "日" + "至" + endY + "年" + endM + "月" + endD + "日");
					}
				} else {// 其他区间
					result.append(beginY + "年" + beginM + "月" + beginD + "日" + "至" + endY + "年" + endM + "月" + endD + "日");
				}

			} else {// 跨年区间
				result.append(beginY + "年" + beginM + "月" + beginD + "日" + "至" + endY + "年" + endM + "月" + endD + "日");
			}

		} else if (beginTime != null && !"".equals(beginTime) && (endTime == null || "".equals(endTime))) {// 如果结束日期为空，开始日期不为空
			String[] beginTimes = beginTime.split("-");
			Date now = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			String resultNow = sf.format(now);
			String[] nowAfterSpilt = resultNow.split("-");
			if (resultNow.equals(beginTime)) {
				result.append(beginTimes[0] + "年" + beginTimes[1] + "月" + beginTimes[2] + "日");
			} else {
				result.append(beginTimes[0] + "年" + beginTimes[1] + "月" + beginTimes[2] + "日" + "至" + nowAfterSpilt[0] + "年" + nowAfterSpilt[1] + "月" + nowAfterSpilt[2] + "日");
			}
		} else if (endTime != null && !"".equals(endTime) && (beginTime == null || "".equals(beginTime))) {// 如果结束日期不为空，开始日期为空
			String[] endTimes = endTime.split("-");
			result.append(endTimes[0] + "年" + endTimes[1] + "月" + endTimes[2] + "日" + "以前");
		}
		return result.toString();
	}

	/**
	 * 获取指定区间内的所有时间 格式为yyyy-MM-dd
	 * 
	 * @param begTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	public static List<String> getAllDateInZone(String begTime, String endTime) throws Exception {
		List<String> result = new ArrayList<String>();
		boolean isEqual = false;
		// 如果不符合格式，抛出异常
		if ((!StringUtil.isEmpty(begTime) && begTime.length() != 10) || (!StringUtil.isEmpty(endTime) && endTime.length() != 10)) {
			System.out.println("[wsError]无效的开始时间或者结束时间");
			throw new IllegalArgumentException("[wsError]无效的开始时间或者结束时间");
		}
		int begTimeTemp = Integer.parseInt(begTime.replaceAll("-", ""));
		int endTimeTemp = Integer.parseInt(endTime.replaceAll("-", ""));
		if (begTimeTemp > endTimeTemp) {
			throw new IllegalArgumentException("[wsError]开始时间大于结束时间");
		} else if (begTimeTemp == endTimeTemp) {
			isEqual = true;
		}
		int begYear = Integer.parseInt(begTime.substring(0, 4));
		int begMonth = Integer.parseInt(begTime.substring(5, 7));
		int begDay = Integer.parseInt(begTime.substring(8, 10));

		int endYear = Integer.parseInt(endTime.substring(0, 4));
		int endMonth = Integer.parseInt(endTime.substring(5, 7));
		int endDay = Integer.parseInt(endTime.substring(8, 10));
		// 添加开始时间
		result.add(begYear + "//" + begMonth + "//" + begDay);
		if (isEqual) {
			return result;
		}

		boolean flag = true;
		while (flag) {
			if (begYear == endYear && begMonth == endMonth && begDay == endDay) {
				flag = false;
				continue;
			}
			if (begDay == 31) {
				if (begMonth == 12) {
					begYear = begYear + 1;
					begMonth = 1;
					begDay = 1;
				} else {
					begMonth = begMonth + 1;
					begDay = 1;
				}

			} else {
				begDay = begDay + 1;
			}
			result.add(begYear + "-" + begMonth + "-" + begDay);
		}
		// 添加结束时间
		return result;
	}

	public static List<String> getAllMonthInZone(int yearBeg, int monthBeg, int yearEnd, int monthEnd) throws Exception {
		List<String> list = new ArrayList<>();
		// 添加开始时间
		list.add(yearBeg + "-" + ((monthBeg + "").length() == 1 ? "0" + monthBeg : monthBeg));
		boolean flag = true;
		while (flag) {
			if (yearBeg == yearEnd && monthBeg == monthEnd) {
				flag = false;
				break;
			}
			if (monthBeg == 12) {
				yearBeg = yearBeg + 1;
				monthBeg = 1;
			} else {
				monthBeg = monthBeg + 1;
			}
			list.add(yearBeg + "-" + ((monthBeg + "").length() == 1 ? "0" + monthBeg : monthBeg));
		}
		return list;
	}

	/**
	 * 获取num年前
	 * 
	 * @param date
	 * @return Date
	 */
	public static String getDateBeforeYear(Date date, int num) {

		if (null == date) {
			date = new Date();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		cal.set(Calendar.YEAR, year - num);// 几年前
		return new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
	}

	public static void main(String[] args) {
		System.out.println(getDateBeforeYear(new Date(), 26));
	}

	/**
	 * 得到几天前的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	/**
	 * 得到几天前的时间 Str
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static String getDateBeforeStr(String str, int num, String dateFormat) {
		Date date = getDate(str, dateFormat);
		Date beforeDate = getDateBefore(date, num);
		return getDateString(beforeDate, dateFormat);
	}

	/**
	 * 得到几天后的时间 Str
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static String getDateAfterStr(String str, int num, String dateFormat) {
		Date date = getDate(str, dateFormat);
		Date afterDate = getDateAfter(date, num);
		return getDateString(afterDate, dateFormat);
	}

	/**
	 * 得到几天后的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date d, int day) {

		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

	/**
	 * 
	 * @Title: getIntervalDays 相差天数。满24小时差一天
	 * @Description:
	 * @param @param fDate
	 * @param @param oDate
	 * @param @return
	 * @return int
	 */
	public static int getIntervalDays(Date fDate, Date oDate) {
		if (null == fDate || null == oDate) {
			return -1;
		}

		long intervalMilli = oDate.getTime() - fDate.getTime();

		return (int) (intervalMilli / (24 * 60 * 60 * 1000));
	}

	/**
	 * 
	 * @Title: getCurrentAge
	 * @Description: 获取当前年龄
	 * @param birthDate
	 * @return
	 * @return int 返回类型
	 * @throws
	 * @author Administrator
	 */
	public static int getCurrentAge(Date birthDate) {
		Date currentDate = new Date();
		if (birthDate == null) {
			return 0;
		}
		double diff = 0.0f;
		int age = Integer.parseInt(DateUtil.formatDateToStringWithNull(currentDate, "yyyy")) - Integer.parseInt(DateUtil.formatDateToStringWithNull(birthDate, "yyyy"));
		int emonth = Integer.parseInt(DateUtil.formatDateToStringWithNull(currentDate, "MM")) - Integer.parseInt(DateUtil.formatDateToStringWithNull(birthDate, "MM"));
		if (emonth == 0) {
			int eDay = Integer.parseInt(DateUtil.formatDateToStringWithNull(currentDate, "dd")) - Integer.parseInt(DateUtil.formatDateToStringWithNull(birthDate, "dd"));
			if (eDay < 0) {
				diff = -0.1;
			} else {
				diff = 0.1;
			}
		} else if (emonth < 0) {
			diff = -0.1;
		} else if (emonth > 0) {
			diff = 0.1;
		}

		return (int) (diff + age);
	}

	/**
	 * 
	 * @Title: getCurrentAgeStr
	 * @Description: 获取当前年龄
	 * @param birthDate
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	public static String getCurrentAgeStr(Date birthDate) {
		Date currentDate = new Date();
		if (birthDate == null) {
			return "";
		}
		double diff = 0.0f;
		int age = Integer.parseInt(DateUtil.formatDateToStringWithNull(currentDate, "yyyy")) - Integer.parseInt(DateUtil.formatDateToStringWithNull(birthDate, "yyyy"));
		int emonth = Integer.parseInt(DateUtil.formatDateToStringWithNull(currentDate, "MM")) - Integer.parseInt(DateUtil.formatDateToStringWithNull(birthDate, "MM"));
		if (emonth == 0) {
			int eDay = Integer.parseInt(DateUtil.formatDateToStringWithNull(currentDate, "dd")) - Integer.parseInt(DateUtil.formatDateToStringWithNull(birthDate, "dd"));
			if (eDay < 0) {
				diff = -0.1;
			} else {
				diff = 0.1;
			}
		} else if (emonth < 0) {
			diff = -0.1;
		} else if (emonth > 0) {
			diff = 0.1;
		}
		Integer nowAge = (int) (diff + age);
		return nowAge.toString();
	}

	/**
	 * 
	 * @Title: daysOfTwo 相差天数 日期相减
	 * @Description:
	 * @param @param fDate
	 * @param @param oDate
	 * @param @return
	 * @return int
	 */
	public static int daysOfTwo(Date fDate, Date oDate) {
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(fDate);
		int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
		aCalendar.setTime(oDate);
		int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
		return day2 - day1;

	}

	/**
	 * 日期+时间格式校验 数据库日期格式亦适用 add by sumfing 2011-5-5
	 * 
	 * @param checkValue
	 * @return
	 */
	public static boolean isDateStrValid(String checkValue) {
		try {
			String eL = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))( (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d(.0)?$)?";
			Pattern p = Pattern.compile(eL);
			Matcher m = p.matcher(checkValue);
			return m.matches();
		} catch (NullPointerException e) {
		}
		return false;
	}

	/**
	 * @title tranFormDateString
	 * @description 将yyyy、yyyyMM、yyyyMMDD格式的字符串转换为Date
	 * @param dateStr
	 * @return Date
	 */
	public static Date tranFormDateString(String dateStr) {
		Date date = null;
		String tranFormDateStr = "";
		try {
			if (dateStr.length() == 4) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				date = sdf.parse(dateStr);
				tranFormDateStr = sdf.format(date);
				if (!tranFormDateStr.equals(dateStr)) {
					return null;
				}
			}
			if (dateStr.length() == 6) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
				date = sdf.parse(dateStr);
				tranFormDateStr = sdf.format(date);
				if (!tranFormDateStr.equals(dateStr)) {
					return null;
				}
			}
			if (dateStr.length() == 8) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				date = sdf.parse(dateStr);
				tranFormDateStr = sdf.format(date);
				if (!tranFormDateStr.equals(dateStr)) {
					return null;
				}
			}
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
		return date;
	}

	/**
	 * 
	 * @Title: formatDate
	 * @Description: 格式化日期 校验通过返回 格式后数据，否则返回空字符串
	 * @param dateStr
	 *            日期字符串
	 * @param pattern
	 *            格式(如:yyyy.MM)
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author LFG
	 * @eg: 2014-03-12 00:00:00 空串 2014-03-12 2014.03 2014-03 2014.03 2014.03
	 *      2014.03 20140312 2014.03 201403 2014.03
	 */
	public static String formatDate(String dateStr, String pattern) {
		HashMap<String, String> dateRegFormat = new HashMap<String, String>();
		dateRegFormat.put("^\\d{4}\\D+\\d{2}\\D+\\d{2}\\D+\\d{2}$", "yyyy-MM-dd-HH");
		dateRegFormat.put("^\\d{4}\\D+\\d{2}\\D+\\d{2}$", "yyyy-MM-dd");
		dateRegFormat.put("^\\d{4}\\D+\\d{2}$", "yyyy-MM");
		dateRegFormat.put("^\\d{8}$", "yyyyMMdd");
		dateRegFormat.put("^\\d{6}$", "yyyyMM");
		String curDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		DateFormat formatter1 = new SimpleDateFormat(pattern);
		DateFormat formatter2;
		String dateReplace;
		String strSuccess = "";
		try {
			for (String key : dateRegFormat.keySet()) {
				if (Pattern.compile(key).matcher(dateStr).matches()) {
					formatter2 = new SimpleDateFormat(dateRegFormat.get(key));
					if (key.equals("^\\d{2}\\s*:\\s*\\d{2}\\s*:\\s*\\d{2}$") || key.equals("^\\d{2}\\s*:\\s*\\d{2}$")) {
						dateStr = curDate + "-" + dateStr;
					} else if (key.equals("^\\d{1,2}\\D+\\d{1,2}$")) {
						dateStr = curDate.substring(0, 4) + "-" + dateStr;
					}
					dateReplace = dateStr.replaceAll("\\D+", "-");
					strSuccess = formatter1.format(formatter2.parse(dateReplace));
					break;
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
		return strSuccess;
	}

	/**
	 * 
	 * @Title: diffDays
	 * @Description: 两个事件相差天数
	 * @param date1
	 * @param date2
	 * @return
	 * @return long 返回类型
	 * @throws
	 * @author LFG
	 */
	public static Long diffDays(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			return null;
		}
		Long between = (date1.getTime() - date2.getTime()) / 1000;
		Long day = between / (24 * 3600);
		return day;
	}

	public static int compereDateToMonth(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);

		if (c1.get(Calendar.YEAR) > c2.get(Calendar.YEAR)) {
			return 1;
		} else if (c1.get(Calendar.YEAR) < c2.get(Calendar.YEAR)) {
			return -1;
		} else {
			if (c1.get(Calendar.MONTH) > c2.get(Calendar.MONTH)) {
				return 1;
			} else if (c1.get(Calendar.MONTH) < c2.get(Calendar.MONTH)) {
				return -1;
			} else {
				return 0;
			}
		}
	}
	
	/**
	 * 将日期格式转换为“XXXX年XX月XX日”
	 * @param date
	 * @return
	 */
	public static String formatDateToStr(Date date){
		String dateStr = formatDateToStringWithNull(date, "yyyy-MM-dd");
		return dateStr.substring(0,4)+"年"+dateStr.substring(5,7)+"月"+dateStr.substring(8,10)+"日";
	}


	/**
	 * @param date 当前日期
	 * @param offSet 时间偏移量
	 * @return
	 */
	public static final Date getOffsetMinute(Date date,int offSet) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, offSet);
		return calendar.getTime();
	}




}
