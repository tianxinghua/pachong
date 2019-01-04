package com.shangpin.iog.selfridgesHK.schedule;

import com.shangpin.iog.selfridgesHK.service.FetchStockImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;


@Component
public class Worker implements Runnable{
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl=null;
    private static String supplierId = "";
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }
	private FetchStockImpl stockImp;
	public Worker(){};
	public Worker(FetchStockImpl stockImp) {
		this.stockImp = stockImp;
	}
	@Override
	public void run() {
		try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startDateTime = format.format(new Date());
            System.out.println("lkbennett库存数据库开始 "+startDateTime);

			logger.info("更新lkbennett库存数据库开始 "+startDateTime);
			try {
//				stockImp.setUseThread(true);
//				stockImp.setSkuCount4Thread(500);
				//stockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
				//定时拉取 意大利 官网商品库存信息
				stockImp.fetchItlyProductStock();
			} catch (Exception e) {
				logger.info("更新库存数据库出错"+e.toString());
			}
            String endtDateTime = format.format(new Date());
			logger.info("更新数据库结束 "+endtDateTime);
			System.out.println("更新数据库结束 "+endtDateTime);
		} catch (Exception e) {
			logger.info("lkbennett被取消了");
		}
	}
	
}
