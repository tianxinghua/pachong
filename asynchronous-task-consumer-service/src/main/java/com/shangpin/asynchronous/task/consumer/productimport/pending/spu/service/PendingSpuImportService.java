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
import com.shangpin.ephub.client.data.mysql.product.gateway.PengdingToHubGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
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
import com.shangpin.ephub.client.product.business.size.dto.MatchSizeDto;
import com.shangpin.ephub.client.product.business.size.gateway.MatchSizeGateWay;
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
	HubSkuGateWay hubSkuGateWay;
	@Autowired
	MatchSizeGateWay matchSizeGateWay;
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
	
	
	public String handMessage(ProductImportTask task) throws Exception {

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
		return checkAndsaveHubPendingProduct(task.getTaskNo(), listHubProduct);
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
	private String  checkAndsaveHubPendingProduct(String taskNo, List<HubPendingSpuImportDTO> listHubProduct)
			throws Exception {
		if (listHubProduct == null) {
			return null;
		}
		
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		for (HubPendingSpuImportDTO product : listHubProduct) {
			map = new HashMap<String, String>();
			map.put("taskNo", taskNo);
			map.put("spuModel", product.getSpuModel());
			
			// 查询是否已存在pendingSpu表中
			HubSpuPendingDto hubPendingSpuDto = convertHubPendingProduct2PendingSpu(product);
			List<HubSpuPendingDto> listSpu = dataHandleService.selectPendingSpu(hubPendingSpuDto);
			HubSpuPendingDto isSpuPendingExist = null;
			if (listSpu != null && listSpu.size() > 0) {
				log.info(product.getSpuModel()+"已存在hub");
				isSpuPendingExist = listSpu.get(0);
			}
			
			//如果规格为尺码，则校验spu下所有的尺码
			boolean flag = true;
			HubPendingSkuCheckResult checkResult = new HubPendingSkuCheckResult();
			StringBuffer str = new StringBuffer();
			if(isSpuPendingExist!=null){
					HubSkuPendingCriteriaDto criteria = new HubSkuPendingCriteriaDto();
					criteria.createCriteria().andSupplierIdEqualTo(product.getSupplierId()).andSpuPendingIdEqualTo(isSpuPendingExist.getSpuPendingId());
					List<HubSkuPendingDto> listSku = hubSkuPendingGateWay.selectByCriteria(criteria);
					if(listSku!=null&&listSku.size()>0){
						for(HubSkuPendingDto sku:listSku){
							HubPendingSkuCheckResult hubPendingSkuCheckResult = new HubPendingSkuCheckResult();
							HubPendingProductImportDTO product1 = new HubPendingProductImportDTO();
							product1.setSpecificationType(product.getSpecificationType());
							product1.setHubSkuSize(sku.getHubSkuSize());
							boolean isMultiSizeType = false;
							if("尺码".equals(product.getSpecificationType())||StringUtils.isBlank(product.getSpecificationType())){
								MatchSizeDto match = new MatchSizeDto();
								match.setHubBrandNo(product.getHubBrandNo());
								match.setHubCategoryNo(product.getHubCategoryNo());
								match.setSize(sku.getHubSkuSize());	
								MatchSizeResult matchSizeResult = matchSizeGateWay.matchSize(match);
								if(matchSizeResult.isPassing()){
									hubPendingSkuCheckResult.setPassing(true);
									hubPendingSkuCheckResult.setResult(matchSizeResult.getSizeId()+","+matchSizeResult.getSizeType()+":"+matchSizeResult.getSizeValue());
									String sizeType = matchSizeResult.getSizeType();
									product1.setSizeType(sizeType);
								}else{
									isMultiSizeType = matchSizeResult.isMultiSizeType();
									hubPendingSkuCheckResult.setPassing(false);
									flag = false;
									str.append(matchSizeResult.getResult()).append(",");
								}
							}else{
								hubPendingSkuCheckResult.setPassing(true);
								hubPendingSkuCheckResult.setResult(sku.getHubSkuSize());
							}
							checkPendingSku(hubPendingSkuCheckResult, sku, map,product1,isMultiSizeType);
						}
					}
			}
			// 校验sku信息
			checkResult.setPassing(flag);
			checkResult.setResult(str.toString());
			taskService.checkPendingSpu(isSpuPendingExist,checkResult,hubPendingSpuDto,map,flag);
			
			boolean isPassing = Boolean.parseBoolean(map.get("isPassing"));
			if (isPassing) {
				taskService.sendToHub(hubPendingSpuDto, map);
			}
			
			listMap.add(map);
		}
		
		// 处理结果的excel上传ftp，并更新任务表状态和文件在ftp的路径
		return taskService.convertExcel(listMap, taskNo);
	}
	
	@SuppressWarnings("unused")
	public void checkPendingSku(HubPendingSkuCheckResult hubPendingSkuCheckResult, HubSkuPendingDto hubSkuPendingDto,
			 Map<String, String> map,HubPendingProductImportDTO product,boolean isMultiSizeType) throws Exception{
		String hubSpuNo = map.get("hubSpuNo");
		if (map.get("pendingSpuId") != null) {
			hubSkuPendingDto.setSpuPendingId(Long.valueOf(map.get("pendingSpuId")));
		}

		String specificationType = product.getSpecificationType();
		String sizeType = product.getSizeType();
		HubSkuPendingDto hubSkuPendingTempDto = taskService.findHubSkuPending(hubSkuPendingDto.getSupplierId(),
				hubSkuPendingDto.getSupplierSkuNo());
		if (hubPendingSkuCheckResult.isPassing()) {
			String result = hubPendingSkuCheckResult.getResult();
			String size = null;
			if (StringUtils.isNotBlank(result)) {
				if(result.split(",").length>1){
					String sizeId = result.split(",")[0];
					hubSkuPendingDto.setScreenSize(sizeId);
				}else{
					hubSkuPendingDto.setHubSkuSize(result);
				}
			}
			if(hubSkuPendingTempDto!=null){
				if(hubSpuNo!=null){
					HubSkuCriteriaDto sku = new HubSkuCriteriaDto();
					if(product.getHubSkuSize()!=null&&product.getSizeType()!=null){
						sku.createCriteria().andSpuNoEqualTo(hubSpuNo).andSkuSizeEqualTo(product.getHubSkuSize()).andSkuSizeTypeEqualTo(product.getSizeType());	
					}else{
						sku.createCriteria().andSpuNoEqualTo(hubSpuNo);
					}
					List<HubSkuDto> listSku = hubSkuGateWay.selectByCriteria(sku);
					if(listSku!=null&&listSku.size()>0){
						log.info(hubSpuNo+"hub中已存在尺码:"+size);
						hubSkuPendingDto.setSkuState((byte) SpuState.INFO_IMPECCABLE.getIndex());
					}else{
						hubSkuPendingDto.setSkuState((byte) SpuState.HANDLING.getIndex());
					}
				}else{
					hubSkuPendingDto.setSkuState((byte) SpuState.HANDLING.getIndex());
				}
			}else{
				hubSkuPendingDto.setSkuState((byte) SpuState.HANDLING.getIndex());
			}
			
			hubSkuPendingDto.setSpSkuSizeState((byte) 1);
			hubSkuPendingDto.setFilterFlag((byte)1);
		} else {
			if(isMultiSizeType){
				hubSkuPendingDto.setSkuState((byte) SpuState.INFO_PECCABLE.getIndex());
				//此尺码含有多个尺码类型，需要手动选择
				hubSkuPendingDto.setMemo("此尺码含有多个尺码类型，需要手动选择");
			}else{
				hubSkuPendingDto.setSkuState((byte) SpuState.INFO_PECCABLE.getIndex());
				//此尺码过滤不处理
				hubSkuPendingDto.setMemo("此尺码过滤不处理");
			}
		}
		if("尺码".equals(specificationType)||StringUtils.isBlank(specificationType)){
			hubSkuPendingDto.setHubSkuSizeType(sizeType);
		}else{
			hubSkuPendingDto.setHubSkuSizeType("尺寸");
			if(hubSkuPendingDto.getHubSkuSize()==null){
				hubSkuPendingDto.setHubSkuSize("");
			}
		}
		
		//更新或插入操作
		if (hubSkuPendingTempDto != null) {
			hubSkuPendingDto.setSkuPendingId(hubSkuPendingTempDto.getSkuPendingId());
			hubSkuPendingDto.setUpdateTime(new Date());
			hubSkuPendingGateWay.updateByPrimaryKeySelective(hubSkuPendingDto);
		} else {
			hubSkuPendingDto.setCreateTime(new Date());
			hubSkuPendingDto.setUpdateTime(new Date());
			hubSkuPendingDto.setDataState((byte) 1);
			hubSkuPendingGateWay.insert(hubSkuPendingDto);
		}
	}
	private HubSkuCheckDto convertHubPendingProduct2PendingSkuCheck(HubPendingSpuImportDTO product,String size) {
		HubSkuCheckDto hubPendingSkuDto = new HubSkuCheckDto();
		hubPendingSkuDto.setBrandNo(product.getHubBrandNo());
		hubPendingSkuDto.setCategoryNo(product.getHubCategoryNo());
		hubPendingSkuDto.setSkuSize(size);
		hubPendingSkuDto.setSpuModel(product.getSpuModel());
		hubPendingSkuDto.setSpecificationType(product.getSpecificationType());
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
