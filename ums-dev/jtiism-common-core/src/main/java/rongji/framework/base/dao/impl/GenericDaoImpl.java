package rongji.framework.base.dao.impl;

import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import rongji.framework.base.PageContextHolder;
import rongji.framework.base.dao.GenericDao;
import rongji.framework.base.dao.mapper.RowMapper;
import rongji.framework.base.dao.support.DynamicHibernateStatementBuilder;
import rongji.framework.base.dao.support.StatementTemplate;
import rongji.framework.base.exception.ApplicationException;
import rongji.framework.base.pojo.Filter;
import rongji.framework.base.pojo.Filter.Operator;
import rongji.framework.base.pojo.Order;
import rongji.framework.base.pojo.Order.Direction;
import rongji.framework.util.Page;
import rongji.framework.util.ParamRequest;
import rongji.framework.util.ParamRequest.JoinType;
import rongji.framework.util.StringUtil;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Template;

public class GenericDaoImpl<T> implements GenericDao<T>, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(GenericDaoImpl.class);

	/**
	 * 模板缓存
	 */
	protected Map<String, StatementTemplate> templateCache;

	@Resource(name = "dynamicStatementBuilder")
	protected DynamicHibernateStatementBuilder dynamicStatementBuilder;

	@Resource(name = "freeMarkerConfigurer")
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Resource(name = "jdbcTemplate")
	protected JdbcTemplate jdbcTemplate;

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private void setNamedParameterJdbcTemplate(JdbcTemplate jdbcTemplate) {
		if (this.namedParameterJdbcTemplate == null) {
			synchronized (this) {
				this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
			}
		}
	}

	private NamedParameterJdbcTemplate getNJdbcTemplate() {
		if (this.namedParameterJdbcTemplate == null) {
			setNamedParameterJdbcTemplate(jdbcTemplate);
		}
		return this.namedParameterJdbcTemplate;
	}

	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

	public Session getSession() {
		// 事务必须是开启的(Required)，否则获取不到
		return sessionFactory.getCurrentSession();
	}

	public void setDynamicStatementBuilder(DynamicHibernateStatementBuilder dynamicStatementBuilder) {
		this.dynamicStatementBuilder = dynamicStatementBuilder;
	}

	protected Class<T> clazz;

	protected Class<T> getClazz() {
		return clazz;
	}

	@SuppressWarnings("unchecked")
	public GenericDaoImpl() {
		java.lang.reflect.Type type = getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			clazz = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
		}
	}

	public void save(T entity) {
		getSession().save(entity);
	}

	public void batchSave(List<T> entities, int batchSize) {
		if (entities != null && entities.size() > 0) {
			for (int i = 0; i < entities.size(); i++) {
				save(entities.get(i));
				if (batchSize > 0 && (i % batchSize == 0 || i == entities.size() - 1)) {
					// batchSize个对象后才清理缓存，写入数据库
					getSession().flush();
				}
			}
		}
	}

	public void update(T entity) {
		getSession().update(entity);
	}

	public void saveOrUpdate(T entity) {
		getSession().saveOrUpdate(entity);
	}

	public void delete(Serializable[] ids) {
		for (Serializable id : ids) {
			getSession().delete(this.load(id));
		}
	}
	
	public void delete(Serializable id) {
		getSession().delete(this.load(id));
	}

	@SuppressWarnings("unchecked")
	public T find(Serializable id) {
		return (T) getSession().get(this.getClazz(), id);
	}

	@SuppressWarnings("unchecked")
	public T findAndLock(Serializable id, LockOptions lockOptions) {
		T entity = (T) this.getSession().get(this.getClazz(), id, lockOptions);
		return entity;
	}

	public void bulkDelete(String[] propertyNames, Object[] values) {
		StringBuffer buf = new StringBuffer();
		String entityName = getClazz().getSimpleName();
		buf.append("delete " + entityName);// 实体名称
		int len = propertyNames.length;
		for (int i = 0; i < len; i++) {
			if (i == 0)
				buf.append(" WHERE ").append(propertyNames[i]).append(" = :").append(propertyNames[i]);
			else
				buf.append(" and ").append(propertyNames[i]).append(" = :").append(propertyNames[i]);
		}
		Query query = this.getSession().createQuery(buf.toString());
		for (int i = 0; i < len; i++) {
			query.setParameter(propertyNames[i], values[i]);
		}
		query.executeUpdate();
	}

	public Object merge(T entity) {
		return getSession().merge(entity);
	}

	public void merge(Collection<T> entities) {
		for (T entity : entities) {
			merge(entity);
		}
	}

	public void refresh(T entity) {
		getSession().refresh(entity);
	}

	public List<T> findAll() {
		return this.findByPropertysAndOrder(null, null, null, null, false, null);
	}

	/**
	 * 获取全部对象, 支持按属性行序.
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAll(String orderByProperty, boolean isAsc) {
		Criteria c = createCriteria();
		if (isAsc) {
			c.addOrder(org.hibernate.criterion.Order.asc(orderByProperty));
		} else {
			c.addOrder(org.hibernate.criterion.Order.desc(orderByProperty));
		}
		return c.list();
	}

	public List<T> findByOrder(String orderCol) {
		return this.findByPropertysAndOrder(null, null, null, null, false, orderCol);
	}

	public List<T> findByPropertyAndOrder(String propertyName, Object value, String orderCol) {
		return this.findByPropertysAndOrder(null, new String[] { propertyName }, new Object[] { value }, null, false, orderCol);
	}

	public List<T> findByPropertysAndOrders(String[] joinEntitys, String[] propertyNames, Object[] values, String orderCol) {
		return this.findByPropertysAndOrder(joinEntitys, propertyNames, values, null, false, orderCol);
	}

	public List<T> findByPropertysAndOrder(String[] propertyNames, Object[] values, String orderCol) {
		return this.findByPropertysAndOrder(null, propertyNames, values, null, false, orderCol);
	}

	@SuppressWarnings("unchecked")
	public List<T> findByPropertysAndOrder(String[] joinEntitys, String[] propertyNames, Object[] values, org.hibernate.type.Type[] types, boolean includeNull, String orderCol) {
		StringBuffer buf = new StringBuffer();
		String entityName = this.getClazz().getSimpleName();
		String postName = entityName.toLowerCase();
		buf.append("FROM " + entityName + " as " + postName + " ");// 实体名称

		if (joinEntitys != null) {
			for (String je : joinEntitys) {
				buf.append(" left outer join fetch " + postName + "." + je + " " + je + " ");
			}
		}

		if (propertyNames != null && propertyNames.length > 0) {
			for (int i = 0; i < propertyNames.length; i++) {
				if (i == 0)
					buf.append(" WHERE ");
				else
					buf.append(" and ");

				Object value = values[i];
				if (value instanceof String) {
					if (StringUtil.isEmpty((String) value) && includeNull) {
						buf.append(propertyNames[i]).append(" is null ");
					} else {
						if (((String) value).startsWith("%") || ((String) value).endsWith("%")) {
							buf.append(propertyNames[i]).append(" like :").append(encodeParamName(propertyNames[i]));
						} else if (((String) value).indexOf(",") > 0) {
							buf.append(propertyNames[i]).append(" in (:").append(encodeParamName(propertyNames[i])).append(")");
						} else {
							buf.append(propertyNames[i]).append(" = :").append(encodeParamName(propertyNames[i]));
						}
					}
				} else {
					buf.append(propertyNames[i]).append(" = :").append(encodeParamName(propertyNames[i]));
				}
			}
		}

		// 添加排序字段
		if (StringUtil.isNotEmpty(orderCol)) {
			buf.append(" ORDER BY ").append(orderCol);
		}

		Query queryObject = getSession().createQuery(buf.toString());

		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				if (types != null && types[i] != null) {
					queryObject.setParameter(encodeParamName(propertyNames[i]), values[i], types[i]);
				} else if (values[i] instanceof Object[] || values[i] instanceof Collection) {
					if (values[i] instanceof Object[]) {
						queryObject.setParameterList(encodeParamName(propertyNames[i]), (Object[]) values[i]);
					} else {
						queryObject.setParameterList(encodeParamName(propertyNames[i]), (Collection<?>) values[i]);
					}

				} else {
					queryObject.setParameter(encodeParamName(propertyNames[i]), values[i]);
				}
			}
		}
		return queryObject.list();
	}

	/**
	 * 参数指定如 :name 名称中不能有. 有则用此方法替换
	 * 
	 * @param param
	 * @return
	 */
	private String encodeParamName(String param) {
		return getParamName(param);
	}

	public List<T> findByProperty(String propertyName, Object value) {
		return this.findByPropertysAndOrder(new String[] {}, new String[] { propertyName }, new Object[] { value }, null, false, null);
	}

	public List<T> findByPropertys(String[] propertyNames, Object[] values) {
		return this.findByPropertysAndOrder(new String[] {}, propertyNames, values, null, false, null);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAllByParamRequest(final ParamRequest paramRequest) {

		String entityName = this.getClazz().getSimpleName();
		String postName = entityName.toLowerCase();

		// from
		StringBuilder queryString = new StringBuilder("");
		addSelect(paramRequest, entityName, postName, queryString);

		// join
		buildJoin(paramRequest, queryString, postName, paramRequest.getJoinType());

		// where
		addRestrictions(paramRequest, queryString);

		// 排序字段
		addOrders(paramRequest, queryString);

		// group字段
		addGroups(paramRequest, queryString);

		// 创建查询Query
		Query query = this.getSession().createQuery(queryString.toString());

		// set Param value
		addRestrictionsValue(paramRequest, query);

		return query.list();
	}

	public void addOrders(final ParamRequest paramRequest, StringBuilder queryString) {

		List<Order> orderList = null;

		if (StringUtils.isNotEmpty(paramRequest.getOrderProperty()) && paramRequest.getOrderDirection() != null) {
			orderList = new ArrayList<Order>();
			if (paramRequest.getOrderDirection() == Direction.asc) {
				orderList.add(new Order(paramRequest.getOrderProperty(), Direction.asc));
			} else if (paramRequest.getOrderDirection() == Direction.desc) {
				orderList.add(new Order(paramRequest.getOrderProperty(), Direction.desc));
			}
		}

		if (paramRequest.getOrders() != null && !paramRequest.getOrders().isEmpty()) {
			if (orderList == null) {
				orderList = new ArrayList<Order>(paramRequest.getOrders());
			} else {
				orderList.addAll(paramRequest.getOrders());
			}
		}
		if (orderList == null || orderList.isEmpty()) {
			return;
		}
		queryString.append(" ORDER BY ");
		for (Order order : orderList) {
			queryString.append(order.getProperty() + " " + order.getDirection() + ",");
		}
		queryString.deleteCharAt(queryString.length() - 1).append(" ");
	}

	public Page<T> findAllForPage(final ParamRequest paramRequest) {

		String entityName = this.getClazz().getSimpleName();
		String postName = entityName.toLowerCase();

		// from
		StringBuilder queryString = new StringBuilder("");
		addSelect(paramRequest, entityName, postName, queryString);

		StringBuilder countQueryString = new StringBuilder(" FROM ");
		countQueryString.append(entityName + " as " + postName + " ");// 实体名称

		// join
		buildJoin(paramRequest, queryString, postName, paramRequest.getJoinType());
		buildJoin(paramRequest, countQueryString, postName, JoinType.leftOutJoin);

		// where
		addRestrictions(paramRequest, queryString);
		addRestrictions(paramRequest, countQueryString);

		// 排序字段
		addOrders(paramRequest, queryString);

		// group字段
		addGroups(paramRequest, queryString);

		// 创建查询Query
		Query query = this.getSession().createQuery(queryString.toString());
		Query countQuery = createCountQuery(paramRequest, postName, countQueryString);

		// set Param value
		addRestrictionsValue(paramRequest, query);
		addRestrictionsValue(paramRequest, countQuery);

		return executeQueryForPage(paramRequest, query, countQuery);
	}

	public Query createCountQuery(final ParamRequest paramRequest, String postName, StringBuilder countQueryString) {
		String pkName = this.getIdName();
		Query countQuery = null;
		if (JoinType.fetch.equals(paramRequest.getJoinType())) {
			countQuery = this.getSession().createQuery("SELECT count(distinct " + postName + "." + pkName + " ) " + countQueryString.toString());
		} else if (JoinType.leftOutJoin.equals(paramRequest.getJoinType())) {
			countQuery = this.getSession().createQuery("SELECT count( " + postName + "." + pkName + " ) " + countQueryString.toString());
		}
		return countQuery;
	}

	private void addSelect(final ParamRequest paramRequest, String entityName, String postName, StringBuilder queryString) {
		if (StringUtil.isNotEmpty(paramRequest.getSearchParam())) {
			queryString.append("select new ").append(clazz.getSimpleName());
			queryString.append("( ").append(paramRequest.getSearchParam()).append(" ) ");
		} else {
			if (JoinType.fetch.equals(paramRequest.getJoinType())) {
				queryString.append("select ").append(postName);
			}
		}
		queryString.append(" FROM ");
		queryString.append(entityName + " as " + postName + " ");// 实体名称
	}

	private void addGroups(final ParamRequest paramRequest, StringBuilder queryString) {
		if (StringUtil.isNotEmpty(paramRequest.getGroupColumns())) {
			queryString.append(" GROUP BY " + paramRequest.getGroupColumns());
		}
	}

	public void addRestrictionsValue(final ParamRequest paramRequest, Query query) {

		Map<String, Object> equalParamMap = paramRequest.getEqualFilters();
		// 设置参数
		if (equalParamMap != null && equalParamMap.size() > 0) {
			Iterator<String> keys = equalParamMap.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				query.setParameter(getParamName(key), equalParamMap.get(key));
			}
		}

		Map<String, String> likeParamMap = paramRequest.getLikeFilters();
		if (likeParamMap != null && likeParamMap.size() > 0) {
			Iterator<String> keys = likeParamMap.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				String value = "%" + likeParamMap.get(key) + "%";
				query.setParameter(getParamName(key), value);
			}
		}

		Map<String, Object> inParamMap = paramRequest.getInFilters();
		if (inParamMap != null && inParamMap.size() > 0) {
			Iterator<String> keys = inParamMap.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				Object value = inParamMap.get(key);

				// 这里考虑传入的参数是什么类型，不同类型使用的方法不同
				if (value instanceof Collection<?>) {
					query.setParameterList(getParamName(key), (Collection<?>) value);
				} else if (value instanceof Object[]) {
					query.setParameterList(getParamName(key), (Object[]) value);
				} else {
					query.setParameter(getParamName(key), value);
				}
			}
		}

		Map<String, String> notInParamMap = paramRequest.getNotInFilters();
		if (notInParamMap != null && notInParamMap.size() > 0) {
			Iterator<String> keys = notInParamMap.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				String value = notInParamMap.get(key);
				query.setParameter(getParamName(key), value);
			}
		}

		Map<String, Map<String, Object>> rangeParamMap = paramRequest.getRangeFilters();
		if (rangeParamMap != null && rangeParamMap.size() > 0) {
			Iterator<String> keys = rangeParamMap.keySet().iterator();
			while (keys.hasNext()) {
				String paramName = keys.next();
				if (rangeParamMap.get(paramName).size() > 0 && (rangeParamMap.get(paramName).get("startRange") != null || rangeParamMap.get(paramName).get("endRange") != null)) {
					if (rangeParamMap.get(paramName).get("startRange") != null) {
						query.setParameter(getParamName(paramName) + "_startRange", rangeParamMap.get(paramName).get("startRange"));
					}
					if (rangeParamMap.get(paramName).get("endRange") != null) {
						query.setParameter(getParamName(paramName) + "_endRange", rangeParamMap.get(paramName).get("endRange"));
					}
				}
			}
		}

		Map<String, Map<String, Date>> timeParamMap = paramRequest.getTimeFilters();
		// 时间范围条件参数
		if (timeParamMap != null && timeParamMap.size() > 0) {
			Iterator<String> keys = timeParamMap.keySet().iterator();
			while (keys.hasNext()) {
				String paramName = keys.next();
				if (timeParamMap.get(paramName).size() > 0 && (timeParamMap.get(paramName).get("startTime") != null || timeParamMap.get(paramName).get("endTime") != null)) {
					if (timeParamMap.get(paramName).get("startTime") != null) {
						query.setParameter(getParamName(paramName) + "_startTime", timeParamMap.get(paramName).get("startTime"));
					}
					if (timeParamMap.get(paramName).get("endTime") != null) {
						query.setParameter(getParamName(paramName) + "_endTime", timeParamMap.get(paramName).get("endTime"));
					}
				}
			}
		}

		// Filter条件
		addFilterValue(query, paramRequest.getFilters());
	}

	public void addRestrictions(final ParamRequest paramRequest, StringBuilder queryString) {

		// 等于查询条件
		Map<String, Object> equalParamMap = paramRequest.getEqualFilters();
		boolean hasWhere = false;
		if (equalParamMap != null && equalParamMap.size() > 0) {
			queryString.append(" WHERE ");
			Iterator<String> keys = equalParamMap.keySet().iterator();
			while (keys.hasNext()) {
				if (hasWhere) {
					queryString.append(" AND ");
				} else {
					hasWhere = true;
				}

				String paramName = keys.next();
				queryString.append(paramName).append(" =:").append(getParamName(paramName));
			}
		}

		// Like查询条件
		Map<String, String> likeParamMap = paramRequest.getLikeFilters();
		if (likeParamMap != null && likeParamMap.size() > 0) {
			// 增加查询条件
			if (!hasWhere) {
				queryString.append(" WHERE ");
			}
			Iterator<String> keys = likeParamMap.keySet().iterator();
			while (keys.hasNext()) {
				if (hasWhere) {
					queryString.append(" AND ");
				} else {
					hasWhere = true;
				}
				String paramName = keys.next();
				queryString.append(paramName).append(" like :").append(getParamName(paramName));
			}
		}

		// In查询条件
		Map<String, Object> inParamMap = paramRequest.getInFilters();
		if (inParamMap != null && inParamMap.size() > 0) {
			// 增加查询条件
			if (!hasWhere) {
				queryString.append(" WHERE ");
			}
			Iterator<String> keys = inParamMap.keySet().iterator();
			while (keys.hasNext()) {
				if (hasWhere) {
					queryString.append(" AND ");
				} else {
					hasWhere = true;
				}
				String paramName = keys.next();
				queryString.append(paramName).append(" in (:").append(getParamName(paramName)).append(")");
			}
		}

		// notIn查询条件
		Map<String, String> notInParamMap = paramRequest.getNotInFilters();
		if (notInParamMap != null && notInParamMap.size() > 0) {
			// 增加查询条件
			if (!hasWhere) {
				queryString.append(" WHERE ");
			}
			Iterator<String> keys = inParamMap.keySet().iterator();
			while (keys.hasNext()) {
				if (hasWhere) {
					queryString.append(" AND ");
				} else {
					hasWhere = true;
				}

				String paramName = keys.next();
				queryString.append(paramName).append(" not in (:").append(getParamName(paramName)).append(")");
			}
		}

		// 范围条件
		Map<String, Map<String, Object>> rangeParamMap = paramRequest.getRangeFilters();
		if (rangeParamMap != null && rangeParamMap.size() > 0) {
			// 增加查询条件
			if (!hasWhere) {
				queryString.append(" WHERE ");
			}
			Iterator<String> keys = rangeParamMap.keySet().iterator();
			while (keys.hasNext()) {
				String paramName = keys.next();
				if (rangeParamMap.get(paramName).size() > 0 && (rangeParamMap.get(paramName).get("startRange") != null || rangeParamMap.get(paramName).get("endRange") != null)) {
					if (hasWhere) {
						queryString.append(" AND ");
					} else {
						hasWhere = true;

					}
					boolean isHasStart = false;
					if (rangeParamMap.get(paramName).get("startRange") != null) {
						isHasStart = true;
						queryString.append(paramName).append(" >= :").append(getParamName(paramName)).append("_startRange");
					}

					if (rangeParamMap.get(paramName).get("endRange") != null) {
						if (isHasStart) {
							queryString.append(" and ");
						}
						queryString.append(paramName).append(" <= :").append(getParamName(paramName)).append("_endRange");
					}
				}
			}
		}

		// 时间范围条件
		Map<String, Map<String, Date>> timeParamMap = paramRequest.getTimeFilters();
		if (timeParamMap != null && timeParamMap.size() > 0) {
			// 增加查询条件
			if (!hasWhere) {
				queryString.append(" WHERE ");
			}
			Iterator<String> keys = timeParamMap.keySet().iterator();
			while (keys.hasNext()) {
				String paramName = keys.next();
				if (timeParamMap.get(paramName).size() > 0 && (timeParamMap.get(paramName).get("startTime") != null || timeParamMap.get(paramName).get("endTime") != null)) {
					if (hasWhere) {
						queryString.append(" AND ");
					} else {
						hasWhere = true;
					}
					boolean isHasStart = false;
					if (timeParamMap.get(paramName).get("startTime") != null) {
						isHasStart = true;
						queryString.append(paramName).append(" >= :").append(getParamName(paramName)).append("_startTime");
					}
					if (timeParamMap.get(paramName).get("endTime") != null) {
						if (isHasStart) {
							queryString.append(" and ");
						}
						queryString.append(paramName).append(" <= :").append(getParamName(paramName)).append("_endTime");
					}
				}
			}
		}

		// Filter条件
		addFilterRestrictions(hasWhere, queryString, paramRequest.getFilters());
	}

	private String getParamName(String paramName) {
		return paramName.replaceAll("\\.", "");
	}

	private void buildJoin(final ParamRequest pageRequest, StringBuilder queryString, String postName, JoinType joinType) {

		Set<String> joinEntitySet = null;

		List<Filter> filterList = pageRequest.getFilters();
		String[] joinEntitys = pageRequest.getJoinEntitys();

		if (filterList != null && !filterList.isEmpty()) {
			if (joinEntitySet == null) {
				joinEntitySet = new HashSet<String>();
			}

			for (Filter filter : filterList) {
				if (StringUtils.isNotEmpty(filter.getJoinEntity())) {
					joinEntitySet.add(filter.getJoinEntity());
				}
			}
		}

		if (joinEntitys != null) {
			if (joinEntitySet == null) {
				joinEntitySet = new HashSet<String>();
			}

			for (String je : joinEntitys) {
				joinEntitySet.add(je);
			}
		}

		if (joinEntitySet != null && !joinEntitySet.isEmpty()) {
			for (String je : joinEntitySet) {
				if (JoinType.fetch.equals(joinType)) {
					queryString.append("left outer join fetch " + postName + "." + je + " " + je + " ");
				} else if (JoinType.leftOutJoin.equals(joinType)) {
					queryString.append("left outer join " + postName + "." + je + " " + je + " ");
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <E> List<E> findByVo(Class<E> clazz, String queryName, String[] joinEntitys, String[] propertyNames, Object[] values, org.hibernate.type.Type[] types, boolean includeNull, String orderCol) {
		StringBuffer hql = new StringBuffer();
		hql.append("select new ").append(clazz.getSimpleName());
		if (StringUtil.isNotEmpty(queryName)) {
			hql.append("(").append(queryName).append(")");
		} else {
			hql.append("()");
		}
		String entityName = this.getClazz().getSimpleName();
		String postName = entityName.toLowerCase();
		hql.append(" FROM " + entityName + " as " + postName + " ");// 实体名称

		if (joinEntitys != null) {
			for (String je : joinEntitys) {
				hql.append(" left outer join fetch " + postName + "." + je + " " + je + " ");
			}
		}

		int len = propertyNames.length;
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				if (i == 0)
					hql.append(" WHERE ");
				else
					hql.append(" and ");

				Object value = values[i];
				if (value instanceof String) {
					if (StringUtil.isEmpty((String) value) && includeNull) {
						hql.append(propertyNames[i]).append(" is null ");
					} else {
						if (((String) value).startsWith("%") || ((String) value).endsWith("%")) {
							hql.append(propertyNames[i]).append(" like :").append(propertyNames[i]);
						} else {
							hql.append(propertyNames[i]).append(" = :").append(propertyNames[i]);
						}
					}
				} else {
					hql.append(propertyNames[i]).append(" = :").append(propertyNames[i]);
				}
			}
		}

		// 添加排序字段
		if (StringUtil.isNotEmpty(orderCol)) {
			hql.append(" ORDER BY ").append(orderCol);
		}
		Query queryObject = getSession().createQuery(hql.toString());
		queryObject.setParameter(propertyNames[0], values[0], types[0]);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				if (types != null && types[i] != null) {
					queryObject.setParameter(propertyNames[i], values[i], types[i]);
				} else {
					queryObject.setParameter(propertyNames[i], values[i]);
				}
			}
		}
		return queryObject.list();
	}

	public boolean isUnique(T entity, String uniquePropertyNames) {
		Criteria criteria = getSession().createCriteria(this.getClazz()).setProjection(Projections.rowCount());
		String[] nameList = uniquePropertyNames.split(",");
		try {
			// 循环加入唯一列
			for (int i = 0; i < nameList.length; i++) {
				criteria.add(Restrictions.eq(nameList[i], PropertyUtils.getProperty(entity, nameList[i])));
			}
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		}
		return ((Number) criteria.uniqueResult()).intValue() == 1;
	}

	public List<?> execute(String spName, Map<String, Object> parameters) {
		try {
			Session session = getSession().getSessionFactory().getCurrentSession();
			Set<String> set = parameters.keySet();
			StringBuffer proc = new StringBuffer();
			proc.append("{Call ").append(spName).append("(");
			;
			if (parameters != null) {
				for (int i = 0; i < set.size(); i++) {
					if (i == set.size() - 1) {
						proc.append("?");
					} else {
						proc.append("?,");
					}
				}
			}
			proc.append(")}");
			SQLQuery query = session.createSQLQuery(proc.toString());
			for (String key : set) {
				if (parameters.get(key) instanceof String)
					query.setString(key, (String) parameters.get(key));
				if (parameters.get(key) instanceof Short)
					query.setShort(key, (Short) parameters.get(key));
				if (parameters.get(key) instanceof Date)
					query.setDate(key, (Date) parameters.get(key));
				if (parameters.get(key) instanceof Double)
					query.setDouble(key, (Double) parameters.get(key));
				if (parameters.get(key) instanceof Float)
					query.setFloat(key, (Float) parameters.get(key));
				if (parameters.get(key) instanceof Integer)
					query.setInteger(key, (Integer) parameters.get(key));
				if (parameters.get(key) instanceof BigDecimal)
					query.setBigDecimal(key, (BigDecimal) parameters.get(key));
			}
			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public List<?> execute(String spName) {
		try {
			Session session = getSession().getSessionFactory().getCurrentSession();
			StringBuffer proc = new StringBuffer();
			proc.append("{Call ").append(spName).append("()}");
			SQLQuery query = session.createSQLQuery(proc.toString());
			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Page<T> executeQueryForPage(final ParamRequest pageRequest, Query query, Query countQuery) {
		Page<T> page = new Page<T>(pageRequest, ((Number) countQuery.uniqueResult()).intValue());
		if (page.getTotalCount() <= 0) {
			page.setResult(new ArrayList<T>(0));
		} else {
			page.setResult(query.setFirstResult(page.getFirstResult()).setMaxResults(page.getPageSize()).list());
		}
		return page;
	}

	public <E> E queryObjectByJdbc(String sql, Class<E> clazz) {
		return jdbcTemplate.queryForObject(sql, clazz);
	}

	public Map<String, Object> queryMapByJdbc(String sql) {
		return jdbcTemplate.queryForMap(sql);
	}

	public Map<String, Object> queryMapByJdbc(String sql, Object[] args) {
		return jdbcTemplate.queryForMap(sql, args);
	}

	public Map<String, Object> queryMapByJdbc(String sql, Object[] args, int[] argTypes) {
		return jdbcTemplate.queryForMap(sql, args, argTypes);
	}

	public List<Map<String, Object>> queryListByJdbc(String sql) {
		return jdbcTemplate.queryForList(sql);
	}

	public List<Map<String, Object>> queryListByJdbc(String sql, Object[] args) {
		return jdbcTemplate.queryForList(sql, args);
	}

	public <E> List<E> queryListByJdbc(String sql, Class<E> elementType) {
		return jdbcTemplate.queryForList(sql, elementType);
	}

	public <E> List<E> queryListByJdbc(String sql, Object[] args, Class<E> elementType) {
		return jdbcTemplate.queryForList(sql, args, elementType);
	}

	public <E> List<E> queryListByJdbc(String sql, Object[] args, int[] argTypes, Class<E> elementType) {
		return jdbcTemplate.queryForList(sql, args, argTypes, elementType);
	}

	public void executeByJdbc(String sql) {
		this.jdbcTemplate.execute(sql);
	}

	public void updateByJdbc(String sql) {
		this.jdbcTemplate.update(sql);
	}

	public void updateByJdbc(String sql, Object... args) {
		this.jdbcTemplate.update(sql, args);
	}

	public void updateByJdbc(String sql, Object[] args, int[] argTypes) {
		this.jdbcTemplate.update(sql, args, argTypes);
	}

	public void batchUpdateByJdbc(String[] sql) {
		this.jdbcTemplate.batchUpdate(sql);
	}

	public <E> Map<String, Object> callProcByJdbc(final String sql, final Object[] args, int[] argTypes, final Class<E> returnType) {
		List<SqlParameter> params = new ArrayList<SqlParameter>();
		if (args != null && args.length > 0) {
			for (int argType : argTypes) {
				params.add(new SqlParameter(argType));
			}
			params.add(new SqlReturnResultSet("", new ResultSetExtractor<E>() {
				public E extractData(ResultSet rs) {
					try {
						return rs.getObject(1, returnType);
					} catch (SQLException e) {
						logger.error(e.getMessage(), e);
					}
					return null;
				}
			}));
		}
		return jdbcTemplate.call(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection conn) throws SQLException {
				CallableStatement cstmt = conn.prepareCall(sql);
				if (args != null && args.length > 0) {
					for (int i = 1; i <= args.length; i++) {
						cstmt.setObject(i, args[i]);
					}
				}
				return cstmt;
			}
		}, params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T load(Serializable id) {
		return (T) getSession().load(this.getClazz(), id);
	}

	/**
	 * @param sql
	 *            select * from tablename where id = :id and mc like :mc;
	 * @param paramMap
	 *            paramMap.put("bh",1);paramMap.put("mc","%变更数%");
	 * @return
	 */
	public List<Map<String, Object>> queryForList(String sql, Map<String, Object> paramMap) {
		try {
			return (this.getNJdbcTemplate()).queryForList(sql, paramMap);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * 
	 * @param sql
	 *            select * from tablename where id = :id and mc like :mc;
	 * @param paramMap
	 *            paramMap.put("bh",1);paramMap.put("mc","%变更数%");
	 * @return
	 */
	public Map<String, Object> queryForMap(String sql, Map<String, Object> paramMap) {
		try {
			return (this.getNJdbcTemplate()).queryForMap(sql, paramMap);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> findByHQL(String hql, Object[] values, Type[] types) {
		Query query = this.createHQLQuery(hql, values, types);
		return query.list();
	}

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> findByHQL(final String hql, final Map<String, ?> values) {
		return createHQLQuery(hql, values).list();
	}

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> findByHQL(final String hql, final Object... values) {
		return createHQLQuery(hql, values).list();
	}

	/**
	 * 按SQL查询对象列表.
	 * 
	 * @param sql
	 *            SQL查询语句
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> findBySQL(final String sql, final Map<String, ?> values) {
		return createSQLQuery(sql, values).list();
	}

	/**
	 * 按SQL查询对象列表,并将结果集转换成指定的对象列表
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> findBySQL(final String sql, final Object... values) {
		return createSQLQuery(sql, values).list();
	}

	@SuppressWarnings("unchecked")
	public <E> List<E> find(Class<E> classType, String hql, Object[] values, Type[] types) {
		if (hql.indexOf(classType.getSimpleName()) > 0) {
			Query query = this.createHQLQuery(hql, values, types);
			return query.list();
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public Page<T> findAllForPage(String hql, Object[] values, Type[] types) {
		Page<T> page = PageContextHolder.getPageParam();
		StringBuffer countHql = new StringBuffer();
		Query query = this.createHQLQuery(hql, values, types);
		countHql.append("select count(*) ").append(hql.substring(hql.toLowerCase().indexOf("from"), hql.length() - 1));
		Query countQuery = this.createHQLQuery(countHql.toString(), values, types);
		page.setTotalCount(((Number) countQuery.uniqueResult()).intValue());
		if (page.getTotalCount() <= 0) {
			page.setResult(new ArrayList<T>(0));
		} else {
			page.setResult(query.setFirstResult(page.getFirstResult()).setMaxResults(page.getPageSize()).list());
		}
		return page;
	}

	private Query createHQLQuery(String hql, Object[] values, Type[] types) {
		Query query = getSession().createQuery(hql.toString());
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				if (types != null && types[i] != null) {
					query.setParameter(i, values[i], types[i]);
				} else {
					query.setParameter(i, values[i]);
				}
			}
		}
		return query;
	}

	/**
	 * 根据查询HQL与参数列表创建Query对象. 与find()函数可进行更加灵活的操作.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	public Query createHQLQuery(final String hql, final Object... values) {
		Query query = getSession().createQuery(hql);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}

	/**
	 * 根据查询HQL与参数列表创建Query对象. 与find()函数可进行更加灵活的操作.
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	public Query createHQLQuery(final String queryString, final Map<String, ?> values) {
		Query query = getSession().createQuery(queryString);
		if (values != null) {
			query.setProperties(values);
		}
		return query;
	}

	/**
	 * 根据查询SQL与参数列表创建Query对象. 与find()函数可进行更加灵活的操作.
	 * 
	 * @param queryString
	 *            SQL语句
	 * @param values
	 *            命名参数,按名称绑定.
	 */
    public Query createSQLQuery(final String sql, final Map<String, ?> values) {
		Query query = getSession().createSQLQuery(sql);
		if (values != null) {
			query.setProperties(values);
		}
		return query;
	}

	/**
	 * 根据查询SQL与参数列表创建Query对象. 与find()函数可进行更加灵活的操作.
	 * 
	 * @param sqlQueryString
	 *            sql语句
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
    public Query createSQLQuery(final String sqlQueryString, final Object... values) {
		Query query = getSession().createSQLQuery(sqlQueryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i, values[i]);
			}
		}
		return query;
	}

	protected String buildOrderBy(String[] orderBy) {
		if (orderBy == null || orderBy.length == 0) {
			return "";
		}
		StringBuilder orderSql = new StringBuilder(" order by ");
		for (String key : orderBy) {
			orderSql.append(key + " , ");

		}
		orderSql.deleteCharAt(orderSql.lastIndexOf(","));
		return orderSql.toString();
	}

	protected Map<String, Object> getFilterParam(Map<String, Object> queryMap, List<Filter> filters) {
		if (filters == null || filters.isEmpty()) {
			return queryMap;
		}
		for (int i = 0; i < filters.size(); i++) {
			Filter filter = filters.get(i);
			if (filter == null || (StringUtils.isEmpty(filter.getProperty())&&!Operator.sql.equals(filter.getOperator()))) {
				continue;
			}

			// @TODO 加sql处理
			if (Operator.sql.equals(filter.getOperator())) {
				continue;
			}

			String key = filter.getProperty();
			if (StringUtils.isNotEmpty(filter.getJoinEntity())) {
				key = filter.getJoinEntity() + "." + key;
			}

			String paramName = getParamName(key) + i;

			if (filter.getOperator() == Operator.eq && filter.getValue() != null) {
				queryMap.put(paramName, filter.getValue());
			} else if (filter.getOperator() == Operator.ne && filter.getValue() != null) {
				queryMap.put(paramName, filter.getValue());
			} else if (filter.getOperator() == Operator.gt && filter.getValue() != null) {
				queryMap.put(paramName, filter.getValue());
			} else if (filter.getOperator() == Operator.lt && filter.getValue() != null) {
				queryMap.put(paramName, filter.getValue());
			} else if (filter.getOperator() == Operator.ge && filter.getValue() != null) {
				queryMap.put(paramName, filter.getValue());
			} else if (filter.getOperator() == Operator.le && filter.getValue() != null) {
				queryMap.put(paramName, filter.getValue());
			} else if (filter.getOperator() == Operator.like && filter.getValue() != null && filter.getValue() instanceof String) {
				// 设置参数
				String value = "%" + String.valueOf(filter.getValue()) + "%";
				queryMap.put(paramName, value);
			} else if (filter.getOperator() == Operator.notLike && filter.getValue() != null && filter.getValue() instanceof String) {
				// 设置参数
				String value = "%" + String.valueOf(filter.getValue()) + "%";
				queryMap.put(paramName, value);
			} else if (filter.getOperator() == Operator.in && filter.getValue() != null) {
				Object value = filter.getValue();
				if (value instanceof Collection<?>) {
					queryMap.put(paramName, (Collection<?>) value);
				} else if (value instanceof Object[]) {
					queryMap.put(paramName, (Object[]) value);
				} else {
					queryMap.put(paramName, value);
				}
			} else if (filter.getOperator() == Operator.notIn && filter.getValue() != null) {
				Object value = filter.getValue();
				if (value instanceof Collection<?>) {
					queryMap.put(paramName, (Collection<?>) value);
				} else if (value instanceof Object[]) {
					queryMap.put(paramName, (Object[]) value);
				} else {
					queryMap.put(paramName, value);
				}
			} else if (filter.getOperator() == Operator.isNull) {
				// 无参数
			} else if (filter.getOperator() == Operator.isNotNull) {
				// 无参数
			} else if (filter.getOperator() == Operator.between && filter.getValue() != null && filter.getValueTwo() != null) {
				queryMap.put(getParamName(key) + "_fir" + i, filter.getValue());
				queryMap.put(getParamName(key) + "_sec" + i, filter.getValueTwo());
			}
		}
		return queryMap;
	}

	protected void addFilterValue(Query query, List<Filter> filters) {
		if (filters == null || filters.isEmpty()) {
			return;
		}
		for (int i = 0; i < filters.size(); i++) {
			Filter filter = filters.get(i);
			if (filter == null || (StringUtils.isEmpty(filter.getProperty())&&!Operator.sql.equals(filter.getOperator()))) {
				continue;
			}

			// @TODO 加sql处理
			if (Operator.sql.equals(filter.getOperator())) {
				continue;
			}

			String key = filter.getProperty();
			if (StringUtils.isNotEmpty(filter.getJoinEntity())) {
				key = filter.getJoinEntity() + "." + key;
			}

			String paramName = getParamName(key) + i;

			if (filter.getOperator() == Operator.eq && filter.getValue() != null) {
				query.setParameter(paramName, filter.getValue());
			} else if (filter.getOperator() == Operator.ne && filter.getValue() != null) {
				query.setParameter(paramName, filter.getValue());
			} else if (filter.getOperator() == Operator.gt && filter.getValue() != null) {
				query.setParameter(paramName, filter.getValue());
			} else if (filter.getOperator() == Operator.lt && filter.getValue() != null) {
				query.setParameter(paramName, filter.getValue());
			} else if (filter.getOperator() == Operator.ge && filter.getValue() != null) {
				query.setParameter(paramName, filter.getValue());
			} else if (filter.getOperator() == Operator.le && filter.getValue() != null) {
				query.setParameter(paramName, filter.getValue());
			} else if (filter.getOperator() == Operator.like && filter.getValue() != null && filter.getValue() instanceof String) {
				// 设置参数
				String value = "%" + String.valueOf(filter.getValue()) + "%";
				query.setParameter(paramName, value);
			} else if (filter.getOperator() == Operator.notLike && filter.getValue() != null && filter.getValue() instanceof String) {
				// 设置参数
				String value = "%" + String.valueOf(filter.getValue()) + "%";
				query.setParameter(paramName, value);
			} else if (filter.getOperator() == Operator.in && filter.getValue() != null) {
				Object value = filter.getValue();
				if (value instanceof Collection<?>) {
					query.setParameterList(paramName, (Collection<?>) value);
				} else if (value instanceof Object[]) {
					query.setParameterList(paramName, (Object[]) value);
				} else {
					query.setParameter(paramName, value);
				}
			} else if (filter.getOperator() == Operator.notIn && filter.getValue() != null) {
				Object value = filter.getValue();
				if (value instanceof Collection<?>) {
					query.setParameterList(paramName, (Collection<?>) value);
				} else if (value instanceof Object[]) {
					query.setParameterList(paramName, (Object[]) value);
				} else {
					query.setParameter(paramName, value);
				}
			} else if (filter.getOperator() == Operator.isNull) {
				// 无参数
			} else if (filter.getOperator() == Operator.isNotNull) {
				// 无参数
			} else if (filter.getOperator() == Operator.between && filter.getValue() != null && filter.getValueTwo() != null) {
				query.setParameter(getParamName(key) + "_fir" + i, filter.getValue());
				query.setParameter(getParamName(key) + "_sec" + i, filter.getValueTwo());
			}
		}

	}

	protected void addFilterRestrictions(boolean hasWhere, StringBuilder queryString, List<Filter> filters) {
		if (filters == null || filters.isEmpty()) {
			return;
		}

		for (int i = 0; i < filters.size(); i++) {
			Filter filter = filters.get(i);

			if (filter == null || (StringUtils.isEmpty(filter.getProperty())&&!Operator.sql.equals(filter.getOperator()))) {
				continue;
			}
			if (!hasWhere) {
				queryString.append(" WHERE ");
				if (filter.getLeftBracket() != null) {
					queryString.append(filter.getLeftBracket().getCode());
				}
			}
			if (hasWhere) {

				if (Filter.Logic.or.equals(filter.getLogic())) {
					queryString.append(" OR ");
				} else {
					queryString.append(" AND ");
				}

				if (filter.getLeftBracket() != null) {
					queryString.append(filter.getLeftBracket().getCode());
				}
			}

			// @TODO 加sql处理
			if (Operator.sql.equals(filter.getOperator())) {
				queryString.append(" ( ").append(filter.getSql()).append(" ) ");
			} else {
				String key = filter.getProperty();
				if (StringUtils.isNotEmpty(filter.getJoinEntity())) {
					key = filter.getJoinEntity() + "." + key;
				}

				String paramName = getParamName(key) + i;

				if (filter.getOperator() == Operator.eq && filter.getValue() != null) {
					queryString.append(key).append(" = :").append(paramName);
				} else if (filter.getOperator() == Operator.ne && filter.getValue() != null) {
					queryString.append(key).append(" <> :").append(paramName);
				} else if (filter.getOperator() == Operator.gt && filter.getValue() != null) {
					queryString.append(key).append(" > :").append(paramName);
				} else if (filter.getOperator() == Operator.lt && filter.getValue() != null) {
					queryString.append(key).append(" < :").append(paramName);
				} else if (filter.getOperator() == Operator.ge && filter.getValue() != null) {
					queryString.append(key).append(" >= :").append(paramName);
				} else if (filter.getOperator() == Operator.le && filter.getValue() != null) {
					queryString.append(key).append(" <= :").append(paramName);
				} else if (filter.getOperator() == Operator.like && filter.getValue() != null && filter.getValue() instanceof String) {
					queryString.append(key).append(" like :").append(paramName);
				} else if (filter.getOperator() == Operator.notLike && filter.getValue() != null && filter.getValue() instanceof String) {
					queryString.append(key).append(" not like :").append(paramName);
				} else if (filter.getOperator() == Operator.in && filter.getValue() != null) {
					queryString.append(key).append(" in (:").append(paramName).append(")");
				} else if (filter.getOperator() == Operator.notIn && filter.getValue() != null) {
					queryString.append(key).append(" not in (:").append(paramName).append(")");
				} else if (filter.getOperator() == Operator.isNull) {
					queryString.append(key).append(" is null ");
				} else if (filter.getOperator() == Operator.isNotNull) {
					queryString.append(key).append(" is not null ");
				} else if (filter.getOperator() == Operator.between && filter.getValue() != null && filter.getValueTwo() != null) {
					queryString.append(key).append(" between :").append(getParamName(key) + "_fir" + i).append(" and :").append(getParamName(key) + "_sec" + i);
				}
			}

			hasWhere = true;
			if (filter.getRightBracket() != null) {
				queryString.append(filter.getRightBracket().getCode());
			}
		}

	}

	/**
	 * 查询在xxx-dynamic.xml中配置的查询语句
	 * 
	 * @param queryName
	 *            查询的名称
	 * @param parameters
	 *            参数
	 * @return
	 */
	public List<T> findByNamedQuery(final String queryName, final Map<String, ?> parameters) {
		StatementTemplate statementTemplate = templateCache.get(queryName);
		String statement = processTemplate(statementTemplate, parameters);
		if (statementTemplate.getType() == StatementTemplate.TYPE.HQL) {
			return this.findByHQL(statement, parameters);
		} else {
			return this.findBySQL(statement, parameters);
		}
	}
	
	public String findQueryByNamed(final String queryName, final Map<String, ?> parameters) {
		StatementTemplate statementTemplate = templateCache.get(queryName);
		String statement = processTemplate(statementTemplate, parameters);
		return statement;
	}

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
	@Override
	public <E> List<E> findByNamedQuery(RowMapper<E> rowMapper, final String queryName, final Map<String, ?> parameters) {
		StatementTemplate statementTemplate = templateCache.get(queryName);
		String statement = processTemplate(statementTemplate, parameters);
		if (statementTemplate.getType() == StatementTemplate.TYPE.HQL) {
			return this.findByHQLRowMapper(rowMapper, statement);
		} else {
			return this.findBySQLRowMapper(rowMapper, statement);
		}
	}

	/**
	 * 按HQL查询对象列表，并将对象封装成指定的对象
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> findByHQLRowMapper(RowMapper<E> rowMapper, final String hql, final Object... values) {
		Validate.notNull(rowMapper, "rowMapper不能为空！");
		List<Object[]> result = createHQLQuery(hql, values).list();
		return buildListResultFromRowMapper(rowMapper, result);
	}

	/**
	 * 按HQL查询对象列表,并将结果集封装成对象列表
	 * 
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> findByHQLRowMapper(RowMapper<E> rowMapper, final String hql, final Map<String, ?> values) {
		Validate.notNull(rowMapper, "rowMapper不能为空！");
		List<Object[]> result = createHQLQuery(hql, values).list();
		return buildListResultFromRowMapper(rowMapper, result);
	}

	/**
	 * 按SQL查询对象列表.
	 * 
	 * @param values
	 *            数量可变的参数,按顺序绑定.
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> findBySQLRowMapper(RowMapper<E> rowMapper, final String sql, final Object... values) {
		Validate.notNull(rowMapper, "rowMapper不能为空！");
		List<Object[]> result = createSQLQuery(sql, values).list();
		return buildListResultFromRowMapper(rowMapper, result);
	}

	/**
	 * 按SQL查询对象列表,并将结果集封装成对象列表
	 * 
	 * @param sql
	 *            SQL查询语句
	 * @param values
	 *            命名参数,按名称绑定.
	 */
	@SuppressWarnings("unchecked")
	public <X> List<X> findBySQLRowMapper(RowMapper<X> rowMapper, final String sql, final Map<String, ?> values) {
		Validate.notNull(rowMapper, "rowMapper不能为空！");
		List<Object[]> result = createSQLQuery(sql, values).list();
		return buildListResultFromRowMapper(rowMapper, result);
	}

	protected <E> List<E> buildListResultFromRowMapper(RowMapper<E> rowMapper, List<Object[]> result) {
		List<E> rs = new ArrayList<E>(result.size());
		for (Object[] obj : result) {
			rs.add(rowMapper.fromColumn(obj));
		}
		return rs;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		templateCache = new HashMap<String, StatementTemplate>();
		dynamicStatementBuilder.init();
		Map<String, String> namedHQLQueries = dynamicStatementBuilder.getNamedHQLQueries();
		Map<String, String> namedSQLQueries = dynamicStatementBuilder.getNamedSQLQueries();

		StringTemplateLoader stringLoader = new StringTemplateLoader();
		for (Entry<String, String> entry : namedHQLQueries.entrySet()) {
			stringLoader.putTemplate(entry.getKey(), entry.getValue());
			templateCache.put(entry.getKey(), new StatementTemplate(StatementTemplate.TYPE.HQL, new Template(entry.getKey(), new StringReader(entry.getValue()), freeMarkerConfigurer.getConfiguration())));
		}

		for (Entry<String, String> entry : namedSQLQueries.entrySet()) {
			stringLoader.putTemplate(entry.getKey(), entry.getValue());
			templateCache.put(entry.getKey(), new StatementTemplate(StatementTemplate.TYPE.SQL, new Template(entry.getKey(), new StringReader(entry.getValue()), freeMarkerConfigurer.getConfiguration())));
		}
	}

	protected String processTemplate(StatementTemplate statementTemplate, Map<String, ?> parameters) {
		StringWriter stringWriter = new StringWriter();
		try {
			statementTemplate.getTemplate().process(parameters, stringWriter);
		} catch (Exception e) {
			logger.error("处理DAO查询参数模板时发生错误：{}", e.toString());
			throw new ApplicationException(e);
		}
		return stringWriter.toString();
	}

	@Override
	public void delete(T entity) {
		getSession().delete(entity);
	}

	/**
	 * 初始化对象. 使用load()方法得到的仅是对象Proxy, 在传到View层前需要进行初始化. 如果传入entity,
	 * 则只初始化entity的直接属性,但不会初始化延迟加载的关联集合和属性. 如需初始化关联属性,需执行:
	 * Hibernate.initialize(user.getRoles())，初始化User的直接属性和关联集合.
	 * Hibernate.initialize
	 * (user.getDescription())，初始化User的直接属性和延迟加载的Description属性.
	 */
	public void initProxyObject(Object proxy) {
		Hibernate.initialize(proxy);
	}

	/**
	 * Flush当前Session.
	 */
	public void flush() {
		getSession().flush();
	}

	/**
	 * 为Query添加distinct transformer. 预加载关联对象的HQL会引起主对象重复, 需要进行distinct处理.
	 */
	public Query distinct(Query query) {
		query.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return query;
	}

	/**
	 * 取得对象的主键名.
	 */
	public String getIdName() {
		//this.getSession().getSessionFactory().getClassMetadata(this.clazz);
		ClassMetadata meta = sessionFactory.getClassMetadata(clazz);
		return meta.getIdentifierPropertyName();
	}

	/**
	 * 判断对象的属性值在数据库内是否唯一.
	 * 
	 * 在修改对象的情景下,如果属性新修改的值(value)等于属性原来的值(orgValue)则不作比较.
	 */
	public boolean isPropertyUnique(final String propertyName, final Object newValue, final Object oldValue) {
		if (newValue == null || newValue.equals(oldValue)) {
			return true;
		}
		Object object = findUniqueBy(propertyName, newValue);
		return (object == null);
	}

	/**
	 * 按属性查找唯一对象, 匹配方式为相等.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T findUniqueBy(final String propertyName, final Object value) {
		Criterion criterion = Restrictions.eq(propertyName, value);
		return ((T) createCriteria(criterion).uniqueResult());
	}

	/**
	 * 根据Criterion条件创建Criteria. 与find()函数可进行更加灵活的操作.
	 * 
	 * @param criterions
	 *            数量可变的Criterion.
	 */
	public Criteria createCriteria(final Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(clazz);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

}
