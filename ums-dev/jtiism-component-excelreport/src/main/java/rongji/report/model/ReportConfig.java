package rongji.report.model;

import org.apache.commons.lang3.StringUtils;

public class ReportConfig {

	private static final String defaultMethodPath = "/reportEngin/method.xml";

	private static final String defaultFontsPath = "/reportEngin/fonts.xml";
	
	private static final String defaultFunctionsPath = "/reportEngin/functions.txt";
	
	private static final String defaultCdsClassName = "rongji.shjt.service.report.impl.CDSEnterpriseImpl";
	
	public static final String defaultTempDocFolder = "tempDoc";

	private OutFileType defaultOutFileType = OutFileType.singleFile;

	public enum OutFileType {
		/**
		 * 多文件导出
		 */
		mulitFile,

		/**
		 * 单文件多sheet导出
		 */
		singleFile
	}

	private String templateName;

	/**
	 * 具体方法实现
	 */
	private String methodPath = defaultMethodPath;
	
	/**
	 * 模板路径
	 */
	private String tempPath;
	
	/**
	 * 报表引擎生成文件保存的根路径
	 */
	private String baseFolderPath;
	
	
	private String relativePath;
	
	private OutFileType outFileType;
	
	/**
	 * 单文件最大 sheet数量。如果超过则创建新的excel。
	 */
	private int maxSheetCount = 250;

	/**
	 * excel 中默认字体的长度、宽度
	 */
	private String fontsPath = defaultFontsPath;

	/**
	 * excel 文件导出文件的位置
	 */
	private String excelFileDir;

	/**
	 * 生成的zip文件的路径
	 */
	private String zipFileDir;
	
	/**
	 * Chained data set
	 * 链式数据集具体实现类
	 */
	private String cdsClassName = defaultCdsClassName;
	
	/**
	 * JSH 执行器默认函数
	 */
	private String functionsPath = defaultFunctionsPath;

	public String getMethodPath() {
		return methodPath;
	}

	public ReportConfig setMethodPath(String methodPath) {
		this.methodPath = methodPath;
		return this;
	}

	public String getTempPath() {
		return tempPath;
	}

	public ReportConfig setTempPath(String tempPath) {
		this.tempPath = tempPath;
		return this;
	}

	public String getBaseFolderPath() {
		return baseFolderPath;
	}

	public ReportConfig setBaseFolderPath(String baseFolderPath) {
		this.baseFolderPath = baseFolderPath;
		return this;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public ReportConfig setRelativePath(String relativePath) {
		this.relativePath = relativePath;
		return this;
	}

	public OutFileType getOutFileType() {
		return outFileType;
	}

	public ReportConfig setOutFileType(OutFileType forEachType) {
		this.outFileType = forEachType;
		return this;
	}

	public ReportConfig setOutFileType(String forEachType) {

		if (StringUtils.isEmpty(forEachType)) {
			this.outFileType = defaultOutFileType;
		} else {
			this.outFileType = OutFileType.valueOf(forEachType);
		}
		return this;
	}

	public String getFontsPath() {
		return fontsPath;
	}

	public ReportConfig setFontsPath(String fontsPath) {
		this.fontsPath = fontsPath;
		return this;
	}

	public String getExcelFileDir() {
		return excelFileDir;
	}

	public ReportConfig setExcelFileDir(String excelFileDir) {
		this.excelFileDir = excelFileDir;
		return this;
	}

	public String getZipFileDir() {
		return zipFileDir;
	}

	public void setZipFileDir(String zipFileDir) {
		this.zipFileDir = zipFileDir;
	}

	public int getMaxSheetCount() {
		return maxSheetCount;
	}

	public void setMaxSheetCount(int maxSheetCount) {
		this.maxSheetCount = maxSheetCount;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getFunctionsPath() {
		return functionsPath;
	}

	public void setFunctionsPath(String functionsPath) {
		this.functionsPath = functionsPath;
	}

	public String getCdsClassName() {
		return cdsClassName;
	}

	public void setCdsClassName(String cdsClassName) {
		this.cdsClassName = cdsClassName;
	}
	
	
	
}
