package com.shangpin.asynchronous.task.consumer.productimport.pending.spu.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.asynchronous.task.consumer.productimport.common.service.TaskService;
import com.shangpin.asynchronous.task.consumer.productimport.common.util.ExportExcelUtils;
import com.shangpin.asynchronous.task.consumer.productimport.common.util.FTPClientUtil;
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.dao.HubPendingSpuImportDTO;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;
import com.shangpin.ephub.client.product.business.hubpending.sku.gateway.HubPendingSkuCheckGateWay;
import com.shangpin.ephub.client.product.business.hubpending.spu.dao.HubPendingSpuDto;
import com.shangpin.ephub.client.product.business.hubpending.spu.gateway.HubPendingSpuCheckGateWay;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.HubPendingSpuCheckResult;
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
	HubSpuImportTaskGateWay spuImportGateway;

	@Autowired
	HubSkuPendingGateWay hubSkuPendingGateWay;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	HubSpuPendingGateWay hubSpuPendingGateWay;
	
	@Autowired
	HubPendingSkuCheckGateWay pendingSkuCheckGateWay;
	@Autowired
	HubPendingSpuCheckGateWay pendingSpuCheckGateWay;
	
	private static String[] pendingSpuTemplate = null;
	static {
		pendingSpuTemplate = TaskImportTemplate.getPendingSpuTemplate();
	}
	
	//1、更新任务表，把task_state更新成正在处理  2、从ftp下载文件并解析成对象   3、公共类校验hub数据并把校验结果写入excel   4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
	public void handMessage(ProductImportTask task) throws Exception{
		
		//1、更新任务表，把task_state更新成正在处理
		taskService.updateHubSpuImportStatusByTaskNo(TaskState.HANDLEING.getIndex(),task.getTaskNo(),null);
		log.info("任务编号："+task.getTaskNo()+"状态更新成正在处理");
		// 2、从ftp下载文件并解析成对象
		List<HubPendingSpuImportDTO> listHubProduct = handleHubExcel(task.getTaskFtpFilePath(),task.getTaskNo());
		if(listHubProduct==null){
			return ;
		}
		//3、公共类校验hub数据并把校验结果写入excel
		 List<Map<String, String>> result = checkAndsaveHubPendingProduct(task.getTaskNo(),listHubProduct);
		 
		//4、处理结果的excel上传ftp，并更新任务表状态和文件在ftp的路径
		 convertExcel(result,task.getTaskNo());
	}

	private void convertExcel(List<Map<String, String>> result, String taskNo) throws Exception{
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String resultFileName = sim.format(new Date());
		File file = new File(resultFileName+".xls");
		FileOutputStream out = new FileOutputStream(file);
		
		String[] headers = { "任务编号", "货号", "任务状态", "任务说明"};
		String[] columns = { "taskNo", "spuModel","taskState", "processInfo"};
		ExportExcelUtils.exportExcel(resultFileName, headers, columns, result,
				out);
		//4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
		String path = FTPClientUtil.uploadFile(file, resultFileName+".xls");
		
		int status;
		if(result!=null&&result.size()>0){
			//部分成功
			status = TaskState.SOME_SUCCESS.getIndex();
		}else{
			//全部成功
			status = TaskState.ALL_SUCCESS.getIndex();
		}
		file.delete();
		
		taskService.updateHubSpuImportByTaskNo(taskNo,path+resultFileName+".xls",status);
		
	}

	//校验数据以及保存到hub表
	private List<Map<String, String>> checkAndsaveHubPendingProduct(String taskNo,List<HubPendingSpuImportDTO> listHubProduct) {

		List<Map<String, String>> listMap = new ArrayList<Map<String,String>>();
		for(HubPendingSpuImportDTO product:listHubProduct){
			
			Map<String, String> map = new HashMap<String, String>();
			//处理spu信息
			
			HubPendingSpuCheckResult hubPendingSpuCheckResult = handlePendingSpu(product);
			
			
			if(hubPendingSpuCheckResult.isPassing()==true){
				log.info(taskNo+"spu校验通过");
				//校验sku信息
					log.info(taskNo+"sku校验通过");
					
					updateOrSaveSpu(product);
					
					map.put("taskNo",taskNo);
					map.put("spuModel",product.getSpuModel());
					map.put("taskState","校验成功");
					map.put("processInfo","校验通过");
			}else {
				log.info(taskNo+"spu校验不通过");
				map.put("taskNo", taskNo);
				map.put("spuModel", product.getSpuModel());
				map.put("taskState", "校验失败");
				map.put("processInfo", hubPendingSpuCheckResult.getResult());
			}
			
			listMap.add(map);
		}
		return listMap;
	}

	
	private void updateOrSaveSpu(HubPendingSpuImportDTO product) {
		//查询数据库spu信息是否已存在，存在则更新，反之插入
		HubSpuPendingDto hubSpuPendingDto = convertHubPendingProduct2Spu(product);
		String spuId = product.getSpuPendingId();
		if(spuId!=null){
			hubSpuPendingDto.setSpuPendingId(Long.valueOf(spuId));
			hubSpuPendingGateWay.updateByPrimaryKey(hubSpuPendingDto);
		}
	}

	private HubPendingSpuCheckResult handlePendingSpu(HubPendingSpuImportDTO product) {
		//校验spu信息
		HubPendingSpuDto HubPendingSpuDto = convertHubPendingProduct2PendingSpu(product);
		return pendingSpuCheckGateWay.checkSpu(HubPendingSpuDto);
		
	}

	private HubPendingSpuDto convertHubPendingProduct2PendingSpu(
			HubPendingSpuImportDTO product) {
		HubPendingSpuDto HubPendingSpuDto = new HubPendingSpuDto();
		BeanUtils.copyProperties(product, HubPendingSpuDto);
		return HubPendingSpuDto;
	}


	private HubSpuPendingDto convertHubPendingProduct2Spu(HubPendingSpuImportDTO product) {
		
		HubSpuPendingDto hubSpuPendingDto = new HubSpuPendingDto();
		BeanUtils.copyProperties(product, hubSpuPendingDto);
		return hubSpuPendingDto;
	}

	//解析excel转换为对象
	private List<HubPendingSpuImportDTO> handleHubExcel(String taskFtpFilePath,String taskNo)  throws Exception {
		
		InputStream in = FTPClientUtil.downFile(taskFtpFilePath);
		if(in==null){
			log.info("任务编号："+taskNo+","+taskFtpFilePath+"从ftp下载失败数据为空");
			taskService.updateHubSpuImportStatusByTaskNo(TaskState.SOME_SUCCESS.getIndex(),taskNo,"从ftp下载失败");
		}
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(in);
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		if (xssfSheet.getRow(0) == null) {
			log.info("任务编号："+taskNo+","+taskFtpFilePath+"下载的excel数据为空");
			taskService.updateHubSpuImportStatusByTaskNo(TaskState.SOME_SUCCESS.getIndex(),taskNo,"下载的excel数据为空");
			return null;
		}
		boolean flag = checkFileTemplet(xssfSheet.getRow(0));
		if(!flag){
			//TODO 上传文件与模板不一致
			log.info("任务编号："+taskNo+","+taskFtpFilePath+"上传文件与模板不一致");
			taskService.updateHubSpuImportStatusByTaskNo(TaskState.SOME_SUCCESS.getIndex(),taskNo,"上传文件与模板不一致");
			return null;
		}
		List<HubPendingSpuImportDTO> listHubProduct = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			HubPendingSpuImportDTO product = convertSpuDTO(xssfRow);
			listHubProduct.add(product);
		}
		return listHubProduct;
	}
	@SuppressWarnings("unchecked")
	private static HubPendingSpuImportDTO convertSpuDTO(XSSFRow xssfRow) {
		HubPendingSpuImportDTO item = null;
		if (xssfRow != null) {
			try {
				item = new HubPendingSpuImportDTO();
				String [] hubValueTemplate = item.getPendingSpuTemplate();
				Class c = item.getClass();
				for (int i = 0; i < hubValueTemplate.length; i++) {
					if (xssfRow.getCell(i) != null) {
						xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
						String setMethodName = "set" + hubValueTemplate[i].toUpperCase().charAt(0)
								+ hubValueTemplate[i].substring(1);
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
	private static boolean checkFileTemplet(XSSFRow xssfRow) {
			
		boolean flag = true;
		for (int i = 0; i < pendingSpuTemplate.length; i++) {
			if (xssfRow.getCell(i) != null) {
				String fieldName = xssfRow.getCell(i).toString();
				if (!pendingSpuTemplate[i].equals(fieldName)) {
					flag = false;
					break;
				}
			}
		}
		return flag;
	}
}
