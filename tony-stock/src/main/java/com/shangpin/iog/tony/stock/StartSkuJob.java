package com.shangpin.iog.tony.stock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.tony.common.Constant;
import com.shangpin.iog.tony.service.FetchProduct;
import com.shangpin.iog.tony.stock.service.TonyStockImp;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wangyuzhi on 2015/9/11.
 */
public class StartSkuJob {

    private static Logger log = Logger.getLogger("info");

    private static ApplicationContext factory;
    private static void loadSpringContext() {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String[] args) throws Exception {
        //加载spring
        loadSpringContext();
        log.info("----初始SPRING成功----");
        //拉取数据
        log.info("----拉取tony数据开始----");
        TonyStockImp tonyStockImp =(TonyStockImp)factory.getBean("tony");
        TonyStockImp impl = new TonyStockImp();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
         impl.updateProductStock(Constant.SUPPLIER_ID, "2015-01-01 00:00", format.format(new Date()));
        log.info("----拉取tony数据完成----");
        System.out.println("-------fetch end---------");
        System.exit(0);
    }

}
