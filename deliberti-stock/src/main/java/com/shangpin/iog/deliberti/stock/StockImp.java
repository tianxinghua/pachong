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
        try {
            List<Product> products = MyUtil.readLocalCSV(url, Product.class);
            if(products.size()>0){

                logger.info("------------------一共有"+products.size()+"条数据----------------");
                for(Product pro :products){
                    String sizeAndquantity = pro.getSizeAndquantity();
                    if (sizeAndquantity!=null) {
                        String[] split2 = sizeAndquantity.split(";");
                        if (split2.length > 0) {
                            for (String s : split2) {
                                if (s.contains("(")){

                                    SkuDTO sku = new SkuDTO();
                                    String substring1 = s.substring(0, s.indexOf("("));
                                    sku.setProductSize(substring1);
                                    String stock = s.substring(s.indexOf("(") + 1, s.indexOf(")"));
                                    sku.setStock(stock);
                                    stockMap.put(pro.getSpuId() + "_" + sku.getProductSize(), sku.getStock());

                                }
                            }


                        }
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

        }catch (Exception e){
            e.printStackTrace();

        }
        return skustock;
    }

    public static void main(String[] args) throws Exception {
    	//加载spring

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
