package com.shangpin.iog.smets.crawer.customer;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.smets.queue.PicQueue;
import com.shangpin.iog.smets.queue.SkuQueue;

public class SkuCustomer extends Thread{
	private ProductFetchService productFetchService;
	public SkuCustomer(ProductFetchService productFetchService) {
		super();
		this.productFetchService = productFetchService;
	}


	@Override
	public void run() {
		while(true){
			if (SkuQueue.unVisitedSkusEmpty()) {
				try {
					Thread.currentThread().sleep(1000);
					System.out.println("=======================================sku队列数"+SkuQueue.getunVisitedUrlNum());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				try {
					System.out.println("消费sku======================");
					productFetchService.saveSKU(SkuQueue.unVisitEdSkuDeQueue());
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
			
		}
	
	}
	
	
	
	
}
