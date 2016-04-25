package com.shangpin.iog.smets.util;

import java.util.HashSet;
import java.util.Set;

public class LinkQueue {
	//已访问的url
	private static Set<String> visitedUrl = new HashSet<String>();
	//等待访问的url
	private static Queue unVisitedUrl = new Queue();
	//所有产品队列
	private static Queue allSkuUrl = new Queue();
	//获得url队列
	public static Queue getUnVisitedUrl(){
		return unVisitedUrl;
	}
	public static Queue getAllSkuUrl(){
		return allSkuUrl;
	}
	//添加到访问过的url
	public static void addVisitedUrl(String url){
		visitedUrl.add(url);
	}
	//移除访问过的
	public static void removeVisitUrl(String url){
		visitedUrl.remove(url);
	}
	//未访问过的出队列
	public static String unVisitEdUrlDeQueue(){
		return (String) unVisitedUrl.deQueue();
	}
	public static String allSkuUrlDeQueue(){
		return (String) allSkuUrl.deQueue();
	}
	//未访问入队
	public static void addUnvisitedUrl(String url){
		unVisitedUrl.enQueue(url);
	}
	public static void addAllSkuUrl(String url){
		allSkuUrl.enQueue(url);
	}
	//获得已访问的url数目
	public static int getVisitedUrlNum(){
		return visitedUrl.size();
	}
	//判断未访问url是否为空
	public static boolean unVisitedUrlsEmpty(){
		return unVisitedUrl.isQueueEmpty();
	}
	public static boolean allSkuUrllsEmpty(){
		return allSkuUrl.isQueueEmpty();
	}
}
