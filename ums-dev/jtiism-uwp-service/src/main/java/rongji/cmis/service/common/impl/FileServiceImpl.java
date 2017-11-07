package rongji.cmis.service.common.impl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
import rongji.cmis.service.common.FileService;
import rongji.cmis.service.common.PluginService;
import rongji.framework.base.model.FileInfo;
import rongji.framework.base.model.FileInfo.FileType;
import rongji.framework.base.model.FileInfo.OrderType;
import rongji.framework.base.plugin.StoragePlugin;
import rongji.framework.base.pojo.Setting;
import rongji.framework.util.DocResourceUtils;
import rongji.framework.util.FreemarkerUtils;
import rongji.framework.util.SettingUtils;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Service - 文件
 * 
 * @version 1.0
 */
@Service("fileServiceImpl")
public class FileServiceImpl implements FileService, ServletContextAware {

	private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	/** servletContext */
	private ServletContext servletContext;

	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;

	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	/**
	 * 添加上传任务
	 * 
	 * @param storagePlugin
	 *            存储插件
	 * @param path
	 *            上传路径
	 * @param tempFile
	 *            临时文件
	 * @param contentType
	 *            文件类型
	 */
	private void addTask(final StoragePlugin storagePlugin, final String path, final File tempFile, final String contentType) {
		taskExecutor.execute(new Runnable() {
			public void run() {
				try {
					storagePlugin.upload(path, tempFile, contentType);
				} finally {
					FileUtils.deleteQuietly(tempFile);
				}
			}
		});
	}

	public boolean isValid(FileType fileType, MultipartFile multipartFile) {
		if (multipartFile == null) {
			return false;
		}
		Setting setting = SettingUtils.get();
		if (setting.getUploadMaxSize() != null && setting.getUploadMaxSize() != 0 && multipartFile.getSize() > setting.getUploadMaxSize() * 1024L * 1024L) {
			return false;
		}
		String[] uploadExtensions;
		if (fileType == FileType.flash) {
			uploadExtensions = setting.getUploadFlashExtensions();
		} else if (fileType == FileType.media) {
			uploadExtensions = setting.getUploadMediaExtensions();
		} else if (fileType == FileType.file) {
			uploadExtensions = setting.getUploadFileExtensions();
		} else {
			uploadExtensions = setting.getUploadImageExtensions();
		}
		if (ArrayUtils.isNotEmpty(uploadExtensions)) {
			return FilenameUtils.isExtension(multipartFile.getOriginalFilename(), uploadExtensions);
		}
		return false;
	}

	public String[] uploadDoc(MultipartFile multipartFile, boolean async) {
		return uploadStorage(FileType.doc, multipartFile, async);
	}

	public String[] uploadImg(MultipartFile multipartFile, boolean async) {
		return uploadStorage(FileType.image, multipartFile, async);
	}

	public void uploadTemplate(MultipartFile multipartFile, String storeName, boolean async) {
		Setting setting = SettingUtils.get();
		String uploadPath = setting.getTemplateUploadPath();
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("uuid", UUID.randomUUID().toString());
			String path = FreemarkerUtils.process(uploadPath, model);
			String destPath = path + storeName;
			File tempFile = new File(System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".tmp");
			if (!tempFile.getParentFile().exists()) {
				tempFile.getParentFile().mkdirs();
			}
			multipartFile.transferTo(tempFile);
			if (async) {
				addTask(getStoragePlugin(), destPath, tempFile, multipartFile.getContentType());
			} else {
				try {
					getStoragePlugin().upload(destPath, tempFile, multipartFile.getContentType());
				} finally {
					FileUtils.deleteQuietly(tempFile);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public InputStream openInputStream(String filePath, String fileName) throws IOException {
		return getStoragePlugin().openInputStream(filePath + fileName);
	}

	public String[] uploadStorage(FileType fileType, MultipartFile multipartFile, boolean async) {
		if (multipartFile == null) {
			return null;
		}
		Setting setting = SettingUtils.get();
		String uploadPath;
		if (fileType == FileType.flash) {
			uploadPath = setting.getFlashUploadPath();
		} else if (fileType == FileType.media) {
			uploadPath = setting.getMediaUploadPath();
		} else if (fileType == FileType.file) {
			uploadPath = setting.getFileUploadPath();
		} else if (fileType == FileType.temp) {
			uploadPath = setting.getTempUploadPath();
		} else if (fileType == FileType.doc) {
			uploadPath = setting.getDocUploadPath();
		} else if (fileType == FileType.exchange) {
			uploadPath = setting.getExchangeUploadPath();
		} else {
			uploadPath = setting.getImageUploadPath();
		}
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("uuid", UUID.randomUUID().toString());
			String path = FreemarkerUtils.process(uploadPath, model);
			String fileName = UUID.randomUUID() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
			String destPath = path + fileName;
			String realPath = "";
			File tempFile = new File(System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".tmp");
			if (!tempFile.getParentFile().exists()) {
				tempFile.getParentFile().mkdirs();
			}
			multipartFile.transferTo(tempFile);
			if (async) {
				addTask(getStoragePlugin(), destPath, tempFile, multipartFile.getContentType());
			} else {
				try {
					realPath = getStoragePlugin().upload(destPath, tempFile, multipartFile.getContentType());
				} finally {
					FileUtils.deleteQuietly(tempFile);
				}
			}
			String[] ret = new String[3];
			ret[0] = path;
			ret[1] = fileName;
			ret[2] = realPath;
			return ret;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public String upload(FileType fileType, MultipartFile multipartFile, boolean async) {
		String[] ret = uploadStorage(fileType, multipartFile, async);
		if (ret != null) {
			return getStoragePlugin().getUrl(ret[0] + ret[1]);
		}
		return null;
	}

	public String uploadZipOrRar(MultipartFile multipartFile, boolean async) {
		String[] ret = uploadStorage(FileType.file, multipartFile, async);
		if (ret != null) {
			return getStoragePlugin().getUrl(ret[2]);
		}
		return null;
	}

	private StoragePlugin getStoragePlugin() {
		List<StoragePlugin> plugins = pluginService.getStoragePlugins(true);
		if (plugins == null || plugins.isEmpty()) {
			return null;
		}
		StoragePlugin storagePlugin = plugins.get(0);
		return storagePlugin;
	}

	public String[] uploadExchange(FileType fileType, MultipartFile multipartFile) {
		String[] ret = uploadStorage(fileType, multipartFile, false);
		if (ret != null) {
			return ret;
		}
		return null;
	}

	public String upload(FileType fileType, MultipartFile multipartFile) {
		return upload(fileType, multipartFile, false);
	}

	public String uploadLocal(FileType fileType, MultipartFile multipartFile) {
		if (multipartFile == null) {
			return null;
		}
		Setting setting = SettingUtils.get();
		String uploadPath;
		if (fileType == FileType.flash) {
			uploadPath = setting.getFlashUploadPath();
		} else if (fileType == FileType.media) {
			uploadPath = setting.getMediaUploadPath();
		} else if (fileType == FileType.file) {
			uploadPath = setting.getFileUploadPath();
		} else {
			uploadPath = setting.getImageUploadPath();
		}
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("uuid", UUID.randomUUID().toString());
			String path = FreemarkerUtils.process(uploadPath, model);
			String destPath = path + UUID.randomUUID() + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());

			File destFile = DocResourceUtils.getResourceFile(destPath);
			if (!destFile.getParentFile().exists()) {
				destFile.getParentFile().mkdirs();
			}
			multipartFile.transferTo(destFile);
			return destPath;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public List<FileInfo> browser(String path, FileType fileType, OrderType orderType) {
		if (path != null) {
			if (!path.startsWith("/")) {
				path = "/" + path;
			}
			if (!path.endsWith("/")) {
				path += "/";
			}
		} else {
			path = "/";
		}
		Setting setting = SettingUtils.get();
		String uploadPath;
		if (fileType == FileType.flash) {
			uploadPath = setting.getFlashUploadPath();
		} else if (fileType == FileType.media) {
			uploadPath = setting.getMediaUploadPath();
		} else if (fileType == FileType.file) {
			uploadPath = setting.getFileUploadPath();
		} else {
			uploadPath = setting.getImageUploadPath();
		}
		String browsePath = StringUtils.substringBefore(uploadPath, "${");
		browsePath = StringUtils.substringBeforeLast(browsePath, "/") + path;

		List<FileInfo> fileInfos = new ArrayList<FileInfo>();
		if (browsePath.indexOf("..") >= 0) {
			return fileInfos;
		}
		for (StoragePlugin storagePlugin : pluginService.getStoragePlugins(true)) {
			fileInfos = storagePlugin.browser(browsePath);
			break;
		}
		if (orderType == OrderType.size) {
			Collections.sort(fileInfos, new SizeComparator());
		} else if (orderType == OrderType.type) {
			Collections.sort(fileInfos, new TypeComparator());
		} else {
			Collections.sort(fileInfos, new NameComparator());
		}
		return fileInfos;
	}

	private class NameComparator implements Comparator<FileInfo> {
		public int compare(FileInfo fileInfos1, FileInfo fileInfos2) {
			return new CompareToBuilder().append(!fileInfos1.getIsDirectory(), !fileInfos2.getIsDirectory()).append(fileInfos1.getName(), fileInfos2.getName()).toComparison();
		}
	}

	private class SizeComparator implements Comparator<FileInfo> {
		public int compare(FileInfo fileInfos1, FileInfo fileInfos2) {
			return new CompareToBuilder().append(!fileInfos1.getIsDirectory(), !fileInfos2.getIsDirectory()).append(fileInfos1.getSize(), fileInfos2.getSize()).toComparison();
		}
	}

	private class TypeComparator implements Comparator<FileInfo> {
		public int compare(FileInfo fileInfos1, FileInfo fileInfos2) {
			return new CompareToBuilder().append(!fileInfos1.getIsDirectory(), !fileInfos2.getIsDirectory()).append(FilenameUtils.getExtension(fileInfos1.getName()), FilenameUtils.getExtension(fileInfos2.getName())).toComparison();
		}
	}

	public String uploadAbsolute(FileType fileType, String relativePath, String fileName, MultipartFile multipartFile) {
		if (multipartFile == null) {
			return null;
		}
		Setting setting = SettingUtils.get();
		String uploadPath;
		if (fileType == FileType.flash) {
			uploadPath = setting.getFlashUploadPath();
		} else if (fileType == FileType.media) {
			uploadPath = setting.getMediaUploadPath();
		} else if (fileType == FileType.file) {
			uploadPath = setting.getFileUploadPath();
		} else {
			uploadPath = setting.getImageUploadPath();
		}
		uploadPath = uploadPath + relativePath;
		try {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("uuid", UUID.randomUUID().toString());
			String path = FreemarkerUtils.process(uploadPath, model);
			String destPath = path + fileName;
			File destFile = new File(destPath);
			if (!destFile.getParentFile().exists()) {
				destFile.getParentFile().mkdirs();
			}
			multipartFile.transferTo(destFile);
			return destPath;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}


	public String getUrlByType(FileType fileType) {
		return getStoragePlugin().getUrlByType(fileType);
	}

}