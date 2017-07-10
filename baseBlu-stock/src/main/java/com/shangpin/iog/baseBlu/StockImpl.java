package com.shangpin.iog.baseBlu;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.baseBlu.dto.Item;
import com.shangpin.iog.baseBlu.schedule.AppContext;

@Component("baseBlu")
public class StockImpl extends AbsUpdateProductStock {

	private static Logger logInfo = Logger.getLogger("info");
	
	private static ResourceBundle bdl = null;
	private static String supplierId = "";	
	private static String uri;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		uri = bdl.getString("uri");
	}
	
	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		
		Map<String, String> skustock = new HashMap<String, String>();
		Map<String,String> stockMap = new HashMap<String, String>();
		
		List<Item> items = CVSUtil.readCSV(uri, Item.class, ',');
		if(null != items && items.size()>0){
			logInfo.info("------------------一共有"+items.size()+"条数据----------------"); 
			for(Item item : items){
				try {
					String size_stock_barcode = item.getSize_stock_barcode();
					String[] strs = size_stock_barcode.split("\\|");
					for(int i=0;i<strs.length;i++){
						try {
							if(i % 3 == 0){
								stockMap.put(strs[i+2], strs[i+1]);
							}
						} catch (Exception e) {
							
						}						
					}
				} catch (Exception e) {
					e.printStackTrace();
					logInfo.error(item.getSku_brand()+"出错 "+e.toString()); 
				}				
			}
		}else{
			logInfo.info("======抓取数据失败======");
			return skustock;
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
	
	private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    public static void main(String[] args) {
    	
    	//加载spring
        loadSpringContext();       
    }
	
}
