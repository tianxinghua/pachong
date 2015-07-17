package com.shangpin.igo.ebay.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年7月16日
 */
public class CallablePoolTest {

	public void poolTest() throws InterruptedException, ExecutionException{
		ExecutorService exe = Executors.newFixedThreadPool(5);//每页一个线程去跑
		List<String> strs=new ArrayList<>();
		List<Future<String>> fu = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			int intv=new Double(Math.random()*2000).intValue();
			Future<String> rs=exe.submit(new CallThread(intv,i));
			fu.add(rs);
		}
		System.out.println("main end------");
		exe.shutdown();
		while(!exe.awaitTermination(3, TimeUnit.SECONDS)){
			System.out.println("================not finished------------");
		}
		for (Future<String> future : fu) {
			strs.add(future.get());
		}
		System.out.println("=====main end");
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		CallablePoolTest tt=new CallablePoolTest();
		tt.poolTest();
	}
	static class CallThread implements Callable<String>{
		int cnt=0;
		int nameValue=0;
		/**
		 * @param i
		 */
		public CallThread(int i,int name) {
			this.cnt=i;
			this.nameValue=name;
		}

		@Override
		public String call() throws Exception {
			System.out.println(nameValue+"等待"+cnt);
			Thread.sleep(cnt*10);
			System.out.println(nameValue+"线程执行完毕");
			return "t"+cnt;
		}
		
	}
}
