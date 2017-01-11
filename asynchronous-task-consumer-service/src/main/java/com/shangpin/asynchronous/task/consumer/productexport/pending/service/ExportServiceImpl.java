package com.shangpin.asynchronous.task.consumer.productexport.pending.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.asynchronous.task.consumer.conf.ftp.FtpProperties;
import com.shangpin.asynchronous.task.consumer.productexport.pending.vo.PendingProductDto;
import com.shangpin.asynchronous.task.consumer.productexport.pending.vo.PendingProducts;
import com.shangpin.asynchronous.task.consumer.productimport.common.util.FTPClientUtil;
import com.shangpin.ephub.client.data.mysql.enumeration.CatgoryState;
import com.shangpin.ephub.client.data.mysql.enumeration.PicState;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskCriteriaDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.client.util.TaskImportTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExportServiceImpl {
	
	private static String comma = ",";
	@Autowired
	private FtpProperties ftpProperties;
	@Autowired 
	private HubSpuImportTaskGateWay spuImportGateway;

	/**
	 * 待处理页面导出sku
	 * @param taskNo 任务编号
	 * @param products
	 */
	public void exportSku(String taskNo,PendingProducts products){
		HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("产品信息");
        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle  style = wb.createCellStyle();
        String[] row0 = TaskImportTemplate.getPendingSkuTemplate();
        for(int i= 0;i<row0.length;i++){
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(row0[i]);
            cell.setCellStyle(style);
        }
        try {
        	String[] rowTemplate = TaskImportTemplate.getPendingSkuValueTemplate();
            if(null != products && null != products.getProduts() && products.getProduts().size()>0){
                int j = 0;
                for(PendingProductDto product : products.getProduts()){
                    for(HubSkuPendingDto sku : product.getHubSkus()){
                        try {
                            j++;
                            row = sheet.createRow(j);
                            row.setHeight((short) 1500);
                            insertProductSkuOfRow(row,product,sku,rowTemplate);
                        } catch (Exception e) {
                        	log.error("insertProductSkuOfRow异常："+e.getMessage(),e);
                            j--;
                        }
                    }
                }
            }
            saveAndUploadExcel(taskNo,products.getCreateUser(),wb);
        } catch (Exception e) {
            log.error("待处理页导出sku异常："+e.getMessage(),e);
        }
	}
	/**
	 * 待处理页面导出spu
	 * @param taskNo 任务编号
	 * @param products
	 */
	public void exportSpu(String taskNo,PendingProducts products){
		HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("产品信息");
        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle  style = wb.createCellStyle();
        String[] row0 = TaskImportTemplate.getPendingSpuTemplate();
        for(int i= 0;i<row0.length;i++){
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(row0[i]);
            cell.setCellStyle(style);
        }
        try {
        	String[] rowTemplate = TaskImportTemplate.getPendingSpuValueTemplate();
            if(null != products && null != products.getProduts() && products.getProduts().size()>0){
                int j = 0;
                for(PendingProductDto product : products.getProduts()){
                    try {
                        j++;
                        row = sheet.createRow(j);  
                        row.setHeight((short) 1500);
                		sheet.setColumnWidth(0, (36*150));
                        insertProductSpuOfRow(row,product,rowTemplate);
                    } catch (Exception e) {
                    	 log.error("insertProductSpuOfRow异常："+e.getMessage(),e);
                        j--;
                    }
                }
            }
            saveAndUploadExcel(taskNo,products.getCreateUser(),wb);
        } catch (Exception e) {
            log.error("待处理页导出spu异常："+e.getMessage(),e);
        }
	}
	
	/**
	 * 上传Excel到ftp
	 * @param wb
	 */
	private void saveAndUploadExcel(String taskNo, String createUser, HSSFWorkbook wb){
		FileOutputStream fout = null;
		File file = null;
		try {
			file = new File(ftpProperties.getLocalResultPath()+createUser+"_" + taskNo+".xls");
			fout = new FileOutputStream(file);  
	        wb.write(fout); 
	        FTPClientUtil.uploadToExportPath(file, file.getName());
	        updateHubSpuImportTask(taskNo); 
		} catch (Exception e) {
			log.error("保存并上传ftp时异常："+e.getMessage(),e); 
		}finally{
			try {
				if(null != fout){
					fout.close(); 
				}
				if(null != file && file.exists()){
					file.delete();
				}
			} catch (Exception e2) {
				e2.printStackTrace(); 
			}
			
		}
	}
	/**
	 * 更新任务的状态
	 * @param taskNo
	 */
	private void updateHubSpuImportTask(String taskNo){
		HubSpuImportTaskWithCriteriaDto taskDto = new HubSpuImportTaskWithCriteriaDto();
		HubSpuImportTaskCriteriaDto criteria = new HubSpuImportTaskCriteriaDto();
		criteria.createCriteria().andTaskNoEqualTo(taskNo);
		taskDto.setCriteria(criteria);
		HubSpuImportTaskDto hubSpuTask = new HubSpuImportTaskDto();
		hubSpuTask.setTaskState((byte)TaskState.ALL_SUCCESS.getIndex());
		hubSpuTask.setUpdateTime(new Date());
		taskDto.setHubSpuImportTask(hubSpuTask); 
		spuImportGateway.updateByCriteriaSelective(taskDto);
	}
	
	/**
     * 将sku信息插入Excel的一行
     * @param row
     * @param product
     * @throws Exception
     */
    private void insertProductSkuOfRow(HSSFRow row,PendingProductDto product,HubSkuPendingDto sku,String[] rowTemplate) throws Exception{
    	Class<?> spuClazz = product.getClass();
    	Class<?> skuClazz = sku.getClass();
    	Method fieldSetMet = null;
		Object value = null;
    	for(int i=0;i<rowTemplate.length;i++){
    		try {
    			String fileName = parSetName(rowTemplate[i]);
    			if("supplierSkuNo".equals(rowTemplate[i]) || "skuName".equals(rowTemplate[i]) || "supplierBarcode".equals(rowTemplate[i]) || "supplyPrice".equals(rowTemplate[i])
            			|| "supplyPriceCurrency".equals(rowTemplate[i]) || "marketPrice".equals(rowTemplate[i]) || "marketPriceCurrencyorg".equals(rowTemplate[i]) || "hubSkuSize".equals(rowTemplate[i])){
    				//所有sku的属性
    				fieldSetMet = skuClazz.getMethod(fileName);
					value = fieldSetMet.invoke(sku);
					row.createCell(i).setCellValue(null != value ? value.toString() : "");
            	}else if("seasonYear".equals(rowTemplate[i])){
            		setRowOfSeasonYear(row, product, spuClazz, i);
            	}else if("seasonName".equals(rowTemplate[i])){
            		setRowOfSeasonName(row, product, spuClazz, i); 
            	}else if("specification".equals(rowTemplate[i]) || "originalProductSizeType".equals(rowTemplate[i]) || "originalProductSizeValue".equals(rowTemplate[i]) ){
            		//TODO 规格类型 原尺码类型 原尺码值 从哪取值？
            		row.createCell(i).setCellValue("");
            	}else{
            		//所有spu的属性
            		fieldSetMet = spuClazz.getMethod(fileName);
					value = fieldSetMet.invoke(product);
					row.createCell(i).setCellValue(null != value ? value.toString() : "");
            	}
			} catch (Exception e) {
				log.error("待处理页导出sku时异常："+e.getMessage()); 
			}        	
        }    	
    }
    /**
     * 将spu信息插入Excel的一行
     * @param row
     * @param product
     * @param rowTemplate 导入模板
     * @throws Exception
     */
    private void insertProductSpuOfRow(HSSFRow row,PendingProductDto product,String[] rowTemplate) throws Exception{		
		Class<?> cls = product.getClass();
		StringBuffer buffer = new StringBuffer();  
		Method fieldSetMet = null;
		Object value = null;
		for (int i=0;i<rowTemplate.length;i++) {
			try {
				if("spPicUrl".equals(rowTemplate[i])){
					insertImageToExcel(product.getSpPicUrl(),row, (short)i); 
				}else if("seasonYear".equals(rowTemplate[i])){
					setRowOfSeasonYear(row, product, cls, i);
				}else if("seasonName".equals(rowTemplate[i])){
					setRowOfSeasonName(row, product, cls, i); 
				}else if("memo".equals(rowTemplate[i])){
					if((null != product.getPicState() && PicState.NO_PIC.getIndex() == product.getPicState()) || (null != product.getPicState() && PicState.PIC_INFO_NOT_COMPLETED.getIndex() == product.getPicState())){
			            buffer = buffer.append("图片").append(comma);
			        }
			        if(CatgoryState.PERFECT_MATCHED.equals(product.getCatgoryState())){
			            buffer = buffer.append("品类").append(comma);
			        }
			        row.createCell(i).setCellValue(buffer.toString()); 
				}else{
					String fileName = parSetName(rowTemplate[i]);
					fieldSetMet = cls.getMethod(fileName);
					value = fieldSetMet.invoke(product);
					row.createCell(i).setCellValue(null != value ? value.toString() : "");
				}				
			} catch (Exception e) {
				log.error("待处理页导出spu时异常："+e.getMessage()); 
				continue;
			}
		}
    }	
    /**
     * 设置导出上市季节的值，这个字段比较特殊，是从hubSeason字段拆解出来的
     * @param row
     * @param product
     * @param clazz
     * @param i
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private void setRowOfSeasonName(HSSFRow row, PendingProductDto product, Class<?> clazz, int i)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		String fileName = "getHubSeason";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(product);
		row.createCell(i).setCellValue((null != value && value.toString().contains("_")) ? value.toString().split("_")[1] : (null != value ? value.toString() : ""));
	}
    /**
     * 设置导出上市年份的值，这个字段比较特殊，是从hubSeason字段拆解出来的
     * @param row
     * @param product
     * @param clazz
     * @param i
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
	private void setRowOfSeasonYear(HSSFRow row, PendingProductDto product, Class<?> clazz, int i)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		String fileName = "getHubSeason";
		Method fieldSetMet = clazz.getMethod(fileName);
		Object value = fieldSetMet.invoke(product);
		row.createCell(i).setCellValue((null != value && value.toString().contains("_")) ? value.toString().split("_")[0] : "");
	}
	
	/**
	 * 插入图片
	 * @param url 图片链接
	 * @param row Excel的一行
	 * @param startColumn 图片插入开始列
	 */
	private void insertImageToExcel(String url,HSSFRow row,short startColumn){
		BufferedImage bufferImg = null;
		try {
			if(!StringUtils.isEmpty(url)){
				bufferImg = ImageIO.read(new URL(url));
				ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();							
				ImageIO.write(bufferImg, "jpg", byteArrayOut);
				HSSFPatriarch patriarch = row.getSheet().createDrawingPatriarch();
				HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 500, 255,startColumn, row.getRowNum(), startColumn, row.getRowNum());
//				anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);
				patriarch.createPicture(anchor, row.getSheet().getWorkbook().addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
			}else{
				row.createCell((int)startColumn).setCellValue("无图片");
			}
		} catch (Exception e) {
			log.error("插入图片异常："+e.getMessage()); 
			bufferImg = null;
			row.createCell((int)startColumn).setCellValue("图片错误");
		}
	}
	
	/**
     * 构造属性的get方法，比如传入name，返回getName
     * @param fieldName
     * @return
     */
    private String parSetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		int startIndex = 0;
		if (fieldName.charAt(0) == '_')
			startIndex = 1;
		return "get" + fieldName.substring(startIndex, startIndex + 1).toUpperCase()
				+ fieldName.substring(startIndex + 1);
	}
}
