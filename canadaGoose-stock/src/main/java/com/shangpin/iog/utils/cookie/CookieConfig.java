package com.shangpin.iog.utils.cookie;
/**
 * 	cookieClient的相关参数
 * @author njt
 * @date 2018年12月6日 下午7:30:41
 * @desc
 * CookieConfig
 */
public class CookieConfig {
	/**
	 * cookie更换阈值
	 */
	private Long thresold = 1000L;
	/**
	 * cookie存活数
	 */
	private Long aliveCount = 0L;
	/**
	 * 获取cookie的页面
	 */
	private String url;
	/**
	 * 重要的cookie参数（client获取不到的重要参数）
	 */
	private String cookieExtraParams;
	/**
	 * cookie副本
	 */
	private String cookie;
	/**
	 * client的代理IP
	 */
	private String ipProxy;
	
	public CookieConfig() {
		super();
	}
	public CookieConfig(Long thresold, String url, String cookieExtraParams, String ipProxy) {
		super();
		this.thresold = thresold;
		this.url = url;
		this.cookieExtraParams = cookieExtraParams;
		this.ipProxy = ipProxy;
	}
	public Long getThresold() {
		return thresold;
	}
	public void setThresold(Long thresold) {
		this.thresold = thresold;
	}
	public Long getAliveCount() {
		return aliveCount;
	}
	public void setAliveCount(Long aliveCount) {
		this.aliveCount = aliveCount;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCookieExtraParams() {
		return cookieExtraParams;
	}
	public void setCookieExtraParams(String cookieExtraParams) {
		this.cookieExtraParams = cookieExtraParams;
	}
	public String getCookie() {
		return cookie;
	}
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	public String getIpProxy() {
		return ipProxy;
	}
	public void setIpProxy(String ipProxy) {
		this.ipProxy = ipProxy;
	}
	
}
