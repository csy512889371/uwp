package rongji.cmis.model.sys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@Entity
@Table(name = "SHJT_SYS_CONFIG")
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE)
public class SysConfig implements java.io.Serializable {

	/**
	* @Fields serialVersionUID : (用一句话描述这个变量表示什么)
	*/ 
	private static final long serialVersionUID = 8642876828547711079L;
	/**
	 * id
	 */
	private String id;
	private String name;
	private String value;
	
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@JsonProperty
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "NAME", length = 255,unique = true)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "VALUE", length = 255)
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

}