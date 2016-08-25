package com.shangpin.iog.product.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.product.dao.ProductsMapper;
import com.shangpin.iog.product.dao.SpuMapper;
import com.shangpin.iog.product.dto.CategoryDTO;
import com.shangpin.iog.product.util.excel.ReadExcel;

@Service
public class HandleProductService {

	private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger
			.getLogger("info");
	private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger
			.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String excelPath = null;
	private static String processed = null;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		excelPath = bdl.getString("excel_path");
		processed = bdl.getString("processed");
	}
	
	@Autowired
	ProductsMapper productDAO;
	@Autowired
    SpuMapper spuDAO;
	
	public void handleProduct(){
		//解析文件
		File file = new File(excelPath);
		if(!file.exists()){
			file.mkdir();
		}	
		File[] files  = file.listFiles();		
		//查找category		
		if(null != files && files.length>0){
			ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 300, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(10),new ThreadPoolExecutor.CallerRunsPolicy());
			for(File excelFile : files){
				try {
					String supplierId = excelFile.getName().substring(0, excelFile.getName().indexOf("_")); 
					Date startTime = DateTimeUtil.getDateFomate("2015-01-01 00:00:00");
					Date endTime = new Date();	
					executor.execute(new Handler(supplierId,startTime,endTime,excelFile)); 
				} catch (Exception e) {
					e.printStackTrace();
					loggerError.error(e.toString()); 
				}
				
				
			}
		}
	}
	
	class Handler extends TimerTask{
		
		private String supplierId;
		private Date startTime;
		private Date endTime;
		private File categoryFile;
		
		public Handler(String supplierId,Date startTime,Date endTime,File categoryFile){
			this.supplierId = supplierId;
			this.startTime = startTime;
			this.endTime = endTime;
			this.categoryFile = categoryFile;
		}
		
		@Override
		public void run() {
			handleProductBySupplier(supplierId,startTime,endTime,categoryFile);
		}
		
		private void handleProductBySupplier(String supplier,Date startTime,Date endTime,File categoryFile){
			
			//品类分析
			List<CategoryDTO> categories = null;
			try {
				categories = ReadExcel.readExcel(CategoryDTO.class, categoryFile.getPath());
				loggerInfo.info("解析到的品类List大小================"+categories.size());
				System.out.println("解析到的品类List大小================"+categories.size());
			} catch (Exception e) {
				e.printStackTrace();
				loggerError.error(e.toString()); 
			}
			//TODO 查找Brand
			
			//赋值更新			
			List<SpuDTO> productList = spuDAO.findAllBySupplierId(supplier, startTime, endTime);
			loggerInfo.info(supplier+" 查找到的产品列表大小是=============="+productList.size()); 
			System.out.println(supplier+" 查找到的产品列表大小是=============="+productList.size()); 
			for(SpuDTO product : productList){
				try {
					SpuDTO spu = new SpuDTO();
					spu.setSupplierId(supplier);
					spu.setSpuId(product.getSpuId()); 
					//给spCategory赋值
					if(null != categories && categories.size()>0){
						CategoryDTO theCategory = null;
						for(CategoryDTO category : categories){
							if(category.getSupplierCategoryName().toUpperCase().equals(null != product.getSubCategoryName() ? product.getSubCategoryName().toUpperCase() : "") || category.getSupplierCategoryName().toUpperCase().equals(null != product.getCategoryName() ? product.getCategoryName().toUpperCase() : "")){
								if(StringUtils.isNotBlank(category.getGender()) && category.getGender().equals(product.getCategoryGender())){
									theCategory = category;
									break;
								}
//								else if(StringUtils.isNotBlank(category.getColor()) && category.getColor().equals(product.get)){
//									theCategory = category;
//									break;
//								}else if(StringUtils.isNotBlank(category.getBrand()) && category.getBrand().equals(product.getBrandName())){
//									theCategory = category;
//									break;
//								}else{
//									theCategory = category;
//									break;
//								}
							}
						}
						if(null != theCategory){
							if(StringUtils.isNotBlank(theCategory.getCategoryId())){
								spu.setSpCategory(theCategory.getCategoryId());
							}else if(StringUtils.isNotBlank(theCategory.getFortCategory())){
								spu.setSpCategory(theCategory.getFortCategory());
							}else if(StringUtils.isNotBlank(theCategory.getThirCategory())){
								spu.setSpCategory(theCategory.getThirCategory());
							}else if(StringUtils.isNotBlank(theCategory.getSecCategory())){
								spu.setSpCategory(theCategory.getSecCategory());
							}else if(StringUtils.isNotBlank(theCategory.getFirCategory())){
								spu.setSpCategory(theCategory.getFirCategory());
							}					 
						}	
					}					
								
					//更新
					spuDAO.updateSpCategoryAndBrand(spu); 
				} catch (Exception e) {
					e.printStackTrace();
					loggerError.error(e.getMessage()); 
				}
				
			}
			
			//处理完的文件移到processed文件夹
			mvFile(processed+File.separator+categoryFile.getName(),categoryFile);
			
		}
		
		private void mvFile(String newFilePath,File file){
			try {
				File newFile = new File(newFilePath);
				if(!newFile.getParentFile().exists()){
					newFile.getParentFile().mkdir();
				}
				file.renameTo(newFile);
				file.delete();				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
}
