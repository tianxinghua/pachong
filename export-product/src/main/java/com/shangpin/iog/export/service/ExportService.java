package com.shangpin.iog.export.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.product.dao.SupplierMapper;
import com.shangpin.iog.service.ProductSearchService;

@Component("exportService") 
public class ExportService {
	
	private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger
			.getLogger("error");
	private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger
			.getLogger("info");
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
	
//	private static String smtpHost = null;
//	private static String from = null;
//	private static String fromUserPassword = null;
//	private static String to = null;
//	private static String messageType = null;
	
	private static String conf_suppliers = null;
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		savepath = bdl.getString("savepath");
		startDate = bdl.getString("startDate");
		endDate = bdl.getString("endDate");
		picpath = bdl.getString("picpath");
		
//		fromUserPassword = bdl.getString("fromUserPassword");
//		from = bdl.getString("from");
//		to = bdl.getString("to");
//		messageType = bdl.getString("messageType");
//		smtpHost = bdl.getString("smtpHost");
		
		conf_suppliers = bdl.getString("conf_suppliers");
	}

	@Autowired
    SupplierMapper supplierDAO;
	@Autowired
    ProductSearchService productService;
	
	public void writeFile(){
		String filePath = "";
		 List<SupplierDTO> suppliers = new ArrayList<SupplierDTO>();
		 
		 if(StringUtils.isNotBlank(conf_suppliers)){
			 filePath = savepath+File.separator+DateTimeUtil.getShortCurrentDate()+"_specified"+File.separator;
			 for(String supplier : conf_suppliers.split(",")){				 
				 suppliers.add(supplierDAO.findBysupplierId(supplier));
			 }
		 }else{
			 filePath = savepath+File.separator+DateTimeUtil.getShortCurrentDate()+File.separator;
			 List<SupplierDTO> suppliers1 = supplierDAO.findByState("1");
			 for(SupplierDTO supplier : suppliers1){
				//排除brunarosso和Della
				 if(!"2015091801507".equals(supplier.getSupplierId()) && !"2015112001671".equals(supplier.getSupplierId())){
					 suppliers.add(supplier);
				 }
			 }
			 
//			 suppliers.addAll(supplierDAO.findByState("2"));
		 }
		 
		 Date startTime = null ;
		 if(StringUtils.isNotBlank(startDate)){
			 startTime = DateTimeUtil.parse(startDate, "yyyy-MM-dd");
		 }else{
			 startTime = DateTimeUtil.getShortDate(DateTimeUtil.getShortCurrentDate());
		 }
		 Date endTime = null;
		 if(StringUtils.isNotBlank(endDate)){
			 endTime = DateTimeUtil.parse(endDate, "yyyy-MM-dd HH:mm:ss");
		 }else{
			 endTime = new Date();
		 }
		 
		 loggerInfo.info("开始时间："+DateTimeUtil.convertFormat(startTime, "yyyy-MM-dd HH:mm:ss")+"============ 结束时间："+DateTimeUtil.convertFormat(endTime, "yyyy-MM-dd HH:mm:ss"));		 
		 System.out.println("开始时间："+DateTimeUtil.convertFormat(startTime, "yyyy-MM-dd HH:mm:ss")+"============ 结束时间："+DateTimeUtil.convertFormat(endTime, "yyyy-MM-dd HH:mm:ss"));
		 
		 for(SupplierDTO supplier : suppliers){
			 
			 try {	
				 
				 loggerInfo.info(supplier.getSupplierId()+" "+supplier.getSupplierName()+" 开始生成文件================");
				 System.out.println(supplier.getSupplierId()+" "+supplier.getSupplierName()+" 开始生成文件================");
				 String fileName = supplier.getSupplierName()+"_"+DateTimeUtil.getShortCurrentDate();
				 //查数据
				 List<ProductDTO> products = productService.findReportProduct(supplier.getSupplierId(), startTime, endTime, null, null);
				 if(null != products && products.size()>0){
					 saveCsvFile(products,filePath+fileName+".csv");
					 saveExcelFile(picpath,products,filePath+fileName+".xls");
				 }				
				 loggerInfo.info(supplier.getSupplierId()+" "+supplier.getSupplierName()+" 生成文件结束================"); 
				 System.out.println(supplier.getSupplierId()+" "+supplier.getSupplierName()+" 生成文件结束================");
			} catch (Exception e) {
				loggerInfo.info(e.toString()); 
				e.printStackTrace();
			}
		 }
		//压缩发邮件
//		 try {
//			 String zipfile  = ZipUtils.compressedFile(filePath, savepath);
//			 File zipFile = new File(savepath+File.separator+zipfile);
//			 if(zipFile.exists()){
//				 String messageText = "筛选产品列表见附件";
//				 SendMail.sendGroupMailWithFile(smtpHost, from, fromUserPassword, to, "每天推送筛选产品", messageText , messageType,zipFile);
//			 }			 
//			 
//		} catch (Exception e) {
//			e.printStackTrace();
//			loggerError.error(e.toString()); 
//		}
		 
		 
	}
	
	/**
	 * 保存csv文件
	 * @param products 产品
	 * @param filePath 保存的文件地址
	 */
	private void saveCsvFile(List<ProductDTO> products,String filePath){
		BufferedWriter writer = null;
		try {
			StringBuffer buffer = productService.reportProductToBuffer(products);
			File localFile = new File(filePath);
			 localFile.getParentFile().mkdir();		
			 OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(localFile),"gb2312");        
		     writer=new BufferedWriter(write);  
		     writer.write(buffer.toString());
			 writer.flush();
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
	
	/**
	 * 保存excel文件
	 * @param picPath 图片路径
	 * @param products 产品
	 * @param localFilePath 保存的文件地址
	 */
	private void saveExcelFile(String picPath,List<ProductDTO> products,String localFilePath){
	
		FileOutputStream fout = null;
		HSSFWorkbook wb = null;
		try {
			wb = productService.reportProductToExcel(products, picPath);			
			fout = new FileOutputStream(new File(localFilePath));  
	        wb.write(fout); 
	        
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != wb){
					wb.close();
				}
				if(null != fout){
					fout.close(); 
				}
			} catch (Exception e2) {
				e2.printStackTrace(); 
			}
			
		}
	}
	
}
