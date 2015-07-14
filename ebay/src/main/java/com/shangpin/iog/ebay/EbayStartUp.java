/**
 * 
 */
package com.shangpin.iog.ebay;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.ebay.conf.EbayConf;

/**
 * ebay抓取，更新启动类，用于选择是更新还是抓取
 * @description 
 * @author 陈小峰
 * <br/>2015年7月3日
 */
public class EbayStartUp {
	static Logger logger = LoggerFactory.getLogger(EbayStartUp.class);
	private static ApplicationContext factory;
	static V1GrabUpdateMain grabSrv=null;
    private static void loadSpringContext(){
        factory = new AnnotationConfigApplicationContext(AppContext.class);
        grabSrv=factory.getBean(V1GrabUpdateMain.class);
    }
	public static void main(String[] args) {
		if(args!=null &&args.length>0){
			loadSpringContext();
			if("u".equals(args[0])){//更新库存
				updateStock();
			}else{				
				grabProduct();
			}
		}else{
			System.out.println("参数：u表示更新库存，其他表示拉取数据");
		}
	}
	
	private static void grabProduct(){
		//grabSrv.grabSaveProduct(supplier);
		Map<String, String> storeBrand=EbayConf.getStoreBrand();
		Set<Entry<String, String>> kvs=storeBrand.entrySet();
		ExecutorService exe=Executors.newFixedThreadPool(10);//相当于跑10遍
		for (Entry<String, String> entry : kvs) {
			String st=entry.getValue();//storeName
			String[] storeNames=st.split("`");
			for (String storeName : storeNames) {
				exe.execute(new GrabEbayItemThread(storeName, entry.getKey()));				
			}
		}
	}
	
	private static void updateStock(){
		String supplier="";
		String start=DateTimeUtil.LongFmt(new Date());
		Calendar c=Calendar.getInstance();c.add(Calendar.MONTH, -3);
		String end=DateTimeUtil.LongFmt(c.getTime());
		try {
			grabSrv.updateProductStock(supplier, start, end);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static class GrabEbayItemThread extends Thread{
		private String storeName;
		private String brand;
		/**
		 * @param storeName
		 * @param brand
		 */
		private GrabEbayItemThread(String storeName, String brand) {
			super();
			this.storeName = storeName;
			this.brand = brand;
			this.setName(brand+"@"+storeName);
		}

		@Override
		public void run() {		
			logger.info("线程 {} 开始抓取",getName());
			grabSrv.grabSaveProduct4Find(storeName, brand);
			logger.info("线程 {} 抓取保存完成",getName());
		}
		
	}
}
