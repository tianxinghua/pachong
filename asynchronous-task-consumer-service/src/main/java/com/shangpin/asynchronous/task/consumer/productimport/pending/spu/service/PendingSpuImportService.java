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
import com.google.common.collect.Lists;
import com.shangpin.asynchronous.task.consumer.productimport.common.service.DataHandleService;
import com.shangpin.asynchronous.task.consumer.productimport.common.service.TaskImportService;
import com.shangpin.asynchronous.task.consumer.productimport.pending.sku.dao.HubPendingProductImportDTO;
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.dao.HubPendingSpuImportDTO;
import com.shangpin.ephub.client.data.mysql.enumeration.MsgMissHandleState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.message.task.product.body.Task;
import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.client.product.business.hubpending.spu.dto.NohandleReason;
import com.shangpin.ephub.client.product.business.hubpending.spu.gateway.HubNohandleReasonGateWay;
import com.shangpin.ephub.client.product.business.size.result.MatchSizeResult;
import com.shangpin.ephub.client.util.JsonUtil;
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
	HubSpuPendingGateWay hubSpuPendingGateWay;
	@Autowired
	DataHandleService dataHandleService;
	@Autowired
	TaskImportService taskService;
	@Autowired
	HubNohandleReasonGateWay nohandleGateWay;

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
	
	//开始校验数据
	public String checkAndsaveHubPendingProduct(String taskNo, List<HubPendingSpuImportDTO> listHubProduct,String createUser)
			throws Exception {
		
		if (listHubProduct == null) {
			return null;
		}
		
		//记录单条数据的校验结果
		Map<String, String> map = null;
		//记录所有数据的校验结果集
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
	
		for (HubPendingSpuImportDTO product : listHubProduct) {
			if (product == null || StringUtils.isBlank(product.getSupplierId())) {
				continue;
			}
			map = new HashMap<String, String>();
			map.put("taskNo", taskNo);
			map.put("spuModel", product.getSpuModel());
			//首先判断是否人工排除
			if(!filterSpu(product,createUser,map)){
				loopHandleSpuImportDto(map,product,createUser);	
			}
			listMap.add(map);
		}
		// 处理的结果以excel文件上传ftp，并更新任务表的任务状态和结果文件在ftp的路径
		return taskService.convertExcel(listMap, taskNo);
	}
	private boolean filterSpu(HubPendingSpuImportDTO product,String createUser,Map<String, String> map) {
		/**
		 * 首先添加无法处理原因
		 */
		boolean insertReason = insertNohandleReason(product,createUser);
		/**
		 * 再设置数据状态
		 */
		if(StringUtils.isNotBlank(product.getFilter())&&product.getFilter().trim().equals("排除")){
			HubSpuPendingWithCriteriaDto croteria = new HubSpuPendingWithCriteriaDto();
			HubSpuPendingDto hubPendingSpuDto = new HubSpuPendingDto();
			if(insertReason){
				hubPendingSpuDto.setMsgMissHandleState(MsgMissHandleState.HAVE_HANDLED.getIndex());
			}
			hubPendingSpuDto.setSpuState((byte)4);
			hubPendingSpuDto.setMemo("spu导入人工排除");
			hubPendingSpuDto.setUpdateUser(createUser);
			hubPendingSpuDto.setUpdateTime(new Date());
			HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
			criteria.createCriteria().andSupplierIdEqualTo(product.getSupplierId()).andSupplierSpuNoEqualTo(product.getSupplierSpuNo());
			croteria.setCriteria(criteria);
			croteria.setHubSpuPending(hubPendingSpuDto);
			map.put("taskState", "校验成功");
			map.put("processInfo", "人工排除");
			hubSpuPendingGateWay.updateByCriteriaSelective(croteria);
			return true;
		}else{
			if(insertReason){
				HubSpuPendingWithCriteriaDto croteria = new HubSpuPendingWithCriteriaDto();
				HubSpuPendingDto hubPendingSpuDto = new HubSpuPendingDto();
				hubPendingSpuDto.setMsgMissHandleState(MsgMissHandleState.HAVE_HANDLED.getIndex());
				hubPendingSpuDto.setUpdateUser(createUser);
				hubPendingSpuDto.setUpdateTime(new Date());
				hubPendingSpuDto.setSpuState((byte)0);
				hubPendingSpuDto.setAuditState((byte)0);
				HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
				criteria.createCriteria().andSupplierIdEqualTo(product.getSupplierId()).andSupplierSpuNoEqualTo(product.getSupplierSpuNo());
				croteria.setCriteria(criteria);
				croteria.setHubSpuPending(hubPendingSpuDto);
				hubSpuPendingGateWay.updateByCriteriaSelective(croteria);
				return true;
			}
		}
		return false;
	}
	
	private boolean insertNohandleReason(HubPendingSpuImportDTO product,String createUser){
		if(StringUtils.isNotBlank(product.getReason1()) || StringUtils.isNotBlank(product.getReason2()) || StringUtils.isNotBlank(product.getReason3()) || StringUtils.isNotBlank(product.getReason4())){
			NohandleReason nohandleReason = new NohandleReason();
			nohandleReason.setSupplierId(product.getSupplierId());
			nohandleReason.setSupplierSpuNo(product.getSupplierSpuNo());
			nohandleReason.setCreateUser(createUser); 
			List<String> reasons = Lists.newArrayList();
			add(reasons,product.getReason1());
			add(reasons,product.getReason2());
			add(reasons,product.getReason3());
			add(reasons,product.getReason4());
			nohandleReason.setReasons(reasons);
			log.info("插入无法处理实体===="+JsonUtil.serialize(nohandleReason)); 
			return nohandleGateWay.insertNohandleReason(nohandleReason);
		}else{
			return false;
		}
	}
	
	private void add(List<String> reasons, String e){
		if(StringUtils.isNotBlank(e)){
			reasons.add(e);
		}
	}
	
	private void loopHandleSpuImportDto(Map<String, String> map, HubPendingSpuImportDTO product,String createUser) throws Exception{
		
		//excel数据转换为数据库对象
		HubSpuPendingDto hubPendingSpuDto = convertHubPendingProduct2PendingSpu(product,createUser);
		
		//判断spuPending是否已存在
		List<HubSpuPendingDto> listSpu = dataHandleService.selectPendingSpu(hubPendingSpuDto);
		HubSpuPendingDto isSpuPendingExist = null;
		if (listSpu != null && listSpu.size() > 0) {
			isSpuPendingExist = listSpu.get(0);
			map.put("pendingSpuId", isSpuPendingExist.getSpuPendingId() + "");
		}
		
//		//判断hubSpu是否已存在
//		HubSpuDto hubSpuDto = dataHandleService.selectHubSpu(hubPendingSpuDto.getSpuModel(),hubPendingSpuDto.getHubBrandNo());
//		if (hubSpuDto != null) {
//			if(hubSpuDto.getHubColor().equals(product.getHubColor())){
//				Long hubSpuId = hubSpuDto.getSpuId();
//				String hubSpuNo = hubSpuDto.getSpuNo();
//				boolean hubIsExist = true;
//				map.put("hubIsExist", hubIsExist + "");
//				map.put("hubSpuId", hubSpuId + "");
//				map.put("hubSpuNo", hubSpuNo);
//				if(isSpuPendingExist!=null){
//					
//				}	
//			}else{
//				//同品牌同货号不同颜色
//				map.put("taskState", "校验失败");
//				map.put("processInfo", "同品牌同货号，颜色不一样");
//				dataHandleService.updateHubSpuPending(hubPendingSpuDto);
//				return;
//			}
//		}
		HubPendingSkuCheckResult checkResult = selectAndcheckSku(product,isSpuPendingExist, map);
		taskService.checkPendingSpu(isSpuPendingExist, checkResult, hubPendingSpuDto, map, checkResult.isPassing());
//		boolean isPassing = Boolean.parseBoolean(map.get("isPassing"));
//		if (isPassing) {
//			taskService.sendToHub(hubPendingSpuDto, map);
//		}
				
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
			boolean allFliter = true;
			boolean noSku = false;
			List<HubSkuPendingDto> listSku = dataHandleService.selectHubSkuPendingBySpuPendingId1(isSpuPendingExist);
			if (listSku != null && listSku.size() > 0) {
				//判断sku是否都过滤或者都已处理
				for (HubSkuPendingDto hubSkuPendingDto : listSku) {
					HubPendingSkuCheckResult hubPendingSkuCheckResult = loopCheckHubSkuPending(hubSkuPendingDto,product,map);
					if(hubSkuPendingDto.getFilterFlag()==1){
						allFliter = false;
					}
					if(!hubPendingSkuCheckResult.isPassing()){
						flag = false;	
					}
					str.append(hubPendingSkuCheckResult.getMessage()).append(",");
				}
				map.put("allFilter",allFliter+"");
			} else {
				noSku = true;
				map.put("noSku",noSku+"");
				str.append("无sku信息或者sku都已处理");
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
					hubPendingSkuCheckResult.setSizeValue(matchSizeResult.getSizeValue());
					if (matchSizeResult.isPassing()) {
						flag = true;
						hubPendingSkuCheckResult.setSizeId(matchSizeResult.getSizeId());
						hubPendingSkuCheckResult.setSizeType(matchSizeResult.getSizeType());
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
