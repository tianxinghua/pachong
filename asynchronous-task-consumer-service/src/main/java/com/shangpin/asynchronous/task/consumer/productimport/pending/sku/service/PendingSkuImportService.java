package com.shangpin.asynchronous.task.consumer.productimport.pending.sku.service;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;
import com.shangpin.ephub.client.product.business.hubpending.sku.dto.HubSkuCheckDto;
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
	HubSkuGateWay hubSkuGateWay;
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
	@Autowired
	HubSpuPendingGateWay hubSpuPendingGateWay;

	public String handMessage(ProductImportTask task) throws Exception {

		// ftp下载文件
		JSONObject json = JSONObject.parseObject(task.getData());
		String filePath = json.get("taskFtpFilePath").toString();
		task.setData(filePath);
		InputStream in = taskService.downFileFromFtp(task);
		
		//解析excel
		List<HubPendingProductImportDTO> listHubProduct = null;
		String fileFormat = filePath.split("\\.")[1];
		if ("xls".equals(fileFormat)) {
			listHubProduct = handleHubXlsExcel(in, task, "sku");
		} else if ("xlsx".equals(fileFormat)) {
			listHubProduct = handleHubXlsxExcel(in, task, "sku");
		}

		// 3、公共类校验hub数据并把校验结果写入excel
		return checkAndHandlePendingProduct(task.getTaskNo(), listHubProduct);
	}

	// 校验数据以及保存到hub表
	private String checkAndHandlePendingProduct(String taskNo, List<HubPendingProductImportDTO> listHubProduct)
			throws Exception {

		if (listHubProduct == null) {
			return null;
		}
		
//		Map<String, List<HubPendingProductImportDTO>> mapSpu = new HashMap<String, List<HubPendingProductImportDTO>>();
//		for (HubPendingProductImportDTO product : listHubProduct) {
//			String key = product.getSupplierId()+"_"+product.getSupplierSpuNo();
//			if(mapSpu.containsKey(key)){
//				mapSpu.get(key).add(product);
//			}else{
//				List<HubPendingProductImportDTO> arr = new ArrayList<HubPendingProductImportDTO>();
//				arr.add(product);
//				mapSpu.put(key, arr);
//			}
//		}
//		
//		
//	  for (Map.Entry<String, List<HubPendingProductImportDTO>> entry : mapSpu.entrySet()) {
//        	List<HubPendingProductImportDTO> spuList = entry.getValue();
//        	int i = 0;
//	  }
		
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		Map<Long,String> spuMap = new HashMap<Long,String>();
		for (HubPendingProductImportDTO product : listHubProduct) {
			if(product == null||StringUtils.isBlank(product.getSupplierId())){
				continue;
			}
			map = new HashMap<String, String>();
			
			checkProduct(taskNo, product, map,spuMap);
			listMap.add(map);
		}
		
		for (Map.Entry<Long, String> entry : spuMap.entrySet()) {
			Long spuPendingId = entry.getKey();
			
			HubSkuPendingCriteriaDto criate = new HubSkuPendingCriteriaDto();
			criate.createCriteria().andSpuPendingIdEqualTo(spuPendingId);
			List<HubSkuPendingDto> list = hubSkuPendingGateWay.selectByCriteria(criate);
			boolean flag = false;
			if(list!=null&&list.size()>0){
				for(HubSkuPendingDto sku:list){
					if(sku.getFilterFlag().intValue()==1&&sku.getSpSkuSizeState().intValue()==1){
						flag = true;
					}
				}
			}
			
			if(!flag){
				HubSpuPendingDto spu = new HubSpuPendingDto();
				spu.setSpuPendingId(spuPendingId);
				spu.setSpuState((byte)0);
				hubSpuPendingGateWay.updateByPrimaryKeySelective(spu);
			}
		
	  }
		// 4、处理结果的excel上传ftp，并更新任务表状态和文件在ftp的路径
		return taskService.convertExcel(listMap, taskNo);
	}

	private void checkProduct(String taskNo, HubPendingProductImportDTO product, Map<String, String> map,Map<Long,String> spuMap) throws Exception{

		map.put("taskNo", taskNo);
		map.put("spuModel", product.getSpuModel());

		// 校验sku信息
		HubSkuCheckDto hubSkuCheckDto = convertHubPendingProduct2PendingSkuCheck(product);
		log.info("pendindSku校验参数：{}", hubSkuCheckDto);
		HubPendingSkuCheckResult hubPendingSkuCheckResult = pendingSkuCheckGateWay.checkSku(hubSkuCheckDto);
		log.info("pendindSku校验返回结果：{}", hubPendingSkuCheckResult);
		
		// 校验spu信息
		HubSpuPendingDto hubPendingSpuDto = convertHubPendingProduct2PendingSpu(product);
		List<HubSpuPendingDto> listSpu = dataHandleService.selectPendingSpu(hubPendingSpuDto);
		HubSpuPendingDto isPendingSpuExist = null;
		if (listSpu != null && listSpu.size() > 0) {
			log.info(hubPendingSpuDto.getSpuModel() + "已存在pendingSpu");
			isPendingSpuExist = listSpu.get(0);
			spuMap.put(isPendingSpuExist.getSpuPendingId(),null);
		}
		taskService.checkPendingSpu(isPendingSpuExist, hubPendingSkuCheckResult, hubPendingSpuDto, map,true);
		// 校验sku信息
		HubSkuPendingDto HubPendingSkuDto = convertHubPendingProduct2PendingSku(product);
		taskService.checkPendingSku(hubPendingSkuCheckResult, HubPendingSkuDto, map,product,false);

		if (Boolean.parseBoolean(map.get("isPassing"))) {
			taskService.sendToHub(hubPendingSpuDto, map);
		}
	}

	private HubSkuPendingDto convertHubPendingProduct2PendingSku(HubPendingProductImportDTO product) throws Exception{
		HubSkuPendingDto hubPendingSkuDto = new HubSkuPendingDto();
		BeanUtils.copyProperties(product, hubPendingSkuDto);
		if(hubPendingSkuDto.getMarketPrice()!=null){
			hubPendingSkuDto.setMarketPrice(new BigDecimal(product.getMarketPrice()));	
		}
		if(hubPendingSkuDto.getSupplyPrice()!=null){
			hubPendingSkuDto.setSupplyPrice(new BigDecimal(product.getSupplyPrice()));	
		}
		hubPendingSkuDto.setHubSkuSize(product.getHubSkuSize());
		hubPendingSkuDto.setHubSkuSizeType(product.getSizeType());
		return hubPendingSkuDto;
	}

	private HubSkuCheckDto convertHubPendingProduct2PendingSkuCheck(HubPendingProductImportDTO product) throws Exception{
		HubSkuCheckDto hubPendingSkuDto = new HubSkuCheckDto();
		hubPendingSkuDto.setBrandNo(product.getHubBrandNo());
		hubPendingSkuDto.setCategoryNo(product.getHubCategoryNo());
		hubPendingSkuDto.setSizeType(product.getSizeType());
		hubPendingSkuDto.setSkuSize(product.getHubSkuSize());
		hubPendingSkuDto.setSpuModel(product.getSpuModel());
		hubPendingSkuDto.setSpecificationType(product.getSpecificationType());
		return hubPendingSkuDto;
	}


	// 解析excel转换为对象
	private List<HubPendingProductImportDTO> handleHubXlsxExcel(InputStream in, ProductImportTask task, String type)
			throws Exception {

		XSSFSheet xssfSheet = taskService.checkXlsxExcel(in, task, "sku");
		if (xssfSheet == null) {
			return null;
		}
		List<HubPendingProductImportDTO> listHubProduct = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			HubPendingProductImportDTO product = convertSpuDTO(xssfRow);
			if (product != null) {
				listHubProduct.add(product);
			}

		}
		return listHubProduct;
	}

	// 解析excel转换为对象
	private List<HubPendingProductImportDTO> handleHubXlsExcel(InputStream in, ProductImportTask task, String type)
			throws Exception {

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

	private HubSpuPendingDto convertHubPendingProduct2PendingSpu(HubPendingProductImportDTO product) throws Exception{
		HubSpuPendingDto HubPendingSpuDto = new HubSpuPendingDto();
		BeanUtils.copyProperties(product, HubPendingSpuDto);
		HubPendingSpuDto.setHubSeason(product.getSeasonYear() + "_" + product.getSeasonName());
		return HubPendingSpuDto;
	}

	@SuppressWarnings("unchecked")
	private static HubPendingProductImportDTO convertSpuDTO(XSSFRow xssfRow) throws Exception{
		HubPendingProductImportDTO item = null;
		if (xssfRow != null) {
			try {
				item = new HubPendingProductImportDTO();
				String[] hubValueTemplate = item.getHubProductTemplate();
				Class cls = item.getClass();
				for (int i=0;i<hubValueTemplate.length;i++) {
					if(xssfRow.getCell(i)!=null){
						xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
						String fieldSetName = "set" + hubValueTemplate[i].toUpperCase().charAt(0)
								+ hubValueTemplate[i].substring(1);
						Method setMethod = cls.getDeclaredMethod(fieldSetName, String.class);
						setMethod.invoke(item, xssfRow.getCell(i).toString());
					}
//						@SuppressWarnings("unchecked")
//						Method fieldSetMet = cls.getMethod(fieldSetName, field.getType());
//						if (!hubValueTemplate[i].equals(field.getName())) {
//							return null;
//						}
//						if(xssfRow.getCell(i)!=null){
//							xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
//							String value = xssfRow.getCell(i).toString();
//							i++;
//							if (null != value && !"".equals(value)) {
//								String fieldType = field.getType().getSimpleName();
//								if ("String".equals(fieldType)) {
//									fieldSetMet.invoke(item, value);
//								} else if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
//									Integer intval = Integer.parseInt(value);
//									fieldSetMet.invoke(item, intval);
//								} else if ("Long".equalsIgnoreCase(fieldType)) {
//									Long temp = Long.parseLong(value);
//									fieldSetMet.invoke(item, temp);
//								} else if ("Double".equalsIgnoreCase(fieldType)) {
//									Double temp = Double.parseDouble(value);
//									fieldSetMet.invoke(item, temp);
//								} else if ("Boolean".equalsIgnoreCase(fieldType)) {
//									Boolean temp = Boolean.parseBoolean(value);
//									fieldSetMet.invoke(item, temp);
//								} else if ("BigDecimal".equalsIgnoreCase(fieldType)) {
//									BigDecimal temp = new BigDecimal(value);
//									fieldSetMet.invoke(item, temp);
//								} else {
//									log.info("not supper type" + fieldType);
//								}
//							}
//						
					
				}

			} catch (Exception ex) {
				ex.getStackTrace();
			}
			
	}
		return item;
	}
	@SuppressWarnings("unchecked")
	private static HubPendingProductImportDTO convertSpuDTO(HSSFRow xssfRow) throws Exception{
		HubPendingProductImportDTO item = null;
		if (xssfRow != null) {
			try {
				item = new HubPendingProductImportDTO();
				String[] hubValueTemplate = item.getHubProductTemplate();
				Class cls = item.getClass();
				
				for (int i=0;i<hubValueTemplate.length;i++) {
					if(xssfRow.getCell(i)!=null){
						xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
						String fieldSetName = "set" + hubValueTemplate[i].toUpperCase().charAt(0)
								+ hubValueTemplate[i].substring(1);
						Method setMethod = cls.getDeclaredMethod(fieldSetName, String.class);
						setMethod.invoke(item, xssfRow.getCell(i).toString());
					}
//				for (Field field : fields) {
//					try {
//						String fieldSetName = parSetName(field.getName());
//						@SuppressWarnings("unchecked")
//						Method fieldSetMet = cls.getMethod(fieldSetName, field.getType());
//						if (!hubValueTemplate[i].equals(field.getName())) {
//							return null;
//						}
//						xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
//						String value = xssfRow.getCell(i).toString();
//						if (null != value && !"".equals(value)) {
//							String fieldType = field.getType().getSimpleName();
//							if ("String".equals(fieldType)) {
//								fieldSetMet.invoke(item, value);
//							} else if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
//								Integer intval = Integer.parseInt(value);
//								fieldSetMet.invoke(item, intval);
//							} else if ("Long".equalsIgnoreCase(fieldType)) {
//								Long temp = Long.parseLong(value);
//								fieldSetMet.invoke(item, temp);
//							} else if ("Double".equalsIgnoreCase(fieldType)) {
//								Double temp = Double.parseDouble(value);
//								fieldSetMet.invoke(item, temp);
//							} else if ("Boolean".equalsIgnoreCase(fieldType)) {
//								Boolean temp = Boolean.parseBoolean(value);
//								fieldSetMet.invoke(item, temp);
//							} else if ("BigDecimal".equalsIgnoreCase(fieldType)) {
//								BigDecimal temp = new BigDecimal(value);
//								fieldSetMet.invoke(item, temp);
//							} else {
//								log.info("not supper type" + fieldType);
//							}
//						}
//					} catch (Exception e) {
//						continue;
//					}
				}

			} catch (Exception ex) {
				ex.getStackTrace();
			}
		}
		return item;
	}

	public static boolean checkSetMet(Method[] methods, String fieldSetMet) throws Exception{
		for (Method met : methods) {
			if (fieldSetMet.equals(met.getName())) {
				return true;
			}
		}
		return false;
	}

	public static String parSetName(String fieldName) throws Exception{
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
