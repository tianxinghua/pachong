package com.shangpin.asynchronous.task.consumer.productimport.common.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.asynchronous.task.consumer.conf.ftp.FtpProperties;
import com.shangpin.asynchronous.task.consumer.productimport.common.util.ExportExcelUtils;
import com.shangpin.asynchronous.task.consumer.productimport.common.util.FTPClientUtil;
import com.shangpin.asynchronous.task.consumer.productimport.pending.sku.dao.HubPendingProductImportDTO;
import com.shangpin.ephub.client.data.mysql.enumeration.HubSpuState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.product.dto.HubPendingDto;
import com.shangpin.ephub.client.data.mysql.product.dto.SpuModelDto;
import com.shangpin.ephub.client.data.mysql.product.gateway.PengdingToHubGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskCriteriaDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;
import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.client.product.business.hubpending.spu.gateway.HubPendingSpuCheckGateWay;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.HubPendingSpuCheckResult;
import com.shangpin.ephub.client.product.business.hubproduct.dto.HubProductDto;
import com.shangpin.ephub.client.product.business.model.result.BrandModelResult;
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
@SuppressWarnings("unused")
@Service
@Slf4j
public class TaskImportService {

	@Autowired
	PengdingToHubGateWay pengdingToHubGateWay;
	@Autowired
	DataHandleService dataHandleService;
	@Autowired
	HubPendingSpuCheckGateWay pendingSpuCheckGateWay;
	@Autowired
	HubSpuPendingGateWay hubSpuPendingGateWay;
	@Autowired
	FtpProperties ftpProperties;
	@Autowired
	HubSpuImportTaskGateWay spuImportGateway;
	@Autowired
	HubSkuPendingGateWay hubSkuPendingGateWay;
	@Autowired
	HubSkuGateWay hubSkuGateWay;

	private static String[] pendingSpuTemplate = null;
	static {
		pendingSpuTemplate = TaskImportTemplate.getPendingSpuTemplate();
	}

	private static String[] pendingSkuTemplate = null;
	static {
		pendingSkuTemplate = TaskImportTemplate.getPendingSkuTemplate();
	}

	private static String[] hubProductTemplate = null;
	static {
		hubProductTemplate = HubProductDto.getHubProductTemplate();
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
		HubSkuPendingDto hubSkuPendingTempDto = findHubSkuPending(hubSkuPendingDto.getSupplierId(),
				hubSkuPendingDto.getSupplierSkuNo());
		hubSkuPendingDto.setHubSkuSize(product.getHubSkuSize());
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
				hubSkuPendingDto.setFilterFlag((byte)1);
				hubSkuPendingDto.setMemo("此尺码含有多个尺码类型，需要手动选择");
			}else{
				hubSkuPendingDto.setSkuState((byte) SpuState.INFO_PECCABLE.getIndex());
				//此尺码过滤不处理
				hubSkuPendingDto.setMemo("此尺码过滤不处理");
				hubSkuPendingDto.setFilterFlag((byte)0);
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
	public HubSkuPendingDto findHubSkuPending(String supplierId, String supplierSkuNo) throws Exception{

		HubSkuPendingCriteriaDto criteria = new HubSkuPendingCriteriaDto();
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSkuNoEqualTo(supplierSkuNo);
		List<HubSkuPendingDto> list = hubSkuPendingGateWay.selectByCriteria(criteria);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	public boolean updateHubSpuImportByTaskNo(int status, String taskNo, String processInfo, String resultFilePath) {
		HubSpuImportTaskWithCriteriaDto dto = new HubSpuImportTaskWithCriteriaDto();
		HubSpuImportTaskDto hubSpuImportTaskDto = new HubSpuImportTaskDto();

		hubSpuImportTaskDto.setUpdateTime(new Date());
		if (processInfo != null) {
			hubSpuImportTaskDto.setProcessInfo(processInfo);
		}
		if (resultFilePath != null) {
			hubSpuImportTaskDto.setResultFilePath(resultFilePath);
		}
		hubSpuImportTaskDto.setTaskState((byte) status);
		dto.setHubSpuImportTask(hubSpuImportTaskDto);
		HubSpuImportTaskCriteriaDto hubSpuImportTaskCriteriaDto = new HubSpuImportTaskCriteriaDto();
		hubSpuImportTaskCriteriaDto.createCriteria().andTaskNoEqualTo(taskNo);
		dto.setCriteria(hubSpuImportTaskCriteriaDto);
		spuImportGateway.updateByCriteriaSelective(dto);
		return true;
	}

	public String convertExcel(List<Map<String, String>> result, String taskNo) throws Exception {
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String resultFileName = sim.format(new Date());
		File filePath = new File(ftpProperties.getLocalResultPath());
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		String pathFile = ftpProperties.getLocalResultPath() + resultFileName + ".xls";
		File file = new File(pathFile);
		FileOutputStream out = new FileOutputStream(file);

		String[] headers = { "任务编号", "货号", "任务状态", "任务说明","新货号" };
		String[] columns = { "taskNo", "spuModel", "taskState", "processInfo","spuNewModel"};
		ExportExcelUtils.exportExcel(resultFileName, headers, columns, result, out);
		// 4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
		String path = FTPClientUtil.uploadFile(file, resultFileName + ".xls");
		FTPClientUtil.closeFtp();
		if (file.exists()) {
			file.delete();
		}
		// 更新结果文件路径到表中
		
		return path + resultFileName + ".xls";
//		updateHubSpuImportByTaskNo(TaskState.ALL_SUCCESS.getIndex(), taskNo, null, path + resultFileName + ".xls");
	}

	public XSSFSheet checkXlsxExcel(InputStream in, ProductImportTask task, String type) throws Exception {

		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(in);
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		if (xssfSheet.getRow(0) == null) {
			log.info("任务编号：" + task.getTaskNo() + "," + task.getData() + "下载的excel数据为空");
			updateHubSpuImportByTaskNo(TaskState.SOME_SUCCESS.getIndex(), task.getTaskNo(), "下载的excel数据为空", null);
			return null;
		}
		boolean flag = checkXlsxFileTemplet(xssfSheet.getRow(0), type);
		if (!flag) {
			log.info("任务编号：" + task.getTaskNo() + "," + task.getData() + "上传文件与模板不一致");
			updateHubSpuImportByTaskNo(TaskState.SOME_SUCCESS.getIndex(), task.getTaskNo(), "上传文件与模板不一致", null);
			return null;
		}
		return xssfSheet;
	}

	public HSSFSheet checkXlsExcel(InputStream in, ProductImportTask task, String type) throws Exception {

		HSSFWorkbook xssfWorkbook = new HSSFWorkbook(in);
		HSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		if (xssfSheet.getRow(0) == null) {
			log.info("任务编号：" + task.getTaskNo() + "," + task.getData() + "下载的excel数据为空");
			updateHubSpuImportByTaskNo(TaskState.SOME_SUCCESS.getIndex(), task.getTaskNo(), "下载的excel数据为空", null);
			return null;
		}
		boolean flag = checkXlsFileTemplet(xssfSheet.getRow(0), type);
		if (!flag) {
			log.info("任务编号：" + task.getTaskNo() + "," + task.getData() + "上传文件与模板不一致");
			updateHubSpuImportByTaskNo(TaskState.SOME_SUCCESS.getIndex(), task.getTaskNo(), "上传文件与模板不一致", null);
			return null;
		}
		return xssfSheet;
	}

	public static boolean checkXlsxFileTemplet(XSSFRow xssfRow, String type) {

		boolean flag = true;
		if ("spu".equals(type)) {
			for (int i = 0; i < pendingSpuTemplate.length; i++) {
				if (xssfRow.getCell(i) != null) {
					String fieldName = xssfRow.getCell(i).toString();
					if (!pendingSpuTemplate[i].equals(fieldName)) {
						flag = false;
						break;
					}
				}
			}
		}
		if ("sku".equals(type)) {
			for (int i = 0; i < pendingSkuTemplate.length; i++) {
				if (xssfRow.getCell(i) != null) {
					String fieldName = xssfRow.getCell(i).toString();
					if (!pendingSkuTemplate[i].equals(fieldName)) {
						flag = false;
						break;
					}
				}
			}
		}
		if ("hub".equals(type)) {
			for (int i = 0; i < hubProductTemplate.length; i++) {
				if (xssfRow.getCell(i) != null) {
					String fieldName = xssfRow.getCell(i).toString();
					if (!hubProductTemplate[i].equals(fieldName)) {
						flag = false;
						break;
					}
				}
			}
		}

		return flag;
	}

	public static boolean checkXlsFileTemplet(HSSFRow xssfRow, String type) {

		boolean flag = true;
		if ("spu".equals(type)) {
			for (int i = 0; i < pendingSpuTemplate.length; i++) {
				if (xssfRow.getCell(i) != null) {
					String fieldName = xssfRow.getCell(i).toString();
					if (!pendingSpuTemplate[i].equals(fieldName)) {
						flag = false;
						break;
					}
				}
			}
		}
		if ("sku".equals(type)) {
			for (int i = 0; i < pendingSkuTemplate.length; i++) {
				if (xssfRow.getCell(i) != null) {
					String fieldName = xssfRow.getCell(i).toString();
					if (!pendingSkuTemplate[i].equals(fieldName)) {
						flag = false;
						break;
					}
				}
			}
		}
		if ("hub".equals(type)) {
			for (int i = 0; i < hubProductTemplate.length; i++) {
				if (xssfRow.getCell(i) != null) {
					String fieldName = xssfRow.getCell(i).toString();
					if (!hubProductTemplate[i].equals(fieldName)) {
						flag = false;
						break;
					}
				}
			}
		}
		return flag;
	}

	public InputStream downFileFromFtp(ProductImportTask task) throws Exception {
		InputStream in = FTPClientUtil.downFile(task.getData());
//		FTPClientUtil.closeFtp();
		if (in == null) {
			log.info("任务编号：" + task.getTaskNo() + "," + task.getData() + "从ftp下载失败数据为空");
			updateHubSpuImportByTaskNo(TaskState.SOME_SUCCESS.getIndex(), task.getTaskNo(), "从ftp下载失败", null);
			return null;
		}
		return in;
	}

	public void checkPendingSpu(HubSpuPendingDto isPendingSpuExist,HubPendingSkuCheckResult hubPendingSkuCheckResult,HubSpuPendingDto hubPendingSpuDto, 
			Map<String, String> map,boolean flag) {
		
		boolean skuIsPassing = hubPendingSkuCheckResult.isPassing();
		Long pendingSpuId = null;
		boolean spuIsPassing = false;
		boolean hubIsExist= false;
		Long hubSpuId= null;
		String hubSpuNo = null;
		String checkResult = null;
		
		// 校验货号
		HubPendingSpuCheckResult hubPendingSpuCheckResult = null;
		BrandModelResult result = dataHandleService.checkSpuModel(hubPendingSpuDto);
		log.info(hubPendingSpuDto.getSpuModel()+"检验货号结果：{}",result);
		String spuModel = null;
		if (result.isPassing()) {
			spuModel = result.getBrandMode();
			// 查询货号是否已存在hubSpu中
			hubPendingSpuDto.setSpuModel(spuModel);
			List<HubSpuDto> list = dataHandleService.selectHubSpu(hubPendingSpuDto);
			if (list != null && list.size() > 0) {
				// 货号已存在hubSpu中,不需要推送hub，直接把hubSpu信息拿过来，查询pendingSpu是否存在==》保存或更新pendingSpu表
				convertHubSpuToPendingSpu(hubPendingSpuDto, list.get(0));
				hubSpuId = list.get(0).getSpuId();
				hubSpuNo = list.get(0).getSpuNo();
				spuIsPassing = true;
				hubIsExist = true;
				checkResult = spuModel+"在hub已存在";
				hubPendingSpuCheckResult = new HubPendingSpuCheckResult();
				hubPendingSpuCheckResult.setPassing(true);
			} else {
				// 货号不存在hubSpu中,继续校验其它信息，查询pendingSpu是否存在==》保存或更新pendingSpu表
				log.info(hubPendingSpuDto.getSpuModel()+"检验pendingSpu参数：{}",hubPendingSpuDto);
				hubPendingSpuCheckResult = pendingSpuCheckGateWay.checkSpu(hubPendingSpuDto);
				log.info(hubPendingSpuDto.getSpuModel()+"检验pendingSpu结果：{}",hubPendingSpuCheckResult);
				if (hubPendingSpuCheckResult.isPassing()) {
					// 其它信息校验通过，需要推送hub，查询pendingSpu是否存在==》保存或更新pendingSpu表
					spuIsPassing = true;
					hubIsExist = false;
					checkResult = "校验成功";
				} else {
					spuIsPassing = false;
					hubIsExist = false;
					checkResult = hubPendingSpuCheckResult.getResult();
				}
			}
		} else {
			hubPendingSpuCheckResult = new HubPendingSpuCheckResult();
			hubPendingSpuCheckResult.setPassing(false);
			spuIsPassing = false;
			hubIsExist = false;
			checkResult = "货号校验失败";
		}
		
		pendingSpuId = saveOrUpdatePendingSpu(hubIsExist,isPendingSpuExist, hubPendingSpuDto, hubPendingSpuCheckResult,skuIsPassing,flag);
		if (spuIsPassing==true&&flag==true) {
			map.put("taskState", "校验通过");
			map.put("processInfo", "spu:"+checkResult+",sku:"+hubPendingSkuCheckResult.getResult());
		} else {
			map.put("taskState", "校验失败");
			map.put("processInfo", "spu："+checkResult+",sku:"+hubPendingSkuCheckResult.getResult());
		}
	
		map.put("pendingSpuId",pendingSpuId+"");
		map.put("hubIsExist",hubIsExist+"");
		map.put("isPassing",spuIsPassing+"");
		map.put("hubSpuId",hubSpuId+"");
		map.put("hubSpuNo",hubSpuNo);
	}
	private void convertHubSpuToPendingSpu(HubSpuPendingDto hubPendingSpuDto, HubSpuDto hubSpuDto) {
		hubPendingSpuDto.setHubBrandNo(hubSpuDto.getBrandNo());
		hubPendingSpuDto.setHubCategoryNo(hubSpuDto.getCategoryNo());
		hubPendingSpuDto.setHubColor(hubSpuDto.getHubColor());
		hubPendingSpuDto.setHubColorNo(hubSpuDto.getHubColorNo());
		hubPendingSpuDto.setHubGender(hubSpuDto.getGender());
		hubPendingSpuDto.setHubMaterial(hubSpuDto.getMaterial());
		hubPendingSpuDto.setHubOrigin(hubSpuDto.getOrigin());
		hubPendingSpuDto.setHubSeason(hubSpuDto.getMarketTime()+"_"+hubSpuDto.getSeason());
		hubPendingSpuDto.setHubSpuNo(hubSpuDto.getSpuNo());
		hubPendingSpuDto.setSpuModel(hubSpuDto.getSpuModel());
		hubPendingSpuDto.setSpuName(hubSpuDto.getSpuName());
	}

	public void sendToHub(HubSpuPendingDto hubPendingSpuDto, Map<String,String> map) {
		boolean hubIsExist = Boolean.parseBoolean(map.get("hubIsExist"));
		if (hubIsExist) {
			if (map.get("pendingSpuId") != null) {
				hubPendingSpuDto.setSpuPendingId(Long.valueOf(map.get("pendingSpuId")));
			}
			
			String hubSpuId = map.get("hubSpuId");
			HubPendingDto hubPendingDto = new HubPendingDto();
			if(hubSpuId!=null){
				hubPendingDto.setHubSpuId(Long.parseLong(hubSpuId));	
			}
			hubPendingDto.setHubSpuPendingId(hubPendingSpuDto.getSpuPendingId());
			log.info("pendingToHub.addSkuOrSkuSupplierMapping推送参数:{}", hubPendingDto);
			//更新
			pengdingToHubGateWay.addSkuOrSkuSupplierMapping(hubPendingDto);
		} 
//		else {尺寸、尺码
//			SpuModelDto spuModelDto = new SpuModelDto();
//			spuModelDto.setBrandNo(hubPendingSpuDto.getHubBrandNo());
//			spuModelDto.setSpuModel(hubPendingSpuDto.getSpuModel());
//			log.info("pendingToHub.auditPending推送参数:{}", spuModelDto);
//			pengdingToHubGateWay.auditPending(spuModelDto);
//		}
	}

	private Long saveOrUpdatePendingSpu(boolean hubIsExist,HubSpuPendingDto isPendingSpuExist, HubSpuPendingDto hubPendingSpuDto,
			HubPendingSpuCheckResult hubPendingSpuCheckResult,boolean skuIsPassing,boolean flag) {

		Long pengingSpuId = null;
		boolean spuIsPassing = hubPendingSpuCheckResult.isPassing();
		if(isPendingSpuExist!=null&&flag==true){//&&skuIsPassing==true
			if(isPendingSpuExist.getSpuState().byteValue()==SpuState.HANDLED.getIndex()||isPendingSpuExist.getSpuState().byteValue()==SpuState.HANDLING.getIndex()||isPendingSpuExist.getSpuState().byteValue()==SpuState.INFO_IMPECCABLE.getIndex()){
				log.info("spu货号:"+isPendingSpuExist.getSpuModel()+"状态为："+isPendingSpuExist.getSpuState()+"，不更新");
				return isPendingSpuExist.getSpuPendingId();
			}
		}
		if (spuIsPassing&&flag==true) {//&&skuIsPassing==true
			if(hubIsExist){
				hubPendingSpuDto.setSpuState((byte) SpuState.HANDLED.getIndex());	
			}else{
				hubPendingSpuDto.setSpuState((byte) SpuState.INFO_IMPECCABLE.getIndex());
			}
			hubPendingSpuDto.setCatgoryState((byte)1);
			hubPendingSpuDto.setMaterialState((byte)1);
			hubPendingSpuDto.setOriginState((byte)1);
			hubPendingSpuDto.setSpuBrandState((byte)1);
			hubPendingSpuDto.setSpuColorState((byte)1);
			hubPendingSpuDto.setSpuGenderState((byte)1);
			hubPendingSpuDto.setSpuModelState((byte)1);
			hubPendingSpuDto.setSpuSeasonState((byte)1);
			
		} else {
			
			if(hubPendingSpuCheckResult.isCategory()){
				hubPendingSpuDto.setCatgoryState((byte)1);
			}else{
				hubPendingSpuDto.setCatgoryState((byte)0);
			}
			
			if(hubPendingSpuCheckResult.isMaterial()){
				hubPendingSpuDto.setMaterialState((byte)1);
			}else{
				hubPendingSpuDto.setMaterialState((byte)0);
			}
			
			if(hubPendingSpuCheckResult.isOriginal()){
				hubPendingSpuDto.setOriginState((byte)1);
			}else{
				hubPendingSpuDto.setOriginState((byte)0);
			}
			
			if(hubPendingSpuCheckResult.isBrand()){
				hubPendingSpuDto.setSpuBrandState((byte)1);
			}else{
				hubPendingSpuDto.setSpuBrandState((byte)0);
			}
			
			if(hubPendingSpuCheckResult.isColor()){
				hubPendingSpuDto.setSpuColorState((byte)1);
			}else{
				hubPendingSpuDto.setSpuColorState((byte)0);
			}
			
			if(hubPendingSpuCheckResult.isGender()){
				hubPendingSpuDto.setSpuGenderState((byte)1);
			}else{
				hubPendingSpuDto.setSpuGenderState((byte)0);
			}
			
			if(hubPendingSpuCheckResult.isSeasonName()){
				hubPendingSpuDto.setSpuSeasonState((byte)1);
			}else{
				hubPendingSpuDto.setSpuSeasonState((byte)0);
			}
			
			hubPendingSpuDto.setSpuState((byte) SpuState.INFO_PECCABLE.getIndex());
		}
		if (isPendingSpuExist != null) {
			log.info("spu:" + isPendingSpuExist.getSpuModel()+ "," +isPendingSpuExist.getSupplierSpuNo() + "存在，更新操作");
			pengingSpuId = isPendingSpuExist.getSpuPendingId();
			hubPendingSpuDto.setUpdateTime(new Date());
			hubPendingSpuDto.setSpuPendingId(pengingSpuId);
			hubSpuPendingGateWay.updateByPrimaryKeySelective(hubPendingSpuDto);
		} else {
			log.info("spu:" + hubPendingSpuDto.getSpuModel() + "不存在，插入新值");
			hubPendingSpuDto.setCreateTime(new Date());
			hubPendingSpuDto.setUpdateTime(new Date());
			hubPendingSpuDto.setSpuSeasonState((byte) 1);
			hubPendingSpuDto.setUpdateTime(new Date());
			hubPendingSpuDto.setSupplierSpuId(0l);
			pengingSpuId = hubSpuPendingGateWay.insert(hubPendingSpuDto);
		}
		return pengingSpuId;
	}

}
