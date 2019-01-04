package com.shangpin.iog.common.utils.queue;

import java.util.HashSet;
import java.util.Set;

public class PicQueue {
	//已访问的url
	private  Set<String> visitedUrl = new HashSet<String>();
	//等待访问的url
	private  Queue<String> unVisitedUrl = new Queue<String>();
	//所有产品队列
	private  Queue<String> allSkuUrl = new Queue<String>();
	//获得url队列
	public  Queue<String> getUnVisitedUrl(){
		return unVisitedUrl;
	}
	public  Queue<String> getAllSkuUrl(){
		return allSkuUrl;
	}
	//添加到访问过的url
	public  void addVisitedUrl(String url){
		visitedUrl.add(url);
	}
	//移除访问过的
	public  void removeVisitUrl(String url){
		visitedUrl.remove(url);
	}
	//未访问过的出队列
	public  String unVisitEdUrlDeQueue(){
		if(unVisitedUrl.isQueueEmpty()){
			return "";
		}else{
			try {
				return unVisitedUrl.deQueue();
			} catch (Exception e) {
				return "";
			}
		}

	}
	public  String allSkuUrlDeQueue(){
		return  allSkuUrl.deQueue();
	}
	//未访问入队
	public  void addUnvisitedUrl(String url){
		unVisitedUrl.enQueue(url);
	}
	public  void addAllSkuUrl(String url){
		allSkuUrl.enQueue(url);
	}
	//获得已访问的url数目
	public  int getVisitedUrlNum(){
		return visitedUrl.size();
	}
	public  int getunVisitedUrlNum(){
		return unVisitedUrl.size();
	}
	//判断未访问url是否为空
	public  boolean unVisitedUrlsEmpty(){
		return unVisitedUrl.isQueueEmpty();
	}
	public  boolean allSkuUrllsEmpty(){
		return allSkuUrl.isQueueEmpty();
	}
}
