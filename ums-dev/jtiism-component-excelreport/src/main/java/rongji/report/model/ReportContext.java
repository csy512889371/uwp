package rongji.report.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rongji.framework.base.exception.ApplicationException;
import rongji.report.CDSBaseImpl;
import rongji.report.model.ReportConfig.OutFileType;
import bsh.Interpreter;

/**
 * 
 * @Title: ReportContext.java
 * @Package rongji.report
 * @Description: 报表上下文数据
 * @author liuzhen
 * @date 2017年3月1日 上午8:39:12
 * @version V1.0
 */
public class ReportContext {
	
	private static final Logger logger = LoggerFactory.getLogger(ReportContext.class);

	public enum DealType {
		COPYFILE, COPYFILE_SHEET, COPYSHEET, PAGE, COPYLINES
	}

	public enum DealFileType {
		// 多文件 、单sheet
		MULFILE_SINGLESHEET,
		// 单文件 多sheet
		SINGLEFILE_MULSHEET,
		// 多文件 多sheet
		MULFILE_MULSHEET,
		// 单sheet 多行
		SINGLESHEET_MULLINE
	}

	public enum CurValueType {
		STRINGVAL, DATEVAL, LISTVAL, MAPVAL, LISTMAPVAL, OBJECTVAL
	}

	private Map<Integer, DealType> dealMap = new LinkedHashMap<Integer, ReportContext.DealType>();

	private Map<Integer, Map<String, Integer>> lineInfoMap = new LinkedHashMap<Integer, Map<String, Integer>>();

	private Interpreter inter = null;

	private Integer curLevel;

	private ReportConfig reportConfig;

	private WorkBookEngine bookEngine;

	/**
	 * 输出文件类型
	 */
	private DealFileType dealFileType;

	private Map<String, Object> dataSet = null;

	private Object curValue = null;

	private CurValueType curValueType = CurValueType.OBJECTVAL;

	/**
	 * 当前操作文件名
	 */
	private String fileName = null;

	/**
	 * 当前操作sheet名
	 */
	private String sheetName = null;

	/**
	 * 文件全局变量
	 */
	private Map<String, Object> globalParams = new HashMap<String, Object>();

	/**
	 * 页变量
	 */
	private Map<String, Object> sheetParams = new HashMap<String, Object>();

	private Map<String, Integer> fileNameMap = new HashMap<String, Integer>();

	private ReportRule reportRule;

	private SqlInfo commonSql;

	private List<CDSBaseImpl> rowsCds = new ArrayList<CDSBaseImpl>();

	private List<CDSBaseImpl> columnsCds = new ArrayList<CDSBaseImpl>();

	public ReportContext(ReportConfig reportConfig) {
		this.reportConfig = reportConfig;
		if (StringUtils.isEmpty(reportConfig.getTempPath())) {
			throw new ApplicationException("excel模板路径为空");
		}
	}

	public void createNewBookEngine() throws Exception {
		try {
			if (bookEngine != null) {
				bookEngine.clear();
			}
			bookEngine = WorkBookEngine.instance(reportConfig.getTempPath());
		} catch (Exception e) {
			logger.error("创建 bookEngine 异常", e);
			throw e;
		}
	}

	public WorkBookEngine getBookEngine() {
		return bookEngine;
	}

	public HSSFWorkbook getBook() {
		return bookEngine.getBook();
	}

	public HSSFSheet getCurSheet() {
		return bookEngine.getCurSheet();
	}

	/**
	 * 
	 * @Title: setNextCurSheet
	 * @Description: 选择下一个sheet
	 * @param index
	 *            如果index有值，则选择该下标的sheet,如果不存在则取copySheetIndex
	 * @return void 返回类型
	 * @throws
	 * @author LZ
	 */
	public void setNextCurSheet(Integer index) {
		bookEngine.setNextCurSheet(index);
	}

	public void setCurSheet(Integer index) {
		bookEngine.setCurSheet(index);
	}

	public Map<String, Object> getDataSet() {
		return dataSet;
	}

	public void setDataSet(Map<String, Object> dataSet) {
		this.dataSet = dataSet;
	}

	public Object getCurValue() {
		return curValue;
	}

	public void setCurValue(Object curValue, CurValueType curValueType) {
		this.curValue = curValue;
		this.curValueType = curValueType;
	}

	public CurValueType getCurValueType() {
		return curValueType;
	}

	public Map<String, Object> getGlobalParams() {
		return globalParams;
	}

	public void setGlobalParams(Map<String, Object> globalParams) {
		this.globalParams = globalParams;
	}

	public Map<String, Object> getSheetParams() {
		return sheetParams;
	}

	public void setSheetParams(Map<String, Object> sheetParams) {
		this.sheetParams = sheetParams;
	}

	public Object getParam(String name) {
		Object val = sheetParams.get(name);
		if (val != null) {
			return val;
		}
		return globalParams.get(name);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public ReportConfig getReportConfig() {
		return reportConfig;
	}

	public boolean isMulFile() {
		if (OutFileType.mulitFile.equals(reportConfig.getOutFileType())) {
			return true;
		}
		return false;
	}

	public void resetAll() {
		curValue = null;
		globalParams.clear();
		sheetParams.clear();
		dataSet.clear();
	}

	public void resetSheet() {
		curValue = null;
		sheetName = null;
		dataSet = null;
		sheetParams.clear();
	}

	public void clear() {
		resetFile();
		globalParams = null;
		sheetParams = null;
	}

	public void resetFile() {
		curValue = null;
		sheetName = null;
		fileName = null;
		dataSet = null;
		globalParams.clear();
		sheetParams.clear();
		if (bookEngine != null) {
			bookEngine.clear();
			bookEngine = null;
		}

	}

	public Map<Integer, DealType> getDealMap() {
		return dealMap;
	}

	public void setDealMap(Map<Integer, DealType> dealMap) {
		this.dealMap = dealMap;
	}

	public boolean hasFile() {
		if (bookEngine != null && bookEngine.getBook() != null) {
			return true;
		}
		return false;
	}

	public DealFileType getDealFileType() {
		return dealFileType;
	}

	public void setDealFileType(DealFileType dealFileType) {
		this.dealFileType = dealFileType;
	}

	public void setReportConfig(ReportConfig reportConfig) {
		this.reportConfig = reportConfig;
	}

	public void setBookEngine(WorkBookEngine bookEngine) {
		this.bookEngine = bookEngine;
	}

	public Map<Integer, Map<String, Integer>> getLineInfoMap() {
		return lineInfoMap;
	}

	public void setLineInfoMap(Map<Integer, Map<String, Integer>> lineInfoMap) {
		this.lineInfoMap = lineInfoMap;
	}

	public Integer getCurLevel() {
		return curLevel;
	}

	public void setCurLevel(Integer curLevel) {
		this.curLevel = curLevel;
	}

	public void setRowsNumInCurLevel(Integer rowsNum) {
		Map<String, Integer> lineInfo = getLineInfo(curLevel);
		lineInfo.put("rowsNum", rowsNum);
	}

	private Map<String, Integer> getLineInfo(Integer level) {
		if (!lineInfoMap.containsKey(level)) {
			lineInfoMap.put(level, new HashMap<String, Integer>());
		}
		return lineInfoMap.get(level);
	}

	/**
	 * 
	 * @Title: setCurLevelShiftRows
	 * @Description: 设置当前行遍历的偏移（行）量
	 * @return void 返回类型
	 * @throws
	 * @author LZ
	 */
	public void setCurLevelShiftRows() {
		Map<String, Integer> lineInfo = lineInfoMap.get(curLevel);
		if (lineInfo != null) {
			if (lineInfo.containsKey("shiftRows")) {
				Integer newShiftRows = lineInfo.get("shiftRows") + lineInfo.get("rowsNum");
				lineInfo.put("shiftRows", newShiftRows);
			} else {
				lineInfo.put("shiftRows", 0);
			}
		}
	}

	public Integer getShiftRows() {
		Map<String, Integer> lineInfo = lineInfoMap.get(curLevel);
		if (lineInfo != null) {
			Integer shiftRows = lineInfo.get("shiftRows");
			if (shiftRows != null) {
				return shiftRows;
			}
		}
		return 0;
	}

	public String solveSameFileName(String fileName) {
		if (fileNameMap.containsKey(fileName)) {
			Integer index = fileNameMap.get(fileName) + 1;
			fileNameMap.put(fileName, index);
			fileName += "(" + index + ")";
		} else {
			fileNameMap.put(fileName, 0);
		}
		return fileName;
	}

	public Interpreter getInter() {
		return inter;
	}

	public void setInter(Interpreter inter) {
		this.inter = inter;
	}

	public void setReportRule(ReportRule reportRule) {
		this.reportRule = reportRule;

	}

	public ReportRule getReportRule() {
		return this.reportRule;
	}

	public SqlInfo getCommonSql() {
		return commonSql;
	}

	public void appendCommonSql(SqlInfo sqlInfo) {
		if (commonSql == null) {
			commonSql = sqlInfo;
		} else {
			commonSql = commonSql.addNewSqlInfo(sqlInfo);
		}
	}

	public List<CDSBaseImpl> getRowsCds() {
		return rowsCds;
	}

	public void setRowsCds(List<CDSBaseImpl> rowsCds) {
		this.rowsCds = rowsCds;
	}

	public List<CDSBaseImpl> getColumnsCds() {
		return columnsCds;
	}

	public void setColumnsCds(List<CDSBaseImpl> columnsCds) {
		this.columnsCds = columnsCds;
	}

}
