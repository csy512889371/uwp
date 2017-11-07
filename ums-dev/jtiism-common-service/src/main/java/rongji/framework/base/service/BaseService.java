package rongji.framework.base.service;

import java.io.Serializable;
import java.util.List;

import rongji.framework.util.Page;
import rongji.framework.util.ParamRequest;

/**
 * Service - 基类
 * 
 * @version 1.0
 */
public interface BaseService<T> {

	/**
	 * 
	 * @Title: save
	 * 
	 * @Description: 保存实体对象
	 * 
	 * @param entity 实体对象
	 */
	void save(T entity);

	/**
	 * 
	 * @Title: update
	 * 
	 * @Description: 更新实体对象
	 * 
	 * @param 实体对象
	 * 
	 * @return 实体对象
	 */
	void update(T entity);

	void saveOrUpdate(T entity);

	Object merge(T entity);

	void delete(T entity);

	void delete(Serializable[] ids);
	
	void delete(Serializable id);
	
	/**
	 * 直接到数据库查询出一个具体的id实体
	 * @param id
	 * @return
	 */
	T find(Serializable id);
	
	List<T> findAllByParamRequest(ParamRequest pageRequest);

	List<T> findByPropertysAndOrders(String[] joinEntitys,
			String[] propertyNames, Object[] values, String orderCol);

	Page<T> findAllForPage(ParamRequest pageRequest);

	boolean isUnique(T entity, String uniquePropertyNames);

	List<T> findByPropertys(String[] propertyName, Object[] value);

	T findByPropertysForUniqueResult(String[] propertyName, Object[] value);
	/**
	 * 先虚拟一个代理类。正在调用具体内容在查询数据库
	 * @param id
	 * @return
	 */
	T load(Serializable id);
	
	List<T> findAll();
	
	
	public boolean isPropertyUnique(final String propertyName, final Object newValue, final Object oldValue);
	

}