package rongji.report.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import rongji.report.model.ReportConfig.OutFileType;
import rongji.report.model.ReportContext.DealFileType;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "REPORT_TEMPLATE")
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE)
public class ReportTemplate implements Serializable {

	public enum TemplateType {
		// 花名册
		muster,
		// 任免表
		appointDismiss,
		// 统计类
		statistics
	}

	private static final long serialVersionUID = 4070911808212407819L;

	/**
	 * 主键
	 */
	private String id;

	/**
	 * 模板规则
	 */
	private String rules;

	/**
	 * 模板路径
	 */
	private String filePath;

	/**
	 * 模板名称
	 */
	private String templateName;

	/**
	 * 模板类型
	 */
	private TemplateType templateType;

	/**
	 * 排序号
	 */
	private int inino;

	/**
	 * 是否启用
	 */
	private boolean isvalid;

	@GenericGenerator(name = "generator", strategy = "rongji.framework.base.dao.generater.UUIDKeyGen")
	@Id
	@JsonProperty
	@GeneratedValue(generator = "generator")
	@Column(name = "REPORT_TEMPLATE_ID", unique = true, nullable = false, length = 36)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty
	@Column(name = "TEMPLATENAME")
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	@JsonProperty
	@Column(name = "TEMPLATETYPE")
	public TemplateType getTemplateType() {
		return templateType;
	}

	public void setTemplateType(TemplateType templateType) {
		this.templateType = templateType;
	}

	@JsonProperty
	@Column(name = "RULES")
	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	@JsonProperty
	@Column(name = "FILEPATH")
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@JsonProperty
	@Column(name = "ININO")
	public int getInino() {
		return inino;
	}

	public void setInino(int inino) {
		this.inino = inino;
	}

	@JsonProperty
	@Column(name = "ISVALID")
	public boolean getIsvalid() {
		return isvalid;
	}

	public void setIsvalid(boolean isvalid) {
		this.isvalid = isvalid;
	}

	@Transient
	public DealFileType getDealFileType(OutFileType outFileType) {

		if (TemplateType.appointDismiss.equals(templateType) && OutFileType.mulitFile.equals(outFileType)) {
			return DealFileType.MULFILE_SINGLESHEET;
		}

		// 使用场景： 任免表 单文件 多sheet
		if (TemplateType.appointDismiss.equals(templateType) && OutFileType.singleFile.equals(outFileType)) {
			return DealFileType.SINGLEFILE_MULSHEET;
		}

		// 花名册
		if (TemplateType.muster.equals(templateType)) {
			return DealFileType.SINGLESHEET_MULLINE;
		}

		// // 使用场景：多文件 多sheet
		// if (TemplateType.muster.equals(templateType) &&
		// ForEachType.mulitSheet.equals(forEacheType)) {
		// return DealFileType.MULFILE_MULSHEET;
		// }

		return null;
	}
}
