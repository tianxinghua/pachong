package com.shangpin.asynchronous.task.consumer.productimport.pending.spu.service;

import java.io.InputStream;
import java.lang.reflect.Method;
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
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.dao.HubPendingSpuImportDTO;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.message.task.product.body.Task;
import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.client.product.business.size.result.MatchSizeResult;
import com.shangpin.ephub.client.util.TaskImportTemplate;

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
public class PendingSpuImportService {
	

	@Autowired
	DataHandleService dataHandleService;
	@Autowired
	TaskImportService taskService;

	private static String[] pendingSpuValueTemplate = null;
	static {
		pendingSpuValueTemplate = TaskImportTemplate.getPendingSpuValueTemplate();
	}

	public String handMessage(Task task) throws Exception {
		
		//从ftp下载文件
		JSONObject json = JSONObject.parseObject(task.getData());
		String filePath = json.get("taskFtpFilePath").toString();
		String createUser = json.get("createUser").toString();
		task.setData(filePath);
		
		
		InputStream in = taskService.downFileFromFtp(task);
		
		//excel转对象
		List<HubPendingSpuImportDTO> listHubProduct = null;
		String fileFormat = task.getData().split("\\.")[1];
		if ("xls".equals(fileFormat)) {
			listHubProduct = handlePendingSpuXls(in, task, "spu");
		} else if ("xlsx".equals(fileFormat)) {
			listHubProduct = handlePendingSpuXlsx(in, task, "spu");
		}

		//校验数据并把校验结果写入excel
		return checkAndsaveHubPendingProduct(task.getTaskNo(), listHubProduct,createUser);
	}
	// 校验数据以及保存到hub表
	
		public String checkAndsaveHubPendingProduct(String taskNo, List<HubPendingSpuImportDTO> listHubProduct,String createUser)
				throws Exception {
			
			if (listHubProduct == null) {
				return null;
			}
			
			List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
			Map<String, String> map = null;
			for (HubPendingSpuImportDTO product : listHubProduct) {
				if (product == null || StringUtils.isBlank(product.getSupplierId())) {
					continue;
				}
				
				map = new HashMap<String, String>();
				map.put("taskNo", taskNo);
				map.put("spuModel", product.getSpuModel());
				loopHandleSpuImportDto(map,product,createUser);
				listMap.add(map);
			}

			// 处理结果的excel上传ftp，并更新任务表状态和文件在ftp的路径
			return taskService.convertExcel(listMap, taskNo);
		}
	private void loopHandleSpuImportDto(Map<String, String> map, HubPendingSpuImportDTO product,String createUser) throws Exception{
		
		//判断spuPending是否已存在
		HubSpuPendingDto hubPendingSpuDto = convertHubPendingProduct2PendingSpu(product,createUser);
		List<HubSpuPendingDto> listSpu = dataHandleService.selectPendingSpu(hubPendingSpuDto);
		HubSpuPendingDto isSpuPendingExist = null;
		if (listSpu != null && listSpu.size() > 0) {
			isSpuPendingExist = listSpu.get(0);
		}
		//判断hubSpu是否已存在
		HubSpuDto hubSpuDto = dataHandleService.selectHubSpu(hubPendingSpuDto.getSpuModel(),hubPendingSpuDto.getHubBrandNo());
		if (hubSpuDto != null) {
			Long hubSpuId = hubSpuDto.getSpuId();
			String hubSpuNo = hubSpuDto.getSpuNo();
			boolean hubIsExist = true;
			map.put("hubIsExist", hubIsExist + "");
			map.put("hubSpuId", hubSpuId + "");
			map.put("hubSpuNo", hubSpuNo);
			if(isSpuPendingExist!=null){
				map.put("pendingSpuId", isSpuPendingExist.getSpuPendingId() + "");	
			}
		}
		
		HubPendingSkuCheckResult checkResult = selectAndcheckSku(product,isSpuPendingExist, map);

		taskService.checkPendingSpu(isSpuPendingExist, checkResult, hubPendingSpuDto, map, checkResult.isPassing());
		boolean isPassing = Boolean.parseBoolean(map.get("isPassing"));
		if (isPassing) {
			taskService.sendToHub(hubPendingSpuDto, map);
		}
				
	}
	private List<HubPendingSpuImportDTO> handlePendingSpuXlsx(InputStream in, Task task, String type)
			throws Exception {

		XSSFSheet xssfSheet = taskService.checkXlsxExcel(in, task, type);
		if (xssfSheet == null) {
			return null;
		}
		List<HubPendingSpuImportDTO> listHubProduct = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			HubPendingSpuImportDTO product = convertSpuDTO(xssfRow);
			if (product != null) {
				listHubProduct.add(product);
			}

		}
		return listHubProduct;
	}

	private List<HubPendingSpuImportDTO> handlePendingSpuXls(InputStream in, Task task, String type)
			throws Exception {
		HSSFSheet xssfSheet = taskService.checkXlsExcel(in, task, type);
		if (xssfSheet == null) {
			return null;
		}
		List<HubPendingSpuImportDTO> listHubProduct = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			HSSFRow xssfRow = xssfSheet.getRow(rowNum);
			HubPendingSpuImportDTO product = convertSpuDTO(xssfRow);
			if (product != null) {
				listHubProduct.add(product);
			}
		}
		return listHubProduct;
	}
	

	private HubPendingSkuCheckResult selectAndcheckSku(HubPendingSpuImportDTO product, HubSpuPendingDto isSpuPendingExist, Map<String, String> map)
			throws Exception {
	
		// 如果规格为尺码，则校验spu下所有的尺码
		HubPendingSkuCheckResult checkResult = new HubPendingSkuCheckResult();
		boolean flag = true;
		StringBuffer str = new StringBuffer();
		
		if (isSpuPendingExist != null) {

			List<HubSkuPendingDto> listSku = dataHandleService.selectHubSkuPendingBySpuPendingId(isSpuPendingExist);
			if (listSku != null && listSku.size() > 0) {
				for (HubSkuPendingDto hubSkuPendingDto : listSku) {
					
					if(hubSkuPendingDto.getSkuState()!=null&&(hubSkuPendingDto.getSkuState()==SpuState.HANDLED.getIndex()||hubSkuPendingDto.getSkuState()==SpuState.HANDLING.getIndex())){
						continue;
					}
					HubPendingSkuCheckResult hubPendingSkuCheckResult = loopCheckHubSkuPending(hubSkuPendingDto,product,map);
					flag = hubPendingSkuCheckResult.isPassing();
					str.append(hubPendingSkuCheckResult.getMessage()).append(",");
				}
			} else {
				flag = false;
				str.append("无sku信息");
			}
		} else {
			flag = false;
		}
		checkResult.setPassing(flag);
		checkResult.setMessage(str.toString());
		return checkResult;
	}

	private HubPendingSkuCheckResult loopCheckHubSkuPending(HubSkuPendingDto hubSkuPendingDto,HubPendingSpuImportDTO pendingSpuImportDto,Map<String, String> map) throws Exception{
		
		boolean flag = false;
		String result = null;
		HubPendingSkuCheckResult hubPendingSkuCheckResult = new HubPendingSkuCheckResult();
		HubPendingProductImportDTO pendingSkuImportDto = new HubPendingProductImportDTO();
		pendingSkuImportDto.setSpecificationType(pendingSpuImportDto.getSpecificationType());
		pendingSkuImportDto.setHubSkuSize(hubSkuPendingDto.getHubSkuSize());
		pendingSkuImportDto.setSizeType(hubSkuPendingDto.getHubSkuSizeType());
		boolean isMultiSizeType = false;
		if ("尺码".equals(pendingSpuImportDto.getSpecificationType())|| StringUtils.isBlank(pendingSpuImportDto.getSpecificationType())) {
	
			if (hubSkuPendingDto.getHubSkuSize() != null) {
				MatchSizeResult matchSizeResult = taskService.matchSize(pendingSpuImportDto.getHubBrandNo(),pendingSpuImportDto.getHubCategoryNo(),hubSkuPendingDto.getHubSkuSize());
				if(matchSizeResult!=null){
					if (matchSizeResult.isPassing()) {
						flag = true;
						hubPendingSkuCheckResult.setSizeId(matchSizeResult.getSizeId());
						hubPendingSkuCheckResult.setSizeType(matchSizeResult.getSizeType());
						hubPendingSkuCheckResult.setSizeValue(matchSizeResult.getSizeValue());
						pendingSkuImportDto.setSizeType(matchSizeResult.getSizeType());
						
					} else {
						isMultiSizeType = matchSizeResult.isMultiSizeType();
						flag = false;
					}
					result = matchSizeResult.getResult();
				}else{
					result = "返回结果为空，校验失败";
				}
			} else {
				flag = false;
				result = hubSkuPendingDto.getSupplierSkuNo() + "尺码为空";
			}
		}else if("尺寸".equals(pendingSpuImportDto.getSpecificationType())){
			pendingSkuImportDto.setSizeType("尺寸");
			result = "校验通过：" + hubSkuPendingDto.getHubSkuSize();
			flag = true;
			hubPendingSkuCheckResult.setSizeValue(hubSkuPendingDto.getHubSkuSize());
		}else{
			result = "校验失败,规格类型无效:" + pendingSpuImportDto.getSpecificationType();
			flag = false;
		}
		
		hubPendingSkuCheckResult.setMessage(result);
		hubPendingSkuCheckResult.setPassing(flag);
		taskService.checkPendingSku(hubPendingSkuCheckResult, hubSkuPendingDto, map, pendingSkuImportDto, isMultiSizeType);
		return hubPendingSkuCheckResult;
	}

	private HubSpuPendingDto convertHubPendingProduct2PendingSpu(HubPendingSpuImportDTO product,String createUser) {
		HubSpuPendingDto HubPendingSpuDto = new HubSpuPendingDto();
		BeanUtils.copyProperties(product, HubPendingSpuDto);
		HubPendingSpuDto.setHubSeason(product.getSeasonYear() + "_" + product.getSeasonName());
		HubPendingSpuDto.setUpdateUser(createUser);
		return HubPendingSpuDto;
	}

	@SuppressWarnings("unchecked")
	private static HubPendingSpuImportDTO convertSpuDTO(XSSFRow xssfRow)  throws Exception{
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
				throw ex; 
			}
		}
		return item;
	}

	@SuppressWarnings("unchecked")
	private static HubPendingSpuImportDTO convertSpuDTO(HSSFRow xssfRow) throws Exception{
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
				throw ex; 
			}
		}
		return item;
	}
}
