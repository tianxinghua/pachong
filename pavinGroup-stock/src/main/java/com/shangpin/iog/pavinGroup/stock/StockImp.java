package com.shangpin.iog.pavinGroup.stock;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.EventProductDTO;
import com.shangpin.iog.pavinGroup.dto.Channel;
import com.shangpin.iog.pavinGroup.dto.Item;
import com.shangpin.iog.pavinGroup.dto.Rss;
import com.shangpin.iog.product.service.EventProductServiceImpl;
import com.shangpin.iog.service.EventProductService;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2015/7/8.
 */
@Component("pavinGroup")
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
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String Designers;
	private static String Donna;
	private static String FlashSale;
	private static String Highlights ;
	private static String NuoviArrivi	;
	private static String SpecialPrice;
	private static String Spring;
	private static String Uomo ;
	private static String [] array;
	private static Map<String,String>	map = null;
	private static String new1 ;
	private static String new2 ;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		Designers = bdl.getString("Designers");
		Donna = bdl.getString("Donna");
		FlashSale = bdl.getString("FlashSale");
		Highlights = bdl.getString("Highlights");
		NuoviArrivi = bdl.getString("NuoviArrivi");
		SpecialPrice = bdl.getString("SpecialPrice");
		Spring = bdl.getString("Spring");
		Uomo = bdl.getString("Uomo");
		new1 = bdl.getString("new1");
		new2 = bdl.getString("new2");
		array = new String[]{Designers,Donna,FlashSale,Highlights,NuoviArrivi,SpecialPrice,Spring,Uomo,new1,new2};
	}
   
    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        //get tony return date
        //定义三方
    	Map<String,String> stockMap = new HashMap<>();
    	Map<String,String>  map = fetchProductStcok();
    	if(map!=null){
    		for (String skuno : skuNo) {
    			if(map.containsKey(skuno)){
    				  stockMap.put(skuno,map.get(skuno));
    			}else{
    				stockMap.put(skuno,"0");
    			}
            }
    	}
        return stockMap;
    }
	public static Map<String, String> fetchProductStcok() {
		map = new HashMap<String,String>();
		Map<String,String> map = null;
		String xml = null;
		for(int i=0;i<array.length;i++){
			fetchProduct(array[i]);
		}
		return map;
	}
	
	private static void fetchProduct(String url){
		
		try {
			String xml = null;
			xml = HttpUtil45
					.get(url,
							new OutTimeConfig(1000 * 60*5, 1000 * 60*5, 1000 * 60*5),
							null);
			System.out.println(url);
				ByteArrayInputStream is = null;
				
				is = new ByteArrayInputStream(
						xml.getBytes("UTF-8"));
				Rss rss = null;
				rss = ObjectXMLUtil.xml2Obj(Rss.class, is);
				if(rss!=null){
					Channel channel = rss.getChannel();
					if(channel!=null){
						
						List<Item> array = channel.getListItem();
						if(array!=null){
							for (Item item : array) {
								map.put(item.getSupplierSkuNo(),item.getStock());
							}
						}
						if(channel.getNextPage()!=null){
							fetchProduct(channel.getNextPage());
						}
					}
				}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
    public static void main(String[] args) {
    
    	//加载spring
        loadSpringContext();
        //拉取数据
        StockImp stockImp =(StockImp)factory.getBean("pavinGroup");
        fetchProductStcok();
//        List list = new ArrayList();
//        list.add("NAM61310160_AXXS804L");
//        try {
//			stockImp.grabStock(list);
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("pavinGroup更新库存开始");
        System.out.println("pavinGroup更新库存开始");
        try {
			stockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        logger.info("pavinGroup更新库存结束");
        System.out.println("pavinGroup更新库存结束");
        System.exit(0);
    }
}
