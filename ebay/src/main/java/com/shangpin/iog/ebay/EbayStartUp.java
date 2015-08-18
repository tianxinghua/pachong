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
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.ebay.conf.EbayInit;
import com.shangpin.iog.ebay.page.PageGrabService;

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
	static PageGrabService pgGrabSrv;
    private static void loadSpringContext(){
        factory = new AnnotationConfigApplicationContext(AppContext.class);
        grabSrv=factory.getBean(V1GrabUpdateMain.class);
        pgGrabSrv=factory.getBean(PageGrabService.class);
    }
	public static void main(String[] args) {
		System.out.println("参数：u表示更新库存，其他表示拉取数据");
		if(args!=null){
			if(args.length>0 && "u".equals(args[0])){//更新库存
					logger.info("-----------开始更新库存---------");
					updateStock();
					logger.info("-----------更新库存完成---------");
					System.exit(0);
					return ;
			}
			loadSpringContext();
			//默认拉取
			logger.info("-----------开始抓取数据---------");
			grabProduct();
			//HttpUtil45.closePool();
			logger.info("-----------抓取数据完成---------");
			
		}
	}
	
	private static void grabProduct(){
		//grabSrv.grabSaveProduct(supplier);
		/*Date date1 = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String d = format.format(new Date());*/
		Map<String, String> storeBrand=EbayInit.getStoreBrand("store-brand-sports");
		Set<Entry<String, String>> kvs=storeBrand.entrySet();
		//ebay是说不要超过20个并发跑
		ExecutorService exe=Executors.newFixedThreadPool(18);//相当于跑10遍
		for (Entry<String, String> entry : kvs) {
			String st=entry.getValue();//storeName
			String[] storeNames=st.split("`");
			for (String storeName : storeNames) {
				exe.execute(new GrabEbayItemThread(storeName, entry.getKey()));				
			}
		}
		exe.shutdown();
		try {
			while (!exe.awaitTermination(10, TimeUnit.SECONDS)) {
				
			}
		} catch (InterruptedException e) {
			logger.error("关闭线程池异常-------",e);
		}
	}
	
	private static void updateStock(){
		String supplier=EbayInit.EBAY.substring(3);
		String end=DateTimeUtil.LongFmt(new Date());
		Calendar c=Calendar.getInstance();c.add(Calendar.MONTH, -3);
		String start=DateTimeUtil.LongFmt(c.getTime());
		V1UpdateStock vs = new V1UpdateStock();
		try {
			vs.setUseThread(true);
			vs.setSkuCount4Thread(20);
			vs.updateProductStock(supplier, start, end);
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
			//grabSrv.grabSaveProduct4Find(storeName, brand);
			//使用分页保存拉取
			pgGrabSrv.findStoreBrand(storeName,brand);
			logger.info("线程 {} 抓取保存完成",getName());
		}
		
	}
	
}
