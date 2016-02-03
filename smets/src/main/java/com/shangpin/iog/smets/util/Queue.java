package com.shangpin.iog.smets.util;

import java.util.LinkedList;
/**
 * 保存要访问的url
 */
public class Queue {
	//链表队列
	private LinkedList<String> queue = new LinkedList<String>();
	//入队
	public void enQueue(String url){
		queue.add(url);
	}
	//出队
	public String deQueue(){
		return queue.removeFirst();
	}
	//判断队列是否为空
	public boolean isQueueEmpty(){
		return queue.isEmpty();
	}
	//判断是否包含url
	public boolean contians(String url){
		return queue.contains(url);
	}
	
}
