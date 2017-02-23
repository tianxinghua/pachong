package com.shangpin.ep.order.module.orderapiservice.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.ep.order.enumeration.OrderStatus;
import com.shangpin.ep.order.module.order.bean.HubOrderDetail;
import com.shangpin.ep.order.module.order.service.IHubOrderDetailService;
import com.shangpin.ep.order.util.date.DateTimeUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MonnierfreresServiceImpl {

	@Autowired
	private IHubOrderDetailService hubOrderDetailService;
	private static String split = ",";
	/**
	 * 产生的订单文件
	 */
	private static String localPath = "/usr/local/share/applications/order-push-consumer-service/lib/monnierfreres/";
	
	@Scheduled(cron="00 00 04 * * ?")
	public void uploadFtp(){
		Date endTime = new Date();
		Date startTime = DateTimeUtil.getbeforeDay(endTime);
		log.info("monnierfreres推送订单开始时间："+startTime+" 结束时间："+endTime); 
		List<HubOrderDetail> orderDetails = hubOrderDetailService.findHubOrderDetails("2015101001587", OrderStatus.PAYED, startTime, endTime);
		if(null != orderDetails && orderDetails.size() >0){
			log.info("monnierfreres查询到的订单的数量是========="+orderDetails.size());
			StringBuffer buffer = new StringBuffer();
			buffer.append("REFCMD").append(split).append("DATCMD").append(split).append("DATLIV").append(split).append("INSCMD").append(split).append("CODCLI").append(split)
			.append("CODTRP").append(split).append("REFCMD").append(split).append("NOMCLI").append(split).append("NOMCLI").append(split).append("NOMSTE").append(split).append("ADRES1").append(split)
			.append("ADRES2").append(split).append("VILNOM").append(split).append("CODPST").append(split).append("CODPAY").append(split).append("MNTTOT").append(split).append("CODPRD").append(split)
			.append("QTECMD").append("\r\n");
			String dayTime = DateTimeUtil.strForDate(endTime);
			for(HubOrderDetail orderDetail : orderDetails){
				buffer.append(orderDetail.getPurchaseNo()).append(split)
				.append(dayTime).append(split)
				.append("").append(split)
				.append("").append(split)
				.append("Shangpin").append(split)
				.append("54").append(split)
				.append(orderDetail.getPurchaseNo()).append(split)
				.append("SHANGPIN").append(split)
				.append("GENERTEC ITALIA").append(split)
				.append("VIA GIACOMO LEOPARDI, 27").append(split)
				.append("").append(split)
				.append("LURATE CACCIVIO").append(split)
				.append("22075").append(split)
				.append("IT").append(split)
				.append("").append(split)
				.append(orderDetail.getSupplierSkuNo()).append(split)
				.append(orderDetail.getQuantity()).append("\r\n"); 
			}
			String orders = buffer.toString();
			log.info("monnierfreres今日推送订单："+orders);
			String localFile = localPath+"shangpinOrders_"+dayTime+".csv";
			save(localFile,orders);
			upload(localFile);
			
		}else{
			log.info("monnierfreres在该时间段内没有订单。");
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
				log.error("monnierfreres保存订单到磁盘创建目录失败："+e.getMessage(),e); 
			}
		}
		FileWriter fwriter = null;
		try {
			fwriter = new FileWriter(localFile);
			fwriter.write(data);
		} catch (IOException ex) {
			log.error("monnierfreres保存订单到磁盘失败："+ex.getMessage(),ex); 
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
			log.info("monnierfreres上传订单到ftp第"+i+"次上传开始");
			FTPClient ftp = new FTPClient(); 
			FileInputStream fis = null;
	        try {
	        	ftp.setDefaultTimeout(1000 * 60 * 5);
				ftp.setConnectTimeout(1000 * 60 * 5);
				ftp.enterLocalActiveMode();
				ftp.connect("194.213.125.81", 21);
				ftp.login("ftp_mnf_shangpin", "jobkakbonJais");
				ftp.setControlEncoding("UTF-8");
				ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
				boolean bool = ftp.changeWorkingDirectory("/SoeBusiness/Marketplaces/Shangpin/");
				if(!bool){
					ftp.makeDirectory("/SoeBusiness/Marketplaces/Shangpin/");
					ftp.changeWorkingDirectory("/SoeBusiness/Marketplaces/Shangpin/");
				}
				ftp.enterLocalPassiveMode();
				File srcFile = new File(localFile);
				fis = new FileInputStream(srcFile);
				log.info("monnierfreres上传订单连接ftp成功");
				ftp.storeFile(srcFile.getName(), fis);
				log.info("monnierfreres上传订单"+srcFile.getName()+"上传成功!!!!!!!!!!!!!!!!!");
				break;
			} catch (Exception e) {	
				log.error("monnierfreres上传订单到ftp异常："+e.getMessage(),e); 
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
	
}
