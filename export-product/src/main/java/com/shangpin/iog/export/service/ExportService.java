package com.shangpin.iog.export.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.export.utils.FTPUtils;
import com.shangpin.iog.product.dao.SupplierMapper;
import com.shangpin.iog.service.ProductSearchService;

@Component("exportService") 
public class ExportService {
	
	private static ResourceBundle bdl = null;
	private static String savepath = null;
	private static String startDate = null;
	private static String endDate = null;
	private static String ip=null;
	private static String port=null;
	private static String usrName=null;
	private static String password=null;
	private static String remotePath=null;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		savepath = bdl.getString("savepath");
		startDate = bdl.getString("startDate");
		endDate = bdl.getString("endDate");
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
		 for(SupplierDTO supplier : suppliers){
			 BufferedWriter writer = null;
			 try {
				 //查数据，存文件
				 StringBuffer productBuffer =productService.exportReportProduct(supplier.getSupplierId(),startTime,endTime,null,null);
				 File localFile = new File(savepath+File.separator+supplier.getSupplierName()+"_"+DateTimeUtil.getShortCurrentDate()+".csv");
				 localFile.getParentFile().mkdir();		
				 OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(localFile),"gb2312");        
			     writer=new BufferedWriter(write);  
			     writer.write(productBuffer.toString());  
//				 writer = new BufferedWriter(new FileWriter(localFile));
//				 writer.write(productBuffer.toString()); 
				 writer.flush();
				 //上传ftp
				 int myPort = 21;
				 if(StringUtils.isNotBlank(port)){
					 myPort = Integer.parseInt(port);
				 }
				 FTPUtils.upload(ip, myPort, usrName, password, remotePath, localFile); 
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
	} 
}
