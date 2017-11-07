package rongji.framework.util.csv;

/**
 * csv文件生成及解析字符转义静态处理方法
 * @author sumfing 
 * @create at 2011-8-17
 */
public class UtilCode {
	protected boolean convertFlag = false;// 字符（反）转义标志

	protected static final String DEL_CHAR = ",";

	protected static final String AV_CHAR = "\"";
    
    public static String CSVEncode(String in) {
        String out = "";
        if (in == null)
            return out;
        out = in.replaceAll("&", "&amp;");
        out = out.replaceAll("\\r\\n", "&quot;wrn&quot;");
        out = out.replaceAll("\\n", "&quot;lrn&quot;");
        out = out.replaceAll("\"", "&quot;");
        return out;
    }

    public static String CSVDecode(String in) {
        String out = "";
        if (in == null)
            return out;
        out = in.replaceAll("&quot;wrn&quot;", "\r\n");
        out = out.replaceAll("&quot;lrn&quot;", "\n");
        out = out.replaceAll("&quot;", "\"");
        out = out.replaceAll("&amp;", "&");
        return out;
    }
    
    public void setConvertFlag(boolean b) {
        convertFlag = b;
    }
}