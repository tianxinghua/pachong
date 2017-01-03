package com.shangpin.asynchronous.task.consumer.productimport.pending.spu.service;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
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

import com.shangpin.asynchronous.task.consumer.productimport.common.service.TaskImportService;
import com.shangpin.asynchronous.task.consumer.productimport.common.util.FTPClientUtil;
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.dao.HubPendingSpuImportDTO;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;
import com.shangpin.ephub.client.product.business.hubpending.sku.gateway.HubPendingSkuCheckGateWay;
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
public class PendingSpuImportService {
	
	@Autowired
	HubSpuImportTaskGateWay spuImportGateway;

	@Autowired
	HubSkuPendingGateWay hubSkuPendingGateWay;
	
	@Autowired
	TaskImportService taskService;
	
	@Autowired
	HubSpuPendingGateWay hubSpuPendingGateWay;
	
	@Autowired
	HubPendingSkuCheckGateWay pendingSkuCheckGateWay;
	@Autowired
	HubPendingSpuCheckGateWay pendingSpuCheckGateWay;
	
	private static String[] pendingSpuTemplate = null;
	static {
		pendingSpuTemplate = TaskImportTemplate.getPendingSpuTemplate();
	}
	
	public void handMessage(ProductImportTask task) throws Exception{
		
		//1、更新任务表，把task_state更新成正在处理
		taskService.updateHubSpuImportStatusByTaskNo(TaskState.HANDLEING.getIndex(),task.getTaskNo(),null);
		log.info("任务编号："+task.getTaskNo()+"状态更新为正在处理");
		
		// 2、从ftp下载文件并校验模板
		XSSFSheet xssfSheet = taskService.checkExcel(task.getTaskFtpFilePath(),task.getTaskNo(),"spu");
		// 3、excel解析成java对象
		List<HubPendingSpuImportDTO> listHubProduct = excelToPendingSpuImportDto(xssfSheet);
		
		//3、公共类校验hub数据并把校验结果写入excel
		 checkAndsaveHubPendingProduct(task.getTaskNo(),listHubProduct);
	}

	//校验数据以及保存到hub表
	private void checkAndsaveHubPendingProduct(String taskNo,List<HubPendingSpuImportDTO> listHubProduct) throws Exception{

		if(listHubProduct==null){
			return ;
		}
		boolean flag = false;
		List<Map<String, String>> listMap = new ArrayList<Map<String,String>>();
		for(HubPendingSpuImportDTO product:listHubProduct){
			
			Map<String, String> map = new HashMap<String, String>();
			//处理spu信息
			HubPendingSpuCheckResult hubPendingSpuCheckResult = handlePendingSpu(product);
			
			if(hubPendingSpuCheckResult.isPassing()==true){
				log.info(taskNo+"spu校验通过");
				//校验sku信息
				
				updateOrSaveSpu(product);
				
				map.put("taskNo",taskNo);
				map.put("spuModel",product.getSpuModel());
				map.put("taskState","校验成功");
				map.put("processInfo","校验通过");
			}else {
				log.info(taskNo+"spu校验不通过");
				flag = true;
				map.put("taskNo", taskNo);
				map.put("spuModel", product.getSpuModel());
				map.put("taskState", "校验失败");
				map.put("processInfo", hubPendingSpuCheckResult.getResult());
			}
			listMap.add(map);
		}
		
		//处理结果的excel上传ftp，并更新任务表状态和文件在ftp的路径
		taskService.convertExcel(listMap,taskNo,flag);
		
	}

	
	private void updateOrSaveSpu(HubPendingSpuImportDTO product) {
		//查询数据库spu信息是否已存在，存在则更新，反之插入
		HubSpuPendingDto hubSpuPendingDto = convertHubPendingProduct2Spu(product);
		HubSpuPendingWithCriteriaDto criteria = new HubSpuPendingWithCriteriaDto();
		
		HubSpuPendingCriteriaDto hubSpuPendingCriteria = new HubSpuPendingCriteriaDto();
		hubSpuPendingCriteria.createCriteria().andSupplierSpuNoEqualTo(hubSpuPendingDto.getSupplierSpuNo()).andSupplierIdEqualTo(hubSpuPendingDto.getSupplierId());
		hubSpuPendingDto.setHubSeason(product.getSeasonYear()+"_"+product.getSeasonName());
		criteria.setCriteria(hubSpuPendingCriteria);
		criteria.setHubSpuPending(hubSpuPendingDto);
		
		hubSpuPendingGateWay.updateByCriteriaSelective(criteria);
	}

	private HubPendingSpuCheckResult handlePendingSpu(HubPendingSpuImportDTO product) {
		//校验spu信息
		HubSpuPendingDto HubPendingSpuDto = convertHubPendingProduct2PendingSpu(product);
		return pendingSpuCheckGateWay.checkSpu(HubPendingSpuDto);
		
	}

	private HubSpuPendingDto convertHubPendingProduct2PendingSpu(
			HubPendingSpuImportDTO product) {
		HubSpuPendingDto HubPendingSpuDto = new HubSpuPendingDto();
		BeanUtils.copyProperties(product, HubPendingSpuDto);
		return HubPendingSpuDto;
	}


	private HubSpuPendingDto convertHubPendingProduct2Spu(HubPendingSpuImportDTO product) {
		
		HubSpuPendingDto hubSpuPendingDto = new HubSpuPendingDto();
		BeanUtils.copyProperties(product, hubSpuPendingDto);
		return hubSpuPendingDto;
	}

	//解析excel转换为对象
	private List<HubPendingSpuImportDTO> excelToPendingSpuImportDto(XSSFSheet xssfSheet)  throws Exception {
		if(xssfSheet==null){
			return null;
		}
		List<HubPendingSpuImportDTO> listHubProduct = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			HubPendingSpuImportDTO product = convertSpuDTO(xssfRow);
			listHubProduct.add(product);
		}
		return listHubProduct;
	}
	@SuppressWarnings("unchecked")
	private static HubPendingSpuImportDTO convertSpuDTO(XSSFRow xssfRow) {
		HubPendingSpuImportDTO item = null;
		if (xssfRow != null) {
			try {
				item = new HubPendingSpuImportDTO();
				String [] hubValueTemplate = item.getPendingSpuTemplate();
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
		for (int i = 0; i < pendingSpuTemplate.length; i++) {
			if (xssfRow.getCell(i) != null) {
				String fieldName = xssfRow.getCell(i).toString();
				if (!pendingSpuTemplate[i].equals(fieldName)) {
					flag = false;
					break;
				}
			}
		}
		return flag;
	}
}
