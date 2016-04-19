package com.shangpin.iog.deliberti.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.deliberti.stock.dto.Product;
import com.shangpin.iog.deliberti.stock.utils.MyUtil;
import com.shangpin.iog.dto.SkuDTO;

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
@Component("delibertistock")
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
    	Map<String,String> skustock = new HashMap<String,String>();
        Map<String,String> stockMap = new HashMap<String,String>();
        
        List<Product> products = MyUtil.readLocalCSV(url, Product.class);
        if(products.size()>0){
        	logger.info("------------------一共有"+products.size()+"条数据----------------");
        	for(Product pro :products){
        		String sizes [] = {pro.getSize35(),pro.getSize35x(),pro.getSize36(),pro.getSize36x(),pro.getSize37(),pro.getSize37x(),
						pro.getSize38(),pro.getSize38x(),pro.getSize39(),pro.getSize39x(),pro.getSize40(),pro.getSize40x(),
						pro.getSize41(),pro.getSize41x(),pro.getSize42(),pro.getSize42x(),pro.getSize43(),pro.getSize43x(),
						pro.getSize44(),pro.getSize44x(),pro.getSize45(),pro.getSize45x(),pro.getSize46(),pro.getSize46x(),
						pro.getSize47(),pro.getSize47x(),pro.getSize48(),pro.getSize48x(),pro.getSize49(),pro.getSize49x()};
        		for (int i = 0; i < sizes.length; i++) {
        			SkuDTO sku = new SkuDTO();
        			if(sizes[i]!=null){
        				String si[] = sizes[i].split("~");
        				if(si[0].equals("")){
    						continue;
            			}else{
            				if(si[0].indexOf("x")!=-1){
								String size = si[0].replace("x", ".5");
								sku.setProductSize(size);
							}else{
								sku.setProductSize(si[0]);
							}
            			}
            			if(si[1].equals("0")){
    						continue;
            			}else{
            				sku.setStock(si[1]);
            			}
        			}else{
        				continue;
        			}
        			
        			
        			stockMap.put(pro.getSpuId()+"-"+sku.getProductSize(),sku.getStock());
        		}
			}
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

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();
//        StockImp stockImp =(StockImp)factory.getBean("delibertistock");
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        logger.info("deliberti更新数据库开始");
//        System.out.println("deliberti更新数据库开始");
//        try {
//			stockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
//		} catch (Exception e) {
//			logger.info("deliberti更新库存数据库出错"+e.toString());
//		}
//        logger.info("deliberti更新数据库结束");
//        System.out.println("deliberti更新数据库结束");
//        System.exit(0);
    }
}
