package rongji.framework.base.service.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rongji.framework.base.dao.mapper.BulkMapper;
import rongji.framework.base.dao.mapper.MulBulkMapper;
import rongji.framework.util.FastjsonUtils;
import rongji.framework.util.Page;
import rongji.framework.util.ParamRequest;

public class MulBulkMapperConverter<T> {
	
	/**
	 * @Title: 将resultList 里面的 entity 转换为对应的map<String, String>
	 */
	public void convertResultToMap(Page<T> page, BulkMapper<T> bulkMapper) {
		// 1. 清除
		page.getResultMapList().clear();
		ParamRequest pageRequest = page.getPageRequest();
		List<T> resultList = page.getResult();
		if (resultList != null && !resultList.isEmpty()) {
			for (T entity : resultList) {
				Map<String, Object> objectMap = FastjsonUtils.toMap(entity);

				// 3.附加属性
				if (bulkMapper != null && entity != null) {
					if (pageRequest == null) {
						bulkMapper.mapBulk(entity, objectMap, null);
					} else {
						bulkMapper.mapBulk(entity, objectMap, pageRequest.getRequestMap());
					}
				}

				page.getResultMapList().add(objectMap);
			}
		}

	}

	
	/**
	 * 
	 * @Title: convertMulResultToMap
	 * @Description: (将resultList 里面的 entity 转换为对应的Map<String, Object>)
	 * @param bulkMapper
	 * @param userId
	 *            如果userId = null,则查询当前登录用户ID
	 * @return void 返回类型
	 * @throws
	 * @author Administrator
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void convertMulResultToMap(Page<T> page, MulBulkMapper bulkMapper, Integer userId) {
		// 1. 清除
		page.getResultMapList().clear();

		Iterator iterator = page.getResult().iterator();
		ParamRequest pageRequest = page.getPageRequest();
		
		// hql join 查询 返回 多个对象
		while (iterator.hasNext()) {
			Object object = iterator.next();
			Object[] entitys = null;
			T mainEntity = null;
			if (object instanceof Object[]) {
				entitys = (Object[]) object;
				mainEntity = (T) entitys[0];
			} else {
				mainEntity = (T) object;
				entitys = new Object[] { object };
			}

			Map<String, Object> objectMap = FastjsonUtils.toMap(mainEntity);

			// 3.附加属性
			if (bulkMapper != null && mainEntity != null) {
				if (pageRequest == null) {
					bulkMapper.mapBulk(entitys, objectMap, null, userId);
				} else {
					bulkMapper.mapBulk(entitys, objectMap, pageRequest.getRequestMap(), userId);
				}
			}

			page.getResultMapList().add(objectMap);
		}

	}
	
	
	/**
	 * 
	 * @Title: convertMulResultToMap
	 * @Description: (将resultList 里面的 entity 转换为对应的List<Map<String, Object>>)
	 * @param bulkMapper
	 * @param resultList
	 * @param userId
	 *            如果userId = null,则查询当前登录用户ID
	 * @return void 返回类型
	 * @throws
	 * @author Administrator
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> convertMulResultToMap(MulBulkMapper bulkMapper, List<T> resultList, Integer userId) {
		// 1. 清除
		List<Map<String, Object>> resultMapList = new ArrayList<Map<String, Object>>();

		Iterator iterator = resultList.iterator();

		// hql join 查询 返回 多个对象
		while (iterator.hasNext()) {
			Object object = iterator.next();
			Object[] entitys = null;
			T mainEntity = null;
			if (object instanceof Object[]) {
				entitys = (Object[]) object;
				mainEntity = ((T) entitys[0]);
			} else {
				mainEntity = (T) object;
				entitys = new Object[] { object };
			}

			Map<String, Object> objectMap = FastjsonUtils.toMap(mainEntity);

			// 3.附加属性
			if (bulkMapper != null && mainEntity != null) {
				bulkMapper.mapBulk(entitys, objectMap, null, null);
			}

			resultMapList.add(objectMap);
		}
	
		return resultMapList;

	}
	
	/**
	 * @Title: 将list<T> 里面的 entity 转换为对应的map<String, String> 返回
	 */
	public List<Map<String, Object>> convertListResultToMap(BulkMapper<T> bulkMapper, List<T> resultList) {

		List<Map<String, Object>> resultMapList = new ArrayList<Map<String, Object>>();

		if (resultList != null && !resultList.isEmpty()) {
			for (T result : resultList) {

				// 2.先将基本属性 转换为map
				Map<String, Object> objectMap = FastjsonUtils.toMap(result);

				// 3.附加属性
				if (bulkMapper != null || result == null) {
					bulkMapper.mapBulk(result, objectMap, null);
				}
				resultMapList.add(objectMap);
			}
		}

		return resultMapList;
	}

}
