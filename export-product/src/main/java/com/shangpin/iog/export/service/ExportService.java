package com.shangpin.iog.export.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.export.utils.FTPUtils;
import com.shangpin.iog.export.utils.ZipUtils;
import com.shangpin.iog.product.dao.SupplierMapper;
import com.shangpin.iog.service.ProductSearchService;

@Component("exportService") 
public class ExportService {
	
	private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger
			.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String savepath = null;
	private static String startDate = null;
	private static String endDate = null;
//	private static String ip=null;
//	private static String port=null;
//	private static String usrName=null;
//	private static String password=null;
//	private static String remotePath=null;
	private static String picpath = null;
	
	private static String smtpHost = null;
	private static String from = null;
	private static String fromUserPassword = null;
	private static String to = null;
	private static String messageType = null;
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		savepath = bdl.getString("savepath");
		startDate = bdl.getString("startDate");
		endDate = bdl.getString("endDate");
		picpath = bdl.getString("picpath");
		
		fromUserPassword = bdl.getString("fromUserPassword");
		from = bdl.getString("from");
		to = bdl.getString("to");
		messageType = bdl.getString("messageType");
		smtpHost = bdl.getString("smtpHost");
	}

	@Autowired
    SupplierMapper supplierDAO;
	@Autowired
    ProductSearchService productService;
	
	public void writeFile(){
		
		 List<SupplierDTO> suppliers = supplierDAO.findByState("1");
		 
		 //2016-08-26
		 Date startTime = null ;
		 if(StringUtils.isNotBlank(startDate)){
			 startTime = DateTimeUtil.parse(startDate, "yyyy-MM-dd");
		 }else{
			 DateTimeUtil.getShortDate(DateTimeUtil.getShortCurrentDate());
		 }
		 Date endTime = null;
		 if(StringUtils.isNotBlank(endDate)){
			 endTime = DateTimeUtil.parse(endDate, "yyyy-MM-dd HH:mm:ss");
		 }else{
			 endTime = new Date();
		 }
		 String filePath = savepath+File.separator+DateTimeUtil.getShortCurrentDate()+File.separator;
		 for(SupplierDTO supplier : suppliers){
			 BufferedWriter writer = null;
			 try {				
				 String fileName = supplier.getSupplierName()+"_"+DateTimeUtil.getShortCurrentDate();
				 //查数据，生成csv文件
				 StringBuffer productBuffer =productService.exportReportProduct(supplier.getSupplierId(),startTime,endTime,null,null);
				 File localFile = new File(filePath+fileName+".csv");
				 localFile.getParentFile().mkdir();		
				 OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(localFile),"gb2312");        
			     writer=new BufferedWriter(write);  
			     writer.write(productBuffer.toString());
				 writer.flush();
				 //生成带图片的excel文件
				 productService.exportAndSaveReportProduct(picpath,supplier.getSupplierId(),startTime,endTime,null,null,filePath+fileName+".xls");
				 		 
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if(null != writer){
						writer.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}				
			}
			 
		 }
		//压缩发邮件
		 try {
			 String zipfile  = ZipUtils.compressedFile(filePath, savepath);
			 File zipFile = new File(savepath+File.separator+zipfile);
			 if(zipFile.exists()){
				 String messageText = "筛选产品列表见附件";
				 SendMail.sendGroupMailWithFile(smtpHost, from, fromUserPassword, to, "每天推送筛选产品", messageText , messageType,zipFile);
			 }			 
			 
		} catch (Exception e) {
			e.printStackTrace();
			loggerError.error(e.toString()); 
		}
		 
	} 
}