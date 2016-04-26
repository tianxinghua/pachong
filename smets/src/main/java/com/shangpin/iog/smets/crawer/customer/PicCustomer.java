package com.shangpin.iog.smets.crawer.customer;

import java.util.Arrays;

import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.smets.queue.PicQueue;

public class PicCustomer  extends Thread{
	private ProductFetchService productFetchService;
	public PicCustomer(ProductFetchService productFetchService) {
		super();
		this.productFetchService = productFetchService;
	}


	@Override
	public void run() {
		while(true){
			if (PicQueue.unVisitedUrlsEmpty()) {
				try {
					Thread.currentThread().sleep(1000);
					System.out.println("=======================================pic队列数"+PicQueue.getunVisitedUrlNum());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				System.out.println("消费图片+++++++++++++++++++++++");
				String string = PicQueue.unVisitEdUrlDeQueue();
				productFetchService.savePicture("201604231848", null, string.split("\\^")[0], Arrays.asList(string.split("\\^")[1].split(";")));
			}
			
		}
	
	}
	
	
	
	
}
