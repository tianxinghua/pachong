package com.shangpin.iog.smets.queue;

import java.util.HashSet;
import java.util.Set;

import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.smets.util.Queue;

public class SkuQueue {
	//已访问的url
	private static Set<SkuDTO> visitedSku = new HashSet<SkuDTO>();
	//等待访问的url
	private static Queue<SkuDTO> unVisitedSku = new Queue<SkuDTO>();
	//所有产品队列
	private static Queue<SkuDTO> allSkuUrl = new Queue<SkuDTO>();
	//获得url队列
	public static Queue<SkuDTO> getUnVisitedUrl(){
		return unVisitedSku;
	}
	public static Queue<SkuDTO> getAllSkuUrl(){
		return allSkuUrl;
	}
	//添加到访问过的url
	public static void addVisitedUrl(SkuDTO sku){
		visitedSku.add(sku);
	}
	//移除访问过的
	public static void removeVisitUrl(SkuDTO sku){
		visitedSku.remove(sku);
	}
	//未访问过的出队列
	public static SkuDTO unVisitEdSkuDeQueue(){
		return unVisitedSku.deQueue();
	}
	public static SkuDTO allSkuSkuDeQueue(){
		return allSkuUrl.deQueue();
	}
	//未访问入队
	public static void addUnvisitedSku(SkuDTO sku){
		unVisitedSku.enQueue(sku);
	}
	public static void addAllSkuSku(SkuDTO sku){
		allSkuUrl.enQueue(sku);
	}
	//获得已访问的url数目
	public static int getVisitedSkuNum(){
		return visitedSku.size();
	}
	public static int getunVisitedUrlNum(){
		return unVisitedSku.size();
	}
	//判断未访问url是否为空
	public static boolean unVisitedSkusEmpty(){
		return unVisitedSku.isQueueEmpty();
	}
	public static boolean allSkuSkulsEmpty(){
		return allSkuUrl.isQueueEmpty();
	}
}
