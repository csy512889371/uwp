package rongji.framework.base.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.Type;

import rongji.framework.base.dao.mapper.RowMapper;
import rongji.framework.util.Page;
import rongji.framework.util.ParamRequest;

/**
 * GenericDao DAO层泛型接口,提供基础的DAO功能
 * 
 * @author chenxiang 实体状态说明： 持久化状态：已经被持久化了且与当前Session关联的实体状态。 临时状态：没有被持久化过的实体状态。
 *         游离状态：已经被持久化，但是没有与当前Session关联的实体对象，且有相同标识的对象与当前Session关联。
 *
 * @param <T>
 *            实体类
 */
public interface GenericDao<T> {
	public Session getSession();

	/**
	 * 持久化一个实体
	 * 
	 * @param entity
	 *            实体对象
	 */
	public void save(T entity);

	/**
	 * 持久化多个实体。
	 * 
	 * @param entities
	 *            处于临时状态的实体的集合。
	 */
	// public void save(Collection<T> entities);

	/**
	 * 批量保存数据
	 * 
	 * @param entitys
	 *            要持久化的临时实体对象集合
	 */
	public void batchSave(List<T> entities, int batchSize);

	/**
	 * 更新一个实体对象
	 * 
	 * @param entity
	 *            实体对象
	 */
	public void update(T entity);

	/**
	 * 更新多个实体。
	 * 
	 * @param entities
	 *            处于持久化状态的实体的集合。
	 */
	// public void update(Collection<T> entities);

	/**
	 * 批量更新数据
	 * 
	 * @param entitys
	 *            要持久化的临时实体对象集合
	 */
	// public void batchUpdate(List<T> entities,int batchSize);

	/**
	 * 持久化或者更新实体。
	 * 
	 * @param entity
	 *            处于临时或者持久化状态的实体。
	 */
	public void saveOrUpdate(T entity);

	/**
	 * 持久化或者更新多个实体。
	 * 
	 * @param entities
	 *            处于临时或者持久化状态的实体的集合。
	 */
	// public void saveOrUpdate(Collection<T> entities);

	/**
	 * 更新处于游离状态的实体，更新后实体对象仍然处于游离状态。
	 * 
	 * @param entity
	 *            处于游离状态的实体。
	 */
	public Object merge(T entity);

	/**
	 * 保存处于游离状态的多个实体，更新后实体对象仍然处于游离状态。
	 * 
	 * @param entities
	 *            处于游离状态的实体的集合。
	 */
	// public void merge(Collection<T> entities);

	/**
	 * 删除实体主键id标识的实体。
	 * 
	 * @param ids
	 *            传入数组 ， 不可传入collection
	 */
	public void delete(Serializable[] ids);
	
	public void delete(Serializable id);

	/**
	 * 删除一个实体对象
	 * 
	 * @param entity
	 *            实体对象
	 */
	public void delete(T entity);

	/**
	 * 删除多个持久化的实体。
	 * 
	 * @param entities
	 *            处于持久化状态的实体的集合。
	 */
	// public void delete(Collection<T> entities);

	/**
	 * 批量删除
	 * 
	 * @param propertyNames
	 * @param values
	 */
	public void bulkDelete(String[] propertyNames, Object[] values);

	/**
	 * 查找一个虚拟的代理实体
	 * 
	 * @param id
	 *            主键值
	 * @return 代理类，具体使用时才查库
	 */
	public T load(Serializable id);

	/**
	 * 按照实体类型和实体唯一标识查询实体。
	 * 
	 * @param id
	 *            主键值
	 * @return 记录实体对象，如果没有符合主键条件的记录，则返回null
	 */
	public T find(Serializable id);

	/**
	 * 按照实体类型和实体唯一标识查询实体，并锁定该实体对象，直到事务结束。
	 * 
	 * @param id
	 * @param lockOptions
	 * @return
	 */
	public T findAndLock(Serializable id, LockOptions lockOptions);

	/**
	 * 按照泛型的实体类型查询得到所有持久化实体。
	 * 
	 * @return 所有持久化实体的集合
	 */
	public List<T> findAll();
	
	/** 
     *  获取全部对象, 支持按属性行序. 
     */
	public List<T> findAll(String orderByProperty, boolean isAsc);

	/**
	 * 刷新持久化实体到数据库
	 * 
	 * @param entity
	 *            处于持久化状态的实体。
	 */
	public void refresh(T entity);

	/**
	 * 根据多对属性值与属性类型，查询出所有满足条件的持久化实体
	 * 
	 * @param hql
	 *            hql语句
	 * @param values
	 *            属性值
	 * @param types
	 *            属性类型
	 * @return 所有持久化实体的集合
	 */
	public List<T> findByHQL(String hql, Object[] values, Type[] types);

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	public <E> List<E> findByHQL(final String hql, final Map<String, ?> values);

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	public <E> List<E> findByHQL(final String hql, final Object... values);

	/**
	 * 按SQL查询对象列表,并将结果集转换成指定的对象列表
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	public <E> List<E> find(Class<E> classType, String hql, Object[] values, Type[] types);

	public <E> List<E> findBySQL(final String sql, final Object... values);

	/**
	 * 按SQL查询对象列表.
	 * 
	 * @param sql
	 *            SQL查询语句
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	public <E> List<E> findBySQL(final String sql, final Map<String, ?> values);

	/**
	 * 按照泛型的实体类型，分页查询得到所有持久化实体。
	 * 
	 * @return 所有持久化实体的集合
	 */
	public Page<T> findAllForPage(String hql, Object[] values, Type[] types);

	/**
	 * 查询出所有的持久化实体，并排序
	 * 
	 * @param orderCol
	 *            排序字段 (包含升降序)
	 * @return 所有持久化实体的集合
	 */
	public List<T> findByOrder(String orderCol);

	/**
	 * 根据属性名称与值，查询出所有满足条件的持久化实体，并排序
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性值
	 * @param orderCol
	 *            排序字段 (包含升降序)
	 * @return 所有持久化实体的集合
	 */
	public List<T> findByPropertyAndOrder(String propertyName, Object value, String orderCol);

	/**
	 * 根据属性名称与值，查询出所有满足条件的持久化实体，并排序
	 * 
	 * @param joinEntitys
	 *            连接查询的实体名称
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性值
	 * @param orderCol
	 *            排序字段 (包含升降序)
	 * @return 所有持久化实体的集合
	 */
	public List<T> findByPropertysAndOrders(String[] joinEntitys, String[] propertyNames, Object[] values, String orderCol);

	/**
	 * 根据多对属性名称与值，查询出所有满足条件的持久化实体，并排序
	 * 
	 * @param propertyNames
	 *            属性名称
	 * @param values
	 *            属性值
	 * @param orderCol
	 *            排序字段 (包含升降序)
	 * @return 所有持久化实体的集合
	 */
	public List<T> findByPropertysAndOrder(String[] propertyNames, Object[] values, String orderCol);

	/**
	 * 根据多对属性名称与值，查询出所有满足条件的持久化实体，并排序
	 * 
	 * @param joinEntitys
	 *            连接查询的实体名称
	 * @param propertyNames
	 *            属性名称
	 * @param values
	 *            属性值
	 * @param types
	 *            属性类型
	 * @param includeNull
	 *            当字段值为空，是否组装为空条件
	 * @param orderCol
	 *            排序字段 (包含升降序)
	 * @return 所有持久化实体的集合
	 */
	public List<T> findByPropertysAndOrder(String[] joinEntitys, String[] propertyNames, Object[] values, org.hibernate.type.Type[] types, boolean includeNull, String orderCol);

	/**
	 * 根据属性名称与值，查询出所有满足条件的持久化实体
	 * 
	 * @param propertyName
	 *            属性名称
	 * @param value
	 *            属性值
	 * @return 所有持久化实体的集合
	 */
	public List<T> findByProperty(String propertyName, Object value);

	/**
	 * 根据多对属性名称与值，查询出所有满足条件的持久化实体
	 * 
	 * @param propertyNames
	 *            属性名称
	 * @param values
	 *            属性值
	 * @return 所有持久化实体的集合
	 */
	public List<T> findByPropertys(String[] propertyNames, Object[] values);

	/**
	 * 按照泛型的实体类型 和查询条件，查询得到所有持久化实体。
	 * 
	 * @param paramRequest
	 * @return
	 */
	public List<T> findAllByParamRequest(final ParamRequest paramRequest);

	/**
	 * 按照泛型的实体类型，分页查询得到所有持久化实体。
	 * 
	 * @return 所有持久化实体的集合
	 */
	public Page<T> findAllForPage(final ParamRequest pageRequest);

	/**
	 * @param clazz
	 *            新对象类型
	 * @param queryName
	 *            新对象的查询字段
	 * @return 新组合的对象
	 */
	public <E> List<E> findByVo(Class<E> clazz, String queryName, String[] joinEntitys, String[] propertyNames, Object[] values, org.hibernate.type.Type[] types, boolean includeNull, String orderCol);

	/**
	 * 判断对象某些属性的值在数据库中是否唯一.
	 * 
	 * @param uniquePropertyNames在POJO里不能重复的属性列表
	 *            ,以逗号分割 如"name,loginid,password"
	 */
	public boolean isUnique(T entity, String uniquePropertyNames);

	/**
	 * 执行存储过程 使用Spring JDBC 来调用存储过程
	 * 
	 * @param spName
	 *            存储过程名称
	 * @param parameters
	 *            存储过程参数
	 */
	public List<?> execute(String spName, Map<String, Object> parameters);

	/**
	 * 执行存储过程 使用Spring JDBC 来调用存储过程
	 * 
	 * @param spName
	 *            存储过程名称
	 */
	public List<?> execute(String spName);

	/**
	 * @param sql
	 *            查询的sql语句
	 * @param clazz
	 *            返回类型
	 * @return
	 */
	public <E> E queryObjectByJdbc(String sql, Class<E> clazz);

	/**
	 * 返回一行数据并将改行数据转为Map
	 * 
	 * @param sql
	 *            查询的sql语句
	 * @return
	 */
	public Map<String, Object> queryMapByJdbc(String sql);

	/**
	 * 返回一行数据并将改行数据转为Map
	 * 
	 * @param sql
	 *            查询的sql语句
	 * @param args
	 *            参数值数组
	 * @return
	 */
	public Map<String, Object> queryMapByJdbc(String sql, Object[] args);

	/**
	 * 返回一行数据并将改行数据转为Map
	 * 
	 * @param sql
	 *            查询的sql语句
	 * @param args
	 *            参数值数组
	 * @param argTypes
	 *            参数值类型
	 * @return
	 */
	public Map<String, Object> queryMapByJdbc(String sql, Object[] args, int[] argTypes);

	/**
	 * @param sql
	 *            查询的sql语句
	 * @return
	 */
	public List<Map<String, Object>> queryListByJdbc(String sql);

	/**
	 * @param sql
	 *            查询的sql语句
	 * @param args
	 *            参数值数组
	 * @return
	 */
	public List<Map<String, Object>> queryListByJdbc(String sql, Object[] args);

	/**
	 * @param sql
	 *            查询的sql语句
	 * @param elementType
	 *            返回结果列表中的参数类型
	 * @return
	 */
	public <E> List<E> queryListByJdbc(String sql, Class<E> elementType);

	/**
	 * @param sql
	 *            查询的sql语句
	 * @param args
	 *            参数值数组
	 * @param elementType
	 *            返回结果列表中的参数类型
	 * @return
	 */
	public <E> List<E> queryListByJdbc(String sql, Object[] args, Class<E> elementType);

	/**
	 * @param sql
	 *            查询的sql语句
	 * @param args
	 *            参数值数组
	 * @param argTypes
	 *            参数值类型
	 * @param elementType
	 *            返回结果列表中的参数类型
	 * @return
	 */
	public <E> List<E> queryListByJdbc(String sql, Object[] args, int[] argTypes, Class<E> elementType);

	/**
	 * 可以用于执行任何sql语句,一般用于执行DDL语句
	 * 
	 * @param sql
	 *            sql语句
	 */
	public void executeByJdbc(String sql);

	/**
	 * 执行insert，update，delete等操作
	 * 
	 * @param sql
	 *            sql语句
	 */
	public void updateByJdbc(String sql);

	/**
	 * 执行insert，update，delete等操作
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数值数组
	 * 
	 */
	public void updateByJdbc(String sql, Object... args);

	/**
	 * 执行insert，update，delete等操作
	 * 
	 * @param sql
	 *            sql语句
	 * @param args
	 *            参数值数组
	 * @param argTypes
	 *            参数值类型
	 */
	public void updateByJdbc(String sql, Object[] args, int[] argTypes);

	/**
	 * 执行insert，update，delete等操作
	 * 
	 * @param sql
	 *            sql语句数组
	 */
	public void batchUpdateByJdbc(String[] sql);

	/**
	 * @param sql
	 *            调用的存储过程
	 * @param args
	 *            存储过程参数值数组
	 * @param argTypes
	 *            存储过程参数值类型
	 * @param returnType
	 *            返回类型
	 * @return
	 */
	public <E> Map<String, Object> callProcByJdbc(final String sql, final Object[] args, int[] argTypes, final Class<E> returnType);

	/**
	 * @param sql
	 *            select * from tablename where id = :id and mc like :mc;
	 * @param paramMap
	 *            paramMap.put("id",1);paramMap.put("mc","%变更数%");
	 * @return
	 */
	public List<Map<String, Object>> queryForList(String sql, Map<String, Object> paramMap);

	/**
	 * 
	 * @param sql
	 *            select * from tablename where id = :id and mc like :mc;
	 * @param paramMap
	 *            paramMap.put("id",1);paramMap.put("mc","%变更数%");
	 * @return
	 */
	public Map<String, Object> queryForMap(String sql, Map<String, Object> paramMap);

	/**
	 * 查询在xxx-dynamic.xml中配置的查询语句
	 * 
	 * @param rowMapper
	 * @param queryName
	 *            查询的名称
	 * @param parameters
	 *            参数
	 * @return
	 */
	public List<T> findByNamedQuery(final String queryName, final Map<String, ?> parameters);
	
	/**
	 * 查询在xxx-dynamic.xml中配置的查询语句
	 * 
	 * @param rowMapper
	 * @param queryName
	 *            查询的名称
	 * @param parameters
	 *            参数
	 * @return
	 */
	public String findQueryByNamed(final String queryName, final Map<String, ?> parameters);

	/**
	 * 查询在xxx-dynamic.xml中配置的查询语句
	 * 
	 * @param rowMapper
	 * @param queryName
	 *            查询的名称
	 * @param parameters
	 *            参数
	 * @return
	 */
	public <E> List<E> findByNamedQuery(RowMapper<E> rowMapper, final String queryName, final Map<String, ?> parameters);

	/**
	 * 按HQL查询对象列表，并将对象封装成指定的对象
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	public <E> List<E> findByHQLRowMapper(RowMapper<E> rowMapper, final String hql, final Object... values);

	/**
	 * 按HQL查询对象列表,并将结果集封装成对象列表
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	public <E> List<E> findByHQLRowMapper(RowMapper<E> rowMapper, final String hql, final Map<String, ?> values);

	/**
	 * 按SQL查询对象列表.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	public <E> List<E> findBySQLRowMapper(RowMapper<E> rowMapper, final String sql, final Object... values);

	/**
	 * 按SQL查询对象列表,并将结果集封装成对象列表
	 * 
	 * @param sql
	 *            SQL查询语句
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	public <E> List<E> findBySQLRowMapper(RowMapper<E> rowMapper, final String sql, final Map<String, ?> values);
	
	/** 
     * 初始化对象. 
     * 使用load()方法得到的仅是对象Proxy, 在传到View层前需要进行初始化. 
     * 如果传入entity, 则只初始化entity的直接属性,但不会初始化延迟加载的关联集合和属性. 
     * 如需初始化关联属性,需执行: 
     * Hibernate.initialize(user.getRoles())，初始化User的直接属性和关联集合. 
     * Hibernate.initialize(user.getDescription())，初始化User的直接属性和延迟加载的Description属性. 
     */ 
	public void initProxyObject(Object proxy);
	
	/** 
     * Flush当前Session. 
     */
	public void flush();
	
	 /** 
     * 为Criteria添加distinct transformer. 
     * 预加载关联对象的HQL会引起主对象重复, 需要进行distinct处理. 
     */ 
	public Query distinct(Query query);
	
	/** 
     * 取得对象的主键名. 
     */  
	public String getIdName();
	
	/** 
     * 判断对象的属性值在数据库内是否唯一. 
     *  
     * 在修改对象的情景下,如果属性新修改的值(value)等于属性原来的值(orgValue)则不作比较. 
     */
	public boolean isPropertyUnique(final String propertyName, final Object newValue, final Object oldValue);
	
	public T findUniqueBy(final String propertyName, final Object value);

}
