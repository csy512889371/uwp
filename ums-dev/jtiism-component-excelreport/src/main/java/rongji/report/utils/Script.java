package rongji.report.utils;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rongji.report.CDSBaseImpl;
import rongji.report.ReportEngine;
import rongji.report.methods.Functions;
import rongji.report.model.BeanRule;
import rongji.report.model.BeanRule.CommondType;
import rongji.report.model.DataEntry;
import rongji.report.model.ReportConfig;
import rongji.report.model.ReportContext;
import rongji.report.model.ReportContext.DealType;
import rongji.report.model.ReportRule;
import rongji.report.model.SqlInfo;
import bsh.EvalError;
import bsh.Interpreter;

import com.othelle.jtuples.Tuple2;

/**
 * 
 * @Title: Scrpit.java
 * @Package rongji.report.utils
 * @Description: 预执行脚本
 * @author liuzhen
 * @date 2017年3月1日 上午8:40:27
 * @version V1.0
 */
public class Script {

	private static final Logger logger = LoggerFactory.getLogger(ReportEngine.class);

	private Script() {

	}

	public static Script instance() {
		return new Script();
	}

	/**
	 * 
	 * @Title: recurExcute
	 * @Description: 递归执行
	 * @param level
	 * @param reportRule
	 * @param context
	 * @param methods
	 * @param inter
	 * @param entry
	 * @throws Exception
	 * @return void 返回类型
	 * @throws
	 * @author LZ
	 */
	public void recurExcute(ReportRule reportRule, ReportContext context, DataEntry entry) throws Exception {
		context.setCurLevel(entry.getLevel());
		// step1
		preDeal(entry.getLevel(), context, reportRule);
		// step2
		List<BeanRule> rules = reportRule.getCellRules(entry.getLevel());
		if (rules != null && rules.size() > 0) {
			context.setDataSet(entry.getDataInfo());
			excute(context, rules, reportRule.getMethods());

		}
		// step3
		if (!entry.isLeaf()) {
			for (DataEntry child : entry.getChilds()) {
				recurExcute(reportRule, context, child);
			}
		}
		// step4
		preDone(entry.getLevel(), context);

	}

	private void preDeal(Integer level, ReportContext context, ReportRule reportRule) throws Exception {
		DealType fielDeal = context.getDealMap().get(level);

		if (DealType.COPYFILE.equals(fielDeal)) {
			// 1.创建文件
			context.createNewBookEngine();
			context.getBookEngine().createFontInfos(context.getReportConfig().getFontsPath());
			// 2.初始化
			ReportUtils.newInter(context, reportRule);

		} else if (DealType.COPYFILE_SHEET.equals(fielDeal)) {
			// 1.创建文件
			context.createNewBookEngine();
			context.getBookEngine().createFontInfos(context.getReportConfig().getFontsPath());
			// 2.设置sheet
			context.setNextCurSheet(null);
			// 3.初始化
			ReportUtils.newInter(context, reportRule);
		} else if (DealType.COPYSHEET.equals(fielDeal)) {
			// 设置sheet下表
			if (!context.hasFile()) {
				// 1.创建文件
				context.createNewBookEngine();
				context.getBookEngine().createFontInfos(context.getReportConfig().getFontsPath());
				// 2.设置sheet
				context.setNextCurSheet(null);
				// 3.初始化
				ReportUtils.newInter(context, reportRule);
			} else {
				context.setNextCurSheet(null);
			}
		} else if (DealType.COPYLINES.equals(fielDeal)) {
			if (!context.hasFile()) {
				// 1.创建文件
				context.createNewBookEngine();
				context.getBookEngine().createFontInfos(context.getReportConfig().getFontsPath());
				// 2.设置sheet 及复制出一个模板，好复制行
				context.setNextCurSheet(0);
				// 3.初始化
				ReportUtils.newInter(context, reportRule);
			}
		}

	}

	private void preDone(Integer level, ReportContext context) {
		DealType fielDeal = context.getDealMap().get(level);

		if (DealType.COPYFILE.equals(fielDeal)) {
			// step1
			if (StringUtils.isEmpty(context.getReportConfig().getExcelFileDir())) {
				// 创建多文件的文件夹目录
				ReportUtils.createExcelFilesDir(context);
			}
			// step2
			ReportUtils.outPutExcelFile(context);
		} else if (DealType.COPYFILE_SHEET.equals(fielDeal)) {
			// step1
			if (StringUtils.isEmpty(context.getReportConfig().getExcelFileDir())) {
				// 创建多文件的文件夹目录
				ReportUtils.createExcelFilesDir(context);
			}
			if (StringUtils.isNotEmpty(context.getSheetName())) {
				context.getBookEngine().setCurSheetName(context.getSheetName());
			}
			// step2
			ReportUtils.outPutExcelFile(context);
		} else if (DealType.COPYSHEET.equals(fielDeal)) {
			if (StringUtils.isNotEmpty(context.getSheetName())) {
				context.getBookEngine().setCurSheetName(context.getSheetName());
			}
			context.resetSheet();

			/**
  			 * 超过限额sheet，则输出一个文件
			 */
			Integer sheetNums = context.getBookEngine().getBook().getNumberOfSheets();
			if (context.getBookEngine().hasCopySheet()) {
				sheetNums--;
			}
			if (sheetNums >= context.getReportConfig().getMaxSheetCount()) {
				// step1
				if (StringUtils.isEmpty(context.getReportConfig().getExcelFileDir())) {
					// 创建多文件的文件夹目录
					ReportUtils.createExcelFilesDir(context);
				}
				// step2
				ReportUtils.outPutExcelFile(context);
			}
		}
	}

	/**
	 * 
	 * @Title: excute
	 * @Description: 处理一次遍历
	 * @param context
	 * @param inter
	 * @param beanRules
	 * @param methods
	 * @throws Exception
	 * @return void 返回类型
	 * @throws
	 * @author LZ
	 */
	public void excute(ReportContext context, List<BeanRule> beanRules, Map<String, Tuple2<Functions, String>> methods) {
		
		ReportConfig config = context.getReportConfig();
		for (BeanRule rule : beanRules) {
			try {
				Interpreter inter = context.getInter();
				if (CommondType.assign.equals(rule.getCommendType())) {
					/**
					 * > 赋值命令
					 */
					CDSBaseImpl cds = (CDSBaseImpl) Class.forName(config.getCdsClassName()).newInstance();
					inter.set("cds", cds);
					cds.setMethods(methods);
					cds.setTarget(rule);

					inter.eval(rule.getCommond());
					cds.excute();
					setVariable(inter, rule.getTarget().split(","));
				} else {
					/**
					 * >> 代码块命令
					 */
					if (rule.getCommond().indexOf("cds.") >= 0) {
						CDSBaseImpl cds = (CDSBaseImpl) Class.forName(config.getCdsClassName()).newInstance();
						inter.set("cds", cds);
						cds.setMethods(methods);
						inter.eval(rule.getCommond());
						cds.excute();
					} else {
						inter.eval(rule.getCommond());
					}
				}
			} catch (Exception e) {
				logger.error(rule.getCommond() + "\r\n" + e.getMessage(), e);
			}
		}
	}

	public void excuteStatisticsRule(ReportRule reportRule, ReportContext context) throws Exception {
		ReportConfig config = context.getReportConfig();
		
		// step1
		if (!context.hasFile()) {
			// 1.创建文件
			context.createNewBookEngine();
			context.getBookEngine().createFontInfos(context.getReportConfig().getFontsPath());
			// 2.设置sheet 及复制出一个模板，好复制行
			context.setCurSheet(0);
			context.setCurLevel(0);
			// 3.初始化
			ReportUtils.newInter(context, reportRule);
		}
		// step2
		if (reportRule.getGlobalRules() != null && reportRule.getGlobalRules().size() > 0) {
			excuteStaticstic(context, reportRule.getGlobalRules(), reportRule.getMethods(), "global");
		}
		if (reportRule.getRowRules() != null && reportRule.getRowRules().size() > 0) {
			excuteStaticstic(context, reportRule.getRowRules(), reportRule.getMethods(), "row");
		}
		if (reportRule.getColumnRules() != null && reportRule.getColumnRules().size() > 0) {
			excuteStaticstic(context, reportRule.getColumnRules(), reportRule.getMethods(), "column");
		}

		SqlInfo commonSqlInfo = context.getCommonSql();
		commonSqlInfo = commonSqlInfo == null ? new SqlInfo() : commonSqlInfo;
		for (CDSBaseImpl columnCds : context.getColumnsCds()) {
			String columnTarget = columnCds.getBeanRule().getTarget();
			for (CDSBaseImpl rowCds : context.getRowsCds()) {
				try {
					SqlInfo sqlInfo = columnCds.getSqlInfo().addNewSqlInfo(rowCds.getSqlInfo()).addNewSqlInfo(commonSqlInfo);
					String rowTarget = rowCds.getBeanRule().getTarget();
					String target = columnTarget + rowTarget;
					BeanRule rule = new BeanRule(">> ");
					rule.setTarget(target);
					CDSBaseImpl cds = (CDSBaseImpl) Class.forName(config.getCdsClassName()).newInstance();
					cds.setMethods(reportRule.getMethods());
					cds.setTarget(rule);
					cds.sql(sqlInfo.createASql());
					cds.addValueHolders(rowCds.getHolderHead());
					cds.addValueHolders(columnCds.getHolderHead());
					cds.setObject();
					cds.excute();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		context.getCurSheet().setForceFormulaRecalculation(true);
	}

	public void excuteStaticstic(ReportContext context, List<BeanRule> beanRules, Map<String, Tuple2<Functions, String>> methods, String type) {
		ReportConfig config = context.getReportConfig();
		
		for (BeanRule rule : beanRules) {
			try {
				Interpreter inter = context.getInter();
				if (CommondType.assign.equals(rule.getCommendType())) {
					/**
					 * > 赋值命令
					 */
					CDSBaseImpl cds = (CDSBaseImpl) Class.forName(config.getCdsClassName()).newInstance();
					inter.set("cds", cds);
					cds.setMethods(methods);
					cds.setTarget(rule);
					inter.eval(rule.getCommond());
					if (cds.getSqlInfo() != null) {
						setCdsToContext(cds, context, type);
					} else {
						cds.excute();
						setVariable(inter, rule.getTarget().split(","));
					}
				} else {
					/**
					 * >> 代码块命令
					 */
					if (rule.getCommond().indexOf("cds.") >= 0) {
						CDSBaseImpl cds = (CDSBaseImpl) Class.forName(config.getCdsClassName()).newInstance();
						inter.set("cds", cds);
						cds.setMethods(methods);
						inter.eval(rule.getCommond());
						if (cds.getSqlInfo() != null) {
							setCdsToContext(cds, context, type);
						} else {
							cds.excute();
						}
					} else {
						inter.eval(rule.getCommond());
					}
				}
			} catch (Exception e) {
				logger.error(rule.getCommond() + "\r\n" + e.getMessage(), e);
			}
		}
	}

	private void setCdsToContext(CDSBaseImpl cds, ReportContext context, String type) {
		if ("global".equals(type)) {
			context.appendCommonSql(cds.getSqlInfo());
		} else if ("row".equals(type)) {
			context.getRowsCds().add(cds);
		} else if ("column".equals(type)) {
			context.getColumnsCds().add(cds);
		}
	}

	/**
	 * 
	 * @Title: setVariable
	 * @Description: 设置变量到Interpreter作用域中
	 * @param inter
	 * @param targets
	 * @throws EvalError
	 * @return void 返回类型
	 * @throws
	 * @author LZ
	 */
	public void setVariable(Interpreter inter, String[] targets) {
		for (String target : targets) {
			if (target.startsWith("$")) {
				try {
					inter.eval(target + "=context.getParam(\"" + target + "\")");
				} catch (EvalError e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

}
