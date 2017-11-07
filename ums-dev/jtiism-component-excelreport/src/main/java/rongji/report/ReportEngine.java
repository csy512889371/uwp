package rongji.report;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rongji.framework.util.StringUtil;
import rongji.report.methods.Functions;
import rongji.report.model.DataEntry;
import rongji.report.model.ReportConfig;
import rongji.report.model.ReportContext;
import rongji.report.model.ReportContext.DealFileType;
import rongji.report.model.ReportContext.DealType;
import rongji.report.model.ReportRule;
import rongji.report.model.ReportTemplate;
import rongji.report.utils.ReportUtils;
import rongji.report.utils.Script;

import com.othelle.jtuples.Tuple2;

public class ReportEngine {

	private static final Logger logger = LoggerFactory.getLogger(ReportEngine.class);

	private ReportEngine() {

	}

	public static ReportEngine instance() {
		return new ReportEngine();
	}

	public void parseDealType(ReportConfig reportConfig, ReportContext context) {

		// 使用场景： 任免表 多文件 、单sheet
		if (DealFileType.MULFILE_SINGLESHEET.equals(context.getDealFileType())) {
			context.getDealMap().put(1, DealType.COPYFILE_SHEET);
		}

		// 使用场景： 任免表 单文件 多sheet
		if (DealFileType.SINGLEFILE_MULSHEET.equals(context.getDealFileType())) {
			context.getDealMap().put(1, DealType.COPYSHEET);
		}

		if (DealFileType.SINGLESHEET_MULLINE.equals(context.getDealFileType())) {
			context.getDealMap().put(1, DealType.COPYLINES);
		}
	}

	public ReportContext initReportInfo(ReportConfig reportConfig, ReportTemplate template) {
		ReportContext context = null;
		try {
			/**
			 * step1 设置报表上下文
			 */
			context = new ReportContext(reportConfig);

			/**
			 * step2 分装及处理命令流
			 */
			ReportRule reportRule = new ReportRule();
			reportRule.createBeanRules(template.getRules());

			reportRule.createMethods(reportConfig.getMethodPath());
			for (Entry<String, Tuple2<Functions, String>> method : reportRule.getMethods().entrySet()) {
				method.getValue()._1().setReportContext(context);
			}

			/**
			 * step3 根据报表模板设置处理方案
			 */
			DealFileType dealFileType = template.getDealFileType(reportConfig.getOutFileType());
			context.setDealFileType(dealFileType);
			parseDealType(reportConfig, context);

			context.setReportRule(reportRule);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return context;

	}

	/**
	 * 执行命令，生成文件
	 */
	public void excuteWithDataOfPart(DataEntry dataEntry, ReportContext context, ReportConfig reportConfig) {
		Script script = Script.instance();
		try {
			script.recurExcute(context.getReportRule(), context, dataEntry);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public String createReportFile(DataEntry dataEntry, ReportConfig reportConfig, ReportTemplate template) {

		ReportContext context = null;
		String path = "";
		try {
			/**
			 * step1 设置报表上下文
			 */
			context = new ReportContext(reportConfig);

			/**
			 * step2 分装及处理命令流
			 */
			ReportRule reportRule = new ReportRule();
			reportRule.createBeanRules(template.getRules());

			reportRule.createMethods(reportConfig.getMethodPath());
			for (Entry<String, Tuple2<Functions, String>> method : reportRule.getMethods().entrySet()) {
				method.getValue()._1().setReportContext(context);
			}

			/**
			 * step3 根据报表模板设置处理方案
			 */
			DealFileType dealFileType = template.getDealFileType(reportConfig.getOutFileType());
			context.setDealFileType(dealFileType);
			parseDealType(reportConfig, context);

			/**
			 * step4 执行命令，生成文件
			 */
			Script script = Script.instance();
			script.recurExcute(reportRule, context, dataEntry);

			/**
			 * step5 生成输出文件，并返回下载路径
			 */
			path = createOutPutFile(context);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (context != null) {
				context.clear();
			}
		}
		return path;
	}

	/**
	 * 
	 * @Title: createStatisticReport
	 * @Description:创建统计类报表
	 * @param reportConfig
	 * @param template
	 * @param dataSet
	 * @return void 返回类型
	 * @throws
	 * @author LZ
	 */
	public String createStatisticReport(ReportConfig reportConfig, ReportTemplate template, Map<String, Object> dataSet) {
		ReportContext context = null;
		String path = reportConfig.getRelativePath() + "/";
		try {
			/**
			 * step1 设置报表上下文
			 */
			context = new ReportContext(reportConfig);
			context.setDataSet(dataSet);

			/**
			 * step2 分装及处理命令流
			 */
			ReportRule reportRule = new ReportRule();
			reportRule.createStatisticsRules(template.getRules());

			reportRule.createMethods(reportConfig.getMethodPath());
			for (Entry<String, Tuple2<Functions, String>> method : reportRule.getMethods().entrySet()) {
				method.getValue()._1().setReportContext(context);
			}
			/**
			 * step3 执行命令，生成文件
			 */
			Script script = Script.instance();
			script.excuteStatisticsRule(reportRule, context);

			if (StringUtil.isEmpty(reportConfig.getExcelFileDir())) {
				String outputDir = reportConfig.getBaseFolderPath() + reportConfig.getRelativePath();
				File folder = new File(outputDir);
				folder.mkdirs();
				reportConfig.setExcelFileDir(outputDir);
			}
			path += ReportUtils.outPutExcelFile(context);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			path="";
		} finally {
			if (context != null) {
				context.clear();
			}
		}
		return path;
	}

	public String createOutPutFile(ReportContext context) {
		DealType fielDeal = context.getDealMap().get(1);
		ReportConfig config = context.getReportConfig();

		String path = config.getRelativePath() + "/";

		if (DealType.COPYFILE.equals(fielDeal)) {
			path += ReportUtils.zipExcelFileDir(context);
		} else if (DealType.COPYFILE_SHEET.equals(fielDeal)) {
			path += ReportUtils.zipExcelFileDir(context);
		} else if (DealType.COPYSHEET.equals(fielDeal)) {
			if (StringUtil.isEmpty(context.getReportConfig().getExcelFileDir())) {
				String outputDir = config.getBaseFolderPath() + config.getRelativePath();
				File folder = new File(outputDir);
				folder.mkdirs();
				config.setExcelFileDir(outputDir);
			}
			if (context.hasFile()) {
				// 输出剩余文件
				ReportUtils.outPutExcelFile(context);
			}
			if (ReportUtils.hasMultFile(context)) {
				// 有多文件则输出压缩包
				path += ReportUtils.zipExcelFileDir(context);
			} else if (ReportUtils.isSingleFile(context)) {
				// 单文件
				path += ReportUtils.getSingleExcelFile(context, path);
			}
		} else if (DealType.COPYLINES.equals(fielDeal)) {
			if (StringUtil.isEmpty(config.getExcelFileDir())) {
				String outputDir = config.getBaseFolderPath() + config.getRelativePath();
				File folder = new File(outputDir);
				folder.mkdirs();
				config.setExcelFileDir(outputDir);
			}
			if (StringUtil.isNotEmpty(context.getSheetName())) {
				context.getBookEngine().setCurSheetName(context.getSheetName());
			}
			path += ReportUtils.outPutExcelFile(context);
		}
		return path;
	}

}
