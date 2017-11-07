package rongji.framework.util.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rongji.framework.util.StringUtil;
import rongji.framework.util.csv.UtilCode;

/**
 * CSV文件解析
 * @author sumfing
 * @create at 2011-8-17 
 */
public class CSVAnalysis extends UtilCode{
    private InputStreamReader fr = null;// 输入流读取
    private ArrayList dataContainer = new ArrayList();

    public CSVAnalysis(File file) throws IOException {
    	fr = new InputStreamReader(new FileInputStream(file));
    }
    
    public CSVAnalysis(FileInputStream inStream) throws IOException {
    	fr = new InputStreamReader(inStream);
    }
    
    public CSVAnalysis(String f) throws IOException {
        fr = new InputStreamReader(new FileInputStream(f));
    }

    public ArrayList analysis() throws IOException {
        BufferedReader br = new BufferedReader(fr);
        String rec = null;
        try {
        	Pattern pCells = Pattern.compile("(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,");
            while ((rec = br.readLine()) != null) {
                ArrayList alLine = analysisLine(pCells,rec);
                dataContainer.add(alLine);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            br.close();
        }

        return dataContainer;
    }

    private ArrayList analysisLine(Pattern pCells,String strLine) {
    	ArrayList cells = new ArrayList();// 每行记录一个list
    	if(!StringUtil.isEmpty(strLine)){
    		Matcher mCells = pCells.matcher(strLine+",\"\"");
    		String str = null;
    		// 读取每个单元格
    		while (mCells.find()) {
    			str = mCells.group();
    			if(str.endsWith(",")){
    				str = str.substring(0, str.length() - 1);
    			}
    			if (str.startsWith("\"") && str.endsWith("\"")) {
    				str = str.substring(1, str.length() - 1);
    			}
//    			str = str.replaceAll("(?sm)\"?([^\"]*(\"{2})*[^\"]*)\"?.*,", "$1");
    			str = str.replaceAll("(?sm)(\"(\"))", "$2");
    			str = str.trim();
    			cells.add((convertFlag)?UtilCode.CSVDecode(str):str);
    		}   
    	}
		return cells;
    }

    public void close() throws IOException {
        fr.close();
    }
    
    private static final String SPECIAL_CHAR_A = "[^\",\\n 　]";
    private static final String SPECIAL_CHAR_B = "[^\",\\n]";
    /**
     * 匹配csv文件里最小单位的正则表达式
     * @return
     */
    private String getRegExp() {
        String strRegExp = "";
        strRegExp =
            "\"(("+ SPECIAL_CHAR_A + "*[,\\n 　])*("+ SPECIAL_CHAR_A + "*\"{2})*)*"+ SPECIAL_CHAR_A + "*\"[ 　]*,[ 　]*"
            +"|"+ SPECIAL_CHAR_B + "*[ 　]*,[ 　]*"
            + "|\"(("+ SPECIAL_CHAR_A + "*[,\\n 　])*("+ SPECIAL_CHAR_A + "*\"{2})*)*"+ SPECIAL_CHAR_A + "*\"[ 　]*"
            + "|"+ SPECIAL_CHAR_B + "*[ 　]*";
        
        return strRegExp;
    }
    
    public static void main(String[] args) {
        try {
            CSVAnalysis csvAna = new CSVAnalysis("D:\\test.csv");
            csvAna.setConvertFlag(true);
            ArrayList al = csvAna.analysis();
            for (int i = 0; i < al.size(); i++) {
                ArrayList al1 = (ArrayList) al.get(i);
                for (int j = 0; j < al1.size(); j++) {
                    System.out.println(al1.get(j));
                }
            }
            csvAna.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}