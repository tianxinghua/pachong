package com.shangpin.asynchronous.task.consumer.productimport.common.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.asynchronous.task.consumer.conf.ftp.FtpProperties;
import com.shangpin.asynchronous.task.consumer.productimport.common.util.ExportExcelUtils;
import com.shangpin.asynchronous.task.consumer.productimport.common.util.FTPClientUtil;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskCriteriaDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
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
	
	public boolean updateHubSpuImportStatusByTaskNo(int status,String taskNo,String processInfo) {
		HubSpuImportTaskWithCriteriaDto dto = new HubSpuImportTaskWithCriteriaDto();
		HubSpuImportTaskDto hubSpuImportTaskDto = new HubSpuImportTaskDto();
		hubSpuImportTaskDto.setTaskState((byte)status);
		hubSpuImportTaskDto.setUpdateTime(new Date());
		hubSpuImportTaskDto.setProcessInfo(processInfo);
		dto.setHubSpuImportTask(hubSpuImportTaskDto);
		HubSpuImportTaskCriteriaDto hubSpuImportTaskCriteriaDto = new HubSpuImportTaskCriteriaDto();
		hubSpuImportTaskCriteriaDto.createCriteria().andTaskNoEqualTo(taskNo);
		dto.setCriteria(hubSpuImportTaskCriteriaDto);
		spuImportGateway.updateByCriteriaSelective(dto);
		return true;
	}
	
	public void updateHubSpuImportByTaskNo(String taskNo, String resultFilePath, int status) {
		HubSpuImportTaskWithCriteriaDto dto = new HubSpuImportTaskWithCriteriaDto();
		HubSpuImportTaskDto hubSpuImportTaskDto = new HubSpuImportTaskDto();
		hubSpuImportTaskDto.setTaskState((byte)status);
		hubSpuImportTaskDto.setResultFilePath(resultFilePath);
		dto.setHubSpuImportTask(hubSpuImportTaskDto);
		HubSpuImportTaskCriteriaDto hubSpuImportTaskCriteriaDto = new HubSpuImportTaskCriteriaDto();
		HubSpuImportTaskCriteriaDto.Criteria criteria = hubSpuImportTaskCriteriaDto.createCriteria().andTaskNoEqualTo(taskNo);
		dto.setCriteria(hubSpuImportTaskCriteriaDto);
		int result = spuImportGateway.updateByCriteriaSelective(dto);
	}
	
	public void convertExcel(List<Map<String, String>> result, String taskNo,boolean flag) throws Exception{
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String resultFileName = sim.format(new Date());
		File filePath = new File(ftpProperties.getLocalResultPath());
		if(!filePath.exists()){
			filePath.mkdirs();
		}
		String pathFile = ftpProperties.getLocalResultPath()+resultFileName + ".xls";
		File file = new File(pathFile);
		FileOutputStream out = new FileOutputStream(file);
		
		String[] headers = { "任务编号", "货号", "任务状态", "任务说明"};
		String[] columns = { "taskNo", "spuModel","taskState", "processInfo"};
		ExportExcelUtils.exportExcel(resultFileName, headers, columns, result,
				out);
		//4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
		String path = FTPClientUtil.uploadFile(file, resultFileName+".xls");
		
		int status;
		if(flag){
			//部分成功
			status = TaskState.SOME_SUCCESS.getIndex();
		}else{
			//全部成功
			status = TaskState.ALL_SUCCESS.getIndex();
		}
		if(file.exists()){
			file.delete();	
		}
		updateHubSpuImportByTaskNo(taskNo,path+resultFileName+".xls",status);
	}
	
	public XSSFSheet checkExcel(String taskFtpFilePath, String taskNo,String type) throws Exception{
		InputStream in = FTPClientUtil.downFile(taskFtpFilePath);
		if(in==null){
			log.info("任务编号："+taskNo+","+taskFtpFilePath+"从ftp下载失败数据为空");
			updateHubSpuImportStatusByTaskNo(TaskState.SOME_SUCCESS.getIndex(),taskNo,"从ftp下载失败");
			return null;
		}
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(in);
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		if (xssfSheet.getRow(0) == null) {
			log.info("任务编号："+taskNo+","+taskFtpFilePath+"下载的excel数据为空");
			updateHubSpuImportStatusByTaskNo(TaskState.SOME_SUCCESS.getIndex(),taskNo,"下载的excel数据为空");
			return null;
		}
		boolean flag = checkFileTemplet(xssfSheet.getRow(0),type);
		if(!flag){
			log.info("任务编号："+taskNo+","+taskFtpFilePath+"上传文件与模板不一致");
			updateHubSpuImportStatusByTaskNo(TaskState.SOME_SUCCESS.getIndex(),taskNo,"上传文件与模板不一致");
			return null;
		}
		return xssfSheet;
	}
	
	public static boolean checkFileTemplet(XSSFRow xssfRow,String type) {
		
		boolean flag = true;
		if("spu".equals(type)){
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
		if("sku".equals(type)){
			for (int i = 0; i < pendingSkuTemplate.length; i++) {
				if (xssfRow.getCell(i) != null) {
					String fieldName = xssfRow.getCell(i).toString();
					if (!pendingSpuTemplate[i].equals(fieldName)) {
						flag = false;
						break;
					}
				}
			}
		}
		
		return flag;
	}
}
