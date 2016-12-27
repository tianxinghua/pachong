package com.shangpin.asynchronous.task.consumer.productimport.pending.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.asynchronous.task.consumer.productimport.common.TaskService;
import com.shangpin.asynchronous.task.consumer.productimport.hub.util.ExportExcelUtils;
import com.shangpin.asynchronous.task.consumer.productimport.hub.util.FTPClientUtil;
import com.shangpin.asynchronous.task.consumer.productimport.pending.dto.HubPendingProductImportDTO;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;
import com.shangpin.ephub.client.product.business.hubpending.sku.dto.HubPendingSkuDto;
import com.shangpin.ephub.client.product.business.hubpending.sku.gateway.HubPendingSkuCheckGateWay;
import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.client.product.business.hubpending.spu.dao.HubPendingSpuDto;
import com.shangpin.ephub.client.product.business.hubpending.spu.gateway.HubPendingSpuCheckGateWay;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.HubPendingSpuCheckResult;
import com.shangpin.ephub.client.util.TaskImportTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Title:SupplierOrderService.java
 * </p>
 * <p>
 * Description: 任务队列消费
 * </p>
 * <p>
 * Company: www.shangpin.com
 * </p>	
 * 
 * @author zhaogenchun
 * @date 2016年11月23日 下午4:06:52
 */
@SuppressWarnings("rawtypes")
@Service
@Slf4j
public class PendingProductImportService {
	
	@Autowired
	HubSpuImportTaskGateWay spuImportGateway;

	@Autowired
	HubSkuPendingGateWay hubSkuPendingGateWay;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	HubSpuPendingGateWay hubSpuPendingGateWay;
	
	@Autowired
	HubPendingSkuCheckGateWay pendingSkuCheckGateWay;
	@Autowired
	HubPendingSpuCheckGateWay pendingSpuCheckGateWay;
	
	private static String[] pendingSkuTemplate = null;
	static {
		pendingSkuTemplate = TaskImportTemplate.getPendingSkuTemplate();
	}
	
	//1、更新任务表，把task_state更新成正在处理  2、从ftp下载文件并解析成对象   3、公共类校验hub数据并把校验结果写入excel   4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
	public void handMessage(ProductImportTask task) throws Exception{
		
		//1、更新任务表，把task_state更新成正在处理
		taskService.updateHubSpuImportStatusByTaskNo(TaskState.HANDLEING.getIndex(),task.getTaskNo(),null);
		log.info("任务编号："+task.getTaskNo()+"状态更新成正在处理");
		// 2、从ftp下载文件并解析成对象
		List<HubPendingProductImportDTO> listHubProduct = handleHubExcel(task.getTaskFtpFilePath(),task.getTaskNo());
		
		//3、公共类校验hub数据并把校验结果写入excel
		 List<Map<String, String>> result = checkAndsaveHubPendingProduct(task.getTaskNo(),listHubProduct);
		 
		//4、处理结果的excel上传ftp，并更新任务表状态和文件在ftp的路径
		 convertExcel(result,task.getTaskNo());
	}

	private void convertExcel(List<Map<String, String>> result, String taskNo) throws Exception{
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String resultFileName = sim.format(new Date());
		FileOutputStream out = new FileOutputStream(new File(resultFileName+".xlsx"));
		
		String[] headers = { "任务编号", "货号", "任务状态", "任务说明"};
		String[] columns = { "taskNo", "spuModel","taskState", "processInfo"};
		ExportExcelUtils.exportExcel(resultFileName, headers, columns, result,
				out);
		//4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
		String path = FTPClientUtil.uploadFile(new File(resultFileName+".xlsx"), resultFileName+".xlsx");
		
		int status;
		if(result!=null&&result.size()>0){
			//部分成功
			status = TaskState.SOME_SUCCESS.getIndex();
		}else{
			//全部成功
			status = TaskState.ALL_SUCCESS.getIndex();
		}
		taskService.updateHubSpuImportByTaskNo(taskNo,path+resultFileName+".xlsx",status);
		
	}

	//校验数据以及保存到hub表
	private List<Map<String, String>> checkAndsaveHubPendingProduct(String taskNo,List<HubPendingProductImportDTO> listHubProduct) {

		List<Map<String, String>> listMap = new ArrayList<Map<String,String>>();
		for(HubPendingProductImportDTO product:listHubProduct){
			
			Map<String, String> map = new HashMap<String, String>();
			//处理spu信息
			
			updateOrSaveSku(product);
			
			HubPendingSpuCheckResult hubPendingSpuCheckResult = handlePendingSpu(product);
			
			
			if(hubPendingSpuCheckResult.isPassing()==true){
				log.info(taskNo+"spu校验通过");
				//校验sku信息
				HubPendingSkuCheckResult hubPendingSkuCheckResult = handlePendingSku(product);
				if (hubPendingSkuCheckResult.isPassing() == true) {
					log.info(taskNo+"sku校验通过");
					updateOrSaveSku(product);
					
					updateOrSaveSpu(product);
					
					map.put("taskNo",taskNo);
					map.put("spuModel",product.getSpuModel());
					map.put("taskState","校验成功");
					map.put("processInfo","校验通过");
				}else {
					log.info(taskNo+"sku校验不通过");
					map.put("taskNo", taskNo);
					map.put("spuModel", product.getSpuModel());
					map.put("taskState", "校验失败");
					map.put("processInfo", hubPendingSkuCheckResult.getResult());
				}
			}else {
				log.info(taskNo+"spu校验不通过");
				map.put("taskNo", taskNo);
				map.put("spuModel", product.getSpuModel());
				map.put("taskState", "校验失败");
				map.put("processInfo", hubPendingSpuCheckResult.getResult());
			}
			
			listMap.add(map);
		}
		return listMap;
	}

	
	private void updateOrSaveSpu(HubPendingProductImportDTO product) {
		//查询数据库spu信息是否已存在，存在则更新，反之插入
		HubSpuPendingDto hubSpuPendingDto = convertHubPendingProduct2Spu(product);
		Long ll = hubSpuPendingDto.getSpuPendingId();
		Long spuId = findHubSpuPending(ll);
		if(spuId!=null){
			hubSpuPendingDto.setSpuPendingId(spuId);
			hubSpuPendingGateWay.updateByPrimaryKey(hubSpuPendingDto);
		}else{
			hubSpuPendingGateWay.insert(hubSpuPendingDto);	
		}
	}

	private void updateOrSaveSku(HubPendingProductImportDTO product) {
		product.setSupplierId("201612271520");
		HubSkuPendingDto hubSkuPendingDto = convertHubPendingProduct2Sku(product);
		//查询数据库sku信息是否已存在
		HubSkuPendingDto hubSkuPendingTempDto = findHubSkuPending(hubSkuPendingDto.getSupplierId(),hubSkuPendingDto.getSupplierSkuNo());
		if(hubSkuPendingTempDto!=null){
			hubSkuPendingDto.setSkuPendingId(hubSkuPendingTempDto.getSkuPendingId());
			hubSkuPendingGateWay.updateByPrimaryKey(hubSkuPendingDto);
		}else{
			hubSkuPendingGateWay.insert(hubSkuPendingDto);	
		}
		
	}

	private HubPendingSkuCheckResult handlePendingSku(HubPendingProductImportDTO product) {
		HubPendingSkuDto HubPendingSkuDto = convertHubPendingProduct2PendingSku(product);
		return pendingSkuCheckGateWay.checkSku(HubPendingSkuDto);
	}

	private HubPendingSpuCheckResult handlePendingSpu(HubPendingProductImportDTO product) {
		//校验spu信息
		HubPendingSpuDto HubPendingSpuDto = convertHubPendingProduct2PendingSpu(product);
		return pendingSpuCheckGateWay.checkSpu(HubPendingSpuDto);
		
	}

	private HubPendingSpuDto convertHubPendingProduct2PendingSpu(
			HubPendingProductImportDTO product) {
		HubPendingSpuDto HubPendingSpuDto = new HubPendingSpuDto();
		BeanUtils.copyProperties(product, HubPendingSpuDto);
		return HubPendingSpuDto;
	}

	private HubPendingSkuDto convertHubPendingProduct2PendingSku(
			HubPendingProductImportDTO product) {
		HubPendingSkuDto HubPendingSkuDto = new HubPendingSkuDto();
		BeanUtils.copyProperties(product, HubPendingSkuDto);
		return HubPendingSkuDto;
	}

	private HubSpuPendingDto convertHubPendingProduct2Spu(HubPendingProductImportDTO product) {
		
		HubSpuPendingDto hubSpuPendingDto = new HubSpuPendingDto();
		BeanUtils.copyProperties(product, hubSpuPendingDto);
		return hubSpuPendingDto;
	}

	private HubSkuPendingDto findHubSkuPending(String supplierId, String supplierSkuNo) {
		
		HubSkuPendingCriteriaDto criteria = new HubSkuPendingCriteriaDto();
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSkuNoEqualTo(supplierSkuNo);
		criteria.setFields("supplier_id,supplier_sku_no");
		List<HubSkuPendingDto> list = hubSkuPendingGateWay.selectByCriteria(criteria);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	private Long findHubSpuPending(Long spuId) {
		HubSpuPendingDto list = hubSpuPendingGateWay.selectByPrimaryKey(spuId);
		if(list!=null){
			return list.getSpuPendingId();
		}
		return null;
	}

	private HubSkuPendingDto convertHubPendingProduct2Sku(
			HubPendingProductImportDTO product) {
		HubSkuPendingDto hubSkuPendingDto = new HubSkuPendingDto();
		BeanUtils.copyProperties(product, hubSkuPendingDto);
		return hubSkuPendingDto;
	}

	//解析excel转换为对象
	private List<HubPendingProductImportDTO> handleHubExcel(String taskFtpFilePath,String taskNo)  throws Exception {
		
		InputStream in = FTPClientUtil.downFile(taskFtpFilePath);
		if(in==null){
			log.info("任务编号："+taskNo+","+taskFtpFilePath+"从ftp下载失败数据为空");
			taskService.updateHubSpuImportStatusByTaskNo(TaskState.SOME_SUCCESS.getIndex(),taskNo,"从ftp下载失败");
		}
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(in);
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		if (xssfSheet.getRow(0) == null) {
			log.info("任务编号："+taskNo+","+taskFtpFilePath+"下载的excel数据为空");
			taskService.updateHubSpuImportStatusByTaskNo(TaskState.SOME_SUCCESS.getIndex(),taskNo,"下载的excel数据为空");
			return null;
		}
		boolean flag = checkFileTemplet(xssfSheet.getRow(0));
		if(!flag){
			//TODO 上传文件与模板不一致
			log.info("任务编号："+taskNo+","+taskFtpFilePath+"上传文件与模板不一致");
			taskService.updateHubSpuImportStatusByTaskNo(TaskState.SOME_SUCCESS.getIndex(),taskNo,"上传文件与模板不一致");
			return null;
		}
		List<HubPendingProductImportDTO> listHubProduct = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			HubPendingProductImportDTO product = convertSpuDTO(xssfRow);
			listHubProduct.add(product);
		}
		return listHubProduct;
	}
	private static HubPendingProductImportDTO convertSpuDTO(XSSFRow xssfRow) {
		HubPendingProductImportDTO item = null;
		if (xssfRow != null) {
			try {
				item = new HubPendingProductImportDTO();
				String [] hubValueTemplate = item.getHubProductTemplate();
				Class c = item.getClass();
				for (int i = 0; i < hubValueTemplate.length; i++) {
					if (xssfRow.getCell(i) != null) {
						xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
						String setMethodName = "set" + hubValueTemplate[i].toUpperCase().charAt(0)
								+ hubValueTemplate[i].substring(1);
						Method setMethod = c.getDeclaredMethod(setMethodName, String.class);
						setMethod.invoke(item, xssfRow.getCell(i).toString());
					}
				}
				
			} catch (Exception ex) {
				ex.getStackTrace();
			}
		}
		return item;
	}
	private static boolean checkFileTemplet(XSSFRow xssfRow) {
			
		boolean flag = true;
		for (int i = 0; i < pendingSkuTemplate.length; i++) {
			if (xssfRow.getCell(i) != null) {
				String fieldName = xssfRow.getCell(i).toString();
				if (!pendingSkuTemplate[i].equals(fieldName)) {
					flag = false;
					break;
				}
			}
		}
		return flag;
	}
}
