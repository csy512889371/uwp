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
public abstract class MulBulkMapper {

	// private static final String PRE_NAME = "Name";

	private static final Logger logger = LoggerFactory.getLogger(MulBulkMapper.class);

	public abstract void mapBulk(Object[] entitys, Map<String, Object> paramMap, Map<String, Object> requestMap, Integer userId);

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

}
