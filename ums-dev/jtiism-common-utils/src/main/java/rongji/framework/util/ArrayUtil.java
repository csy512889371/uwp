package rongji.framework.util;

import java.util.*;
import java.util.Map.Entry;

/**
 * <p>
 * Title: 数组工具类
 * </p>
 * <p>
 * Description:提供常用数组的一些操作
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: RongJi
 * </p>
 *
 * @author redlwb
 * @version 1.0
 * @create in 2012-8-22
 */
public class ArrayUtil {

    /**
     * 这个类不需要实例化，因为全是静态方法 则写私有无参构造方法覆盖默认方法 防止外部调用产生不必要的实例
     */
    private ArrayUtil() {
    }

    /**
     * 检查一个数组是否包含一个字符窜或者数字,包含返回true 否则返回false
     *
     * @param arr  需要检查的数组,不支持int等基本类型数组，可以转换为Interge数组后再进行检查
     * @param flag 要检查是否包含的字符或者数字
     * @return
     * @author：redlwb add 2012-8-22
     */
    public static boolean isContain(Object[] arr, Object flag) {
        boolean isContain = false;
        String flagStr = flag + "";// 先转化为String类型
        if (arr != null && arr.length > 0) {
            for (Object object : arr) {
                String value = object + "";
                if (value != null && value.trim().equals(flagStr.trim())) {
                    isContain = true;
                    break;
                }
            }
        } else {
            IllegalArgumentException exception = new IllegalArgumentException(
                    "数组不能为空，无效的数组，请检查");
            throw exception;
        }
        return isContain;
    }

    /**
     * 获取arr数组中flag的第一个位置，如果不包含有flag则返回-1，包含则返回第一格flag位置
     *
     * @param arr  需要检查的数组,不支持int等基本类型数组，可以转换为相应的java.lang包下相应对象数组后再进行检查
     * @param flag 要检查是否包含的字符或者数字
     * @return
     * @author：redlwb add 2012-8-28
     */
    public static long getFirstIndexOf(Object[] arr, Object flag) {
        long index = -1;// 默认坐标负一
        String flagStr = flag + "";
        if (arr != null && arr.length > 0) {
            int i = 0;
            for (Object object : arr) {
                String value = object + "";
                if (value.equals(flagStr)) {
                    index = i;
                    return index;
                }
                i++;
            }
        } else {
            IllegalArgumentException exception = new IllegalArgumentException(
                    "数组不能为空，无效的数组，请检查");
            throw exception;

        }
        return index;
    }

    /**
     * 将map根据值的大小进行排序
     *
     * @param map
     * @param rule ture为从小到大false为从大到小
     * @return
     */
    public static <K, V extends Comparable<V>> Map<K, V> sortMapByValue(Map<K, V> map, final boolean rule) {
        List<Entry<K, V>> list = new LinkedList<Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Entry<K, V>>() {

            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
                Comparable<V> v1 = o1.getValue();
                V v2 = o2.getValue();
                if (v1 == null) {
                    if (v2 == null) {
                        return 0;
                    } else {
                        return rule ? -1 : 1;
                    }
                } else {
                    if (v2 == null) {
                        return rule ? 1 : -1;
                    } else {
                        return rule ? v1.compareTo(v2) : v1.compareTo(v2) * -1;
                    }
                }
            }
        });
        Map<K, V> result = new LinkedHashMap<K, V>();
        Iterator<Entry<K, V>> it = list.iterator();
        while (it.hasNext()) {
            Entry<K, V> entry = it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

}
