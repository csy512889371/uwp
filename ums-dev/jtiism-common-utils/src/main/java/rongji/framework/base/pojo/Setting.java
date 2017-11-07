package rongji.framework.base.pojo;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 系统设置
 * 
 * @version 1.0
 */
public class Setting implements Serializable {

	/**
	 * 水印位置
	 */
	public enum WatermarkPosition {

		/** 无 */
		no,

		/** 左上 */
		topLeft,

		/** 右上 */
		topRight,

		/** 居中 */
		center,

		/** 左下 */
		bottomLeft,

		/** 右下 */
		bottomRight
	}

	private static final long serialVersionUID = -3746000475364396407L;

	/** 缓存名称 */
	public static final String CACHE_NAME = "setting";

	/** 缓存Key */
	public static final Integer CACHE_KEY = 0;

	/** 分隔符 */
	private static final String SEPARATOR = ",";

	/** 网站网址 */
	private String siteUrl;

	/** 上传文件最大限制 */
	private Integer uploadMaxSize;

	/** 允许上传图片扩展名 */
	private String uploadImageExtension;

	/** 允许上传Flash扩展名 */
	private String uploadFlashExtension;

	/** 允许上传媒体扩展名 */
	private String uploadMediaExtension;

	/** 允许上传文件扩展名 */
	private String uploadFileExtension;

	/** 图片上传路径 */
	private String imageUploadPath;

	/** Flash上传路径 */
	private String flashUploadPath;

	/** 媒体上传路径 */
	private String mediaUploadPath;

	/** 文件上传路径 */
	private String fileUploadPath;

	/** 临时文件路径 */
	private String tempUploadPath;

	/** 文档文件路径 */
	private String docUploadPath;

	/** Cookie路径 */
	private String cookiePath;

	/** Cookie作用域 */
	private String cookieDomain;

	/**
	 * 任免表图片宽度
	 */
	private Integer cadreImageWidth;

	/**
	 * 任免表图片高度
	 */
	private Integer cadreImageHeight;

	/**
	 * 模板文件上传路径
	 */
	private String templateUploadPath;

	/**
	 * 数据交换文件上传路径
	 */
	private String exchangeUploadPath;

	/**
	 * IE8 安装包下载路径
	 */
	private String iE8PackagePath;

	/**
	 * windowsXP SP3补丁包下载路径
	 */
	private String windowsXpSp3Path;

	/**
	 * 资源根目录
	 */
	private String resourcePath;
	
	/** 用户分组根节点名称 */
	private String userGroupRootName;

	/**
	 * 是否需要使用缓存 redis
	 */
	private boolean needRedis;
	
	/**
	 * 系统操作手册下载路径
	 */
	private String systemOpManualPath;

	/**
	 * 动态js路径
	 */
	private String dynajsPath;

	/**
	 * 营业执照路径
	 */
	private String licensePicPath;
	/**
	 * 营业执照缩略图路径
	 */
	private String licenseThumbPath;
	/**
	 * 宣传照路径
	 */
	private String publicPicPath;
	/**
	 * 宣传照缩略图路径
	 */
	private String publicThumbPath;
	/**
	 * 账户锁定时间
	 */
	private int accountLockTime;
	/**
	 * 账户超出次数后锁定
	 */
	private int accountLockCount;

	/**
	 * 动态报表引擎导出excel时临时生成的文件
	 */
	private String dynaReportTempDoc;
	/**
	 * 动态表单图片路径
	 */
	private String dynaImgPath;
	/**
	 * 动态表单缩略图路径
	 */
	private String dynaThumbPath;
	/**
	 * 企业用户角色Id
	 */
	private int memberAccountRoleId;
	/**
	 * 企业用户待审角色Id

	 */
	private int memberAccountNoAprRoleId;
	/**
	 * 企业用户账号初始密码
	 */
	private String originPassword;

	/**
	 * 租赁平台url
	 */
	private String appAdminUrl;

	/**
	 * 获取网站网址
	 * 
	 * @return 网站网址
	 */
	@NotEmpty
	@Length(max = 200)
	public String getSiteUrl() {
		return siteUrl;
	}

	/**
	 * 设置网站网址
	 * 
	 * @param siteUrl
	 *            网站网址
	 */
	public void setSiteUrl(String siteUrl) {
		this.siteUrl = StringUtils.removeEnd(siteUrl, "/");
	}

	/**
	 * 获取上传文件最大限制
	 * 
	 * @return 上传文件最大限制
	 */
	@NotNull
	@Min(0)
	public Integer getUploadMaxSize() {
		return uploadMaxSize;
	}

	/**
	 * 设置上传文件最大限制
	 * 
	 * @param uploadMaxSize
	 *            上传文件最大限制
	 */
	public void setUploadMaxSize(Integer uploadMaxSize) {
		this.uploadMaxSize = uploadMaxSize;
	}

	/**
	 * 获取允许上传图片扩展名
	 * 
	 * @return 允许上传图片扩展名
	 */
	@Length(max = 200)
	public String getUploadImageExtension() {
		return uploadImageExtension;
	}

	/**
	 * 设置允许上传图片扩展名
	 * 
	 * @param uploadImageExtension
	 *            允许上传图片扩展名
	 */
	public void setUploadImageExtension(String uploadImageExtension) {
		if (uploadImageExtension != null) {
			uploadImageExtension = uploadImageExtension.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "").toLowerCase();
		}
		this.uploadImageExtension = uploadImageExtension;
	}

	/**
	 * 获取允许上传Flash扩展名
	 * 
	 * @return 允许上传Flash扩展名
	 */
	@Length(max = 200)
	public String getUploadFlashExtension() {
		return uploadFlashExtension;
	}

	/**
	 * 设置允许上传Flash扩展名
	 * 
	 * @param uploadFlashExtension
	 *            允许上传Flash扩展名
	 */
	public void setUploadFlashExtension(String uploadFlashExtension) {
		if (uploadFlashExtension != null) {
			uploadFlashExtension = uploadFlashExtension.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "").toLowerCase();
		}
		this.uploadFlashExtension = uploadFlashExtension;
	}

	/**
	 * 获取允许上传媒体扩展名
	 * 
	 * @return 允许上传媒体扩展名
	 */
	@Length(max = 200)
	public String getUploadMediaExtension() {
		return uploadMediaExtension;
	}

	/**
	 * 设置允许上传媒体扩展名
	 * 
	 * @param uploadMediaExtension
	 *            允许上传媒体扩展名
	 */
	public void setUploadMediaExtension(String uploadMediaExtension) {
		if (uploadMediaExtension != null) {
			uploadMediaExtension = uploadMediaExtension.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "").toLowerCase();
		}
		this.uploadMediaExtension = uploadMediaExtension;
	}

	/**
	 * 获取允许上传文件扩展名
	 * 
	 * @return 允许上传文件扩展名
	 */
	@Length(max = 200)
	public String getUploadFileExtension() {
		return uploadFileExtension;
	}

	/**
	 * 设置允许上传文件扩展名
	 * 
	 * @param uploadFileExtension
	 *            允许上传文件扩展名
	 */
	public void setUploadFileExtension(String uploadFileExtension) {
		if (uploadFileExtension != null) {
			uploadFileExtension = uploadFileExtension.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "").toLowerCase();
		}
		this.uploadFileExtension = uploadFileExtension;
	}

	/**
	 * 获取允许上传Flash扩展名
	 * 
	 * @return 允许上传Flash扩展名
	 */
	public String[] getUploadFlashExtensions() {
		return StringUtils.split(uploadFlashExtension, SEPARATOR);
	}

	/**
	 * 获取允许上传媒体扩展名
	 * 
	 * @return 允许上传媒体扩展名
	 */
	public String[] getUploadMediaExtensions() {
		return StringUtils.split(uploadMediaExtension, SEPARATOR);
	}

	/**
	 * 获取允许上传文件扩展名
	 * 
	 * @return 允许上传文件扩展名
	 */
	public String[] getUploadFileExtensions() {
		return StringUtils.split(uploadFileExtension, SEPARATOR);
	}

	/**
	 * 获取允许上传图片扩展名
	 * 
	 * @return 允许上传图片扩展名
	 */
	public String[] getUploadImageExtensions() {
		return StringUtils.split(uploadImageExtension, SEPARATOR);
	}

	/**
	 * 获取图片上传路径
	 * 
	 * @return 图片上传路径
	 */
	@NotEmpty
	@Length(max = 200)
	public String getImageUploadPath() {
		return imageUploadPath;
	}

	/**
	 * 设置图片上传路径
	 * 
	 * @param imageUploadPath
	 *            图片上传路径
	 */
	public void setImageUploadPath(String imageUploadPath) {
		if (imageUploadPath != null) {
			if (!imageUploadPath.startsWith("/")) {
				imageUploadPath = "/" + imageUploadPath;
			}
			if (!imageUploadPath.endsWith("/")) {
				imageUploadPath += "/";
			}
		}
		this.imageUploadPath = imageUploadPath;
	}

	/**
	 * 获取Flash上传路径
	 * 
	 * @return Flash上传路径
	 */
	@NotEmpty
	@Length(max = 200)
	public String getFlashUploadPath() {
		return flashUploadPath;
	}

	/**
	 * 设置Flash上传路径
	 * 
	 * @param flashUploadPath
	 *            Flash上传路径
	 */
	public void setFlashUploadPath(String flashUploadPath) {
		if (flashUploadPath != null) {
			if (!flashUploadPath.startsWith("/")) {
				flashUploadPath = "/" + flashUploadPath;
			}
			if (!flashUploadPath.endsWith("/")) {
				flashUploadPath += "/";
			}
		}
		this.flashUploadPath = flashUploadPath;
	}

	/**
	 * 获取媒体上传路径
	 * 
	 * @return 媒体上传路径
	 */
	@NotEmpty
	@Length(max = 200)
	public String getMediaUploadPath() {
		return mediaUploadPath;
	}

	/**
	 * 设置媒体上传路径
	 * 
	 * @param mediaUploadPath
	 *            媒体上传路径
	 */
	public void setMediaUploadPath(String mediaUploadPath) {
		if (mediaUploadPath != null) {
			if (!mediaUploadPath.startsWith("/")) {
				mediaUploadPath = "/" + mediaUploadPath;
			}
			if (!mediaUploadPath.endsWith("/")) {
				mediaUploadPath += "/";
			}
		}
		this.mediaUploadPath = mediaUploadPath;
	}

	/**
	 * 获取文件上传路径
	 * 
	 * @return 文件上传路径
	 */
	@NotEmpty
	@Length(max = 200)
	public String getFileUploadPath() {
		return fileUploadPath;
	}

	/**
	 * 设置文件上传路径
	 * 
	 * @param fileUploadPath
	 *            文件上传路径
	 */
	public void setFileUploadPath(String fileUploadPath) {
		if (fileUploadPath != null) {
			if (!fileUploadPath.startsWith("/")) {
				fileUploadPath = "/" + fileUploadPath;
			}
			if (!fileUploadPath.endsWith("/")) {
				fileUploadPath += "/";
			}
		}
		this.fileUploadPath = fileUploadPath;
	}

	/**
	 * 获取文件上传路径
	 * 
	 * @return 文件上传路径
	 */
	@NotEmpty
	@Length(max = 200)
	public String getDocUploadPath() {
		return docUploadPath;
	}

	/**
	 * 设置文件上传路径
	 * 
	 * @param fileUploadPath
	 *            文件上传路径
	 */
	public void setDocUploadPath(String docUploadPath) {
		if (docUploadPath != null) {
			if (!docUploadPath.startsWith("/")) {
				docUploadPath = "/" + docUploadPath;
			}
			if (!docUploadPath.endsWith("/")) {
				docUploadPath += "/";
			}
		}
		this.docUploadPath = docUploadPath;
	}

	public String getTempUploadPath() {
		return tempUploadPath;
	}

	public void setTempUploadPath(String tempUploadPath) {
		if (tempUploadPath != null) {
			if (!tempUploadPath.startsWith("/")) {
				tempUploadPath = "/" + tempUploadPath;
			}
			if (!tempUploadPath.endsWith("/")) {
				tempUploadPath += "/";
			}
		}
		this.tempUploadPath = tempUploadPath;
	}

	public String getCookiePath() {
		return cookiePath;
	}

	public void setCookiePath(String cookiePath) {
		this.cookiePath = cookiePath;
	}

	public String getCookieDomain() {
		return cookieDomain;
	}

	public void setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}

	public boolean isNeedRedis() {
		return needRedis;
	}

	public void setNeedRedis(boolean needRedis) {
		this.needRedis = needRedis;
	}

	public Integer getCadreImageWidth() {
		return cadreImageWidth;
	}

	public void setCadreImageWidth(Integer cadreImageWidth) {
		this.cadreImageWidth = cadreImageWidth;
	}

	public Integer getCadreImageHeight() {
		return cadreImageHeight;
	}

	public void setCadreImageHeight(Integer cadreImageHeight) {
		this.cadreImageHeight = cadreImageHeight;
	}

	public String getTemplateUploadPath() {
		return templateUploadPath;
	}

	public void setTemplateUploadPath(String templateUploadPath) {
		this.templateUploadPath = templateUploadPath;
	}

	public String getExchangeUploadPath() {
		return exchangeUploadPath;
	}

	public void setExchangeUploadPath(String exchangeUploadPath) {
		this.exchangeUploadPath = exchangeUploadPath;
	}

	public String getUserGroupRootName() {
		return userGroupRootName;
	}

	public void setUserGroupRootName(String userGroupRootName) {
		this.userGroupRootName = userGroupRootName;
	}

	public String getiE8PackagePath() {
		return iE8PackagePath;
	}

	public void setiE8PackagePath(String iE8PackagePath) {
		this.iE8PackagePath = iE8PackagePath;
	}

	public String getWindowsXpSp3Path() {
		return windowsXpSp3Path;
	}

	public void setWindowsXpSp3Path(String windowsXpSp3Path) {
		this.windowsXpSp3Path = windowsXpSp3Path;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public String getSystemOpManualPath() {
		return systemOpManualPath;
	}

	public void setSystemOpManualPath(String systemOpManualPath) {
		this.systemOpManualPath = systemOpManualPath;
	}

	public String getDynajsPath() {
		return dynajsPath;
	}

	public void setDynajsPath(String dynajsPath) {
		this.dynajsPath = dynajsPath;
	}


	public String getLicensePicPath() {
		return licensePicPath;
	}

	public void setLicensePicPath(String licensePicPath) {
		this.licensePicPath = licensePicPath;
	}

	public String getLicenseThumbPath() {
		return licenseThumbPath;
	}

	public void setLicenseThumbPath(String licenseThumbPath) {
		this.licenseThumbPath = licenseThumbPath;
	}

	public String getPublicPicPath() {
		return publicPicPath;
	}

	public void setPublicPicPath(String publicPicPath) {
		this.publicPicPath = publicPicPath;
	}

	public String getPublicThumbPath() {
		return publicThumbPath;
	}

	public void setPublicThumbPath(String publicThumbPath) {
		this.publicThumbPath = publicThumbPath;
	}

	public int getAccountLockTime() {
		return accountLockTime;
	}

	public void setAccountLockTime(int accountLockTime) {
		this.accountLockTime = accountLockTime;
	}

	public int getAccountLockCount() {
		return accountLockCount;
	}

	public void setAccountLockCount(int accountLockCount) {
		this.accountLockCount = accountLockCount;
	}

	public String getDynaReportTempDoc() {
		return dynaReportTempDoc;
	}

	public void setDynaReportTempDoc(String dynaReportTempDoc) {
		this.dynaReportTempDoc = dynaReportTempDoc;
	}

	public String getDynaImgPath() {
		return dynaImgPath;
	}

	public void setDynaImgPath(String dynaImgPath) {
		this.dynaImgPath = dynaImgPath;
	}

	public String getDynaThumbPath() {
		return dynaThumbPath;
	}

	public void setDynaThumbPath(String dynaThumbPath) {
		this.dynaThumbPath = dynaThumbPath;
	}

	public int getMemberAccountRoleId() {
		return memberAccountRoleId;
	}

	public void setMemberAccountRoleId(int memberAccountRoleId) {
		this.memberAccountRoleId = memberAccountRoleId;
	}

	public int getMemberAccountNoAprRoleId() {
		return memberAccountNoAprRoleId;
	}

	public void setMemberAccountNoAprRoleId(int memberAccountNoAprRoleId) {
		this.memberAccountNoAprRoleId = memberAccountNoAprRoleId;
	}

	public String getOriginPassword() {
		return originPassword;
	}

	public void setOriginPassword(String originPassword) {
		this.originPassword = originPassword;
	}

	public String getAppAdminUrl() {
		return appAdminUrl;
	}

	public void setAppAdminUrl(String appAdminUrl) {
		this.appAdminUrl = appAdminUrl;
	}
}