package com.shangpin.iog.common.utils.httpclient;

/**
 * http请求超时时间设置
 * @see #defaultOutTimeConfig() 默认超时时间
 * @description 
 * @author 陈小峰
 * <br/>2015年7月12日
 */

public class OutTimeConfig {

	private int connectOutTime;
	private int requestOutTime;
	private int socketOutTime;
	
	private static OutTimeConfig defaultConfig=new OutTimeConfig(2000,3000,2000);
	/**
	 * 获取默认超时时间设置<br/>
	 * 连接超时：2s，请求超时：3s，socket超时：2s
	 * @return
	 */
	public static OutTimeConfig defaultOutTimeConfig(){
		return defaultConfig;
	}
	/**
	 * 
	 */
	public OutTimeConfig() {
		super();
	}
	/**
	 * @param connectOutTime 连接超时时间
	 * @param requestOutTime 请求超时时间
	 * @param socketOutTime socket连接超时时间
	 */
	public OutTimeConfig(int connectOutTime, int requestOutTime,
			int socketOutTime) {
		super();
		this.connectOutTime = connectOutTime;
		this.requestOutTime = requestOutTime;
		this.socketOutTime = socketOutTime;
	}
	/**
	 * 连接超时时间配置
	 * @param connectOutTime
	 * @return
	 */
	public OutTimeConfig confConnectOutTime(int connectOutTime) {
		this.connectOutTime = connectOutTime;
		return this;
	}
	/**
	 * 请求超时时间配置
	 * @param requestOutTime
	 * @return
	 */
	public OutTimeConfig confRequestOutTime(int requestOutTime) {
		this.requestOutTime = requestOutTime;
		return this;
	}
	/**
	 * socket超时时间配置<br/>
	 * httpclient实现是通过socket做的
	 * @param socketOutTime
	 * @return
	 */
	public OutTimeConfig confSocketOutTime(int socketOutTime) {
		this.socketOutTime = socketOutTime;
		return this;
	}
	/**
	 * 连接超时时间
	 * @return
	 */
	public int getConnectOutTime() {
		return connectOutTime;
	}
	/**
	 * 请求超时时间
	 * @return
	 */
	public int getRequestOutTime() {
		return requestOutTime;
	}
	/**
	 * socket超时时间
	 * @return
	 */
	public int getSocketOutTime() {
		return socketOutTime;
	}
	
}
