package rongji.framework.base.pojo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 筛选
 * 
 * @version 1.0
 */
public class Filter implements Serializable {

	private static final long serialVersionUID = -8712382358441065075L;

	public enum Logic {

		/** and */
		and,

		/** or */
		or
	}

	public enum LeftBracket {

		NONE(""), SINGLE("("), DOUBLE("(("), THREE("((("), FOUR("(((("), FIVE("((((("), SIX("(((((("),SEVEN("(((((((");

		private final String code;

		LeftBracket(final String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}
	}

	public enum RightBracket {

		NONE(""), SINGLE(")"), DOUBLE("))"), THREE(")))"), FOUR("))))"), FIVE(")))))"), SIX("))))))"),SEVEN(")))))))");

		private final String code;

		RightBracket(final String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}

	}

	/**
	 * 运算符
	 */
	public enum Operator {

		/** 等于 */
		eq,

		/** 不等于 */
		ne,

		/** 大于 */
		gt,

		/** 小于 */
		lt,

		/** 大于等于 */
		ge,

		/** 小于等于 */
		le,

		/** 相似 */
		like,
		/** 不相似 */
		notLike,

		/** 包含 */
		in,
		/** 不包含 */
		notIn,
		/** 为Null */
		isNull,

		/** 不为Null */
		isNotNull,

		/** 日期大于等于 */
		ged,

		/** 日期小于等于 */
		led,
		/** 日期大于 */
		dge,

		/** 日期小于 */
		dle,
		
		/** 在...之间 */
		between,
		
		/** where 部分语句 */
		sql;

		/**
		 * 从String中获取Operator
		 * 
		 * @param value
		 *            值
		 * @return String对应的operator
		 */
		public static Operator fromString(String value) {
			return Operator.valueOf(value.toLowerCase());
		}

	}

	/** 默认是否忽略大小写 */
	private static final boolean DEFAULT_IGNORE_CASE = false;

	/** 属性 */
	private String property;

	/** 运算符 */
	private Operator operator;

	/** 值 */
	private Object value;

	/** 值2 */
	private Object valueTwo;

	/** 是否忽略大小写 */
	private Boolean ignoreCase = DEFAULT_IGNORE_CASE;

	private Logic logic;

	/** 左挂号 */
	private LeftBracket leftBracket;

	/** 右边挂号 */
	private RightBracket rightBracket;

	private String joinEntity;
	
	private String sql;

	/**
	 * 初始化一个新创建的Filter对象
	 */
	public Filter() {
	}

	/**
	 * 初始化一个新创建的Filter对象
	 * 
	 * @param property
	 *            属性
	 * @param operator
	 *            运算符
	 * @param value
	 *            值
	 */
	public Filter(String property, Operator operator, Object value) {
		this.property = property;
		this.operator = operator;
		this.value = value;
	}
	
	public Filter(String joinEntity, String property, Operator operator, Object value) {
		this.property = property;
		this.operator = operator;
		this.value = value;
		this.joinEntity = joinEntity;
	}

	public Filter(LeftBracket leftBracket, String property, Operator operator, Object value) {
		this.leftBracket = leftBracket;
		this.property = property;
		this.operator = operator;
		this.value = value;
	}
	
	public Filter(String joinEntity, LeftBracket leftBracket, String property, Operator operator, Object value) {
		this.leftBracket = leftBracket;
		this.property = property;
		this.operator = operator;
		this.value = value;
		this.joinEntity = joinEntity;
	}

	public Filter(String property, Operator operator, Object value, RightBracket rightBracket) {
		this.rightBracket = rightBracket;
		this.property = property;
		this.operator = operator;
		this.value = value;
	}
	
	public Filter(String joinEntity, String property, Operator operator, Object value, RightBracket rightBracket) {
		this.rightBracket = rightBracket;
		this.property = property;
		this.operator = operator;
		this.value = value;
		this.joinEntity = joinEntity;
	}

	/**
	 * 初始化一个新创建的Filter对象
	 * 
	 * @param property
	 *            属性
	 * @param operator
	 *            运算符
	 * @param value
	 *            值
	 * @param logic
	 *            or and
	 */
	public Filter(Logic logic, String property, Operator operator, Object value) {
		this.property = property;
		this.operator = operator;
		this.value = value;
		this.logic = logic;
	}
	
	public Filter(String joinEntity, Logic logic, String property, Operator operator, Object value) {
		this.property = property;
		this.operator = operator;
		this.value = value;
		this.logic = logic;
		this.joinEntity = joinEntity;
	}


	public Filter(Logic logic, LeftBracket leftBracket, String property, Operator operator, Object value) {
		this.leftBracket = leftBracket;
		this.property = property;
		this.operator = operator;
		this.value = value;
		this.logic = logic;
	}
	
	public Filter(String joinEntity, Logic logic, LeftBracket leftBracket, String property, Operator operator, Object value) {
		this.leftBracket = leftBracket;
		this.property = property;
		this.operator = operator;
		this.value = value;
		this.logic = logic;
		this.joinEntity = joinEntity;
	}

	public Filter(Logic logic, String property, Operator operator, Object value, RightBracket rightBracket) {
		this.rightBracket = rightBracket;
		this.property = property;
		this.operator = operator;
		this.value = value;
		this.logic = logic;
	}
	
	public Filter(String joinEntity, Logic logic, String property, Operator operator, Object value, RightBracket rightBracket) {
		this.rightBracket = rightBracket;
		this.property = property;
		this.operator = operator;
		this.value = value;
		this.logic = logic;
		this.joinEntity = joinEntity;
	}

	/**
	 * 返回等于筛选
	 * 
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 等于筛选
	 */
	public static Filter eq(String property, Object value) {
		return new Filter(property, Operator.eq, value);
	}

	/**
	 * 返回等于筛选
	 * 
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @param ignoreCase
	 *            忽略大小写
	 * @return 等于筛选
	 */
	public static Filter eq(Logic logic, String property, Object value) {
		return new Filter(logic, property, Operator.eq, value);
	}

	/**
	 * 返回不等于筛选
	 * 
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 不等于筛选
	 */
	public static Filter ne(String property, Object value) {
		return new Filter(property, Operator.ne, value);
	}

	/**
	 * 返回不等于筛选
	 * 
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @param ignoreCase
	 *            忽略大小写
	 * @return 不等于筛选
	 */
	public static Filter ne(String property, Object value, Logic logic) {
		return new Filter(logic, property, Operator.ne, value);
	}

	/**
	 * 返回大于筛选
	 * 
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 大于筛选
	 */
	public static Filter gt(String property, Object value) {
		return new Filter(property, Operator.gt, value);
	}

	/**
	 * 返回小于筛选
	 * 
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 小于筛选
	 */
	public static Filter lt(String property, Object value) {
		return new Filter(property, Operator.lt, value);
	}

	/**
	 * 返回大于等于筛选
	 * 
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 大于等于筛选
	 */
	public static Filter ge(String property, Object value) {
		return new Filter(property, Operator.ge, value);
	}

	/**
	 * 返回小于等于筛选
	 * 
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 小于等于筛选
	 */
	public static Filter le(String property, Object value) {
		return new Filter(property, Operator.le, value);
	}

	/**
	 * 返回相似筛选
	 * 
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 相似筛选
	 */
	public static Filter like(String property, Object value) {
		return new Filter(property, Operator.like, value);
	}

	/**
	 * 返回包含筛选
	 * 
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 包含筛选
	 */
	public static Filter in(Logic logic,String property, Object value) {
		return new Filter(logic,property, Operator.in, value);
	}
	
	/**
	 * 返回包含筛选
	 * 
	 * @param property
	 *            属性
	 * @param value
	 *            值
	 * @return 包含筛选
	 */
	public static Filter in(String property, Object value) {
		return new Filter(property, Operator.in, value);
	}

	/**
	 * 返回为Null筛选
	 * 
	 * @param property
	 *            属性
	 * @return 为Null筛选
	 */
	public static Filter isNull(String property) {
		return new Filter(property, Operator.isNull, null);
	}

	/**
	 * 返回不为Null筛选
	 * 
	 * @param property
	 *            属性
	 * @return 不为Null筛选
	 */
	public static Filter isNotNull(String property) {
		return new Filter(property, Operator.isNotNull, null);
	}

	/**
	 * 返回忽略大小写筛选
	 * 
	 * @return 忽略大小写筛选
	 */
	public Filter ignoreCase() {
		this.ignoreCase = true;
		return this;
	}

	/**
	 * 获取属性
	 * 
	 * @return 属性
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * 设置属性
	 * 
	 * @param property
	 *            属性
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * 获取运算符
	 * 
	 * @return 运算符
	 */
	public Operator getOperator() {
		return operator;
	}

	/**
	 * 设置运算符
	 * 
	 * @param operator
	 *            运算符
	 */
	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	/**
	 * 获取值
	 * 
	 * @return 值
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * 设置值
	 * 
	 * @param value
	 *            值
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * 获取值2
	 * 
	 * @return 值
	 */
	public Object getValueTwo() {
		return valueTwo;
	}

	/**
	 * 设置值2
	 * 
	 * @param valueTwo
	 *            值
	 */
	public void setValueTwo(Object valueTwo) {
		this.valueTwo = valueTwo;
	}

	/**
	 * 获取是否忽略大小写
	 * 
	 * @return 是否忽略大小写
	 */
	public Boolean getIgnoreCase() {
		return ignoreCase;
	}

	/**
	 * 设置是否忽略大小写
	 * 
	 * @param ignoreCase
	 *            是否忽略大小写
	 */
	public void setIgnoreCase(Boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public Logic getLogic() {
		return logic;
	}

	public void setLogic(Logic logic) {
		this.logic = logic;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		Filter other = (Filter) obj;
		return new EqualsBuilder().append(getProperty(), other.getProperty()).append(getOperator(), other.getOperator()).append(getValue(), other.getValue()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(getProperty()).append(getOperator()).append(getValue()).toHashCode();
	}

	public LeftBracket getLeftBracket() {
		return leftBracket;
	}

	public void setLeftBracket(LeftBracket leftBracket) {
		this.leftBracket = leftBracket;
	}

	public RightBracket getRightBracket() {
		return rightBracket;
	}

	public void setRightBracket(RightBracket rightBracket) {
		this.rightBracket = rightBracket;
	}

	public String getJoinEntity() {
		return joinEntity;
	}

	public void setJoinEntity(String joinEntity) {
		this.joinEntity = joinEntity;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
	
}