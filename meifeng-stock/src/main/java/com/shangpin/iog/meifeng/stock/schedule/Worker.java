package com.shangpin.iog.meifeng.stock.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.meifeng.stock.StockClientImp;



@Component
public class Worker implements Runnable{
	private static Logger logger = Logger.getLogger("info");
	private static LoggerUtil logError = LoggerUtil.getLogger("error");
	private static ResourceBundle bdl=null;
    private static String supplierId = "";
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }
	private StockClientImp stockImp;
	public Worker(){};
	public Worker(StockClientImp stockImp) {
		this.stockImp = stockImp;
	}
	@Override
	public void run() {
		try {
			System.out.println("开始");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			logger.info("更新数据库开始");
			try {
				stockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
			} catch (Exception e) {
				logError.info("meifeng更新库存数据库出错"+e.toString());
				logger.info("更新库存数据库出错"+e.toString());
			}
			logger.info("更新数据库结束");
			System.out.println("结束");
		} catch (Exception e) {
			logger.info("超时销毁");
			System.out.println("超时销毁");
		}
	}
	
}
