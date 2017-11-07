package rongji.cmis.model.sys;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 
 * @author LFG 版本发布历史
 */
@Entity
@Table(name = "CFG_BASE_VERSION_PUBLIC")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "cfg_base_version_public_sequence")
public class VersionPublic {
	/**
	 * 发布流水
	 */
	private Integer publicId;
	/**
	 * 发布日期
	 */
	private Date pubDate;
	/**
	 * 发布说明
	 */
	private String pubDesc;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 0:FZ 1:FJ
	 */
	private Integer pubType;
	/**
	 * 版本号
	 */
	private String versionNo;
	/**
	 * 脚本类型0:WEB 1:桌面
	 */
	private Integer scriptType;

	@Id
	@JsonProperty
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "public_id", unique = true, nullable = false)
	public Integer getPublicId() {
		return publicId;
	}

	public void setPublicId(Integer publicId) {
		this.publicId = publicId;
	}

	@JsonProperty
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "pub_date")
	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	@JsonProperty
	@Column(name = "pub_desc", length = 500)
	public String getPubDesc() {
		return pubDesc;
	}

	public void setPubDesc(String pubDesc) {
		this.pubDesc = pubDesc;
	}

	@JsonProperty
	@Column(name = "memo", length = 500)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@JsonProperty
	@Column(name = "pub_type")
	public Integer getPubType() {
		return pubType;
	}

	public void setPubType(Integer pubType) {
		this.pubType = pubType;
	}

	@JsonProperty
	@Column(name = "VERSION_NO", length = 20)
	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}
	@JsonProperty
	@Column(name = "SCRIPT_TYPE")
	public Integer getScriptType() {
		return scriptType;
	}

	public void setScriptType(Integer scriptType) {
		this.scriptType = scriptType;
	}

}
