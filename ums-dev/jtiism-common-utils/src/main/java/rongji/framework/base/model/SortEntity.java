package rongji.framework.base.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity - 排序基类
 * 
 * @version 1.0
 */
@MappedSuperclass
public abstract class SortEntity extends BaseEntity implements Comparable<SortEntity> {

	private static final long serialVersionUID = 5995013015967525827L;

	/** "排序"属性名称 */
	public static final String ORDER_PROPERTY_NAME = "seq";

	/** 排序 */
	private Integer seq;

	/**
	 * 获取排序
	 * 
	 * @return 排序
	 */
	@JsonProperty
	@Column(name = "seq")
	public Integer getSeq() {
		return seq;
	}

	/**
	 * 设置排序/权重
	 * 
	 * @param order
	 *            排序
	 */
	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	/**
	 * 实现compareTo方法
	 * 
	 * @param orderEntity
	 *            排序对象
	 * @return 比较结果
	 */
	public int compareTo(SortEntity orderEntity) {
		return new CompareToBuilder().append(getSeq(), orderEntity.getSeq()).append(getId(), orderEntity.getId()).toComparison();
	}

}