package rongji.framework.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rongji.framework.base.pojo.Filter;
import rongji.framework.base.pojo.Order;
import rongji.framework.base.pojo.Order.Direction;

/**
 * 分页信息
 * 
 */
public class ParamRequest {
	
	public enum JoinType {
		
		/**
		 * 查询的对象去重复
		 */
		fetch,
		
		leftOutJoin
		
	}

	/** 默认页码 */
	private static final int DEFAULT_PAGE_NUMBER = 1;

	/** 默认每页记录数 */
	private static final int DEFAULT_PAGE_SIZE = 20;

	/** 最大每页记录数 */
	private static final int MAX_PAGE_SIZE = 1000;
	
	private Map<String, Object> requestMap;

	private String searchParam;

	private Map<String, Object> equalFilters;

	private Map<String, String> likeFilters;

	private Map<String, Object> inFilters;

	private Map<String, String> notInFilters;

	private Map<String, Map<String, Object>> rangeFilters;

	private Map<String, Map<String, Date>> timeFilters;

	private String groupColumns;

	private String[] JoinEntitys;

	/** 排序属性 */
	private String orderProperty;

	/** 排序方向 */
	private Direction orderDirection;

	/** 页码 */
	private int pageNumber = DEFAULT_PAGE_NUMBER;


	/** 每页记录数 */
	private Integer pageSize = DEFAULT_PAGE_SIZE;

	/** 排序 */
	private List<Order> orders = new ArrayList<Order>();
	
	/** 筛选 */
	private List<Filter> filters = new ArrayList<Filter>();
	
	private JoinType joinType = JoinType.fetch;

	/**
	 * 初始化一个新创建的ParamRequest对象
	 */
	public ParamRequest() {

	}

	/**
	 * 初始化一个新创建的ParamRequest对象
	 * 
	 * @param pageNumber
	 *            页码
	 * @param pageSize
	 *            每页记录数
	 */
	public ParamRequest(Integer pageNumber, Integer pageSize) {
		if (pageNumber != null && pageNumber >= 1) {
			this.pageNumber = pageNumber;
		}
		if (pageSize != null && pageSize >= 1 && pageSize <= MAX_PAGE_SIZE) {
			this.pageSize = pageSize;
		}
	}

	public Map<String, Object> getEqualFilters() {
		return equalFilters;
	}

	public void setEqualFilters(Map<String, Object> equalFilters) {
		this.equalFilters = equalFilters;
	}

	public Map<String, String> getLikeFilters() {
		return likeFilters;
	}

	public void setLikeFilters(Map<String, String> likeFilters) {
		this.likeFilters = likeFilters;
	}

	public Map<String, Object> getInFilters() {
		return inFilters;
	}

	public void setInFilters(Map<String, Object> inFilters) {
		this.inFilters = inFilters;
	}

	public Map<String, String> getNotInFilters() {
		return notInFilters;
	}

	public void setNotInFilters(Map<String, String> notInFilters) {
		this.notInFilters = notInFilters;
	}

	public Map<String, Map<String, Object>> getRangeFilters() {
		return rangeFilters;
	}

	public void setRangeFilters(Map<String, Map<String, Object>> rangeFilters) {
		this.rangeFilters = rangeFilters;
	}

	public Map<String, Map<String, Date>> getTimeFilters() {
		return timeFilters;
	}

	public void setTimeFilters(Map<String, Map<String, Date>> timeFilters) {
		this.timeFilters = timeFilters;
	}

	public String getGroupColumns() {
		return groupColumns;
	}

	public void setGroupColumns(String groupColumns) {
		this.groupColumns = groupColumns;
	}

	public String[] getJoinEntitys() {
		return JoinEntitys;
	}

	public void setJoinEntitys(String[] joinEntitys) {
		JoinEntitys = joinEntitys;
	}


	/**
	 * 获取每页记录数
	 * 
	 * @return 每页记录数
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页记录数
	 * 
	 * @param pageSize
	 *            每页记录数
	 */
	public void setPageSize(int pageSize) {
		if (pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
			pageSize = DEFAULT_PAGE_SIZE;
		}
		this.pageSize = pageSize;
	}

	public String getSearchParam() {
		return searchParam;
	}

	public void setSearchParam(String searchParam) {
		this.searchParam = searchParam;
	}

	/**
	 * 获取页码
	 * 
	 * @return 页码
	 */
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * 设置页码
	 * 
	 * @param pageNumber
	 *            页码
	 */
	public void setPageNumber(int pageNumber) {
		if (pageNumber < 1) {
			pageNumber = DEFAULT_PAGE_NUMBER;
		}
		this.pageNumber = pageNumber;
	}

	/**
	 * 获取排序属性
	 * 
	 * @return 排序属性
	 */
	public String getOrderProperty() {
		return orderProperty;
	}

	/**
	 * 设置排序属性
	 * 
	 * @param orderProperty
	 *            排序属性
	 */
	public void setOrderProperty(String orderProperty) {
		this.orderProperty = orderProperty;
	}

	/**
	 * 获取排序方向
	 * 
	 * @return 排序方向
	 */
	public Direction getOrderDirection() {
		return orderDirection;
	}

	/**
	 * 设置排序方向
	 * 
	 * @param orderDirection
	 *            排序方向
	 */
	public void setOrderDirection(Direction orderDirection) {
		this.orderDirection = orderDirection;
	}

	/**
	 * 获取排序
	 * 
	 * @return 排序
	 */
	public List<Order> getOrders() {
		return orders;
	}

	/**
	 * 设置排序
	 * 
	 * @param orders
	 *            排序
	 */
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	/**
	 * 获取筛选
	 * 
	 * @return 筛选
	 */
	public List<Filter> getFilters() {
		return filters;
	}

	/**
	 * 设置筛选
	 * 
	 * @param filters
	 *            筛选
	 */
	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}
	
	
	
	public void addFilter(Filter filter) {
		
		if (this.filters == null) {
			this.filters = new ArrayList<Filter>();
		}
		
		this.filters.add(filter);
	}
	
	public JoinType getJoinType() {
		return joinType;
	}

	public void setJoinType(JoinType joinType) {
		this.joinType = joinType;
	}

	public Map<String, Object> getRequestMap() {
		return requestMap;
	}

	public void setRequestMap(Map<String, Object> requestMap) {
		this.requestMap = requestMap;
	}

	public void addRequestParam(String key, String value) {
		if (requestMap == null) {
			requestMap = new HashMap<String, Object>();
		}
		requestMap.put(key, value);
	}

}
