package rongji.framework.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;

public class Zip4jUtils {

	private static final Logger logger = LoggerFactory.getLogger(Zip4jUtils.class);

	public static void addStreamToZip(String zipPath, InputStream is, String fileName, String passWord) {
		try {
			ZipFile zipFile = new ZipFile(zipPath);

			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

			if (!StringUtil.isEmpty(passWord)) {
				parameters.setEncryptFiles(true);
				parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
				parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
				parameters.setPassword(passWord);
			}

			parameters.setFileNameInZip(fileName);
			parameters.setSourceExternalStream(true);
			zipFile.addStream(is, parameters);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (is != null) {
				IOUtils.closeQuietly(is);
			}
		}
	}

	public static void addFilesToFolderInZip(String zipPath, ArrayList<File> filesToAdd, String passWord, String rootFolder) {
		try {
			ZipFile zipFile = new ZipFile(zipPath);
			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

			if (!StringUtil.isEmpty(passWord)) {
				parameters.setEncryptFiles(true);
				parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
				parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
				parameters.setPassword(passWord);
			}

			if (!StringUtil.isEmpty(rootFolder)) {
				parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
				parameters.setRootFolderInZip(rootFolder);
			}

			parameters.setRootFolderInZip(rootFolder);
			if (filesToAdd.size() > 0) {
				zipFile.addFiles(filesToAdd, parameters);
			}
		} catch (ZipException e) {
			logger.error(e.getMessage(), e);
		}

	}

	public static void extractZipAllFiles(String zipPath, String passWord, String extractZipPath) {

		try {
			ZipFile zipFile = new ZipFile(zipPath);
			// 如果解压的文件需要密码，可以添加以下代码：
			if (zipFile.isEncrypted()) {
				zipFile.setPassword(passWord);
			}
			zipFile.extractAll(extractZipPath);
		} catch (ZipException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static void main(String[] arg) {
		String zipPath = "D:\\test\\csy.zip";
		String pathWord = "123456";

		String cadreInfo = "text info ";
		InputStream is = new ByteArrayInputStream(cadreInfo.getBytes());

		addStreamToZip(zipPath, is, "cadreINfo.txt", pathWord);
		ArrayList<File> filesToAdd = new ArrayList<File>();
		filesToAdd.add(new File("d:\\csy.jpg"));
		filesToAdd.add(new File("d:\\logs.log"));

		addFilesToFolderInZip(zipPath, filesToAdd, pathWord, "image/");
	}

	/**
	 * @title unRarFile
	 * @description 解压rar文件
	 * @param srcRarPath
	 *            rar文件路径
	 * @param dstDirectoryPath
	 *            解压文件路径
	 */
	public static void unRarFile(String srcRarPath, String dstDirectoryPath) {
		File dstDiretory = new File(dstDirectoryPath);
		// 目标目录不存在时，创建该文件夹
		if (!dstDiretory.exists()) {
			dstDiretory.mkdirs();
		}
		Archive archive = null;
		FileOutputStream fos = null;
		try {
			archive = new Archive(new File(srcRarPath));
			FileHeader fh = archive.nextFileHeader();
			while (fh != null) {
				if (!fh.isDirectory()) {
					// 1 根据不同的操作系统拿到相应的 destDirName 和 destFileName
					String compressFileName = fh.getFileNameString().trim();
					String destFileName = "";
					String destDirName = "";
					// 非windows系统
					if (File.separator.equals("/")) {
						destFileName = dstDirectoryPath + File.separator + compressFileName.replaceAll("\\\\", "/");
						destDirName = destFileName.substring(0, destFileName.lastIndexOf("/"));
						// windows系统
					} else {
						destFileName = dstDirectoryPath + File.separator + compressFileName.replaceAll("/", "\\\\");
						destDirName = destFileName.substring(0, destFileName.lastIndexOf("\\"));
					}
					// 2创建文件夹
					File dir = new File(destDirName);
					if (!dir.exists() || !dir.isDirectory()) {
						dir.mkdirs();
					}
					// 3解压缩文件
					fos = new FileOutputStream(new File(destFileName));
					archive.extractFile(fh, fos);
					fos.close();
					fos = null;
				}
				fh = archive.nextFileHeader();
			}
			archive.close();
			archive = null;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (fos != null) {
				IOUtils.closeQuietly(fos);
			}
			if (archive != null) {
				IOUtils.closeQuietly(archive);
			}
		}
	}

}
