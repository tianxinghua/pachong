package com.shangpin.asynchronous.task.consumer.productimport.pending.sku.service;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import com.shangpin.ephub.client.data.mysql.enumeration.SkuState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;
import com.shangpin.ephub.client.product.business.hubpending.sku.gateway.HubPendingSkuCheckGateWay;
import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.client.product.business.model.gateway.HubBrandModelRuleGateWay;

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
public class PendingSkuImportService {

	@Autowired
	DataHandleService dataHandleService;
	@Autowired
	HubSpuImportTaskGateWay spuImportGateway;
	@Autowired
	HubSkuPendingGateWay hubSkuPendingGateWay;
	@Autowired
	TaskImportService taskService;

	@Autowired
	HubPendingSkuCheckGateWay pendingSkuCheckGateWay;

	@Autowired
	HubBrandModelRuleGateWay hubBrandModelRuleGateWay;
	@Autowired
	HubSpuGateWay hubSpuGateway;

	public void handMessage(ProductImportTask task) throws Exception {
		
		//ftp下载文件
		JSONObject json = JSONObject.parseObject(task.getData());
		String filePath = json.get("taskFtpFilePath").toString();
		task.setData(filePath);
		
		InputStream in = taskService.downFileFromFtp(task);
		List<HubPendingProductImportDTO> listHubProduct = null;
		String fileFormat = filePath.split("\\.")[1];
		if ("xls".equals(fileFormat)) {
			listHubProduct = handleHubXlsExcel(in, task, "sku");
		} else if ("xlsx".equals(fileFormat)) {
			listHubProduct = handleHubXlsxExcel(in, task, "sku");
		}
		
		// 3、公共类校验hub数据并把校验结果写入excel
		checkAndHandlePendingProduct(task.getTaskNo(), listHubProduct);
	}

	// 校验数据以及保存到hub表
	private void checkAndHandlePendingProduct(String taskNo, List<HubPendingProductImportDTO> listHubProduct)
			throws Exception {

		if (listHubProduct == null) {
			return;
		}
		
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		for (HubPendingProductImportDTO product : listHubProduct) {
			if(product==null){
				continue;
			}
			map = new HashMap<String, String>();
			checkProduct(taskNo,product,map);
			listMap.add(map);
		}
		// 4、处理结果的excel上传ftp，并更新任务表状态和文件在ftp的路径
		taskService.convertExcel(listMap, taskNo);
	}

	private void checkProduct(String taskNo,HubPendingProductImportDTO product, Map<String, String> map) {
		map.put("taskNo", taskNo);
		map.put("spuModel", product.getSpuModel());
		
		// 校验spu信息
		HubSpuPendingDto hubPendingSpuDto = convertHubPendingProduct2PendingSpu(product);
		taskService.checkPendingSpu(hubPendingSpuDto,map);
		log.info("返回的pendingSpuId:"+map.get("pendingSpuId"));
		// 校验sku信息
		HubSkuPendingDto HubPendingSkuDto = convertHubPendingProduct2PendingSku(product);
		HubPendingSkuDto.setSpuPendingId(Long.valueOf(map.get("pendingSpuId")));
		checkPendingSku(HubPendingSkuDto,map);
		
		if (Boolean.parseBoolean(map.get("isPassing"))) {
			taskService.sendToHub(hubPendingSpuDto, Boolean.parseBoolean(map.get("isSaveHub")), map.get("hubSpuId"));
		}
		
	}

	private void checkPendingSku(HubSkuPendingDto hubSkuPendingDto, Map<String, String> map) {
		
	
		HubPendingSkuCheckResult hubPendingSkuCheckResult = pendingSkuCheckGateWay.checkSku(hubSkuPendingDto);
		if(hubPendingSkuCheckResult.isPassing()){
			hubSkuPendingDto.setSkuState((byte) SpuState.HANDLING.getIndex());
			//TODO: 状态需要加判断
			hubSkuPendingDto.setSpSkuSizeState((byte)1);
		}else{
			map.put("taskState","校验失败");
			map.put("processInfo", map.get("processInfo")+","+hubPendingSkuCheckResult.getResult());
			hubSkuPendingDto.setSkuState((byte) SpuState.INFO_PECCABLE.getIndex());
		}
		
		HubSkuPendingDto hubSkuPendingTempDto = findHubSkuPending(hubSkuPendingDto.getSupplierId(), hubSkuPendingDto.getSupplierSkuNo());
		if (hubSkuPendingTempDto != null) {
			log.info("sku:"+hubSkuPendingTempDto.getSupplierSkuNo() + "存在");
			hubSkuPendingDto.setSkuPendingId(hubSkuPendingTempDto.getSkuPendingId());
			hubSkuPendingDto.setUpdateTime(new Date());
			hubSkuPendingDto.setSkuState((byte) SkuState.INFO_IMPECCABLE.getIndex());
			hubSkuPendingGateWay.updateByPrimaryKeySelective(hubSkuPendingDto);
		}else{
			log.info("sku:"+hubSkuPendingDto.getSupplierSkuNo() + "不存在");
			hubSkuPendingDto.setCreateTime(new Date());
			hubSkuPendingDto.setDataState((byte) 1);
			hubSkuPendingGateWay.insert(hubSkuPendingDto);
		}
	}

	

	private HubSkuPendingDto convertHubPendingProduct2PendingSku(HubPendingProductImportDTO product) {
		HubSkuPendingDto HubPendingSkuDto = new HubSkuPendingDto();
		BeanUtils.copyProperties(product, HubPendingSkuDto);
		return HubPendingSkuDto;
	}

	private HubSkuPendingDto findHubSkuPending(String supplierId, String supplierSkuNo) {

		HubSkuPendingCriteriaDto criteria = new HubSkuPendingCriteriaDto();
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSkuNoEqualTo(supplierSkuNo);
		List<HubSkuPendingDto> list = hubSkuPendingGateWay.selectByCriteria(criteria);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	// 解析excel转换为对象
	private List<HubPendingProductImportDTO> handleHubXlsxExcel(InputStream in,ProductImportTask task,String type) throws Exception {
		
		XSSFSheet xssfSheet = taskService.checkXlsxExcel(in, task, "sku");
		if (xssfSheet == null) {
			return null;
		}
		List<HubPendingProductImportDTO> listHubProduct = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			HubPendingProductImportDTO product = convertSpuDTO(xssfRow);
			if(product!=null){
				listHubProduct.add(product);	
			}
			
		}
		return listHubProduct;
	}
	// 解析excel转换为对象
	private List<HubPendingProductImportDTO> handleHubXlsExcel(InputStream in,ProductImportTask task,String type) throws Exception {

		HSSFSheet hSSFSheet = taskService.checkXlsExcel(in, task, "sku");
		if (hSSFSheet == null) {
			return null;
		}
		List<HubPendingProductImportDTO> listHubProduct = new ArrayList<>();
		for (int rowNum = 1; rowNum <= hSSFSheet.getLastRowNum(); rowNum++) {
			HSSFRow hssfRow = hSSFSheet.getRow(rowNum);
			HubPendingProductImportDTO product = convertSpuDTO(hssfRow);
			listHubProduct.add(product);
		}
		return listHubProduct;
	}

	private HubSpuPendingDto convertHubPendingProduct2PendingSpu(HubPendingProductImportDTO product) {
		HubSpuPendingDto HubPendingSpuDto = new HubSpuPendingDto();
		BeanUtils.copyProperties(product, HubPendingSpuDto);
		HubPendingSpuDto.setHubSeason(product.getSeasonYear() + "_" + product.getSeasonName());
		return HubPendingSpuDto;
	}
	private static HubPendingProductImportDTO convertSpuDTO(XSSFRow xssfRow) {
		HubPendingProductImportDTO item = null;
		if (xssfRow != null) {
			try {
				item = new HubPendingProductImportDTO();
				String[] hubValueTemplate = item.getHubProductTemplate();
				Class cls = item.getClass();
				Field[] fields = cls.getDeclaredFields();
				int i = 0;
				for (Field field : fields) {
					try {
						String fieldSetName = parSetName(field.getName());
						@SuppressWarnings("unchecked")
						Method fieldSetMet = cls.getMethod(fieldSetName, field.getType());
						if (!hubValueTemplate[i].equals(field.getName())) {
							return null;
						}
						xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
						String value = xssfRow.getCell(i).toString();
						if (null != value && !"".equals(value)) {
							String fieldType = field.getType().getSimpleName();
							if ("String".equals(fieldType)) {
								fieldSetMet.invoke(item, value);
							} else if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
								Integer intval = Integer.parseInt(value);
								fieldSetMet.invoke(item, intval);
							} else if ("Long".equalsIgnoreCase(fieldType)) {
								Long temp = Long.parseLong(value);
								fieldSetMet.invoke(item, temp);
							} else if ("Double".equalsIgnoreCase(fieldType)) {
								Double temp = Double.parseDouble(value);
								fieldSetMet.invoke(item, temp);
							} else if ("Boolean".equalsIgnoreCase(fieldType)) {
								Boolean temp = Boolean.parseBoolean(value);
								fieldSetMet.invoke(item, temp);
							} else if ("BigDecimal".equalsIgnoreCase(fieldType)) {
								BigDecimal temp = new BigDecimal(value);
								fieldSetMet.invoke(item, temp);
							} else {
								log.info("not supper type" + fieldType);
							}
						}
					} catch (Exception e) {
						continue;
					}
					i++;
				}

			} catch (Exception ex) {
				ex.getStackTrace();
			}
		}
		return item;
	}

	private static HubPendingProductImportDTO convertSpuDTO(HSSFRow xssfRow) {
		HubPendingProductImportDTO item = null;
		if (xssfRow != null) {
			try {
				item = new HubPendingProductImportDTO();
				String[] hubValueTemplate = item.getHubProductTemplate();
				Class cls = item.getClass();
				Field[] fields = cls.getDeclaredFields();
				int i = 0;
				for (Field field : fields) {
					try {
						String fieldSetName = parSetName(field.getName());
						@SuppressWarnings("unchecked")
						Method fieldSetMet = cls.getMethod(fieldSetName, field.getType());
						if (!hubValueTemplate[i].equals(field.getName())) {
							return null;
						}
						xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
						String value = xssfRow.getCell(i).toString();
						if (null != value && !"".equals(value)) {
							String fieldType = field.getType().getSimpleName();
							if ("String".equals(fieldType)) {
								fieldSetMet.invoke(item, value);
							} else if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
								Integer intval = Integer.parseInt(value);
								fieldSetMet.invoke(item, intval);
							} else if ("Long".equalsIgnoreCase(fieldType)) {
								Long temp = Long.parseLong(value);
								fieldSetMet.invoke(item, temp);
							} else if ("Double".equalsIgnoreCase(fieldType)) {
								Double temp = Double.parseDouble(value);
								fieldSetMet.invoke(item, temp);
							} else if ("Boolean".equalsIgnoreCase(fieldType)) {
								Boolean temp = Boolean.parseBoolean(value);
								fieldSetMet.invoke(item, temp);
							} else if ("BigDecimal".equalsIgnoreCase(fieldType)) {
								BigDecimal temp = new BigDecimal(value);
								fieldSetMet.invoke(item, temp);
							} else {
								log.info("not supper type" + fieldType);
							}
						}
					} catch (Exception e) {
						continue;
					}
					i++;
				}

			} catch (Exception ex) {
				ex.getStackTrace();
			}
		}
		return item;
	}

	public static boolean checkSetMet(Method[] methods, String fieldSetMet) {
		for (Method met : methods) {
			if (fieldSetMet.equals(met.getName())) {
				return true;
			}
		}
		return false;
	}

	public static String parSetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		int startIndex = 0;
		if (fieldName.charAt(0) == '_')
			startIndex = 1;
		return "set" + fieldName.substring(startIndex, startIndex + 1).toUpperCase()
				+ fieldName.substring(startIndex + 1);
	}
}
