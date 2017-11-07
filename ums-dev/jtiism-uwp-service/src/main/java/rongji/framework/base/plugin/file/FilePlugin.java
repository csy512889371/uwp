package rongji.framework.base.plugin.file;

import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import rongji.framework.base.model.FileInfo;
import rongji.framework.base.plugin.StoragePlugin;
import rongji.framework.base.pojo.Setting;
import rongji.framework.util.DocResourceUtils;
import rongji.framework.util.FreemarkerUtils;
import rongji.framework.util.SettingUtils;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Plugin - 本地文件存储
 *
 * @version 1.0
 */
@Component("filePlugin")
public class FilePlugin extends StoragePlugin implements ServletContextAware {

    private static final Logger logger = LoggerFactory.getLogger(FilePlugin.class);


    /**
     * servletContext
     */
    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public String getName() {
        return "本地文件存储";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getAuthor() {
        return "LongE";
    }

    @Override
    public String getSiteUrl() {
        return "http://www.fjlonge.com";
    }

    @Override
    public String getInstallUrl() {
        return null;
    }

    @Override
    public String getUninstallUrl() {
        return null;
    }

    @Override
    public String getSettingUrl() {
        return "file/setting.jhtml";
    }

    @Override
    public String upload(String path, File file, String contentType) {
        File destFile = DocResourceUtils.getResourceFile(path);
        try {
            FileUtils.moveFile(file, destFile);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return destFile.getPath();
    }

    public InputStream openInputStream(String filePath) throws IOException {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        File file = DocResourceUtils.getResourceFile(filePath);

        if (!file.exists()) {
            return null;
        }
        return FileUtils.openInputStream(file);

    }

    @Override
    public String getUrl(String path) {
        //Setting setting = SettingUtils.get();
        return path;
    }


    @Override
    public String getUrlByType(FileInfo.FileType fileType) {
        String path=null;
        try {
            Setting setting = SettingUtils.get();
            String uploadPath;
            if (fileType == FileInfo.FileType.flash) {
                uploadPath = setting.getFlashUploadPath();
            } else if (fileType == FileInfo.FileType.media) {
                uploadPath = setting.getMediaUploadPath();
            } else if (fileType == FileInfo.FileType.file) {
                uploadPath = setting.getFileUploadPath();
            } else if (fileType == FileInfo.FileType.temp) {
                uploadPath = setting.getTempUploadPath();
            } else if (fileType == FileInfo.FileType.doc) {
                uploadPath = setting.getDocUploadPath();
            } else if (fileType == FileInfo.FileType.exchange) {
                uploadPath = setting.getExchangeUploadPath();
            } else {
                uploadPath = setting.getImageUploadPath();
            }
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("uuid", UUID.randomUUID().toString());
            path = FreemarkerUtils.process(uploadPath, model);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return path.replaceAll("\\\\", "/").replaceAll("\\\\\\\\", "/");
    }

    @Override
    public List<FileInfo> browser(String path) {
        Setting setting = SettingUtils.get();
        List<FileInfo> fileInfos = new ArrayList<FileInfo>();
        File directory = DocResourceUtils.getResourceFile(path);
        if (directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                FileInfo fileInfo = new FileInfo();
                fileInfo.setName(file.getName());
                fileInfo.setUrl(setting.getSiteUrl() + path + file.getName());
                fileInfo.setIsDirectory(file.isDirectory());
                fileInfo.setSize(file.length());
                fileInfo.setLastModified(new Date(file.lastModified()));
                fileInfos.add(fileInfo);
            }
        }
        return fileInfos;
    }
}