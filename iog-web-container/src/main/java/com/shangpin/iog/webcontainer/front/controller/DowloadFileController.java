package com.shangpin.iog.webcontainer.front.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.json.JsonUtil;
import com.shangpin.iog.dto.ProductSearchDTO;
import com.shangpin.iog.webcontainer.front.dto.PicUrlDTO;
import com.shangpin.iog.webcontainer.front.util.excel.ReadExcel;

/**
 * 下载图片
 * 1.上传文件下载图片
 * 2.选择供应商下载图片
 * @author sunny
 *
 */


@Controller
@RequestMapping("/download")
public class DowloadFileController {
	
	private Logger log = LoggerFactory.getLogger(DowloadFileController.class) ;
	
	private static ResourceBundle bdl = null;
	private static String tmpfffff = null;
	private static String pictmpdownloadpath = null;
	private static String data_product = null;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		tmpfffff = bdl.getString("tmpfffff");
		pictmpdownloadpath = bdl.getString("pictmpdownloadpath");
		
		data_product = bdl.getString("data_product");
		
	}

	@RequestMapping("uploadAndDownPics") 
	public void uploadAndDownPics(@RequestParam(value = "tmpfffff", required = false) MultipartFile file,HttpServletResponse response){
		
		//统计下载失败的链接.
		BufferedWriter writer = null;
		
		BufferedInputStream in = null;
    	BufferedOutputStream out = null;    	
    	ZipFile zipfile = null;
    	if(null != file){    	
	    	String fileName = file.getOriginalFilename();  
	        File targetFile = new File(tmpfffff, fileName);  
	        ArrayList<File> filesToAdd = new ArrayList<File>();
	        try {
	        	//先创建一个新的log文件，用于记录下载情况
	        	File logFile = new File(tmpfffff+File.separator+"piclog_"+new Date().getTime()+".log");
	        	writer = new BufferedWriter(new FileWriter(logFile));  
	        	writer.write("===============开始记录日志=================\r\n"); 
	        	
	        	if (!targetFile.exists()) {
	        		targetFile.mkdirs(); 
	        		targetFile.createNewFile();
				}
	            file.transferTo(targetFile);  
	            List<PicUrlDTO> picUrlDTOList =  ReadExcel.readExcel(PicUrlDTO.class, targetFile.getPath());
	            if(null != picUrlDTOList && picUrlDTOList.size()>0){
	            	Map<String,String> ruleUrlMap = new HashMap<>();
//	            	List<String> ruleUrlList = new ArrayList<String>();
	            	for(PicUrlDTO pic : picUrlDTOList){
//	            		ruleUrlList.add(pic.getUrl());
	            		ruleUrlMap.put(pic.getUrl(), "");
	            	}
	            	//查找图片目录下有没有该供应商文件夹
	            	File filef = new File(pictmpdownloadpath);
	        		String theSupplier = null;
	        		String[] havSupplierIds = filef.list();
	        		if(null != havSupplierIds && havSupplierIds.length>0){
	        			for(String havSupplierId : havSupplierIds){
	        				if(havSupplierId.equals(picUrlDTOList.get(0).getSupplierId())){
	        					theSupplier = havSupplierId;
	        					break;
	        				}
	        			}
	        		}else{
	        			writer.write(pictmpdownloadpath+"下无文件！！！！\r\n"); 
	        		}
	        		System.out.println("需要下载的供应商supplierId=================="+picUrlDTOList.get(0).getSupplierId());
	        		log.error("需要下载的供应商supplierId=================="+picUrlDTOList.get(0).getSupplierId()); 
	        		System.out.println("找到对应的文件夹的名称========================"+theSupplier); 
	        		log.error("找到对应的文件夹的名称========================"+theSupplier);
	        		//在选定的日期目录中查找符合规则的文件
	        		String tmpFileName="";
	        		Map<String,String> findMap = new HashMap<>();
	        		if(org.apache.commons.lang.StringUtils.isNotBlank(theSupplier)){
	        			File mySupplierFile = new File(pictmpdownloadpath+File.separator+theSupplier);    			
	        			if(null != mySupplierFile.list() && mySupplierFile.list().length>0){
	        				for(String dirName : mySupplierFile.list()){
	        					File ffff = new File(pictmpdownloadpath+File.separator+theSupplier+File.separator+dirName);
	            				File[] picFiles = ffff.listFiles();
	        					if(null != picFiles && picFiles.length>0){
	        						for(int i= 0;i<picFiles.length;i++){
	        							tmpFileName = picFiles[i].getName();
	        							if(ruleUrlMap.containsKey(tmpFileName.substring(0,tmpFileName.indexOf(" ")).trim())){
	        								filesToAdd.add(picFiles[i]);
	        								findMap.put(tmpFileName.substring(0,tmpFileName.indexOf(" ")).trim(), "");
	        							}
	        						}
//	            					if(null != ruleUrlList && ruleUrlList.size()>0){	            						
//	            						for(String ruleName : ruleUrlList){
//	            							for(int i= 0;i<picFiles.length;i++){
//	            								if(picFiles[i].getName().contains(ruleName)){
//	            									filesToAdd.add(picFiles[i]);
////	            									break;
//	            								}else if(i == picFiles.length-1){
//	            									writer.write(ruleName+"  没有找到对应的图片\r\n"); 
//	            								}
//	            							}
//	            						}
//	            					}
	            				}
	        					
	        				}
	        				for(String fileKey:ruleUrlMap.keySet() ){
	        					if(findMap.containsKey(fileKey)){
	        						
	        					}else{
	        						writer.write(fileKey+"  没有找到对应的图片\r\n"); 
	        					}
	        				}
	        			}else{
	        				writer.write(theSupplier+"文件夹下没有找到文件或文件夹！！！！\r\n"); 
	        			}  	
	        		}else{
	        			writer.write("未找到名称为 "+picUrlDTOList.get(0).getSupplierId()+" 的文件夹=================\r\n"); 
	        		}
	        		
	        		System.out.println("filesToAdd=========================="+filesToAdd.size()); 
	        		log.error("filesToAdd=========================="+filesToAdd.size());	        		
	            }else{
	            	writer.write("解析上传的规则文件时出错，下载图片失败");
	            }
	            writer.flush();
        		filesToAdd.add(logFile); 
        		zipfile = new ZipFile(new File(new Date().getTime()+""));
            	
    			ZipParameters parameters = new ZipParameters();  
    		    parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
    		    parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);  
    			try {
    				zipfile.addFiles(filesToAdd, parameters);
				} catch (Exception e) {
					e.printStackTrace();
					log.error(e.toString());
				}
    		    
    			response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode("picture"+new Date().getTime()+".zip", "UTF-8"));

    			in = new BufferedInputStream(new FileInputStream(zipfile.getFile()));

                out = new BufferedOutputStream(response.getOutputStream());
                byte[] data = new byte[1048576];
                int len = 0;
                while (-1 != (len=in.read(data, 0, data.length))) {
                    out.write(data, 0, len);
                }
        		
	        } catch (Exception e) {  
	            e.printStackTrace();  
	            log.error(e.toString());
	        }finally{
				try {
					if(null != in){
						in.close();
					}
					if(null != out){
						out.close();
					}	
					if(null != writer){
						writer.close(); 
					}
					zipfile.getFile().delete();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
    	}
    	
    	
	}
	
	@RequestMapping("downloadpicBySupplier")
	public void downloadpicBySupplier(HttpServletResponse response, String queryJson){
		
		BufferedWriter writer = null;
		
		BufferedInputStream in = null;
    	BufferedOutputStream out = null;
    	
		ProductSearchDTO productSearchDTO = (ProductSearchDTO) JsonUtil.getObject4JsonString(queryJson, ProductSearchDTO.class);    	
    	if(null==productSearchDTO) productSearchDTO = new ProductSearchDTO();
        String supplier = null;
        if(!StringUtils.isEmpty(productSearchDTO.getSupplier()) && !productSearchDTO.getSupplier().equals("-1")){
        	supplier = productSearchDTO.getSupplier();
        }
        Date startDate  =null;
        if(!StringUtils.isEmpty(productSearchDTO.getStartDate())){
            startDate =  DateTimeUtil.convertFormat(productSearchDTO.getStartDate(),"yyyy-MM-dd");
        }
        Date endDate = null;
        if(!StringUtils.isEmpty(productSearchDTO.getEndDate())){
            endDate= DateTimeUtil.convertFormat(productSearchDTO.getEndDate(), "yyyy-MM-dd");
        }    
       
        ZipFile zipfile = null;
        try {
        	//先创建一个新的log文件，用于记录下载情况
        	File logFile = new File(tmpfffff+File.separator+"piclog_"+new Date().getTime()+".log");
        	writer = new BufferedWriter(new FileWriter(logFile));  
        	writer.write("===============开始记录日志=================\r\n"); 
        	
    		//查找图片目录下有没有该供应商文件夹
    		File file = new File(pictmpdownloadpath);
    		String theSupplier = null;
    		String[] havSupplierIds = file.list();
    		if(null != havSupplierIds && havSupplierIds.length>0){
    			for(String havSupplierId : havSupplierIds){
    				if(havSupplierId.equals(supplier)){
    					theSupplier = havSupplierId;
    					break;
    				}
    			}
    		}else{
    			writer.write(pictmpdownloadpath+"下无文件！！！！\r\n"); 
    		}
    		System.out.println("需要下载的供应商supplierId=================="+supplier);
    		log.error("需要下载的供应商supplierId=================="+supplier); 
    		System.out.println("找到对应的文件夹的名称========================"+theSupplier); 
    		log.error("找到对应的文件夹的名称========================"+theSupplier);    		
    		//按照选择的日期选择文件夹
    		List<String> myDirs = new ArrayList<String>();
    		if(org.apache.commons.lang.StringUtils.isNotBlank(theSupplier)){
    			File mySupplierFile = new File(pictmpdownloadpath+File.separator+theSupplier);    			
    			if(null != mySupplierFile.list() && mySupplierFile.list().length>0){
    				if(null != startDate && null != endDate){
    					for(String localDir : mySupplierFile.list()){
    						try {
    							if(startDate.getTime() <= DateTimeUtil.convertFormat(localDir, "yyyy-MM-dd").getTime() && DateTimeUtil.convertFormat(localDir, "yyyy-MM-dd").getTime()<=endDate.getTime()){
            						myDirs.add(localDir);
            					}
							} catch (Exception e) {
								e.printStackTrace();
							}        					
        				}
    				}else if(null != startDate && null == endDate){
    					File dayFile = new File (pictmpdownloadpath+File.separator+theSupplier+File.separator+DateTimeUtil.strForDateNew(startDate));
    					if(dayFile.exists()){
    						myDirs.add(dayFile.getName());
    					}else{
    						writer.write("未找到 "+DateTimeUtil.strForDateNew(startDate)+" 的文件夹=================\r\n");
    					}
    				}else if(null != endDate && null == startDate){
    					File dayFile = new File (pictmpdownloadpath+File.separator+theSupplier+File.separator+DateTimeUtil.strForDateNew(endDate));
    					if(dayFile.exists()){
    						myDirs.add(dayFile.getName());
    					}else{
    						writer.write("未找到 "+DateTimeUtil.strForDateNew(endDate)+" 的文件夹=================\r\n");
    					}
    				}else{
    					Collections.addAll(myDirs, mySupplierFile.list());
    				}    				
    			}    			
    		}else{
    			writer.write("未找到名称为 "+supplier+" 的文件夹=================\r\n");
    		}
    		System.out.println("myDirs=========================="+myDirs.size());
    		//在选定的日期目录中添加文件
    		ArrayList<File> filesToAdd = new ArrayList<File>();
    		if(null != myDirs && myDirs.size()>0){
    			for(String dirName : myDirs){
    				File ffff = new File(pictmpdownloadpath+File.separator+theSupplier+File.separator+dirName);
    				if(null != ffff.listFiles() && ffff.listFiles().length>0){    					
    					Collections.addAll(filesToAdd, ffff.listFiles());    					
    				}
    			}
    		}
    		
    		writer.flush();
    		filesToAdd.add(logFile); 
    		System.out.println("filesToAdd=========================="+filesToAdd.size()); 
    		zipfile = new ZipFile(new File(new Date().getTime()+""));
        	
			ZipParameters parameters = new ZipParameters();  
		    parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		    parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);  
			zipfile.addFiles(filesToAdd, parameters);
			response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode("picture"+new Date().getTime()+".zip", "UTF-8"));

			in = new BufferedInputStream(new FileInputStream(zipfile.getFile()));

            out = new BufferedOutputStream(response.getOutputStream());
            byte[] data = new byte[1048576];
            int len = 0;
            while (-1 != (len=in.read(data, 0, data.length))) {
                out.write(data, 0, len);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != in){
					in.close();
				}
				if(null != out){
					out.close();
				}		
				if(null != writer){
					writer.close();
				}
				zipfile.getFile().delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	@RequestMapping("exporteveryday")
	public void download(HttpServletResponse response, String queryJson){
		
		BufferedWriter writer = null;
		
		BufferedInputStream in = null;
    	BufferedOutputStream out = null;
    	
		ProductSearchDTO productSearchDTO = (ProductSearchDTO) JsonUtil.getObject4JsonString(queryJson, ProductSearchDTO.class);    	
    	if(null==productSearchDTO) productSearchDTO = new ProductSearchDTO();
        
        Date startDate  =null;
        if(!StringUtils.isEmpty(productSearchDTO.getStartDate())){
            startDate =  DateTimeUtil.convertFormat(productSearchDTO.getStartDate(),"yyyy-MM-dd");
        }
        Date endDate = null;
        if(!StringUtils.isEmpty(productSearchDTO.getEndDate())){
            endDate= DateTimeUtil.convertFormat(productSearchDTO.getEndDate(), "yyyy-MM-dd");
        }    
       
        ZipFile zipfile = null;
        try {
        		
    		//按照选择的日期选择文件夹
    		List<String> myDirs = new ArrayList<String>();    		
			File mySupplierFile = new File(data_product);
			ArrayList<File> filesToAdd = new ArrayList<File>();
			if(null != startDate && null != endDate){
				for(File localFile : mySupplierFile.listFiles()){
					try {
						if(localFile.isFile()){
							String fileDate = "";
							String fileName = localFile.getName();
							if(fileName.contains("specified")){
								fileDate = fileName.substring(fileName.indexOf("_")+1, fileName.lastIndexOf("_"));
							}else{
								fileDate = fileName.substring(fileName.indexOf("_")+1, fileName.lastIndexOf("."));
							}
							if(startDate.getTime() <= DateTimeUtil.convertFormat(fileDate, "yyyy-MM-dd").getTime() && DateTimeUtil.convertFormat(fileDate, "yyyy-MM-dd").getTime()<=endDate.getTime()){
								filesToAdd.add(localFile);
	    					}
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}        					
				}
			}else if(null != startDate && null == endDate){
				for(File localFile : mySupplierFile.listFiles()){
					if(localFile.isFile() && localFile.getName().contains(DateTimeUtil.convertFormat(startDate, "yyyy-MM-dd"))){
						filesToAdd.add(localFile);
					}
				}
			}else if(null != endDate && null == startDate){
				for(File localFile : mySupplierFile.listFiles()){
					if(localFile.isFile() && localFile.getName().contains(DateTimeUtil.convertFormat(endDate, "yyyy-MM-dd"))){
						filesToAdd.add(localFile);
					}
				}
			}
			
    		System.out.println("filesToAdd=========================="+filesToAdd.size()); 
    		if(filesToAdd.size() == 0){    			
    			File logFile = new File(tmpfffff+File.separator+"productLog_"+new Date().getTime()+".log");
            	writer = new BufferedWriter(new FileWriter(logFile));  
            	writer.write("=================没有文件========================"); 
            	writer.flush();
            	filesToAdd.add(logFile);
    		}
    		zipfile = new ZipFile(new File(new Date().getTime()+""));
        	
			ZipParameters parameters = new ZipParameters();  
		    parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		    parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);  
			zipfile.addFiles(filesToAdd, parameters);
			response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode("product"+new Date().getTime()+".zip", "UTF-8"));

			in = new BufferedInputStream(new FileInputStream(zipfile.getFile()));

            out = new BufferedOutputStream(response.getOutputStream());
            byte[] data = new byte[1048576];
            int len = 0;
            while (-1 != (len=in.read(data, 0, data.length))) {
                out.write(data, 0, len);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != in){
					in.close();
				}
				if(null != out){
					out.close();
				}
				if(null != writer){
					writer.close();
				}
				zipfile.getFile().delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
