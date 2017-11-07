package rongji.cmis.service.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import rongji.framework.base.model.FileInfo;
import rongji.framework.base.model.FileInfo.FileType;
import rongji.framework.base.model.FileInfo.OrderType;

/**
 * Service - 文件
 * 
 * @version 1.0
 */
public interface FileService {

	/**
	 * 文件验证
	 * 
	 * @param fileType
	 *            文件类型
	 * @param multipartFile
	 *            上传文件
	 * @return 文件验证是否通过
	 */
	boolean isValid(FileType fileType, MultipartFile multipartFile);

	/**
	 * 文件上传
	 * 
	 * @param fileType
	 *            文件类型
	 * @param multipartFile
	 *            上传文件
	 * @param async
	 *            是否异步
	 * @return 访问URL
	 */
	String upload(FileType fileType, MultipartFile multipartFile, boolean async);

	/**
	 * 文件上传(异步)
	 * 
	 * @param fileType
	 *            文件类型
	 * @param multipartFile
	 *            上传文件
	 * @return 访问URL
	 */
	String upload(FileType fileType, MultipartFile multipartFile);

	/**
	 * 压缩文件上传(异步)
	 * 
	 * @param multipartFile
	 *            上传文件
	 * @return 上传压缩文件绝对路径
	 */
	String uploadZipOrRar(MultipartFile multipartFile, boolean async);
	
	/**
	 * 数据交换文件上传(异步)
	 * 
	 * @param fileType
	 *            文件类型
	 * @param multipartFile
	 *            上传文件
	 * @return 访问URL
	 */
	String[] uploadExchange(FileType fileType, MultipartFile multipartFile);

	/**
	 * 文件上传至本地
	 * 
	 * @param fileType
	 *            文件类型
	 * @param multipartFile
	 *            上传文件
	 * @return 路径
	 */
	String uploadLocal(FileType fileType, MultipartFile multipartFile);

	/**
	 * 文件浏览
	 * 
	 * @param path
	 *            浏览路径
	 * @param fileType
	 *            文件类型
	 * @param orderType
	 *            排序类型
	 * @return 文件信息
	 */
	List<FileInfo> browser(String path, FileType fileType, OrderType orderType);

	/**
	 * 文档文件上传
	 * 
	 * @param multipartFile
	 *            上传文件
	 * @param async
	 *            是否异步
	 * @return String数组，数组第一个为路径，第二个为文件名
	 */
	String[] uploadDoc(MultipartFile multipartFile, boolean async);

	/**
	 * 打开文件输入流
	 * 
	 * @param filePath
	 *            文件保存路径
	 * @param fileName
	 *            文件保存名称
	 */
	public InputStream openInputStream(String filePath, String fileName) throws IOException;

	/**
	 * 图片文件上传
	 * 
	 * @param multipartFile
	 *            上传图片
	 * @param async
	 *            是否异步
	 * @return String数组，数组第一个为路径，第二个为文件名
	 */
	String[] uploadImg(MultipartFile multipartFile, boolean async);

	/**
	 * 
	 * @Title: uploadAbsolute
	 * @Description: (文件上传至绝对路径)
	 * @param fileType
	 * @param relativePath
	 * @param fileName
	 * @param multipartFile
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author Administrator
	 */
	public String uploadAbsolute(FileType fileType, String relativePath, String fileName, MultipartFile multipartFile);

	/**
	 * @title uploadTemplate
	 * @description 上传模板
	 * @param multipartFile
	 * @param async
	 * @return String[]
	 * @author HuJingqiang
	 */
	void uploadTemplate(MultipartFile multipartFile, String storeName, boolean async);
}