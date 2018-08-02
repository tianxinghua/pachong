package com.shangpin.asynchronous.task.consumer.productimport.pending.spu.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.shangpin.asynchronous.task.consumer.productexport.template.TaskImportTemplate2;
import com.shangpin.asynchronous.task.consumer.productimport.common.service.DataHandleService;
import com.shangpin.asynchronous.task.consumer.productimport.common.service.TaskImportService;
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.dao.HubMadeImportDTO;
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.dao.HubPendingSpuImportDTO;
import com.shangpin.ephub.client.data.mysql.enumeration.MsgMissHandleState;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
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
import com.shangpin.ephub.client.product.business.mapp.dto.HubSupplierSizeDicRequestDto;
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
public class PendingMadeImportService {
	

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
	HubSupplierValueMappingGateWay hubSupplierValueMappingGateWay;
	@Autowired
	DicRefreshGateWay dicRefreshGateWay;
	@Autowired


	private static String[] pendingSpuValueTemplate = null;
	static {
		pendingSpuValueTemplate = TaskImportTemplate.getPendingSpuValueTemplate();
	}
	private static String[] madeValueTemplate=null;
	static {
		 madeValueTemplate = TaskImportTemplate2.getMadeValueTemplate();
	}

	public String handMessage(Task task) throws Exception {
		
		//从ftp下载文件
		JSONObject json = JSONObject.parseObject(task.getData());
		String filePath = json.get("taskFtpFilePath").toString();
		String createUser = json.get("createUser").toString();
		task.setData(filePath);
		
		InputStream in = taskService.downFileFromFtp(task);
		
		//excel转对象
		List<HubMadeImportDTO> hubMadeImportDTO = null;
		String fileFormat = task.getData().split("\\.")[1];
		if ("xls".equals(fileFormat)) {
			hubMadeImportDTO = handlePendingMadeXls(in, task, "made");
		} else if ("xlsx".equals(fileFormat)) {
			hubMadeImportDTO = handlePendingMadeXlsx(in, task, "made");
		}

		//校验数据并把校验结果写入excel
		return checkAndsaveHubPendingProduct(task.getTaskNo(), hubMadeImportDTO,createUser);
	}
	
	//开始校验数据
	public String checkAndsaveHubPendingProduct(String taskNo, List<HubMadeImportDTO> listHubMadeImportDTO,String createUser)
			throws Exception {
		
		if (listHubMadeImportDTO == null) {
			return null;
		}
		
		//记录单条数据的校验结果

		//记录所有数据的校验结果集
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
	
		for (HubMadeImportDTO productImport : listHubMadeImportDTO) {
			if (productImport == null ) {
				continue;
			}
			Map<String, String>	map = new HashMap<String, String>();
			map.put("taskNo", taskNo);
			//首先判断是否人工排除
			Map<String, String> map1 = filterMade(productImport, createUser, map);
			listMap.add(map1);
		}
		// 处理的结果以excel文件上传ftp，并更新任务表的任务状态和结果文件在ftp的路径
		return taskService.convertExcelMade(listMap, taskNo);
	}
	private Map<String, String>filterMade(HubMadeImportDTO productImport,String createUser,Map<String, String> map) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (productImport.getHubSupplierValMappingId()!=null){
			//先查数据库，
			HubSupplierValueMappingDto hubSupplierValueMappingDto1 = hubSupplierValueMappingGateWay.selectByPrimaryKey(Long.parseLong(productImport.getHubSupplierValMappingId()));
			HubSupplierValueMappingDto hubSupplierValueMappingDto =new HubSupplierValueMappingDto() ;
			if (productImport.getHubSupplierValMappingId()!=null){
				hubSupplierValueMappingDto.setHubSupplierValMappingId(Long.parseLong(productImport.getHubSupplierValMappingId()));
			}
			if (productImport.getSupplierVal()!=null){
				hubSupplierValueMappingDto.setSupplierVal(productImport.getSupplierVal());
				map.put("supplierVal",(productImport.getSupplierVal()));
			}
			if (productImport.getHubVal()!=null){
				hubSupplierValueMappingDto.setHubVal(productImport.getHubVal());
				hubSupplierValueMappingDto.setMappingType((byte)1);
				map.put("hubVal",(productImport.getHubVal()));
			}
			hubSupplierValueMappingDto.setHubValType((byte)3);
			hubSupplierValueMappingDto.setUpdateTime(new Date());
			int i = hubSupplierValueMappingGateWay.updateByPrimaryKeySelective(hubSupplierValueMappingDto);
            if (i==1){
            	map.put("task","校验成功");
			}else {
				map.put("task","失败");
			}
			HubSupplierSizeDicRequestDto hubSupplierSizeDicRequestDto = new HubSupplierSizeDicRequestDto();
			hubSupplierSizeDicRequestDto.setHubSupplierValMappingId(Long.parseLong(productImport.getHubSupplierValMappingId()));
			if (productImport.getHubVal()!=null){
				hubSupplierSizeDicRequestDto.setHubVal(productImport.getHubVal());
			}if (productImport.getSupplierVal()!=null){
				hubSupplierSizeDicRequestDto.setSupplierVal(productImport.getSupplierVal());
			}
			if (hubSupplierValueMappingDto1.getHubVal()!=null){
				if (!hubSupplierValueMappingDto1.getHubVal().equals(productImport.getHubVal())){
					dicRefreshGateWay.originRefresh(hubSupplierSizeDicRequestDto);
				}
			}
			return map;

		}else {

			HubSupplierValueMappingDto hubSupplierValueMappingDto = new HubSupplierValueMappingDto();
			if (productImport.getSupplierVal()!=null){
				hubSupplierValueMappingDto.setSupplierVal(productImport.getSupplierVal());
				map.put("supplierVal",(productImport.getSupplierVal()));
			}
			if (productImport.getHubVal()!=null){
				hubSupplierValueMappingDto.setHubVal(productImport.getHubVal());
				map.put("hubVal",(productImport.getHubVal()));
			}
			hubSupplierValueMappingDto.setCreateTime(new Date());
			hubSupplierValueMappingGateWay.insert(hubSupplierValueMappingDto);
			HubSupplierSizeDicRequestDto hubSupplierSizeDicRequestDto =new HubSupplierSizeDicRequestDto();
			if (productImport.getHubVal()!=null){
				hubSupplierSizeDicRequestDto.setHubVal(productImport.getHubVal());
			}if (productImport.getSupplierVal()!=null){
				hubSupplierSizeDicRequestDto.setSupplierVal(productImport.getHubVal());
			}

			dicRefreshGateWay.originRefresh(hubSupplierSizeDicRequestDto);
			return map;
		}





/*
		else {
			HubSupplierSizeDicRequestDto hubSupplierSizeDicRequestDto = null;

			HubSupplierValueMappingDto hubSupplierValueMappingDto =null ;
			hubSupplierValueMappingDto.setSupplierVal(productImport.getSupplierVal());
			hubSupplierValueMappingDto.setHubVal(productImport.getHubVal());
			hubSupplierValueMappingDto.setCreateTime(format.parse(productImport.getCreateTime()));
			hubSupplierValueMappingDto.setHubValType((byte)3);
			hubSupplierValueMappingDto.setUpdateTime(format.parse(productImport.getUpdateTime()));
			hubSupplierValueMappingGateWay.insert(hubSupplierValueMappingDto);
			hubSupplierSizeDicRequestDto.setHubVal(hubSupplierValueMappingDto.getHubVal());
			hubSupplierSizeDicRequestDto.setMappingType(hubSupplierValueMappingDto.getHubValType());
			hubSupplierSizeDicRequestDto.setSupplierVal(hubSupplierValueMappingDto.getSupplierVal());
			dicRefreshGateWay.originRefresh(hubSupplierSizeDicRequestDto);

		}*/
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
	

	private List<HubMadeImportDTO> handlePendingMadeXlsx(InputStream in, Task task, String type)
			throws Exception {

		XSSFSheet xssfSheet = taskService.checkXlsxExcel(in, task, type);
		if (xssfSheet == null) {
			return null;
		}
		List<HubMadeImportDTO> listHubProduct = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			HubMadeImportDTO product = convertMadeDTO(xssfRow);
			if (product != null) {
				listHubProduct.add(product);
			}

		}
		return listHubProduct;
	}

	private List<HubMadeImportDTO> handlePendingMadeXls(InputStream in, Task task, String type)
			throws Exception {
		HSSFSheet xssfSheet = taskService.checkXlsExcel(in, task, type);
		if (xssfSheet == null) {
			return null;
		}
		List<HubMadeImportDTO> listHubProduct = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			HSSFRow xssfRow = xssfSheet.getRow(rowNum);
			HubMadeImportDTO product = convertMadeDTO(xssfRow);
			if (product != null) {
				listHubProduct.add(product);
			}
		}
		return listHubProduct;
	}
	

	private HubPendingSkuCheckResult selectAndcheckSku(HubPendingSpuImportDTO productImport, HubSpuPendingDto isSpuPendingExist, Map<String, String> map)
			throws Exception {
	
		// 如果规格为尺码，则校验spu下所有的尺码
		HubPendingSkuCheckResult checkResult = new HubPendingSkuCheckResult();
		boolean flag = true;
		StringBuffer str = new StringBuffer();
		if (isSpuPendingExist != null) {
			boolean allFliter = true;
			boolean noSku = false;
			//查询出所有状态不是2(已完成)或5(审核中)的sku
			List<HubSkuPendingDto> listSku = dataHandleService.selectHubSkuPendingBySpuPendingId(isSpuPendingExist);
			if (listSku != null && listSku.size() > 0) {
				//判断sku是否都过滤或者都已处理
				for (HubSkuPendingDto hubSkuPendingDto : listSku) {
					HubPendingSkuCheckResult hubPendingSkuCheckResult = loopCheckHubSkuPending(hubSkuPendingDto,productImport,map);
					if(hubSkuPendingDto.getFilterFlag()==1){
						allFliter = false;
					}
					if(!hubPendingSkuCheckResult.isPassing()){
						if(!hubPendingSkuCheckResult.isFilter()){
							flag = false;	
						}
					}
					str.append(hubPendingSkuCheckResult.getMessage()).append(",");
				}
				map.put("allFilter",allFliter+"");
			} else {
				flag = false;
				noSku = true;
				map.put("noSku",noSku+"");
				str.append("无sku信息或者sku都已处理");
			}
		}else{
			flag = false;
		}
		checkResult.setPassing(flag);
		checkResult.setMessage(str.toString());
		return checkResult;
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
	private static HubMadeImportDTO convertMadeDTO(XSSFRow xssfRow)  throws Exception{
		HubMadeImportDTO item = null;
		if (xssfRow != null) {
			try {
				item = new HubMadeImportDTO();
				Class c = item.getClass();
				for (int i = 0; i < madeValueTemplate.length; i++) {
					if (xssfRow.getCell(i) != null) {
						xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
						String setMethodName = "set" + madeValueTemplate[i].toUpperCase().charAt(0)
								+ madeValueTemplate[i].substring(1);
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
	private static HubMadeImportDTO convertMadeDTO(HSSFRow xssfRow) throws Exception{
		HubMadeImportDTO item = null;
		if (xssfRow != null) {
			try {
				item = new HubMadeImportDTO();
				Class c = item.getClass();
				for (int i = 0; i < madeValueTemplate.length; i++) {
					if (xssfRow.getCell(i) != null) {
						xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
						String setMethodName = "set" + madeValueTemplate[i].toUpperCase().charAt(0)
								+ madeValueTemplate[i].substring(1);
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