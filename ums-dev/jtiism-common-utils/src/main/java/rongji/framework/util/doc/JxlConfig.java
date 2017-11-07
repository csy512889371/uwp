package rongji.framework.util.doc;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import jxl.Sheet;
import jxl.SheetSettings;
import jxl.Workbook;
import jxl.biff.DisplayFormat;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.CellFormat;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;
import jxl.format.VerticalAlignment;
import jxl.write.NumberFormats;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import rongji.framework.base.ContextHolderUtils;
import rongji.framework.util.DateUtil;
import rongji.framework.util.StringUtil;


/**
 * jxl公用操作类
 * add 2010-03-12  by sumfing
 * */

public final class JxlConfig {
	
	/** 回响对象 */
	private HttpServletResponse response;
	
	/** 导出类型——默认为数据列表*/
	private boolean dataExport = true;
	
	/** 导出数据表头list */
	private List headListArr = new ArrayList();
	
	/** Excel表头宽度 */
	private int[] headWidthArr = new int[]{};
	
	/** Excel表头高度 */
	private int[] headHeightArr = new int[]{};
	
	/** 导出数据list */
	private List contentList = new ArrayList();
	/**
	 * style for Content , start with 0;
	 */
	private Map<Integer , CellFormat> contetnStyleMap;
	
	/** 导出包含序号*/
	private boolean ordinal = true;
	
	/** 标题*/
	private String title = null;
	
	/** 内容*/
	private String content = null;
	
	/** 生成文件名*/
	private String exportName = null;
	
	/**数据表总列数*/
	private int headColSize = 0;
	
	/**数据表最后一行(合计)*/
	private List lastRowArr;
	
	/**标题高度*/
	private int  titleHeight=0;
	
	/**文本内容的对齐格式*/
	public static int CENTER=0;
	public static int RIGHT=1;
	public static int LEFT=2;
	/**文本内容(文本信息)的对齐格式*/
	private int textAlign=0;
	/**表头内容的对齐格式*/
	private int titleAlign=0;
	/**合并后，单元格的对齐方式（不包括表头）*/
	private int mergeAlign=0;
	
	/**
	 * 为一个四元数组，[起始单元格所在列，起始单元格所在行，末尾单元格所在列，末尾单元格所在行]  的数组
	 * 起始行列都为0
	 * 以标题（非表头，如XXX部门请假表）以下第一行为第0行，以序号列以右为第0行
	 */
	private int[][] mergeCells;
	
	public JxlConfig(boolean dataExport,List headListArr,List contentList,boolean ordinal,
			int[] headWidthArr,int[] headHeightArr,String title,String exportName,String content){
		this.dataExport = dataExport;
		this.headListArr = headListArr;
		this.contentList = contentList;
		this.ordinal = ordinal;
		this.headWidthArr = headWidthArr;
		this.headHeightArr = headHeightArr;
		this.title = title;
		this.exportName = exportName;
		this.content = content;
	}

	public JxlConfig(){
	}
	
	/**
	 * 导出EXEL文件
	 * 如果数据量大于65535，则分多个sheet进行导出
	 * @param sheetName   页名称
	 * @param index       页位置   无用参数
	 * @see CommonDataExport
	 * @throws IOException
	 * @throws WriteException
	 * @throws RowsExceededException
	 * @throws ParseException
	 */
	public void DataExport(HttpServletResponse rsp,String sheetName, int index,String expotName) throws IOException,
			RowsExceededException, WriteException, ParseException {
		this.response = rsp;
		List<JxlConfig> exportList = new ArrayList<JxlConfig>();
		if(contentList != null && contentList.size() > 65535) {
			List<Object[]> tempContentList = new ArrayList<Object[]>();
			int num = 1;
			try {
				for (int count=1; count <=contentList.size(); count++) {
					Object[] objs =  (Object[]) contentList.get(count-1);
					tempContentList.add(objs);
						
					if(count>1 && (count%10000 == 0 || count==contentList.size())){
						exportList.add(new JxlConfig(true,headListArr,tempContentList,ordinal,
								headWidthArr,headHeightArr,sheetName+"["+num+"]",expotName,null));
						
						num++;
						tempContentList = new ArrayList<Object[]>();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			exportList.add(new JxlConfig(true,headListArr,contentList,ordinal,
					headWidthArr,headHeightArr,sheetName,expotName,null));
		}
		this.CommonDataExport(exportList);
	}
	
	/**
	 * 导出EXEL文件
	 * 如果数据量大于65535，则分多个sheet进行导出
	 * @param sheetName 页名称
	 * @param index
	 * @param expotName 导出文件名
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 * @throws ParseException
	 */
	public void DataExport(String sheetName, int index,String expotName) throws IOException,
			RowsExceededException, WriteException, ParseException {
		this.DataExport(ContextHolderUtils.getResponse(), sheetName, index, expotName);
	}
	
	/**
	 * 导出EXEL文件
	 * 如果数据量大于65535，则分多个sheet进行导出
	 * @param sheetName 页名称
	 * @param index
	 * @param response
	 * @param expotName 导出文件名
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 * @throws ParseException
	 */
	public void DataExport(String sheetName, int index,String expotName, HttpServletResponse response) throws IOException,
	RowsExceededException, WriteException, ParseException {
		this.DataExport(response, sheetName, index, expotName);
	}
	
	
	/**
	 * 导出为EXCEL文件，最终处理方法
	 * add 2011-05-05
	 * update 2013-03-20
	 * @param exportList
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 * @throws ParseException
	 */
	public void CommonDataExport(List<JxlConfig> exportList) throws IOException,
	RowsExceededException, WriteException, ParseException {
		//初始化response
		initResponse();
		OutputStream out = response.getOutputStream();
		WritableWorkbook book = Workbook.createWorkbook(out);
		// 加大、粗字体
		// 20号字体，加粗，居中
		WritableCellFormat bigBoldc = getBigBlod();
		// 普通加粗字体
		// 居中，垂直居中，细边框，自动换行，加粗11号宋体
		WritableCellFormat normalBoldc = getNormalBoldfCenter();
		// 默认单元格式内容样式
		// 居中，细边框，自动换行
		WritableCellFormat normalCont = getNormalCont();
		// 默认单元格式内容样式（数字）
		// 居右，细边框，自动换行
		WritableCellFormat normalContNum = getNormalContNum();
		// 默认百分比单元格式内容样式
		// 居右，细边框，自动换行
		WritableCellFormat normalContPercent = getNormalContPercent();

		try{
			String errorMsg = null;
			String sheetName = null;
			String content = null;
			String exportName = null;
			if(exportList != null && exportList.size() > 0){
				JxlConfig jc = null;
				List tempHeadListArr = null;
				List tempContentList = null;
				for(int i=0; i<exportList.size(); i++){
					jc = exportList.get(i);
					tempHeadListArr = jc.getHeadListArr();
					tempContentList = jc.getContentList();
					content = (!StringUtil.isEmpty(jc.getContent()))?jc.getContent():"";
					if(!StringUtil.isEmpty(jc.getTitle())){
						sheetName = jc.getTitle();
					}else{
						errorMsg = "您还没设置好导出数据页名称，请先设置！";
					}
					int headCount = 0,objLen = 0;
					if(jc.dataExport){
						try{
							headCount = tempHeadListArr.size();
						}catch(Exception e){}
						
						if(headCount == 0){
							errorMsg = "您还没设置好要导出的数据项名称，请先设置！";
						}else if(tempContentList != null && tempContentList.size() > 0){
							/**	
							 * 数据与数据项名称个数匹配	
							 * System.out.println(((Object[])contentList.get(0)).length);
							 */
							try{
								objLen = ((Object[])tempContentList.get(0)).length;
							}catch(Exception e){
								errorMsg = "导出内容数据类型为非对象数组，请检查！";
							}
							if(StringUtil.isEmpty(errorMsg) && headCount > objLen){
								errorMsg = "导出数据项个数少于表头数据项个数，请检查！";
							}
						}
					}
					
					if(!StringUtil.isEmpty(errorMsg)){
						setErrorMsgToSheet(book, bigBoldc, normalCont,errorMsg);
					}else{
						WritableSheet sheet = book.createSheet(sheetName, i);
						char newLine = 0x000A;
						if(jc.dataExport){
							int intRow = 0;
							int intCell = (jc.ordinal)?1:0;// 序号列
							exportName = (!StringUtil.isEmpty(jc.getExportName()))?jc.getExportName():"";
							if(!StringUtil.isEmpty(exportName)){
								String fileName = exportName;
								if(fileName.indexOf("件"+DateUtil.formatDateToStringWithNull(new Date(), "yyyyMMdd"))>-1){
									//业务部门案件统计模块导出不做文件名截取
								}else if (fileName.indexOf("绩效档案")>-1) {//绩效档案 审判业务 导出文件名不做文件名截取
									fileName = fileName.substring(4);
									exportName=exportName.substring(4);
								}else if(fileName.indexOf("绩效考评")>-1){
									;
								}
								else if(exportName.length() > 16){
									byte[] s = fileName.getBytes();									
									if(s.length>38){
										int c = StringUtil.getCharInt(fileName, 38);
										fileName = fileName.substring(0,c)+"_";
									}
								}
								fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
								this.response.setHeader("Content-disposition","attachment;filename="+ fileName +".xls");
//								sheet.addCell(new jxl.write.Label(0,0,exportName,bigBoldc));
								sheet.addCell(new jxl.write.Label(0,0,sheetName,bigBoldc));
								if(titleHeight!=0){
									sheet.setRowView(0, titleHeight);
								}else{
									sheet.setRowView(0, 600);
								}
								sheet.mergeCells(0, 0, headCount+intCell-1, 0);
								sheet.getWritableCell(0,0).setCellFormat(bigBoldc);
								intRow++;
							}
							
							if(jc.ordinal){
								sheet.addCell(new jxl.write.Label(0,intRow,"序号",normalBoldc));// 普通的带有定义格式的单元格
								sheet.setColumnView(0, 6);// 宽度设置
							}
							
							/** 局部变量(col:列;row:行)*/
							int  col = 0, row = 0;
							String cellValue="";
							
							/** 将数据类名写入到sheet工作环境中*/
							for(int h=0;h<headCount;h++){
								cellValue = "";
								if(tempHeadListArr.get(h) != null && !StringUtil.isEmpty(String.valueOf(tempHeadListArr.get(h)))){
									cellValue = String.valueOf(tempHeadListArr.get(h));
								}
								sheet.addCell(new jxl.write.Label(h+intCell,intRow,cellValue,normalBoldc));
								try{
									sheet.setColumnView(h+intCell, jc.getHeadWidthArr()[h]); // 宽度设置
								}catch(Exception e){}
							}
							
							
							/** 将数据写入到sheet工作环境中*/
							if(tempContentList != null && tempContentList.size() > 0){
								sheet = InsertDateToSheet(normalCont,
										normalContNum, normalContPercent, jc,
										tempContentList, headCount, sheet,
										newLine, intRow, intCell);
								//初始化打印设置
								int[] printArea = new int[] { 0, 0, headCount,
										tempContentList.size() + intRow };
								int widthCount = 0;
								int headWidthArrLength = this.getHeadWidthArr().length;
								for (int m = 0; m < headWidthArrLength; m++) {// 计算总宽度
									widthCount = widthCount + headWidthArr[m];
								}
								this.printSetting(sheet, printArea, widthCount,1);
							}
							//设置高度（针对行）
							if(headHeightArr!=null&&headHeightArr.length>0){
								for (int j = 0; j <=tempContentList.size(); j++) {
									if(j>headHeightArr.length-1){
										sheet.setRowView(j+intRow, headHeightArr[headHeightArr.length-1]);
									}else{
										sheet.setRowView(j+intRow, headHeightArr[j]);
									}
								}
							}
							//如果需要合并单元格，则合并
							this.mergeCells(sheet, intRow,intCell,false);
						}else{
							sheet.addCell(new jxl.write.Label(0,0,sheetName,bigBoldc));
							sheet.setRowView(0, 600);
							sheet.mergeCells(0, 0, 8, 0);
							sheet.addCell(new jxl.write.Label(0,1,content.replace('$', newLine),normalCont));
							sheet.mergeCells(0, 1, 8, 6);
						}
					}
				}
			}else{
				setErrorMsgToSheet(book, bigBoldc, normalCont,"当前无任何内容可导出！");
			}
		}catch(Exception e){
			System.out.println("=============导出Excel异常==============");
		}finally{
			book.write();
			book.close();
			out.flush();
			out.close();
		}
	}


	/**
	 * 导出为EXCEL文件,此方法支持多维表头
	 * 具体使用方法为，传入一个一维整型数组List
	 * 数组形如:[a,b,c] a为表头名称，b为所占列数，c为所在行
	 * 不允许表头最后一列与数据项的个数不一致
	 * 如需表头跨行，只需在下端设置空白即可，即如果需要合并   第0行 第1，2列与第1行第1，2列。则只需设第1行1，2列的内容为空即可
	 * 例：[{"第0行第一格"，2，0},{"第0行第三格"，1，0},{"第0行第四格"，1，0},
	 *      {""，2，1},{"第1行第三格"，1，1}},{"第1行第四格"，1，1]  
	 * add 2012-03-05
	 * @author redlwb
	 * @param exportList
	 * @param dropColumn  需要删除的列，一般统计数据的第一列
	 * @param startBorder 是否开启边框
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 * @throws ParseException
	 */
	public void CommonDataExport(List<JxlConfig> exportList,List unitArray,Integer dropColumn,boolean startBorder) throws IOException,
	RowsExceededException, WriteException, ParseException {
		initResponse();
		OutputStream out = response.getOutputStream();
		//创建工作簿
		WritableWorkbook book = Workbook.createWorkbook(out);
		// 出错信息样式
		WritableCellFormat bigBoldc = getBigBlod();
		// 普通加粗字体
		WritableCellFormat normalBoldc = getNormalBoldfCenter();
		// 常规格式
		WritableCellFormat normalCont =  getNormalCont();// 自动换行
		// 常规数字格式
		WritableCellFormat normalContNum = getNormalContNum();
		//百分比
		WritableCellFormat normalContPercent=getNormalContentPercent();
		try{
			String errorMsg = null;
			String sheetName = null;
			String content = null;
			String exportName = null;
			if(exportList != null && exportList.size() > 0){//如果导出List非空
				JxlConfig jc = null;//声明一个jxl工具类
				List tempHeadListArr = null;//表头列表
				List tempContentList = null;//内容列表
				for(int i=0; i<exportList.size(); i++){//遍历导出List
					jc = exportList.get(i);//获取jxl 数据传输对象
					tempHeadListArr = jc.getHeadListArr();//获取表头
					tempContentList = jc.getContentList();//获取内容
					content = (!StringUtil.isEmpty(jc.getContent()))?jc.getContent():"";//获取内容,没用初始化为空
					if(!StringUtil.isEmpty(jc.getTitle())){
						sheetName = jc.getTitle();
					}else{
						errorMsg = "您还没设置好导出数据页名称，请先设置！";
					}
					int headCount = 0,objLen = 0;//初始化标志  表头列数，对象长度
					int rowCount = 0;//初始化行数
					if(jc.dataExport){
						try{//获取表头列数
							Object[] endColumn=(Object[]) tempHeadListArr.get(tempHeadListArr.size()-1);
							rowCount=Integer.parseInt(endColumn[2]+"")+1;
							for (int j = 0; j <tempHeadListArr.size(); j++) {
								Object[] column=(Object[]) tempHeadListArr.get(j);
								int rowIndex=Integer.parseInt(column[2]+"");
								int columnCount=Integer.parseInt(column[1]+"");
								if (rowIndex==0) {
									headCount=headCount+columnCount;
								}
							}
						}catch(Exception e){
							e.printStackTrace();
						}
						if(headCount == 0){
							errorMsg = "您还没设置好要导出的数据项名称，请先设置！";
						}else if(tempContentList != null && tempContentList.size() > 0){
							/**	
							 * 数据与数据项名称个数匹配	
							 * System.out.println(((Object[])contentList.get(0)).length);
							 */
							try{
								objLen = ((Object[])tempContentList.get(0)).length;
							}catch(Exception e){
								errorMsg = "导出内容数据类型为非对象数组，请检查！";
							}
							if(StringUtil.isEmpty(errorMsg) && headCount > objLen){
								errorMsg = "导出数据项个数少于表头数据项个数，请检查！";
							}
						}
					}
					
					if(!StringUtil.isEmpty(errorMsg)){
						setErrorMsgToSheet(book, bigBoldc, normalCont,errorMsg);
					}else{
						WritableSheet sheet = book.createSheet(sheetName, i);//创建表
						char newLine = 0x000A;
						if(jc.dataExport){
							int intRow = 0;
							int intCell = (jc.ordinal)?1:0;// 是否包含序号列，包含则从第一列开始
							exportName = (!StringUtil.isEmpty(jc.getExportName()))?jc.getExportName():"";
							if(!StringUtil.isEmpty(exportName)){
								String fileName = exportName;
								if(fileName.indexOf("件"+DateUtil.formatDateToStringWithNull(new Date(), "yyyyMMdd"))>-1){
									//业务部门案件统计模块导出不做文件名截取
								}else if (fileName.indexOf("绩效档案")>-1) {
									//绩效档案 审判业务 导出文件名不做文件名截取
									fileName = fileName.substring(4);
									exportName=exportName.substring(4);
								}else if(fileName.indexOf("绩效考评")>-1){
									
								}
								else if(exportName.length() > 16){
									byte[] s = fileName.getBytes();
									if(s.length>38){
										int c = StringUtil.getCharInt(fileName, 38);
										fileName = fileName.substring(0,c)+"_";
									}
								}
								fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
								this.response.setHeader("Content-disposition","attachment;filename="+ fileName +".xls");
								sheet.addCell(new jxl.write.Label(0,0,exportName,bigBoldc));
								sheet.setRowView(0, 600);
								sheet.mergeCells(0, 0, headCount+intCell-1, 0);
								//重新应用样式
								sheet.getWritableCell(0,0).setCellFormat(bigBoldc);
								intRow++;
							}
							//如果有序，则添加序号列
							if(jc.ordinal){
								// 普通的带有定义格式的单元格
								sheet.addCell(new jxl.write.Label(0,intRow,"序号",normalBoldc));
								// 宽度设置
								sheet.setColumnView(0, 6);
							}
							
							
							/** 将数据类名写入到sheet工作环境中*/
							sheet = InitExcelTableHead(normalBoldc,tempHeadListArr, headCount, sheet, intRow,intCell);
							//根据传入坐标进行合并初始化多行表头
							if (unitArray!=null&&unitArray.size()>0){
								this.unitRowByArray(sheet, unitArray,intRow,jc.ordinal,startBorder,headCount,rowCount);
							}
							
							/** 如果有序，且标题行数超过一行，序号列需要合并单元格让所占行数   与行数一致*/
							if (ordinal) {
							//	System.out.println("rowCount is "+rowCount);
								if (rowCount>1) {
									sheet.mergeCells(0, intRow, 0,rowCount+intRow-1);
									sheet.getWritableCell(0, intRow).setCellFormat(normalBoldc);
								}
							}
							
							String cellValue="";
							/** 将数据写入到sheet工作环境中*/
							if(tempContentList != null && tempContentList.size() > 0){
								sheet = InsertDataToExcel(dropColumn,
										normalCont, normalContNum,
										normalContPercent, jc, tempContentList,
										headCount, rowCount, sheet, newLine,
										intRow, intCell, cellValue);
								initPrintSetting(tempHeadListArr,
										tempContentList, headCount, rowCount,
										sheet, intRow);
							}
							//设置高度（针对行）
							if(headHeightArr!=null&&headHeightArr.length>0){
								for (int j = 0; j <headHeightArr.length; j++) {
									sheet.setRowView(j+intRow, headHeightArr[j]);
								}
							}
						}else{
							setErrorMsgToSheet(book, bigBoldc, normalCont,content.replace('$', newLine));
						}
					}
				}
			}else{
				setErrorMsgToSheet(book, bigBoldc, normalCont,"当前无任何内容可导出！");
			}
		}catch(Exception e){
			System.out.println("=============导出Excel异常==============");
			e.printStackTrace();
		}finally{
			book.write();
			book.close();
			out.flush();
			out.close();
		}
	}

	
	/**
	 * 导出EXCEL文件,支持多维表头（无需传表头为null的列对象，无需传合并坐标数组）
	 * @param exportList 表头标题列（二维数组列， 一个对象为一行，如list[][],第一行list[0]={[a,b,c,d],[a,b,c,d],....},a为表头名称(不为空)，b为合并列数，c合并行数，d为列宽）
	 * @param dropColumn 需要删除的列，一般统计数据的第一列
	 * @param startBorder 是否开启边框
	 * @throws IOException
	 * @throws RowsExceededException
	 * @throws WriteException
	 * @throws ParseException
	 * add by anna
	 */
	public void CommonDataExport(List<JxlConfig> exportList,Integer dropColumn,boolean startBorder) throws IOException,
	RowsExceededException, WriteException, ParseException{
		initResponse();
		OutputStream out = response.getOutputStream();
		// 创建工作簿
		WritableWorkbook book = Workbook.createWorkbook(out);
		// 标题样式
		WritableCellFormat bigBoldc = getBigBlod();
		// 普通加粗字体
		WritableCellFormat normalBoldc =getNormalBoldfCenter();
		// 常规字符格式
		WritableCellFormat normalCont = getNormalCont();
		// 常规整数格式
		WritableCellFormat normalContNum =getNormalContNum();
		// 百分比格式
		WritableCellFormat normalContPercent = getNormalContentPercent();
		// 普通加粗整数格式
		WritableCellFormat normalBoldNum = getNormalBoldNumf();
		try{
			String errorMsg = null;
			String sheetName = null;
			String content = null;
			String exportName = null;
			
			//如果导出List非空
			if(exportList != null && exportList.size() > 0){
				//声明一个jxl工具类
				JxlConfig jc = null;
				//表头列表
				List tempHeadListArr = null;
				//内容列表
				List tempContentList = null;
				for(int i=0; i<exportList.size(); i++){//遍历导出List
					jc = exportList.get(i);//获取jxl 数据传输对象
					tempHeadListArr = jc.getHeadListArr();//获取表头
					tempContentList = jc.getContentList();//获取内容
					content = (!StringUtil.isEmpty(jc.getContent()))?jc.getContent():"";//获取内容,没用初始化为空
					if(!StringUtil.isEmpty(jc.getTitle())){
						sheetName = jc.getTitle();
					}else{
						errorMsg = "您还没设置好导出数据页名称，请先设置！";
					}
					int headCount = jc.getHeadColSize();//  表头列数
					int objLen = 0;//对象长度
					int headRowCount = tempHeadListArr.size();//初始化表头行数
					
					if(jc.dataExport){
						
						if(headCount == 0){
							errorMsg = "您还没设置好要导出的数据项名称，请先设置！";
						}else if(tempContentList != null && tempContentList.size() > 0){
							try{
								objLen = ((Object[])tempContentList.get(0)).length;
							}catch(Exception e){
								errorMsg = "导出内容数据类型为非对象数组，请检查！";
							}
							if(StringUtil.isEmpty(errorMsg) && headCount > objLen){
								errorMsg = "导出数据项个数少于表头数据项个数，请检查！";
							}
						}
					}
					
					if(!StringUtil.isEmpty(errorMsg)){
						setErrorMsgToSheet(book, bigBoldc, normalCont,errorMsg);
					}else{
						exportName = (!StringUtil.isEmpty(jc.getExportName()))?jc.getExportName():"";
						WritableSheet sheet = book.createSheet(exportName, i);//创建表
						char newLine = 0x000A;
						if(jc.dataExport){
							int intRow = 0;
							int intCell = (jc.ordinal)?1:0;// 是否包含序号列，包含则从第一列开始
							if(!StringUtil.isEmpty(exportName)){
								String fileName = exportName;
								if(fileName.indexOf("件"+DateUtil.formatDateToStringWithNull(new Date(), "yyyyMMdd"))>-1){
									//业务部门案件统计模块导出不做文件名截取
								}else if (fileName.indexOf("绩效档案")>-1) {//绩效档案 审判业务 导出文件名不做文件名截取
									fileName = fileName.substring(4);
									exportName=exportName.substring(4);
								}else if(fileName.indexOf("绩效考评")>-1){
									;
								}
								else if(exportName.length() > 16){
									byte[] s = fileName.getBytes();
									if(s.length>38){
										int c = StringUtil.getCharInt(fileName, 38);
										fileName = fileName.substring(0,c)+"_";
									}
								}
								fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
								this.response.setHeader("Content-disposition","attachment;filename="+ fileName +".xls");
								sheet.addCell(new jxl.write.Label(0,0,sheetName,bigBoldc));
								sheet.setRowView(0, 600);
								sheet.mergeCells(0, 0, headCount+intCell-1, 0);
								sheet.getWritableCell(0,0).setCellFormat(bigBoldc);//重新应用样式
								intRow++;
							}
							
							if(jc.ordinal){
								sheet.addCell(new jxl.write.Label(0,intRow,"序号",normalBoldc));// 普通的带有定义格式的单元格
								sheet.setColumnView(0, 6);// 宽度设置
							}
							
					
							
							int rowCounts = intRow;
							/** 初始化表头*/
							sheet=InitUnitExcelTableHead(normalBoldc,tempHeadListArr, sheet, intCell, rowCounts);
							
							/** 如果有序，且标题行数超过一行，序号列需要合并单元格让所占行数   与行数一致*/
							if (ordinal) {
								if (headRowCount>1) {
									sheet.mergeCells(0, intRow, 0,headRowCount+intRow-1);
									sheet.getWritableCell(0, intRow).setCellFormat(normalBoldc);
								}
							}
							
							/** 局部变量(col:列;row:行)*/
							int  col = 0, row = 0;
							String cellValue;
							/** 将数据写入到sheet工作环境中*/
							if(tempContentList != null && tempContentList.size() > 0){
								SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
								for(row=0;row<tempContentList.size();row++){
									if(jc.ordinal){
										sheet.addCell(new jxl.write.Number(0,row+intRow+headRowCount,Double.valueOf(row+1),normalCont));
									}
									int executeCount=0;
									for(col=0;col<headCount+1;col++){
										int colExport=col;
										boolean isBreak=true;
										if(dropColumn!=null){
											if (col==dropColumn) {//当列数等于丢弃列时
												isBreak=false;
											}
											else if(col>dropColumn){
												colExport=colExport-1;
											}
										}
										if(isBreak){
										executeCount++;
										if (executeCount>headCount) {
											break;
										}
										cellValue = "";
										if(((Object[])tempContentList.get(row))[col] != null && !((Object[])tempContentList.get(row))[col].equals("")){
											cellValue = String.valueOf(((Object[])tempContentList.get(row))[col]);
											try{
												if(DateUtil.isDateStrValid(cellValue)){
													cellValue = df.format(df.parse(cellValue));
												}else{
													cellValue = cellValue.replace('$', newLine);
												}
											}catch(Exception e){
												cellValue = cellValue.replace('$', newLine);
											}
										}
										Pattern pLon=Pattern.compile("(^[+\\-]?\\d+(\\.\\d+)?$)|(^\\d+(\\.\\d+)?$)");
										Matcher mLon=pLon.matcher(cellValue);
										boolean isLong=mLon.matches();
										Pattern pPer=Pattern.compile("(^[+\\-]?\\d+(\\.\\d+)?%$)|(^\\d+(\\.\\d+)?%$)");
										Matcher mPer=pPer.matcher(cellValue);
										boolean isPercent=mPer.matches();
										Double newCellValue=null;
										CellFormat cf = null;
										if(contetnStyleMap != null) cf = contetnStyleMap.get(col);
										if (isLong) {
											newCellValue=Double.parseDouble(cellValue);
											sheet.addCell(new jxl.write.Number(colExport+intCell,row+intRow+headRowCount,newCellValue,cf == null ? normalContNum : cf));											
										}
										else if (isPercent) {
											newCellValue=Double.parseDouble(cellValue.split("%")[0])/100;											
											sheet.addCell(new jxl.write.Number(colExport+intCell,row+intRow+headRowCount,newCellValue,cf == null ? normalContPercent : cf));
										} 
										else{
											sheet.addCell(new jxl.write.Label(colExport+intCell,row+intRow+headRowCount,cellValue,cf == null ? normalCont : cf));
										}
										}
									}
								}
								
								//最后一行（合计行）
								List lastRows = jc.getLastRowArr();
								int contentCount = tempContentList.size();
								if(lastRows!=null&&lastRows.size()>0){
									int intlastRow =  intRow+headRowCount+contentCount;//起始行数
									for(int er=0;er<lastRows.size();er++){
										List<Object[]> objectE = (List<Object[]>)lastRows.get(er);
										//起始列数
										int colCount = intCell;
										if(jc.ordinal){
											sheet.addCell(new jxl.write.Number(0,intlastRow,Double.valueOf(contentCount+er+1),normalCont));
										}
										//遍历列
										for(int m=0;m<objectE.size();m++){
											Object[] lastCell = objectE.get(m);
											String cellVa = "";
											if(lastCell[0] != null && !StringUtil.isEmpty(String.valueOf(lastCell[0]))){
												cellVa = String.valueOf(lastCell[0]); 
											}
											// 合并列数
											int cellCols = new Integer(String.valueOf(lastCell[1])).intValue();
											// 合并行数
											int cellRows = new Integer(String.valueOf(lastCell[2])).intValue();
											// 宽度
											int cellWidth = lastCell[2]!=null? new Integer(String.valueOf(lastCell[3])).intValue():0;
											
											if(!StringUtil.isEmpty(cellVa)){
												try{
													if(DateUtil.isDateStrValid(cellVa)){
														cellVa = df.format(df.parse(cellVa));
													}else{
														cellVa = cellVa.replace('$', newLine);
													}
												}catch(Exception e){
													cellVa = cellVa.replace('$', newLine);
												}
												
												Pattern pLon=Pattern.compile("(^[+\\-]?\\d+(\\.\\d+)?$)|(^\\d+(\\.\\d+)?$)");
												Matcher mLon=pLon.matcher(cellVa);
												boolean isLong=mLon.matches();
												Pattern pPer=Pattern.compile("(^[+\\-]?\\d+(\\.\\d+)?%$)|(^\\d+(\\.\\d+)?%$)");
												Matcher mPer=pPer.matcher(cellVa);
												boolean isPercent=mPer.matches();
												Double newCellValue=null;
												if (isLong) {
													newCellValue=Double.parseDouble(cellVa);
													sheet.addCell(new jxl.write.Number(colCount,intlastRow,newCellValue,normalBoldNum));											
												}
												else if (isPercent) {
													newCellValue=Double.parseDouble(cellVa.split("%")[0])/100;											
													sheet.addCell(new jxl.write.Number(colCount,intlastRow,newCellValue,normalContPercent));
												} 
												else{
													sheet.addCell(new jxl.write.Label(colCount,intlastRow,cellVa,normalBoldc));
												}
												
											}
											
											if(cellWidth>0){
												if(cellRows>1&&er<lastRows.size()-1){
													for(int cr=1;cr<cellRows;cr++){//合并行
														List<Object[]> objectHnext = (List<Object[]>)lastRows.get(er+cr);
														objectHnext.add(colCount-intCell, new Object[]{null,1,1,cellWidth});
													}										
												}
												for(int cc=0;cc<cellCols;cc++){//合并列
													sheet.setColumnView(colCount+cc, cellWidth); // 宽度设置
												}
											}									
											
											if(cellCols>1||cellRows>1){//合并
												WritableCellFormat normalBoldCenter = getNormalBoldfCenter();
												sheet.mergeCells(colCount, intlastRow, colCount+cellCols-1,intlastRow + cellRows-1);// 合并单元格,
												sheet.getWritableCell(colCount, intlastRow).setCellFormat(normalBoldCenter);
											}

											colCount += cellCols;
										}

										intlastRow++;
									}
									
								}
								//初始化打印区域
								int[] printArea=new int[]{0,0,headCount,tempContentList.size()+intRow+headRowCount};
							    int widthCount=0;//单行宽度
								this.printSetting(sheet,printArea,widthCount,headRowCount);
							}
						}else{
							setErrorMsgToSheet(book, bigBoldc, normalCont,content.replace('$', newLine));
						}
					}
				}
			}else{
				setErrorMsgToSheet(book, bigBoldc, normalCont,"当前无任何内容可导出！");
			}
		}catch(Exception e){
			System.out.println("=============导出Excel异常==============");
			e.printStackTrace();
		}finally{
			book.write();
			book.close();
			out.flush();
			out.close();
		}
	}
	
	/**
	 * 导入excel文件
	 * 读取指定的excel文件，将数据以String数组形式返回
	 * @param file         excel文件对象
	 * @param ignoreRows   忽略的行
	 * add 2012-6-8
	 */
	public static String[][] getData(File file,int ignoreRows) throws FileNotFoundException,IOException{
		List<String[]> result = new ArrayList<String[]>();
		int rowSize = 0;
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
		POIFSFileSystem fs = new POIFSFileSystem(bis);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFCell cell = null;
		for(int sheetIndex=0;sheetIndex<wb.getNumberOfSheets();sheetIndex++){
			HSSFSheet sheet = wb.getSheetAt(sheetIndex);
			for(int rowIndex=ignoreRows;rowIndex<sheet.getLastRowNum();rowIndex++){
				HSSFRow row = sheet.getRow(rowIndex);
				if(row==null){
					continue;
				}
				int tempRowSize = row.getLastCellNum()+1;
				if(tempRowSize>rowSize){
					rowSize=tempRowSize;
				}
				String[] values = new String[rowSize];
				Arrays.fill(values, "");
				boolean hasValue = false;
				for(short columnIndex=0;columnIndex<=row.getLastCellNum();columnIndex++){
					String value="";
					cell = row.getCell(columnIndex);
					if(cell!=null){
						switch (cell.getCellType()) {
							case HSSFCell.CELL_TYPE_STRING:
								value = cell.getStringCellValue();
								break;
							case HSSFCell.CELL_TYPE_NUMERIC:
								if(HSSFDateUtil.isCellDateFormatted(cell)){
									Date date = cell.getDateCellValue();
									if(date!=null){
										value = new SimpleDateFormat("yyyy-MM-dd").format(date);
									}else{
										value = "";
									}
								}else{
									value = new DecimalFormat("0").format(cell.getNumericCellValue());
								}
								break;
							case HSSFCell.CELL_TYPE_FORMULA:
								if(!cell.getStringCellValue().equals("")){
									value = cell.getStringCellValue();
								}else{
									value = cell.getNumericCellValue()+"";
								}
								break;
							case HSSFCell.CELL_TYPE_BLANK:
								value = cell.getStringCellValue();
								break;
							case HSSFCell.CELL_TYPE_ERROR:
								value = "";
								break;
							case HSSFCell.CELL_TYPE_BOOLEAN:
								value = (cell.getBooleanCellValue()==true?"Y":"N");
								break;	
							default:
								value = "";
						}
					}
					if(columnIndex==0&&value.trim().equals("")){
						continue;
					}
					values[columnIndex]=StringUtil.rightTrim(value.trim());
					hasValue = true;
				}
				if(hasValue){
					result.add(values);
				}
			}
		}
		bis.close();
		String[][] returnArray = new String[result.size()][rowSize];
		for(int i=0;i<returnArray.length;i++){
			returnArray[i]=(String[])result.get(i);
		}
		return returnArray;
	}
	/**
	 * 初始化导出Excel的表头
	 * @param normalBoldc   样式
	 * @param tempHeadListArr 数据
	 * @param headCount       表头项目数
	 * @param sheet           数据页
	 * @param intRow          标题所占行
	 * @param intCell         序号列所占列
	 * @param count           辅助计数器
	 * @param cellValue       
	 * @return
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	private WritableSheet InitExcelTableHead(WritableCellFormat normalBoldc,
			List tempHeadListArr, int headCount, WritableSheet sheet,
			int intRow, int intCell) throws WriteException,
			RowsExceededException {
		int count=0;
		String cellValue = "";
		for(int h=0;h<tempHeadListArr.size();h++){
			Object[] objectH=(Object[]) tempHeadListArr.get(h);
			int rowIndex=Integer.parseInt(objectH[2]+"");//获取当前数据所在行的下标
			int columnCount=Integer.parseInt(objectH[1]+"");
			cellValue="";
			if(objectH[0] != null && !StringUtil.isEmpty(String.valueOf(objectH[0]))){
				cellValue = String.valueOf(objectH[0]);
			}
			//System.out.println("count 为："+count+"   h为"+h+"    rowIndex"+rowIndex+"  值为"+String.valueOf(objectH[0])+"                        columnCount为"+columnCount);
			for(int m=0;m<columnCount;m++){
				if (m==0) {
					if ("".equals(cellValue)) {
					}else{
					   sheet.addCell(new jxl.write.Label(count+intCell,intRow+rowIndex,cellValue,normalBoldc));
					}
				}
				int headWidthArrLength=this.getHeadWidthArr().length;
				if (h > headWidthArrLength - 1) {
					int lastIndex =headWidthArrLength - 1;
					sheet.setColumnView(count + intCell, this
							.getHeadWidthArr()[lastIndex]); // 宽度设置
				} else {
					sheet.setColumnView(count + intCell, this
							.getHeadWidthArr()[h]); // 宽度设置
				}
				count++;
			}
			if (count==headCount) {
				count=0;//当count为表头长度时，代表换行，重置count
			}
		}
		return sheet;
	}

	

	/**
	 * 初始化带有合并坐标的表头，将表头数据插入Excel
	 * @param normalBoldc
	 * @param tempHeadListArr
	 * @param sheet
	 * @param intCell
	 * @param rowCounts
	 * @return
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	private WritableSheet InitUnitExcelTableHead(WritableCellFormat normalBoldc,
			List tempHeadListArr, WritableSheet sheet, int intCell,
			int rowCounts) throws WriteException, RowsExceededException {
		for(int h=0;h<tempHeadListArr.size();h++){//遍历行
			
			List<Object[]> objectH = (List<Object[]>)tempHeadListArr.get(h);
			int colCount = intCell;//起始列数
			for(int m=0;m<objectH.size();m++){//遍历列
				Object[] headCell = objectH.get(m);
				String cellVa = "";
				if(headCell[0] != null && !StringUtil.isEmpty(String.valueOf(headCell[0]))){
					cellVa = String.valueOf(headCell[0]); 
				}
				int cellCols = new Integer(String.valueOf(headCell[1])).intValue();// 合并列数
				int cellRows = new Integer(String.valueOf(headCell[2])).intValue();// 合并行数
				int cellWidth = headCell[2]!=null? new Integer(String.valueOf(headCell[3])).intValue():0;// 宽度
				
				
				if(!StringUtil.isEmpty(cellVa)){
					sheet.addCell(new jxl.write.Label(colCount,rowCounts,cellVa,normalBoldc));
				}
				
				if(cellWidth>0){
					if(cellRows>1&&h<tempHeadListArr.size()-1){
						for(int cr=1;cr<cellRows;cr++){//合并行
							List<Object[]> objectHnext = (List<Object[]>)tempHeadListArr.get(h+cr);
							objectHnext.add(colCount-intCell, new Object[]{null,1,1,cellWidth});
						}										
					}
					for(int cc=0;cc<cellCols;cc++){//合并列
						sheet.setColumnView(colCount+cc, cellWidth); // 宽度设置
					}
				}									
				
				if(cellCols>1||cellRows>1){//合并
					WritableCellFormat normalBoldCenter = getNormalBoldfCenter();
					sheet.mergeCells(colCount, rowCounts, colCount+cellCols-1,rowCounts + cellRows-1);// 合并单元格,
					sheet.getWritableCell(colCount, rowCounts).setCellFormat(normalBoldCenter);
				}
				colCount += cellCols;
			}
			rowCounts++;
		}
		return  sheet;
	}
	
	
	
	
	/**
	 * 设置错误信息至Excel表格
	 * @param book       操作Excel对象
	 * @param bigBoldc   单元格样式
	 * @param normalCont 单元格样式
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	private void setErrorMsgToSheet(WritableWorkbook book,
			WritableCellFormat bigBoldc, WritableCellFormat normalCont,String errorMsg)
			throws WriteException, RowsExceededException {
		String sheetName;
		sheetName = "数据导出问题描述";
		WritableSheet sheet = book.createSheet(sheetName, 0);
		sheet.addCell(new jxl.write.Label(0,0,sheetName,bigBoldc));
		sheet.setRowView(0, 600);
		sheet.mergeCells(0, 0, 8, 0);
		sheet.addCell(new jxl.write.Label(0,1,errorMsg,normalCont));
		sheet.setRowView(0, 1000);
		sheet.mergeCells(0, 1, 8, 0);
	}

	/**
	 * 初始化打印设置，一点打印即可使用，无需调整
	 * @param tempHeadListArr
	 * @param tempContentList
	 * @param headCount
	 * @param rowCount
	 * @param sheet
	 * @param intRow
	 */
	private void initPrintSetting(List tempHeadListArr, List tempContentList,
			int headCount, int rowCount, WritableSheet sheet, int intRow) {
		//初始化打印区域
		int[] printArea=new int[]{0,0,headCount,tempContentList.size()+intRow+rowCount};
		int widthCount=0;//单行宽度
		int[] headWitdhArr=this.getHeadWidthArr();
		int headWidthArrLength=headWitdhArr.length;
		for (int j = 0; j <tempHeadListArr.size(); j++) {
			Object[] column=(Object[]) tempHeadListArr.get(j);
			int rowIndex=Integer.parseInt(column[2]+"");
			int columnCount=Integer.parseInt(column[1]+"");
			if (j>headWidthArrLength-1) {
				widthCount=widthCount+columnCount*(headWitdhArr[headWidthArrLength-1]);
			}else {
				widthCount=widthCount+columnCount*(headWitdhArr[j]);
			}
			if (rowIndex!=0) {
				break;
			}
		}
		this.printSetting(sheet,printArea,widthCount,rowCount);
	}

	/**
	 * 将数据插入至指定的sheet中
	 * @param dropColumn   不需要的列，一般为统计数据的第一列（例如某些统计的第一列为法院分级码）
	 * @param normalCont   默认文字样式
	 * @param normalContNum 默认数字样式
	 * @param normalContPercent 默认百分比样式
	 * @param jc                 JxlConfig对象
	 * @param tempContentList    需要导出的数据
	 * @param headCount          表头列数
	 * @param rowCount           行数
	 * @param sheet              表格当前页
	 * @param newLine       
	 * @param intRow             
	 * @param intCell
	 * @param cellValue
	 * @return
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	private WritableSheet InsertDataToExcel(Integer dropColumn,
			WritableCellFormat normalCont, WritableCellFormat normalContNum,
			WritableCellFormat normalContPercent, JxlConfig jc,
			List tempContentList, int headCount, int rowCount,
			WritableSheet sheet, char newLine, int intRow, int intCell,
			String cellValue) throws WriteException, RowsExceededException {
		int col;
		int row;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		for(row=0;row<tempContentList.size();row++){
			if(jc.ordinal){
				sheet.addCell(new jxl.write.Number(0,row+intRow+rowCount,Double.valueOf(row+1),normalCont));
			}
			int executeCount=0;
			for(col=0;col<headCount+1;col++){
				int colExport=col;
				boolean isBreak=true;
				if(dropColumn!=null){
					if (col==dropColumn) {//当列数等于丢弃列时
						isBreak=false;
					}
					else if(col>dropColumn){
						colExport=colExport-1;
					}
				}
				if(isBreak){
				executeCount++;
				if (executeCount>headCount) {
					break;
				}
				cellValue = "";
				if(((Object[])tempContentList.get(row))[col] != null && !((Object[])tempContentList.get(row))[col].equals("")){
					cellValue = String.valueOf(((Object[])tempContentList.get(row))[col]);
					try{
						if(DateUtil.isDateStrValid(cellValue)){
							cellValue = df.format(df.parse(cellValue));
						}else{
							cellValue = cellValue.replace('$', newLine);
						}
					}catch(Exception e){
						cellValue = cellValue.replace('$', newLine);
					}
				}
				Pattern pLon=Pattern.compile("(^[+\\-]?\\d+(\\.\\d+)?$)|(^\\d+(\\.\\d+)?$)");
				Matcher mLon=pLon.matcher(cellValue);
				boolean isLong=mLon.matches();
				Pattern pPer=Pattern.compile("(^[+\\-]?\\d+(\\.\\d+)?%$)|(^\\d+(\\.\\d+)?%$)");
				Matcher mPer=pPer.matcher(cellValue);
				boolean isPercent=mPer.matches();
				
				Pattern sPer=Pattern.compile("(^[+\\-]?\\d+(\\.\\d+)?‰$)|(^\\d+(\\.\\d+)?‰$)");
				Matcher smPer=sPer.matcher(cellValue);
				boolean isSPercent=smPer.matches();
				
				Double newCellValue=null;
				CellFormat cf = null;
				if(contetnStyleMap != null) cf = contetnStyleMap.get(col);
				if (isLong) {
					newCellValue=Double.parseDouble(cellValue);
					sheet.addCell(new jxl.write.Number(colExport+intCell,row+intRow+rowCount,newCellValue,cf == null ? normalContNum : cf));											
				}
				else if (isPercent) {
					newCellValue=Double.parseDouble(cellValue.split("%")[0])/100;											
					sheet.addCell(new jxl.write.Number(colExport+intCell,row+intRow+rowCount,newCellValue,cf == null ? normalContPercent : cf));
				} else if (isSPercent) {
					sheet.addCell(new jxl.write.Label(colExport+intCell,row+intRow+rowCount,cellValue,cf == null ? normalContPercent : cf));
				} 
				else{
					sheet.addCell(new jxl.write.Label(colExport+intCell,row+intRow+rowCount,cellValue,cf == null ? normalCont : cf));
				}
				}
			}
		}
		return sheet;
	}
	
	/**
	 * 表头的合并
	 * 合并指定坐标的单元格
	 * @param sheet         当前表格页
	 * @param unitArray     合并的数组
	 * @param intRow    
	 * @param ordinal       是否排序，即是否需要序号列
	 * @param startBorder   是否需要边框
	 * @param headCount     表头项目数
	 * @param rowCount      行数
	 * @throws WriteException
	 */
	public void unitRowByArray(WritableSheet sheet,List unitArray,int intRow,boolean ordinal,boolean startBorder,int headCount,int rowCount) throws WriteException{
		// 普通加粗字体居中
		WritableCellFormat normalBoldCenter = getNormalBoldfCenter();
		for (int i = 0; i < unitArray.size(); i++) {
			int[] objects = (int[]) unitArray.get(i);
			int beginCol = Integer.parseInt(objects[0]+"");
			int beginRow = Integer.parseInt(objects[1]+"");
			int endCol = Integer.parseInt(objects[2]+"");
			int endRow =  Integer.parseInt(objects[3]+"");
			try {
				sheet.mergeCells(beginCol + 1, beginRow + intRow, endCol + 1,endRow + intRow);// 合并单元格
				sheet.getWritableCell(beginCol + 1, beginRow + intRow).setCellFormat(normalBoldCenter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setCellFormat(boolean isPercent,WritableCell writableCell,WritableCellFormat now,WritableCellFormat perceCellFormat){
		if (isPercent) {
			writableCell.setCellFormat(perceCellFormat);
		}else {
			writableCell.setCellFormat(now);
		}
		
	}
	
	/**
	 * 打印设置
	 * @param sheet
	 * @param printArea
	 * @param headWidthArr  列宽度数组
	 * @param titleRowCount 标题栏所占行数 用于设置打印标题
	 * @author：redlwb
	 * add 2012-3-31
	 */
	public void printSetting(Sheet sheet,int[] printArea,int widthCount,int titleRowCount){
		SheetSettings sst=sheet.getSettings();//获取打印设置
		if (widthCount>0&&widthCount<105) {
			sst.setOrientation(PageOrientation.PORTRAIT);//纵向打印
		}else {
			sst.setOrientation(PageOrientation.LANDSCAPE);//横向打印
			if (widthCount>0&&widthCount>140) {//设置缩放
				int perCent=(140*100/widthCount);
				sst.setScaleFactor(perCent);
			}
		}
		sst.setPaperSize(PaperSize.A4);//A4纸
		if(printArea!=null&&printArea.length>=4){//设置打印区域
		  sst.setPrintArea(printArea[0], printArea[1], printArea[2], printArea[3]);
		}
		sst.setPrintTitlesRow(0, titleRowCount);//设置打印标题（重复出现）
		sst.setHorizontalCentre(true);//水平居中
		sst.setLeftMargin(0);//左边距
		sst.setRightMargin(0);//右边距
	}
	
	/**
	 * 获取默认文本字体格式，水平居中
	 * 11号宋体，加粗，垂直水平居中，自动换行，细边框
	 * @param startBorder  是否开启边框
	 * @return
	 * @throws WriteException
	 */
	private WritableCellFormat getNormalBoldfCenter()
			throws WriteException {
		WritableFont normalBoldfCenter = new WritableFont(WritableFont.ARIAL,11, WritableFont.BOLD);
		WritableCellFormat normalBoldCenter = new WritableCellFormat(normalBoldfCenter); // 单元格定义
		if (titleAlign==CENTER) {
			normalBoldCenter.setAlignment(jxl.format.Alignment.CENTRE); // 设置对齐方式
		}else if (titleAlign==RIGHT) {
			normalBoldCenter.setAlignment(jxl.format.Alignment.RIGHT);
		}
		else if (titleAlign==LEFT) {
			normalBoldCenter.setAlignment(jxl.format.Alignment.LEFT);
		}
		normalBoldCenter.setVerticalAlignment(VerticalAlignment.CENTRE);
		normalBoldCenter.setWrap(true);
	    normalBoldCenter.setBorder(Border.ALL, BorderLineStyle.THIN);
		return normalBoldCenter;
	}
	/**
	 * 获取默认文本字体格式，水平居中
	 * 11号宋体，垂直水平居中，自动换行，细边框
	 * @return
	 * @throws WriteException
	 */
	private WritableCellFormat getNormalCenter() throws WriteException {
		WritableFont normalBoldfCenter = new WritableFont(WritableFont.ARIAL,11);
		WritableCellFormat normalBoldCenter = new WritableCellFormat(normalBoldfCenter); // 单元格定义
		if (mergeAlign==CENTER) {
			normalBoldCenter.setAlignment(jxl.format.Alignment.CENTRE);
		}else if (mergeAlign==RIGHT) {
			normalBoldCenter.setAlignment(jxl.format.Alignment.RIGHT);
		}
		else if (mergeAlign==LEFT) {
			normalBoldCenter.setAlignment(jxl.format.Alignment.LEFT);
		}
		normalBoldCenter.setVerticalAlignment(VerticalAlignment.CENTRE);
		normalBoldCenter.setWrap(true);
		normalBoldCenter.setBorder(Border.ALL, BorderLineStyle.THIN);
		return normalBoldCenter;
	}
	
	/**
	 * 获取百分比内容样式
	 * 居右，自动换行
	 * @return
	 * @throws WriteException
	 */
	private WritableCellFormat getNormalContentPercent() throws WriteException {
		DisplayFormat displayFormat=NumberFormats.PERCENT_FLOAT;
		WritableCellFormat normalContPercent = new WritableCellFormat(displayFormat);
		normalContPercent.setAlignment(jxl.format.Alignment.RIGHT);
		normalContPercent.setWrap(true);// 自动换行
		normalContPercent.setBorder(Border.ALL, BorderLineStyle.THIN);
		return normalContPercent;
	}

	/**
	 * 获取加粗数字样式
	 * 居右，11号，加粗，细边框
	 * @return
	 * @throws WriteException
	 */
	private WritableCellFormat getNormalBoldNumf() throws WriteException {
		WritableFont normalBoldNumf = new WritableFont(WritableFont.ARIAL,11,WritableFont.BOLD);
		WritableCellFormat normalBoldPercent = new WritableCellFormat(normalBoldNumf);
		normalBoldPercent.setAlignment(jxl.format.Alignment.RIGHT);
		normalBoldPercent.setWrap(true);// 自动换行
		normalBoldPercent.setBorder(Border.ALL, BorderLineStyle.THIN);
		return normalBoldPercent;
	}
	
	/**
	 * 获取默认百分比样式
	 * 居右，自动换行，细边框
	 * @return
	 * @throws WriteException
	 */
	private WritableCellFormat getNormalContPercent() throws WriteException {
		WritableCellFormat normalContPercent = new WritableCellFormat(NumberFormats.PERCENT_FLOAT);
		normalContPercent.setAlignment(jxl.format.Alignment.RIGHT);
		normalContPercent.setWrap(true);// 自动换行
		normalContPercent.setBorder(Border.ALL, BorderLineStyle.THIN);
		return normalContPercent;
	}
	/**
	 * 获取默认数字样式
	 * 居右，自动换行，细边框
	 * @return
	 * @throws WriteException
	 */
	private WritableCellFormat getNormalContNum() throws WriteException {
		//默认类型
		WritableCellFormat normalContNum = new WritableCellFormat(NumberFormats.DEFAULT);
		//右对齐
		normalContNum.setAlignment(jxl.format.Alignment.RIGHT);
		// 自动换行
		normalContNum.setWrap(true);
		//细边框
		normalContNum.setBorder(Border.ALL, BorderLineStyle.THIN);
		return normalContNum;
	}

	/**
	 * 获取默认文本样式
	 * @return
	 * @throws WriteException
	 */
	private WritableCellFormat getNormalCont() throws WriteException {
		WritableCellFormat normalCont = new WritableCellFormat(NumberFormats.DEFAULT);
		if (textAlign==CENTER) {
			normalCont.setAlignment(jxl.format.Alignment.LEFT);
		}else if (textAlign==RIGHT) {
			normalCont.setAlignment(jxl.format.Alignment.RIGHT);
		}
		else if (textAlign==LEFT) {
			normalCont.setAlignment(jxl.format.Alignment.LEFT);
		}
		// 自动换行
		normalCont.setWrap(true);
		// 细边框
		normalCont.setBorder(Border.ALL, BorderLineStyle.THIN);
		return normalCont;
	}

	/**
	 * 获取加大，加粗样式
	 * @return
	 * @throws WriteException
	 */
	private WritableCellFormat getBigBlod() throws WriteException {
		WritableFont bigBold = new WritableFont(WritableFont.ARIAL,20,WritableFont.BOLD);
		 // 单元格定义
		WritableCellFormat bigBoldc = new WritableCellFormat(bigBold);
		// 设置对齐方式
		bigBoldc.setAlignment(jxl.format.Alignment.CENTRE); 
		// 自动换行
		bigBoldc.setBorder(Border.ALL, BorderLineStyle.THIN);
		bigBoldc.setWrap(true);
		return bigBoldc;
	}

	/**
	 * 将数据插入到sheet中
	 * @param normalCont         默认的文本样式
	 * @param normalContNum		 数字样式
	 * @param normalContPercent  百分比样式
	 * @param jc				 JxlConfig对象
	 * @param tempContentList    数据数组
	 * @param headCount          表头项目数
	 * @param sheet              表格页
	 * @param newLine
	 * @param intRow             数据区域外的行数 例如标题行
	 * @param intCell            数据区域外的列数 例如序号列
	 * @return
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	private WritableSheet InsertDateToSheet(WritableCellFormat normalCont,
			WritableCellFormat normalContNum,
			WritableCellFormat normalContPercent, JxlConfig jc,
			List tempContentList, int headCount, WritableSheet sheet,
			char newLine, int intRow, int intCell) throws WriteException,
			RowsExceededException {
		int col;
		int row;
		String cellValue;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		for(row=0;row<tempContentList.size();row++){
			//如果包含序号列
			if(jc.ordinal){
				sheet.addCell(new jxl.write.Number(0,row+intRow+1,Double.valueOf(row+1),normalCont));
			}
			for(col=0;col<headCount;col++){
				cellValue = "";
				Object obj = "";
				if(((Object[])tempContentList.get(row))[col] != null && !((Object[])tempContentList.get(row))[col].equals("")){
					obj = ((Object[])tempContentList.get(row))[col];
					cellValue = String.valueOf(((Object[])tempContentList.get(row))[col]);
					try{
						if(DateUtil.isDateStrValid(cellValue)){
							cellValue = df.format(df.parse(cellValue));
						}else{
							cellValue = cellValue.replace('$', newLine);
						}
					}catch(Exception e){
						cellValue = cellValue.replace('$', newLine);
					}
				}
				Pattern pLon=Pattern.compile("(^[+\\-]?\\d+(\\.\\d+)?$)|(^\\d+(\\.\\d+)?$)");
				Matcher mLon=pLon.matcher(cellValue);
				boolean isLong=mLon.matches();
				Pattern pPer=Pattern.compile("(^[+\\-]?\\d+(\\.\\d+)?%$)|(^\\d+(\\.\\d+)?%$)");
				Matcher mPer=pPer.matcher(cellValue);
				boolean isPercent=mPer.matches();
				Pattern sPer=Pattern.compile("(^[+\\-]?\\d+(\\.\\d+)?‰$)|(^\\d+(\\.\\d+)?‰$)");
				Matcher smPer=sPer.matcher(cellValue);
				boolean isSPercent=smPer.matches();
				
				Double newCellValue=null;
				if (isLong) {
					if(String.valueOf(cellValue).length()>11){
						WritableCellFormat normalContNum1 = new WritableCellFormat(NumberFormats.TEXT);
						sheet.addCell(new jxl.write.Label(col+intCell,row+intRow+1,String.valueOf(cellValue),normalContNum1));
					}else{
						newCellValue=Double.parseDouble(cellValue);
						sheet.addCell(new jxl.write.Number(col+intCell,row+intRow+1,newCellValue,normalContNum));
					}
				}else if(obj instanceof Double || obj instanceof Float){
					jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#.##");
					jxl.write.WritableCellFormat wcfN = new jxl.write.WritableCellFormat(nf);
					jxl.write.Number labelNF = new jxl.write.Number(col+intCell,row+intRow+1,(Double)obj,normalContNum);
					sheet.addCell(labelNF);
				}

				else if (isPercent) {
					newCellValue=Double.parseDouble(cellValue.split("%")[0])/100;
					sheet.addCell(new jxl.write.Number(col+intCell,row+intRow+1,newCellValue,normalContPercent));
				} else if (isSPercent) {
					sheet.addCell(new jxl.write.Label(col+intCell,row+intRow+1,cellValue,normalContPercent));
				} else{
					if("-".equals(cellValue)){
						sheet.addCell(new jxl.write.Label(col+intCell,row+intRow+1,cellValue,normalContNum));
					}else{
						sheet.addCell(new jxl.write.Label(col+intCell,row+intRow+1,cellValue,normalCont));
					}
				}
			}
		}
		return sheet;
	}
	
	/**
	 * 合并指定单元格
	 * @param sheet   数据sheet
	 * @param intRow  标题行行数
	 * @return
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	private WritableSheet mergeCells(WritableSheet sheet,int intRow,int intCell,boolean startBorder) throws WriteException,
			RowsExceededException {
		WritableCellFormat normalBoldCenter = getNormalCenter();
		if (mergeCells!=null&&mergeCells.length>0) {
			for (int i = 0; i < mergeCells.length; i++) {
				int[] mergeCell=mergeCells[i];
				sheet.mergeCells(mergeCell[0]+intCell, mergeCell[1], mergeCell[2]+intCell, mergeCell[3]);
				sheet.getWritableCell(mergeCell[0]+intCell, mergeCell[1]).setCellFormat(normalBoldCenter);
			}
		}
		return sheet;
	}

	/**
	 * 初始化reponse输出流
	 */
	private void initResponse() {
		if(this.response == null){
			this.response = ContextHolderUtils.getResponse();
		}
		this.response.reset();
		this.response.setHeader("Content-type", "application/octet-stream");
		this.response.setHeader("Accept-Ranges", "bytes");
		this.response.setContentType("application/x-msdownload");
		this.response.setContentType("application nd.ms-execl; charset=gbk");
		this.response.setHeader("Content-Disposition", "attachment;filename="
				+ (new Date()).getTime() + ".xls");
	}
	
	/**
	 * constructor
	 * @return
	 */
	public boolean isDataExport() {
		return dataExport;
	}

	public void setDataExport(boolean dataExport) {
		this.dataExport = dataExport;
	}

	public void setHeadListArr(List headListArr) {
		this.headListArr = headListArr;
	}

	public void setHeadWidthArr(int[] headWidthArr) {
		this.headWidthArr = headWidthArr;
	}

	public void setHeadHeightArr(int[] headHeightArr) {
		this.headHeightArr = headHeightArr;
	}

	public void setContentList(List contentList) {
		this.contentList = contentList;
	}

	public void setOrdinal(boolean ordinal) {
		this.ordinal = ordinal;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List getHeadListArr() {
		return headListArr;
	}

	public int[] getHeadWidthArr() {
		return headWidthArr;
	}

	public int[] getHeadHeightArr() {
		return headHeightArr;
	}

	public List getContentList() {
		return contentList;
	}

	public boolean isOrdinal() {
		return ordinal;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public String getExportName() {
		return exportName;
	}

	public void setExportName(String exportName) {
		this.exportName = exportName;
	}
	
	public Map<Integer, CellFormat> getContetnStyleMap() {
		return contetnStyleMap;
	}
	public void setContetnStyleMap(Map<Integer, CellFormat> contetnStyleMap) {
		this.contetnStyleMap = contetnStyleMap;
	}
	public int getHeadColSize() {
		return headColSize;
	}
	public void setHeadColSize(int headColSize) {
		this.headColSize = headColSize;
	}
	public List getLastRowArr() {
		return lastRowArr;
	}
	public void setLastRowArr(List lastRowArr) {
		this.lastRowArr = lastRowArr;
	}

	public int getTitleHeight() {
		return titleHeight;
	}

	public void setTitleHeight(int titleHeight) {
		this.titleHeight = titleHeight;
	}

	public int[][] getMergeCells() {
		return mergeCells;
	}

	public void setMergeCells(int[][] mergeCells) {
		this.mergeCells = mergeCells;
	}

	public int getTextAlign() {
		return textAlign;
	}

	public void setTextAlign(int textAlign) {
		this.textAlign = textAlign;
	}
	public int getTitleAlign() {
		return titleAlign;
	}
	
	public void setTitleAlign(int titleAlign) {
		this.titleAlign = titleAlign;
	}

	public int getMergeAlign() {
		return mergeAlign;
	}

	public void setMergeAlign(int mergeAlign) {
		this.mergeAlign = mergeAlign;
	}
}
