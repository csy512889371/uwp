package rongji.framework.util.doc;

import java.io.File;

import jp.ne.so_net.ga2.no_ji.jcom.IDispatch;
import jp.ne.so_net.ga2.no_ji.jcom.ReleaseManager;

/**
 * 
 * @author alan
 *
 * 需要导入jcom.jar包;
 * 也需要将其中的jcom.dll文件放置到windows系统System32目录下，
 * 如果不想污染System32目录中的dll文件，也可以将其放置到本机JDK安装目录下的bin文件夹中;
 * 服务器端安装Adobe Acrobat 8.1.2以上版本
 * 且不支持64位版本
 */
public class Office2Pdf {

	/**
	 * office文档转换为pdf文档，保存再相同文件目录下
	 * 
	 * @param officeFile
	 * @return
	 * @throws Exception
	 */
	public String createPdfFromOfficeFile(String officeFile) throws Exception {   
		ReleaseManager rm = null;   
		IDispatch app = null;
		String pdfFile = null;
		try {
			File file = new File(officeFile);
	   		if (file.exists()) {
	   			String dot = ".";
   			    String fileName = file.getName().substring(0,file.getName().lastIndexOf(dot));
   			    pdfFile =  file.getParent() + File.separator + fileName + ".pdf";
   			    
	   			rm=new ReleaseManager();
				app = new IDispatch(rm, "PDFMakerAPI.PDFMakerApp");
				app.method("CreatePDF",new Object[]{officeFile,pdfFile});
	   		}
	   		
		} catch (Exception e) {   
			throw e;   
		} finally {   
			try {   
				app=null;   
				rm.release();   
				rm = null;   
			} catch (Exception e) {   
				throw e;   
			}   
		}
		return pdfFile;
	}   
		 
		
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 Office2Pdf one=new Office2Pdf();   
		 try {
//			one.createPdfFromOfficeFile("D:\\pdf\\1.xls"); 
			one.createPdfFromOfficeFile("D:\\pdf\\2.doc");
//			one.createPdfFromOfficeFile("D:\\pdf\\4.docx");   
//			one.createPdfFromOfficeFile("D:\\pdf\\5.pptx");   
//			one.createPdfFromOfficeFile("D:\\pdf\\6.ppt");   
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		
	}

}
