package rongji.framework.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ExportExcel {
	
	private static final Logger logger = LoggerFactory.getLogger(ExportExcel.class);
	
	/**
	 * 列头单元格样式
	 * @param workbook
	 */
  	public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {
  		
  		  // 设置字体
    	  HSSFFont font = workbook.createFont();
    	  //设置字体大小
    	  font.setFontHeightInPoints((short)11);
    	  //字体加粗
    	  font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    	  //设置字体名字 
    	  font.setFontName("宋体");
    	  //设置样式; 
    	  HSSFCellStyle style = workbook.createCellStyle();
    	  //设置底边框; 
    	  style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    	  //设置底边框颜色;  
    	  style.setBottomBorderColor(HSSFColor.BLACK.index);
    	  //设置左边框;   
    	  style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    	  //设置左边框颜色; 
    	  style.setLeftBorderColor(HSSFColor.BLACK.index);
    	  //设置右边框; 
    	  style.setBorderRight(HSSFCellStyle.BORDER_THIN);
    	  //设置右边框颜色; 
    	  style.setRightBorderColor(HSSFColor.BLACK.index);
    	  //设置顶边框; 
    	  style.setBorderTop(HSSFCellStyle.BORDER_THIN);
    	  //设置顶边框颜色;  
    	  style.setTopBorderColor(HSSFColor.BLACK.index);
    	  //在样式用应用设置的字体;  
    	  style.setFont(font);
    	  //设置自动换行; 
    	  style.setWrapText(false);
    	  //设置水平对齐的样式为居中对齐;  
    	  style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    	  //设置垂直对齐的样式为居中对齐; 
    	  style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    	  
    	  return style;
    	  
  	}
  	
  	
  	/**
	 * 列数据信息单元格样式
	 * @param workbook
	 */
  	public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
	  	  // 设置字体
	  	  HSSFFont font = workbook.createFont();
	  	  //设置字体大小
	  	  //font.setFontHeightInPoints((short)10);
	  	  //字体加粗
	  	  //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	  	  //设置字体名字 
	  	  font.setFontName("宋体");
	  	  //设置样式; 
	  	  HSSFCellStyle style = workbook.createCellStyle();
	  	  //设置底边框; 
	  	  style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	  	  //设置底边框颜色;  
	  	  style.setBottomBorderColor(HSSFColor.BLACK.index);
	  	  //设置左边框;   
	  	  style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	  	  //设置左边框颜色; 
	  	  style.setLeftBorderColor(HSSFColor.BLACK.index);
	  	  //设置右边框; 
	  	  style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	  	  //设置右边框颜色; 
	  	  style.setRightBorderColor(HSSFColor.BLACK.index);
	  	  //设置顶边框; 
	  	  style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	  	  //设置顶边框颜色;  
	  	  style.setTopBorderColor(HSSFColor.BLACK.index);
	  	  //在样式用应用设置的字体;  
	  	  style.setFont(font);
	  	  //设置自动换行; 
	  	  style.setWrapText(false);
	  	  //设置水平对齐的样式为居中对齐;  
	  	  style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	  	  //设置垂直对齐的样式为居中对齐; 
	  	  style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	  	 
	  	  return style;
  	
  	}

  	/**
	 * 处理数据导出excel
	 * @param list	导出数据集合
	 * @param header 表格title
	 * @param fileName 导出的文件名
	 * @param res	request对象
	 * @param req	response对象
	 */
	public void exportQueryInfo(List<List<String>> list,String[] header,String fileName,HttpServletResponse res,HttpServletRequest req){
		try {
			if (req.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0){
				fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");//firefox浏览器
			} else if (req.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0){
					fileName = URLEncoder.encode(fileName, "UTF-8");//IE浏览器
			}else if (req.getHeader("User-Agent").toUpperCase().indexOf("CHROME") > 0) {  
		        	fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");// 谷歌  
		    }
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		ExportExcel exportExcel = new ExportExcel();//单元格样式类
    	String[] excelHeader = header;
        HSSFWorkbook wb = new HSSFWorkbook();  
        HSSFSheet sheet = wb.createSheet("sheet1");  
        sheet.setDefaultColumnWidth(30);      
        HSSFRow row = sheet.createRow((int) 0);        
        HSSFCellStyle columnTopStyle = exportExcel.getColumnTopStyle(wb);//获取列头样式对象
        HSSFCellStyle style = exportExcel.getStyle(wb);//单元格样式对象
        
        for (int i = 0; i < excelHeader.length; i++) {//表头
            HSSFCell cell = row.createCell(i);  
            cell.setCellValue(excelHeader[i]);  
            cell.setCellStyle(columnTopStyle);   
        }
        
        for(int i=0;i<list.size();i++){
        	row = sheet.createRow(i + 1);
        	List<String> subList = list.get(i);
        	
        	String[] data = new String[subList.size()];
        	for(int j=0;j<subList.size();j++){
        		data[j] = subList.get(j);
        	}
        	
        	for(int j=0; j< data.length ; j++){     		                   
                HSSFCell cell=row.createCell(j);               
                cell.setCellValue(data[j]); 
                cell.setCellStyle(style);
        	}
        }
        
        res.setContentType("application/vnd.ms-excel");
        res.setHeader("Content-disposition", "attachment;filename="+fileName+".xls");
        OutputStream ouputStream;
		try {
			ouputStream = res.getOutputStream();
			wb.write(ouputStream);  
	        ouputStream.flush();  
	        ouputStream.close(); 
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}  
	}
	
	/**
	 * 导出包含多个标签页的数据表格
	* @Title: exportQueryInfo
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param list	      数据列表
	* @param header   标签页表格标题
	* @param fileName 文件名称
	* @param sheets	      标签页名称
	* @param res
	* @param req
	 */
	public void exportQueryInfo(List<List<List<String>>> list, String[][] header, String fileName, String[] sheets,
			HttpServletResponse res, HttpServletRequest req){
		try {
			if (req.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0){
				fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");//firefox浏览器
			} else if (req.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0){
					fileName = URLEncoder.encode(fileName, "UTF-8");//IE浏览器
			}else if (req.getHeader("User-Agent").toUpperCase().indexOf("CHROME") > 0) {  
		        	fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");// 谷歌  
		    }
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		ExportExcel exportExcel = new ExportExcel();//单元格样式类
		HSSFWorkbook wb = new HSSFWorkbook(); 
		
		for(int s=0; s<sheets.length; s++){
			String[] excelHeader = header[s];
	        HSSFSheet sheet = wb.createSheet(sheets[s]);  
	        sheet.setDefaultColumnWidth(30);      
	        HSSFRow row = sheet.createRow((int) 0);        
	        HSSFCellStyle columnTopStyle = exportExcel.getColumnTopStyle(wb);//获取列头样式对象
	        HSSFCellStyle style = exportExcel.getStyle(wb);//单元格样式对象
	        
	        for (int i = 0; i < excelHeader.length; i++) {//表头
	            HSSFCell cell = row.createCell(i);  
	            cell.setCellValue(excelHeader[i]);  
	            cell.setCellStyle(columnTopStyle);   
	        }
	        
	        for(int i=0;i<list.get(s).size();i++){
	        	row = sheet.createRow(i + 1);
	        	List<String> subList = list.get(s).get(i);
	        	
	        	String[] data = new String[subList.size()];
	        	for(int j=0;j<subList.size();j++){
	        		data[j] = subList.get(j);
	        	}
	        	
	        	for(int j=0; j< data.length ; j++){     		                   
	                HSSFCell cell=row.createCell(j);               
	                cell.setCellValue(data[j]); 
	                cell.setCellStyle(style);
	        	}
	        }
		}
        
//		wb.writeProtectWorkbook("password", "123456");
        res.setContentType("application/vnd.ms-excel");
        res.setHeader("Content-disposition", "attachment;filename="+fileName+".xls");
        OutputStream ouputStream;
		try {
			ouputStream = res.getOutputStream();
			wb.write(ouputStream);  
	        ouputStream.flush();  
	        ouputStream.close(); 
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}  
	}
	
}
