package rongji.cmis.model.sys;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import rongji.cmis.model.ums.CfgUmsUser;
import rongji.framework.base.dao.utils.CustomDateSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CFG_BASE_MSG_REMIND")
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE)
public class MsgRemind implements Serializable {
	/**
	 * @Fields serialVersionUID : (用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = -8170487690267165660L;
	/**
	 * 消息提醒标识
	 */
	private String msgRemindId;
	/**
	 * 用户ID
	 */
	private CfgUmsUser cfgUmsUser;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 内容
	 */
	private String contents;
	/**
	 * 发布时间
	 */
	private Date publictime;
	/**
	 * 消息类型
	 */
	private MsgType msgType;
	/**
	 * 0:未读1:已读
	 */
	private Integer msgStatus;
	/**
	 * 参数
	 */
	private String param;
	/**
	 * URL跳转
	 */
	private String skipUrl;

	public enum MsgType {

		/** 一般消息 */
		general("0", ""),

		/** 流程消息 */
		flow("1", ""),

		/** 事务提醒 */
		job("2", "remindIndex");

		private String code;
		private String name;

		MsgType(final String code, final String name) {
			this.code = code;
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public static String getName(String code) {
			for (MsgType msgType : MsgType.values()) {
				if (msgType.getCode().equals(code)) {
					return msgType.name;
				}
			}
			return null;
		}

	}

	@JsonProperty
	@Id
	@GenericGenerator(name = "systemUUID", strategy = "uuid")
	@GeneratedValue(generator = "systemUUID")
	@Column(name = "MSG_REMIND_ID", unique = true, nullable = false)
	public String getMsgRemindId() {
		return this.msgRemindId;
	}

	public void setMsgRemindId(String _msgRemindId) {
		this.msgRemindId = _msgRemindId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	public CfgUmsUser getCfgUmsUser() {
		return cfgUmsUser;
	}

	public void setCfgUmsUser(CfgUmsUser cfgUmsUser) {
		this.cfgUmsUser = cfgUmsUser;
	}

	@JsonProperty
	@Column(name = "TITLE", length = 50)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty
	@Column(name = "CONTENTS", length = 500)
	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	@JsonProperty
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonSerialize(using = CustomDateSerializer.class)
	@Column(name = "PUBLICTIME")
	public Date getPublictime() {
		return publictime;
	}

	public void setPublictime(Date publictime) {
		this.publictime = publictime;
	}

	@JsonProperty
	@Column(name = "MSG_TYPE")
	public MsgType getMsgType() {
		return msgType;
	}

	public void setMsgType(MsgType msgType) {
		this.msgType = msgType;
	}

	@JsonProperty
	@Column(name = "MSG_STATUS")
	public Integer getMsgStatus() {
		return msgStatus;
	}

	public void setMsgStatus(Integer msgStatus) {
		this.msgStatus = msgStatus;
	}

	@JsonProperty
	@Column(name = "PARAM", length = 50)
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	@JsonProperty
	@Column(name = "SKIP_URL", length = 200)
	public String getSkipUrl() {
		return skipUrl;
	}

	public void setSkipUrl(String skipUrl) {
		this.skipUrl = skipUrl;
	}

}
