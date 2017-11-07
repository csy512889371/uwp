package rongji.cmis.model.sys;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import rongji.framework.base.dao.utils.CustomDateSerializer;

/**
 * 脚本版本控制
 * 
 * @author LFG
 *
 */
@Entity
@Table(name = "CFG_UMS_SCRIPT_VERSION")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "script_version_sequence")
public class ScriptVersion {
	/**
	 * 序列号
	 */
	private Integer seqno;
	/**
	 * sqlServer库变更脚本
	 */
	private String chnScript;
	/**
	 * 变更说明
	 */
	private String chnDesc;
	/**
	 * 变更时间
	 */
	private Date chnTime;
	/**
	 * 变更类型0：结构 1：数据
	 */
	private Integer chnType;
	/**
	 * SVN版本号
	 */
	private String svnVerno;
	/**
	 * 开发库库变更状态0:未更新1：已更新
	 */
	private Integer devdbUp;
	/**
	 * 183库变更状态0:未更新1：已更新
	 */
	private Integer demodbUp;
	/**
	 * 市组库变更状态0:未更新1：已更新
	 */
	private Integer fzdbUp;
	/**
	 * 省组库变更状态0:未更新1：已更新
	 */
	private Integer fjdbUp;
	/**
	 * 测试库变更状态0:未更新1：已更新
	 */
	private Integer testdbUp;
	/**
	 * PDM结构维护0：未维护 1：已维护
	 */
	private Integer pdmUp;
	/**
	 * 
	 */
	private String depVersion;

	private Integer isSelected;// 1:添加版本中已选中 ，0：添加版本中为选中
	/**
	 * access库变更脚本
	 */
	private String chnScriptAccess;

	@Id
	@JsonProperty
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "SEQNO", unique = true, nullable = false)
	public Integer getSeqno() {
		return seqno;
	}

	public void setSeqno(Integer seqno) {
		this.seqno = seqno;
	}

	@JsonProperty
	@Column(name = "CHN_SCRIPT")
	public String getChnScript() {
		return chnScript;
	}

	public void setChnScript(String chnScript) {
		this.chnScript = chnScript;
	}

	@JsonProperty
	@Column(name = "CHN_DESC", length = 200)
	public String getChnDesc() {
		return chnDesc;
	}

	public void setChnDesc(String chnDesc) {
		this.chnDesc = chnDesc;
	}

	@JsonProperty
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonSerialize(using = CustomDateSerializer.class)
	@Column(name = "CHN_TIME")
	public Date getChnTime() {
		return chnTime;
	}

	public void setChnTime(Date chnTime) {
		this.chnTime = chnTime;
	}

	@JsonProperty
	@Column(name = "CHN_TYPE")
	public Integer getChnType() {
		return chnType;
	}

	public void setChnType(Integer chnType) {
		this.chnType = chnType;
	}

	@JsonProperty
	@Column(name = "SVN_VERNO", length = 50)
	public String getSvnVerno() {
		return svnVerno;
	}

	public void setSvnVerno(String svnVerno) {
		this.svnVerno = svnVerno;
	}

	@JsonProperty
	@Column(name = "DEVDB_UP")
	public Integer getDevdbUp() {
		return devdbUp;
	}

	public void setDevdbUp(Integer devdbUp) {
		this.devdbUp = devdbUp;
	}

	@JsonProperty
	@Column(name = "DEMODB_UP")
	public Integer getDemodbUp() {
		return demodbUp;
	}

	public void setDemodbUp(Integer demodbUp) {
		this.demodbUp = demodbUp;
	}

	@JsonProperty
	@Column(name = "FZDB_UP")
	public Integer getFzdbUp() {
		return fzdbUp;
	}

	public void setFzdbUp(Integer fzdbUp) {
		this.fzdbUp = fzdbUp;
	}

	@JsonProperty
	@Column(name = "FJDB_UP")
	public Integer getFjdbUp() {
		return fjdbUp;
	}

	public void setFjdbUp(Integer fjdbUp) {
		this.fjdbUp = fjdbUp;
	}

	@JsonProperty
	@Column(name = "TESTDB_UP")
	public Integer getTestdbUp() {
		return testdbUp;
	}

	public void setTestdbUp(Integer testdbUp) {
		this.testdbUp = testdbUp;
	}

	@JsonProperty
	@Column(name = "PDM_UP")
	public Integer getPdmUp() {
		return pdmUp;
	}

	public void setPdmUp(Integer pdmUp) {
		this.pdmUp = pdmUp;
	}

	@JsonProperty
	@Column(name = "dep_version", length = 20)
	public String getDepVersion() {
		return depVersion;
	}

	public void setDepVersion(String depVersion) {
		this.depVersion = depVersion;
	}

	@JsonProperty
	@Transient
	public Integer getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Integer isSelected) {
		this.isSelected = isSelected;
	}

	@JsonProperty
	@Column(name = "CHN_SCRIPT_ACCESS")
	public String getChnScriptAccess() {
		return chnScriptAccess;
	}

	public void setChnScriptAccess(String chnScriptAccess) {
		this.chnScriptAccess = chnScriptAccess;
	}

}
