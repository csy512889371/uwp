package rongji.cmis.model.ums;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import rongji.framework.base.model.BaseEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CFG_UMS_CODE(代码表) 指标代码实体类
 * 
 * @author Administrator
 * 
 */
@Entity
@Table(name = "CFG_UMS_CODE")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "CFG_UMS_CODE")
public class CfgUmsCode extends BaseEntity {

	private static final long serialVersionUID = 6382667230119503805L;

	/**
	 * 代码
	 */
	private String code;
	/**
	 * 备注
	 */
	private String remark;

	private Set<CfgUmsCodeAttribute> cfgUmsCodeAttribute = new HashSet<CfgUmsCodeAttribute>();

	@JsonProperty
	@Column(name = "CODE", length = 20)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@JsonProperty
	@Column(name = "REMARK", length = 50)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cfgUmsCode")
	@OrderBy("seq")
	public Set<CfgUmsCodeAttribute> getCfgUmsCodeAttribute() {
		return cfgUmsCodeAttribute;
	}

	public void setCfgUmsCodeAttribute(Set<CfgUmsCodeAttribute> cfgUmsCodeAttribute) {
		this.cfgUmsCodeAttribute = cfgUmsCodeAttribute;
	}

}
