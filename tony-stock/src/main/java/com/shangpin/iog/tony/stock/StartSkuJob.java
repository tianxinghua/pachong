package com.shangpin.iog.tony.stock;
import com.shangpin.iog.app.AppContext;

import com.shangpin.iog.dto.StockUpdateDTO;
import com.shangpin.iog.product.service.UpdateStockServiceImpl;
import com.shangpin.iog.service.UpdateStockService;
import com.shangpin.iog.tony.stock.service.TonyStockImp;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by wangyuzhi on 2015/9/11.
 */
public class StartSkuJob {

    private static Logger log = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");

    private static ApplicationContext factory;
    private static void loadSpringContext() {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    //供应商ID
    public static ResourceBundle bundle = ResourceBundle.getBundle("conf");
    public static String SUPPLIER_ID = bundle.getString("supplierId");
    public static void main(String[] args) throws Exception {
        //加载spring
        loadSpringContext();
        log.info("----初始SPRING成功----");
        //拉取数据
        log.info("----拉取tony数据开始----");
        TonyStockImp tonyStockImp =(TonyStockImp)factory.getBean("tonyStock");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            tonyStockImp.updateProductStock(SUPPLIER_ID, "2015-01-01 00:00", format.format(new Date()));

        } catch (Exception e) {
            loggerError.error("更新库存失败。"+ e.getMessage());
            e.printStackTrace();
        }
        log.info("----拉取tony数据完成----");
        System.out.println("-------fetch end---------");
        System.exit(0);
    }

}
