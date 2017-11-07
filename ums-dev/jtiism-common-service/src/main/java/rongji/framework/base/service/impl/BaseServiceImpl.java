package rongji.framework.base.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import rongji.framework.base.dao.GenericDao;
import rongji.framework.base.service.BaseService;
import rongji.framework.util.Page;
import rongji.framework.util.ParamRequest;

public abstract class BaseServiceImpl<T> implements BaseService<T> {


	@Autowired(required=false)
	protected GenericDao<T> baseDao;
	@Override
	public void save(T entity){
		baseDao.save(entity);
	}
	@Override
	public void update(T entity){
		baseDao.update(entity);
	}
	@Override
	public void saveOrUpdate(T entity){
		baseDao.saveOrUpdate(entity);
	}
	@Override
	public Object merge(T entity){
		return baseDao.merge(entity);
	}
	
	@Override
	public T load(Serializable id){
		return baseDao.find(id);
	}
	@Override
	public T find(Serializable id){
		return baseDao.find(id);
	}
	@Override
	public List<T> findAllByParamRequest(ParamRequest pageRequest){
		return baseDao.findAllByParamRequest(pageRequest);
	}
	@Override
	public List<T> findByPropertysAndOrders(String[] joinEntitys,String[] propertyNames, Object[] values, String orderCol){
		return baseDao.findByPropertysAndOrders(joinEntitys, propertyNames, values, orderCol);
	}
	@Override
	public Page<T> findAllForPage(ParamRequest pageRequest){
		return baseDao.findAllForPage(pageRequest);
	}
	
	@Override
	public List<T> findAll(){
		return baseDao.findAll();
	}
	@Override
	public List<T> findByPropertys(String[] propertyName,Object[] value){
		return baseDao.findByPropertys(propertyName, value);
	}
	@Override
	public T findByPropertysForUniqueResult(String[] propertyName,Object[] value){
		List<T> results =  baseDao.findByPropertys(propertyName, value);
		if(results !=null && !results.isEmpty()){
			return results.get(0);
		}
		return null;
	}
	@Override
	public boolean isUnique(T entity, String uniquePropertyNames){
		return baseDao.isUnique(entity, uniquePropertyNames);
	}
	
	@Override
	public void delete(Serializable[] ids) {
		baseDao.delete(ids);
	}
	
	@Override
	public void delete(Serializable id) {
		baseDao.delete(id);
	}
	
	@Override
	public void delete(T entity){
		baseDao.delete(entity);
	}
	
	@Override
	public boolean isPropertyUnique(final String propertyName, final Object newValue, final Object oldValue) {
		return baseDao.isPropertyUnique(propertyName, newValue, oldValue);
	}
	
}
