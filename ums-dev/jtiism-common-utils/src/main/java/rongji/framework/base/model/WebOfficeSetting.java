package rongji.framework.base.model;

/**  
* @Title: WebOfficeSetting.java
* @Description: (weboffice设置)
* @author wqq 
* @date 2016年5月16日 上午11:53:57
* @version V1.0  
*/ 
public class WebOfficeSetting {
	
	private String docNo;			//文号
	
	private String docUrl;			//文档url
	/*
	Word文档：						“Word.Document”
	PowerPoint幻灯片：				“PowerPoint.Show”
	Excel工作表：					“Excel.Sheet”
	*/
	//docType非必选字段
	private String docType;		//文档类型

	private String docName;			//文档文件名,保存到服务器/保存到本地的时候使用
	
	private boolean readOnly;		//是否只读，只读仅是隐藏菜单栏和工具栏，不是word的readOnly模式
	
	private boolean uploadSupport;	//是否支持上传
	
	private String uploadUrl;		//上传文件处理路径
	
	private String operater;		//操作人
	
	private String bussData;		//业务参数
	
	private boolean print;			//是否直接预览打印
	
	private boolean fullScreen = true;	//是否默认全屏模式

	public String getDocNo() {
		return docNo;
	}

	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}

	public String getDocUrl() {
		return docUrl;
	}

	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isUploadSupport() {
		return uploadSupport;
	}

	public void setUploadSupport(boolean uploadSupport) {
		this.uploadSupport = uploadSupport;
	}

	

	public String getUploadUrl() {
		return uploadUrl;
	}

	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
	}

	public String getOperater() {
		return operater;
	}

	public void setOperater(String operater) {
		this.operater = operater;
	}

	public String getBussData() {
		return bussData;
	}

	public void setBussData(String bussData) {
		this.bussData = bussData;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public boolean isPrint() {
		return print;
	}

	public void setPrint(boolean print) {
		this.print = print;
	}

	public boolean isFullScreen() {
		return fullScreen;
	}

	public void setFullScreen(boolean fullScreen) {
		this.fullScreen = fullScreen;
	}
	
	
}
