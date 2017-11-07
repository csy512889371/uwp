package rongji.framework.util.doc;

import java.io.File;

import jp.ne.so_net.ga2.no_ji.jcom.IDispatch;
import jp.ne.so_net.ga2.no_ji.jcom.ReleaseManager;


/**
 * 
 * @author alan
 *
 * 需要导入jcom.jar包
 * 且jcom不支持64位系统，建议用jacob1.7以上版本
 */
public class Word2Html {
	
	/**
	 * word文档转换为html文档，保存在相同文件目录下
	 * 
	 * @param wordFilePath
	 */
	public String word2html(String wordFile) {
		ReleaseManager rm = new ReleaseManager();
		String htmlFile = null;
	    try {
	    	File file = new File(wordFile);
	   		if (file.exists()) {
	   			String dot = ".";
   			    String fileName = file.getName().substring(0,file.getName().lastIndexOf(dot));
	   			htmlFile =  file.getParent() + File.separator + fileName + ".html";
	   			
	   			IDispatch wdApp = new IDispatch(rm, "Word.Application");
	   			wdApp.put("Visible", new Boolean(false));
	   			IDispatch wdDocuments = (IDispatch)wdApp.get("Documents");
	   			IDispatch wdDocument = (IDispatch)wdDocuments.method("Open", new Object[]{wordFile});
	   			//转换为HTML文件
	   			wdDocument.method("SaveAs", new Object[]{htmlFile,8});
	   			wdApp.method("Quit", null);
	   		}
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }finally{
	    	rm.release();
	    }
	    return htmlFile;
	}

	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Word2Html one =  new Word2Html();
		one.word2html("d:\\src\\111111111111.doc");
		one.word2html("d:\\src\\2222222222222.docx");

	}
}
