package com.shangpin.iog.coltorti;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.coltorti.service.ColtortiProductService;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

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
	 * 抓取数据
	 */
	static void grabProduct(){

		Map<String,ProductFetchService> fetchsrvs=factory.getBeansOfType(ProductFetchService.class);
		Map<String, ProductSearchService> fetch=factory.getBeansOfType(ProductSearchService.class);
		ProductFetchService pfs=null;
		ProductSearchService productSearchService = null;
		if(fetchsrvs!=null && fetchsrvs.size()>0)
			pfs=fetchsrvs.entrySet().iterator().next().getValue();
		if(fetch!=null && fetch.size()>0)
			productSearchService=fetch.entrySet().iterator().next().getValue();
		if(productSearchService==null||pfs==null) {
			logger.error("初始化Spring上下文错误!");
			return ;			
		}
		ColtortiProductService dataSrv= new ColtortiProductService();
		String isoFmt="yyyy-MM-dd'T'HH:mm:ss'Z'";
		String s=DateTimeUtil.convertFormat(startDate,isoFmt);
		String e=DateTimeUtil.convertFormat(endDate,isoFmt);
		dataSrv.fetchProductAndSave(s, e);
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
		grabProduct();
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
		
	}
	
	private static File getConfFile() throws IOException{
		String realPath = ColtortiStartup.class.getClassLoader().getResource("").getFile();
		realPath= URLDecoder.decode(realPath,"utf-8");
		File df = new File(realPath+"conf.ini");
		if(!df.exists()){
			df.createNewFile();
		}
		return df;
	}
	private static String getLastGrapDate(){
		File df;
		String dstr=null;
		try {
			df = getConfFile();
			try(BufferedReader br = new BufferedReader(new FileReader(df))){
				dstr=br.readLine();			
			}
		} catch (IOException e) {
			logger.error("读取日期配置文件错误");
		}
		return dstr;
	}
	
	private static void writeGrapDate(Date d){
		File df;
		try {
			df = getConfFile();
			String dstr=DateTimeUtil.convertFormat(d, YYYY_MMDD_HH);
			try(BufferedWriter bw = new BufferedWriter(new FileWriter(df))){
				bw.write(dstr);			
			}
		} catch (IOException e) {
			logger.error("写入日期配置文件错误");			
		}
	}
}
