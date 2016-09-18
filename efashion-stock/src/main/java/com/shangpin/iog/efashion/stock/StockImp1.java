//package com.shangpin.iog.efashion.stock;
//
//import com.google.gson.Gson;
//import com.shangpin.framework.ServiceException;
//import com.shangpin.ice.ice.AbsUpdateProductStock;
//import com.shangpin.iog.app.AppContext;
//import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
//import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
//import com.shangpin.iog.dto.EventProductDTO;
//import com.shangpin.iog.efashion.dto.Item;
//import com.shangpin.iog.efashion.dto.Result;
//import com.shangpin.iog.efashion.dto.ReturnObject;
//import com.shangpin.iog.product.service.EventProductServiceImpl;
//import com.shangpin.iog.service.EventProductService;
//
//import net.sf.json.JSONObject;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import org.springframework.stereotype.Component;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
///**
// * Created by zhaogenchun on 2015/12/2.
// */
//@Component("efashion")
//public class StockImp1 extends AbsUpdateProductStock {
//
//    private static Logger logger = Logger.getLogger("info");
//    private static Logger loggerError = Logger.getLogger("error");
//    private static ApplicationContext factory;
//  private static void loadSpringContext()
//  {
//
//      factory = new AnnotationConfigApplicationContext(AppContext.class);
//  }
//    @Autowired
//	EventProductService eventProductService;
//    private static ResourceBundle bdl=null;
//    private static String supplierId;
//	private static String url;
//	public static int day;
//	public static int max;
//	public static int i=0;;
//    static {
//        if(null==bdl)
//         bdl=ResourceBundle.getBundle("conf");
//        supplierId = bdl.getString("supplierId");
//		url = bdl.getString("url");
//    }
//    @Override
//    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
//    	
//    	Map<String,String> stockMap = new HashMap<String,String>();
//    	try{
//            //定义三方
//    		
//            for (String skuno : skuNo) {
//            	i++;
//            	//skuno格式：skuId|proCode|colorCode|size
//            	String tempSku = null;
//            	String [] skuArray = skuno.split("\\|");
//            	tempSku = skuArray[0];
//            	//389977|BBUCKLE35MT6200222|REDBEIGE|80
//            	String json = HttpUtil45
//          				.get("http://api.gebnegozi.com/api/v2/skus/"+tempSku+"/stock.json?storeCode=DW3LT",
//          						new OutTimeConfig(1000 * 60, 1000 * 60, 1000 * 60),
//          						null);
//            	System.out.println("===第"+i+"个===");
//            	ReturnObject obj = new Gson().fromJson(json, ReturnObject.class);
//            	if(obj!=null){
//            		
//            		Item item = obj.getResults().getItem();
//            		if(item!=null){
//            			System.out.println("skuId:qty==="+tempSku+":"+item.getQuantity());
//            			if(tempSku.equals(item.getSku_id())){
//            				stockMap.put(skuno,item.getQuantity());
//            			}else{
//            				stockMap.put(skuno,"0");
//            			}
//            		}else{
//            			stockMap.put(skuno,"0");
//            		}
//            		
//            	}else{
//            		stockMap.put(skuno,"0");
//            	}
//            }
//            System.out.println("===拉取完成===");
//    	}catch(Exception e){
//    		loggerError.error(e);
//    	}
//    	
//        return stockMap;
//    }
//    public static void main(String[] args) {
//    	//加载spring
//        loadSpringContext();
//        //拉取数据
//        StockImp1 stockImp =(StockImp1)factory.getBean("efashion");
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        logger.info("efashiom更新库存开始");
//        System.out.println("efashiom更新库存开始");
//        try {
//			stockImp.setUseThread(true);
//			stockImp.setSkuCount4Thread(500);
//			stockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
//		} catch (Exception e) { 
//			loggerError.info(e);
//			e.printStackTrace();
//		}
//        logger.info("efashiom更新库存结束");
//        System.out.println("efashiom更新库存结束");
//        System.exit(0);
//    }
//}
