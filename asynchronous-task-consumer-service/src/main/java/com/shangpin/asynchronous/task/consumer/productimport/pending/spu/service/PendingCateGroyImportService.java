package com.shangpin.asynchronous.task.consumer.productimport.pending.spu.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.shangpin.asynchronous.task.consumer.productexport.template.TaskImportTemplate2;
import com.shangpin.asynchronous.task.consumer.productimport.common.service.DataHandleService;
import com.shangpin.asynchronous.task.consumer.productimport.common.service.TaskImportService;
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.dao.HubColorImportDTO;
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.dao.HubPendingCateGroyImportDTO;
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.dao.HubPendingSpuImportDTO;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.categroy.gateway.HubSupplierCategroyDicGateWay;
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
import com.shangpin.ephub.client.product.business.hubpending.spu.gateway.HubPendingHandleGateWay;
import com.shangpin.ephub.client.product.business.mapp.dto.HubSupplierCategoryDicRequestDto;
import com.shangpin.ephub.client.product.business.mapp.gateway.DicRefreshGateWay;
import com.shangpin.ephub.client.product.business.size.result.MatchSizeResult;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.client.util.TaskImportTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
public class PendingCateGroyImportService {


	@Autowired
	HubSpuPendingGateWay hubSpuPendingGateWay;
	@Autowired
	DataHandleService dataHandleService;
	@Autowired
	TaskImportService taskService;
	@Autowired
	HubNohandleReasonGateWay nohandleGateWay;
	@Autowired
	HubPendingHandleGateWay pendingHandleGateWay;
	@Autowired
	HubSupplierCategroyDicGateWay hubSupplierCategroyDicGateWay;
	@Autowired
	DicRefreshGateWay dicRefreshGateWay;

	private static String[] pendingCateGroyTemplate = null;
	static {
		pendingCateGroyTemplate = TaskImportTemplate2.getCategoryValueTemplate();
	}
	public String handMessage(Task task) throws Exception {

		//从ftp下载文件
		JSONObject json = JSONObject.parseObject(task.getData());
		String filePath = json.get("taskFtpFilePath").toString();
		String createUser = json.get("createUser").toString();
		System.out.println("createUser-----"+createUser);
		task.setData(filePath);

		InputStream in = taskService.downFileFromFtp(task);

		//excel转对象
		List<HubPendingCateGroyImportDTO> hubPendingCateGroyImportDTO = null;
		String fileFormat = task.getData().split("\\.")[1];
		if ("xls".equals(fileFormat)) {
			hubPendingCateGroyImportDTO = handlePendingCateGroyXls(in, task, "categroy");
		} else if ("xlsx".equals(fileFormat)) {
			hubPendingCateGroyImportDTO = handlePendingCateGroyXlsx(in, task, "categroy");
		}

		//校验数据并把校验结果写入excel
		return checkAndsaveHubPendingProduct(task.getTaskNo(), hubPendingCateGroyImportDTO,createUser);
	}
	private List<HubPendingCateGroyImportDTO> handlePendingCateGroyXlsx(InputStream in, Task task, String type)
			throws Exception {

		XSSFSheet xssfSheet = taskService.checkXlsxExcel(in, task, type);
		if (xssfSheet == null) {
			return null;
		}
		List<HubPendingCateGroyImportDTO> listHubProduct = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			HubPendingCateGroyImportDTO product = convertCategroyXlsxDTO(xssfRow);
			if (product != null) {
				listHubProduct.add(product);
			}

		}
		return listHubProduct;
	}



	//开始校验数据
	public String checkAndsaveHubPendingProduct(String taskNo, List<HubPendingCateGroyImportDTO> hubPendingCateGroyImportDTOS,String createUser)
			throws Exception {

		if (hubPendingCateGroyImportDTOS == null) {
			return null;
		}
		//记录所有数据的校验结果集
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();

		for (HubPendingCateGroyImportDTO productImport : hubPendingCateGroyImportDTOS) {
			if (productImport == null) {
				continue;
			}
			//记录单条数据的校验结果
			Map<String, String> map = new HashMap<String, String>();
			map.put("taskNo", taskNo);
			Map<String, String> map1 = filterCateGroy(productImport, createUser, map);
			listMap.add(map1);
		}
		// 处理的结果以excel文件上传ftp，并更新任务表的任务状态和结果文件在ftp的路径
		return taskService.convertExcelCategory(listMap, taskNo);
	}
	private Map<String, String>  filterCateGroy(HubPendingCateGroyImportDTO productImport,String createUser,Map<String, String> map) throws ParseException {
		HubSupplierCategroyDicDto categroyDicDto2 = hubSupplierCategroyDicGateWay.selectByPrimaryKey(Long.parseLong(productImport.getSupplierCategoryDicId()));
		    if (productImport.getSupplierCategoryDicId()!=null){

			HubSupplierCategroyDicDto categroyDicDto = new HubSupplierCategroyDicDto();
			categroyDicDto.setSupplierCategoryDicId(Long.parseLong(productImport.getSupplierCategoryDicId()));
			//map.put("supplierCategoryDicId",productImport.getSupplierCategoryDicId());
			if (productImport.getSupplierCategory()!=null){
				categroyDicDto.setSupplierCategory(productImport.getSupplierCategory());
				map.put("supplierCategory",productImport.getSupplierCategory());
			}if (productImport.getSupplierGender()!=null){
				categroyDicDto.setSupplierGender(productImport.getSupplierGender());
			}/*if (productImport.getCategoryType()!=null){
				categroyDicDto.setCategoryType(Byte.parseByte(productImport.getCategoryType()));
			}*/if (productImport.getHubCategoryNo()!=null){
				categroyDicDto.setHubCategoryNo(productImport.getHubCategoryNo());
				byte[] bytes = productImport.getHubCategoryNo().getBytes();
				if (bytes.length==12){
					categroyDicDto.setCategoryType((byte)4);
					categroyDicDto.setMappingState((byte)1);
				}else  if (bytes.length==9){
					categroyDicDto.setCategoryType((byte)3);
					categroyDicDto.setMappingState((byte)2);
				}else  if (bytes.length==6){
					categroyDicDto.setCategoryType((byte)2);
					categroyDicDto.setMappingState((byte)2);
				}else {
					categroyDicDto.setCategoryType((byte)1);
					categroyDicDto.setMappingState((byte)2); }
				map.put("hubCategoryNo",productImport.getHubCategoryNo());
			}

			categroyDicDto.setUpdateTime(new Date());
			if (createUser!=null){
				categroyDicDto.setUpdateUser(createUser);
			}
			int i = hubSupplierCategroyDicGateWay.updateByPrimaryKey(categroyDicDto);
			if (i==1){
				map.put("task","校验成功");
			}else {
				map.put("task","校验失败");
			}
			HubSupplierCategoryDicRequestDto hubSupplierCategoryDicRequestDto =new HubSupplierCategoryDicRequestDto();
			if (productImport.getSupplierId()!=null){
				hubSupplierCategoryDicRequestDto.setSupplierId(productImport.getSupplierId());
			}if (productImport.getSupplierCategoryDicId()!=null){
				hubSupplierCategoryDicRequestDto.setSupplierCategoryDicId(Long.parseLong(productImport.getSupplierCategoryDicId()));
				}if (productImport.getHubCategoryNo()!=null){
					hubSupplierCategoryDicRequestDto.setHubCategoryNo(productImport.getSupplierCategoryDicId());
				}
				dicRefreshGateWay.categoryRefresh(hubSupplierCategoryDicRequestDto);

		/*if (productImport.getHubCategoryNo()!=null ){
				if (!categroyDicDto2.getHubCategoryNo().equals(productImport.getHubCategoryNo()) || categroyDicDto2.getHubCategoryNo()==null){
					dicRefreshGateWay.categoryRefresh(hubSupplierCategoryDicRequestDto);
				}
			}*/
			return map ;

		}else {
			//添加
			HubSupplierCategroyDicDto categroyDicDto =new  HubSupplierCategroyDicDto();
			if (productImport.getSupplierCategory()!=null){
				categroyDicDto.setSupplierCategory(productImport.getSupplierCategory());
				map.put("supplierCategory",productImport.getSupplierCategory());
			}if (productImport.getSupplierGender()!=null){
				categroyDicDto.setSupplierGender(productImport.getSupplierGender());
			}if (productImport.getCategoryType()!=null){
				categroyDicDto.setCategoryType(Byte.parseByte(productImport.getCategoryType()));
			}if (productImport.getHubCategoryNo()!=null){
				categroyDicDto.setHubCategoryNo(productImport.getHubCategoryNo());
				map.put("hubCategoryNo",productImport.getHubCategoryNo());
			}if (productImport.getMappingState()!=null){
				categroyDicDto.setMappingState(Byte.parseByte(productImport.getMappingState()));
			}
			categroyDicDto.setCreateTime(new Date());
			hubSupplierCategroyDicGateWay.insert(categroyDicDto);
			map.put("task","成功");
			HubSupplierCategoryDicRequestDto hubSupplierCategoryDicRequestDto = new HubSupplierCategoryDicRequestDto();
			if (productImport.getSupplierCategory()!=null){
				hubSupplierCategoryDicRequestDto.setSupplierCategory(productImport.getSupplierCategory());
			}if (productImport.getSupplierId()!=null){
				hubSupplierCategoryDicRequestDto.setSupplierId(productImport.getSupplierId());
			}
			if (productImport.getHubCategoryNo()!=null){
				hubSupplierCategoryDicRequestDto.setHubCategoryNo(productImport.getHubCategoryNo());
			}if (productImport.getCategoryType()!=null){
				hubSupplierCategoryDicRequestDto.setCategoryType(Byte.parseByte(productImport.getCategoryType()));
			}
			dicRefreshGateWay.categoryRefresh(hubSupplierCategoryDicRequestDto);

			return map;

		}
	}
	

	
	private void add(List<String> reasons, String e){
		if(StringUtils.isNotBlank(e)){
			reasons.add(e);
		}
	}
	


	private List<HubPendingCateGroyImportDTO> handlePendingCateGroyXls(InputStream in, Task task, String type)
			throws Exception {
		HSSFSheet xssfSheet = taskService.checkXlsExcel(in, task, type);
		if (xssfSheet == null) {
			return null;
		}
		List<HubPendingCateGroyImportDTO> listHubProduct = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			HSSFRow xssfRow = xssfSheet.getRow(rowNum);
			HubPendingCateGroyImportDTO product = convertCateGroyDTO(xssfRow);
			if (product != null) {
				listHubProduct.add(product);
			}
		}
		return listHubProduct;
	}
	



	private HubPendingSkuCheckResult loopCheckHubSkuPending(HubSkuPendingDto hubSkuPendingDto,
			HubPendingSpuImportDTO pendingSpuImportDto,
			Map<String, String> map) throws Exception{
		
		boolean flag = false;
		String result = null;
		HubPendingSkuCheckResult hubPendingSkuCheckResult = new HubPendingSkuCheckResult();
		boolean isMultiSizeType = false;
		if ("尺码".equals(pendingSpuImportDto.getSpecificationType())|| 
				StringUtils.isBlank(pendingSpuImportDto.getSpecificationType())) {
	
			if (hubSkuPendingDto.getHubSkuSize() != null) {
				MatchSizeResult matchSizeResult = taskService.matchSize(pendingSpuImportDto.getHubBrandNo(),pendingSpuImportDto.getHubCategoryNo(),hubSkuPendingDto.getHubSkuSize());
				if(matchSizeResult!=null){
					hubPendingSkuCheckResult.setSizeValue(matchSizeResult.getSizeValue());
					if (matchSizeResult.isPassing()) {
						flag = true;
						hubPendingSkuCheckResult.setSizeId(matchSizeResult.getSizeId());
						hubPendingSkuCheckResult.setSizeType(matchSizeResult.getSizeType());
						result = matchSizeResult.getResult();
					}else if(matchSizeResult.isMultiSizeType()) {//多个匹配  失败 增加备注
						isMultiSizeType = matchSizeResult.isMultiSizeType();
						result = " " + hubSkuPendingDto.getHubSkuSize() + "多个匹配,失败";
					}else  if(matchSizeResult.isFilter()){//有模板没匹配上
//						hubPendingSkuCheckResult.setSizeId(matchSizeResult.getSizeId());
						hubPendingSkuCheckResult.setSizeType("排除");
						hubPendingSkuCheckResult.setFilter(true);
						result =" " + hubSkuPendingDto.getHubSkuSize() + "有模板没匹配上,排除";
					}else{
					   //不做处理
						result =" " + hubSkuPendingDto.getHubSkuSize() + "未找到品牌品类尺码，不做处理";
					}
				}else{
					result =  hubSkuPendingDto.getHubSkuSize() +" 返回结果为空，校验失败";
				}
			} else {
				result = hubSkuPendingDto.getSupplierSkuNo() + "尺码为空";
			}
		}else if("尺寸".equals(pendingSpuImportDto.getSpecificationType())){
			result = "校验通过：" + hubSkuPendingDto.getHubSkuSize();
			flag = true;
			hubPendingSkuCheckResult.setSizeType("尺寸");
			hubPendingSkuCheckResult.setSizeValue(hubSkuPendingDto.getHubSkuSize());
		}else{
			result = "校验失败,规格类型无效:" + pendingSpuImportDto.getSpecificationType();

		}
		
		hubPendingSkuCheckResult.setMessage(result);
		hubPendingSkuCheckResult.setPassing(flag);
		taskService.checkPendingSku(hubPendingSkuCheckResult, hubSkuPendingDto, map, isMultiSizeType);
		return hubPendingSkuCheckResult;
	}

	private HubSpuPendingDto convertHubPendingProduct2PendingSpu(HubPendingSpuImportDTO product,String createUser) {
		HubSpuPendingDto HubPendingSpuDto = new HubSpuPendingDto();
		BeanUtils.copyProperties(product, HubPendingSpuDto);
		if(StringUtils.isNotBlank(product.getSeasonYear())&&StringUtils.isNotBlank(product.getSeasonName())){
			HubPendingSpuDto.setHubSeason(product.getSeasonYear() + "_" + product.getSeasonName());
		}
		
		HubPendingSpuDto.setUpdateUser(createUser);
		return HubPendingSpuDto;
	}




	@SuppressWarnings("unchecked")
	private static HubPendingCateGroyImportDTO convertCateGroyDTO(HSSFRow xssfRow) throws Exception{
		HubPendingCateGroyImportDTO item = null;
		if (xssfRow != null) {
			try {
				item = new HubPendingCateGroyImportDTO();
				Class c = item.getClass();
				for (int i = 0; i <pendingCateGroyTemplate.length; i++) {
					if (xssfRow.getCell(i) != null) {
						xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
						String setMethodName = "set" + pendingCateGroyTemplate[i].toUpperCase().charAt(0)
								+ pendingCateGroyTemplate[i].substring(1);
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
	private static HubPendingCateGroyImportDTO convertCategroyXlsxDTO(XSSFRow xssfRow) {
		HubPendingCateGroyImportDTO item=null;
		if (xssfRow != null) {
			try {
				item = new HubPendingCateGroyImportDTO();
				Class c = item.getClass();
				for (int i = 0; i < pendingCateGroyTemplate.length; i++) {
					if (xssfRow.getCell(i) != null) {
						xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
						String setMethodName = "set" + pendingCateGroyTemplate[i].toUpperCase().charAt(0)
								+ pendingCateGroyTemplate[i].substring(1);
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
