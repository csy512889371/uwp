package rongji.framework.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>Title:      </p>
 * <p>Description:      </p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: RongJi</p>
 * 
 * @author Anna 
 * @create in 2013-8-2
 * @version 3.0 
 */
public class PasswordUtil {
	
	/**
	 * 传递一个字符串，返回这个字符串的密文
	 * @param plainText
	 * @return
	 */
	public static String EncoderByMd5(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

//			System.out.println("result: " + buf.toString());// 32位的加密

//			System.out.println("result: " + buf.toString().substring(8, 24));// 16位的加密

			return buf.toString();

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * 将一个原始密码和加密密码进行比较，如果匹配返回true ，否则返回false.
	 * @param newpasswd 页面上传递过来的密码（未加密）
	 * @param oldpasswd 数据库里取出的密码（已加密）
	 * @return
	 */
	public static boolean checkpassword(String newpasswd, String oldpasswd) {
		if (EncoderByMd5(newpasswd).equals(oldpasswd))
			return true;
		else
			return false;
	}

	
}
