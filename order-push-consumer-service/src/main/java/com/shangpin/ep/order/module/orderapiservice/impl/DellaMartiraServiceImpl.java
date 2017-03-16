package com.shangpin.ep.order.module.orderapiservice.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shangpin.ep.order.enumeration.OrderStatus;
import com.shangpin.ep.order.module.order.bean.HubOrderDetail;
import com.shangpin.ep.order.module.order.service.IHubOrderDetailService;
import com.shangpin.ep.order.module.sku.bean.HubSku;
import com.shangpin.ep.order.module.sku.service.IHubSkuService;
import com.shangpin.ep.order.util.date.DateTimeUtil;

import lombok.extern.slf4j.Slf4j;

@Component("dellaMartiraServiceImpl") 
@Slf4j
public class DellaMartiraServiceImpl {
	@Autowired
	private IHubOrderDetailService hubOrderDetailService;
	@Autowired
	private IHubSkuService hubSkuService;
	
	private static String split = ";";
	private static String YYYY_MMDD_HH = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 产生的订单文件
	 */
	private static String localPath = "/usr/local/share/applications/order-push-consumer-service/lib/dellaMartira/";
	/**
	 * csv文件第一行
	 */
	private static String header = "Purchasing number;PO Line;Item code;Description;Item supplier code;Price;Quantity;Size";
	
	@Scheduled(cron="00 00 00,14,19 * * ?")
	public void uploadFtp(){
		try {
			Date endTime = new Date();
			Date startTime = getLastGrapDate();
			log.info("dellaMartira推送订单开始时间："+startTime+" 结束时间："+endTime); 
			List<HubOrderDetail> orderDetails = hubOrderDetailService.findHubOrderDetails("2015112001671", OrderStatus.PAYED, startTime, endTime);
			if(null != orderDetails && orderDetails.size() >0){
				log.info("dellaMartira查询到的订单的数量是========="+orderDetails.size());
				StringBuffer buffer = new StringBuffer();
				buffer.append(header).append("\r\n");
				String dayTime = DateTimeUtil.strForDateTime(endTime);
				for(HubOrderDetail orderDetail : orderDetails){
					HubSku sku = hubSkuService.getSku(orderDetail.getSupplierId(), orderDetail.getSupplierSkuNo());
					buffer.append(orderDetail.getPurchaseNo()).append(split)
					.append("").append(split)
					.append(sku.getProductCode()).append(split)
					.append("").append(split)
					.append(orderDetail.getSupplierSkuNo()).append(split)
					.append("").append(split)
					.append(orderDetail.getQuantity()).append(split)
					.append(sku.getProductSize());
					buffer.append("\r\n"); 
				}
				String orders = buffer.toString();
				log.info("dellaMartira今日推送订单："+orders);
				String localFile = localPath+dayTime+".csv";
				save(localFile,orders);
				upload(localFile);
			}else{
				log.info("dellaMartira在该时间段内没有订单。");
			}
			writeGrapDate(endTime);
		} catch (Exception e) {
			log.info("dellaMartira推送订单异常："+e.getMessage(),e); 
		}
	}
	
	/**
	 * 保存订单文件到磁盘
	 * @param localFile
	 * @param data
	 */
	private void save(String localFile,String data){
    	File file = new File(localFile);
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				log.error("dellaMartira保存订单到磁盘创建目录失败："+e.getMessage(),e); 
			}
		}
		FileWriter fwriter = null;
		try {
			fwriter = new FileWriter(localFile);
			fwriter.write(data);
		} catch (IOException ex) {
			log.error("dellaMartira保存订单到磁盘失败："+ex.getMessage(),ex); 
		} finally {
			try {
				fwriter.flush();
				fwriter.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
    }
	/**
	 * 上传订单文件到ftp
	 * @param localFile
	 */
	public void upload(String localFile) { 
		int i=0;
		for(;i<10;i++){
			log.info("dellaMartira上传订单到ftp第"+i+"次上传开始");
			FTPClient ftp = new FTPClient(); 
			FileInputStream fis = null;
	        try {
	        	ftp.setDefaultTimeout(1000 * 60 * 5);
				ftp.setConnectTimeout(1000 * 60 * 5);
				ftp.enterLocalActiveMode();
				ftp.connect("92.223.134.2", 21);
				ftp.login("mosuftp", "inter2015£");
				ftp.setControlEncoding("UTF-8");
				ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
				ftp.changeWorkingDirectory("/MOSU/Orders");
				ftp.enterLocalPassiveMode();
				File srcFile = new File(localFile);
				fis = new FileInputStream(srcFile);
				log.info("dellaMartira上传订单连接ftp成功");
				ftp.storeFile(srcFile.getName(), fis);
				log.info("dellaMartira上传订单"+srcFile.getName()+"上传成功!!!!!!!!!!!!!!!!!");
				break;
			} catch (Exception e) {	
				log.error("dellaMartira上传订单到ftp异常："+e.getMessage(),e); 
			}finally{
				try {
					if(null != fis){
						fis.close();
					}
					ftp.disconnect();
				} catch (IOException e) {
				}
			}
		}
    }
	/**
	 * 获取时间配置文件
	 * @return
	 * @throws IOException
	 */
	private File getConfFile() throws IOException{
		File df = new File(localPath+"date-conf.ini");
		if(!df.exists()){
			df.getParentFile().mkdirs();
			df.createNewFile();
		}
		return df;
	}
	/**
	 * 获取配置文件中的时间
	 * @return
	 */
	private Date getLastGrapDate(){
		File df;
		String dstr=null;
		try {
			df = getConfFile();
			try(BufferedReader br = new BufferedReader(new FileReader(df))){
				dstr=br.readLine();			
			}
		} catch (Exception e) {
			log.error("dellaMartira 读取日期配置文件错误："+e.getMessage(),e);
		}
		if(StringUtils.isEmpty(dstr)){
			return DateTimeUtil.getYesterdayTime();
		}else{
			return DateTimeUtil.convertFormat(dstr, YYYY_MMDD_HH);
		}
	}
	/**
	 * 记录最后时间
	 * @param d
	 */
	private void writeGrapDate(Date d){
		File df;
		try {
			df = getConfFile();
			String dstr=DateTimeUtil.convertFormat(d, YYYY_MMDD_HH);
			try(BufferedWriter bw = new BufferedWriter(new FileWriter(df))){
				bw.write(dstr);			
			}
		} catch (Exception e) {
			log.error("dellaMartira 写入日期配置文件错误："+e.getMessage(),e);			
		}
	}

}
