package com.shangpin.iog.magway.stock;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.magway.dto.GoodsStock;
import com.shangpin.iog.magway.dto.Token;
import com.shangpin.iog.magway.schedule.AppContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lubaijiang on 2016/9/14.
 */
@Component("magwaystock")
public class StockImp  extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static LoggerUtil logError = LoggerUtil.getLogger("error");
    private static OutTimeConfig outTimeConf = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
    private static ResourceBundle bdl=null;
    private static String supplierId = null;
    private static String url_token =  null;
    private static String url_goodsStock = null;
    private static String Authorization = null;
	private static String grant_type = null;
    
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        url_token = bdl.getString("url_token");
        url_goodsStock = bdl.getString("url_goodsStock");
        Authorization = bdl.getString("Authorization");
		grant_type = bdl.getString("grant_type");
    }
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	
    	//获取库存元数据
    	Map<String,String> stockMap = new HashMap<String,String>();
        Map<String,String> skustock = new HashMap<String,String>();
        
        Token token= getToken();
		String access_token = token.getAccess_token();
		if(StringUtils.isNotBlank(access_token)){
			
			Map<String,String> headMap = new HashMap<String,String>();
			headMap.put("Authorization", token.getToken_type()+" "+access_token);
			String goodsStockStr = HttpUtil45.get(url_goodsStock, outTimeConf, null, headMap, "", "");
			logger.info("goodsStockStr==="+goodsStockStr);
			GoodsStock goodsStock = new Gson().fromJson(goodsStockStr, GoodsStock.class);
			for(com.shangpin.iog.magway.dto.Data data:goodsStock.getData()){
				try{
					
					for(com.shangpin.iog.magway.dto.Stocks stocks:data.getStocks()){
						try{
							
							stockMap.put(data.getID()+"-"+stocks.getSize(), stocks.getQty());
							
						}catch(Exception e){
							e.printStackTrace();
						}						
					}
					
				}catch(Exception e){
					e.printStackTrace();
					logError.error(e);
					logError.error("ID========"+data.getID());
				}
				
			}
			
		}else{
			logError.error("更新库存时获取token失败，无法更新库存!!!!!!!!!!!!!!!!!!!!"); 
		}
        
        for (String skuno : skuNo) {
            if(stockMap.containsKey(skuno)){
                skustock.put(skuno, stockMap.get(skuno));
            } else{
                skustock.put(skuno, "0");
            }
        }
        return skustock;
    }
    
    public Token getToken(){
		Token token = null;
		try{
			
			Map<String,String> param = new HashMap<String,String>();
			param.put("grant_type", grant_type);
			Map<String,String> headerMap = new HashMap<String,String>();
			headerMap.put("Authorization", Authorization);
			String result = HttpUtil45.post(url_token, param, headerMap, outTimeConf);
			logger.info("Token===="+result);
			System.out.println(result);
			Gson gson = new Gson();
			token = gson.fromJson(result, Token.class);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return token;
		
	}

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();
//        StockImp stockImp =(StockImp)factory.getBean("magwaystock");
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        logger.info("magway更新数据库开始");
//        try {
//			stockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
//		} catch (Exception e) {
//			logger.info("magway更新库存数据库出错"+e.toString());
//		}
//        logger.info("magway更新数据库结束");
//        System.exit(0);
    	
    }
}
