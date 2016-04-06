package com.shangpin.iog.inviqa.stock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuthService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.inviqa.dto.API;
import com.shangpin.iog.inviqa.dto.Stock;
import com.shangpin.iog.service.EventProductService;

/**
 * Created by Administrator on 2015/7/8.
 */
@Component("inviqa")
public class StockImp extends AbsUpdateProductStock {
    
	private static String MAGENTO_API_KEY = null;
    private static String MAGENTO_API_SECRET = null;
    private static String MAGENTO_REST_API_URL = null;
    private static Logger logger = Logger.getLogger("info");
    private static ApplicationContext factory;
    private static String token = null;
    private static String secret = null;
	private int sum = 0;
	private int excSum = 0;
	private int j = 0,tempPage=0;
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    @Autowired
	EventProductService eventProductService;
    private static ResourceBundle bdl=null;
    private static String supplierId;
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        
        supplierId = bdl.getString("supplierId");
        MAGENTO_API_KEY = bdl.getString("MAGENTO_API_KEY");
		MAGENTO_API_SECRET = bdl.getString("MAGENTO_API_SECRET");
		MAGENTO_REST_API_URL = bdl.getString("MAGENTO_REST_API_URL");
        token = bdl.getString("token");
		secret = bdl.getString("secret");
    }
    private int i = 1;
    private Map<String,String> map = new HashMap<String,String>();
    public  void getStockMap(List<Stock> allStockList) {
    	
    	for(Stock stock : allStockList){
    		map.put(stock.getSkuId(),stock.getStock());
    	}
	}
    public  void getStockList() {
		String stock = null;
		try{
			OAuthService service = new ServiceBuilder().provider(API.class)
					.apiKey(MAGENTO_API_KEY).apiSecret(MAGENTO_API_SECRET).build();
			Token accessToken = new Token(token,secret);
			getProductStock(service,accessToken,1);
		}catch(Exception e){
			stock="0";
		}
	}
    public   void getProductStock(OAuthService service,Token accessToken,int page){
		//https://glamorous-uat.phplab.co.uk/api/rest
		try{
			OAuthRequest request = new OAuthRequest(Verb.GET,
					MAGENTO_REST_API_URL+ "stock?limit=100&page="+page,
					service);
			service.signRequest(accessToken, request);
			Response response = request.send();
			String json = response.getBody();
			if(!json.isEmpty()){
				List<Stock> retList = new Gson().fromJson(json,  
		                new TypeToken<List<Stock>>() {  
		                }.getType());  
				logger.info("拉取的数量："+retList.size());
				System.out.println("拉取的数量："+retList.size());
				j=0;
				if(retList.size()==100){
					getStockMap(retList);
					page++;
					sum += 100;
					System.out.println("已拉取的数量："+sum);
					logger.info("已拉取的数量："+sum);
					getProductStock(service,accessToken,page);
				}else{
					getStockMap(retList);
				}
			}
		}catch(Exception ex){
			excSum+=1;
			tempPage = page;
			if(tempPage==page){
				j++;
			}
			logger.info("第"+excSum+"次发生异常,异常原因："+ex.getMessage());
			System.out.println("第"+excSum+"次发生异常,异常原因："+ex.getMessage());
			getProductStock(service,accessToken,page);
			if(j==3){
				ex.printStackTrace();
			}
		}
		
	}
    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        //get tony return date
        //定义三方
    	Map<String,String> stockMap = new HashMap<>();
    	getStockList();
    	System.out.println("11=========================");
    	System.out.println(map.size());
        for (String skuno : skuNo) {
        	
        	if(map.get(skuno)!=null){
        		String value = map.get(skuno);
        		int stock = Integer.parseInt(value);
        		if(stock<0){
        			stock=0;
        		}
        		stockMap.put(skuno,String.valueOf(stock));
        	}else{
        		stockMap.put(skuno,"0");
        	}
        }
        return stockMap;
    }

    public static void main(String[] args) {
    
    	//加载spring
        loadSpringContext();
        //拉取数据
        StockImp stockImp =(StockImp)factory.getBean("inviqa");
        stockImp.setUseThread(true);stockImp.setSkuCount4Thread(500);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("inviqa更新库存开始");
        System.out.println("inviqa更新库存开始");
        try {
			stockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		}
        logger.info("inviqa更新库存结束");
        System.out.println("inviqa更新库存结束");
        System.exit(0);
    }
}
