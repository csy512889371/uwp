package rongji.framework.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.lang3.StringUtils;

public class ToPinYin {
	public static String getFirstSpell(String chinese) {
		if (StringUtils.isEmpty(chinese)) {
			return "";
		}
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > '')
				try {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(
							arr[i], defaultFormat);
					if (temp != null)
						pybf.append(temp[0].charAt(0));
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString().replaceAll("\\W", "").trim();
	}

	public static String toPinYin(String str) {
		if (StringUtils.isEmpty(str)) {
			return "";
		}
		String py = "";
		String[] t = new String[str.length()];

		char[] hanzi = new char[str.length()];
		for (int i = 0; i < str.length(); i++) {
			hanzi[i] = str.charAt(i);
		}

		HanyuPinyinOutputFormat t1 = new HanyuPinyinOutputFormat();
		t1.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		t1.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		t1.setVCharType(HanyuPinyinVCharType.WITH_V);
		try {
			for (int i = 0; i < str.length(); i++)
				if (((str.charAt(i) >= 'a') && (str.charAt(i) <= 'z'))
						|| ((str.charAt(i) >= 'A') && (str.charAt(i) <= 'Z'))
						|| ((str.charAt(i) >= '0') && (str.charAt(i) <= '9'))) {
					py = py + str.charAt(i);
				} else {
					t = PinyinHelper.toHanyuPinyinStringArray(hanzi[i], t1);
					if ((t != null) && (t.length > 0))
						py = py + t[0];
				}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}

		return py.trim().toString();
	}

	public static void main(String[] args) {
		System.out.println(toPinYin(""));
	}
}
