package rongji.framework.util;

import java.io.File;
import java.net.URLDecoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

import rongji.framework.base.exception.ApplicationException;
import rongji.framework.base.pojo.Setting;

/**
 * 文件存储的磁盘位置。
 * setting.xml resourcePath
 * @author nick
 */
public class DocResourceUtils {
	
	public static String getResourceRootDir() {
		Setting setting = SettingUtils.get();
		String resourceDir = setting.getResourcePath();
		if(StringUtils.isBlank(resourceDir)) {
			throw new ApplicationException("can't not find the resource config in setting.xml");
		}
		if(!StringUtils.endsWith(resourceDir, "/")){
			resourceDir = resourceDir + "/";
		}
		return resourceDir;
	}
	
	
	public static String getResourceFilePath(String path) {
		String rootDirPath = getResourceRootDir();
		File rootDir = new File(rootDirPath);
		if (!rootDir.exists()) {
			rootDir.mkdirs();
		}
		
		String resourceFilePath = null;
		if(StringUtils.startsWith(path, "/")){
			resourceFilePath = rootDirPath + path.substring(1);
		} else {
			resourceFilePath = rootDirPath + path;
		}
		return resourceFilePath;
	}
	
	
	public static File getResourceFile(String path) {
		String resourceFilePath = getResourceFilePath(path);
		return new File(resourceFilePath);
	}
	
	
	/**
	 * @Description: (获取classes/template的路径)
	 * @return
	 * @return String 返回类型
	 * @throws
	 */
	public static String getDocFolderPath(HttpServletRequest request) {
		String path = DocResourceUtils.getResourceFilePath("resource/doc") + "/";
		try {
			path = URLDecoder.decode(path, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}
	
	public static String getDynaReportTempDoc() {
		String path = DocResourceUtils.getResourceFilePath(SettingUtils.get().getDynaReportTempDoc());
		try {
			path = URLDecoder.decode(path, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}
	
	public  static String getClassPathWithRePath(HttpServletRequest request,String repath) {
		String path = DocResourceUtils.getResourceFilePath(repath) + "/";
		try {
			path = URLDecoder.decode(path, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}
	
	/**
	 * 
	 * @Title: getTodayCacheExportFolderName
	 * @Description: 获取当天任免表导出的缓存文件夹名
	 * @param request
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	public static String getTodayCacheExportFolderName(HttpServletRequest request) {
		Date date = new Date();
		String todFolderStr = DateUtil.formatDateToStringWithNull(date, "yyyy-MM-dd");
		File todFolder = new File(getDocFolderPath(request) + todFolderStr);
		if (!todFolder.exists() && !todFolder.isDirectory()) {
			todFolder.mkdirs();
		}
		return todFolderStr;
	}
	
}
