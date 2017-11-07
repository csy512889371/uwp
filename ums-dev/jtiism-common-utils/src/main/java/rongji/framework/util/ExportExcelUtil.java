package rongji.framework.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

public class ExportExcelUtil {
	private static Logger logger = Logger.getLogger(ExportExcelUtil.class);
	
	@SuppressWarnings("deprecation")
	public static void createExcelRow(HSSFSheet sheet, int rowNO, int rowHeight, int colNum, String fontCaption, HSSFCellStyle cellStyle) {
		if (colNum < 0) {
			colNum = 100;
		}

		HSSFRow row = sheet.createRow(rowNO); // 创建第一行
		row.setHeight((short) (rowHeight < 1 ? 300 : rowHeight)); // 设置行高

		HSSFCell cell = row.createCell(0);// 设置第一行
		cell.setCellType(HSSFCell.ENCODING_UTF_16); // 定义单元格为字符串类型
		cell.setCellValue(new HSSFRichTextString(fontCaption));
		sheet.addMergedRegion(new Region(rowNO, (short) 0, rowNO, (short) (colNum - 1))); // 指定合并区域
		if (cellStyle != null) {
			cell.setCellStyle(cellStyle);
		}
	}


	/**
	* @Title: createColumnHeader
	* @Description: (创建表头)
	* @param sheet
	* @param rowNO 行号
	* @param rowHeight 行高
	* @param columnHeader 表头
	* @param cellStyle 单元格样式
	* @return void    返回类型
	* @throws
	* @author wqq 
	*/ 
	public static void createColumnHeader(HSSFSheet sheet, int rowNO, int rowHeight, String[] columnHeader, HSSFCellStyle cellStyle, short[] columnWidths) {
		if (columnHeader == null || columnHeader.length < 1) {
			return;
		}
		HSSFRow row = sheet.createRow(rowNO);
		row.setHeight((short) rowHeight);

		HSSFCell cell = null;
		for (int i = 0; i < columnHeader.length; i++) {
			sheet.setColumnWidth(i, columnWidths[i] * 256); // 设置列宽，20个字符宽度。宽度参数为1/256，故乘以256
			cell = row.createCell(i);
			cell.setCellType(HSSFCell.ENCODING_UTF_16);
			if (cellStyle != null) {
				cell.setCellStyle(cellStyle);
			}
			cell.setCellValue(new HSSFRichTextString(columnHeader[i]));
		}
	}


	/**
	* @Title: createColumnData
	* @Description: (创建数据行)
	* @param workbook
	* @param sheet
	* @param rowNO 行号
	* @param columnData 数据对象
	* @param maxValue 最大行
	* @param cellStyle 单元格样式
	* @return
	* @return HSSFSheet    返回类型
	* @throws
	* @author wqq 
	*/ 
	public static HSSFSheet createColumnData(HSSFWorkbook workbook, HSSFSheet sheet, int rowNO, int rowHeight, String[][] columnData, int maxValue, HSSFCellStyle[] cellStyles) {
		maxValue = (maxValue < 1 || maxValue > 65535) ? 65535 : maxValue;
		int currRowNO = rowNO;
		for (int numNO = currRowNO; numNO < columnData.length + currRowNO; numNO++) {
			if (numNO % maxValue == 0) {
				sheet = workbook.createSheet();
				rowNO = 0;
			}
			createColumnDataDesc(sheet, numNO, rowNO, currRowNO, rowHeight, columnData, cellStyles);
			rowNO++;
		}
		return sheet;
	}

	private static void createColumnDataDesc(HSSFSheet sheet, int numNO, int rowNO, int currRowNO, int rowHeight, String[][] columnData, HSSFCellStyle[] cellStyles) {
		if (columnData == null || columnData.length < 1) {
			return;
		}
		HSSFRow row = sheet.createRow(rowNO);
		row.setHeight((short) rowHeight);
		
		HSSFCell cell = null;
		for (int i = 0; i < columnData[numNO - currRowNO].length; i++) {
			//sheet.setColumnWidth(i, 20 * 256); // 设置列宽，20个字符宽度。宽度参数为1/256，故乘以256
			cell = row.createCell(i);
			cell.setCellType(HSSFCell.ENCODING_UTF_16);
			if (cellStyles[i] != null) {
				cell.setCellStyle(cellStyles[i]);
			}
			cell.setCellValue(new HSSFRichTextString(columnData[numNO - currRowNO][i]));
		}
	}

	
	
	/**
	* @Title: createCellFontStyle
	* @Description: (设置单元格样式)
	* @param workbook
	* @param fontSize 字体大小,默认200
	* @param fontWeight 字体加粗 (normal/bold)
	* @param align	水平对齐方式(left/right/center,默认center)
	* @param valign 垂直对齐方式(top/bottom/center,默认center)
	* @param wrapText 是否文本自动换行
	* @return
	* @return HSSFCellStyle    返回类型
	* @throws
	* @author wqq 
	*/ 
	public static HSSFCellStyle createCellFontStyle(HSSFWorkbook workbook, int fontSize, String fontWeight, String align, String valign, boolean wrapText) {
		if (workbook == null) {
			return null;
		}
		HSSFCellStyle cellStyle = workbook.createCellStyle();

		//水平对齐方式
		if (StringUtils.equalsIgnoreCase("left", align)) {
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 指定单元格居中对齐
		} else if(StringUtils.equalsIgnoreCase("right", align)){
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 指定单元格居中对齐
		} else {
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
		}
		
		//垂直对齐方式
		if (StringUtils.equalsIgnoreCase("top", valign)) {
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP); // 指定单元格居中对齐
		} else if(StringUtils.equalsIgnoreCase("bottom", valign)){
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_BOTTOM); // 指定单元格居中对齐
		} else {
			cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 指定单元格居中对齐
		}
		
		//设置单元格是否换行
		cellStyle.setWrapText(wrapText);// 指定单元格自动换行

		// 单元格字体是否加粗
		HSSFFont font = workbook.createFont();
		if (fontWeight != null && fontWeight.equalsIgnoreCase("normal")) {
			font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		} else {
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		}
		//字体
		font.setFontName("宋体");
		//字体大小
		font.setFontHeight((short) (fontSize < 1 ? 200 : fontSize));
		
		cellStyle.setFont(font);
		return cellStyle;
	}


	public static void exportExcelToLocal(HSSFWorkbook workbook, String fileName) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(new File(fileName));
			workbook.write(os);
			os.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static void exportExcel(HSSFWorkbook workbook, OutputStream os) {
		try {
			workbook.write(os);
			os.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static void main(String[] args) {
		
		/*
		 * 在用java 编写生成报表时发现了个问题，将行，列隐藏，将列隐藏很简单用
		 * this.sheet.setColumnHidden((short)12, true);将第13列隐藏注意excel的第一列用0表示
		 * 
		 * 隐藏行：
		 * 
		 * HSSFRow row = sheet.getRow(8); row.setZeroHeight(true);
		 * 
		 * 将第8行隐藏就是将他的高度设为0也等同为隐藏
		 */
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		String[] strArr = new String[] { "姓名", "现单位及职务", "拟任免职务", "公示地点"};
		int colNum = strArr.length;

		int rowNO = 0;
		// 1. titleCaption
		
		ExportExcelUtil.createExcelRow(sheet, rowNO, 1200, colNum, "公示方案\n（共XX人）", 
				ExportExcelUtil.createCellFontStyle(workbook, 400, "bold", "center", "center", true));								
		// 2.columnTitleHeader
		rowNO++;
		ExportExcelUtil.createColumnHeader(sheet, rowNO, 800, strArr, 
				ExportExcelUtil.createCellFontStyle(workbook, 250, "bold", "center", "center", false), new short[]{15, 35, 35, 20});
		// 3.
		rowNO++;
		ExportExcelUtil.createExcelRow(sheet, rowNO, 800, colNum, "一、正厅级拟提任人选（6人）", 
				ExportExcelUtil.createCellFontStyle(workbook, 250, "bold", "left", "center", false));
		
		// 4.数据行 循环创建中间的单元格的各项的值
		rowNO++;
		HSSFCellStyle[] styles = new HSSFCellStyle[4];
		styles[0] = ExportExcelUtil.createCellFontStyle(workbook, 250, "normal", "center", "center", true);
		styles[1] = ExportExcelUtil.createCellFontStyle(workbook, 250, "normal", "left", "center", true);
		styles[2] = ExportExcelUtil.createCellFontStyle(workbook, 250, "normal", "left", "center", true);
		styles[3] = ExportExcelUtil.createCellFontStyle(workbook, 250, "normal", "center", "center", true);
		
		String[][] columnData = new String[][] { 
				{ "张XX", "省纪委常委", "拟提任省委巡视六组组长、正厅级巡视专员", "福建日报\n福建电视台\n福建组工专网"},
				{ "张惠妹", "省纪委常委", "拟提任省委巡视六组组长、正厅级巡视专员", "福建日报\n福建电视台\n福建组工专网"}, 
				{ "张惠妹", "省委宣传部副部长", "拟兼任省委外宣办（省政府新闻办）主任，确定为正厅级干部", "省委宣传部"}, 
				{ "张惠妹", "省人大常委会办公厅副主任、信访局局长", "拟提任省人大常委会办公厅巡视员，免现职","省人大"}, 
				{ "张惠妹", "省检察院副检察长、党组成员、检委会委员", "拟提任省检察院巡视员，免去其省检察院副检察长、党组成员职务", "省检察院"}
			};
		sheet = ExportExcelUtil.createColumnData(workbook, sheet, rowNO, 800, columnData, -1, styles);
		
		//循环下一个
		rowNO = sheet.getLastRowNum();
		rowNO++;
		ExportExcelUtil.createExcelRow(sheet, rowNO, 800, colNum, "二、副厅级拟提任人选（6人）", 
				ExportExcelUtil.createCellFontStyle(workbook, 250, "bold", "left", "center", false));
		
		// 数据行 循环创建中间的单元格的各项的值
		rowNO++;
		columnData = new String[][] { 
				{ "张XX", "省纪委常委", "拟提任省委巡视六组组长、正厅级巡视专员", "福建日报\n福建电视台\n福建组工专网"}, 
				{ "张XX", "省委宣传部副部长", "拟兼任省委外宣办（省政府新闻办）主任，确定为正厅级干部", "省委宣传部"}, 
				{ "张XX", "省人大常委会办公厅副主任、信访局局长", "拟提任省人大常委会办公厅巡视员，免现职","省人大"}, 
				{ "张XX", "省检察院副检察长、党组成员、检委会委员", "拟提任省检察院巡视员，免去其省检察院副检察长、党组成员职务", "省检察院"}
			};
		sheet = ExportExcelUtil.createColumnData(workbook, sheet, rowNO, 800, columnData, -1, styles);
		//eeu.createSummaryRow(workbook, sheet, colNum, "合计：" + columnData.length, 180, "normal", "right");
		ExportExcelUtil.exportExcelToLocal(workbook, "d://test//test14.xls");
		
		
	}

}
