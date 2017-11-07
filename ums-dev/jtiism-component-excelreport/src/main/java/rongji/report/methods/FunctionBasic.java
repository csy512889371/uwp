package rongji.report.methods;

import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import rongji.framework.base.exception.ApplicationException;
import rongji.framework.util.SpringUtils;
import rongji.framework.util.StringUtil;
import rongji.report.CDSBaseImpl.MethodOverloadType;
import rongji.report.model.ReportContext;
import rongji.report.model.ReportContext.CurValueType;
import rongji.report.model.WorkBookEngine;
import rongji.report.utils.DateUtil;
import rongji.report.utils.POIXlsUtils;
import rongji.report.utils.ReportUtils;
import bsh.EvalError;

import com.othelle.jtuples.Tuple;
import com.othelle.jtuples.Tuple1;
import com.othelle.jtuples.Tuple2;
import com.othelle.jtuples.Tuple3;
import com.othelle.jtuples.Tuple4;
import com.othelle.jtuples.Tuple5;

import freemarker.template.Template;

public class FunctionBasic extends Functions {
	
	private static final Logger logger = LoggerFactory.getLogger(FunctionBasic.class);

	public FunctionBasic() {
	}

	@Override
	public void setReportContext(ReportContext context) {
		this.reportContext = context;
	}

	public void getDataSet(Tuple nullVal, MethodOverloadType type) {
		reportContext.setCurValue(reportContext.getDataSet(), CurValueType.MAPVAL);
	}

	@SuppressWarnings("unchecked")
	public void getParam(Tuple paramTup, MethodOverloadType type) {
		String name = ((Tuple1<String>) paramTup)._1();
		Object value = reportContext.getParam(name);
		if (isInstance(value, Map.class)) {
			reportContext.setCurValue(value, CurValueType.MAPVAL);
		} else if (isInstance(value, List.class, Map.class)) {
			reportContext.setCurValue(value, CurValueType.LISTMAPVAL);
		} else if (isInstance(value, List.class)) {
			reportContext.setCurValue(value, CurValueType.LISTVAL);
		} else {
			reportContext.setCurValue(value, CurValueType.OBJECTVAL);
		}
	}

	@SuppressWarnings("unchecked")
	public void setCell(Tuple valTup, MethodOverloadType type) {
		Object value = ((Tuple1<Object>) valTup)._1();
		logger.debug("FunctionBasic.setCell value:" + value);
		reportContext.setCurValue(value, CurValueType.OBJECTVAL);
	}

	@SuppressWarnings("unchecked")
	public void get(Tuple paramTup, MethodOverloadType type) {
		String name = ((Tuple1<String>) paramTup)._1();
		if (isInstance(reportContext.getCurValue(), Map.class)) {
			Map<String, Object> map = (Map<String, Object>) reportContext.getCurValue();
			Object obj = null;
			if (map != null) {
				obj = map.get(name);
			}
			reportContext.setCurValue(obj, CurValueType.OBJECTVAL);
		} else {
			throw new ApplicationException("入参的类型不正确");
		}
	}

	@SuppressWarnings("unchecked")
	public void index(Tuple indexTup, MethodOverloadType type) {
		Integer index = ((Tuple1<Integer>) indexTup)._1();
		Object obj = reportContext.getCurValue();
		if (obj == null) {
			reportContext.setCurValue(null, CurValueType.OBJECTVAL);
		}
		if (isInstance(reportContext.getCurValue(), List.class)) {
			List<Object> list = (List<Object>) obj;
			if (list == null || list.size() < index + 1) {
				reportContext.setCurValue(null, CurValueType.OBJECTVAL);
			} else {
				Object val = list.get(index);
				if (isInstance(val, Map.class)) {
					reportContext.setCurValue(val, CurValueType.MAPVAL);
				} else {
					reportContext.setCurValue(val, CurValueType.OBJECTVAL);
				}
			}
		} else {
			throw new ApplicationException("入参的类型不正确");
		}
	}

	public void getCurDate(Tuple nullTup, MethodOverloadType type) {
		reportContext.setCurValue(new Date(), CurValueType.DATEVAL);
	}
	
	@SuppressWarnings("unchecked")
	public void getDateBeforeYear(Tuple beforeYear, MethodOverloadType type) {
		Date date = null;
		Integer year = ((Tuple1<Integer>) beforeYear)._1();
		try {
			String dateStr = DateUtil.getDateBeforeYear(new Date(), year);
			date = DateUtil.getDate(dateStr, "yyyyMMdd");
		} catch (Exception e) {
			logger.error("获取前几年时间失败" + e.getMessage());
		}
		
		reportContext.setCurValue(date, CurValueType.DATEVAL);
	}

	@SuppressWarnings("unchecked")
	public void ageToBirthdate(Tuple ageTup, MethodOverloadType type) {
		Integer age = ((Tuple1<Integer>) ageTup)._1();
		reportContext.setCurValue(DateUtil.ageToBirthdate(age), CurValueType.DATEVAL);
	}

	@SuppressWarnings("unchecked")
	public void sql(Tuple sqlTup, MethodOverloadType type) throws Exception {
		String sql = ((Tuple1<String>) sqlTup)._1();
		FreeMarkerConfigurer freeMarkerConfigurer = SpringUtils.getBean("freeMarkerConfigurer", FreeMarkerConfigurer.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(this.reportContext.getGlobalParams());
		map.putAll(this.reportContext.getSheetParams());
		Template template = new Template("adTemplate", new StringReader(sql), freeMarkerConfigurer.getConfiguration());
		String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
		JdbcTemplate jdbcTem = SpringUtils.getBean("jdbcTemplate", JdbcTemplate.class);
		reportContext.setCurValue(jdbcTem.queryForList(text), CurValueType.LISTMAPVAL);
	}

	@SuppressWarnings("unchecked")
	public void cache(Tuple paramTup, MethodOverloadType type) {
		String name = ((Tuple1<String>) paramTup)._1();
		this.reportContext.getSheetParams().put(name, this.reportContext.getCurValue());
		try {
			this.reportContext.getInter().set(name, this.reportContext.getCurValue());
		} catch (EvalError e) {
			throw new ApplicationException("设置变量错误");
		}
	}

	@SuppressWarnings("unchecked")
	public void dateFormate(Tuple paramTup, MethodOverloadType type) throws ApplicationException {
		if (MethodOverloadType.sec.equals(type)) {
			Tuple2<String, String> param = (Tuple2<String, String>) paramTup;
			String name = param._1();
			String pattern = param._2();
			if (CurValueType.MAPVAL.equals(reportContext.getCurValueType())) {
				Map<String, Object> map = (Map<String, Object>) reportContext.getCurValue();
				String val = DateUtil.formatDateToStringWithSpace((Date) map.get(name), pattern);
				map.put(name, val);
			} else if (CurValueType.LISTMAPVAL.equals(reportContext.getCurValueType())) {
				List<Map<String, Object>> vals = (List<Map<String, Object>>) reportContext.getCurValue();
				for (Map<String, Object> map : vals) {
					String val = DateUtil.formatDateToStringWithSpace((Date) map.get(name), pattern);
					map.put(name, val);
				}
			} else {
				throw new ApplicationException("入参的类型不正确");
			}
		} else {
			String pattern = ((Tuple1<String>) paramTup)._1();
			if (isInstance(reportContext.getCurValue(), Date.class)) {
				Date date = (Date) reportContext.getCurValue();
				String dateStr = DateUtil.formatDateToStringWithSpace(date, pattern);
				reportContext.setCurValue(dateStr, CurValueType.STRINGVAL);
			} else {
				throw new ApplicationException("birthday的前参必须为Date类型");
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void separator(Tuple separaTup, MethodOverloadType type) throws Exception {
		String separa = ((Tuple1<String>) separaTup)._1();

		if (CurValueType.LISTVAL.equals(reportContext.getCurValueType())) {
			List<Object> coll = (List<Object>) reportContext.getCurValue();
			String val = "";
			for (Object str : coll) {
				val += str.toString() + separa;
			}
			if (val.length() > 0) {
				val.substring(0, val.lastIndexOf(separa));
			}
			reportContext.setCurValue(val, CurValueType.STRINGVAL);
		} else {
			throw new Exception("separator的前参必须为集合");
		}
	}

	@SuppressWarnings("unchecked")
	public void count(Tuple paramTup, MethodOverloadType type) throws Exception {
		if (isInstance(reportContext.getCurValue(), List.class)) {
			List<Object> coll = (List<Object>) reportContext.getCurValue();
			reportContext.setCurValue(coll.size(), CurValueType.OBJECTVAL);
		} else {
			throw new Exception("separator的前参必须为集合");
		}
	}

	@SuppressWarnings("unchecked")
	public void avgAge(Tuple paramTup, MethodOverloadType type) throws Exception {
		Tuple2<String, String> param = (Tuple2<String, String>) paramTup;
		String birthKey = param._1();
		String dividendKey = param._2();
		if (CurValueType.LISTMAPVAL.equals(reportContext.getCurValueType())) {
			List<Map<String, Object>> vals = (List<Map<String, Object>>) reportContext.getCurValue();
			Set<Object> dividendNum = new HashSet<Object>();
			double total = 0.0f;
			for (Map<String, Object> map : vals) {
				Object dividend = map.get(dividendKey);
				if (dividend != null) {
					dividendNum.add(dividend);
				}
				Date birthday = (Date) map.get(birthKey);
				total += DateUtil.getCurrentAge(birthday);
			}
			if (dividendNum.isEmpty()) {
				reportContext.setCurValue("", CurValueType.STRINGVAL);
			} else {
				double avg = total / dividendNum.size();
				reportContext.setCurValue(Double.valueOf(new DecimalFormat("0.00").format(avg)), CurValueType.OBJECTVAL);
			}

		} else {
			throw new Exception("separator的前参必须为集合");
		}
	}

	@SuppressWarnings("unchecked")
	public void buildInQuery(Tuple paramTup, MethodOverloadType type) throws Exception {
		Tuple2<String, List<String>> param = (Tuple2<String, List<String>>) paramTup;
		String paramaNme = param._1();
		List<String> inValues = param._2();
		String inParam = StringUtil.getInParameter(inValues, paramaNme);
		if (inParam.length() > 3) {
			inParam = inParam.substring(3);
		}
		reportContext.setCurValue(inParam, CurValueType.STRINGVAL);
	}

	@SuppressWarnings("unchecked")
	public void autoHeight(Tuple cellInfoTup, MethodOverloadType type) {
		Tuple1<String> cellInfo = (Tuple1<String>) cellInfoTup;
		String target = cellInfo._1();

		WorkBookEngine bookEngine = reportContext.getBookEngine();
		Integer row = reportContext.getShiftRows() + ReportUtils.targetToRow(target);
		Integer col = ReportUtils.targetToCol(target);

		HSSFCell cell = bookEngine.getCurSheet().getRow(row).getCell(col);
		POIXlsUtils.setAutoFitRowHeight(bookEngine, cell, col);
	}

	@SuppressWarnings("unchecked")
	public void autoFit(Tuple cellInfoTup, MethodOverloadType type) {
		Tuple3<String, Integer, Integer> cellInfo = (Tuple3<String, Integer, Integer>) cellInfoTup;
		String target = cellInfo._1();
		Integer width = cellInfo._2();
		Integer height = cellInfo._3();

		WorkBookEngine bookEngine = reportContext.getBookEngine();
		Integer row = reportContext.getShiftRows() + ReportUtils.targetToRow(target);
		Integer col = ReportUtils.targetToCol(target);

		HSSFCell cell = bookEngine.getCurSheet().getRow(row).getCell(col);
		String val = cell.getStringCellValue();
		List<String> values = Arrays.asList(val.split("\r\n"));
		POIXlsUtils.getExcelSuitFont(bookEngine, values, target, width, height);
		POIXlsUtils.setExcelStlyeFont(bookEngine, cell, target);
	}

	@SuppressWarnings("unchecked")
	public void autoResumeFit(Tuple cellInfoTup, MethodOverloadType type) {
		Tuple3<String, Integer, Integer> cellInfo = (Tuple3<String, Integer, Integer>) cellInfoTup;
		String target = cellInfo._1();
		Integer width = cellInfo._2();
		Integer height = cellInfo._3();

		WorkBookEngine bookEngine = reportContext.getBookEngine();
		Integer row = ReportUtils.targetToRow(target);
		Integer col = ReportUtils.targetToCol(target);
		HSSFCell cell = bookEngine.getCurSheet().getRow(row).getCell(col);
		String val = cell.getStringCellValue();
		String resume = POIXlsUtils.getExcelResmueFontAndHanging(bookEngine, val, target, width, height);
		cell.setCellValue(resume);
		POIXlsUtils.setExcelStlyeFont(bookEngine, cell, target);
	}

	@SuppressWarnings("unchecked")
	public void autoFitEach(Tuple cellInfoTup, MethodOverloadType type) {
		Tuple5<String, Integer, Integer, Integer, Integer> cellInfo = (Tuple5<String, Integer, Integer, Integer, Integer>) cellInfoTup;
		String target = cellInfo._1();
		Integer step = cellInfo._2();
		Integer max = cellInfo._3();
		Integer width = cellInfo._4();
		Integer height = cellInfo._5();

		WorkBookEngine bookEngine = reportContext.getBookEngine();
		Integer col = ReportUtils.targetToCol(target);
		for (int i = 0; i < max; i++) {
			String newTarget = ReportUtils.targetAddRows(target, step * i);
			Integer row = reportContext.getShiftRows() + ReportUtils.targetToRow(newTarget);
			HSSFCell cell = bookEngine.getCurSheet().getRow(row).getCell(col);
			if (cell == null) {
				continue;
			}
			String val = cell.getStringCellValue();
			if (StringUtils.isEmpty(val)) {
				continue;
			}
			List<String> values = Arrays.asList(val.split("\r\n"));
			POIXlsUtils.getExcelSuitFont(bookEngine, values, newTarget, width, height);
			POIXlsUtils.setExcelStlyeFont(bookEngine, cell, target);
		}
	}

	@SuppressWarnings("unchecked")
	public void setSheetName(Tuple nameTup, MethodOverloadType type) {
		String objName = ((Tuple1<String>) nameTup)._1();
		this.reportContext.setSheetName(objName);
	}

	@SuppressWarnings("unchecked")
	public void setFileName(Tuple nameTup, MethodOverloadType type) {
		String objName = ((Tuple1<String>) nameTup)._1();
		this.reportContext.setFileName(objName);
	}

	@SuppressWarnings("unchecked")
	public void copyRowsAndAddShiftLine(Tuple copyParamTup, MethodOverloadType type) {
		Tuple3<Integer, Integer, Integer> copyParam = (Tuple3<Integer, Integer, Integer>) copyParamTup;
		Integer startRow = copyParam._1();
		Integer endRow = copyParam._2();
		Integer tarStartRow = copyParam._3();
		if (startRow == null || startRow < 1) {
			throw new ApplicationException("复制模板的开始行必须大于等于1");
		}

		if (endRow == null || startRow > endRow) {
			throw new ApplicationException("复制模板的结束必须大于等于开始行");
		}
		if (tarStartRow == null || tarStartRow < 0) {
			throw new ApplicationException("被复制的开始行必须大于等于1");
		}
		// 将展示的行数（1开始），改为使用的行数（0开始）
		startRow--;
		endRow--;
		tarStartRow--;
		Integer step = endRow - startRow + 1;
		this.reportContext.setRowsNumInCurLevel(step);
		this.reportContext.setCurLevelShiftRows();
		POIXlsUtils.copyRows(this.reportContext.getBookEngine(), startRow, endRow, this.reportContext.getShiftRows() + tarStartRow);

	}

	@SuppressWarnings("unchecked")
	public void copyARowWithStartTempRow(Tuple copyParamTup, MethodOverloadType type) {
		Tuple1<Integer> copyParam = (Tuple1<Integer>) copyParamTup;
		Integer startRow = copyParam._1();
		if (startRow == null || startRow < 1) {
			throw new ApplicationException("复制模板的开始行必须大于等于1");
		}
		// 将展示的行数（1开始），改为使用的行数（0开始）
		startRow--;
		Integer step = 1;
		this.reportContext.setRowsNumInCurLevel(step);
		this.reportContext.setCurLevelShiftRows();
		POIXlsUtils.copyRows(this.reportContext.getBookEngine(), startRow, startRow, this.reportContext.getShiftRows() + startRow);

	}

	/**
	 * 
	 * 赋值方法
	 */
	@SuppressWarnings("unchecked")
	public void setObject(Tuple targetTup, MethodOverloadType type) {
		String target = ((Tuple1<String>) targetTup)._1();
		logger.debug("FunctionBasic.setObject target:" + target);
		if (target.startsWith("$")) {
			logger.debug("FunctionBasic.setObject paramVal:" + reportContext.getCurValue());
			reportContext.getSheetParams().put(target, reportContext.getCurValue());
		} else {
			POIXlsUtils.setCell(reportContext, target, reportContext.getCurValue());
		}
	}

	@SuppressWarnings("unchecked")
	public void fill(Tuple paramTup, MethodOverloadType type) throws ApplicationException {
		Tuple2<String, String[]> param = (Tuple2<String, String[]>) paramTup;
		String target = param._1();
		List<String> names = Arrays.asList(param._2());
		List<String> targets = Arrays.asList(target.split(","));

		if (names.size() > 0 && names.size() != targets.size()) {
			throw new ApplicationException("‘指代参数’的个数要同目标一一对应");
		}
		if (CurValueType.MAPVAL.equals(reportContext.getCurValueType())) {
			Map<String, Object> map = (Map<String, Object>) reportContext.getCurValue();
			int j = 0;
			if (names.size() > 0) {
				for (String name : names) {
					Object val = map.get(name);
					if (targets.get(j).startsWith("$")) {
						reportContext.getSheetParams().put(targets.get(j), val);
					} else {
						POIXlsUtils.setCell(reportContext, targets.get(j), val);
					}
					j++;
				}
			} else {
				for (Object val : map.values()) {
					if (targets.get(j).startsWith("$")) {
						reportContext.getSheetParams().put(targets.get(j), val);
					} else {
						POIXlsUtils.setCell(reportContext, targets.get(j), val);
					}
					j++;
				}
			}
		} else if (reportContext.getCurValue() != null) {
			throw new ApplicationException("入参的类型必须为Map类型");
		}
	}

	@SuppressWarnings("unchecked")
	public void fillEach(Tuple paramTup, MethodOverloadType type) throws ApplicationException {
		Tuple4<String, Integer, Integer, String[]> param = (Tuple4<String, Integer, Integer, String[]>) paramTup;
		String[] targets = param._1().split(",");
		Integer step = param._2();
		Integer max = param._3();
		String[] names = param._4();
		if (names != null && names.length > 0) {
			if (names.length != targets.length) {
				throw new ApplicationException("‘指代参数’的个数要同目标一一对应");
			}
		}
		if (CurValueType.LISTMAPVAL.equals(reportContext.getCurValueType())) {
			int i = 0;
			for (Map<String, Object> map : (List<Map<String, Object>>) reportContext.getCurValue()) {
				if (i >= max) {
					break;
				}
				if (names != null && names.length > 0) {
					int j = 0;
					for (String name : names) {
						String newTarget = ReportUtils.targetAddRows(targets[j], step * i);
						POIXlsUtils.setCell(reportContext, newTarget, map.get(name));
						j++;
					}
				} else {
					int j = 0;
					for (Object val : map.values()) {
						String newTarget = ReportUtils.targetAddRows(targets[j], step * i);
						POIXlsUtils.setCell(reportContext, newTarget, val);
						j++;
					}
				}
				i++;
			}
		} else if (CurValueType.LISTVAL.equals(reportContext.getCurValueType())) {
			int i = 0;
			for (Object val : (List<Object>) reportContext.getCurValue()) {
				if (i >= max) {
					break;
				}
				String newTarget = ReportUtils.targetAddRows(targets[0], step * i);
				POIXlsUtils.setCell(reportContext, newTarget, val);
				i++;
			}
		} else {
			throw new ApplicationException("入参的类型必须为List类型");
		}
	}

	@SuppressWarnings("unchecked")
	public void setPicture(Tuple cellInfoTup, MethodOverloadType type) throws ApplicationException {
		Tuple1<String> cellInfo = (Tuple1<String>) cellInfoTup;
		String[] targets = cellInfo._1().split(",");
		if (targets.length != 2) {
			throw new ApplicationException("设置图片需要左上顶点与右下顶点两个坐标");
		}
		if (CurValueType.MAPVAL.equals(reportContext.getCurValueType())) {
			Map<String, Object> dataMap = (Map<String, Object>) this.reportContext.getCurValue();
			if (dataMap.containsKey("imageType") && dataMap.containsKey("imByte")) {
				String iType = (String) dataMap.get("imageType");
				byte[] imByte = (byte[]) dataMap.get("imByte");
				POIXlsUtils.setPicture(reportContext, iType, imByte, targets[0], targets[1]);
			}
		} else {
			throw new ApplicationException("入参的类型必须为Map类型");
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void setGlobalParam(Tuple cellInfoTup, MethodOverloadType type) throws ApplicationException {
		Tuple2<String, Object> params = (Tuple2<String, Object>) cellInfoTup;
		String paramName = params._1();
		Object paramVal = params._2();
		reportContext.getGlobalParams().put(paramName, paramVal);
	}
	

	/**
	 * 
	 * 输出“是/否”
	 * ignoreEmpty = true 时候, 空字符串 不转成“否”
	 * ignoreEmpty = false 空转成“否”
	 */
	@SuppressWarnings("unchecked")
	public void yesOrno(Tuple paramTup, MethodOverloadType type) throws ApplicationException {
		Tuple1<Boolean> param = (Tuple1<Boolean>) paramTup;
		Boolean ignoreEmpty = param._1();
		if (ignoreEmpty == null) {
			ignoreEmpty = true;
		}
		
		if (isInstance(reportContext.getCurValue(), String.class)) {
			String curVal = (String)reportContext.getCurValue();
			String val = "";
			if (StringUtils.isEmpty(curVal) && !ignoreEmpty) {
				val = "否";
			} else if ("1".equals(curVal)) {
				val = "是";
			}
			reportContext.setCurValue(val, CurValueType.STRINGVAL);
		}
	}
}
