package rongji.report.utils;

import org.apache.commons.lang3.StringUtils;


public final class ReportStringUtil {

	private static int compare(String str, String target) {
		if (str == null || "".equals(str))
			str = " ";
		if (target == null || "".equals(target))
			target = " ";
		int d[][]; // 矩阵
		int n = str.length();
		int m = target.length();
		int i; // 遍历str的
		int j; // 遍历target的
		char ch1; // str的
		char ch2; // target的
		int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
		if (n == 0) {
			return m;
		}
		if (m == 0) {
			return n;
		}
		d = new int[n + 1][m + 1];
		for (i = 0; i <= n; i++) { // 初始化第一列
			d[i][0] = i;
		}

		for (j = 0; j <= m; j++) { // 初始化第一行
			d[0][j] = j;
		}

		for (i = 1; i <= n; i++) { // 遍历str
			ch1 = str.charAt(i - 1);
			// 去匹配target
			for (j = 1; j <= m; j++) {
				ch2 = target.charAt(j - 1);
				if (ch1 == ch2 || ch1 == ch2 + 32 || ch1 + 32 == ch2) {
					temp = 0;
				} else {
					temp = 1;
				}
				// 左边+1,上边+1, 左上角+temp取最小
				d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
			}
		}
		return d[n][m];
	}

	private static int min(int one, int two, int three) {
		return (one = one < two ? one : two) < three ? one : three;
	}

	/**
	 * 获取两字符串的相似度
	 */

	public static float getSimilarityRatio(String str, String target) {
		return 1 - (float) compare(str, target) / Math.max(str.length(), target.length());
	}

	public static Integer getErrorEndNum(String str) {
		Integer num = 0;
		for (int i = str.length() - 1; i > 0; i--) {
			char charCode = str.charAt(i);
			if (charCode == '（' || charCode == '(') {
				num++;
			} else {
				break;
			}
		}
		return num;
	}

	public static String getErrorStartStr(String str, Integer allowNum) {
		Integer num = 0;
		String errorCodes = "";
		for (int i = 0; i < str.length(); i++) {
			char charCode = str.charAt(i);
			if (charCode == ')' || charCode == '，' || charCode == ';' || charCode == ':' || charCode == '.') {
				num += 1;
				if (num > allowNum) {
					break;
				}
				errorCodes += charCode;
			} else if (charCode == '）' || charCode == ',' || charCode == '；' || charCode == '。' || charCode == '：') {
				num += 2;
				if (num > allowNum) {
					break;
				}
				errorCodes += charCode;
			} else {
				break;
			}
		}
		return errorCodes;
	}

	public static String removeSpace(String str) {
		if (StringUtils.isNotEmpty(str)) {
			return str.replaceAll(" ", "");
		}
		return "";
	}
}
