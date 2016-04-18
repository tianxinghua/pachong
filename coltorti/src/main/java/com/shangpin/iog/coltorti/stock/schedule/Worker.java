package com.shangpin.iog.coltorti.stock.schedule;

import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import com.shangpin.iog.coltorti.service.ColtortiUtil;
import com.shangpin.iog.common.utils.DateTimeUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.shangpin.ice.ice.AbsUpdateProductStock;

@Component
public class Worker implements Runnable{
	private static final String YYYY_MMDD_HH = "yyyyMMddHH";
	private static Date startDate=null,endDate=null;
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl=null;
    private static String supplierId = "";
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }
	private AbsUpdateProductStock stockImp;
	public Worker(){};
	public Worker(AbsUpdateProductStock stockImp) {
		this.stockImp = stockImp;
	}
	public void run() {
		try {
			System.out.println("开始");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			logger.info("更新数据库开始");
			String fmt="yyyy-MM-dd HH:mm";
			try {

//				stockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
				stockImp.setUseThread(true);stockImp.setSkuCount4Thread(1000);
				stockImp.updateProductStock(ColtortiUtil.supplier, DateTimeUtil.convertFormat(startDate, fmt),
						DateTimeUtil.convertFormat(endDate, fmt));

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

	private static void initDate(String[] args) {
		if(args!=null && args.length>2){
			startDate=DateTimeUtil.parse(args[1],YYYY_MMDD_HH);
			endDate=DateTimeUtil.parse(args[2],YYYY_MMDD_HH);
			if(startDate==null ||endDate==null){
//				logger.error("拉取时间参数错误,start:{},end:{}",args[0],args[1]);
			}
		}else{
			endDate = DateTimeUtil.convertDateFormat(new Date(), YYYY_MMDD_HH);
			if("s".equals(args[0]))
				startDate=DateTimeUtil.parse("2015061500",YYYY_MMDD_HH);
			else{
				String lastDate=getLastGrapDate();
				startDate= StringUtils.isNotEmpty(lastDate) ? DateTimeUtil.convertFormat(lastDate,
						YYYY_MMDD_HH) : DateUtils.addDays(endDate, -180);
			}
		}
		if("p".equals(args[0])){
			writeGrapDate(endDate);
		}
		/*if(DateTimeUtil.LongFmt(endDate).equals(DateTimeUtil.LongFmt(startDate))){
			logger.warn("抓取开始时间，结束时间一致!");
		}*/
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

	private static File getConfFile() throws IOException{
		String realPath = Thread.currentThread().getContextClassLoader().getResource("").getFile();
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
	
}
