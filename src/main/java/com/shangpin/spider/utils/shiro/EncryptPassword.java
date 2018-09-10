package com.shangpin.spider.utils.shiro;

import java.util.Random;

import org.apache.shiro.crypto.hash.Md5Hash;

/** 
 * @author  njt 
 * @date 创建时间：2017年11月10日 下午5:37:16 
 * @version 1.0 
 * @parameter  
 */

public class EncryptPassword {
	/**
	 * 密码加密
	 * @param username
	 * @param password
	 * @return
	 */
	public static String encryptPassword(String username,String password){
		Random random = new Random();
	    int salt = random.nextInt(999999)%900000+100000;
	    String encryptPw = new Md5Hash(password, username+salt, 2).toString();
	    return encryptPw;
	}
}
