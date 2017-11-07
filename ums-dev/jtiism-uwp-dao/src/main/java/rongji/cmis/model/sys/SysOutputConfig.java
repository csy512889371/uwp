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

/**
 * SysOutputConfig entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_OUTPUT_CONFIG")
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE)
public class SysOutputConfig implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4454052278001566679L;
	// Fields

	private String id;
	private String colFlag;
	private String colName;
	private String colTitle;
	private Short colWidth;
	private Short sortno;
	private Short isuse;
	private Short rowHeight;
	private Short fontSize;

	// Constructors

	/** default constructor */
	public SysOutputConfig() {
	}

	/** full constructor */
	public SysOutputConfig(String colFlag, String colName, String colTitle,
			Short colWidth, Short sortno, Short isuse, Short rowHeight, Short fontSize) {
		this.colFlag = colFlag;
		this.colName = colName;
		this.colTitle = colTitle;
		this.colWidth = colWidth;
		this.sortno = sortno;
		this.isuse = isuse;
		this.rowHeight = rowHeight;
		this.fontSize = fontSize;
	}

	// Property accessors
	@JsonProperty
	@GenericGenerator(name = "generator", strategy = "uuid")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 36)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty
	@Column(name = "COL_FLAG", length = 20)
	public String getColFlag() {
		return this.colFlag;
	}

	public void setColFlag(String colFlag) {
		this.colFlag = colFlag;
	}

	@JsonProperty
	@Column(name = "COL_NAME", length = 20)
	public String getColName() {
		return this.colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	@JsonProperty
	@Column(name = "COL_TITLE", length = 20)
	public String getColTitle() {
		return this.colTitle;
	}

	public void setColTitle(String colTitle) {
		this.colTitle = colTitle;
	}

	@JsonProperty
	@Column(name = "COL_WIDTH")
	public Short getColWidth() {
		return this.colWidth;
	}

	public void setColWidth(Short colWidth) {
		this.colWidth = colWidth;
	}

	@JsonProperty
	@Column(name = "SORTNO")
	public Short getSortno() {
		return this.sortno;
	}

	public void setSortno(Short sortno) {
		this.sortno = sortno;
	}

	@JsonProperty
	@Column(name = "ISUSE")
	public Short getIsuse() {
		return this.isuse;
	}

	public void setIsuse(Short isuse) {
		this.isuse = isuse;
	}

	@JsonProperty
	@Column(name = "ROW_HEIGHT")
	public Short getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(Short rowHeight) {
		this.rowHeight = rowHeight;
	}

	@JsonProperty
	@Column(name = "FONT_SIZE")
	public Short getFontSize() {
		return fontSize;
	}

	public void setFontSize(Short fontSize) {
		this.fontSize = fontSize;
	}
}