package rongji.framework.util.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class TestZip4j {

	// 创建压缩包添 加文件到压缩包中（未设置加密）
	public static void addFilesToFolderInZip() {
		try {
			ZipFile zipFile = new ZipFile("D:\\test\\AddFilesDeflateComp.zip");
			ArrayList<File> filesToAdd = new ArrayList<File>();
			filesToAdd.add(new File("d:\\test\\sample.txt"));
			filesToAdd.add(new File("d:\\test\\myvideo.avi"));
			filesToAdd.add(new File("d:\\test\\mysong.mp3"));

			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD); // 加密级别

			zipFile.addFiles(filesToAdd, parameters);
		} catch (ZipException e) {
			e.printStackTrace();
		}

	}

	public static void zipFile() {
		try {
			final ZipFile zipFile = new ZipFile("d:\\test\\测试Zip.zip"); // 創建zip包，指定了zip路徑和zip名稱
			final ArrayList<File> fileAddZip = new ArrayList<File>(); // 向zip包中添加文件集合
			fileAddZip.add(new File("d:\\test\\column命名规则.docx")); // 向zip包中添加一个word文件
			final ZipParameters parameters = new ZipParameters(); // 设置zip包的一些参数集合
			parameters.setEncryptFiles(true); // 是否设置密码（此处设置为：是）
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // 压缩方式(默认值)
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL); // 普通级别（参数很多）
			parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD); // 加密级别

			parameters.setPassword("123456"); // 压缩包密码为123456
			zipFile.createZipFile(fileAddZip, parameters); // 创建压缩包完成
		} catch (final ZipException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加文件夹到压缩包中 需要添加的文件夹必须存在，否则抛出异常：ZipException: input folder does not exist
	 * 如果已经存在同名文件则会出现一个文件的时候会出现一个问题，程序会生成一个临时包并去修改之前存在的同名压缩文件，
	 * 最后修改不成功且会抛出异常：ZipException: cannot rename modified zip file最后只留下一个临时包，
	 * 建议在生成的时候添加判断 避免出现这种错误
	 * 
	 */
	public static void addFolder() {

		try {

			ZipFile zipFile = new ZipFile("d:\\test\\AddFolder.zip");
			String folderToAdd = "d:\\FolderToAdd";

			ZipParameters parameters = new ZipParameters();

			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

			zipFile.addFolder(folderToAdd, parameters);

		} catch (ZipException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建加密压缩包
	 */
	public static void addFilesWithAESEncryption() {

		try {
			ZipFile zipFile = new ZipFile("d:\\test\\AddFilesWithAESZipEncryption.zip");

			ArrayList<File> filesToAdd = new ArrayList<File>();
			filesToAdd.add(new File("d:\\test\\sample.txt"));
			filesToAdd.add(new File("d:\\test\\myvideo.avi"));
			filesToAdd.add(new File("d:\\test\\mysong.mp3"));

			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
			parameters.setEncryptFiles(true);

			parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);

			parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
			parameters.setPassword("123");

			zipFile.addFiles(filesToAdd, parameters);
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}

	// 创建分卷压缩包
	public static void createSplitZipFile() {

		try {

			ZipFile zipFile = new ZipFile("d:\\test\\CreateSplitZipFile.zip");

			ArrayList<File> filesToAdd = new ArrayList<File>();
			filesToAdd.add(new File("d:\\test\\sample.txt"));
			filesToAdd.add(new File("d:\\test\\myvideo.avi"));
			filesToAdd.add(new File("d:\\test\\mysong.mp3"));

			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

			zipFile.createZipFile(filesToAdd, parameters, true, 65536);
		} catch (ZipException e) {
			e.printStackTrace();
		}
	}

	// 通过流的方式添加文件到压缩包中
	public static void addStreamToZip() {

		InputStream is = null;
		try {
			ZipFile zipFile = new ZipFile("D:\\test\\AddStreamToZip.zip");

			ZipParameters parameters = new ZipParameters();
			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			parameters.setFileNameInZip("yourfilename.txt");
			parameters.setSourceExternalStream(true);

			is = new FileInputStream("D:\\test\\sample.txt");
			zipFile.addStream(is, parameters);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 解压压缩文件 在进行解压缩时需要判断文件是否为加密压缩，否则会抛出异常：ZipException: empty or null password
	 * provided for AES Decryptor
	 */
	public void extractAllFiles() {
		try {

			ZipFile zipFile = new ZipFile("d:\\test\\AddStreamToZip.zip");
			// 如果解压的文件需要密码，可以添加以下代码：
			if (zipFile.isEncrypted()) {
				zipFile.setPassword("test123!");
			}
			zipFile.extractAll("d:\\ZipTest1");

		} catch (ZipException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		// addStreamToZip();

		// createSplitZipFile();

		// addFolder();
		// addFilesToFolderInZip();
	}

}
