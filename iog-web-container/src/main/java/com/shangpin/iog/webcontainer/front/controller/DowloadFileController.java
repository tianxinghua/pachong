package com.shangpin.iog.webcontainer.front.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
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

@Controller
@RequestMapping("/download")
public class DowloadFileController {
	
	private static ResourceBundle bdl = null;
	private static String tmpfffff = null;
	private static String pictmpdownloadpath = null;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		tmpfffff = bdl.getString("tmpfffff");
		pictmpdownloadpath = bdl.getString("pictmpdownloadpath");
		
	}

	@RequestMapping("uploadAndDownPics") 
	public void uploadAndDownPics(@RequestParam(value = "tmpfffff", required = false) MultipartFile file,HttpServletResponse response){
		
		BufferedInputStream in = null;
    	BufferedOutputStream out = null;    	
    	ZipFile zipfile = null;
    	if(null != file){    	
	    	String fileName = file.getOriginalFilename();  
	        File targetFile = new File(tmpfffff, fileName);  
	        //保存  
	        try {  
	        	if (!targetFile.exists()) {
	        		targetFile.mkdirs(); 
	        		targetFile.createNewFile();
				}
	            file.transferTo(targetFile);  
	            List<PicUrlDTO> picUrlDTOList =  ReadExcel.readExcel(PicUrlDTO.class, targetFile.getPath());
	            if(null != picUrlDTOList && picUrlDTOList.size()>0){
	            	List<String> ruleUrlList = new ArrayList<String>();
	            	for(PicUrlDTO pic : picUrlDTOList){
	            		ruleUrlList.add(pic.getUrl());
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
	        		}
	        		System.out.println("theSupplier========================"+theSupplier); 
	        		//在选定的日期目录中查找符合规则的文件
	        		ArrayList<File> filesToAdd = new ArrayList<File>();
	        		if(org.apache.commons.lang.StringUtils.isNotBlank(theSupplier)){
	        			File mySupplierFile = new File(pictmpdownloadpath+File.separator+theSupplier);    			
	        			if(null != mySupplierFile.list() && mySupplierFile.list().length>0){
	        				for(String dirName : mySupplierFile.list()){
	        					File ffff = new File(pictmpdownloadpath+File.separator+theSupplier+File.separator+dirName);
	            				if(null != ffff.listFiles() && ffff.listFiles().length>0){
	            					if(null != ruleUrlList && ruleUrlList.size()>0){
	            						for(File f : ffff.listFiles()){
	            							for(String ruleName : ruleUrlList){
	            								if(f.getName().contains(ruleName)){
	            									filesToAdd.add(f);
	            									break;
	            								}
	            							}    							
	            						}
	            					}
	            				}
	        				}     								
	        			}    	
	        		}
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
					zipfile.getFile().delete();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
    	}
    	
    	
	}
	
	@RequestMapping("downloadpicBySupplier")
	public void downloadpicBySupplier(HttpServletResponse response, String queryJson){
		
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
        List<String> ruleUrlList = new ArrayList<String>();
        try {
        	//查找有没有图片名称规则文件
//        	try {
//        		File file = new File(tmpfffff);
//            	if(!file.exists()){
//            		file.mkdir();
//            	}
//            	File[] files = file.listFiles();
//            	if(null != files && files.length>0){
//            		for(File ruleFile : files){
//            			if(ruleFile.getName().contains(supplier)){
//            				List<PicUrlDTO> picUrlDTOList =  ReadExcel.readExcel(PicUrlDTO.class, ruleFile.getPath());
//            				for(PicUrlDTO pic : picUrlDTOList){
//            					ruleUrlList.add(pic.getUrl());
//            				}
//            				break;
//            			}
//            		}
//            	}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}  
//        	System.out.println("ruleUrlList===================="+ruleUrlList.size()); 
        	
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
    		}
    		System.out.println("supplier================="+supplier); 
    		System.out.println("theSupplier========================="+theSupplier); 
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
    				}else{
    					Collections.addAll(myDirs, mySupplierFile.list());
    				}    				
    			}    			
    		}
    		System.out.println("myDirs=========================="+myDirs.size());
    		//在选定的日期目录中查找符合规则的文件
    		ArrayList<File> filesToAdd = new ArrayList<File>();
    		if(null != myDirs && myDirs.size()>0){
    			for(String dirName : myDirs){
    				File ffff = new File(pictmpdownloadpath+File.separator+theSupplier+File.separator+dirName);
    				if(null != ffff.listFiles() && ffff.listFiles().length>0){
    					if(null != ruleUrlList && ruleUrlList.size()>0){
    						for(File f : ffff.listFiles()){
    							for(String ruleName : ruleUrlList){
    								if(f.getName().contains(ruleName)){
    									filesToAdd.add(f);
    									break;
    								}
    							}    							
    						}
    					}else{
    						Collections.addAll(filesToAdd, ffff.listFiles());
    					}
    				}
    			}
    		}
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
				zipfile.getFile().delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
