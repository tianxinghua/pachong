package com.shangpin.iog.bagheera.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.bagheera.stock.dto.BagheeraDTO;
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
            bdl=ResourceBundle.getBundle("param");
        supplierId = bdl.getString("supplierId");
    }

    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        Map<String, String> skustock = new HashMap<>(skuNo.size());
        DownloadAndReadExcel excelHelper = new DownloadAndReadExcel();
        List<BagheeraDTO> list=excelHelper.readLocalExcel();
        Iterator<String> it = skuNo.iterator();
        while (it.hasNext()) {
            String skuId = it.next();
            for(int i=0;i<list.size();i++){
                if(skuId.equals(list.get(i).getSUPPLIER_CODE()+"-"+list.get(i).getSIZE())){
                    skustock.put(skuId,list.get(i).getSTOCK()/*+"|"+list.get(i).getLIST_PRICE()*/);
                }
            }
        }
        return skustock;
    }

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();
        StockClientImp stockImp = (StockClientImp)factory.getBean("bagheeraStock");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("bagheera更新数据库开始");
        try {
        	stockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
		} catch (Exception e) {
			logger.info("bagheera更新数据库出错"+e.toString());
		}
        logger.info("bagheera更新数据库结束");
        System.exit(0);

    }

}
