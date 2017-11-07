package rongji.framework.util;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: RongJi
 * </p>
 *
 * @author Anna
 * @version 2.0
 * @create in 2009-3-13
 */
public final class StringUtil {

    /**
     * 验证字符串是否为空
     *
     * @param string
     * @return
     */
    public static boolean isEmpty(String string) {
        boolean result = false;
        if (string == null || "".equals(string.trim())) {
            result = true;
        }
        return result;
    }

    /**
     * @param num
     * @return String 返回类型
     * @throws
     * @Title: getSpaces
     * @Description: 获取固定个数的空格
     * @author Administrator
     */
    public static String getSpaces(int num) {
        StringBuffer buffer = new StringBuffer("");
        for (int i = 0; i < num; i++) {
            buffer.append(" ");
        }
        return buffer.toString();
    }

    /**
     * @param str
     * @return int 返回类型
     * @throws
     * @Title: GetPlaceHolderLength
     * @Description: 获得字符串在展示时实际的相对占位的长度值
     * @author Administrator
     */
    public static int GetPlaceHolderLength(String str) {
        int realLength = 0;
        if (str == null) {
            return 0;
        }
        for (int i = 0; i < str.length(); i++) {
            char charCode = str.charAt(i);
            if (charCode >= 0 && charCode <= 128) {
                realLength += 1;
            } else {
                realLength += 2;
            }
        }
        return realLength;
    }

    /**
     * 验证字符串是否为非空
     *
     * @param string
     * @return
     */
    public static boolean isNotEmpty(String string) {
        boolean result = false;
        if (string != null && !"".equals(string.trim())) {
            result = true;
        }
        return result;
    }

    /**
     * 验证字符串内容
     *
     * @param string  待验证字符
     * @param equ_str 用于验证的内容字符(可传数组[],)
     * @return
     */
    public static boolean isEquals(String string, String... equ_str) {

        // if(!isEmpty(string)&&!isEmpty(equ_str)){
        // if(string.equals(equ_str))return true;
        // }
        // return false;

        if (!isEmpty(string) && !isEmpty(equ_str)) {
            for (String equ : equ_str) {
                if (!isEmpty(equ)) {
                    if (string.equals(equ))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * 验证字符串内容是否以...开始
     *
     * @param string   待验证字符
     * @param star_str 用于验证的开始字符(可传数组[],)
     * @return
     */
    public static boolean startsWith(String string, String... star_str) {
        if (!isEmpty(string) && !isEmpty(star_str)) {
            for (String st : star_str) {
                if (!isEmpty(st)) {
                    if (string.startsWith(st))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * 验证字符串内容是否以...结束
     *
     * @param string  待验证字符
     * @param end_str 用于验证的结束字符(可传数组[],)
     * @return
     */
    public static boolean endsWith(String string, String... end_str) {
        if (!isEmpty(string) && !isEmpty(end_str)) {
            for (String end : end_str) {
                if (!isEmpty(end)) {
                    if (string.endsWith(end))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * 将数字字符串转化为int型
     *
     * @param srcInt
     * @return int
     */
    public static int doNullInt(String srcInt) {
        if (srcInt == null || "".equals(srcInt))
            return 0;
        return Integer.parseInt(srcInt);
    }

    public static int doNullInt(Object obj) {
        String srcInt = doNullStr(obj);
        if (srcInt == null || "".equals(srcInt))
            return 0;
        return Integer.parseInt(srcInt);
    }

    /**
     * 将数字字符串转化为long型
     *
     * @param srcInt
     * @return
     */
    public static long doNullLong(String srcInt) {
        if (srcInt == null || "".equals(srcInt))
            return 0;
        return Long.parseLong(srcInt);
    }

    public static long doNullLong(Object obj) {
        String srcInt = doNullStr(obj);
        if (srcInt == null || "".equals(srcInt))
            return 0;
        return Long.parseLong(srcInt);
    }

    /**
     * 转化为字符串
     *
     * @param obj Object
     * @return String
     */
    public static String doNullStr(Object obj) {
        String str = "";
        if (obj != null) {
            str = String.valueOf(obj);
            if (str.equals("null")) {
                str = "";
            }
        }
        return str;
    }

    public static Integer doNullInteger(Object obj) {
        String str = doNullStr(obj);
        if (isEmpty(str)) {
            str = "0";
        } else {
            int i = str.indexOf(".");
            if (i > 0) {
                str = str.substring(0, i);
            }
        }
        return Integer.valueOf(str);
    }

    /**
     * 验证字符串数组是否为空
     *
     * @param string
     * @return
     */
    public static boolean isEmpty(String[] string) {
        boolean result = (string == null || string.length == 0) ? true : false;
        if (!result) {
            for (String s : string) {
                if (!isEmpty(s)) {
                    return false;
                }
            }
            result = true;
        }
        return result;
    }

    /**
     * 对字符串进行操作，对于小于指定长度的字符，在其右方按特定字符进行补足。
     * <p>
     * <pre>
     * 示例：
     * 	String stringUtils = &quot;abc&quot;;
     * 	System.out.println(StringUtils.padRight(stringUtils, 5, '0'));
     * 输出结果为：abc00；
     * </pre>
     *
     * @param value       输入值
     * @param totalWidth  总长度
     * @param paddingChar 不足时填充的字符
     * @return 重新计算后的字符。
     */
    public static String padRight(String value, int totalWidth, char paddingChar) {
        String temp = value;
        if (value.length() > totalWidth) {
            return value;
        } else {
            while (temp.length() < totalWidth) {
                temp = temp + paddingChar;
            }
            return temp;
        }
    }

    /**
     * 对字符串进行操作，对于小于指定长度的字符，在其左方按特定字符进行补足。 示例：
     * <p>
     * <pre>
     * String stringUtils = &quot;abc&quot;;
     * System.out.println(StringUtils.padLeft(stringUtils, 5, '0'));
     * </pre>
     * <p>
     * 输出结果为：00abc；
     *
     * @param value       输入值
     * @param totalWidth  总长度
     * @param paddingChar 不足时填充的字符
     * @return 重新计算后的字符。
     */
    public static String padLeft(String value, int totalWidth, char paddingChar) {
        String temp = value;
        if (value.length() > totalWidth) {
            return value;
        } else {
            while (temp.length() < totalWidth) {
                temp = paddingChar + temp;
            }
            return temp;
        }
    }

    /**
     * java trim()重写，取出字符串前后空格 add 2009-5-5 by sumfing
     */
    public static String reTrimByString(String value) {
        String reValue;
        if (value == null || value.equals("")) {
            reValue = "";
        } else {
            reValue = value.trim();
        }
        return reValue;
    }

    public static String reTrimByObject(Object obj) {
        String reValue;
        if (obj == null || obj.equals("")) {
            reValue = "";
        } else {
            reValue = String.valueOf(obj).trim();
        }
        return reValue;
    }

    /**
     * 类似String.indexOf() 返回字符串在数组中的位置
     *
     * @param strArr
     * @param str
     * @return
     */
    public static int indexOfStringArray(String[] strArr, String str) {
        int index = -1;
        if (strArr != null && str != null) {
            for (int i = 0; i < strArr.length; i++) {
                if (str.equals(strArr[i])) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    /**
     * 替换特定的字符串，替换位置为第一次遇到的
     *
     * @param whole  完整的字符串
     * @param strold 要被替换的字符串
     * @param strnew 替换的字符串
     * @return
     */
    public static String replaceFirst(String whole, String strold, String strnew) {

        if (whole.indexOf(strold) > -1 && strnew != null) {
            String whole_one = whole.substring(0, whole.indexOf(strold));
            String whole_two = whole.substring(whole.indexOf(strold) + strold.length());
            whole = whole_one + strnew + whole_two;
        }

        return whole;
    }

    /**
     * 将字符串加上单引号
     *
     * @param strs
     * @param splitChar
     * @return '','',''
     */
    public static String addDyh(String strs, String splitChar) {

        if (!isEmpty(strs)) {
            if (isEmpty(splitChar))
                splitChar = ",";
            String[] ss = strs.split(splitChar);
            String reStrs = "";
            for (int i = 0; i < ss.length; i++) {
                if (!isEmpty(ss[i])) {
                    reStrs += splitChar + "'" + ss[i] + "'";
                }
            }
            if (!isEmpty(reStrs))
                reStrs = reStrs.substring(1);
            return reStrs;
        }
        return strs;
    }

    /**
     * 将String数组转换为Long类型数组
     *
     * @param strs
     * @return
     */
    public static Long[] convertionToLong(String[] strs) {
        Long[] longs = null;
        if (!isEmpty(strs)) {
            longs = new Long[strs.length];
            for (int i = 0; i < strs.length; i++) {
                String str = strs[i];
                long thelong = Long.valueOf(str);
                longs[i] = thelong;
            }
        }
        return longs;
    }

    /**
     * 将String转换为Long类型数组
     *
     * @param strs
     * @param splitChar 分割字符
     * @return
     */
    public static Long[] convertionToLongArr(String strs, String splitChar) {
        if (isEmpty(splitChar))
            splitChar = ",";
        Long[] result = null;
        if (!StringUtil.isEmpty(strs)) {
            String[] ids = strs.split(splitChar);
            result = new Long[ids.length];
            for (int i = 0; i < ids.length; i++) {
                result[i] = new Long(ids[i]);
            }
        }
        return result;
    }

    /**
     * 将String转换为List类型数组
     *
     * @param strs
     * @param splitChar 分割字符
     * @return
     */
    public static List<String> convertionToList(String strs, String splitChar) {
        if (isEmpty(splitChar)) {
            splitChar = ",";
        }
        List<String> result = null;
        if (!StringUtil.isEmpty(strs)) {
            String[] ids = strs.split(splitChar);
            result = new ArrayList<String>();
            for (int i = 0; i < ids.length; i++) {
                result.add(ids[i]);
            }
        }
        return result;
    }

    /**
     * 将String转换为List类型数组，不去空
     *
     * @param strs
     * @param splitChar 分割字符
     * @return
     */
    public static List<String> convertionToListWithEmpty(String strs, String splitChar) {
        if (isEmpty(splitChar)) {
            splitChar = ",";
        }
        List<String> result = null;
        if (!StringUtil.isEmpty(strs)) {
            String[] ids = strs.split(splitChar, -1);
            result = new ArrayList<String>();
            for (int i = 0; i < ids.length; i++) {
                result.add(ids[i]);
            }
        }
        return result;
    }

    /**
     * Long[] 转成 String
     *
     * @param l
     * @param splitChar
     * @return
     */
    public static String convertionLongToString(Long[] l, String splitChar) {
        String result = null;
        if (l != null) {
            result = Arrays.toString(l);
            result = result.substring(1, result.length() - 1);
            if (!StringUtil.isEmpty(splitChar)) {
                result = result.replaceAll(",", splitChar);
            }
        }
        return result;
    }

    /**
     * Short[] 转成 String
     *
     * @param l
     * @param splitChar
     * @return
     */
    public static String convertionShortToString(Short[] l, String splitChar) {
        String result = null;
        if (l != null) {
            result = Arrays.toString(l);
            result = result.substring(1, result.length() - 1);
            if (!StringUtil.isEmpty(splitChar)) {
                result = result.replaceAll(",", splitChar);
            }
        }
        return result;
    }

    /**
     * List<Long> 转成 String
     *
     * @param l
     * @param splitChar
     * @return
     */
    public static String convertionLongToString(List<Long> l, String splitChar) {
        StringBuffer result = new StringBuffer();
        if (l != null && l.size() > 0) {
            for (int i = 0; i < l.size(); i++) {
                Long ll = l.get(i);
                if (ll != null) {
                    result.append(ll);
                    if (i != l.size() - 1)
                        result.append(splitChar);
                }
            }
        }
        return result.toString();
    }

    /**
     * List<Short> 转成 String
     *
     * @param l
     * @param splitChar
     * @return
     */
    public static String convertionShortToString(List<Short> l, String splitChar) {
        StringBuffer result = new StringBuffer();
        if (l != null && l.size() > 0) {
            for (int i = 0; i < l.size(); i++) {
                Short ll = l.get(i);
                if (ll != null) {
                    result.append(ll);
                    if (i != l.size() - 1)
                        result.append(splitChar);
                }
            }
        }
        return result.toString();
    }

    /**
     * List<String> 转成 String
     *
     * @param l
     * @param splitChar
     * @param addYh     是否给每个字符串加上引号
     * @return
     */
    public static String convertionArrToString(List<String> l, String splitChar, boolean addYh) {
        StringBuffer result = new StringBuffer();
        if (l != null && l.size() > 0) {
            for (int i = 0; i < l.size(); i++) {
                String ll = l.get(i);
                if (ll != null) {
                    if (addYh) {
                        result.append("'" + ll + "'");
                    } else {
                        result.append(ll);
                    }
                    if (i != l.size() - 1)
                        result.append(splitChar);
                }
            }
        }
        return result.toString();
    }

    /**
     * 将字符串str里的某些字符sregex转义成指定的字符sreplace
     *
     * @param str
     * @param sregex
     * @param sreplace
     * @return String
     */
    public static String getStrTransMean(String str, String sregex, String sreplace) {
        if (!StringUtil.isEmpty(str)) {
            str = str.replaceAll(sregex, sreplace);
        }
        return str;
    }

    /**
     * 通过指定长度分拆字符串，len个一组，装入list
     *
     * @param str
     * @param len
     * @return List<String>
     */
    public static List<String> splitStringByLength(String str, int len) {
        List<String> list = new ArrayList<String>();
        if (str != null && str.length() > 0) {
            String[] strArr = str.split(",");
            int mod = strArr.length % len;
            int divide = strArr.length / len;
            int time = mod == 0 ? divide : divide + 1;
            for (int i = 0; i < time; i++) {
                int end = (i + 1) * len;
                if (strArr.length - i * len < len) {
                    end = strArr.length;
                }
                String temp = "";
                for (int j = i * len; j < end; j++) {
                    temp += strArr[j] + ",";
                }
                list.add(temp.substring(0, temp.length() - 1));
            }
            return list;
        } else {
            return null;
        }
    }

    /**
     * Object转换成Long型
     *
     * @param obj
     * @return
     */
    public static Long convertToLong(Object obj) {
        if (obj != null) {
            return Long.valueOf(String.valueOf(obj));
        }
        return null;
    }

    /**
     * Object转换成Short型
     *
     * @param obj
     * @return
     */
    public static Short convertToShort(Object obj) {
        if (obj != null) {
            return Short.valueOf(String.valueOf(obj));
        }
        return null;
    }

    public static String rightTrim(String str) {
        if (str == null) {
            return "";
        }
        int length = str.length();
        for (int i = length - 1; i >= 0; i++) {
            if (str.charAt(i) != 0x20) {
                break;
            }
            length--;
        }
        return str.substring(0, length);
    }

    /**
     * 判断是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if (!isEmpty(str)) {
            Pattern pattern = Pattern.compile("[0-9]*");
            return pattern.matcher(str).matches();
        }
        return false;
    }

    /**
     * 根据字节下标取字符下标（当截取的字节为半字符则不取）
     *
     * @param str
     * @param byteInt
     * @return
     */
    public static int getCharInt(String str, int byteInt) {
        int l = 0;
        if (!isEmpty(str)) {
            char[] cs = str.toCharArray();
            int g = 0;
            // for(int b = 0;b<cs.length;b++){
            // char c = cs[b];
            for (char c : cs) {
                byte[] ss = String.valueOf(c + "").getBytes();
                g += ss.length;
                if (g > byteInt) {
                    g -= ss.length;
                    l -= 1;
                    break;
                }
                l += 1;
            }
        }
        return l;
    }

    /**
     * 计算replacedStr在sourceStr中出现的次数
     *
     * @param sourceStr
     * @param replacedStr
     * @param str
     * @return
     * @Title: countStrTimesByStr
     * @date 2013-3-19 下午03:36:19
     */
    public static int countStrTimesByStr(String sourceStr, String replacedStr) {
        return (sourceStr.length() - sourceStr.replace(replacedStr, "").length()) / replacedStr.length();
    }

    /**
     * 将null字符串 转成""
     *
     * @param str
     * @return
     */
    public static String convertNullStr(String str) {
        if (isEmpty(str)) {
            return "";
        }
        return str;
    }

    /**
     * @param str 字符串
     * @return boolean 返回类型
     * @throws
     * @Title: isChineseChar
     * @Description: 字符串是否包含中文字符
     * @author LFG
     */
    public static boolean isContainChineseChar(String str) {
        boolean temp = false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            temp = true;
        }
        return temp;
    }

    /**
     * @param str
     * @return String 返回类型
     * @throws
     * @Title: formatChineseStr
     * @Description: 格式化中文名(两个汉字，则加空格)
     * @author LFG
     */
    public static String formatChineseStr(String str) {
        if (StringUtil.isEmpty(str)) {
            return "";
        }
        str = str.replaceAll(" ", "");
        String resultStr = str;
        if (isContainChineseChar(str)) {// 包含中文
            if (str.length() == 2) {
                resultStr = str.substring(0, 1) + " " + str.substring(1, 2);
            }
        }
        return resultStr;
    }

    /**
     * @param str
     * @return Boolean 返回类型
     * @throws
     * @Title: formatChineseStr
     * @Description: 格式化中文名(两个汉字，则加空格)
     * @author LFG
     */
    public static Boolean isTwoChineseStr(String str) {
        if (StringUtil.isEmpty(str)) {
            return false;
        }
        if (isContainChineseChar(str)) {// 包含中文
            if (str.length() == 2) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param str 民族
     * @return String 返回类型
     * @throws
     * @Title: formatOrigin
     * @Description: 格式化民族名称(如果以族结尾则去掉族，否则原样返回)
     * @author LFG
     */
    public static String formatOrigin(String str) {
        if (StringUtil.isEmpty(str)) {
            return "";
        }
        String resultStr = str;
        if ("族".equals(str.substring(str.length() - 1, str.length()))) {
            resultStr = str.substring(0, str.length() - 1);
        }
        return resultStr;
    }

    public static int countSingleByteEleNum(String str) {
        int count = 0;
        if (str == null) {
            return 0;
        }
        for (int i = 0; i < str.length(); i++) {
            char charCode = str.charAt(i);
            if (charCode >= 0 && charCode <= 128) {
                count += 1;
            }

        }
        return count;
    }

    public static String formatWrap(String manualResume) {
        if (StringUtil.isNotEmpty(manualResume)) {
            return manualResume.replaceAll("\r\n", "\r").replaceAll("\n", "\r").replaceAll("\r", "\r\n");
        } else {
            return "";
        }
    }

    public static String ltrim(String str) {
        if (isEmpty(str)) {
            return "";
        }
        int startIndex = str.indexOf(str.trim().charAt(0) + "");
        return str.substring(startIndex);
    }


    public static String trim(String str) {
        if (isEmpty(str)) {
            return "";
        }
        return str.trim();
    }


    public static String formatWrapByExcel(String str) {
        if (StringUtils.isNotEmpty(str)) {
            str = str.replaceAll("\r\n", "\r").replaceAll("\n", "\r").replaceAll("<br>", "\r").replaceAll("\r", "\r\n");
        }
        return str;

    }

    public static String formatBackslashByHtml(String str) {
        if (isEmpty(str)) {
            return "";
        }
        return str.replaceAll("\\\\", "＼");

    }

    public static Boolean isBelongAWordAndNotEnd(String str, Integer index) {
        if (str.length() <= index + 1) {
            return false;
        }
        char charCode = str.charAt(index);
        if (charCode > 128 || charCode == ' ') {
            return false;
        }
        char charCodeSec = str.charAt(index + 1);
        if (charCodeSec > 128 || charCodeSec == ' ') {
            return false;
        }
        return true;
    }

    public static String cullLastWord(String str) {
        for (int i = str.length() - 1; i > 0; i--) {
            char charCode = str.charAt(i);
            if (charCode > 128 || charCode == ' ') {
                return str.substring(0, i + 1);
            }
        }
        return str;
    }

    public static String replaceDot(String object) {
        if (object != null) {
            return object.replaceAll(",", "，");
        }
        return object;
    }

    public static String fomartEmpty(String str) {
        if (isEmpty(str)) {
            return "";
        }
        return str;
    }

    /**
     * @param str
     * @return Integer 返回类型
     * @throws
     * @Title: countNumAndEng
     * @Description: 统计数字+英文字母个数
     * @author LFG
     */
    private static Integer countNumAndEng(String str) {
        if (null == str || str.length() == 0) {
            return 0;
        }

        String E2 = "[a-zA-Z]";// 英文
        String E3 = "[0-9]";// 数字

        int englishCount = 0;
        int numberCount = 0;

        String temp;
        for (int i = 0; i < str.length(); i++) {
            temp = String.valueOf(str.charAt(i));
            if (temp.matches(E2)) {
                englishCount++;
            }
            if (temp.matches(E3)) {
                numberCount++;
            }
        }
        return (str.length() - englishCount - numberCount) * 2 + englishCount + numberCount;
    }

    /**
     * @param str
     * @return Map<String,Object> 返回类型
     * @throws
     * @Title: countStrLen
     * @Description: 计算中文、数字、英文、特殊字符数量
     * @author LFG
     */
    public static Map<String, Object> diffTypeCount(String str) {
        if (null == str || str.length() == 0) {
            return null;
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();

        String E1 = "[\u4e00-\u9fa5]";// 中文
        String E2 = "[a-zA-Z]";// 英文
        String E3 = "[0-9]";// 数字
        String E4 = ".";// .
        String E5 = "-";// -

        int chineseCount = 0;
        int englishCount = 0;
        int numberCount = 0;
        int dotCount = 0;
        int minCount = 0;

        String temp;
        for (int i = 0; i < str.length(); i++) {
            temp = String.valueOf(str.charAt(i));
            if (temp.matches(E1)) {
                chineseCount++;
            }
            if (temp.matches(E2)) {
                englishCount++;
            }
            if (temp.matches(E3)) {
                numberCount++;
            }
            if (temp.contains(E4)) {
                dotCount++;
            }
            if (temp.contains(E5)) {
                minCount++;
            }
        }
        resultMap.put("chnCount", chineseCount);
        resultMap.put("engCount", englishCount);
        resultMap.put("numCount", numberCount);
        resultMap.put("dotCount", dotCount);
        resultMap.put("minCount", minCount);
        resultMap.put("specCount", (str.length() - (chineseCount + englishCount + numberCount + dotCount + minCount)));

        return resultMap;
    }

    public static Integer countStrLength(String str) {
        Integer count = 0;
        if (null == str || "".equals(str)) {
            return 0;
        }
        str = str.replaceAll("<span style='color:red;'>", "").replaceAll("</span>", "").replaceAll("\r\n", "");
        Map<String, Object> map = diffTypeCount(str);
        Integer engCount = (Integer) map.get("engCount");
        Integer numCount = (Integer) map.get("numCount");
        Integer dotCount = (Integer) map.get("dotCount");
        Integer minCount = (Integer) map.get("minCount");

        int twoByteCount = str.length() - (engCount + numCount + dotCount + minCount);// 2个字节
        int oneByteCount = engCount + numCount + dotCount + minCount;
        count = twoByteCount + oneByteCount / 2;
        return count;
    }

    /**
     * @param str
     * @param count
     * @return String 返回类型
     * @throws
     * @Title: countStrLen
     * @Description: 根据总长度计算返回字符串（注：中文和特殊字符算2个字符，其余算一个字符）
     * @author LFG
     */
    public static String countStrLen(String str, Integer count) {
        if (null == str || str.length() == 0) {
            return "";
        }
        if (countNumAndEng(str) <= count) {
            return str;
        }
        // String E1 = "[\u4e00-\u9fa5]";// 中文
        String E2 = "[a-zA-Z]";// 英文
        String E3 = "[0-9]";// 数字

        Integer index = 0;
        Integer i = 0;
        StringBuffer resultStr = new StringBuffer("");
        while (index < count) {
            String temp = String.valueOf(str.charAt(i));
            i++;
            if (temp.matches(E2) || temp.matches(E3)) {
                index++;
            } else {
                index = index + 2;
            }
            resultStr.append(temp);
        }
        if (index > count) {
            resultStr.append("...");
        }
        return resultStr.toString() + "";
    }

    public static void main(String[] args) {
        String str = "（...2016.05--2016.05 在大专学校机修学习 2014.05--2016.06 莆田市委员（其间：2015.02--2015.08参加培训班名称学习；2016.05--2016.05参加中专学校名称挖掘机专业在职学习；2016.05--2016.05参加学士学位授予学校攻读学士学位，获得农学学士学位；2016.05--2016.05参加博士学位授予学校攻读博士学位，获得经济学博士学位；2016.05--2016.05参加中专学校名称挖掘机专业在职学习） 2016.05--2016.05 厦门市审纪员 （其间：2016.05--2016.05参加中专学校名称挖掘机专业在职学习） 2016.05--2016.05 厦门市审纪员,书记 （2016.05--2016.05参加学士学位授予学校攻读学士学位，获得农学学士学位；2016.05--2016.05参加博士学位授予学校攻读博士学位，获得经济学博士学位） 2016.05--2016.07 厦门市审纪员,书记,厦门市审纪员、省领导干部 2016.07-- 厦门市审纪员,书记,厦门市审纪员、省领导干部,省投资开发集团公司书记...）";
        System.out.println(countStrLength(str));
        System.out.println(390 / 60 + 1);
    }

    /**
     * 截取字符串中字母部分<br>
     * 只适用于 AA123 这样的格式,返回123
     *
     * @param str
     * @return
     */
    public static String cutToNum(String str) {
        String retuStr = "";
        for (int i = str.length(); i > 0; i--) {
            if (isNumber(str.charAt(i - 1) + "")) {
                retuStr = str.charAt(i - 1) + retuStr;
            } else {
                break;
            }
        }
        return retuStr;
    }

    /**
     * 截取字符串中字母部分<br>
     * 只适用于 AA123 这样的格式,返回AA
     *
     * @param str
     * @return
     */
    public static String cutToStr(String str) {
        String retuStr = "";
        for (int i = 0; i < str.length(); i++) {
            if (isNumber(str.charAt(i) + "")) {
                break;
            } else {
                retuStr += str.charAt(i);
            }
        }
        return retuStr;
    }

    public static String decimalToAlphabetBinary(Integer num) {
        if (num == null || num < 0) {
            return "";
        }
        char numChar = 'A';
        Integer numA = (int) numChar;
        String result = "";
        while (num.compareTo(26) >= 0) {
            Integer val = num % 26;
            result = String.valueOf((char) (numA + val)) + result;
            num = num / 26 - 1;
        }
        result = String.valueOf((char) (numA + num)) + result;
        return result;
    }

    public static String getInParameter(List<String> list, String parameter) {
        if (!list.isEmpty()) {
            List<String> setList = new ArrayList<String>(0);
            Set<String> set = new HashSet<String>();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 1; i <= list.size(); i++) {
                set.add("'" + list.get(i - 1) + "'");
                if (i % 500 == 0) {//500为阈值
                    setList.add(StringUtils.join(set.iterator(), ","));
                    set.clear();
                }
            }
            if (!set.isEmpty()) {
                setList.add(StringUtils.join(set.iterator(), ","));
            }
            stringBuffer.append(parameter + " in ( " + setList.get(0));
            for (int j = 1; j < setList.size(); j++) {
                stringBuffer.append(") or " + parameter + " in (");
                stringBuffer.append(setList.get(j));
            }
            stringBuffer.append(" ) ");
            return "and ( " + stringBuffer.toString() + " ) ";
        } else {
            return "''";
        }

    }

    public static String[] split(String string, String paramString) {
        List<String> info = new ArrayList<String>();
        Integer index = -1;
        while ((index = string.indexOf(paramString)) >= 0) {
            if (index == 0) {
                info.add("");
            } else {
                info.add(string.substring(0, index));
            }
            string = string.substring(index + 1);
        }
        info.add(string);
        return info.toArray(new String[]{});
    }


}
