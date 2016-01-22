package com.shangpin.iog.newMenlook.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

@Component("menlook")
public class FechProduct {
	private static Logger logInfo  = Logger.getLogger("info");
	private static Logger logError = Logger.getLogger("error");
	private static Logger logMongoDB = Logger.getLogger("MongoDB");
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
	
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static String root = "";
	private static int day;
	private static int thread = 100;//规定100个产品为一个线程
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		root = bdl.getString("root");
		day = Integer.valueOf(bdl.getString("day"));		
	}
	
	@Autowired
	public ProductFetchService productFetchService;
	@Autowired
	ProductSearchService productSearchService;

	public void fetchProductAndSave(){		
		
		List<String> linkList = new ArrayList<String>();
		
		try {
			
			Date startDate,endDate= new Date();
			startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
			//获取原有的SKU 仅仅包含价格和库存
			Map<String,SkuDTO> skuDTOMap = new HashedMap();
			try {
				skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			
			//root
			String result = HttpUtil45.get(root, outTimeConf, null);
//			System.out.println("result==="+result);
			JSONArray array = JSONObject.fromObject(result).getJSONArray("categories");
			for(int i=0;i<array.size();i++){				
				String category = "";
				try{					
					category = array.getJSONObject(i).getString("id");
					logInfo.info("大类("+i+")========"+category);
					String link = "https://staging.menlook.com/dw/shop/v15_4/categories/"+category+"?client_id=c8f0a7ef-dee7-4b94-8e5c-4ee108e61e26&locale=fr&levels=2";
					String catIds = HttpUtil45.get(link, outTimeConf, null);
					JSONArray ids = JSONObject.fromObject(catIds).getJSONArray("categories");
					for(int j=0;j<ids.size();j++){
						String id = ids.getJSONObject(j).getString("id");
						logInfo.info("大类("+i+")========"+category+"=======小类("+j+")========"+id);
						//产品
						int start = 0;
						try{
							
							getSkuLink(id,start,linkList);		
							
						}catch(Exception ex){
							logError.error(ex);
							ex.printStackTrace();
						}
						
					}
					
				}catch(Exception ex){
					logError.error(ex);
					ex.printStackTrace();
					int start = 0;
					try{						
						getSkuLink(category,start,linkList);						
					}catch(Exception e){
						logError.error(e);
						ex.printStackTrace();
					}
				}				
							
			}
			
			//多线程插入
			if(linkList.size()>0){
				int poolCnt = linkList.size()/thread;
				ExecutorService exe=Executors.newFixedThreadPool(poolCnt/4+1);
				final List<Collection<String>> links=subCollection(linkList);
				for(int k=0;k<links.size();k++){
					exe.execute(new FechThread(links.get(k),skuDTOMap));
				}
				exe.shutdown();
			}		
			
			//更新网站不再给信息的老数据
			for(Iterator<Map.Entry<String,SkuDTO>> itor = skuDTOMap.entrySet().iterator();itor.hasNext(); ){
				 Map.Entry<String,SkuDTO> entry =  itor.next();
				if(!"0".equals(entry.getValue().getStock())){//更新不为0的数据 使其库存为0
					entry.getValue().setStock("0");
					try {
						productFetchService.updatePriceAndStock(entry.getValue());
					} catch (ServiceException e) {
						e.printStackTrace();
					}
				}
			}
			
		} catch (Exception e) {
			logError.error(e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 记录所有产品link
	 * @param category_id
	 * @param start
	 * @param linkList 所有产品link集合
	 */
	public void getSkuLink(String category_id,int start,List<String> linkList){
		
		String url = "https://staging.menlook.com/dw/shop/v15_4/product_search?client_id=e8c869c5-cf72-4192-9ec6-0fc72383e1f2&locale=fr&refine_1=cgid="+category_id+"&count=200&sort=default&start="+start;
		logInfo.info("url======"+url);
		try{
			
			String products = HttpUtil45.get(url, outTimeConf, null);
			JSONObject Jproducts = JSONObject.fromObject(products);
			Integer count = Jproducts.getInt("count");
			while(count>0){			
				JSONArray ar = Jproducts.getJSONArray("hits");
				for(int h=0;h<ar.size();h++){
					//skuLink
					String link = ar.getJSONObject(h).getString("link");
					linkList.add(link);
				}
				if(count>=200){
					count = 0;
					start =start+200;
					getSkuLink(category_id,start,linkList);
				}else{
					break;
				}
				
			}
			
		}catch(Exception ex){
			logError.error(ex);
		}
		
	}
	
	/**
	 * 将产品link集合分批
	 * @param skuList
	 * @return
	 */
	private List<Collection<String>> subCollection(Collection<String> skuList) {
		
		List<Collection<String>> list=new ArrayList<>();
		int count=0;int currentSet=0;
		for (Iterator<String> iterator = skuList.iterator(); iterator
				.hasNext();) {
			String skuNo = iterator.next();
			if(count==thread)
				count=0;
			if(count==0){
				Collection<String> e = new ArrayList<>();
				list.add(e);				
				currentSet++;
			}
			list.get(currentSet-1).add(skuNo);
			count++;
		}
		return list;
	}
	
	class FechThread extends Thread{
		private Collection<String> links = null;
		private Map<String,SkuDTO> skuDTOMap = null;
		public FechThread(Collection<String> links,Map<String,SkuDTO> skuDTOMap){
			this.links = links;
			this.skuDTOMap = skuDTOMap;
		}
		
		/**
		 * 保存sku、spu和图片
		 */
		@Override
		public void run() {
			
			for(String link:links){				
				try{
					logInfo.info("开始拉取====="+link);
					String result = HttpUtil45.get(link, outTimeConf, null);
					JSONObject item = JSONObject.fromObject(result);
					String itemId = item.getString("id");
//					System.out.println(itemId);					
					//库存
					String stockurl = "https://staging.menlook.com/dw/shop/v15_4/products/"+itemId+"/availability?client_id=c8f0a7ef-dee7-4b94-8e5c-4ee108e61e26&expand=images,prices,variations";
					String stockRe = HttpUtil45.get(stockurl, outTimeConf, null);					
					int stock = JSONObject.fromObject(stockRe).getJSONObject("inventory").getInt("stock_level");
					if(stock>0){
						//保存sku					
						SkuDTO sku = new SkuDTO();
						sku.setId(UUIDGenerator.getUUID());
						sku.setSupplierId(supplierId);
						sku.setSkuId(itemId);
			            sku.setSpuId(itemId);
			            sku.setColor(item.getString("c_octaveColor"));
			            if(item.containsKey("c_octaveProductReference")){
			            	sku.setProductCode(item.getString("c_octaveProductReference"));
			            }else{
			            	sku.setProductCode(itemId);
			            }		            
			            sku.setProductName(item.getString("name"));
			            if(item.containsKey("c_octaveSize")){
			            	sku.setProductSize(item.getString("c_octaveSize"));
			            }else if(item.containsKey("c_octaveModelSize")){
			            	sku.setProductSize(item.getString("c_octaveModelSize"));
			            }		            
			            sku.setStock(String.valueOf(stock));
			            //价格
			            String price = "http://staging.menlook.com/dw/shop/v15_9/products/"+sku.getSkuId()+"/prices?locale=fr&client_id=c8f0a7ef-dee7-4b94-8e5c-4ee108e61e26";
			            String pri = HttpUtil45.get(price, outTimeConf, null);
			            String p = JSONObject.fromObject(pri).get("price").toString();
			            sku.setMarketPrice(p);
			            sku.setSaleCurrency("EUR");
			            sku.setProductDescription(item.getString("long_description"));
			            try {
			            	if(skuDTOMap.containsKey(sku.getSkuId())){
								skuDTOMap.remove(sku.getSkuId());
							}
			                productFetchService.saveSKU(sku);
			                
			            } catch (ServiceException e) {
			                try {
			                    if (e.getMessage().equals("数据插入失败键重复")) {
			                        //更新价格和库存
			                        productFetchService.updatePriceAndStock(sku);
			                    } else {
			                        e.printStackTrace();
			                    }
			                } catch (ServiceException e1) {
			                	logError.error(e1.getMessage());
			                    e1.printStackTrace();
			                }
			            }
			            
			            //入库spu
			            SpuDTO spu = new SpuDTO();
			            spu.setId(UUIDGenerator.getUUID());
			            spu.setSpuId(item.getString("id"));
			            spu.setSupplierId(supplierId);
			            spu.setBrandName(item.getString("brand"));
			            spu.setCategoryGender("male");
			            spu.setCategoryName(item.getString("primary_category_id"));
			            if(item.containsKey("c_octaveMaterial")){
			            	spu.setMaterial(item.getString("c_octaveMaterial"));
			            }	
			            if(item.containsKey("c_merchantId")){
			            	spu.setProductOrigin(item.getString("c_merchantId"));
			            }			            
			            try {
			                productFetchService.saveSPU(spu);
			            } catch (ServiceException e) {
			            	logError.error(e.getMessage());
			            	try{
			            		productFetchService.updateMaterial(spu);
			            	}catch(ServiceException ex){
			            		logError.error(ex.getMessage());
			            		ex.printStackTrace();
			            	}
			            	
			                e.printStackTrace();
			            }	            
			            
			            //图片
			            List<String> list = new ArrayList<>();
			            String pic_uri = "http://staging.menlook.com/dw/shop/v15_9/products/"+sku.getSkuId()+"/images?locale=fr&client_id=c8f0a7ef-dee7-4b94-8e5c-4ee108e61e26";
			            String pics = HttpUtil45.get(pic_uri, outTimeConf, null);
		            	JSONArray picAr = JSONObject.fromObject(pics).getJSONArray("image_groups");
			            for(int i=0;i<picAr.size();i++){
			            	JSONArray aar = picAr.getJSONObject(i).getJSONArray("images");
			            	for(int j=0;j<aar.size();j++){
			            		list.add(aar.getJSONObject(j).getString("link"));
			            	}
			            }
			            productFetchService.savePicture(supplierId, null, sku.getSkuId(), list);
				            
					}
										
				}catch(Exception ex){
					logError.error(ex);
					ex.printStackTrace(); 					
				} 
				logInfo.info("拉取结束====="+link);
			}			
			
		}
	}
	
}
