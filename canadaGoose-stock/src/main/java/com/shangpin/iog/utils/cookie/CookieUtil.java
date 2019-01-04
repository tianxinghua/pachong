package com.shangpin.iog.utils.cookie;


import org.apache.commons.lang3.StringUtils;

/**
 * 	获取网页cookie
 * @author njt
 * @date 2018年12月6日 下午6:58:01
 * @desc
 * CookieUtil
 */
public class CookieUtil {
	public static String getCookie(CookieConfig config, String url) {
		String cookie = "";
		String cookieCopy = config.getCookie();
		Long aliveCount = config.getAliveCount();
		Long thresold = config.getThresold();
		if(StringUtils.isNotBlank(cookieCopy)&&aliveCount<=thresold) {
			cookie = cookieCopy;
		}else {
			config.setUrl(url);
			cookie = HttpClientDownloaderWithCookie.download(url, config.getIpProxy());
			if(StringUtils.isNotBlank(config.getCookieExtraParams())) {
				cookie+="; "+config.getCookieExtraParams();
			}
			
		}
		System.err.println("-------cookie的值："+cookie+"!");
		if(StringUtils.isNotBlank(cookie)) {
			if(!cookie.equals(cookieCopy)) {
				config.setCookie(cookie);
				aliveCount = 0L;
			}
			aliveCount++;
			config.setAliveCount(aliveCount);
		}
		return cookie;
	}
}
