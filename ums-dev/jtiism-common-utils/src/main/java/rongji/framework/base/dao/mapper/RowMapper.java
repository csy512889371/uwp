package rongji.framework.base.dao.mapper;


public interface RowMapper<T> {
	
	public T fromColumn(final Object[] obj);

}
