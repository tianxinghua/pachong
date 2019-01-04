package com.shangpin.iog.papini.stock;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.papini.dto.Item;
import com.shangpin.iog.papini.dto.Items;
import com.shangpin.iog.papini.dto.Product;
import com.shangpin.iog.papini.util.ZipUtil;

/**
 * Created by houkun on 2015/9/14.
 */
@Component("papinistock")
public class StockImp  extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String url;
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        url = bdl.getString("url");
    }
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	
    	//获取库存元数据
    	Map<String,String> skuMap = new HashMap<String,String>();
    	List<Product> products = new ZipUtil().getAllProduct();
    	String skuId = "";
    	for (Product product : products) {
    		Items items = product.getItems();
			if (null == items) {
				continue;
			}
			List<Item> itemList = items.getItems();
			if (null == itemList){
				continue;
			}
			for (Item item : items.getItems()) {
				if (StringUtils.isNotBlank(item.getStock())) {
					skuId = item.getItem_id();
					if (skuId.indexOf("½") > 0) {
						skuId = skuId.replace("½", "+");
					}
					skuMap.put(skuId, item.getStock());
				}
			}
			
    	}
    	

        Map<String,String> returnMap = new HashMap<String,String>();
        Iterator<String> iterator=skuNo.iterator();
        //为供应商循环赋值
        logger.info("循环赋值");
        String stock = "0";
        while (iterator.hasNext()){
        	skuId = iterator.next();
        	if (StringUtils.isNotBlank(skuId)) {
        		if (skuMap.containsKey(skuId)) {
        			stock = skuMap.get(skuId);
        			returnMap.put(skuId, stock);
				}else{
					returnMap.put(skuId, "0");
				}
			}
        }
        return returnMap;
    }

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();
        StockImp stockImp =(StockImp)factory.getBean("papinistock");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("papini更新数据库开始");
        try {
			stockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
		} catch (Exception e) {
			logger.info("papini更新库存数据库出错"+e.toString());
		}
        logger.info("papini更新数据库结束");
        System.exit(0);
    }
}
