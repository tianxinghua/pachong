package com.shangpin.iog.productweb.test.a;

import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test extends Observable {
	
	public void ttttt(){
		ExecutorService threadPool = Executors.newCachedThreadPool();
		threadPool.submit(new Wooo(this));
		System.out.println(threadPool.toString());
		
			try {
				Thread.currentThread().sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		this.setChanged();
		this.notifyObservers();
		
		
		while (true) {
			System.out.println(threadPool.toString());
			try {
				Thread.currentThread().sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
