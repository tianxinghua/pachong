package com.shangpin.iog.smets.queue;

import java.util.HashSet;
import java.util.Set;

import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.smets.util.Queue;

public class SpuQueue {
	//已访问的url
	private static Set<SpuDTO> visitedSpu = new HashSet<SpuDTO>();
	//等待访问的url
	private static Queue<SpuDTO> unVisitedSpu = new Queue<SpuDTO>();
	//所有产品队列
	private static Queue<SpuDTO> allSpuUrl = new Queue<SpuDTO>();
	//获得url队列
	public static Queue<SpuDTO> getUnVisitedUrl(){
		return unVisitedSpu;
	}
	public static Queue<SpuDTO> getAllSpuUrl(){
		return allSpuUrl;
	}
	//添加到访问过的url
	public static void addVisitedUrl(SpuDTO sku){
		visitedSpu.add(sku);
	}
	//移除访问过的
	public static void removeVisitUrl(SpuDTO sku){
		visitedSpu.remove(sku);
	}
	//未访问过的出队列
	public static SpuDTO unVisitEdSpuDeQueue(){
		return unVisitedSpu.deQueue();
	}
	public static SpuDTO allSpuSpuDeQueue(){
		return allSpuUrl.deQueue();
	}
	//未访问入队
	public static void addUnvisitedSpu(SpuDTO sku){
		unVisitedSpu.enQueue(sku);
	}
	public static void addAllSpuSpu(SpuDTO sku){
		allSpuUrl.enQueue(sku);
	}
	//获得已访问的url数目
	public static int getVisitedSpuNum(){
		return visitedSpu.size();
	}
	public static int getunVisitedUrlNum(){
		return unVisitedSpu.size();
	}
	//判断未访问url是否为空
	public static boolean unVisitedSpusEmpty(){
		return unVisitedSpu.isQueueEmpty();
	}
	public static boolean allSpuSpulsEmpty(){
		return allSpuUrl.isQueueEmpty();
	}
}
