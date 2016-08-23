package com.shangpin.iog.palomaBarcelo.stock;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.EventProductDTO;
import com.shangpin.iog.palomaBarcelo.dto.Item;
import com.shangpin.iog.palomaBarcelo.dto.Result;
import com.shangpin.iog.palomaBarcelo.dto.ReturnObject;
import com.shangpin.iog.product.service.EventProductServiceImpl;
import com.shangpin.iog.service.EventProductService;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
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
    
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    @Autowired
	EventProductService eventProductService;
    private static ResourceBundle bdl=null;
    private static String supplierId;
	private static String uri;
	public static int day;
	public static int max;
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        uri = bdl.getString("uri");
    }
  public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
	  	
	  	Map<String,String> stockMap = new HashMap<String,String>();
	  	try{
	          //定义三方
	          for (String skuno : skuNo) {
	        		String tempSku = null;
	            	String [] skuArray = skuno.split("-");
	            	tempSku = skuArray[0];
	            	String size = URLEncoder.encode(skuArray[1],"UTF-8");
	            	size = size.replace("+","%20");
	        	  String json = HttpUtil45
	      				.get(uri+"/api/v3.0/sku/"+tempSku+"/id/"+size+"/size/stock?storeCode=8CJXT",
	      						new OutTimeConfig(1000 * 60, 1000 * 60, 1000 * 60),
	      						null);
	      		ReturnObject obj = new Gson().fromJson(json, ReturnObject.class);
	      		if(obj!=null){
	      			Result re = obj.getResults();
	      			if(re!=null&&"200".equals(re.getReqCode())&&!"0".equals(re.getCount())){
	      				int i = Integer.parseInt(re.getItems().get(0).getQuantity());
	      				System.out.println(skuno+"======"+i);
	      				if(i<0){
	              			loggerError.info("sku库存小于0："+tempSku+":"+i);
	              			System.out.println("sku库存小于0："+tempSku+":"+i);
	              			stockMap.put(skuno,"0");
	              		}else{
	              			stockMap.put(skuno,i+"");
	              		}
	      			}else{
	      				stockMap.put(skuno,"0");
	          		}
	      		}else{
	      			stockMap.put(skuno,"0");
	      		}
	          }
	  	}catch(Exception e){
	  		loggerError.info(e);
	  	}
	    return stockMap;
  }
    public static void main(String[] args) {
    	//加载spring
        loadSpringContext();
        //拉取数据
        StockImp stockImp =(StockImp)factory.getBean("efashion");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("efashiom更新库存开始");
        System.out.println("efashiom更新库存开始");
        try {
			stockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
		} catch (Exception e) { 
			loggerError.info(e);
			e.printStackTrace();
		}
        logger.info("efashiom更新库存结束");
        System.out.println("efashiom更新库存结束");
        System.exit(0);
    }
}
