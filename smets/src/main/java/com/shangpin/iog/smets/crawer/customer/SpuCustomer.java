package com.shangpin.iog.smets.crawer.customer;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.smets.queue.SkuQueue;
import com.shangpin.iog.smets.queue.SpuQueue;

public class SpuCustomer extends Thread{
	private ProductFetchService productFetchService;
	public SpuCustomer(ProductFetchService productFetchService) {
		super();
		this.productFetchService = productFetchService;
	}


	@Override
	public void run() {
		while(true){
			if (SpuQueue.unVisitedSpusEmpty()) {
				try {
					Thread.currentThread().sleep(1000);
					System.out.println("=======================================spu队列数"+SpuQueue.getunVisitedUrlNum());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				try {
					System.out.println("消费spu***************************");
					productFetchService.saveSPU(SpuQueue.unVisitEdSpuDeQueue());
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
			
		}
	
	}
	
	
	
	
}
