package com.shangpin.iog.efashion.stock;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.EventProductDTO;
import com.shangpin.iog.efashion.dto.Item;
import com.shangpin.iog.efashion.dto.Result;
import com.shangpin.iog.efashion.dto.ReturnObject;
import com.shangpin.iog.product.service.EventProductServiceImpl;
import com.shangpin.iog.service.EventProductService;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhaogenchun on 2015/12/2.
 */
@Component("efashion")
public class StockImp extends AbsUpdateProductStock {

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    
//    private static ApplicationContext factory;
//    private static void loadSpringContext()
//    {
//
//        factory = new AnnotationConfigApplicationContext(AppContext.class);
//    }

    @Autowired
	EventProductService eventProductService;
    private static ResourceBundle bdl=null;
    private static String supplierId;
	private static String url;
	public static int day;
	public static int max;
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
		max = Integer.valueOf(bdl.getString("max"));
		url = bdl.getString("url");
    }
    static Map<String,String> map = new HashMap<String,String>();
    static int i=0;
    public static void getProductList(int index){
    	try{
    		String json = HttpUtil45
    				.get(url+"&limit="+max+"&offset="+index,
    						new OutTimeConfig(1000 * 60, 1000 * 60, 1000 * 60),
    						null);
    		ReturnObject obj = new Gson().fromJson(json, ReturnObject.class);
    		// 第一步：获取活动信息
    		if(obj!=null){
    			Result result = obj.getResults();
    			List<Item> item = result.getItems();
    			if(!item.isEmpty()){
    				for(Item ite:item){
    					map.put(ite.getSku_id(), ite.getQuantity());
    				}
    				i++;
    				System.out.println("---------第"+i+"页-------------");
    				System.out.println("商品数量："+item.size());
    				getProductList(max*i+1);
    			}
    		}
    	}catch(Exception e){
    		loggerError.error(e);
    	}
    	
	}
    
    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	
    	Map<String,String> stockMap = new HashMap<String,String>();
    	try{
    		getProductList(1);
        	System.out.println("总的商品数量："+map.size());
        	logger.info("总的商品数量："+map.size());
        	System.out.println("拉取完成");
            //定义三方
            for (String skuno : skuNo) {
            	//skuno格式：skuId|proCode|colorCode|size
            	String tempSku = null;
            	String [] skuArray = skuno.split("\\|");
            	tempSku = skuArray[0];
                if(map.containsKey(tempSku)){
                	int i =0;
                	if(map.get(tempSku)!=null){
                		i = Integer.parseInt(map.get(tempSku));
                	}
                	if(i<0){
            			logger.info("sku库存小于0："+tempSku+":"+map.get(tempSku));
            			System.out.println("sku库存小于0："+tempSku+":"+map.get(tempSku));
            			stockMap.put(skuno,"0");
            		}else{
            			stockMap.put(skuno,map.get(tempSku));
            		}
                }else{
                	stockMap.put(skuno,"0");
                }
            }
    	}catch(Exception e){
    		loggerError.error(e);
    	}
    	
        return stockMap;
    }
    
//    public static void main(String[] args) {
//    	//加载spring
//        loadSpringContext();
//        //拉取数据
//        StockImp stockImp =(StockImp)factory.getBean("efashion");
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        logger.info("efashiom更新库存开始");
//        System.out.println("efashiom更新库存开始");
//        try {
//			stockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
//		} catch (Exception e) { 
//			loggerError.info(e);
//			e.printStackTrace();
//		}
//        logger.info("efashiom更新库存结束");
//        System.out.println("efashiom更新库存结束");
//        System.exit(0);
//    }
}
