
package com.shangpin.iog.paolo.stock.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.shangpin.sop.AbsUpdateProductStock;


@Component
@PropertySource("classpath:sop.properties")
public class Worker implements Runnable{
	private static Logger logger = Logger.getLogger("info");
//	private static ResourceBundle bdl=null;
//    private static String supplierId = "";
//    static {
//        if(null==bdl)
//         bdl=ResourceBundle.getBundle("sop");
//        supplierId = bdl.getString("supplierId");
//    }
    
    @Value("${HOST}")
	private String host;
	@Value("${APP_KEY}")
	private String app_key;
	@Value("${APP_SECRET}")
	private String app_secret;	
    
	private AbsUpdateProductStock stockImp;
	public Worker(){};
	public Worker(AbsUpdateProductStock stockImp) {
		this.stockImp = stockImp;
	}
	@Override
	public void run() {
		try {
			System.out.println("开始");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			logger.info("更新数据库开始");
			try {
				stockImp.updateProductStock(host, app_key, app_secret, "2015-01-01 00:00", format.format(new Date()));
				//stockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
			} catch (Exception e) {
				logger.info("更新库存数据库出错"+e.toString());
			}
			logger.info("更新数据库结束");
			System.out.println("结束");
		} catch (Exception e) {
			logger.info("aaaaaaaaaaaaaaa被取消了");
			System.out.println("aaaaaaaaaaaaaaa被取消了");
		}
	}
	
}
