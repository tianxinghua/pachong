//package com.shangpin.asynchronous.task.consumer.productimport.pending.sku.service;
//
//import java.io.InputStream;
//import java.lang.reflect.Method;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.alibaba.fastjson.JSONObject;
//import com.shangpin.asynchronous.task.consumer.productimport.common.service.DataHandleService;
//import com.shangpin.asynchronous.task.consumer.productimport.common.service.TaskImportService;
//import com.shangpin.asynchronous.task.consumer.productimport.pending.sku.dao.HubPendingProductImportDTO;
//import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
//import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuCriteriaDto;
//import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;
//import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
//import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
//import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
//import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
//import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
//import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
//import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
//import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;
//import com.shangpin.ephub.client.product.business.hubpending.sku.dto.HubSkuCheckDto;
//import com.shangpin.ephub.client.product.business.hubpending.sku.gateway.HubPendingSkuCheckGateWay;
//import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
//import com.shangpin.ephub.client.product.business.hubpending.spu.gateway.HubPendingSpuCheckGateWay;
//import com.shangpin.ephub.client.product.business.hubpending.spu.result.HubPendingSpuCheckResult;
//import com.shangpin.ephub.client.product.business.model.gateway.HubBrandModelRuleGateWay;
//
//import lombok.extern.slf4j.Slf4j;
//
///**
// * <p>
// * Title:SupplierOrderService.java
// * </p>
// * <p>
// * Description: 任务队列消费
// * </p>
// * <p>
// * Company: www.shangpin.com
// * </p>
// * 
// * @author zhaogenchun
// * @date 2016年11月23日 下午4:06:52
// */
//@SuppressWarnings("rawtypes")
//@Service
//@Slf4j
//public class PendingSkuImportService1 {
//
//	@Autowired
//	HubSkuGateWay hubSkuGateWay;
//	@Autowired
//	DataHandleService dataHandleService;
//	@Autowired
//	HubSpuImportTaskGateWay spuImportGateway;
//	@Autowired
//	HubSkuPendingGateWay hubSkuPendingGateWay;
//	@Autowired
//	TaskImportService taskService;
//	@Autowired
//	HubPendingSkuCheckGateWay pendingSkuCheckGateWay;
//	@Autowired
//	HubBrandModelRuleGateWay hubBrandModelRuleGateWay;
//	@Autowired
//	HubSpuGateWay hubSpuGateway;
//	@Autowired
//	HubPendingSpuCheckGateWay pendingSpuCheckGateWay;
//
//	public String handMessage(ProductImportTask task) throws Exception {
//
//		// ftp下载文件
//		JSONObject json = JSONObject.parseObject(task.getData());
//		String filePath = json.get("taskFtpFilePath").toString();
//		task.setData(filePath);
//		InputStream in = taskService.downFileFromFtp(task);
//		
//		//解析excel
//		List<HubPendingProductImportDTO> listHubProduct = null;
//		String fileFormat = filePath.split("\\.")[1];
//		if ("xls".equals(fileFormat)) {
//			listHubProduct = handleHubXlsExcel(in, task, "sku");
//		} else if ("xlsx".equals(fileFormat)) {
//			listHubProduct = handleHubXlsxExcel(in, task, "sku");
//		}
//
//		// 3、公共类校验hub数据并把校验结果写入excel
//		return checkAndHandlePendingProduct(task.getTaskNo(), listHubProduct);
//	}
//
//	// 校验数据以及保存到hub表
//	private String checkAndHandlePendingProduct(String taskNo, List<HubPendingProductImportDTO> listHubProduct)
//			throws Exception {
//
//		if (listHubProduct == null) {
//			return null;
//		}
//		
//		
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
//		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
//		Map<String, String> map = null;
//		
//        for (Map.Entry<String, List<HubPendingProductImportDTO>> entry : mapSpu.entrySet()) {
//        	List<HubPendingProductImportDTO> spuList = entry.getValue();
//        	int i = 0;
//        	HubPendingSpuCheckResult hubPendingSpuCheckResult = null;
//        	boolean skuIsPassing = true;
//        	map = new HashMap<String, String>();
//        	StringBuffer s = new StringBuffer();
//        	for (HubPendingProductImportDTO product : spuList) {
//        		
//    			if(product == null||StringUtils.isBlank(product.getSupplierId())){
//    				continue;
//    			}
//    			if(i==0){
//    				//校验spu
//    				HubSpuPendingDto hubPendingSpuDto = convertHubPendingProduct2PendingSpu(product);
//    				hubPendingSpuCheckResult = pendingSpuCheckGateWay.checkSpu(hubPendingSpuDto);
//    			}
//    			HubPendingSkuCheckResult hubPendingSkuCheckResult = checkProduct(product);
//    			if(!hubPendingSkuCheckResult.isPassing()){
//    				s.append(hubPendingSkuCheckResult.getMessage());
//    				skuIsPassing = false;
//    			}
//    		}
//        	
//        	listMap.add(map);
//        }
//		// 4、处理结果的excel上传ftp，并更新任务表状态和文件在ftp的路径
//		return taskService.convertExcel(listMap, taskNo);
//	}
//
//	private HubPendingSkuCheckResult checkProduct(HubPendingProductImportDTO product) throws Exception{
//		// 校验sku信息
//		HubSkuCheckDto hubSkuCheckDto = convertHubPendingProduct2PendingSkuCheck(product);
//		log.info("pendindSku校验参数：{}", hubSkuCheckDto);
//		HubPendingSkuCheckResult hubPendingSkuCheckResult = pendingSkuCheckGateWay.checkSku(hubSkuCheckDto);
//		log.info("pendindSku校验返回结果：{}", hubPendingSkuCheckResult);
//		return hubPendingSkuCheckResult;
//	}
//	
//	public void checkPendingSku(HubPendingSkuCheckResult hubPendingSkuCheckResult, HubSkuPendingDto hubSkuPendingDto,
//			 Map<String, String> map,HubPendingProductImportDTO product,boolean isMultiSizeType) throws Exception{
//		String hubSpuNo = map.get("hubSpuNo");
//		if (map.get("pendingSpuId") != null) {
//			hubSkuPendingDto.setSpuPendingId(Long.valueOf(map.get("pendingSpuId")));
//		}
//
//		String specificationType = product.getSpecificationType();
//		String sizeType = product.getSizeType();
//		HubSkuPendingDto hubSkuPendingTempDto = findHubSkuPending(hubSkuPendingDto.getSupplierId(),
//				hubSkuPendingDto.getSupplierSkuNo());
//		if (hubPendingSkuCheckResult.isPassing()) {
//			hubSkuPendingDto.setScreenSize(hubPendingSkuCheckResult.getSizeId());
//			if(hubSkuPendingTempDto!=null){
//				if(hubSpuNo!=null){
//					HubSkuCriteriaDto sku = new HubSkuCriteriaDto();
//					if(product.getHubSkuSize()!=null&&product.getSizeType()!=null){
//						sku.createCriteria().andSpuNoEqualTo(hubSpuNo).andSkuSizeEqualTo(product.getHubSkuSize()).andSkuSizeTypeEqualTo(product.getSizeType());	
//					}else{
//						sku.createCriteria().andSpuNoEqualTo(hubSpuNo);
//					}
//					List<HubSkuDto> listSku = hubSkuGateWay.selectByCriteria(sku);
//					if(listSku!=null&&listSku.size()>0){
//						log.info(hubSpuNo+"hub中已存在尺码:"+product.getHubSkuSize());
//						hubSkuPendingDto.setSkuState((byte) SpuState.INFO_IMPECCABLE.getIndex());
//					}else{
//						hubSkuPendingDto.setSkuState((byte) SpuState.HANDLING.getIndex());
//					}
//				}else{
//					hubSkuPendingDto.setSkuState((byte) SpuState.HANDLING.getIndex());
//				}
//			}else{
//				hubSkuPendingDto.setSkuState((byte) SpuState.HANDLING.getIndex());
//			}
//			
//			hubSkuPendingDto.setSpSkuSizeState((byte) 1);
//			hubSkuPendingDto.setFilterFlag((byte)1);
//		} else {
//			if(isMultiSizeType){
//				hubSkuPendingDto.setSkuState((byte) SpuState.INFO_PECCABLE.getIndex());
//				//此尺码含有多个尺码类型，需要手动选择
//				hubSkuPendingDto.setFilterFlag((byte)1);
//				hubSkuPendingDto.setMemo("此尺码含有多个尺码类型，需要手动选择");
//			}else{
//				hubSkuPendingDto.setSkuState((byte) SpuState.INFO_PECCABLE.getIndex());
//				//此尺码过滤不处理
//				hubSkuPendingDto.setMemo("此尺码未匹配成功");
//				hubSkuPendingDto.setFilterFlag((byte)1);
//			}
//			
//			//临时加
//			hubSkuPendingDto.setMemo("此尺码过滤不处理");
//			hubSkuPendingDto.setFilterFlag((byte)0);
//		}
//		if("尺码".equals(specificationType)||StringUtils.isBlank(specificationType)){
//			hubSkuPendingDto.setHubSkuSizeType(sizeType);
//		}else if("排除".equals(sizeType)){
//			hubSkuPendingDto.setMemo("此尺码过滤不处理");
//			hubSkuPendingDto.setFilterFlag((byte)0);
//		}else if("尺寸".equals(specificationType)){
//			hubSkuPendingDto.setHubSkuSizeType("尺寸");
//			if(hubSkuPendingDto.getHubSkuSize()==null){
//				hubSkuPendingDto.setHubSkuSize("");
//			}
//		}
//		
//		//更新或插入操作
//		if (hubSkuPendingTempDto != null) {
//			hubSkuPendingDto.setSkuPendingId(hubSkuPendingTempDto.getSkuPendingId());
//			hubSkuPendingDto.setUpdateTime(new Date());
//			hubSkuPendingGateWay.updateByPrimaryKeySelective(hubSkuPendingDto);
//		} else {
//			hubSkuPendingDto.setCreateTime(new Date());
//			hubSkuPendingDto.setUpdateTime(new Date());
//			hubSkuPendingDto.setDataState((byte) 1);
//			hubSkuPendingGateWay.insert(hubSkuPendingDto);
//		}
//	}
//	
//	public void checkPendingSpu(HubSpuPendingDto isPendingSpuExist,HubPendingSkuCheckResult hubPendingSkuCheckResult,HubSpuPendingDto hubPendingSpuDto, 
//			Map<String, String> map) {
//		
//		boolean skuIsPassing = hubPendingSkuCheckResult.isPassing();
//		Long pendingSpuId = null;
//		boolean spuIsPassing = false;
//		boolean hubIsExist= false;
//		Long hubSpuId= null;
//		String hubSpuNo = null;
//		String checkResult = null;
//		
//		// 校验货号
//		 HubPendingSpuCheckResult hubPendingSpuCheckResult = pendingSpuCheckGateWay.checkSpu(hubPendingSpuDto);
//		log.info(hubPendingSpuDto.getSpuModel()+"检验spu结果：{}",hubPendingSpuCheckResult);
//		String spuModel = null;
//		if (hubPendingSpuCheckResult.isSpuModel()) {
//			spuModel = hubPendingSpuCheckResult.getModel();
//			// 查询货号是否已存在hubSpu中
//			hubPendingSpuDto.setSpuModel(spuModel);
//			List<HubSpuDto> list = dataHandleService.selectHubSpu(hubPendingSpuDto);
//			if (list != null && list.size() > 0) {
//				// 货号已存在hubSpu中,不需要推送hub，直接把hubSpu信息拿过来，查询pendingSpu是否存在==》保存或更新pendingSpu表
//				convertHubSpuToPendingSpu(hubPendingSpuDto, list.get(0));
//				hubSpuId = list.get(0).getSpuId();
//				hubSpuNo = list.get(0).getSpuNo();
//				spuIsPassing = true;
//				hubIsExist = true;
//				checkResult = spuModel+"在hub已存在";
//				hubPendingSpuCheckResult.setPassing(true);
//			} else {
//				// 货号不存在hubSpu中,继续校验其它信息，查询pendingSpu是否存在==》保存或更新pendingSpu表
//				if (hubPendingSpuCheckResult.isPassing()) {
//					// 其它信息校验通过，需要推送hub，查询pendingSpu是否存在==》保存或更新pendingSpu表
//					spuIsPassing = true;
//					hubIsExist = false;
//					checkResult = "校验成功";
//				} else {
//					spuIsPassing = false;
//					hubIsExist = false;
//					checkResult = hubPendingSpuCheckResult.getResult();
//				}
//			}
//		} else {
//			hubPendingSpuCheckResult.setPassing(false);
//			spuIsPassing = false;
//			hubIsExist = false;
//			checkResult = "货号校验失败";
//		}
//		
//		pendingSpuId = saveOrUpdatePendingSpu(hubIsExist,isPendingSpuExist, hubPendingSpuDto, hubPendingSpuCheckResult,skuIsPassing);
//		if (spuIsPassing==true&&skuIsPassing==true) {
//			map.put("taskState", "校验通过");
//			map.put("processInfo", "spu:"+checkResult+",sku:"+hubPendingSkuCheckResult.getMessage());
//		} else {
//			map.put("taskState", "校验失败");
//			map.put("processInfo", "spu："+checkResult+",sku:"+hubPendingSkuCheckResult.getMessage());
//		}
//	
//		map.put("pendingSpuId",pendingSpuId+"");
//		map.put("hubIsExist",hubIsExist+"");
//		map.put("isPassing",spuIsPassing+"");
//		map.put("hubSpuId",hubSpuId+"");
//		map.put("hubSpuNo",hubSpuNo);
//	}
//	private HubSkuPendingDto convertHubPendingProduct2PendingSku(HubPendingProductImportDTO product) throws Exception{
//		HubSkuPendingDto hubPendingSkuDto = new HubSkuPendingDto();
//		BeanUtils.copyProperties(product, hubPendingSkuDto);
//		if(hubPendingSkuDto.getMarketPrice()!=null){
//			hubPendingSkuDto.setMarketPrice(new BigDecimal(product.getMarketPrice()));	
//		}
//		if(hubPendingSkuDto.getSupplyPrice()!=null){
//			hubPendingSkuDto.setSupplyPrice(new BigDecimal(product.getSupplyPrice()));	
//		}
//		hubPendingSkuDto.setHubSkuSize(product.getHubSkuSize());
//		hubPendingSkuDto.setHubSkuSizeType(product.getSizeType());
//		return hubPendingSkuDto;
//	}
//
//	private HubSkuCheckDto convertHubPendingProduct2PendingSkuCheck(HubPendingProductImportDTO product) throws Exception{
//		HubSkuCheckDto hubPendingSkuDto = new HubSkuCheckDto();
//		hubPendingSkuDto.setBrandNo(product.getHubBrandNo());
//		hubPendingSkuDto.setCategoryNo(product.getHubCategoryNo());
//		hubPendingSkuDto.setSizeType(product.getSizeType());
//		hubPendingSkuDto.setSkuSize(product.getHubSkuSize());
//		hubPendingSkuDto.setSpuModel(product.getSpuModel());
//		hubPendingSkuDto.setSpecificationType(product.getSpecificationType());
//		return hubPendingSkuDto;
//	}
//
//
//	// 解析excel转换为对象
//	private List<HubPendingProductImportDTO> handleHubXlsxExcel(InputStream in, ProductImportTask task, String type)
//			throws Exception {
//
//		XSSFSheet xssfSheet = taskService.checkXlsxExcel(in, task, "sku");
//		if (xssfSheet == null) {
//			return null;
//		}
//		List<HubPendingProductImportDTO> listHubProduct = new ArrayList<>();
//		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
//			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
//			HubPendingProductImportDTO product = convertSpuDTO(xssfRow);
//			if (product != null) {
//				listHubProduct.add(product);
//			}
//
//		}
//		return listHubProduct;
//	}
//
//	// 解析excel转换为对象
//	private List<HubPendingProductImportDTO> handleHubXlsExcel(InputStream in, ProductImportTask task, String type)
//			throws Exception {
//
//		HSSFSheet hSSFSheet = taskService.checkXlsExcel(in, task, "sku");
//		if (hSSFSheet == null) {
//			return null;
//		}
//		List<HubPendingProductImportDTO> listHubProduct = new ArrayList<>();
//		for (int rowNum = 1; rowNum <= hSSFSheet.getLastRowNum(); rowNum++) {
//			HSSFRow hssfRow = hSSFSheet.getRow(rowNum);
//			HubPendingProductImportDTO product = convertSpuDTO(hssfRow);
//			listHubProduct.add(product);
//		}
//		return listHubProduct;
//	}
//
//	private HubSpuPendingDto convertHubPendingProduct2PendingSpu(HubPendingProductImportDTO product) throws Exception{
//		HubSpuPendingDto HubPendingSpuDto = new HubSpuPendingDto();
//		BeanUtils.copyProperties(product, HubPendingSpuDto);
//		HubPendingSpuDto.setHubSeason(product.getSeasonYear() + "_" + product.getSeasonName());
//		return HubPendingSpuDto;
//	}
//
//	@SuppressWarnings("unchecked")
//	private static HubPendingProductImportDTO convertSpuDTO(XSSFRow xssfRow) throws Exception{
//		HubPendingProductImportDTO item = null;
//		if (xssfRow != null) {
//			try {
//				item = new HubPendingProductImportDTO();
//				String[] hubValueTemplate = item.getHubProductTemplate();
//				Class cls = item.getClass();
//				for (int i=0;i<hubValueTemplate.length;i++) {
//					if(xssfRow.getCell(i)!=null){
//						xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
//						String fieldSetName = "set" + hubValueTemplate[i].toUpperCase().charAt(0)
//								+ hubValueTemplate[i].substring(1);
//						Method setMethod = cls.getDeclaredMethod(fieldSetName, String.class);
//						setMethod.invoke(item, xssfRow.getCell(i).toString());
//					}
////						@SuppressWarnings("unchecked")
////						Method fieldSetMet = cls.getMethod(fieldSetName, field.getType());
////						if (!hubValueTemplate[i].equals(field.getName())) {
////							return null;
////						}
////						if(xssfRow.getCell(i)!=null){
////							xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
////							String value = xssfRow.getCell(i).toString();
////							i++;
////							if (null != value && !"".equals(value)) {
////								String fieldType = field.getType().getSimpleName();
////								if ("String".equals(fieldType)) {
////									fieldSetMet.invoke(item, value);
////								} else if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
////									Integer intval = Integer.parseInt(value);
////									fieldSetMet.invoke(item, intval);
////								} else if ("Long".equalsIgnoreCase(fieldType)) {
////									Long temp = Long.parseLong(value);
////									fieldSetMet.invoke(item, temp);
////								} else if ("Double".equalsIgnoreCase(fieldType)) {
////									Double temp = Double.parseDouble(value);
////									fieldSetMet.invoke(item, temp);
////								} else if ("Boolean".equalsIgnoreCase(fieldType)) {
////									Boolean temp = Boolean.parseBoolean(value);
////									fieldSetMet.invoke(item, temp);
////								} else if ("BigDecimal".equalsIgnoreCase(fieldType)) {
////									BigDecimal temp = new BigDecimal(value);
////									fieldSetMet.invoke(item, temp);
////								} else {
////									log.info("not supper type" + fieldType);
////								}
////							}
////						
//					
//				}
//
//			} catch (Exception ex) {
//				ex.getStackTrace();
//			}
//			
//	}
//		return item;
//	}
//	@SuppressWarnings("unchecked")
//	private static HubPendingProductImportDTO convertSpuDTO(HSSFRow xssfRow) throws Exception{
//		HubPendingProductImportDTO item = null;
//		if (xssfRow != null) {
//			try {
//				item = new HubPendingProductImportDTO();
//				String[] hubValueTemplate = item.getHubProductTemplate();
//				Class cls = item.getClass();
//				
//				for (int i=0;i<hubValueTemplate.length;i++) {
//					if(xssfRow.getCell(i)!=null){
//						xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
//						String fieldSetName = "set" + hubValueTemplate[i].toUpperCase().charAt(0)
//								+ hubValueTemplate[i].substring(1);
//						Method setMethod = cls.getDeclaredMethod(fieldSetName, String.class);
//						setMethod.invoke(item, xssfRow.getCell(i).toString());
//					}
////				for (Field field : fields) {
////					try {
////						String fieldSetName = parSetName(field.getName());
////						@SuppressWarnings("unchecked")
////						Method fieldSetMet = cls.getMethod(fieldSetName, field.getType());
////						if (!hubValueTemplate[i].equals(field.getName())) {
////							return null;
////						}
////						xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
////						String value = xssfRow.getCell(i).toString();
////						if (null != value && !"".equals(value)) {
////							String fieldType = field.getType().getSimpleName();
////							if ("String".equals(fieldType)) {
////								fieldSetMet.invoke(item, value);
////							} else if ("Integer".equals(fieldType) || "int".equals(fieldType)) {
////								Integer intval = Integer.parseInt(value);
////								fieldSetMet.invoke(item, intval);
////							} else if ("Long".equalsIgnoreCase(fieldType)) {
////								Long temp = Long.parseLong(value);
////								fieldSetMet.invoke(item, temp);
////							} else if ("Double".equalsIgnoreCase(fieldType)) {
////								Double temp = Double.parseDouble(value);
////								fieldSetMet.invoke(item, temp);
////							} else if ("Boolean".equalsIgnoreCase(fieldType)) {
////								Boolean temp = Boolean.parseBoolean(value);
////								fieldSetMet.invoke(item, temp);
////							} else if ("BigDecimal".equalsIgnoreCase(fieldType)) {
////								BigDecimal temp = new BigDecimal(value);
////								fieldSetMet.invoke(item, temp);
////							} else {
////								log.info("not supper type" + fieldType);
////							}
////						}
////					} catch (Exception e) {
////						continue;
////					}
//				}
//
//			} catch (Exception ex) {
//				ex.getStackTrace();
//			}
//		}
//		return item;
//	}
//
//	public static boolean checkSetMet(Method[] methods, String fieldSetMet) throws Exception{
//		for (Method met : methods) {
//			if (fieldSetMet.equals(met.getName())) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	public static String parSetName(String fieldName) throws Exception{
//		if (null == fieldName || "".equals(fieldName)) {
//			return null;
//		}
//		int startIndex = 0;
//		if (fieldName.charAt(0) == '_')
//			startIndex = 1;
//		return "set" + fieldName.substring(startIndex, startIndex + 1).toUpperCase()
//				+ fieldName.substring(startIndex + 1);
//	}
//}
