package rongji.report.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import rongji.framework.util.ZipUtils;
import rongji.report.model.BeanRule;
import rongji.report.model.DataEntry;
import rongji.report.model.ReportConfig;
import rongji.report.model.ReportContext;
import rongji.report.model.ReportRule;
import bsh.Interpreter;

public class ReportUtils {

	private static final Logger logger = LoggerFactory.getLogger(ReportUtils.class);

	public static Integer targetToCol(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
				String cols = str.substring(0, i).toUpperCase();
				int index = cols.charAt(cols.length() - 1) - 65;
				for (int j = 1; j < cols.length(); j++) {
					index += ((cols.charAt(cols.length() - 1 - j) - 65) + 1) * Math.pow(26, j);
				}
				return index;
			}
		}
		return -1;
	}

	public static Integer targetToRow(String key) {
		String str = key.toUpperCase();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
				return Integer.parseInt(str.substring(i)) - 1;
			}
		}
		return -1;
	}

	public static String targetAddRows(String target, Integer step) {
		Integer index = -1;
		for (int i = 0; i < target.length(); i++) {
			if (target.charAt(i) >= 48 && target.charAt(i) <= 57) {
				index = i;
				break;
			}
		}
		return target.substring(0, index) + (Integer.parseInt(target.substring(index)) + step);
	}

	public static byte[] getImageBytes(String imgFile) {
		File file = new File(imgFile);
		if (!file.exists()) {
			return new byte[0];
		}
		InputStream in = null;
		byte[] data = null;
		try {
			in = new FileInputStream(file);
			data = new byte[in.available()];
			in.read(data);
		} catch (IOException e) {
		} finally {
			try {
				if (in != null) {
					IOUtils.closeQuietly(in);
				}
				in.close();
			} catch (Exception e) {
			}
		}
		return data;
	}

	public static void newInter(ReportContext context, ReportRule reportRule) throws Exception {

		File funcFile = new ClassPathResource(context.getReportConfig().getFunctionsPath()).getFile();
		Interpreter inter = new Interpreter();
		BufferedReader designFis = new BufferedReader(new InputStreamReader(new FileInputStream(funcFile)));
		inter.eval(designFis);
		IOUtils.closeQuietly(designFis);
		Integer level = context.getCurLevel();
		context.setCurLevel(0);
		inter.set("context", context);
		context.setInter(inter);
		List<BeanRule> inputRules = reportRule.getInputRules();

		if (inputRules != null && !inputRules.isEmpty()) {
			Script script = Script.instance();
			script.excute(context, inputRules, reportRule.getMethods());
			// Add 增加之前的变量到作用域
			script.setVariable(inter, context.getSheetParams().keySet().toArray(new String[] {}));
			script = null;
		}

		context.setCurLevel(level);
	}

	/**
	 * 
	 * @Title: createExcelFilesDir
	 * @Description: 创建多excel的文件夹目录
	 * @param context
	 * @return void 返回类型
	 * @throws
	 * @author LZ
	 */
	public static void createExcelFilesDir(ReportContext context) {
		ReportConfig config = context.getReportConfig();
		String outputDir = config.getBaseFolderPath() + config.getRelativePath();
		String zipFileName = "";
		String templName = ReportStringUtil.removeSpace(config.getTemplateName());
		if (StringUtils.isNotEmpty(templName)) {
			zipFileName = templName + DateUtil.getCurrentDateStr();
		} else {
			zipFileName = "报表" + DateUtil.getCurrentDateStr();
		}
		File zipFolder = new File(outputDir + "/" + zipFileName);
		zipFolder.mkdirs();
		config.setExcelFileDir(zipFolder.getAbsolutePath());
	}

	/**
	 * 
	 * @Title: outPutExcelFile
	 * @Description: 生成单个excel文件输出到缓存目录
	 * @param context
	 * @return
	 * @return String 返回类型
	 * @throws
	 * @author LZ
	 */
	public static String outPutExcelFile(ReportContext context) {
		String excelName = null;
		if (StringUtils.isNotEmpty(context.getFileName())) {
			excelName = context.solveSameFileName(context.getFileName()) + ".xls";
		} else if (StringUtils.isNotEmpty(context.getReportConfig().getTemplateName())) {
			String name = ReportStringUtil.removeSpace(context.getReportConfig().getTemplateName());
			excelName = context.solveSameFileName(name) + ".xls";
		} else {
			excelName = UUID.randomUUID().toString() + ".xls";
		}
		context.getBookEngine().removeCopySheet();

		FileOutputStream fileOut = null;
		File excelFile = new File(context.getReportConfig().getExcelFileDir() + "/" + excelName);
		try {
			fileOut = new FileOutputStream(excelFile);
			context.getBook().write(fileOut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			context.resetFile();
			if (fileOut != null) {
				IOUtils.closeQuietly(fileOut);
			}
		}
		return excelName;
	}

	public static String zipExcelFileDir(ReportContext context) {
		File zipFile = ZipUtils.zip(context.getReportConfig().getExcelFileDir());
		return zipFile.getName();

	}

	/**
	 * 
	 * @Title: hasMultFile
	 * @Description: 判断该目录是否为多文件目录
	 * @param context
	 * @return
	 * @return boolean 返回类型
	 * @throws
	 * @author LZ
	 */
	public static boolean hasMultFile(ReportContext context) {
		if (StringUtils.isEmpty(context.getReportConfig().getExcelFileDir())) {
			return false;
		}
		File folder = new File(context.getReportConfig().getExcelFileDir());
		if (folder.isDirectory()) {
			if (folder.listFiles().length > 1) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSingleFile(ReportContext context) {
		if (StringUtils.isEmpty(context.getReportConfig().getExcelFileDir())) {
			return false;
		}
		File folder = new File(context.getReportConfig().getExcelFileDir());
		if (folder.isDirectory()) {
			if (folder.listFiles().length == 1) {
				return true;
			}
		}
		return false;
	}

	public static String getSingleExcelFile(ReportContext context, String relaPath) {
		String path = "";
		if (StringUtils.isEmpty(context.getReportConfig().getExcelFileDir())) {
			return "";
		}
		File folder = new File(context.getReportConfig().getExcelFileDir());
		if (!folder.isDirectory()) {
			return "";
		}
		if (folder.listFiles().length == 0) {
			return "";
		}
		File file = folder.listFiles()[0];
		if (!file.isFile()) {
			return "";
		}

		if (relaPath.indexOf(folder.getName()) == -1) {
			path = folder.getName() + "/";
		}

		return path + file.getName();
	}

	/**
	 * 
	 * @Title: getDataEntry
	 * @Description: 获取只有两级数据关系的DataEntry
	 * @param objs
	 * @return
	 * @return DataEntry 返回类型
	 * @throws
	 * @author LZ
	 */
	public static DataEntry buildDataEntry(List<Object> objs) {

		// 根节点
		DataEntry dataEntry = new DataEntry("root");
		dataEntry.setLevel(0);

		for (Object obj : objs) {
			Map<String, Object> dataInfo = new HashMap<String, Object>();
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Object val = null;
				try {

					val = field.get(obj);

					if (val instanceof Collection) {
						continue;
					}

					dataInfo.put(field.getName().toUpperCase(), val);

				} catch (IllegalArgumentException e) {
					logger.error(e.getMessage(), e);
				} catch (IllegalAccessException e) {
					logger.error(e.getMessage(), e);
				}

			}
			DataEntry child = new DataEntry("enter");
			child.setDataInfo(dataInfo);
			dataEntry.addChild(child);
		}
		return dataEntry;
	}

}
