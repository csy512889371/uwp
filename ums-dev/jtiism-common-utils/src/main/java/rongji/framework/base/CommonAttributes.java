package rongji.framework.base;

/**
 * 公共参数
 * 
 * @version 1.0
 */
public final class CommonAttributes {

	/** 日期格式配比 */
	public static final String[] DATE_PATTERNS = new String[] { "yyyy", "yyyy-MM", "yyyyMM", "yyyy/MM", "yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss" };

	/** settings.xml文件路径 */
	public static final String CMIS_XML_PATH = "/settings.xml";
	
	public static final String SYS_DATE_FORMAT = "yyyyMMdd";
	
	/**
	 * 系统分页大小
	 */
	public static String DEFAULT_PAGE_SIZE;
	/**
	 * 系统分页调节数据
	 */
	public static String DEFAULT_PAGE_LIST;
	/**
	 * 系统上传文件默认保存路径
	 */
	public static String DEFAULT_FILE_PATH;

	/**
	 * 不可实例化
	 */
	private CommonAttributes() {
	}

}