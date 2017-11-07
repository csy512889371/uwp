package rongji.report.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rongji.framework.util.StringUtil;
import rongji.report.model.ReportConfig;
import rongji.report.model.ReportContext;
import rongji.report.model.WorkBookEngine;

public class POIXlsUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(POIXlsUtils.class);

	public static void copyRows(WorkBookEngine bookEngine, int startRow, int endRow, int copyPosition) {
		copyRows(bookEngine.getCopySheet(), bookEngine.getCurSheet(), startRow, endRow, copyPosition);
	}

	/**
	 * 
	 * @Title: copyRows
	 * @Description: 拷贝行
	 * @param st
	 *            工作表
	 * @param startRow
	 *            开始行数(从0行开始)
	 * @param endRow
	 *            结束行数(从0行开始)
	 * @param copyPosition
	 *            拷贝粘贴的相对位置（1表示所拷贝行的下一行）
	 * @return void 返回类型
	 * @throws
	 * @author LFG
	 */
	public static void copyRows(HSSFSheet dest, HSSFSheet target, int startRow, int endRow, int copyPosition) {
		int targetRowFrom;
		int targetRowTo;
		int columnCount;
		CellRangeAddress region = null;
		int i, j;
		try {
			if (startRow == -1 || endRow == -1)
				return;
			for (i = 0; i < dest.getNumMergedRegions(); i++) {
				region = dest.getMergedRegion(i);
				if ((region.getFirstRow() >= startRow) && (region.getLastRow() <= endRow)) {
					targetRowFrom = region.getFirstRow() - startRow + copyPosition;
					targetRowTo = region.getLastRow() - startRow + copyPosition;

					CellRangeAddress newRegion = region.copy();
					newRegion.setFirstRow(targetRowFrom);
					newRegion.setFirstColumn(region.getFirstColumn());
					newRegion.setLastRow(targetRowTo);
					newRegion.setLastColumn(region.getLastColumn());
					target.addMergedRegion(newRegion);
				}
			}
			for (i = 0; i <= endRow - startRow; i++) {
				HSSFRow sourceRow = dest.getRow(startRow + i);
				columnCount = sourceRow.getLastCellNum();
				if (sourceRow != null) {
					HSSFRow newRow = target.createRow(copyPosition + i);
					newRow.setHeight(sourceRow.getHeight());
					for (j = 0; j < columnCount; j++) {
						HSSFCell templateCell = sourceRow.getCell(j);
						if (templateCell != null) {
							HSSFCell newCell = newRow.createCell(j);
							copyCell(templateCell, newCell);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title: copyCell
	 * @Description: 拷贝单元格
	 * @param srcCell
	 *            源单元格
	 * @param distCell
	 *            目标单元格
	 * @return void 返回类型
	 * @throws
	 * @author LFG
	 */
	private static void copyCell(HSSFCell srcCell, HSSFCell distCell) {
		distCell.setCellStyle(srcCell.getCellStyle());
		if (srcCell.getCellComment() != null) {
			distCell.setCellComment(srcCell.getCellComment());
		}
		int srcCellType = srcCell.getCellType();
		distCell.setCellType(srcCellType);

		if (srcCellType == HSSFCell.CELL_TYPE_NUMERIC) {
			if (HSSFDateUtil.isCellDateFormatted(srcCell)) {
				distCell.setCellValue(srcCell.getDateCellValue());
			} else {
				distCell.setCellValue(srcCell.getNumericCellValue());
			}
		} else if (srcCellType == HSSFCell.CELL_TYPE_STRING) {
			distCell.setCellValue(srcCell.getRichStringCellValue());
		} else if (srcCellType == HSSFCell.CELL_TYPE_BOOLEAN) {
			distCell.setCellValue(srcCell.getBooleanCellValue());
		} else if (srcCellType == HSSFCell.CELL_TYPE_ERROR) {
			distCell.setCellErrorValue(srcCell.getErrorCellValue());
		} else if (srcCellType == HSSFCell.CELL_TYPE_FORMULA) {
			distCell.setCellFormula(srcCell.getCellFormula());
		} else if (srcCellType == HSSFCell.CELL_TYPE_BLANK) {
		}
	}

	public static void setCell(ReportContext reportContext, String target, Object val) {
		
		logger.info("begin POIXlsUtils.setCell target:" + target + " val:" + val);
		
		HSSFSheet sheet = reportContext.getCurSheet();
		if (val == null) {
			return;
		}
		
		Integer row = reportContext.getShiftRows() + ReportUtils.targetToRow(target);
		Integer col = ReportUtils.targetToCol(target);
		
		logger.info("row: " + row + " col:" + col);
		if (reportContext.getShiftRows() > 0) {
			int i = 0;
			i = i - 1;
		}
		
		HSSFCell cell = sheet.getRow(row).getCell(col);
		if (cell == null) {
			logger.error("POIXlsUtils.setCell cell is null at row: " + row + " col:" + col);
		}
		if (val instanceof String) {
			cell.setCellValue((String) val);
		} else if (val instanceof Integer) {
			cell.setCellValue(Double.valueOf(val.toString()));
		} else if (val instanceof Double) {
			cell.setCellValue((double) val);
		} else {
			cell.setCellValue(val.toString());
		}

	}

	public static void setPicture(ReportContext reportContext, String iType, byte[] imByte, String top, String bot) {
		iType = iType.toLowerCase();
		if (imByte != null && imByte.length > 0) {
			HSSFWorkbook book = reportContext.getBook();
			HSSFSheet sheet = reportContext.getCurSheet();
			Integer shiftRows = reportContext.getShiftRows();

			HSSFPatriarch patriarch = (HSSFPatriarch) sheet.createDrawingPatriarch();
			Integer row1 = shiftRows + ReportUtils.targetToRow(top);
			Integer col1 = ReportUtils.targetToCol(top);
			Integer row2 = shiftRows + ReportUtils.targetToRow(bot);
			Integer col2 = ReportUtils.targetToCol(bot);

			HSSFClientAnchor anchor = new HSSFClientAnchor(100, 10, 1020, 245, col1.shortValue(), row1, col2.shortValue(), row2);
			int hwbImgeType = HSSFWorkbook.PICTURE_TYPE_JPEG;
			if ("jpeg".equals(iType) || "jpg".equals(iType)) {
				hwbImgeType = HSSFWorkbook.PICTURE_TYPE_JPEG;
			} else if ("png".equals(iType)) {
				hwbImgeType = HSSFWorkbook.PICTURE_TYPE_PNG;
			} else if ("bmp".equals(iType)) {
				hwbImgeType = HSSFWorkbook.PICTURE_TYPE_EMF;
			}
			patriarch.createPicture(anchor, book.addPicture(imByte, hwbImgeType));
		}

	}

	public static String getExcelResmueFontAndHanging(WorkBookEngine bookEngine, String educateResume, String target, Integer cellWidth, Integer cellHeigh) {
		String[] resumes = educateResume.trim().split("\r\n");
		String endResume = "";
		Map<String, Integer> cellFontMap = bookEngine.getCellFontMap();
		Map<Integer, Integer[]> fontInfoMap = bookEngine.getFontInfoMap();

		for (Entry<Integer, Integer[]> entry : fontInfoMap.entrySet()) {
			Integer font = entry.getKey();
			Integer[] wordInfos = entry.getValue();
			Integer widthLt = (cellWidth - wordInfos[1]) / wordInfos[0];
			Integer lineL = (cellHeigh - wordInfos[3]) / wordInfos[2];
			endResume = "";
			for (String aResume : resumes) {
				// 1 step: 过滤
				if (aResume == null || aResume.length() < 18) {
					endResume += aResume + "\r\n";
					continue;
				}
				// 2 step:获取内容实际占用宽度
				Integer widthL = widthLt - StringUtil.GetPlaceHolderLength(aResume.substring(0, 18));
				widthL -= 2;
				// 3 step:以endResume重新拼接一条履历，resumeInfo：还未处理的履历部分
				endResume += aResume.substring(0, 18);
				String resumeInfo = aResume.substring(18);
				// 4 step：处理履历
				while (StringUtil.GetPlaceHolderLength(resumeInfo) > widthL) {
					String aLineInfo = "";
					// dl:剩余可拼接一行的最小字符数
					int dl = 0;
					do {
						dl = (widthL - StringUtil.GetPlaceHolderLength(aLineInfo)) / 2;
						aLineInfo = resumeInfo.substring(0, aLineInfo.length() + dl);
						int nowH = StringUtil.GetPlaceHolderLength(aLineInfo);
						if (nowH == widthL || nowH == widthL - 1) {
							break;
						}
					} while (true);
					// @TODO 换了新模板后似乎以下算法不符合，先关闭
					// 在excel里，当磅数小等于7的时候，一个中文字符是等于两个英文字符的宽度，而大于
					// 7磅的时候，一个中文字符是略小于两个英文字符的宽度，所以做如下差别处理
					// int count = StringUtil.countSingleByteEleNum(aLineInfo);
					// if (widthL >= count * 4 && i < 5) {
					// aLineInfo = resumeInfo.substring(0, aLineInfo.length() +
					// 1);
					// }
					Integer placeNum = widthL - StringUtil.GetPlaceHolderLength(aLineInfo) + 2;
					// 该位置上的字符是否属于一个英文单词，并且不是最后一位
					if (StringUtil.isBelongAWordAndNotEnd(resumeInfo, aLineInfo.length() - 1)) {
						// 剔除最后一个单词（因为不完整）
						aLineInfo = StringUtil.cullLastWord(aLineInfo);
					}
					// 检查有没有（,（ 这些字符出现在末尾，有则移到下一行
					Integer errorEndNum = ReportStringUtil.getErrorEndNum(aLineInfo);
					if (errorEndNum > 0 && errorEndNum < aLineInfo.length()) {
						aLineInfo = aLineInfo.substring(0, aLineInfo.length() - errorEndNum);
					} else {
						// 检查有没有
						// 逗号半角“,”全角：“，” 分号半角";"全角“；”
						// 冒号 半角“:”全角“：” 句号半角“.”全角“。” 右括号 半角")"全角“）”
						// 这些字符出现在开头，则尽可能移到上一行的留白区域
						String nextLine = resumeInfo.substring(aLineInfo.length());
						aLineInfo += ReportStringUtil.getErrorStartStr(nextLine, placeNum);
					}
					endResume += aLineInfo + "\r\n";
					resumeInfo = resumeInfo.substring(aLineInfo.length());
					if (StringUtil.isNotEmpty(resumeInfo)) {
						endResume += StringUtil.getSpaces(18);// 18space
					}
				}
				if (StringUtil.isNotEmpty(resumeInfo)) {
					endResume += resumeInfo + "\r\n";
				}
				if (!endResume.endsWith("\r\n")) {
					endResume += "\r\n";
				}
			}
			if (endResume.trim().split("\r\n").length <= lineL) {
				cellFontMap.put(target, font);
				break;
			}
		}
		if (!cellFontMap.containsKey(target)) {
			cellFontMap.put(target, 7);
		}
		return endResume;
	}

	public static void getExcelSuitFont(WorkBookEngine bookEngine, List<String> values, String target, Integer cellWidth, Integer cellHeigh) {
		Map<String, Integer> cellFontMap = bookEngine.getCellFontMap();
		Map<Integer, Integer[]> fontInfoMap = bookEngine.getFontInfoMap();

		Integer minFont = null;
		for (Entry<Integer, Integer[]> entry : fontInfoMap.entrySet()) {
			Integer font = entry.getKey();
			if (minFont == null || minFont > font) {
				minFont = font;
			}

			Integer[] wordInfos = entry.getValue();
			Integer widthL = (cellWidth - wordInfos[1]) / wordInfos[0];
			Integer lineL = (cellHeigh - wordInfos[3]) / wordInfos[2];
			if (values.size() > lineL) {
				continue;
			}

			int line = 0;
			for (String value : values) {
				int num = StringUtil.GetPlaceHolderLength(value);
				line++;
				while (num > widthL) {
					int dl = 0;
					String alineStr = "";
					do {
						dl = (widthL - StringUtil.GetPlaceHolderLength(alineStr)) / 2;
						alineStr = value.substring(0, alineStr.length() + dl);
						int nowH = StringUtil.GetPlaceHolderLength(alineStr);
						if (nowH == widthL || nowH == widthL - 1) {
							break;
						}
					} while (true);
					value = value.substring(alineStr.length());
					num = StringUtil.GetPlaceHolderLength(value);
					line++;
				}
				;
			}
			if (line <= lineL) {
				cellFontMap.put(target, font);
				break;
			}
		}
		if (!cellFontMap.containsKey(target)) {
			cellFontMap.put(target, minFont);
		}
	}

	public static void setExcelStlyeFont(WorkBookEngine bookEngine, HSSFCell cell, String target) {
		Map<String, Map<Integer, HSSFCellStyle>> hssfCellStyleMaps = bookEngine.getCellStyleMaps();
		Integer font = bookEngine.getCellFontMap().get(target);
		if (font == null || target == null) {
			return;
		}

		if (!hssfCellStyleMaps.containsKey(target)) {
			hssfCellStyleMaps.put(target, new HashMap<Integer, HSSFCellStyle>());
		}
		Map<Integer, HSSFCellStyle> hssfStyleMap = hssfCellStyleMaps.get(target);

		HSSFCellStyle style = hssfStyleMap.get(font);
		if (style == null) {
			HSSFWorkbook wb = bookEngine.getBook();
			style = wb.createCellStyle();
			style.cloneStyleFrom(cell.getCellStyle());
			HSSFFont hfont = wb.createFont();
			hfont.setFontName("宋体");
			hfont.setFontHeightInPoints(font.shortValue());
			style.setFont(hfont);
			hssfStyleMap.put(font, style);
		}
		cell.setCellStyle(style);
	}

	public static void setAutoFitRowHeight(WorkBookEngine bookEngine, HSSFCell cell, Integer colIndex) {
		short font = cell.getCellStyle().getFont(bookEngine.getBook()).getFontHeightInPoints();
		Integer[] wordInfos = bookEngine.getFontInfoMap().get(new Integer(font));

		int cellWidth = bookEngine.getCurSheet().getColumnWidth(colIndex) / 32;
		Integer widthL = (cellWidth - wordInfos[1]) / wordInfos[0];

		Integer rowNUm = 0;
		String[] strs = StringUtil.formatWrap(StringUtil.fomartEmpty(cell.getStringCellValue())).split("\r\n");
		for (String value : strs) {
			rowNUm++;
			int num = StringUtil.GetPlaceHolderLength(value);
			while (num > widthL) {
				int dl = 0;
				String alineStr = "";
				do {
					dl = (widthL - StringUtil.GetPlaceHolderLength(alineStr)) / 2;
					alineStr = value.substring(0, alineStr.length() + dl);
					int nowH = StringUtil.GetPlaceHolderLength(alineStr);
					if (nowH == widthL || nowH == widthL - 1) {
						break;
					}
				} while (true);
				value = value.substring(alineStr.length());
				num = StringUtil.GetPlaceHolderLength(value);
				rowNUm++;
			}
		}

		float rowHeight = (rowNUm * wordInfos[2] + wordInfos[3]) * 3 / 4.0f;
		if (rowHeight > cell.getRow().getHeightInPoints()) {
			cell.getRow().setHeightInPoints(rowHeight);
		}

	}

	public static void outPut(ReportContext context, String path) {
		FileOutputStream fileOut = null;
		String excelName = null;
		if (StringUtil.isNotEmpty(context.getFileName())) {
			excelName = context.getFileName() + ".xls";
		} else {
			excelName = UUID.randomUUID().toString() + ".xls";
		}
		File excelFile = new File(path + "/" + excelName);
		try {
			fileOut = new FileOutputStream(excelFile);
			context.getBook().write(fileOut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static File createFileDir(ReportContext context) {
		ReportConfig reportConfig = context.getReportConfig();
		String outputDir = reportConfig.getBaseFolderPath() + reportConfig.getRelativePath();
		String zipFileName = UUID.randomUUID().toString();
		File zipFolder = new File(outputDir + "/" + zipFileName);
		zipFolder.mkdirs();
		return zipFolder;
	}

}
