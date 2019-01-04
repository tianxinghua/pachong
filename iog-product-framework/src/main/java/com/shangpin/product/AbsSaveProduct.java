package com.shangpin.product;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.service.SkuPriceService;

import net.sf.json.JSONObject;

@SuppressWarnings("unchecked")
public  abstract class AbsSaveProduct {
	
	private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger.getLogger("info");
	@Autowired
	ProductFetchService productFetchService;
	@Autowired
	ProductSearchService productSearchService;
	@Autowired
	SkuPriceService skuPriceService;

	private static ResourceBundle hubBdl = null;
	private static ResourceBundle bdl = null;
	private static String hubProductUrl = null;
	public static SupplierProduct supp = null;
	private static String supplierName;
	private static String supplierId;
	private static String supplierNo;
	public static boolean flag;
	private static SimpleDateFormat sim;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		supplierNo = bdl.getString("supplierNo");
		supplierName = bdl.getString("supplierName");
		
		hubBdl = ResourceBundle.getBundle("hubProductConf");
		hubProductUrl = hubBdl.getString("hubProductUrl");
		flag = Boolean.valueOf(hubBdl.getString("flag"));
		
		sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		supp = new SupplierProduct();
		supp.setMessageType("json");
		supp.setSupplierName(supplierName);
		supp.setSupplierId(supplierId);
		supp.setSupplierNo(supplierNo);
		supp.setMessageId(UUIDGenerator.getUUID());
		supp.setMessageDate(sim.format(new Date()));
		
	}
	public void pushMessage(String json) {
		try {
			RpcConf c = new RpcConf();
			RestTemplate restTemplate = c.restTemplate();
			JSONObject supplierDto = restTemplate.postForEntity(hubProductUrl,supp, JSONObject.class).getBody();
			loggerInfo.info(supp.getSupplierName()+"=="+supplierDto.toString());
			System.out.println(supp.getSupplierName()+"=="+supplierDto.toString());
		} catch (Exception e) {
			loggerInfo.info(e.getMessage());
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 处理数据
	 * @param  flag 如果图片使用skuid存储，设置sku。否则设置spu
	 * @param supplierId
	 * @param day 90
	 * @param picpath 如果为"",表示不下载图片
*/	public abstract Map<String, Object> fetchProductAndSave() ;
	
	public  void handleData(final String flag,final String supplierId,final int day,final String picpath) {
		Map<String, Object> totalMap = fetchProductAndSave();
		if(totalMap==null||totalMap.size()<=0){
			return;
		}
//		ExecutorService executor = new ThreadPoolExecutor(10, Integer.MAX_VALUE, 100, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(100),new ThreadPoolExecutor.CallerRunsPolicy());
		ExecutorService executor=Executors.newFixedThreadPool(500);//相当于跑4遍
		final List<SkuDTO> skuList = (List<SkuDTO>)totalMap.get("sku");
		
		final List<SpuDTO> spuList =(List<SpuDTO>)totalMap.get("spu");

		final Map<String,List<String>> imageMap = (Map<String,List<String>>)totalMap.get("image");
		
		if(skuList!=null&&skuList.size()>0){
		 	Thread t1 = new Thread(new HandSkuDataThread(supplierId,day,skuList,productFetchService,productSearchService,skuPriceService));
	       	executor.execute(t1);
		}
      
		if(spuList!=null&&spuList.size()>0){
			Thread t2 = new Thread(new HandSpuDataThread(supplierId,spuList,productFetchService,productSearchService));
	    	executor.execute(t2);
		}

		if(imageMap!=null&&imageMap.size()>0){
			Thread t3 = new Thread(new HandImageDataThread(supplierId,flag,picpath,imageMap,productFetchService,productSearchService,skuPriceService));
	       	executor.execute(t3);
		}
		executor.shutdown();
		try {
			while (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
	}
	
}
