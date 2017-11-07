package rongji.framework.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Title: 对象（实现）Serializable接口的javaBean的泛型工具类
 * </p>
 * <p>
 * Description:封装一些常用的泛型反射方法 提供获取所有传入对象实例所有属性，所有属性对应值的方法 提供获取id的方法 提供批量set属性的方法
 * 获取类名的方法
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: RongJi
 * </p>
 * 
 * @author redlwb
 * @create in 2012-8-22
 * @version 1.0
 */
public class BeanUtil<T extends Serializable> {
	
	private static final Logger logger = LoggerFactory.getLogger(BeanUtil.class);

	/**
	 * 获取一个实现Serializable接口的实例的所有属性名以及值
	 * 
	 * @param <T>
	 *            实现Serializable接口的类
	 * @param targetEntity
	 *            实现Serializable接口的需要获取所有字段的目标实例
	 * @param exceptFileName
	 *            不需要获取的字段
	 * @return Object[] 返回值为一个object二元数组【0：属性名名数组；1：值数组】
	 */
	public static <T extends Serializable> Object[] getAllFieldsAndValuesArray(T targetEntity, String[] exceptFileNames) {
		Object[] returnArr = new Object[2];
		// 获取目标实体类
		Class<? extends Serializable> cla = targetEntity.getClass();

		// 获得该类下面所有的字段集合
		Field[] filed = cla.getDeclaredFields();

		int length = filed.length;

		String[] fileNamesTemp = new String[length];// 字段名temp数组
		Object[] valuesTemp = new Object[length];// 值temp数组

		int count = 0;// 计数器 记录符合要求的字段数
		// 遍历所有字段 获取除了serialVersionUID operatedate外的所有字段 以及相关的值
		for (int i = 0; i < length; i++) {
			String filedNamei = filed[i].getName();
			boolean isExcept = true;
			if (exceptFileNames != null) {
				for (int j = 0; j < exceptFileNames.length; j++) {
					if (exceptFileNames[j].equals(filedNamei)) {
						isExcept = false;
						break;
					}
				}
			}
			if (isExcept) {
				String firstLetter = filedNamei.substring(0, 1).toUpperCase(); // 获得字段第一个字母大写
				String getMethodName = "get" + firstLetter + filedNamei.substring(1); // 转换成字段的get方法
				try {
					Method getMethod = cla.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(targetEntity, new Object[] {}); // 这个对象字段get方法的值
					fileNamesTemp[count] = filedNamei;
					valuesTemp[count] = value;
					count++;
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		String[] fileNames = new String[count];
		Object[] values = new Object[count];
		for (int i = 0; i < count; i++) {
			fileNames[i] = fileNamesTemp[i];
			values[i] = valuesTemp[i];
		}
		returnArr[0] = fileNames;
		returnArr[1] = values;
		return returnArr;
	}
	
	
	/**
	 * 获取一个实现Serializable接口的实例的所有属性名以及值
	 * 
	 * @param <T>
	 *            实现Serializable接口的类
	 * @param targetEntity
	 *            实现Serializable接口的需要获取所有字段的目标实例
	 * @param exceptFileName
	 *            不需要获取的字段
	 * @return Object[] 返回值为一个object二元数组【0：属性名名数组；1：值数组】
	 */
	public static <T extends Serializable> Map<String, Object> getObjectToMap(T targetEntity, String[] exceptFileNames) {
		
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		// 获取目标实体类
		Class<? extends Serializable> cla = targetEntity.getClass();

		// 获得该类下面所有的字段集合
		Field[] filed = cla.getDeclaredFields();

		int length = filed.length;

		String[] fileNamesTemp = new String[length];// 字段名temp数组
		Object[] valuesTemp = new Object[length];// 值temp数组

		int count = 0;// 计数器 记录符合要求的字段数
		// 遍历所有字段 获取除了serialVersionUID operatedate外的所有字段 以及相关的值
		for (int i = 0; i < length; i++) {
			String filedNamei = filed[i].getName();
			boolean isExcept = true;
			if (exceptFileNames != null) {
				for (int j = 0; j < exceptFileNames.length; j++) {
					if (exceptFileNames[j].equals(filedNamei)) {
						isExcept = false;
						break;
					}
				}
			}
			if (isExcept) {
				String firstLetter = filedNamei.substring(0, 1).toUpperCase(); // 获得字段第一个字母大写
				String getMethodName = "get" + firstLetter + filedNamei.substring(1); // 转换成字段的get方法
				try {
					Method getMethod = cla.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(targetEntity, new Object[] {}); // 这个对象字段get方法的值
					fileNamesTemp[count] = filedNamei;
					valuesTemp[count] = value;
					count++;
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		for (int i = 0; i < count; i++) {
			resultMap.put(fileNamesTemp[i], valuesTemp[i]);
		}
		return resultMap;
	}

	/**
	 * 获取一个实现Serializable接口的实例的所有属性名
	 * 
	 * @param <T>
	 *            实现Serializable接口的类
	 * @param targetEntity
	 *            实现Serializable接口的需要获取所有字段的目标实例
	 * @param exceptFileNames
	 *            不需要获取的字段
	 * @return Object[] 返回值为一个object二元数组【0：字段名数组；1：值数组】
	 */
	public static <T extends Serializable> String[] getAllFieldsArray(T targetEntity, String[] exceptFileNames) {
		// 获取目标实体类
		Class<? extends Serializable> cla = targetEntity.getClass();

		// 获得该类下面所有的字段集合
		Field[] filed = cla.getDeclaredFields();

		int length = filed.length;

		String[] fileNamesTemp = new String[length];// 字段名temp数组

		int count = 0;// 计数器 记录符合要求的字段数
		// 遍历所有字段 获取除了serialVersionUID operatedate外的所有字段 以及相关的值
		for (int i = 0; i < length; i++) {
			String filedNamei = filed[i].getName();

			boolean isExcept = true;
			if (exceptFileNames != null) {
				for (int j = 0; j < exceptFileNames.length; j++) {
					if (exceptFileNames[j].equals(filedNamei)) {
						isExcept = false;
						break;
					}
				}
			}
			if (isExcept) {
				fileNamesTemp[count] = filedNamei;
				count++;
			}
		}
		String[] fileNames = new String[count];
		for (int i = 0; i < count; i++) {
			fileNames[i] = fileNamesTemp[i];
		}
		return fileNames;
	}

	/**
	 * 获取一个实现Serializable接口的实例的所有属性名
	 * 
	 * @param <T>
	 *            实现Serializable接口的类
	 * @param targetEntity
	 *            实现Serializable接口的需要获取所有字段的目标实例
	 * @param exceptFileNames
	 *            不需要获取的字段
	 * @return Object[] 返回值为一个object二元数组【0：字段名数组；1：值数组】
	 */
	public static <T extends Serializable> Object[] getAllValuesArray(T targetEntity, String[] exceptFileNames) {
		// 获取目标实体类
		Class<? extends Serializable> cla = targetEntity.getClass();

		// 获得该类下面所有的字段集合
		Field[] filed = cla.getDeclaredFields();

		int length = filed.length;

		Object[] valuesTemp = new Object[length - exceptFileNames.length];// 值temp数组

		int count = 0;// 计数器 记录符合要求的字段数
		// 遍历所有字段 获取除了serialVersionUID operatedate外的所有字段 以及相关的值
		for (int i = 0; i < length; i++) {
			String filedNamei = filed[i].getName();
			boolean isExcept = true;
			if (exceptFileNames != null) {
				for (int j = 0; j < exceptFileNames.length; j++) {
					if (exceptFileNames[j].equals(filedNamei)) {
						isExcept = false;
						break;
					}
				}
			}

			if (isExcept) {
				String firstLetter = filedNamei.substring(0, 1).toUpperCase(); // 获得字段第一个字母大写
				String getMethodName = "get" + firstLetter + filedNamei.substring(1); // 转换成字段的get方法
				try {
					Method getMethod = cla.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(targetEntity, new Object[] {}); // 这个对象字段get方法的值
					if (value != null) {
						valuesTemp[count] = value;
					}
					count++;
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		Object[] values = new Object[count];
		for (int i = 0; i < count; i++) {
			values[i] = valuesTemp[i];
		}
		return values;
	}

	/**
	 * 获取除去包名的类名
	 * 
	 * @param <T>
	 * @param targetEntity
	 * @return
	 */
	public static <T extends Serializable> String getClassName(T targetEntity) {
		String className = "";
		if (targetEntity != null) {
			String classNameTemp = targetEntity.getClass().getName();
			int lastIndex = classNameTemp.lastIndexOf(".");
			if (lastIndex > -1) {// 获取类名
				className = classNameTemp.substring(lastIndex + 1);
			}
		}
		return className;
	}

	/**
	 * 获取传入实例的属性值（必须包含get属性方法）
	 * 
	 * @param <T>
	 * @param targetEntity
	 * @return
	 */
	public static <T extends Serializable> String getClassProperty(T targetEntity, String propertyName) {
		String propertyValue = "";
		String firstLetter = propertyName.substring(0, 1).toUpperCase(); // 获得字段第一个字母大写
		String getMethodName = "get" + firstLetter + propertyName.substring(1); // 转换成字段的get方法
		if (targetEntity != null) {
			Class<? extends Serializable> cla = targetEntity.getClass();// 获取实例所属的类
			Method getMethod = null;
			try {
				getMethod = cla.getMethod(getMethodName, new Class[] {});
				propertyValue = getMethod.invoke(targetEntity, new Object[] {}) + ""; // 这个对象字段get方法的值
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return null;
			}
		}
		return propertyValue;
	}

	/**
	 * 获取传入实例的id值（必须包含get属性方法）
	 * 
	 * @param <T>
	 * @param targetEntity
	 * @return
	 */
	public static <T extends Serializable> Object getClassProperty(String propertyName, T targetEntity) {
		Object propertyValue = null;
		String firstLetter = propertyName.substring(0, 1).toUpperCase(); // 获得字段第一个字母大写
		String getMethodName = "get" + firstLetter + propertyName.substring(1); // 转换成字段的get方法
		if (targetEntity != null) {
			Class<? extends Serializable> cla = targetEntity.getClass();// 获取实例所属的类
			Method getMethod = null;
			try {
				getMethod = cla.getMethod(getMethodName, new Class[] {});
				propertyValue = getMethod.invoke(targetEntity, new Object[] {}); // 这个对象字段get方法的值
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return null;
			}
		}
		return propertyValue;
	}

	/**
	 * 调用目标实体T的属性propertyName的set方法，将value设入
	 * 
	 * @param <T>
	 * @param targetEntity
	 *            目标实例
	 * @param propertyName
	 *            属性名，属性值
	 * @param value
	 *            值
	 * @author：redlwb add 2012-9-19
	 */
	public static <T extends Serializable> void setClassProperty(T targetEntity, String propertyName, String value) {
		String firstLetter = propertyName.substring(0, 1).toUpperCase(); // 获得字段第一个字母大写
		String setMethodName = "set" + firstLetter + propertyName.substring(1); // 转换成字段的get方法
		if (targetEntity != null) {
			try {
				Class<? extends Serializable> cla = targetEntity.getClass();// 获取实例所属的类
				Field field = cla.getDeclaredField(propertyName);
				Class<?> paramType = field.getType();
				String paramTypeName = paramType.getName();
				Method setMethod = null;
				setMethod = cla.getMethod(setMethodName, paramType);
				if (paramTypeName.equals("java.lang.String")) {
					setMethod.invoke(targetEntity, value);
				} else if (paramTypeName.equals("int") || paramTypeName.equals("java.lang.Integer")) {
					// System.out.println("it  is  int");
					setMethod.invoke(targetEntity, new Integer(value));
				} else if (paramTypeName.equals("long") || paramTypeName.equals("java.lang.Long")) {
					setMethod.invoke(targetEntity, new Long(value));
				} else if (paramTypeName.equals("boolean") || paramTypeName.equals("java.lang.Boolean")) {
					setMethod.invoke(targetEntity, Boolean.valueOf(value));
				} else if (paramTypeName.equals("java.util.Date")) {
					Date d = null;
					SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
					try {
						d = dateFormatter.parse(value);
					} catch (ParseException e) {
						logger.error(e.getMessage(), e);
					}
					setMethod.invoke(targetEntity, new Object[] { d });
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 调用目标实体T的属性propertyName的set方法，将value数组设入
	 * 
	 * @param <T>
	 * @param targetEntity
	 * @param propertyNames
	 *            属性名数组
	 * @param values
	 *            值数组 必须和属性名数组长度相等
	 * @author：redlwb add 2012-9-19
	 */
	public static <T extends Serializable> void setClassPropertys(T targetEntity, String[] propertyNames, Object[] values) {

		for (int i = 0; i < values.length; i++) {
			String propertyName = propertyNames[i];
			Object value = values[i];
			String valueStr = value + "";
			String firstLetter = propertyName.substring(0, 1).toUpperCase(); // 获得字段第一个字母大写
			String setMethodName = "set" + firstLetter + propertyName.substring(1); // 转换成字段的get方法
			if (targetEntity != null && value != null && !valueStr.trim().equals("") && !valueStr.trim().equals("null")) {
				try {
					Class<? extends Serializable> cla = targetEntity.getClass();// 获取实例所属的类
					Field field = cla.getDeclaredField(propertyName);
					Class<?> paramType = field.getType();
					String paramTypeName = paramType.getName();
					Method setMethod = null;
					setMethod = cla.getMethod(setMethodName, paramType);
					if (paramTypeName.equals("java.lang.String")) {
						setMethod.invoke(targetEntity, valueStr);
					} else if (paramTypeName.equals("int") || paramTypeName.equals("java.lang.Integer")) {
						// System.out.println("it  is  int");
						setMethod.invoke(targetEntity, new Integer(valueStr));
					} else if (paramTypeName.equals("long") || paramTypeName.equals("java.lang.Long")) {
						setMethod.invoke(targetEntity, new Long(valueStr));
					} else if (paramTypeName.equals("boolean") || paramTypeName.equals("java.lang.Boolean")) {
						setMethod.invoke(targetEntity, Boolean.valueOf(valueStr));
					} else if (paramTypeName.equals("short") || paramTypeName.equals("java.lang.Short")) {
						setMethod.invoke(targetEntity, Short.valueOf(valueStr));
					} else if (paramTypeName.equals("float") || paramTypeName.equals("java.lang.Float")) {
						setMethod.invoke(targetEntity, Float.valueOf(valueStr));
					} else if (paramTypeName.equals("double") || paramTypeName.equals("java.lang.Double")) {
						setMethod.invoke(targetEntity, Double.valueOf(valueStr));
					} else if (paramTypeName.equals("java.util.Date")) {
						setMethod.invoke(targetEntity, new Object[] { value });
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 获取指定字段名的值
	 * 
	 * @param <T>
	 * @param t
	 * @param fileNames
	 * @return
	 * @author：redlwb add 2012-9-25
	 */
	public static <T extends Serializable> Object[] getPropertyValuesRo(T t, String[] fileNames) {
		Object[] values = new Object[fileNames.length];
		for (int i = 0; i < fileNames.length; i++) {
			values[i] = getClassProperty(fileNames[i], t);
		}
		return values;
	}

	/**
	 * 获取指定字段名的值
	 * 
	 * @param <T>
	 * @param t
	 * @param fileNames
	 * @return
	 * @author：redlwb add 2012-9-25
	 */
	public static <T extends Serializable> String[] getPropertyValuesRs(T t, String[] fileNames) {
		String[] values = new String[fileNames.length];
		for (int i = 0; i < fileNames.length; i++) {
			values[i] = getClassProperty(fileNames[i], t) + "";
		}
		return values;
	}

	/**
	 * 获取指定字段名的值
	 * 
	 * @param <T>
	 * @param t
	 * @param fileNames
	 * @return
	 * @author：redlwb add 2012-9-25
	 */
	public static <T extends Serializable> Object[] getPropertyValuesRsObject(T t, String[] fileNames) {
		Object[] values = new String[fileNames.length];
		for (int i = 0; i < fileNames.length; i++) {
			values[i] = getClassProperty(fileNames[i], t);
		}
		return values;
	}

	/**
	 * 判断一个字符窜是否为日期
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

	// Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
	public static Map<String, Object> transBean2Map(Object obj) {
		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(obj);

					map.put(key.toUpperCase(), value);
				}

			}
		} catch (Exception e) {
			logger.error("transBean2Map Error {}" ,e);
		}
		return map;

	}
}
