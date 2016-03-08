package com.shangpin.iog.railSo.stock;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.EventProductDTO;
import com.shangpin.iog.product.service.EventProductServiceImpl;
import com.shangpin.iog.railSo.dto.Attribute;
import com.shangpin.iog.railSo.dto.Attributes;
import com.shangpin.iog.railSo.dto.Stock;
import com.shangpin.iog.railSo.dto.Stocks;
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
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

/**
 * Created by Administrator on 2015/7/8.
 */
@Component("railSo")
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
        //get tony return date
        //定义三方
    	Map<String,String> stockMap = new HashMap<>();
    	Map<String,String> map = getStockMap();
        for (String skuno : skuNo) {
        	if(map.containsKey(skuno)){
        		stockMap.put(skuno,map.get(skuno));
        	}else{
        		stockMap.put(skuno,"0");
        	}
        }
        return stockMap;
    }

    	
    private static Map<String,String> getStockMap() {
    	 Map<String,String> returnMap = new HashMap<String,String>();
   		try{
   			Map<String,String> map = new HashMap<String,String>();
   	    	map.put("id","4");
   	    	map.put("password","JwqDZDF-5jk%YRH=");
   	    	map.put("affiliate","shangpin");
   	    	String json = HttpUtil45.post("http://www.railso.com/xml_feeds.php", map,new OutTimeConfig(1000*60*10,10*1000*60,10*1000*60));// new HTTPClient(Constant.URL_MARYLOU).fetchProductJson();
   	    	System.out.println("库存"+json);
   	    	logger.info(json);
   	    	Pattern patt =Pattern.compile("</attributes-\\d*>");
   			Pattern patt1 =Pattern.compile("<attributes-\\d*>");
   			Stocks stocks = null;
   			try {
   				String replaceAll = patt.matcher(json).replaceAll("</item>");
   				String replaceAll1 = patt1.matcher(replaceAll).replaceAll("<item>");
   				stocks = ObjectXMLUtil.xml2Obj(Stocks.class, replaceAll1.toString());
   			} catch (JAXBException e) {
   				e.printStackTrace();
   			}
   			List<Stock> list = stocks.getStocks();
   		    
   			for(Stock stock:list){
   				Attributes att = stock.getAttributes();
   				if(att!=null){
   					String productId = stock.getProduct_id().trim();
   					List<Attribute> li = att.getAttributes();
   					for(Attribute a : li){
   						if("Unique".equals(a.getAttribute_name())||"One size".equals(a.getAttribute_name())){
   							returnMap.put(productId+"|A", a.getQuantity());
   						}else{
   							returnMap.put(productId+"|"+a.getAttribute_name(), a.getQuantity());
   						}
   					}
   				}
   			}
   		}catch(Exception ex){
   			
   		} 
   		
   		return returnMap;
   	}
    public static void main(String[] args) {
    
    	//加载spring
        loadSpringContext();
        //拉取数据
        StockImp stockImp =(StockImp)factory.getBean("railSo");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("railSo更新库存开始");
        System.out.println("railSo更新库存开始");
        try {
			stockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		}
        logger.info("railSo更新库存结束");
        System.out.println("railSo更新库存结束");
        System.exit(0);
    }
}
