package rongji.framework.base.dao.mapper;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 于page配合 填充数据
 * 
 * @Title: BulkMapper
 */
public abstract class BulkMapper<T> {

	//private static final String PRE_NAME = "Name";

	private static final Logger logger = LoggerFactory.getLogger(BulkMapper.class);

	public abstract void mapBulk(T entity, Map<String, Object> paramMap, Map<String, Object> requestMap);

	/*
	 * 加载 延时加载的collection 数据
	 */
	public void loadCollectionInfo(Object entity, Map<String, Object> paramMap) {
		try {
			Field[] fields = entity.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				if (Collection.class.isAssignableFrom(field.getType())) {
					paramMap.put(field.getName(), field.get(entity));
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/*public void loadIndexInfo(Object entity, Map<String, Object> paramMap, boolean isNeedSetInfo) {
		try {
			String tableName = entity.getClass().getSimpleName();
			Field[] fields = entity.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				if (Collection.class.isAssignableFrom(field.getType())) {
					buildSetInfo(entity, paramMap, isNeedSetInfo, field);
				} else {
					buildCodeName(entity, paramMap, tableName, field);
				}

			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void buildCodeName(Object entity, Map<String, Object> paramMap, String tableName, Field field) throws IllegalAccessException {

		DataDictService dataDictService = (DataDictService) SpringUtils.getBean("dataDictServiceImpl");

		Object value = field.get(entity);
		if (value == null) {
			return;
		}

		ZtreeDictNote dataDict = dataDictService.findByFieldInfo(tableName, field.getName(), String.valueOf(value));
		if (dataDict == null) {
			return;
		}

		String codeName = dataDict.getCodeName();
		if (!StringUtil.isEmpty(codeName)) {
			paramMap.put(field.getName() + PRE_NAME, codeName);
		}
	}

	@SuppressWarnings("rawtypes")
	private void buildSetInfo(Object entity, Map<String, Object> paramMap, boolean isNeedSetInfo, Field field) throws IllegalAccessException {
		if (!isNeedSetInfo) {
			return;
		}
		Collection collection = (Collection) field.get(entity);
		if (collection == null || collection.isEmpty()) {
			return;
		}

		for (Object info : collection) {
			if (info == null) {
				continue;
			}

			Map<String, Object> setInfoMap = FastjsonUtils.toMap(info);
			paramMap.put(field.getName(), setInfoMap);
		}
	}*/
}
