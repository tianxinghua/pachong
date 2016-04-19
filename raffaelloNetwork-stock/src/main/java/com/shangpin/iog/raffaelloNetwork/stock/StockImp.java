package com.shangpin.iog.raffaelloNetwork.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.raffaelloNetwork.stock.dto.Product;
import com.shangpin.iog.raffaelloNetwork.stock.utils.Util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dongjinghui
 */
@Component("raffaelloNetworkStock")
public class StockImp  extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String uri;
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        uri = bdl.getString("uri");
    }
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	
    	//获取库存元数据
    	Map<String,String> skustock = new HashMap<String,String>();
        Map<String,String> stockMap = new HashMap<String,String>();
        
        //List<Product> products = MyUtil.readLocalCSV(url, Product.class);
        List<Product> items = Util.readCSV(uri, Product.class);
        if(items.size()>0){
        	logger.info("------------------一共有"+items.size()+"条数据----------------");
        	for(Product pro :items){
        		//SkuDTO sku = new SkuDTO();
        		int beginIndex=pro.getSize().indexOf(",");
        		if(beginIndex!=-1){
					String size[] = pro.getSize().split(",");
					String stock[] = pro.getQuantity().split(",");
						if(size.length==stock.length){
							for (int i = 0; i < size.length; i++) {
								stockMap.put(pro.getMpn()+"_"+size[i], stock[i].trim());
								logger.info("SkuId="+pro.getMpn()+"_"+size[i]+"------stock="+stock[i]);
								//System.out.println("SkuId="+pro.getMpn()+"_"+size[i]+"------stock="+stock[i]);
							}
						}else{	
							continue;
						}
        		}else{
        			stockMap.put(pro.getMpn()+"_"+pro.getSize(),pro.getQuantity());
        			logger.info("SkuId="+pro.getMpn()+"_"+pro.getSize()+"------stock="+pro.getQuantity());
        			
        		}
        	}
        } 
        
        for (String skuno : skuNo) {
            if(stockMap.containsKey(skuno)){
            	logger.info("skuNo1="+skuno + " stock="+ stockMap.get(skuno));
                skustock.put(skuno, stockMap.get(skuno));
            } else{
            	logger.info("skuNo2="+skuno);
                skustock.put(skuno, "0");
            }
        }
        return skustock;
    }

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();
//        StockImp stockImp =(StockImp)factory.getBean("raffaelloNetworkStock");
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        logger.info("raffaelloNetwork更新数据库开始");
//        System.out.println("raffaelloNetwork更新数据库开始");
//        try {
//			stockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
//		} catch (Exception e) {
//			logger.info("raffaelloNetwork更新库存数据库出错"+e.toString());
//		}
//        logger.info("raffaelloNetwork更新数据库结束");
//        System.out.println("raffaelloNetwork更新数据库结束");
//        System.exit(0);
    }
}
