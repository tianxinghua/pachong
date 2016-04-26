package com.shangpin.iog.smets.util;

import java.util.LinkedList;

/**
 * 保存要访问的url
 * @param <T>
 */
public class Queue<T> {
	//链表队列
	private LinkedList<T> queue = new LinkedList<T>();
	//入队
	public void enQueue(T url){
		queue.add(url);
	}
	//出队
	public T deQueue(){
		return queue.removeFirst();
	}
	//判断队列是否为空
	public boolean isQueueEmpty(){
		return queue.isEmpty();
	}
	public int size() {
		return queue.size();
	}
}
