
package com.shangpin.igo.ebay.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年7月7日
 */
public class ThreadTest {
	public void start() throws InterruptedException{
		ThreadTest t=new ThreadTest();
		ExecutorService exe=Executors.newFixedThreadPool(4);//相当于跑4遍
		for(int i = 0 ; i<10;i++){
			if(i==6)
				exe.execute(new LongThread(i));
			else
				exe.execute(new ShortThread(i));
		}
		
		exe.shutdown();
		//List<Runnable> runnables = exe.shutdownNow();  
		
		while (!exe.awaitTermination(1, TimeUnit.SECONDS)) {  
			System.out.println("线程池没有关闭");  
		}  
		System.out.println("线程池已经close");  
		
	}
	public static void main(String[] args) throws InterruptedException {
		ThreadTest r = new ThreadTest();
		r.start();
	}
	class LongThread implements Runnable{
		String name;
		/**
		 * @param i
		 */
		public LongThread(int i) {
			this.name=""+i;
		}

		@Override
		public void run() {
			try {
				TimeUnit.SECONDS.sleep(10);
				System.out.println(name+"long task");
			} catch (InterruptedException e) {
			}
		}
		
	}
	class ShortThread implements Runnable{
		String name;
		/**
		 * @param i
		 */
		public ShortThread(int i) {
			this.name=""+i;
		}

		@Override
		public void run() {
			System.out.println(name+"short....");
		}
		
	}

}
