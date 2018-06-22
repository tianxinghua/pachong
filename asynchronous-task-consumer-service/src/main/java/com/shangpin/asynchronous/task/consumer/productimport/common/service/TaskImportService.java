package com.shangpin.asynchronous.task.consumer.productimport.common.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.shangpin.asynchronous.task.consumer.productexport.template.TaskImportTemplate2;
import lombok.extern.slf4j.Slf4j;

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
import com.shangpin.ephub.client.data.mysql.enumeration.CatgoryState;
import com.shangpin.ephub.client.data.mysql.enumeration.MaterialState;
import com.shangpin.ephub.client.data.mysql.enumeration.OriginState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuBrandState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuColorState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuGenderState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuSeasonState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.product.dto.HubPendingDto;
import com.shangpin.ephub.client.data.mysql.product.gateway.PengdingToHubGateWay;
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
import com.shangpin.ephub.client.message.task.product.body.Task;
import com.shangpin.ephub.client.product.business.hubpending.sku.gateway.HubPendingSkuCheckGateWay;
import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.client.product.business.hubpending.spu.gateway.HubPendingHandleGateWay;
import com.shangpin.ephub.client.product.business.hubpending.spu.gateway.HubPendingSpuCheckGateWay;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.HubPendingSpuCheckResult;
import com.shangpin.ephub.client.product.business.hubproduct.dto.HubProductDto;
import com.shangpin.ephub.client.product.business.size.dto.MatchSizeDto;
import com.shangpin.ephub.client.product.business.size.gateway.MatchSizeGateWay;
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
@Service
@Slf4j
public class TaskImportService {

	@Autowired
	HubPendingSkuCheckGateWay hubPendingSkuCheckGateWay;
	@Autowired
	HubPendingHandleGateWay hubPendingHandleGateWay;
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
	@Autowired
	MatchSizeGateWay matchSizeGateWay;
	private static String[] pendingSpuTemplate = null;
	static {
		pendingSpuTemplate = TaskImportTemplate.getPendingSpuTemplate();

	}

	private static String[] pendingSkuTemplate = null;
	static {
		pendingSkuTemplate = TaskImportTemplate.getPendingSkuTemplate();
	}
	private static String[] colorTemplate=null;
	static {
		colorTemplate = TaskImportTemplate2.getColorTemplate();
	}
	private static String[] categroyTemplate=null;
	static {
		categroyTemplate = TaskImportTemplate2.getCategoryTemplate();
	}
	private static String[] hubProductTemplate = null;
	static {
		hubProductTemplate = HubProductDto.getHubProductTemplate();
	}
	private static String[] madeTemplate = null;
	static {
		madeTemplate = TaskImportTemplate2.getMadeTemplate();
	}
	private static String[] materialTemplate = null;
	static {
		materialTemplate = TaskImportTemplate2.getMaterialTemplate();
	}
	private static String[] brandTemplate  = null;
	static {
		brandTemplate = TaskImportTemplate2.getBrandTemplate();
	}
	private static String[] supplierDataTemplate = null;
	static {
		supplierDataTemplate = TaskImportTemplate.getSupplierDataTemplate();
	}

	public void checkPendingSku(HubPendingSkuCheckResult hubPendingSkuCheckResult, HubSkuPendingDto hubSkuPendingDto,
			 Map<String, String> map,boolean isMultiSizeType) throws Exception{
		
//		String hubSpuNo = map.get("hubSpuNo");
		if (map.get("pendingSpuId") != null) {
			hubSkuPendingDto.setSpuPendingId(Long.valueOf(map.get("pendingSpuId")));
		}

		HubSkuPendingDto hubSkuPendingTempDto = findHubSkuPending(hubSkuPendingDto.getSupplierId(),
				hubSkuPendingDto.getSupplierSkuNo());
		
		if(hubSkuPendingTempDto!=null&&hubSkuPendingTempDto.getSkuState()!=null&&(hubSkuPendingTempDto.getSkuState()==SpuState.HANDLED.getIndex()||hubSkuPendingTempDto.getSkuState()==SpuState.HANDLING.getIndex())){
			//TODO 已处理的如果不导出就不用加此判断
			return;
		}

		hubSkuPendingDto.setHubSkuSizeType(hubPendingSkuCheckResult.getSizeType());
		hubSkuPendingDto.setHubSkuSize(hubPendingSkuCheckResult.getSizeValue());
		if (hubPendingSkuCheckResult.isPassing()) {
			//不再直接进入待选品，所有屏蔽下面代码，改为进入待复合
//			if(hubSkuPendingTempDto!=null){
//				if(hubSpuNo!=null){
//					hubSkuPendingDto.setSkuState((byte) SpuState.HANDLING.getIndex());
//				}else{
//					hubSkuPendingDto.setSkuState((byte) SpuState.INFO_IMPECCABLE.getIndex());
//				}
//			}else{
//				hubSkuPendingDto.setSkuState((byte) SpuState.INFO_IMPECCABLE.getIndex());
//			}
			hubSkuPendingDto.setSkuState( SpuState.INFO_IMPECCABLE.getIndex());
			hubSkuPendingDto.setSpSkuSizeState((byte) 1);
			hubSkuPendingDto.setScreenSize(hubPendingSkuCheckResult.getSizeId());
			hubSkuPendingDto.setFilterFlag((byte)1);
		} else {
			if(isMultiSizeType){
				hubSkuPendingDto.setSkuState((byte) SpuState.INFO_PECCABLE.getIndex());
				//此尺码含有多个尺码类型，需要手动选择
				hubSkuPendingDto.setFilterFlag((byte)1);
				hubSkuPendingDto.setSpSkuSizeState((byte) 0);
				hubSkuPendingDto.setMemo(hubPendingSkuCheckResult.getMessage());
			}else  if(hubPendingSkuCheckResult.isFilter()){//排除
				hubSkuPendingDto.setSkuState((byte) SpuState.INFO_IMPECCABLE.getIndex());
				hubSkuPendingDto.setMemo(hubPendingSkuCheckResult.getMessage());
				hubSkuPendingDto.setSpSkuSizeState((byte) 1);
				hubSkuPendingDto.setFilterFlag((byte)0);
				hubSkuPendingDto.setHubSkuSizeType("排除");
			}else{
				hubSkuPendingDto.setSkuState((byte) SpuState.INFO_PECCABLE.getIndex());
				//此尺码过滤不处理
				hubSkuPendingDto.setMemo(hubPendingSkuCheckResult.getMessage());
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
	}
	public String convertExcelMade(List<Map<String, String>> result, String taskNo) throws Exception {
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String resultFileName = sim.format(new Date());
		File filePath = new File(ftpProperties.getLocalResultPath());
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		String pathFile = ftpProperties.getLocalResultPath() + resultFileName + ".xls";
		File file = new File(pathFile);
		FileOutputStream out = new FileOutputStream(file);

		String[] headers = { "任务编号", "供货商产地", "尚品产地", "任务说明"};
		String[] columns = { "taskNo", "supplierVal", "hubVal", "task"};
		ExportExcelUtils.exportExcel(resultFileName, headers, columns, result, out);
		// 4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
		String path = FTPClientUtil.uploadFile(file, resultFileName + ".xls");
		FTPClientUtil.closeFtp();
		if (file.exists()) {
			file.delete();
		}
		// 更新结果文件路径到表中

		return path + resultFileName + ".xls";
	}

	public String convertExcelBrand(List<Map<String, String>> result, String taskNo) throws Exception {
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String resultFileName = sim.format(new Date());
		File filePath = new File(ftpProperties.getLocalResultPath());
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		String pathFile = ftpProperties.getLocalResultPath() + resultFileName + ".xls";
		File file = new File(pathFile);
		FileOutputStream out = new FileOutputStream(file);

		String[] headers = { "任务编号", "供应商品牌", "尚品品牌", "任务说明" };
		String[] columns = { "taskNo", "supplierBrand", "hubBrandNo", "task"};
		ExportExcelUtils.exportExcel(resultFileName, headers, columns, result, out);
		// 4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
		String path = FTPClientUtil.uploadFile(file, resultFileName + ".xls");
		FTPClientUtil.closeFtp();
		if (file.exists()) {
			file.delete();
		}
		// 更新结果文件路径到表中

		return path + resultFileName + ".xls";
	}
	public String convertExcelCategory(List<Map<String, String>> result, String taskNo) throws Exception {
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String resultFileName = sim.format(new Date());
		File filePath = new File(ftpProperties.getLocalResultPath());
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		String pathFile = ftpProperties.getLocalResultPath() + resultFileName + ".xls";
		File file = new File(pathFile);
		FileOutputStream out = new FileOutputStream(file);

		String[] headers = { "任务编码", "供货商品类", "品类编码", "任务说明"};
		String[] columns = { "taskNo", "supplierCategory", "hubCategoryNo","task"};
		ExportExcelUtils.exportExcel(resultFileName, headers, columns, result, out);
		// 4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
		String path = FTPClientUtil.uploadFile(file, resultFileName + ".xls");
		FTPClientUtil.closeFtp();
		if (file.exists()) {
			file.delete();
		}
		// 更新结果文件路径到表中

		return path + resultFileName + ".xls";
	}
	public String convertExcelColor(List<Map<String, String>> result, String taskNo) throws Exception {
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String resultFileName = sim.format(new Date());
		File filePath = new File(ftpProperties.getLocalResultPath());
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		String pathFile = ftpProperties.getLocalResultPath() + resultFileName + ".xls";
		File file = new File(pathFile);
		FileOutputStream out = new FileOutputStream(file);

		String[] headers = {"任务编号","供应商颜色","sp颜色","任务状态" };
		String[] columns = {"colorDicItemId","colorItemName","hubcolor","task"};
		ExportExcelUtils.exportExcel(resultFileName, headers, columns, result, out);
		// 4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
		String path = FTPClientUtil.uploadFile(file, resultFileName + ".xls");
		FTPClientUtil.closeFtp();
		if (file.exists()) {
			file.delete();
		}
		// 更新结果文件路径到表中

		return path + resultFileName + ".xls";
	}

	//材质状态
	public String convertExcelMarterial(List<Map<String, String>> result, String taskNo) throws Exception {
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String resultFileName = sim.format(new Date());
		File filePath = new File(ftpProperties.getLocalResultPath());
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		String pathFile = ftpProperties.getLocalResultPath() + resultFileName + ".xls";
		File file = new File(pathFile);
		FileOutputStream out = new FileOutputStream(file);
		String[] header = {"任务编码", "尚品材质名","供应商材质名","任务状态"};
		String[] column = {"taskNo", "hubMaterial","supplierMaterial","task"};
		ExportExcelUtils.exportExcel(resultFileName,header,column, result,out);
		// 4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
		String path = FTPClientUtil.uploadFile(file, resultFileName + ".xls");
		FTPClientUtil.closeFtp();
		if (file.exists()) {
			file.delete();
		}
		// 更新结果文件路径到表中

		return path + resultFileName + ".xls";
	}

	public XSSFSheet checkXlsxExcel(InputStream in, Task task, String type) throws Exception {

		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(in);
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		if (xssfSheet.getRow(0) == null) {
			log.info("任务编号：" + task.getTaskNo() + "," + task.getData() + "下载的excel数据为空");
			updateHubSpuImportByTaskNo(TaskState.SOME_SUCCESS.getIndex(), task.getTaskNo(), "下载的excel数据为空", null);
			return null;
		}
		boolean flag = checkXlsxFileTemplet(xssfSheet.getRow(0), type,task.getTaskNo());
		if (!flag) {
			return null;
		}
		return xssfSheet;
	}

	public HSSFSheet checkXlsExcel(InputStream in, Task task, String type) throws Exception {

		HSSFWorkbook xssfWorkbook = new HSSFWorkbook(in);
		HSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		if (xssfSheet.getRow(0) == null) {
			log.info("任务编号：" + task.getTaskNo() + "," + task.getData() + "下载的excel数据为空");
			updateHubSpuImportByTaskNo(TaskState.SOME_SUCCESS.getIndex(), task.getTaskNo(), "下载的excel数据为空", null);
			return null;
		}
		boolean flag = checkXlsFileTemplet(xssfSheet.getRow(0), type,task.getTaskNo());
		if (!flag) {
			return null;
		}
		return xssfSheet;
	}

	public  boolean checkXlsxFileTemplet(XSSFRow xssfRow, String type,String taskNo) {

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
		if ("color".equals(type)) {
			for (int i = 0; i < colorTemplate.length; i++) {
				if (xssfRow.getCell(i) != null) {
					String fieldName = xssfRow.getCell(i).toString();
					if (!colorTemplate[i].equals(fieldName)) {
						flag = false;
						break;
					}
				}
			}
		}
		if ("made".equals(type)) {
			for (int i = 0; i < madeTemplate.length; i++) {
				if (xssfRow.getCell(i) != null) {
					String fieldName = xssfRow.getCell(i).toString();
					if (!madeTemplate[i].equals(fieldName)) {
						flag = false;
						break;
					}
				}
			}
		}
		if ("material".equals(type)) {
			for (int i = 0; i < materialTemplate.length; i++) {
				if (xssfRow.getCell(i) != null) {
					String fieldName = xssfRow.getCell(i).toString();
					if (!materialTemplate[i].equals(fieldName)) {
						flag = false;
						break;
					}
				}
			}
		}
		if ("brand".equals(type)) {
			for (int i = 0; i < brandTemplate.length; i++) {
				if (xssfRow.getCell(i) != null) {
					String fieldName = xssfRow.getCell(i).toString();
					if (!brandTemplate[i].equals(fieldName)) {
						flag = false;
						break;
					}
				}
			}
		}

		if(!flag){
			log.info("任务编号：" + taskNo + "," + "上传文件与模板不一致");
			updateHubSpuImportByTaskNo(TaskState.SOME_SUCCESS.getIndex(),taskNo, "上传文件与模板不一致", null);
				
		}
		return flag;
	}

	public  boolean checkXlsFileTemplet(HSSFRow xssfRow, String type,String taskNo) {

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
		if ("color".equals(type)) {
			for (int i = 0; i < colorTemplate.length; i++) {
				if (xssfRow.getCell(i) != null) {
					String fieldName = xssfRow.getCell(i).toString();
					if (!colorTemplate[i].equals(fieldName)) {
						flag = false;
						break;
					}
				}
			}
		}
		if ("categroy".equals(type)) {
			for (int i = 0; i < categroyTemplate.length; i++) {
				if (xssfRow.getCell(i) != null) {
					String fieldName = xssfRow.getCell(i).toString();
					if (!categroyTemplate[i].equals(fieldName)) {
						flag = false;
						break;
					}
				}
			}
		}
		if ("made".equals(type)) {
			for (int i = 0; i <madeTemplate.length; i++) {
				if (xssfRow.getCell(i) != null) {
					String fieldName = xssfRow.getCell(i).toString();
					if (!madeTemplate[i].equals(fieldName)) {
						flag = false;
						break;
					}
				}
			}
		}
		if ("material".equals(type)) {
			for (int i = 0; i < materialTemplate.length; i++) {
				if (xssfRow.getCell(i) != null) {
					String fieldName = xssfRow.getCell(i).toString();
					if (!materialTemplate[i].equals(fieldName)) {
						flag = false;
						break;
					}
				}
			}
		}
		if ("brand".equals(type)) {
			for (int i = 0;i<brandTemplate.length; i++) {
				if (xssfRow.getCell(i) != null) {
					String fieldName = xssfRow.getCell(i).toString();
					if (!brandTemplate[i].equals(fieldName)) {
						flag = false;
						break;
					}
				}
			}
		}
		if ("supplierData".equals(type)) {
			for (int i = 0; i < supplierDataTemplate.length; i++) {
				if (xssfRow.getCell(i) != null) {
					String fieldName = xssfRow.getCell(i).toString();
					if (!supplierDataTemplate[i].equals(fieldName)) {
						flag = false;
						break;
					}
				}
			}
		}
		if(!flag){
			updateHubSpuImportByTaskNo(TaskState.SOME_SUCCESS.getIndex(),taskNo, "上传文件与模板不一致", null);			
		}
		return flag;
	}

	public InputStream downFileFromFtp(Task task) throws Exception {
		
		InputStream in = FTPClientUtil.downFile(task.getData());
		if (in == null) {
			log.info("任务编号：" + task.getTaskNo() + "," + task.getData() + "从ftp下载失败数据为空");
			updateHubSpuImportByTaskNo(TaskState.SOME_SUCCESS.getIndex(), task.getTaskNo(), "从ftp下载失败", null);
			return null;
		}
		return in;
	}

	public void checkPendingSpu(HubSpuPendingDto isPendingSpuExist,HubPendingSkuCheckResult hubPendingSkuCheckResult,
			HubSpuPendingDto hubPendingSpuDto,Map<String, String> map,boolean skuIsPassing) {
		
		Long pendingSpuId = null;
		boolean spuIsPassing = false;
		boolean hubIsExist= false;
		Long hubSpuId= null;
		String hubSpuNo = null;
		String checkResult = null;
		
		// 校验货号
		 HubPendingSpuCheckResult hubPendingSpuCheckResult = pendingSpuCheckGateWay.checkSpu(hubPendingSpuDto);
		log.info(hubPendingSpuDto.getSpuModel()+"检验spu结果：{}",hubPendingSpuCheckResult);
		String spuModel = null;
		if (hubPendingSpuCheckResult.isSpuModel()) {
			spuModel = hubPendingSpuCheckResult.getModel();
			// 查询货号是否已存在hubSpu中
			hubPendingSpuDto.setSpuModel(spuModel);
			HubSpuDto list = dataHandleService.selectHubSpu(hubPendingSpuDto.getSpuModel(),hubPendingSpuDto.getHubBrandNo());
			if (list != null) {
                //颜色需要保留供货商的颜色 如果没有匹配上 取hubspu中的颜色
				String suplierColor = hubPendingSpuDto.getHubColor();
				convertHubSpuToPendingSpu(hubPendingSpuDto, list,hubPendingSpuCheckResult);

				hubSpuId = list.getSpuId();
				hubSpuNo = list.getSpuNo();
				spuIsPassing = true;
				if(suplierColor!=null&&suplierColor.equals(list.getHubColor())){
					hubIsExist = true;
					checkResult = spuModel+"在hub已存在，并且颜色一致";
				}else{
					hubIsExist = false;
					checkResult = spuModel+"hub已存在,但颜色不一样,hub颜色："+list.getHubColor()+",待处理颜色："+suplierColor;
				}
				hubPendingSpuCheckResult.setPassing(true);
			} else {
				// 货号不存在hubSpu中,继续校验其它信息，查询pendingSpu是否存在==》保存或更新pendingSpu表
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
			hubPendingSpuCheckResult.setPassing(false);
			spuIsPassing = false;
			hubIsExist = false;
			checkResult = "货号校验失败";
		}
		String memo = "spu:"+checkResult+",sku:"+hubPendingSkuCheckResult.getMessage();
		boolean allFilter = false;
		if(map.get("allFilter")!=null){
			allFilter = Boolean.parseBoolean(map.get("allFilter"));
		}
		boolean noSku = false;
		if(map.get("noSku")!=null){
			noSku = Boolean.parseBoolean(map.get("noSku"));
		}
		
		pendingSpuId = saveOrUpdatePendingSpu(noSku,allFilter,hubIsExist,isPendingSpuExist, hubPendingSpuDto, 
				hubPendingSpuCheckResult,skuIsPassing,memo);
		if (spuIsPassing==true&&skuIsPassing==true) {
			map.put("taskState", "校验通过");
			map.put("processInfo", "spu:"+checkResult+",sku:"+hubPendingSkuCheckResult.getMessage());
		} else {
			map.put("taskState", "校验失败");
			map.put("processInfo", "spu："+checkResult+",sku:"+hubPendingSkuCheckResult.getMessage());
		}
	
		map.put("pendingSpuId",pendingSpuId+"");
		map.put("hubIsExist",hubIsExist+"");
		map.put("isPassing",spuIsPassing+"");
		map.put("hubSpuId",hubSpuId+"");
		map.put("hubSpuNo",hubSpuNo);
	}
	private void convertHubSpuToPendingSpu(HubSpuPendingDto hubPendingSpuDto, HubSpuDto hubSpuDto,HubPendingSpuCheckResult hubPendingSpuCheckResult) {
		hubPendingSpuDto.setHubBrandNo(hubSpuDto.getBrandNo());
		hubPendingSpuDto.setSpuBrandState(SpuBrandState.HANDLED.getIndex());
		hubPendingSpuDto.setHubCategoryNo(hubSpuDto.getCategoryNo());
		hubPendingSpuDto.setCatgoryState(CatgoryState.PERFECT_MATCHED.getIndex());
//		hubPendingSpuDto.setHubColor(hubSpuDto.getHubColor());
		hubPendingSpuDto.setHubColorNo(hubSpuDto.getHubColorNo());
		hubPendingSpuDto.setSpuColorState(SpuColorState.HANDLED.getIndex());
		hubPendingSpuDto.setHubGender(hubSpuDto.getGender());
		hubPendingSpuDto.setSpuGenderState(SpuGenderState.HANDLED.getIndex());
		hubPendingSpuDto.setHubMaterial(hubSpuDto.getMaterial());
		hubPendingSpuDto.setMaterialState(MaterialState.HANDLED.getIndex());
		hubPendingSpuDto.setHubOrigin(hubSpuDto.getOrigin());
		hubPendingSpuDto.setOriginState(OriginState.HANDLED.getIndex());
		if(StringUtils.isBlank(hubPendingSpuDto.getHubSeason())){
			hubPendingSpuDto.setHubSeason(hubSpuDto.getMarketTime()+"_"+hubSpuDto.getSeason());
		}
//		
		hubPendingSpuDto.setSpuSeasonState(SpuSeasonState.HANDLED.getIndex());
		hubPendingSpuDto.setHubSpuNo(hubSpuDto.getSpuNo());
		hubPendingSpuDto.setSpuModel(hubSpuDto.getSpuModel());
		hubPendingSpuDto.setSpuName(hubSpuDto.getSpuName());

		hubPendingSpuCheckResult.setMaterial(true);
		hubPendingSpuCheckResult.setBrand(true);
		hubPendingSpuCheckResult.setCategory(true);
		hubPendingSpuCheckResult.setColor(true);
		hubPendingSpuCheckResult.setGender(true);
		hubPendingSpuCheckResult.setOriginal(true);
		hubPendingSpuCheckResult.setSeasonName(true);
		hubPendingSpuCheckResult.setSpuModel(true);


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
	}

	private Long saveOrUpdatePendingSpu(boolean noSku,boolean allFilter,boolean hubIsExist,HubSpuPendingDto isPendingSpuExist, 
			HubSpuPendingDto hubPendingSpuDto,HubPendingSpuCheckResult hubPendingSpuCheckResult,boolean skuIsPassing,String memo) {

		Long pengingSpuId = null;
		boolean spuIsPassing = hubPendingSpuCheckResult.isPassing();
		if(isPendingSpuExist!=null){//&&skuIsPassing==true
			if(isPendingSpuExist.getSpuState()!=null&&(isPendingSpuExist.getSpuState().byteValue()==SpuState.HANDLED.getIndex()||isPendingSpuExist.getSpuState().byteValue()==SpuState.HANDLING.getIndex()||isPendingSpuExist.getSpuState().byteValue()==SpuState.INFO_IMPECCABLE.getIndex())){
				log.info("spu货号:"+isPendingSpuExist.getSpuModel()+"状态为："+isPendingSpuExist.getSpuState()+"，不更新");
				return isPendingSpuExist.getSpuPendingId();
			}
		}
		if (spuIsPassing&&skuIsPassing==true) {//&&skuIsPassing==true
			if(hubIsExist){
				//hub存在同品牌同货号同颜色并且有尺码信息，进入待选品
				//TODO 此处如果更改为待选品，后面审核推送可屏蔽
//				hubPendingSpuDto.setSpuState((byte) SpuState.HANDLED.getIndex());
				hubPendingSpuDto.setSpuState((byte) SpuState.INFO_IMPECCABLE.getIndex());	
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
			if(hubPendingSpuCheckResult.isSpuModel()){
				hubPendingSpuDto.setSpuModelState((byte)1);
			}else{
				hubPendingSpuDto.setSpuModelState((byte)0);
			}
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
		//TODO 全部排除的是否进入待审核
//		if(allFilter){
//			hubPendingSpuDto.setSpuState((byte)2);	
//		}
//		if(noSku){
//			hubPendingSpuDto.setSpuState((byte)2);
//		}
		
		//新加的 记录校验结果
		hubPendingSpuDto.setMemo(memo);
		if (isPendingSpuExist != null) {
			log.info("spu:" + isPendingSpuExist.getSpuModel()+ "," +isPendingSpuExist.getSupplierSpuNo() + "存在，更新操作");
			pengingSpuId = isPendingSpuExist.getSpuPendingId();
			hubPendingSpuDto.setUpdateTime(new Date());
			hubPendingSpuDto.setSpuPendingId(pengingSpuId);
			
			hubPendingSkuCheckGateWay.checkSkuBeforeAudit(hubPendingSpuDto);
			
			hubSpuPendingGateWay.updateByPrimaryKeySelective(hubPendingSpuDto);
			if(hubIsExist&&!allFilter){
				//2018-4-16需求 检验通过的直接进入待选品，跳过待审核
				if(SpuState.INFO_IMPECCABLE.getIndex()==hubPendingSpuDto.getSpuState()){
					String result = hubPendingHandleGateWay.audit(hubPendingSpuDto.getSpuPendingId());
					if(result!=null){
						log.info(hubPendingSpuDto.getSpuPendingId()+"待复核直接进入选品失败："+result);
					}
				}
			}
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
	public MatchSizeResult matchSize(String hubBrandNo,String categoryNo,String hubSize) {
		MatchSizeDto match = new MatchSizeDto();
		match.setHubBrandNo(hubBrandNo);
		match.setHubCategoryNo(categoryNo);
		match.setSize(hubSize);
		MatchSizeResult matchSizeResult = matchSizeGateWay.matchSize(match);
		return matchSizeResult;
	}

}
