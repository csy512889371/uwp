package rongji.framework.util;

import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * 
 * @Title: POIUtils.java
 * @Package rongji.framework.util
 * @Description: POIExcel表格工具类
 * @author LFG
 * @date 2016-5-26 上午10:09:23
 * @version V1.0
 */
public class POIUtils {
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
	 * @param pPosition
	 *            拷贝粘贴的相对位置（1表示所拷贝行的下一行）
	 * @return void 返回类型
	 * @throws
	 * @author LFG
	 */
	public static void copyRows(HSSFSheet st, int startRow, int endRow, int pPosition) {
		int targetRowFrom;
		int targetRowTo;
		int columnCount;
		CellRangeAddress region = null;
		int i, j;
		try {
			if (startRow == -1 || endRow == -1)
				return;
			for (i = 0; i < st.getNumMergedRegions(); i++) {
				region = st.getMergedRegion(i);
				if ((region.getFirstRow() >= startRow) && (region.getLastRow() <= endRow)) {
					targetRowFrom = region.getFirstRow() - startRow + pPosition;
					targetRowTo = region.getLastRow() - startRow + pPosition;

					CellRangeAddress newRegion = region.copy();
					newRegion.setFirstRow(targetRowFrom);
					newRegion.setFirstColumn(region.getFirstColumn());
					newRegion.setLastRow(targetRowTo);
					newRegion.setLastColumn(region.getLastColumn());
					st.addMergedRegion(newRegion);
				}
			}
			for (i = startRow; i <= endRow; i++) {
				HSSFRow sourceRow = st.getRow(i);
				columnCount = sourceRow.getLastCellNum();
				if (sourceRow != null) {
					HSSFRow newRow = st.createRow(pPosition + i);
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

	/**
	 * 
	 * @Title: getExcelCellAutoHeight
	 * @Description: 获取最适合单元格高度
	 * @param str
	 * @param fontCountInline
	 * @return
	 * @return float 返回类型
	 * @throws
	 * @author LFG
	 */
	public static float getExcelCellAutoHeight(String str, float fontCountInline) {
		float defaultRowHeight = 12.00f;// 每一行的高度指定
		float defaultCount = 0.00f;
		for (int i = 0; i < str.length(); i++) {
			float ff = getRegex(str.substring(i, i + 1));
			defaultCount += ff;
		}
		return (float) Math.ceil(defaultCount / fontCountInline) * defaultRowHeight;// 计算
	}

	public static float getRegex(String charStr) {
		// if (charStr.equals(" ")) {
		// return 0.5f;
		// }
		// // 判断是否为字母或字符
		// if (Pattern.compile("^[A-Za-z0-9]+$").matcher(charStr).matches()) {
		// return 0.5f;
		// }
		// 判断是否为全角
		if (Pattern.compile("[\u4e00-\u9fa5]+$").matcher(charStr).matches()) {
			return 1.00f;
		}
		// 全角符号 及中文
		if (Pattern.compile("[^x00-xff]").matcher(charStr).matches()) {
			return 1.00f;
		}
		return 0.5f;

	}

	/**
	 * 
	 * @Title: getMax
	 * @Description: 去数组中最大值
	 * @param arr
	 *            浮点数组
	 * @return
	 * @return float 返回类型
	 * @throws
	 * @author LFG
	 */
	public static float getMax(float[] arr) {
		float max = arr[0];
		for (int x = 1; x < arr.length; x++) {
			if (arr[x] > max) {
				max = arr[x];
			}
		}
		return max;
	}

	public static String getStringVal(HSSFCell cell) {
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
			case Cell.CELL_TYPE_FORMULA:
				return cell.getCellFormula();
			case Cell.CELL_TYPE_NUMERIC:
				cell.setCellType(Cell.CELL_TYPE_STRING);
				return cell.getStringCellValue();
			case Cell.CELL_TYPE_STRING:
				return cell.getStringCellValue();
			default:
				return "";
			}
		} else {
			return "";
		}
	}
	
   
	public static void copyRow(HSSFWorkbook workbook, HSSFSheet worksheet, int sourceRowNum, int destinationRowNum) {
	        // Get the source / new row
	        HSSFRow newRow = worksheet.getRow(destinationRowNum);
	        HSSFRow sourceRow = worksheet.getRow(sourceRowNum);

	        // If the row exist in destination, push down all rows by 1 else create a new row
	        if (newRow != null) {
	            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
	        } else {
	            newRow = worksheet.createRow(destinationRowNum);
	        }

	        // Loop through source columns to add to new row
	        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
	            // Grab a copy of the old/new cell
	            HSSFCell oldCell = sourceRow.getCell(i);
	            HSSFCell newCell = newRow.createCell(i);

	            // If the old cell is null jump to next cell
	            if (oldCell == null) {
	                newCell = null;
	                continue;
	            }

	            // Copy style from old cell and apply to new cell
	            HSSFCellStyle newCellStyle = workbook.createCellStyle();
	            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
	            ;
	            newCell.setCellStyle(newCellStyle);

	            // If there is a cell comment, copy
	            if (oldCell.getCellComment() != null) {
	                newCell.setCellComment(oldCell.getCellComment());
	            }

	            // If there is a cell hyperlink, copy
	            if (oldCell.getHyperlink() != null) {
	                newCell.setHyperlink(oldCell.getHyperlink());
	            }

	            // Set the cell data type
	            newCell.setCellType(oldCell.getCellType());

	            // Set the cell data value
	            switch (oldCell.getCellType()) {
	                case Cell.CELL_TYPE_BLANK:
	                    newCell.setCellValue(oldCell.getStringCellValue());
	                    break;
	                case Cell.CELL_TYPE_BOOLEAN:
	                    newCell.setCellValue(oldCell.getBooleanCellValue());
	                    break;
	                case Cell.CELL_TYPE_ERROR:
	                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
	                    break;
	                case Cell.CELL_TYPE_FORMULA:
	                    newCell.setCellFormula(oldCell.getCellFormula());
	                    break;
	                case Cell.CELL_TYPE_NUMERIC:
	                    newCell.setCellValue(oldCell.getNumericCellValue());
	                    break;
	                case Cell.CELL_TYPE_STRING:
	                    newCell.setCellValue(oldCell.getRichStringCellValue());
	                    break;
	            }
	        }

	        // If there are are any merged regions in the source row, copy to new row
	        for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
	            CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
	            if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
	                CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
	                        (newRow.getRowNum() +
	                                (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow()
	                                        )),
	                        cellRangeAddress.getFirstColumn(),
	                        cellRangeAddress.getLastColumn());
	                worksheet.addMergedRegion(newCellRangeAddress);
	            }
	        }
	    }
	
}