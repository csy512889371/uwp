package rongji.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsDateJsonValueProcessor;
import net.sf.json.util.JSONUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtils {

	private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

	private static void setDataFormat() {
		// 设定日期转换格式
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" }));
	}

	/**
	 * 
	 * @Title: beanToJson
	 * @Description: 将数据对象转换成JSON格式字符串
	 * @param object
	 *            POJO、Collection或Object[]
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	public static String beanToJson(Object object) {
		String jsonString = null;
		// 日期值处理器
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JsDateJsonValueProcessor());
		if (object != null) {
			if (object instanceof Collection || object instanceof Object[]) {
				jsonString = JSONArray.fromObject(object, jsonConfig).toString();
			} else {
				jsonString = JSONObject.fromObject(object, jsonConfig).toString();
			}
		}
		return jsonString == null ? "{}" : jsonString;
	}

	/**
	 * 
	 * @Title: mapToJson
	 * @Description: 将Map<?, ?>转成JSON格式字符串
	 * @param map
	 *            map对象
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	public static String mapToJson(Map<?, ?> map) {
		return beanToJson(map);
	}

	/**
	 * 
	 * @Title: listToJson
	 * @Description: 将Collection数据对象转换成JSON格式字符串
	 * @param coll
	 *            Collection对象
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	public static String listToJson(Collection<?> collection) {
		return beanToJson(collection);
	}

	/**
	 * 
	 * @Title: arrayToJson
	 * @Description: 将Object数组数据对象转换成JSON格式字符串
	 * @param objects
	 *            Object对象数组
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	public static String arrayToJson(Object[] objects) {
		return beanToJson(objects);
	}

	/**
	 * 
	 * @Title: jsonToBean
	 * @Description: 将JSON格式字符串转换成Java对象
	 * @param jsonString
	 *            JSON格式字符串
	 * @param beanClass
	 *            Java对象Class
	 * @return
	 * @return Object 返回类型
	 * @throws
	 * @author Administrator
	 */
	public static Object jsonToBean(String jsonString, Class<?> beanClass) {
		JSONObject jsonObject = null;
		try {
			setDataFormat();
			jsonObject = JSONObject.fromObject(jsonString);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return JSONObject.toBean(jsonObject, beanClass);
	}

	/**
	 * 
	 * @Title: jsonToBean
	 * @Description: 将JSON格式字符串转换成Java对象
	 * @param jsonString
	 *            JSON格式字符串
	 * @param beanClass
	 *            Java对象Class
	 * @param classMap
	 *            包含的对象集合中的Java对象Class
	 * @return
	 * @return Object 返回类型
	 * @throws
	 * @author Administrator
	 */
	public static Object jsonToBean(String jsonString, Class<?> beanClass, Map<?, ?> classMap) {
		JSONObject jsonObject = null;
		try {
			setDataFormat();
			jsonObject = JSONObject.fromObject(jsonString);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return JSONObject.toBean(jsonObject, beanClass, classMap);
	}

	/**
	 * bean 存在 Collection 时
	 * @Title: jsonToMulBean
	 * @Description: 
	 * @param 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object jsonToMulBean(String jsonString, Class<?> beanClass) {
		JSONObject jsonObject = null;
		Map<String, Class> classMap = new HashMap<String, Class>();
		try {
			setDataFormat();
			jsonObject = JSONObject.fromObject(jsonString);

			Field[] fields = beanClass.getDeclaredFields();
			for (Field field : fields) {
				String fieldName = field.getName();
				if (Collection.class.isAssignableFrom(field.getType())) {
					Type type = field.getGenericType();
					// 这样判断type 是不是参数化类型。 如Collection<String>就是一个参数化类型。
					if (type instanceof ParameterizedType) {
						// 获取类型的类型参数类型。 你可以去查看jdk帮助文档对ParameterizedType的解释。
						Class clazz = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
						classMap.put(fieldName, clazz);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return JSONObject.toBean(jsonObject, beanClass, classMap);
	}

	/**
	 * 
	 * @Title: jsonToArray
	 * @Description: 将JSON格式字符串转换成Java对象数组
	 * @param jsonString
	 *            JSON格式字符串
	 * @param beanClass
	 *            Java对象Class
	 * @return
	 * @return Object[] 返回类型
	 * @throws
	 * @author Administrator
	 */
	public static Object[] jsonToArray(String jsonString, Class<?> beanClass) {
		setDataFormat();
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Object[] objects = new Object[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			objects[i] = JSONObject.toBean(jsonObject, beanClass);
		}
		return objects;
	}

	/**
	 * 
	 * @Title: jsonToArray
	 * @Description: 将JSON格式字符串转换成Java对象数组
	 * @param jsonString
	 *            JSON格式字符串
	 * @param beanClass
	 *            Java对象Class
	 * @param classMap
	 *            包含的对象集合中的Java对象Class
	 * @return
	 * @return Object[] 返回类型
	 * @throws
	 * @author Administrator
	 */
	public static Object[] jsonToArray(String jsonString, Class<?> beanClass, Map<?, ?> classMap) {
		setDataFormat();
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Object[] objects = new Object[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			objects[i] = JSONObject.toBean(jsonObject, beanClass, classMap);
		}
		return objects;
	}

	/**
	 * 
	 * @Title: jsonToList
	 * @Description: 将JSON格式字符串转换成Java对象集合
	 * @param jsonString
	 *            JSON格式字符串
	 * @param beanClass
	 *            Java对象Class
	 * @return
	 * @return List<Object> 返回类型
	 * @throws
	 * @author Administrator
	 */
	public static List<Object> jsonToList(String jsonString, Class<?> beanClass) {
		setDataFormat();
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		List<Object> list = new ArrayList<Object>();
		for (Iterator<?> iterator = jsonArray.iterator(); iterator.hasNext();) {
			JSONObject jsonObject = (JSONObject) iterator.next();
			list.add(JSONObject.toBean(jsonObject, beanClass));
		}
		return list;
	}

	/**
	 * 
	 * @Title: jsonToList
	 * @Description: 将JSON格式字符串转换成Java对象集合
	 * @param jsonString
	 *            JSON格式字符串
	 * @param beanClass
	 *            Java对象Class
	 * @param classMap
	 *            包含的对象集合中的Java对象Class
	 * @return
	 * @return List<Object> 返回类型
	 * @throws
	 * @author Administrator
	 */
	public static List<Object> jsonToList(String jsonString, Class<?> beanClass, Map<?, ?> classMap) {
		setDataFormat();
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		List<Object> list = new ArrayList<Object>();
		for (Iterator<?> iter = jsonArray.iterator(); iter.hasNext();) {
			JSONObject jsonObject = (JSONObject) iter.next();
			list.add(JSONObject.toBean(jsonObject, beanClass, classMap));
		}
		return list;
	}

	/**
	 * 
	 * @Title: jsonToMap
	 * @Description: 将JSON格式字符串转换成Map对象
	 * @param jsonString
	 *            JSON格式字符串
	 * @return
	 * @return Map<String, Object> 返回类型
	 * @throws
	 * @author Administrator
	 */
	public static Map<String, Object> jsonToMap(String jsonString) {
		setDataFormat();
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Map<String, Object> map = new HashMap<String, Object>();

		for (Iterator<?> iter = jsonObject.keys(); iter.hasNext();) {
			String key = (String) iter.next();
			map.put(key, jsonObject.get(key));
		}
		return map;
	}

	/**
	 * 
	 * @Title: jsonToArray
	 * @Description: 将JSON格式字符串转换成Object对象数组
	 * @param jsonString
	 *            JSON格式字符串
	 * @return
	 * @return Object[] 返回类型
	 * @throws
	 * @author Administrator
	 */
	public static Object[] jsonToArray(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return jsonArray.toArray();
	}

}
