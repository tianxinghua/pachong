package com.shangpin.iog.vinicco.utils;


import java.security.MessageDigest;

public class Md5 {
	public static String getMD5(String src_txt) {
		MessageDigest digest;
		String algorithm = "MD5";
		try {
			digest = MessageDigest.getInstance(algorithm);
			digest.update(src_txt.getBytes());
			return byte2hex(digest.digest());
		} catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toLowerCase();
	}
	

	public static void main(String[] args){
//		Md5 md = new Md5();
//		md.getMD5("123");
//		System.out.println(md.getMD5("123"));
	}
}
