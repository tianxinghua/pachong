package com.shangpin.iog.bagheera.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.bagheera.stock.dto.BagheeraDTO;
import com.shangpin.iog.bagheera.stock.schedule.AppContext;
import com.shangpin.iog.bagheera.stock.utils.DownloadAndReadExcel;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sunny on 2015/9/10.
 */
@Component("bagheeraStock")
public class StockClientImp extends AbsUpdateProductStock{
    private static Logger logger = Logger.getLogger("info");
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    private static ResourceBundle bdl=null;
    private static String supplierId;

    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }

    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	String skuId ="";
    	String size = "";
        Map<String, String> skustock = new HashMap<>();
        Map<String, String> skuData = new HashMap<>();
        
        DownloadAndReadExcel excelHelper = new DownloadAndReadExcel();
        List<BagheeraDTO> list=excelHelper.readLocalExcel();
        for (BagheeraDTO bagheeraDTO : list) {
        	 size = bagheeraDTO.getSIZE();
             if(size.indexOf("½")>0){
                 size=size.replace("½","+");
             }
            skuId = bagheeraDTO.getSUPPLIER_CODE()+"-"+size;
        	skuData.put(skuId, bagheeraDTO.getSTOCK());
		}
        
        Iterator<String> it = skuNo.iterator();
        
        
        while (it.hasNext()) {
            skuId = it.next();
            if (skuData.containsKey(skuId)) {
            	skustock.put(skuId, skuData.get(skuId));
			}else {
				skustock.put(skuId, "0");
			}
        }
        return skustock;
    }

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();
//        StockClientImp stockImp = (StockClientImp)factory.getBean("bagheeraStock");
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        logger.info("bagheera更新数据库开始");
//        try {
//        	stockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
//		} catch (Exception e) {
//			logger.info("bagheera更新数据库出错"+e.toString());
//		}
//        logger.info("bagheera更新数据库结束");
//        System.exit(0);

    }

}
