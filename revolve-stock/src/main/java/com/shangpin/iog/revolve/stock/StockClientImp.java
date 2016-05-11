package com.shangpin.iog.revolve.stock;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.revolve.stock.dto.ProductDTO;
import com.shangpin.iog.revolve.stock.schedule.AppContext;
import com.shangpin.iog.revolve.stock.sepStrategy.ISepStrategy;
import com.shangpin.iog.revolve.stock.sepStrategy.SepStrategyContext;
import com.shangpin.iog.revolve.stock.util.Csv2DTO;

/**
 * Created by sunny on 2015/9/10.
 */
@Component("revolveStock")
public class StockClientImp extends AbsUpdateProductStock{
    private static Logger logger = Logger.getLogger("info");
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String url;
    private static String filepath;

    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        supplierId = bdl.getString("url");
        supplierId = bdl.getString("filepath");
    }
    

    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	String skuId ="";
    	String size = "";
        Map<String, String> skustock = new HashMap<>();
        Map<String, String> skuData = new HashMap<>();
        
        String sep = "\t";
		Csv2DTO csv2 = new Csv2DTO();
		//第一个为size and stock
		String[] needColsNo = new String[]{"","","2","","","","","","","","","","","","","","","","20","",""};
		//策略组
		String[] strategys = new String[]{"","","","","","","","","","","","","","","","","","","","",""};
		ISepStrategy[] iSepStrategies = new SepStrategyContext().operate(strategys);
		List<ProductDTO> list = csv2.toDTO(url, filepath, sep, needColsNo, iSepStrategies, ProductDTO.class);
        
        for (ProductDTO dto : list) {
        	skuData.put(dto.getSkuId(), dto.getStock());
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
//        loadSpringContext();
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
