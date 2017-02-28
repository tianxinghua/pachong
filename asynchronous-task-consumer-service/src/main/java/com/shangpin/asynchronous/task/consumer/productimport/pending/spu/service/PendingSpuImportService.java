package com.shangpin.asynchronous.task.consumer.productimport.pending.spu.service;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
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
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;
import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.client.product.business.size.result.MatchSizeResult;
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
	TaskImportService taskService;

	private static String[] pendingSpuValueTemplate = null;
	static {
		pendingSpuValueTemplate = TaskImportTemplate.getPendingSpuValueTemplate();
	}

	public String handMessage(ProductImportTask task) throws Exception {
		
		//从ftp下载文件
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
		return checkAndsaveHubPendingProduct(task.getTaskNo(), listHubProduct);
	}

	private List<HubPendingSpuImportDTO> handlePendingSpuXlsx(InputStream in, ProductImportTask task, String type)
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

	private List<HubPendingSpuImportDTO> handlePendingSpuXls(InputStream in, ProductImportTask task, String type)
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
	// 校验数据以及保存到hub表
	private String checkAndsaveHubPendingProduct(String taskNo, List<HubPendingSpuImportDTO> listHubProduct)
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

			//判断spuPending是否已存在
			HubSpuPendingDto hubPendingSpuDto = convertHubPendingProduct2PendingSpu(product);
			List<HubSpuPendingDto> listSpu = dataHandleService.selectPendingSpu(hubPendingSpuDto);
			HubSpuPendingDto isSpuPendingExist = null;
			if (listSpu != null && listSpu.size() > 0) {
				isSpuPendingExist = listSpu.get(0);
			}
			
			//判断hubSpu是否已存在
			List<HubSpuDto> list = dataHandleService.selectHubSpu(hubPendingSpuDto);
			if (list != null && list.size() > 0) {
				Long hubSpuId = list.get(0).getSpuId();
				String hubSpuNo = list.get(0).getSpuNo();
				boolean hubIsExist = true;
				map.put("hubIsExist", hubIsExist + "");
				map.put("hubSpuId", hubSpuId + "");
				map.put("hubSpuNo", hubSpuNo);
				map.put("pendingSpuId", isSpuPendingExist.getSpuPendingId() + "");
			}
			
			HubPendingSkuCheckResult checkResult = selectAndcheckSku(product,isSpuPendingExist, map);

			taskService.checkPendingSpu(isSpuPendingExist, checkResult, hubPendingSpuDto, map, checkResult.isPassing());
			boolean isPassing = Boolean.parseBoolean(map.get("isPassing"));
			if (isPassing) {
				taskService.sendToHub(hubPendingSpuDto, map);
			}
			
			listMap.add(map);
		}

		// 处理结果的excel上传ftp，并更新任务表状态和文件在ftp的路径
		return taskService.convertExcel(listMap, taskNo);
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
					HubPendingSkuCheckResult hubPendingSkuCheckResult = loopCheckHubSkuPending(hubSkuPendingDto,product,map);
					flag = hubPendingSkuCheckResult.isPassing();
				}
			} else {
				flag = false;
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
				if (matchSizeResult.isPassing()) {
					flag = true;
					hubPendingSkuCheckResult.setSizeId(matchSizeResult.getSizeId());
					hubPendingSkuCheckResult.setSizeType(matchSizeResult.getSizeType());
					hubPendingSkuCheckResult.setSizeValue(matchSizeResult.getSizeValue());
					pendingSkuImportDto.setSizeType(matchSizeResult.getSizeType());
				} else {
					isMultiSizeType = matchSizeResult.isMultiSizeType();
					flag = false;
					result = matchSizeResult.getResult();
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


	public void checkPendingSku(HubPendingSkuCheckResult hubPendingSkuCheckResult, HubSkuPendingDto hubSkuPendingDto,
			Map<String, String> map, HubPendingProductImportDTO product, boolean isMultiSizeType) throws Exception {
		String hubSpuNo = map.get("hubSpuNo");
		if (map.get("pendingSpuId") != null) {
			hubSkuPendingDto.setSpuPendingId(Long.valueOf(map.get("pendingSpuId")));
		}

		String specificationType = product.getSpecificationType();
		String sizeType = product.getSizeType();
		HubSkuPendingDto hubSkuPendingTempDto = taskService.findHubSkuPending(hubSkuPendingDto.getSupplierId(),
				hubSkuPendingDto.getSupplierSkuNo());
		if (hubPendingSkuCheckResult.isPassing()) {
			hubSkuPendingDto.setScreenSize(hubPendingSkuCheckResult.getSizeId());
			if (hubSkuPendingTempDto != null) {
				if (hubSpuNo != null) {
					
					HubSkuDto hubSku = dataHandleService.findHubSkuBySpuNoAndSize(hubSpuNo,product.getHubSkuSize(),product.getSizeType());
					if (hubSku != null) {
						log.info(hubSpuNo + "hub中已存在尺码:" + product.getHubSkuSize());
						hubSkuPendingDto.setSkuState((byte) SpuState.INFO_IMPECCABLE.getIndex());
					} else {
						hubSkuPendingDto.setSkuState((byte) SpuState.HANDLING.getIndex());
					}
				} else {
					hubSkuPendingDto.setSkuState((byte) SpuState.HANDLING.getIndex());
				}
			} else {
				hubSkuPendingDto.setSkuState((byte) SpuState.HANDLING.getIndex());
			}

			hubSkuPendingDto.setSpSkuSizeState((byte) 1);
			hubSkuPendingDto.setFilterFlag((byte) 1);
		} else {
			if (isMultiSizeType) {
				hubSkuPendingDto.setSkuState((byte) SpuState.INFO_PECCABLE.getIndex());
				// 此尺码含有多个尺码类型，需要手动选择
				hubSkuPendingDto.setFilterFlag((byte) 1);
				hubSkuPendingDto.setMemo("此尺码含有多个尺码类型，需要手动选择");
			} else {
				hubSkuPendingDto.setSkuState((byte) SpuState.INFO_PECCABLE.getIndex());
				// 此尺码过滤不处理
				hubSkuPendingDto.setMemo("此尺码未匹配成功");
				hubSkuPendingDto.setFilterFlag((byte) 1);
			}

			// 临时加
			hubSkuPendingDto.setFilterFlag((byte) 1);
		}
		if ("尺码".equals(specificationType) || StringUtils.isBlank(specificationType)) {
			hubSkuPendingDto.setHubSkuSizeType(sizeType);
		} else if ("排除".equals(sizeType)) {
			hubSkuPendingDto.setMemo("此尺码过滤不处理");
			hubSkuPendingDto.setFilterFlag((byte) 0);
		} else if ("尺寸".equals(specificationType)) {
			hubSkuPendingDto.setHubSkuSizeType("尺寸");
			if (hubSkuPendingDto.getHubSkuSize() == null) {
				hubSkuPendingDto.setHubSkuSize("");
			}
		}

		// 更新或插入操作
		if (hubSkuPendingTempDto != null) {
			hubSkuPendingDto.setSkuPendingId(hubSkuPendingTempDto.getSkuPendingId());
			hubSkuPendingDto.setUpdateTime(new Date());
			dataHandleService.updateHubSkuPendingByPrimaryKey(hubSkuPendingDto);
		} else {
			hubSkuPendingDto.setCreateTime(new Date());
			hubSkuPendingDto.setUpdateTime(new Date());
			hubSkuPendingDto.setDataState((byte) 1);
			dataHandleService.insertHubSkuPendingDto(hubSkuPendingDto);
		}
	}

	private HubSpuPendingDto convertHubPendingProduct2PendingSpu(HubPendingSpuImportDTO product) {
		HubSpuPendingDto HubPendingSpuDto = new HubSpuPendingDto();
		BeanUtils.copyProperties(product, HubPendingSpuDto);
		HubPendingSpuDto.setHubSeason(product.getSeasonYear() + "_" + product.getSeasonName());
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
