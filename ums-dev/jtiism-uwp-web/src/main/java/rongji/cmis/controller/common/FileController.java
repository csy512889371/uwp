package rongji.cmis.controller.common;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import rongji.cmis.service.common.FileService;
import rongji.framework.base.model.FileInfo;
import rongji.framework.base.model.FileInfo.FileType;
import rongji.framework.base.model.FileInfo.OrderType;
import rongji.framework.base.model.ResultModel;
import rongji.framework.common.web.controller.BaseController;
import rongji.framework.util.DocResourceUtils;
import rongji.framework.util.FastjsonUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller - 文件处理
 *
 * @version 1.0
 */
@Controller("adminFileController")
@RequestMapping("/sys/file")
public class FileController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	@Resource(name = "fileServiceImpl")
	private FileService fileService;

	/**
	 * 上传
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "text/html; charset=UTF-8")
	public void upload(FileType fileType, MultipartFile file, HttpServletResponse response) {
		Map<String, Object> data = new HashMap<>();
		if (!fileService.isValid(fileType, file)) {
			data.put("message", ResultModel.warn("admin.upload.invalid"));
		} else {
			String url = fileService.upload(fileType, file, false);
			if (url == null) {
				data.put("message", ResultModel.warn("admin.upload.error"));
			} else {
				data.put("message", SUCCESS_RESULT_MODEL);
				data.put("url", url);
			}
		}
		try {
			response.setContentType("text/html; charset=UTF-8");
			FastjsonUtils.writeValue(response.getWriter(), data);
		} catch (IOException e) {
			logger.error("upload error.", e);
		}
	}

	/**
	 * 浏览
	 */
	@RequestMapping(value = "/browser", method = RequestMethod.GET)
	public @ResponseBody List<FileInfo> browser(String path, FileType fileType, OrderType orderType) {
		return fileService.browser(path, fileType, orderType);
	}

	@ResponseBody
	@RequestMapping("loadFile")
	public void loadFile(String fileName, String relFolderName, HttpServletRequest request, HttpServletResponse response) {
        try {
            fileName = URLDecoder.decode(URLDecoder.decode(fileName,"UTF-8"),"UTF-8");
            relFolderName = URLDecoder.decode(URLDecoder.decode(relFolderName,"UTF-8"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
			logger.error("", e);
        }
        String path = DocResourceUtils.getDocFolderPath(request);
		if (StringUtils.isNotEmpty(relFolderName)) {
			path = DocResourceUtils.getResourceFilePath(relFolderName);
		}
		path = path + fileName;
		File file = new File(path);
		OutputStream output = null;
		BufferedInputStream bufferedInputStream = null;
		try {
			bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
			solveRandomCode(file.getName(), request, response);// 解决乱码问题
			byte[] buffer = new byte[1024];
			int len = 0;
			output = response.getOutputStream();
			while ((len = bufferedInputStream.read(buffer)) > 0) {
				output.write(buffer, 0, len);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (bufferedInputStream != null) {
				IOUtils.closeQuietly(bufferedInputStream);
			}
			if (output != null) {
				IOUtils.closeQuietly(output);
			}
		}
	}



}