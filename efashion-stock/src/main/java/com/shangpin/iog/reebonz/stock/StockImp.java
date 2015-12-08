package com.shangpin.iog.reebonz.stock;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.EventProductDTO;
import com.shangpin.iog.product.service.EventProductServiceImpl;
import com.shangpin.iog.reebonz.dto.Item;
import com.shangpin.iog.reebonz.dto.Result;
import com.shangpin.iog.reebonz.dto.ReturnObject;
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
    
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    @Autowired
	EventProductService eventProductService;
    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String stockUrl;
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        stockUrl = bdl.getString("stockUrl");
    }
   
    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	
    	String json = HttpUtil45
				.get(stockUrl,
						new OutTimeConfig(1000 * 60, 1000 * 60, 1000 * 60),
						null);
		
		ReturnObject obj = new Gson().fromJson(json, ReturnObject.class);
		Map<String,String> map = new HashMap<String,String>();
		if(obj!=null){
			Result result = obj.getResults();
			if(result!=null){
				List<Item> list = result.getItems();
				if(list!=null){
					for(Item item : list){
						map.put(item.getProduct_id()+"|"+item.getSize(),item.getQuantity());
					}
				}
			}
		}
    	
        //get tony return date
        //定义三方
    	Map<String,String> stockMap = new HashMap<String,String>();
        for (String skuno : skuNo) {
        	//skuno格式：skuId|proCode|colorCode|size
        	String tempSku = null;
        	String [] skuArray = skuno.split("\\|");
        	if("A".equals(skuArray[3])){
        		tempSku = skuArray[0]+"|";
        	}else{
        		tempSku = skuArray[0]+"|"+skuArray[3];
        	}
            if(map.containsKey(tempSku)){
            	stockMap.put(skuno,map.get(tempSku));
            }
        }
        return stockMap;
    }
    
    public static void main(String[] args) {
    	
    	//加载spring
        loadSpringContext();
        //拉取数据
        StockImp stockImp =(StockImp)factory.getBean("efashion");
        /*
            List li = new ArrayList();
	        li.add("563cdefe9b30aec2d862b7fc|3945869|3017T|TU");
	        try {
				stockImp.grabStock(li);
			} catch (ServiceException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
         * */
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("efashiom更新库存开始");
        System.out.println("efashiom更新库存开始");
        try {
			stockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
		} catch (Exception e) { 
			e.printStackTrace();
		}
        logger.info("efashiom更新库存结束");
        System.out.println("efashiom更新库存结束");
        System.exit(0);
    }
}
