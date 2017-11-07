package rongji.cmis.model.sys;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
/**
 * 
 * @author LFG
 * 版本发布历史明细
 */
@Entity
@Table(name = "CFG_BASE_VERSION_PUBLIC_DET")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "cfg_base_version_public_det_sequence")
public class VersionPublicDet {
	/**
	 * 版本发布历史明细标识
	 */
	private Integer versionPublicDet;
	/**
	 * 序列号
	 */
	private ScriptVersion scriptVersion;
	/**
	 * 发布流水
	 */
	private VersionPublic versionPublic;
	@Id
	@JsonProperty
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "version_public_det", unique = true, nullable = false)
	public Integer getVersionPublicDet() {
		return versionPublicDet;
	}

	public void setVersionPublicDet(Integer versionPublicDet) {
		this.versionPublicDet = versionPublicDet;
	}
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SEQNO")
	public ScriptVersion getScriptVersion() {
		return scriptVersion;
	}

	public void setScriptVersion(ScriptVersion scriptVersion) {
		this.scriptVersion = scriptVersion;
	}
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "public_id")
	public VersionPublic getVersionPublic() {
		return versionPublic;
	}

	public void setVersionPublic(VersionPublic versionPublic) {
		this.versionPublic = versionPublic;
	}

}
