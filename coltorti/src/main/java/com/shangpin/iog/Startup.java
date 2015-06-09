package com.shangpin.iog;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.coltorti.service.InsertDataBaseService;
import com.shangpin.iog.coltorti.service.UpdateStockService;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.service.ProductFetchService;

public class Startup {
	private static Logger logger = LoggerFactory.getLogger(Startup.class);
	private static ApplicationContext factory;

	private static void loadSpringContext() {
		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	/**
	 * 抓取数据
	 * @param fetchSrv
	 * @param dateStart
	 * @param dateEnd
	 */
	static void grabProduct(ProductFetchService fetchSrv,Date dateStart,Date dateEnd){
		InsertDataBaseService dataSrv= new InsertDataBaseService(fetchSrv);
		String isoFmt="yyyy-MM-dd'T'HH:mm:ssZ";
		dataSrv.grabProduct(DateTimeUtil.convertFormat(dateStart,isoFmt), 
				DateTimeUtil.convertFormat(dateEnd,isoFmt));
	}
	/**
	 * 更新库存
	 * @param dateStart
	 * @param dateEnd
	 */
	static void updateStock(Date dateStart,Date dateEnd){
		String fmt="yyyy-MM-dd HH:mm";
		UpdateStockService.updateStock(DateTimeUtil.convertFormat(dateStart, fmt), 
				DateTimeUtil.convertFormat(dateEnd, fmt));
	}
	
	/**
	 * 抓取指定天数据，可传入3参数：<br/>
	 * 参数1（必填）：p或者s,p代表获取数据，s代表更新库存<br/>
	 * 参数2：开始时间，yyyyMMddHHmm（年月日时分）形式<br/>
	 * 参数3：结束时间，yyyyMMddHHmm（年月日时分）形式<br/>
	 * 时间若没有则取前一天零点到今天零点1天的数据，如：201506010000  201506020000  
	 * @param args
	 */
	public static void main(String[] args) {
		String action=(args==null ||args.length<1)?null:args[0];
		if(!"p".equals(action) && !"s".equals(action)){
			System.out.println(":::::警告::::\r参数1必填,可为p或者s。p代表获取数据，s代表更新库存");
			logger.error("参数1（必填）：p或者s。p代表获取数据，s代表更新库存");
			return ;
		}
		Date ds=null;Date de=null;
		if(args!=null && args.length>2){
			ds=DateTimeUtil.parse(args[0],"yyyyMMddHH");
			de=DateTimeUtil.parse(args[1],"yyyyMMddHH");
			if(ds==null ||de==null){
				logger.error("拉取时间参数错误,start:{},end:{}",args[0],args[1]);
				return ;
			}
		}else{
			de=DateTimeUtil.convertDateFormat(new Date(), "yyyyMMdd");
			ds=DateUtils.addDays(de, -5);
		}
		if("p".equals(action)){
			logger.info("执行抓取数据------");
			// 加载spring
			loadSpringContext();
			Map<String,ProductFetchService> fetchsrvs=factory.getBeansOfType(ProductFetchService.class);
			ProductFetchService pfs=null;
			if(fetchsrvs!=null && fetchsrvs.size()>0)
				pfs=fetchsrvs.entrySet().iterator().next().getValue();
			if(pfs==null) {
				logger.error("初始化Spring上下文错误!");
				return ;			
			}
			grabProduct(pfs, ds, de);
		}else{
			logger.info("执行更新库存------");
			updateStock(ds, de);
		}
		
	}

}
