package rongji.framework.util.csv;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import rongji.framework.base.ContextHolderUtils;
import rongji.framework.util.csv.UtilCode;

/**
 * CSV文件生成
 * @author sumfing
 * @create at 2011-8-17 
 */
public class CSVCreater extends UtilCode{
	private HttpServletResponse response;
	private String fileName = null;// 文件名
    private FileOutputStream fos = null;// 文件输出流
    private StringBuffer dataBuf = null;// 数据内容

    // 添加单元数据
    public void setData(String data) {
    	data = (data!=null)?data:"";
        if (convertFlag)
            data = UtilCode.CSVEncode(data);
        dataBuf.append(AV_CHAR);
        dataBuf.append(data+"\t");
        dataBuf.append(AV_CHAR);
        dataBuf.append(DEL_CHAR);
    }

    // 添加数据换行符
    public void writeLine() {
        if (dataBuf.charAt(dataBuf.length() - 1) == ',')
            dataBuf.delete(dataBuf.length() - 1, dataBuf.length());
        dataBuf.append("\r\n");
    }

    // 增加行数据
    public void setData(String[] args) {
        for (int i=0,il=args.length; i<il; i++)
            setData(args[i]);
        writeLine();
    }

    // 设置存储文件名
    public void setFileName(String fileName) {
    	this.fileName = fileName;
	}

    // 将内容写入文件并关闭文件流
	public void close() throws IOException {
        try {
            fos.write(dataBuf.toString().getBytes());
        } catch (IOException e) {
            throw e;
        } finally {
            fos.close();
            dataBuf = null;
        }
    }
	
	// 输出文件流
	public void outWrite() throws IOException{
		fileName = (fileName!=null && !"".equals(fileName.trim()))?fileName:""+(new Date()).getTime();
		fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
    	this.response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".csv" + "\"");
		PrintWriter write = this.response.getWriter();
		try{
			write.write(this.dataBuf.toString());
		}catch(Exception e){
		}finally{
			write.flush();
			write.close();
			this.dataBuf = null;
			this.response = null;
		}
	}
	public void outWriteEncoderName() throws IOException{
	this.response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("gbk"),"iso8859-1") + ".csv" + "\"");
		PrintWriter write = this.response.getWriter();
		try{
			write.write(this.dataBuf.toString());
		}catch(Exception e){
		}finally{
			write.flush();
			write.close();
			this.dataBuf = null;
			this.response = null;
		}
	}
	// 选择存储路径
    public CSVCreater(HttpServletResponse rep) throws IOException{
    	this.response = (rep!=null)?rep:ContextHolderUtils.getResponse();
    	this.response.reset();
		this.response.setHeader("Content-type", "application/octet-stream");
		this.response.setHeader("Accept-Ranges", "bytes");
		this.response.setContentType("application/x-msdownload");
    	this.response.setCharacterEncoding("GBK");
    	this.response.setContentType("application/csv;charset=GBK");
    	dataBuf = new StringBuffer();
    }
    
    // 指定文件路径
    public CSVCreater(String arg) throws IOException {
        fos = new FileOutputStream(arg, false);
        dataBuf = new StringBuffer();
    }
    
    // 指定文件存储路径测试
    public static void main(String[] args) {
        try {
            CSVCreater csvCre = new CSVCreater("d:\\test.csv");
            csvCre.setConvertFlag(true);
            csvCre.setData("aaa");
            csvCre.setData("aa,a");
            csvCre.writeLine();
            csvCre.setData("aa\"a");
            csvCre.setData("aa,a");
            csvCre.setData("aa,a");
            csvCre.writeLine();
            csvCre.setData("aa\"a");
            csvCre.setData("aa,\"a");
            csvCre.setData("aa,\"a");
            csvCre.setData("aa,\"a");
            csvCre.setData("aa,\"a");
            csvCre.writeLine();
            csvCre.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}