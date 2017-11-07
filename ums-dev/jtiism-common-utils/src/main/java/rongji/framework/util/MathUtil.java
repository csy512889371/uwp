package rongji.framework.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * <p>
 * Title: 数字，数学相关共用类
 * </p>
 * <p>
 * Description:1.提供获取数字的长度
 * </p>
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * <p>
 * Company: RongJi
 * </p>
 * 
 * @author redlwb
 * @create in 2012-8-22
 * @version 1.0
 */
public class MathUtil {
	
	private MathUtil(){}

	/**
	 * 获取数字长度
	 * @param num
	 * @return
	 * @author：redlwb
	 * add 2012-9-20
	 */
	public static int getNumLength(int num){
		return (num+"").length();
	}
	
	/**
	 * 获取数字长度
	 * @param num
	 * @return
	 * @author：redlwb
	 * add 2012-9-20
	 */
	public static int getNumLength(long num){
		return (num+"").length();
	}
	
	/**
	 * double类型的减法，v1 - v2
	 * @date 2012-11-1 上午09:12:00
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}
	/**
	 * 获取a是b的几倍，小于1取1（如3 是2 的  1.5倍，取整为2 ）
	 * @param a
	 * @param b
	 * @return
	 */
	public static int  getTime(int a,int b){
		int result=0;
		if(a%b==0){
			result=a/b;
		}else{
			result=(a/b)+1;
		}
		return result;
	}
	
	/**
	 * 获取a是b的几倍，小于1取1（如3 是2 的  1.5倍，取整为2 ）
	 * @param a
	 * @param b
	 * @return
	 */
	public static long  getTime(long a,long b){
		long result=0;
		if(a%b==0){
			result=a/b;
		}else{
			result=(a/b)+1;
		}
		return result;
	}
	


	/**
	 * 将数据转换为指定的格式
	 * @param num 要转换的数据
	 * @param m 保留小数点位数
	 * @return
	 */
	public static double convertNum(Double num, int m){
		String patten = "#";
		for(int i=0; i<m; i++){
			if(i==0){
				patten+=".#";
			}else{
				patten+="#";
			}
		}
		DecimalFormat df = new DecimalFormat(patten);
		num = Double.parseDouble(df.format(num));
		return num;
	}
	
}
