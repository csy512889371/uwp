package rongji.cmis.model.sys;

import com.fasterxml.jackson.annotation.JsonProperty;
import rongji.framework.base.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "CFG_UMS_EVENT")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "CFG_UMS_EVENT")
public class JmsEvent extends BaseEntity {

	private static final long serialVersionUID = 5414577938204042925L;

	/**
	 * 状态
	 */
	public enum Status {
		/** 新增 **/
		newAdd,

		/** 已处理 **/
		done
	}

	private Status status;

	private String topic;

	private String message;

	private int count;

	private Date updateTime;

	private Date createTime;

	@JsonProperty
	@Column(name = "status")
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@JsonProperty
	@Column(name = "topic")
	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	@JsonProperty
	@Column(name = "message")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@JsonProperty
	@Column(name = "count")
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@JsonProperty
	@Column(name = "updateTime")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@JsonProperty
	@Column(name = "createTime")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
