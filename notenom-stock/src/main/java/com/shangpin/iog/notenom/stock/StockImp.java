package com.shangpin.iog.notenom.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.notenom.dto.Item;
import com.shangpin.iog.notenom.schedule.AppContext;
import com.shangpin.iog.notenom.util.ReadExcel;
import com.shangpin.sop.AbsUpdateProductStock;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by   on 2015/9/14.
 */
@Component("notenomstock")
public class StockImp  extends AbsUpdateProductStock {
	private static Logger logger = Logger.getLogger("info");
	private static Logger error = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String url = "";
	private static String path = "";
	
	private static String host;
	private static String app_key;
	private static String app_secret;
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("url");
		path = bdl.getString("path");
		
		host = bdl.getString("HOST");
	    app_key = bdl.getString("APP_KEY");
	    app_secret = bdl.getString("APP_SECRET");
	}
    @Override
    public Map<String,Integer> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	
    	//获取库存元数据
    	Map<String,Integer> skuMap = new HashMap<String,Integer>();
        Map<String,Integer> returnMap = new HashMap<String,Integer>();
        logger.info("=================下载文件开始================="); 
        ReadExcel.downLoadFile(url, path);
        logger.info("=================转化对象开始================="); 
		List<Item> allProducts = ReadExcel.readExcel(Item.class,path);	
		logger.info("转化对象数组的大小是========"+allProducts.size()); 
		for (Item item : allProducts) {
			String size = item.getSize().trim().replaceAll(",", ".").replaceAll("\t", " ").replaceAll("\\s+", " ").replaceAll("\r", "").replaceAll("\n", "");
			String[] sizes = size.split(" ");
			if(sizes.length>0){
				for (int i = 0; i < sizes.length; i++) {
					try{
						if(StringUtils.isNotBlank(sizes[i])){
							String[] stockSize = sizes[i].split("/");
							String stock = stockSize[0];
							String productSize = stockSize[1];
							skuMap.put(item.getSkuNo()+"-"+productSize, Integer.valueOf(stock)); 
						}						
					}catch(Exception ex){
						ex.printStackTrace();
						error.error(ex); 
						error.error("stockSize==="+sizes[i]); 
					}					
				}
			}
		}
		
		for (String skuno : skuNo) {
            if(skuMap.containsKey(skuno)){
            	returnMap.put(skuno, skuMap.get(skuno));
            } else{
            	returnMap.put(skuno, 0);

            }
        }
        logger.info("返回的map大小====="+returnMap.size());
        return returnMap;
    }

    private static ApplicationContext factory;
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
	
	public static void main(String[] args){
		
		loadSpringContext();		
//		StockImp fetchProduct = (StockImp)factory.getBean("notenomstock");
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		logger.info("notenomstock更新数据库开始");
//		System.out.println("notenomstock更新数据库开始");
//		try {
//			
//			fetchProduct.updateProductStock(host, app_key, app_secret, "2015-01-01 00:00", format.format(new Date()));
//
//		} catch (Exception e) {
//			error.error(e.getMessage());
//			e.printStackTrace();
//		}
//		logger.info("notenomstock更新数据库结束");
//		System.out.println("notenomstock更新数据库结束");
//		System.exit(0);
		
	}
}
