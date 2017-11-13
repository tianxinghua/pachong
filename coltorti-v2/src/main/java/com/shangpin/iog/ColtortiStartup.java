package com.shangpin.iog;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.coltorti.service.ColtortiUtil;
import com.shangpin.iog.coltorti.service.UpdateStockService;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;

public class ColtortiStartup {
	/**
	 * 
	 */
	private static final String YYYY_MMDD_HH = "yyyyMMddHH";
	private static Logger logger = LoggerFactory.getLogger(ColtortiStartup.class);
	private static Date startDate=null,endDate=null;
	private static ApplicationContext factory;
	private static void loadSpringContext() {
		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	
	/**
	 * 更新库存

	 * @throws Exception 
	 */
	static void updateStock() throws Exception{
		logger.info("执行更新库存------");
		String fmt="yyyy-MM-dd HH:mm";
		UpdateStockService uss =(UpdateStockService)factory.getBean("updateStockService");
		uss.setUseThread(true);uss.setSkuCount4Thread(1000);
		uss.updateProductStock(ColtortiUtil.supplier,DateTimeUtil.convertFormat(startDate, fmt), 
				DateTimeUtil.convertFormat(endDate, fmt));
	}
	
	/**
	 * 抓取指定天数据，可传入3参数：<br/>
	 * 参数1（必填）：p或者s,p代表获取数据，s代表更新库存<br/>
	 * 参数2：开始时间，yyyyMMddHH（年月日时）形式<br/>
	 * 参数3：结束时间，yyyyMMddHH（年月日时）形式<br/>
	 * 时间若没有则取上次执行时间到本次时间，如：2015061100
	 * @param args
	 */
	public static void main(String[] args) {
		
		initDate(args);
		//初始化spring
		loadSpringContext();
		try {
			//ColtortiTokenService.initToken();
			updateStock();
			logger.warn("==========更新库存完成========");
		} catch (Exception e) {
			logger.error("更新库存异常",e);
		}
		HttpUtil45.closePool();
		System.exit(0);
	}
	/**
	 * @param args
	 * @return
	 */
	private static void initDate(String[] args) {
		if(args!=null && args.length>2){
			startDate=DateTimeUtil.parse(args[1],YYYY_MMDD_HH);
			endDate=DateTimeUtil.parse(args[2],YYYY_MMDD_HH);
			if(startDate==null ||endDate==null){
				logger.error("拉取时间参数错误,start:{},end:{}",args[0],args[1]);
			}
		}else{
			endDate = DateTimeUtil.convertDateFormat(new Date(), YYYY_MMDD_HH);
			startDate=DateTimeUtil.parse("2017010100",YYYY_MMDD_HH);
		}
		/*if(DateTimeUtil.LongFmt(endDate).equals(DateTimeUtil.LongFmt(startDate))){
			logger.warn("抓取开始时间，结束时间一致!");
		}*/
	}
	
}
