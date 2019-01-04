package com.shangpin.api.airshop.util;

public interface Constants {
	
	/**请求供应商返回的数据状态码*/
	String SUCCESS = "200";
	/** 验证码过期时间 */
	String SESSION_CAPTCHA = "session_captcha";
	/** 验证码过期时间 */
	String SESSION_USER = "airshop_session_user";
	/** 验证码过期时间 */
	long CAPTCHA_EXPIRES = 2 * 60 * 1000;
	
    //exlct
	String NOTE_EXCEL="(Notice： Please modify the stock status according to your stock. 0 means Check Stock, 1 means With Stock, 2 means Without Stock. Any other value will not be accepted.Please DO NOT modify any other column, or the import will be failed.)";
}
