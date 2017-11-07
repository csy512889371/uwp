package rongji.framework.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//xiaoma
public class MathJsgs {
	private static final String optrflag = "(#+-#*/#%)";

	private static final Pattern errorpattern = Pattern
			.compile("([^0-9\\+\\-\\*/\\(\\)\\. %]|"
					+ "[^0-9]{1,}\\.[^0-9]{1,}|" + "^\\.|" + "\\.$|"
					+ "\\([^0-9\\( ]|" + "[^0-9\\) ]\\)|"
					+ "[^\\+\\-\\*/\\(% ]\\(|" + "\\)[^\\+\\-\\*/\\) %])");

	private static Matcher m;

	// getResult("(1+2)/(3+1)")
	public static double getResult(String expression) {
		String validatestr = validateExpression(expression);
		if (validatestr != null) {
			return 0;
		}
		expression = expression.trim();
		expression = expression.replaceAll("\\+", " + ").replaceAll("\\-",
				" - ").replaceAll("\\*", " * ").replaceAll("/", " / ")
				.replaceAll("%", " % ").replaceAll("\\(", " ( ").replaceAll(
						"\\)", " ) ").replaceAll(" +", " ")
				.replaceAll("^ ", "").replaceAll(" $", "");
		Stack valuestack = new Stack();
		Stack optrstack = new Stack();
		String[] expchars = expression.split(" +");
		int startindex = 0;

		String ep;
		String top;
		while (startindex < expchars.length) {
			ep = expchars[startindex];
			startindex++;
			if (optrflag.indexOf(ep) == -1) {
				valuestack.push(ep);
			} else {
				if (optrstack.isEmpty()) {
					optrstack.push(ep);
					continue;
				}
				top = optrstack.pop().toString();

				int rs = comparepri(top, ep);
				if (rs == 0) {
					continue;
				} else if (rs < 0) {
					optrstack.push(top);
					optrstack.push(ep);
				} else {
					startindex--;
					double b = Double.parseDouble(valuestack.pop().toString());
					double a = Double.parseDouble(valuestack.pop().toString());
					valuestack.push("" + getresult(a, b, top));
				}
			}

		}
		while (!optrstack.isEmpty()) {
			double b = Double.parseDouble(valuestack.pop().toString());
			double a = Double.parseDouble(valuestack.pop().toString());
			valuestack.push("" + getresult(a, b, optrstack.pop().toString()));
		}

		return Double.parseDouble(valuestack.pop().toString());
	}
	
	public static String getResultByExpression(String expression) {
		String result = "0";
		try{
		String validatestr = validateExpression(expression);
		if (validatestr != null) {
			return "0";
		}
		expression = expression.trim();
		expression = expression.replaceAll("\\+", " + ").replaceAll("\\-",
				" - ").replaceAll("\\*", " * ").replaceAll("/", " / ")
				.replaceAll("%", " % ").replaceAll("\\(", " ( ").replaceAll(
						"\\)", " ) ").replaceAll(" +", " ")
				.replaceAll("^ ", "").replaceAll(" $", "");
		Stack valuestack = new Stack();
		Stack optrstack = new Stack();
		String[] expchars = expression.split(" +");
		int startindex = 0;

		String ep;
		String top;
		while (startindex < expchars.length) {
			ep = expchars[startindex];
			startindex++;
			if (optrflag.indexOf(ep) == -1) {
				valuestack.push(ep);
			} else {
				if (optrstack.isEmpty()) {
					optrstack.push(ep);
					continue;
				}
				top = optrstack.pop().toString();

				int rs = comparepri(top, ep);
				if (rs == 0) {
					continue;
				} else if (rs < 0) {
					optrstack.push(top);
					optrstack.push(ep);
				} else {
					startindex--;
					double b = Double.parseDouble(valuestack.pop().toString());
					double a = Double.parseDouble(valuestack.pop().toString());
					if("/".equals(top)){
						if(b==0){
							return "-";
						}
					}
					valuestack.push("" + getresult(a, b, top));
				}
			}

		}
		while (!optrstack.isEmpty()) {
			double b = Double.parseDouble(valuestack.pop().toString());
			double a = Double.parseDouble(valuestack.pop().toString());
			String fs = optrstack.pop().toString();
			if("/".equals(fs)){
				if(b==0){
					return "-";
				}
			}
			valuestack.push("" + getresult(a, b, fs));
		}
		 result = (valuestack.pop().toString());
		
		}catch(Exception e){
			return "-";
		}
		return result;
	}

	// ��ȡ����ʽ���������
	public static List getMath(String expression) {
		String validatestr = validateExpression(expression);
		List resultMath = new ArrayList();
		if (validatestr != null) {
			return null;
		}
		expression = expression.trim();
		expression = expression.replaceAll("\\+", " + ").replaceAll("\\-",
				" - ").replaceAll("\\*", " * ").replaceAll("/", " / ")
				.replaceAll("%", " % ").replaceAll("\\(", " ( ").replaceAll(
						"\\)", " ) ").replaceAll(" +", " ")
				.replaceAll("^ ", "").replaceAll(" $", "");

		String[] expchars = expression.split(" +");
		int startindex = 0;

		String math;
		while (startindex < expchars.length) {
			math = expchars[startindex];
			startindex++;
			if (optrflag.indexOf(math) == -1) {
				resultMath.add(math);
			} else {

			}

		}
		return resultMath;
	}

	// ��ȡ����ʽ���������(��,�Ÿ���)
	public static String getMathStr(String expression) {
		String validatestr = validateExpression(expression);
		StringBuffer sb = new StringBuffer();
		if (validatestr != null) {
			return null;
		}
		expression = expression.trim();
		expression = expression.replaceAll("\\+", " + ").replaceAll("\\-",
				" - ").replaceAll("\\*", " * ").replaceAll("/", " / ")
				.replaceAll("%", " % ").replaceAll("\\(", " ( ").replaceAll(
						"\\)", " ) ").replaceAll(" +", " ")
				.replaceAll("^ ", "").replaceAll(" $", "");

		String[] expchars = expression.split(" +");
		int startindex = 0;

		String math;
		while (startindex < expchars.length) {
			math = expchars[startindex];
			startindex++;
			if (optrflag.indexOf(math) == -1) {
				sb.append(math + ",");
			} else {

			}

		}
		return sb.deleteCharAt(sb.length() - 1).toString();
	}

	public static String validateExpression(String expression) {
		expression = expression.replaceAll(" +", " ");
		m = errorpattern.matcher(expression);
		if (m.find()) {
			return m.group(1);
		}
		m = errorpattern.matcher(expression.trim());
		if (m.find()) {
			return m.group(1);
		}
		return null;
	}

	private static int comparepri(String top, String insert) {
		int i = optrflag.indexOf(top);
		int j = optrflag.indexOf(insert);
		if (i < 0 || i == 1 || i == 4 || i == 7 || j < 0 || j == 1 || j == 4
				|| j == 7) {
			return 0;
		}
		int distance = j - i;
		if (distance == -9) {
			return 0;
		}
		if (distance == 9) {
			return 0;
		} else if (i == j && i != 0) {
			return 1;
		} else if (i == j && i == 0) {
			return -1;
		} else if (j == 9) {
			return 1;
		} else if (j == 0) {
			return -1;
		} else if (j - i <= 1) {
			return 1;
		} else {
			return -1;
		}
	}

	public static double getresult(double a, double b, String operator) {
		char operat = operator.toCharArray()[0];
		switch (operat) {
		case '%':
			return getmod(a, b);
		case '+':
			return getsum(a, b);
		case '-':
			return getgap(a, b);
		case '*':
			return getproduct(a, b);
		case '/':
			return getquotient(a, b);
		default: {
			return 0;
		}
		}
	}

	public static double getsum(double a, double b) {
		return a + b;
	}

	public static double getgap(double a, double b) {
		return a - b;
	}

	public static double getproduct(double a, double b) {
		return a * b;
	}

	public static double getquotient(double a, double b) {
		if (b == 0) {
			return 0;
		}
		return a / b;
	}

	public static double getmod(double a, double b) {
		if (b == 0) {
			return 0;
		}
		return a % b;
	}
	
	//����ͬ��lenghtС�����λ(data1-data2)/data2
	public static String jstb(String data1,String data2,int lenght){
		String result = "";
		BigDecimal data1Temp = new BigDecimal("0");
		BigDecimal data2Temp = new BigDecimal("0");
		if(data1 != null && !"".equals(data1)){
			data1Temp = new BigDecimal(data1);
		}
		if(data2 != null && !"".equals(data2)){
			data2Temp = new BigDecimal(data2);
		}
		if("0".equals(data1) && !"0".equals(data2)){
			
			return "-";
		}
		if(!"0".equals(data2)){
			BigDecimal resultTemp = (data1Temp.subtract(data2Temp)).divide(data2Temp,lenght,BigDecimal.ROUND_HALF_UP).scaleByPowerOfTen(2);
			if(resultTemp != null){
				result = resultTemp+"%";
			}else{
				result="-";
			}
		}else{
			result="-";
		}
		return result;
	}
	/**
	 * 
	 * @param data Դ����
	 * @param lenght С���㼸λ��С�������Ϊ0�Ĺ��˵� 2.00 = 2
	 * @return
	 */
	public static String formatValue(String data,int lenght){
		try{
			BigDecimal dataTemp = new BigDecimal(data); 
			BigDecimal resultTemp = new BigDecimal("1");
			resultTemp = dataTemp.divide(resultTemp,lenght,BigDecimal.ROUND_HALF_UP);
			String result = resultTemp.toString();
			String temp = "";
			for(int i=0;i<lenght;i++){
				temp+="0";
			}
			if(result.substring(result.indexOf(".")+1).equals(temp)){
				return result.substring(0,result.indexOf("."));
			}else{
				result = checkValue(result);
				return result;
			}
		}catch(Exception e){
			return "0";
		}
	}
	
	/**
	 * 
	 * @param data Դ����
	 * @param lenght С���㼸λ��С�������Ϊ0�Ĳ����� 2.00 = 2.00
	 * @return
	 */
	public static String formatValueWithZero(String data,int lenght){
		
		try{
			BigDecimal dataTemp = new BigDecimal(data); 
			BigDecimal resultTemp = new BigDecimal("1");
			resultTemp = dataTemp.divide(resultTemp,lenght,BigDecimal.ROUND_HALF_UP);
			return resultTemp.toString();
		}catch(Exception e){
			return "0.00";			
		}
		
	}
	
	//ȥ��С�������Ϊ0��
	public static String checkValue(String data){
		String result="";
		if(data.indexOf(".")>-1){
			String begS = data.substring(0,data.indexOf("."));
			String endS = data.substring(data.indexOf(".")+1);
			for(int i=endS.length()-1;i>=0;i--){
				if("0".equals(endS.substring(i))){
					endS = endS.substring(0,(i));
				}else{
					break;
				}
			}
			if("".equals(endS.trim())){
				result = begS;
			}else{
				result = begS+"."+endS;
			}
		}else{
			result = data;
		}
		return result;
	}
	
	//�������
	public static String jsbl(String data1,String data2,int lenght){
		String result = "";
		BigDecimal data1Temp = new BigDecimal("0");
		BigDecimal data2Temp = new BigDecimal("0");
		if(data1 != null && !"".equals(data1)){
			data1Temp = new BigDecimal(data1);
		}
		if(data2 != null && !"".equals(data2)){
			data2Temp = new BigDecimal(data2);
		}
		if("0".equals(data1) && !"0".equals(data2)){
			
			return "-";
		}
		if(!"0".equals(data2)){
			BigDecimal resultTemp = (data1Temp).divide(data2Temp,lenght,BigDecimal.ROUND_HALF_UP).scaleByPowerOfTen(2);
			if(resultTemp != null){
				result = resultTemp+"%";
			}else{
				result="-";
			}
		}else{
			result="-";
		}
		return result;
	}
	
	/**
	 * return data1-data2
	 * �����ֵ��ǰ��ӡ���������������������ʾ���
	 */
	public static String getSub(String data1,String data2){
		String result = "";
		BigDecimal data1Temp = new BigDecimal("0");
		BigDecimal data2Temp = new BigDecimal("0");
		if(data1 != null && !"".equals(data1) && !"-".equals(data1)){
			data1Temp = new BigDecimal(data1);
		}
		if(data2 != null && !"".equals(data2) && !"-".equals(data2)){
			data2Temp = new BigDecimal(data2);
		}

		BigDecimal resultTemp = data1Temp.subtract(data2Temp);
		if(data1Temp.max(data2Temp) == data1Temp.min(data2Temp)){ //����ֵ���
			result = ""+resultTemp.abs();
		}else{
			if(data1Temp.max(data2Temp)==data1Temp){
				result = "��"+resultTemp.abs();
			}else{
				result = "��"+resultTemp.abs();
			}
		}

		return result;
	}
	
	/**
	 * ��ȡ�������λ��
	 * 
	 * @param dataArr
	 * @param lenght
	 * @return
	 */
	public static String getMiddle(double[] dataArr, int xsw){
		String result = "";
		if(dataArr!=null && dataArr.length>0){
			Arrays.sort(dataArr);
			int length = dataArr.length;
			if(length%2==0){
				//ż����
				result = divide(String.valueOf(dataArr[(length/2-1)] + dataArr[(length/2)]), "2", xsw);
			}else{
				//������
				result = String.valueOf(dataArr[(length/2)]);
			}
		}else{
			result="-";
		}
		return result;
	}
	
	/**
	 * ��ȡ����ı�׼��
	 * 
	 * @param dataArr
	 * @param avg
	 * @return
	 */
	public static String getBzc(double[] dataArr, double avg){
		String result = "";
		if(dataArr!=null && dataArr.length>0){
			double sum = 0;
	        for(int i = 0;i < dataArr.length;i++){
	            sum += ((double)dataArr[i] -avg) * (dataArr[i] - avg);
	        }
	        result = String.format("%.2f", Math.sqrt(sum/dataArr.length));
		}else{
			result="-";
		}
		return result;
	}
	
	public static String divide(String fz,String fm,int lenght){
		String result = "";
		BigDecimal data1Temp = new BigDecimal("0");
		BigDecimal data2Temp = new BigDecimal("0");
		if(fz != null && "-".equals(fz) && fm != null && !"-".equals(fm)){
			return "0";
		}
		if(("0".equals(fz) || "-".equals(fz)) && !"0".equals(fm)){
			return "0";
		}
		if(fm != null && ("-".equals(fm) || "0".equals(fm))){
			return "-";
		}
		
		if(fz != null && !"".equals(fz) && !"-".equals(fz)){
			data1Temp = new BigDecimal(fz);
		}
		if(fm != null && !"".equals(fm) && !"-".equals(fm)){
			data2Temp = new BigDecimal(fm);
		}
		
		if(!"0".equals(fm) && !"-".equals(fm)){
			BigDecimal resultTemp = (data1Temp).divide(data2Temp,lenght,BigDecimal.ROUND_HALF_UP);
			if(resultTemp != null){
				result = resultTemp+"";
			}else{
				result="-";
			}
		}else{
			result="-";
		}
		return result;
	}
	
}
