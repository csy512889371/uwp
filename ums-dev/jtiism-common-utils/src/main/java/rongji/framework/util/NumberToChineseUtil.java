package rongji.framework.util;

public class NumberToChineseUtil {

	public static boolean check(String strNum) {
		boolean valid = false;

		if (strNum.substring(0, 1).equals("0")) {
			strNum = strNum.substring(1);
		}
		try {
			new Double(strNum);
			valid = true;
		} catch (NumberFormatException ex) {
			System.out.println("Bad number format!");
		}

		return valid;
	}

	public static String readNum(String strNum) {
		String strNumChFormat = "";
		String strNumTemp = "";
		Integer intNumLen = null;
		String strBegin = "";
		if (check(strNum)) {
			strNumChFormat = "";
			intNumLen = strNum.length();
			strNumTemp = strNum;
			strNumTemp = strNumTemp.replace('1', '一');
			strNumTemp = strNumTemp.replace('2', '二');
			strNumTemp = strNumTemp.replace('3', '三');
			strNumTemp = strNumTemp.replace('4', '四');
			strNumTemp = strNumTemp.replace('5', '五');
			strNumTemp = strNumTemp.replace('6', '六');
			strNumTemp = strNumTemp.replace('7', '七');
			strNumTemp = strNumTemp.replace('8', '八');
			strNumTemp = strNumTemp.replace('9', '九');
			strNumTemp = strNumTemp.replace('0', '零');
			strNumTemp = strNumTemp.replace('.', '点');
			strBegin = strNumTemp.substring(0, 1);
			try {
				for (int i = 1, j = 1, k = 1; i < intNumLen; i++) {
					if (strNumTemp.charAt(intNumLen - 1) == '零' && i == 1) {
						strNumChFormat = "位";
					} else if (strNumTemp.charAt(intNumLen - i) == '零' && j == 1) {
						strNumChFormat = "位" + strNumChFormat;
					} else if (strNumTemp.charAt(intNumLen - i) == '点') {
						j = 1;
						k = 1;
						strNumChFormat = strNumTemp.charAt(intNumLen - i) + strNumChFormat;
						continue;
					} else {
						strNumChFormat = strNumTemp.charAt(intNumLen - i) + strNumChFormat;
					}

					if (strNumTemp.charAt(intNumLen - i - 1) != '位' && strNumTemp.charAt(intNumLen - i - 1) != '零') {
						if (j == 1 && i < intNumLen) {
							strNumChFormat = '十' + strNumChFormat;
						} else if (j == 2 && i < intNumLen) {
							strNumChFormat = '百' + strNumChFormat;
						} else if (j == 3 && i < intNumLen) {
							strNumChFormat = '千' + strNumChFormat;
						}
					}

					if (j == 4 && i < intNumLen) {
						j = 0;
					}

					if (k == 4 && i < intNumLen) {
						strNumChFormat = '万' + strNumChFormat;
					} else if (k == 8 && i < intNumLen) {
						k = 0;
						strNumChFormat = '亿' + strNumChFormat;
					}
					j++;
					k++;
				}

				if (StringUtil.isEmpty(strNumChFormat)) {
					strNumChFormat = strNumTemp;
				} else {
					while (strNumChFormat.indexOf("位") != -1) {
						strNumChFormat = strNumChFormat.replaceAll("位", " ");
					}

					if (strNumChFormat.substring(0, 2) == "十") {
						strNumChFormat = strNumChFormat.substring(1, strNumChFormat.length());
					}

					if (strNumChFormat.indexOf("点") >= 0) {
						String rebegin = strNumChFormat.substring(0, strNumChFormat.indexOf("点"));
						String relast = strNumChFormat.substring(strNumChFormat.indexOf("点"), strNumChFormat.length());

						for (int i = 1; i <= relast.length(); i++) {
							relast = relast.replaceAll("十", "");
							relast = relast.replaceAll("百", "");
							relast = relast.replaceAll("千", "");
							relast = relast.replaceAll("万", "");
							relast = relast.replaceAll("亿", "");
						}
						strNumChFormat = rebegin + relast;
					}
				}

			} catch (ArrayIndexOutOfBoundsException ex) {
				ex.printStackTrace();
			}

			if ("一".equals(strBegin) && intNumLen == 2) {
				strNumChFormat = strNumChFormat.substring(0);
			} else if (intNumLen == 1) {
				strNumChFormat = strNumChFormat.substring(0);
			} else {
				strNumChFormat = strBegin + strNumChFormat.substring(0);
			}

		} else {
			strNumChFormat = "";
		}

		return strNumChFormat;
	}
}
