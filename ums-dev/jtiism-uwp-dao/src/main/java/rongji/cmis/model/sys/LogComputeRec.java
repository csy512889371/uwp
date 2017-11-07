package rongji.cmis.model.sys;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;
import rongji.framework.base.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

/**
 * Entity - 日志
 * 
 * @version 1.0
 */
@Entity
@Table(name = "CFG_BASE_LOG_COMPUTER_REC")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "cfg_base_log_computer_rec_sequence")
public class LogComputeRec extends BaseEntity {

	public enum OperateType {
		insert, update, delete
	}

	private static final long serialVersionUID = -4494144902110236826L;

	/** 操作员ID */
	private Integer operatorId;

	/** 操作日期 */
	private Date operateDate;

	/** 操作对象 */
	private String operateObj;

	/** 操作集 */
	private String operateSet;

	/** IP */
	private String ip;

	/**
	 * 数据主键
	 */
	private String dataId;

	/**
	 * 备注
	 * 
	 */
	private String remark;

	/**
	 * 操作类型　insert update delete
	 */
	private OperateType operateType;

	/**
	 * 修改前的内容
	 * 
	 */
	private String oldValMap;
	/**
	 * 修改后的内容
	 * 
	 */
	private String newValMap;

	/**
	 * 获取IP
	 * 
	 * @return IP
	 */
	@JsonProperty
	@Column(name = "IP_ADR", nullable = false, updatable = false)
	public String getIp() {
		return ip;
	}

	/**
	 * 设置IP
	 * 
	 * @param ip
	 *            IP
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	@JsonProperty
	@Column(name = "OPERATE_SET")
	public String getOperateSet() {
		return operateSet;
	}

	public void setOperateSet(String operateSet) {
		this.operateSet = operateSet;
	}

	@JsonProperty
	// 与前端表单中的日期格式一致
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	// DB 中的时间 转成 Json
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "OPERATE_DATE")
	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	@JsonProperty
	@Column(name = "DATA_ID")
	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	@JsonProperty
	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@JsonProperty
	@Column(name = "OPERATOR_ID")
	public Integer getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}

	@JsonProperty
	@Column(name = "OPERATE_TYPE")
	public OperateType getOperateType() {
		return operateType;
	}

	public void setOperateType(OperateType operateType) {
		this.operateType = operateType;
	}

	@JsonProperty
	@Column(name = "OLD_VAL_MAP")
	public String getOldValMap() {
		return oldValMap;
	}

	public void setOldValMap(String oldValMap) {
		this.oldValMap = oldValMap;
	}

	@JsonProperty
	@Column(name = "NEW_VAL_MAP")
	public String getNewValMap() {
		return newValMap;
	}

	public void setNewValMap(String newValMap) {
		this.newValMap = newValMap;
	}

	@JsonProperty
	@Column(name = "OPERATE_OBJ")
	public String getOperateObj() {
		return operateObj;
	}

	public void setOperateObj(String operateObj) {
		this.operateObj = operateObj;
	}
}