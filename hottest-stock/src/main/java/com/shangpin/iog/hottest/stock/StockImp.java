package com.shangpin.iog.hottest.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.hottest.stock.schedule.AppContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lubaijiang on 2015/9/14.
 */
@Component("hotteststock")
public class StockImp  extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static LoggerUtil error = LoggerUtil.getLogger("error");
    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String url;
    private static ApplicationContext factory;
    private static OutTimeConfig outTimeConf = new OutTimeConfig(1000 * 60*5,
			1000 * 60 * 5, 1000 * 60 * 5);
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("url");
	}
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	
    	//获取库存元数据
    	Map<String,String> skuMap = new HashMap<String,String>();
        Map<String,String> returnMap = new HashMap<String,String>();
        
        Map<String,String> headMap = new HashMap<>();
		headMap.put("Authorization", "RIX5NkHDIM25yUFZmDlVSdWEE7V3aSYv");
		int page = 1;
		int maxPage = 2;	
		boolean bool = true;
		while(page <=maxPage){
			try{
				
				String uri = url + page;
				logger.info("uri===="+uri); 
				String result = HttpUtil45.get(uri, outTimeConf, null, headMap, "", "");			
//				logger.info(result);
				JSONObject jsonObj = JSONObject.fromObject(result);
				JSONObject products = jsonObj.getJSONObject("products");
				for(int k=0;k<100;k++){
					if (products.containsKey(String.valueOf(k))) {
						JSONObject product = products.getJSONObject(String.valueOf(k));// spu
						try {
							skuMap.put(product.getString("upc"),product.getString("qty"));
						} catch (Exception ex) {
							ex.printStackTrace();
							error.error(ex);
						}

					} else {
						break;
					}
				}
				
				if(bool){
					maxPage = products.getInt("pages");
					logger.info("maxPage====="+maxPage);
					bool = false;
				}
				
			}catch(Exception e){
				error.error(e);
			}			
			
			logger.info("page====="+page);			
			page = page+1;
		}
//		System.out.println(skuMap.toString()); 
		logger.info("从供货商接口拉取的库存信息skuMap.size===="+skuMap.size());
		logger.info("拉取的 skuid===="+skuMap.toString()); 
		logger.info("shangpin skuId==="+skuNo.toString()); 
		for (String skuno : skuNo) {			
            if(skuMap.containsKey(skuno)){
            	returnMap.put(skuno, skuMap.get(skuno));
            } 
            
//            else{
//            	returnMap.put(skuno, "0");
//            }
        }
		logger.info("返回的returnMap.size======="+returnMap.size()); 
        return returnMap;
    }

    public static void main(String[] args){
    	//加载spring
        loadSpringContext();
//        StockImp stockImp =(StockImp)factory.getBean("hotteststock");
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        logger.info("hottest更新数据库开始");
//        try {
//			stockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
//		} catch (Exception e) {
//			logger.info("hottest更新库存数据库出错"+e.toString());
//		}
//        logger.info("hottest更新数据库结束");
//        System.exit(0);
    	
//    	StockImp stock = new StockImp();
//    	try {
//			stock.grabStock(null);
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//    	Map<String,String> skuMap = new HashMap<String,String>();
//    	String result = "{\"products\":{\"pages\":57,\"0\":{\"upc\":\"887960937344\",\"qty\":0,\"price\":\"\"},\"1\":{\"upc\":\"887960937337\",\"qty\":0,\"price\":\"\"},\"2\":{\"upc\":\"887960937351\",\"qty\":0,\"price\":\"\"},\"3\":{\"upc\":\"887960937368\",\"qty\":0,\"price\":\"\"},\"4\":{\"upc\":\"889347531169\",\"qty\":0,\"price\":\"\"},\"5\":{\"upc\":\"889347531138\",\"qty\":0,\"price\":\"\"},\"6\":{\"upc\":\"889347531145\",\"qty\":0,\"price\":\"\"},\"7\":{\"upc\":\"889347531152\",\"qty\":0,\"price\":\"\"},\"8\":{\"upc\":\"884726415269\",\"qty\":0,\"price\":\"\"},\"9\":{\"upc\":\"884726415276\",\"qty\":0,\"price\":\"\"},\"10\":{\"upc\":\"884726415283\",\"qty\":0,\"price\":\"\"},\"11\":{\"upc\":\"884726415290\",\"qty\":0,\"price\":\"\"},\"12\":{\"upc\":\"884726415306\",\"qty\":0,\"price\":\"\"},\"13\":{\"upc\":\"884726415313\",\"qty\":0,\"price\":\"\"},\"14\":{\"upc\":\"884726415443\",\"qty\":0,\"price\":\"\"},\"15\":{\"upc\":\"886060048103\",\"qty\":0,\"price\":\"70.50\"},\"16\":{\"upc\":\"889347443387\",\"qty\":0,\"price\":\"\"},\"17\":{\"upc\":\"889347443356\",\"qty\":0,\"price\":\"\"},\"18\":{\"upc\":\"889347443349\",\"qty\":0,\"price\":\"\"},\"19\":{\"upc\":\"889347443332\",\"qty\":0,\"price\":\"\"},\"20\":{\"upc\":\"889347443363\",\"qty\":0,\"price\":\"\"},\"21\":{\"upc\":\"889347443370\",\"qty\":0,\"price\":\"\"},\"22\":{\"upc\":\"887960937313\",\"qty\":0,\"price\":\"\"},\"23\":{\"upc\":\"887960937283\",\"qty\":0,\"price\":\"\"},\"24\":{\"upc\":\"887960937290\",\"qty\":0,\"price\":\"\"},\"25\":{\"upc\":\"887960937306\",\"qty\":0,\"price\":\"\"},\"26\":{\"upc\":\"640135835964\",\"qty\":0,\"price\":\"\"},\"27\":{\"upc\":\"640135836961\",\"qty\":0,\"price\":\"\"},\"28\":{\"upc\":\"640135837203\",\"qty\":0,\"price\":\"\"},\"29\":{\"upc\":\"640135840418\",\"qty\":0,\"price\":\"\"},\"30\":{\"upc\":\"888410063866\",\"qty\":0,\"price\":\"\"},\"31\":{\"upc\":\"841158002467\",\"qty\":0,\"price\":\"\"},\"32\":{\"upc\":\"841158002436\",\"qty\":0,\"price\":\"\"},\"33\":{\"upc\":\"883503153295\",\"qty\":0,\"price\":\"24.50\"},\"34\":{\"upc\":\"369258147\",\"qty\":17,\"price\":\"\"},\"35\":{\"upc\":\"886857070256\",\"qty\":1,\"price\":\"\"},\"36\":{\"upc\":\"886857070263\",\"qty\":1,\"price\":\"\"},\"37\":{\"upc\":\"886857070270\",\"qty\":1,\"price\":\"\"},\"38\":{\"upc\":\"886857070287\",\"qty\":2,\"price\":\"\"},\"39\":{\"upc\":\"886857070294\",\"qty\":2,\"price\":\"\"},\"40\":{\"upc\":\"886857070300\",\"qty\":2,\"price\":\"\"},\"41\":{\"upc\":\"886857070317\",\"qty\":1,\"price\":\"\"},\"42\":{\"upc\":\"886857070324\",\"qty\":1,\"price\":\"\"},\"43\":{\"upc\":\"886857070331\",\"qty\":1,\"price\":\"\"},\"44\":{\"upc\":\"883503153301\",\"qty\":0,\"price\":\"\"},\"45\":{\"upc\":\"715924385682\",\"qty\":1,\"price\":\"\"},\"46\":{\"upc\":\"715924385699\",\"qty\":2,\"price\":\"\"},\"47\":{\"upc\":\"715924385705\",\"qty\":2,\"price\":\"\"},\"48\":{\"upc\":\"715924385712\",\"qty\":2,\"price\":\"\"},\"49\":{\"upc\":\"715924385729\",\"qty\":1,\"price\":\"\"},\"50\":{\"upc\":\"715924385736\",\"qty\":1,\"price\":\"\"},\"51\":{\"upc\":\"715924385743\",\"qty\":1,\"price\":\"\"},\"52\":{\"upc\":\"715924588502\",\"qty\":0,\"price\":\"\"},\"53\":{\"upc\":\"715924588519\",\"qty\":0,\"price\":\"\"},\"54\":{\"upc\":\"715924588526\",\"qty\":0,\"price\":\"\"},\"55\":{\"upc\":\"715924588533\",\"qty\":0,\"price\":\"\"},\"56\":{\"upc\":\"715924588540\",\"qty\":0,\"price\":\"\"},\"57\":{\"upc\":\"715924588557\",\"qty\":0,\"price\":\"\"},\"58\":{\"upc\":\"715924588564\",\"qty\":0,\"price\":\"\"},\"59\":{\"upc\":\"715924588045\",\"qty\":0,\"price\":\"\"},\"60\":{\"upc\":\"715924588052\",\"qty\":0,\"price\":\"\"},\"61\":{\"upc\":\"715924588069\",\"qty\":0,\"price\":\"\"},\"62\":{\"upc\":\"715924588076\",\"qty\":0,\"price\":\"\"},\"63\":{\"upc\":\"715924588083\",\"qty\":0,\"price\":\"\"},\"64\":{\"upc\":\"715924588090\",\"qty\":0,\"price\":\"\"},\"65\":{\"upc\":\"715924588106\",\"qty\":0,\"price\":\"\"},\"66\":{\"upc\":\"715924588113\",\"qty\":0,\"price\":\"\"},\"67\":{\"upc\":\"715924588120\",\"qty\":0,\"price\":\"\"},\"68\":{\"upc\":\"886916324542\",\"qty\":0,\"price\":\"\"},\"69\":{\"upc\":\"886916324535\",\"qty\":0,\"price\":\"\"},\"70\":{\"upc\":\"886916324528\",\"qty\":0,\"price\":\"\"},\"71\":{\"upc\":\"886916324573\",\"qty\":0,\"price\":\"\"},\"72\":{\"upc\":\"886916851840\",\"qty\":0,\"price\":\"\"},\"73\":{\"upc\":\"886916851833\",\"qty\":0,\"price\":\"\"},\"74\":{\"upc\":\"886916848017\",\"qty\":0,\"price\":\"\"},\"75\":{\"upc\":\"886916851864\",\"qty\":0,\"price\":\"\"},\"76\":{\"upc\":\"886550609784\",\"qty\":1,\"price\":\"\"},\"77\":{\"upc\":\"886550609791\",\"qty\":2,\"price\":\"\"}}}";
//    	JSONObject jsonObj = JSONObject.fromObject(result);
//		JSONObject products = jsonObj.getJSONObject("products");
//		for(int k=0;k<100;k++){
//			if (products.containsKey(String.valueOf(k))) {
//				JSONObject product = products.getJSONObject(String.valueOf(k));// spu
//				try {
//					skuMap.put(product.getString("upc"),product.getString("qty"));
//					System.out.println(skuMap.toString());
//				} catch (Exception ex) {
//					ex.printStackTrace();
//					error.error(ex);
//				}
//
//			} else {
//				break;
//			}
//		}
    }
}
