package com.shangpin.asynchronous.task.consumer.productimport.common.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.shangpin.ephub.client.data.mysql.enumeration.HubSpuState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.product.dto.HubPendingDto;
import com.shangpin.ephub.client.data.mysql.product.dto.SpuModelDto;
import com.shangpin.ephub.client.data.mysql.product.gateway.PengdingToHubGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskCriteriaDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;
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

	public void convertExcel(List<Map<String, String>> result, String taskNo) throws Exception {
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
		updateHubSpuImportByTaskNo(TaskState.ALL_SUCCESS.getIndex(), taskNo, null, path + resultFileName + ".xls");
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
		FTPClientUtil.closeFtp();
		if (in == null) {
			log.info("任务编号：" + task.getTaskNo() + "," + task.getData() + "从ftp下载失败数据为空");
			updateHubSpuImportByTaskNo(TaskState.SOME_SUCCESS.getIndex(), task.getTaskNo(), "从ftp下载失败", null);
			return null;
		}
		return in;
	}

	public void checkPendingSpu1(HubSpuPendingDto hubPendingSpuDto, Map<String, String> map) {
		Long pendingSpuId = null;
		boolean isPassing = false;
		boolean hubIsExist= false;
		Long hubSpuId= null;
		String checkResult = null;
		HubSpuPendingDto isExist = null;
		// 查询是否已存在pendingSpu表中
		List<HubSpuPendingDto> listSpu = dataHandleService.selectPendingSpu(hubPendingSpuDto);
		if (listSpu != null && listSpu.size() > 0) {
			if (SpuState.HANDLED.getIndex() == listSpu.get(0).getSpuState()) {
				log.info(hubPendingSpuDto.getSupplierSpuNo() + "spu状态为已处理，不再操作");
				map.put("taskState", "校验通过");
				map.put("processInfo", "spu状态为已处理，不再操作");
				pendingSpuId = listSpu.get(0).getSpuPendingId();
				hubIsExist = false;
				isPassing = true;
				map.put("pendingSpuId",pendingSpuId+"");
				map.put("isSaveHub",hubIsExist+"");
				map.put("isPassing",isPassing+"");
				return;
			}
			isExist = listSpu.get(0);
		}
		// 校验货号
		HubPendingSpuCheckResult hubPendingSpuCheckResult = null;
		BrandModelResult result = dataHandleService.checkSpuModel(hubPendingSpuDto);
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
				isPassing = true;
				hubIsExist = true;
				checkResult = spuModel+"已存在选品";
			} else {
				// 货号不存在hubSpu中,继续校验其它信息，查询pendingSpu是否存在==》保存或更新pendingSpu表
				hubPendingSpuCheckResult = pendingSpuCheckGateWay.checkSpu(hubPendingSpuDto);
				if (hubPendingSpuCheckResult.isPassing()) {
					// 其它信息校验通过，需要推送hub，查询pendingSpu是否存在==》保存或更新pendingSpu表
					isPassing = true;
					hubIsExist = false;
					checkResult = spuModel+"已推送待选品";
				} else {
					isPassing = false;
					hubIsExist = false;
					checkResult = hubPendingSpuCheckResult.getResult();
				}
			}
		} else {
			isPassing = false;
			hubIsExist = false;
			checkResult = "货号校验失败";
		}
		pendingSpuId = saveOrUpdatePendingSpu(hubIsExist,isExist, hubPendingSpuDto, isPassing);
		if (isPassing) {
			map.put("taskState", "校验通过");
		} else {
			map.put("taskState", "校验失败");
		}
		map.put("processInfo", checkResult);
		map.put("pendingSpuId",pendingSpuId+"");
		map.put("hubIsExist",hubIsExist+"");
		map.put("isPassing",isPassing+"");
		map.put("hubSpuId",hubSpuId+"");
	}
	public void checkPendingSpu(HubSpuPendingDto hubPendingSpuDto, Map<String, String> map) {
		
		Long pendingSpuId = null;
		boolean isPassing = false;
		boolean hubIsExist= false;
		Long hubSpuId= null;
		String checkResult = null;
		HubSpuPendingDto isExist = null;
		
		// 查询是否已存在pendingSpu表中
		List<HubSpuPendingDto> listSpu = dataHandleService.selectPendingSpu(hubPendingSpuDto);
		if (listSpu != null && listSpu.size() > 0) {
			isExist = listSpu.get(0);
		}
		
		
		// 校验货号
		HubPendingSpuCheckResult hubPendingSpuCheckResult = null;
		BrandModelResult result = dataHandleService.checkSpuModel(hubPendingSpuDto);
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
				log.info("hubSpu已存在，hubSpuId:"+hubSpuId);
				isPassing = true;
				hubIsExist = true;
				checkResult = spuModel+"已存在选品";
			} else {
				// 货号不存在hubSpu中,继续校验其它信息，查询pendingSpu是否存在==》保存或更新pendingSpu表
				hubPendingSpuCheckResult = pendingSpuCheckGateWay.checkSpu(hubPendingSpuDto);
				if (hubPendingSpuCheckResult.isPassing()) {
					// 其它信息校验通过，需要推送hub，查询pendingSpu是否存在==》保存或更新pendingSpu表
					isPassing = true;
					hubIsExist = false;
					checkResult = spuModel+"已推送待选品";
				} else {
					isPassing = false;
					hubIsExist = false;
					checkResult = hubPendingSpuCheckResult.getResult();
				}
			}
		} else {
			isPassing = false;
			hubIsExist = false;
			checkResult = "货号校验失败";
		}

		
		pendingSpuId = saveOrUpdatePendingSpu(hubIsExist,isExist, hubPendingSpuDto, isPassing);
		if (isPassing) {
			map.put("taskState", "校验通过");
		} else {
			map.put("taskState", "校验失败");
		}
		map.put("processInfo", checkResult);
		map.put("pendingSpuId",pendingSpuId+"");
		map.put("hubIsExist",hubIsExist+"");
		map.put("isPassing",isPassing+"");
		map.put("hubSpuId",hubSpuId+"");
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

	public void sendToHub(HubSpuPendingDto hubPendingSpuDto, boolean isSaveHub, String hubSpuId) {
		if (isSaveHub) {
			HubPendingDto hubPendingDto = new HubPendingDto();
			if(hubSpuId!=null){
				hubPendingDto.setHubSpuId(Long.parseLong(hubSpuId));	
			}
			hubPendingDto.setHubSpuPendingId(hubPendingSpuDto.getSpuPendingId());
			log.info("pendingToHub.addSkuOrSkuSupplierMapping推送参数:{}", hubPendingDto);
			//更新
			pengdingToHubGateWay.addSkuOrSkuSupplierMapping(hubPendingDto);
			

		} else {
			SpuModelDto spuModelDto = new SpuModelDto();
			spuModelDto.setBrandNo(hubPendingSpuDto.getHubBrandNo());
			spuModelDto.setSpuModel(hubPendingSpuDto.getSpuModel());
			log.info("pendingToHub.auditPending推送参数:{}", spuModelDto);
			pengdingToHubGateWay.auditPending(spuModelDto);
		}
	}

	private Long saveOrUpdatePendingSpu(boolean hubIsExist,HubSpuPendingDto isExist, HubSpuPendingDto hubPendingSpuDto,
			boolean isPassing) {

		Long pengingSpuId = null;
		
		if(isExist!=null){
			pengingSpuId = isExist.getSpuPendingId();
			if(isExist.getSpuState()==SpuState.HANDLED.getIndex()||isExist.getSpuState()==SpuState.HANDLING.getIndex()){
				return pengingSpuId;
			}
		}
		
		if (isPassing) {
			if(hubIsExist){
				hubPendingSpuDto.setSpuState((byte) SpuState.HANDLED.getIndex());	
			}else{
				hubPendingSpuDto.setSpuState((byte) SpuState.HANDLING.getIndex());
			}
			
		} else {
			hubPendingSpuDto.setSpuState((byte) SpuState.INFO_PECCABLE.getIndex());
		}
		if (isExist != null) {
			log.info("spu:" + isExist.getSpuModel()+ "," +isExist.getSupplierSpuNo() + "存在");
			pengingSpuId = isExist.getSpuPendingId();
			hubPendingSpuDto.setUpdateTime(new Date());
			hubPendingSpuDto.setSpuPendingId(pengingSpuId);
			hubSpuPendingGateWay.updateByPrimaryKeySelective(hubPendingSpuDto);
		} else {
			log.info("spu:" + hubPendingSpuDto.getSpuModel() + "不存在");
			hubPendingSpuDto.setCreateTime(new Date());
			hubPendingSpuDto.setUpdateTime(new Date());
			hubPendingSpuDto.setSpuSeasonState((byte) 1);
			hubPendingSpuDto.setUpdateTime(new Date());
			hubPendingSpuDto.setSupplierSpuId(0l);
			pengingSpuId = hubSpuPendingGateWay.insert(hubPendingSpuDto);
		}
		log.info("pengingSpuId:"+pengingSpuId);
		return pengingSpuId;
	}

}
