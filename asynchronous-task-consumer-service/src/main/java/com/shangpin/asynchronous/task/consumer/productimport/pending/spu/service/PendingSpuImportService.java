package com.shangpin.asynchronous.task.consumer.productimport.pending.spu.service;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.shangpin.asynchronous.task.consumer.productimport.common.service.DataHandleService;
import com.shangpin.asynchronous.task.consumer.productimport.common.service.TaskImportService;
import com.shangpin.asynchronous.task.consumer.productimport.pending.sku.dao.HubPendingProductImportDTO;
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.dao.HubPendingSpuImportDTO;
import com.shangpin.ephub.client.data.mysql.product.gateway.PengdingToHubGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;
import com.shangpin.ephub.client.product.business.hubpending.sku.dto.HubSkuCheckDto;
import com.shangpin.ephub.client.product.business.hubpending.sku.gateway.HubPendingSkuCheckGateWay;
import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.client.product.business.hubpending.spu.gateway.HubPendingSpuCheckGateWay;
import com.shangpin.ephub.client.product.business.model.gateway.HubBrandModelRuleGateWay;
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
	DataHandleService dataHandleService;
	@Autowired
	HubSpuImportTaskGateWay spuImportGateway;
	@Autowired
	HubSkuPendingGateWay hubSkuPendingGateWay;
	@Autowired
	TaskImportService taskService;
	@Autowired
	HubSpuPendingGateWay hubSpuPendingGateWay;
	@Autowired
	HubBrandModelRuleGateWay hubBrandModelRuleGateWay;
	@Autowired
	HubPendingSkuCheckGateWay pendingSkuCheckGateWay;
	@Autowired
	HubPendingSpuCheckGateWay pendingSpuCheckGateWay;
	@Autowired
	PengdingToHubGateWay pengdingToHubGateWay;
	
	private static String[] pendingSpuValueTemplate = null;
	static {
		pendingSpuValueTemplate = TaskImportTemplate.getPendingSpuValueTemplate();
	}
	
	
	public void handMessage(ProductImportTask task) throws Exception {

		JSONObject json = JSONObject.parseObject(task.getData());
		String filePath = json.get("taskFtpFilePath").toString();
		task.setData(filePath);
		
		InputStream in = taskService.downFileFromFtp(task);
		// 2、从ftp下载文件并校验模板
		List<HubPendingSpuImportDTO> listHubProduct = null;
		String fileFormat =filePath.split("\\.")[1];
		if ("xls".equals(fileFormat)) {
			listHubProduct = handlePendingSpuXls(in, task, "spu");
		} else if ("xlsx".equals(fileFormat)) {
			listHubProduct = handlePendingSpuXlsx(in, task, "spu");
		}
		
		// 3、公共类校验hub数据并把校验结果写入excel
		checkAndsaveHubPendingProduct(task.getTaskNo(), listHubProduct);
	}

	private List<HubPendingSpuImportDTO> handlePendingSpuXlsx(InputStream in, ProductImportTask task, String type) throws Exception{
		XSSFSheet xssfSheet = taskService.checkXlsxExcel(in, task, type);
		if (xssfSheet == null) {
			return null;
		}
		List<HubPendingSpuImportDTO> listHubProduct = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			HubPendingSpuImportDTO product = convertSpuDTO(xssfRow);
			if(product!=null){
				listHubProduct.add(product);	
			}
			
		}
		return listHubProduct;
	}

	private List<HubPendingSpuImportDTO> handlePendingSpuXls(InputStream in, ProductImportTask task, String type) throws Exception{
		HSSFSheet xssfSheet = taskService.checkXlsExcel(in, task, type);
		if (xssfSheet == null) {
			return null;
		}
		List<HubPendingSpuImportDTO> listHubProduct = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			HSSFRow xssfRow = xssfSheet.getRow(rowNum);
			HubPendingSpuImportDTO product = convertSpuDTO(xssfRow);
			if(product!=null){
				listHubProduct.add(product);	
			}
			
		}
		return listHubProduct;
	}

	// 校验数据以及保存到hub表
	private void checkAndsaveHubPendingProduct(String taskNo, List<HubPendingSpuImportDTO> listHubProduct)
			throws Exception {
		if (listHubProduct == null) {
			return;
		}
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		for (HubPendingSpuImportDTO product : listHubProduct) {

			map = new HashMap<String, String>();
			map.put("taskNo", taskNo);
			map.put("spuModel", product.getSpuModel());
			
			HubSpuPendingDto hubPendingSpuDto = convertHubPendingProduct2PendingSpu(product);
			// 查询是否已存在pendingSpu表中
			List<HubSpuPendingDto> listSpu = dataHandleService.selectPendingSpu(hubPendingSpuDto);
			HubSpuPendingDto isHubExist = null;
			if (listSpu != null && listSpu.size() > 0) {
				log.info(hubPendingSpuDto.getSpuModel()+"已存在hub");
				isHubExist = listSpu.get(0);
			}
			
			boolean flag = true;
			HubPendingSkuCheckResult checkResult = new HubPendingSkuCheckResult();
			if(isHubExist!=null){
				HubSkuPendingCriteriaDto criteria = new HubSkuPendingCriteriaDto();
				criteria.createCriteria().andSupplierIdEqualTo(product.getSupplierId()).andSpuPendingIdEqualTo(isHubExist.getSpuPendingId());
				List<HubSkuPendingDto> listSku = hubSkuPendingGateWay.selectByCriteria(criteria);
				if(listSku!=null&&listSku.size()>0){
					for(HubSkuPendingDto sku:listSku){
						HubSkuCheckDto hubSkuCheckDto =  convertHubPendingProduct2PendingSkuCheck(isHubExist,sku.getHubSkuSize());	
						log.info("pendindSku校验参数：{}",hubSkuCheckDto);
						HubPendingSkuCheckResult hubPendingSkuCheckResult = pendingSkuCheckGateWay.checkSku(hubSkuCheckDto);
						if(!hubPendingSkuCheckResult.isPassing()){
							flag = false;
							checkResult.setResult("尺码检验不通过:"+sku.getHubSkuSize());
							break;
						}
						log.info("pendindSku校验返回结果：{}",hubPendingSkuCheckResult);
					}
					
				}
			}
			// 校验sku信息
			
			
			checkResult.setPassing(flag);
			
			taskService.checkPendingSpu(isHubExist,checkResult,hubPendingSpuDto,map);
			
			if (Boolean.parseBoolean(map.get("isPassing"))) {
				taskService.sendToHub(hubPendingSpuDto, Boolean.parseBoolean(map.get("isSaveHub")), map.get("hubSpuId"));
			}
			listMap.add(map);
		}
		
		// 处理结果的excel上传ftp，并更新任务表状态和文件在ftp的路径
		taskService.convertExcel(listMap, taskNo);
	}
	private HubSkuCheckDto convertHubPendingProduct2PendingSkuCheck(HubSpuPendingDto product,String size) {
		HubSkuCheckDto hubPendingSkuDto = new HubSkuCheckDto();
		hubPendingSkuDto.setBrandNo(product.getHubBrandNo());
		hubPendingSkuDto.setCategoryNo(product.getHubCategoryNo());
		hubPendingSkuDto.setSkuSize(size);
		hubPendingSkuDto.setSpuModel(product.getSpuModel());
		hubPendingSkuDto.setSpecificationType("类型");
		return hubPendingSkuDto;
	}
	private HubSpuPendingDto convertHubPendingProduct2PendingSpu(HubPendingSpuImportDTO product) {
		HubSpuPendingDto HubPendingSpuDto = new HubSpuPendingDto();
		BeanUtils.copyProperties(product, HubPendingSpuDto);
		HubPendingSpuDto.setHubSeason(product.getSeasonYear()+"_"+product.getSeasonName());
		return HubPendingSpuDto;
	}


	@SuppressWarnings("unchecked")
	private static HubPendingSpuImportDTO convertSpuDTO(XSSFRow xssfRow) {
		HubPendingSpuImportDTO item = null;
		if (xssfRow != null) {
			try {
				item = new HubPendingSpuImportDTO();
				Class c = item.getClass();
				for (int i = 0; i < pendingSpuValueTemplate.length; i++) {
					if (xssfRow.getCell(i) != null) {
						xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
						String setMethodName = "set" + pendingSpuValueTemplate[i].toUpperCase().charAt(0)
								+ pendingSpuValueTemplate[i].substring(1);
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
	@SuppressWarnings("unchecked")
	private static HubPendingSpuImportDTO convertSpuDTO(HSSFRow xssfRow) {
		HubPendingSpuImportDTO item = null;
		if (xssfRow != null) {
			try {
				item = new HubPendingSpuImportDTO();
				Class c = item.getClass();
				for (int i = 0; i < pendingSpuValueTemplate.length; i++) {
					if (xssfRow.getCell(i) != null) {
						xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
						String setMethodName = "set" + pendingSpuValueTemplate[i].toUpperCase().charAt(0)
								+ pendingSpuValueTemplate[i].substring(1);
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
}
