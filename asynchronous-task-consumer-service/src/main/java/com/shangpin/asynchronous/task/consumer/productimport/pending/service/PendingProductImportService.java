package com.shangpin.asynchronous.task.consumer.productimport.pending.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.asynchronous.task.consumer.productimport.common.TaskService;
import com.shangpin.asynchronous.task.consumer.productimport.hub.util.ExportExcelUtils;
import com.shangpin.asynchronous.task.consumer.productimport.hub.util.FTPClientUtil;
import com.shangpin.asynchronous.task.consumer.productimport.pending.dto.HubPendingProductImportDTO;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskCriteriaDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;

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
public class PendingProductImportService {
	
	@Autowired
	HubSpuImportTaskGateWay spuImportGateway;

	@Autowired
	TaskService taskService;
	
	//1、更新任务表，把task_state更新成正在处理  2、从ftp下载文件并解析成对象   3、公共类校验hub数据并把校验结果写入excel   4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
	public void handMessage(ProductImportTask task) throws Exception{
		
		//1、更新任务表，把task_state更新成正在处理
		taskService.updateHubSpuImportStatusByTaskNo(TaskState.HANDLEING.getIndex(),task.getTaskNo(),null);
		
		// 2、从ftp下载文件并解析成对象
		List<HubPendingProductImportDTO> listHubProduct = handleHubExcel(task.getTaskFtpFilePath(),task.getTaskNo());
		
		//3、公共类校验hub数据并把校验结果写入excel
		 List<Map<String, String>> result = checkAndsaveHubProduct(task.getTaskNo(),listHubProduct);
		 
		//4、处理结果的excel上传ftp，并更新任务表状态和文件在ftp的路径
		 convertExcel(result,task.getTaskNo());
		 
	}

	private void convertExcel(List<Map<String, String>> result, String taskNo) throws Exception{
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String resultFileName = sim.format(new Date());
		FileOutputStream out = new FileOutputStream(new File(resultFileName+".xlsx"));
		
		String[] headers = { "任务编号", "货号", "任务状态", "任务说明"};
		String[] columns = { "taskNo", "spuModel","taskState", "processInfo"};
		ExportExcelUtils.exportExcel(resultFileName, headers, columns, result,
				out);
		//4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
		String path = FTPClientUtil.uploadFile(new File(resultFileName+".xlsx"), resultFileName+".xlsx");
		
		int status;
		if(result!=null&&result.size()>0){
			//部分成功
			status = 2;
		}else{
			//全部成功
			status = 3;
		}
		taskService.updateHubSpuImportByTaskNo(taskNo,path+resultFileName+".xlsx",status);
		
	}

	//校验数据以及保存到hub表
	private List<Map<String, String>> checkAndsaveHubProduct(String taskNo,List<HubPendingProductImportDTO> listHubProduct) {

		List<Map<String, String>> listMap = new ArrayList<Map<String,String>>();
		for(HubPendingProductImportDTO product:listHubProduct){
			Map<String, String> map = new HashMap<String, String>();
			//TODO hub商品入库前的公共校验方法
			
			map.put("taskNo",taskNo);
			map.put("spuModel",product.getProductCode());
			map.put("taskState","校验失败");
			map.put("processInfo","不合格");
			listMap.add(map);
		}
		return listMap;
	}

	//解析excel转换为对象
	private List<HubPendingProductImportDTO> handleHubExcel(String taskFtpFilePath,String taskNo)  throws Exception {
		
		InputStream in = FTPClientUtil.downFile(taskFtpFilePath);
		if(in==null){
			taskService.updateHubSpuImportStatusByTaskNo(TaskState.SOME_SUCCESS.getIndex(),taskNo,"从ftp下载失败");
		}
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(in);
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		if (xssfSheet.getRow(0) == null) {
			taskService.updateHubSpuImportStatusByTaskNo(TaskState.SOME_SUCCESS.getIndex(),taskNo,"下载的excel数据为空");
			return null;
		}
		boolean flag = checkFileTemplet(xssfSheet.getRow(0));
		if(!flag){
			//TODO 上传文件与模板不一致
			taskService.updateHubSpuImportStatusByTaskNo(TaskState.SOME_SUCCESS.getIndex(),taskNo,"上传文件与模板不一致");
			return null;
		}
		List<HubPendingProductImportDTO> listHubProduct = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			HubPendingProductImportDTO product = convertSpuDTO(xssfRow);
			listHubProduct.add(product);
		}
		return listHubProduct;
	}
	private static HubPendingProductImportDTO convertSpuDTO(XSSFRow xssfRow) {
		HubPendingProductImportDTO item = null;
		if (xssfRow != null) {
			try {
				item = new HubPendingProductImportDTO();
				if (xssfRow.getCell(0) != null) {
					xssfRow.getCell(0).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setSupplierNo(xssfRow.getCell(0).toString());
				}
				if (xssfRow.getCell(1) != null) {
					xssfRow.getCell(1).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setCategoryName(xssfRow.getCell(1).toString());
				}
				if (xssfRow.getCell(2) != null) {
					xssfRow.getCell(2).setCellType(
							Cell.CELL_TYPE_STRING);
					xssfRow.getCell(3).setCellType(
							Cell.CELL_TYPE_STRING);
					String categoryNo = xssfRow.getCell(2).toString();
					String brandNo = xssfRow.getCell(3).toString();
					item.setCategoryNo(categoryNo);
					item.setBrandNo(brandNo);
				}
				if (xssfRow.getCell(4) != null) {
					xssfRow.getCell(4).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setBrandName(xssfRow.getCell(4).toString());
				}
				if (xssfRow.getCell(5) != null) {
					xssfRow.getCell(5).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setProductCode(xssfRow.getCell(5).toString());
				}
				if (xssfRow.getCell(6) != null) {
					xssfRow.getCell(6).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setSeasonYear(xssfRow.getCell(6).toString());
				}
				if (xssfRow.getCell(7) != null) {
					xssfRow.getCell(7).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setSeasonName(xssfRow.getCell(7).toString());
				}
				if (xssfRow.getCell(8) != null) {
					xssfRow.getCell(8).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setGender(xssfRow.getCell(8).toString());
				}
				if (xssfRow.getCell(9) != null) {
					xssfRow.getCell(9).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setSupplierSkuNo(xssfRow.getCell(9).toString());
				}
				if (xssfRow.getCell(10) != null) {
					xssfRow.getCell(10).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setProductName(xssfRow.getCell(10).toString());
				}
				if (xssfRow.getCell(11) != null) {
					xssfRow.getCell(11).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setBarcode(xssfRow.getCell(11).toString());
				}
				if (xssfRow.getCell(12) != null) {
					xssfRow.getCell(12).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setColor(xssfRow.getCell(12).toString());
				}
				if (xssfRow.getCell(13) != null) {
					xssfRow.getCell(13).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setSpecificationType(xssfRow.getCell(13).toString());
				}
				if (xssfRow.getCell(14) != null) {
					xssfRow.getCell(14).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setSizeType(xssfRow.getCell(14).toString());
				}
				if (xssfRow.getCell(15) != null) {
					xssfRow.getCell(15).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setSize(xssfRow.getCell(15).toString());
				}
				if (xssfRow.getCell(16) != null) {
					xssfRow.getCell(16).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setMaterial(xssfRow.getCell(16).toString());
				}
				if (xssfRow.getCell(17) != null) {
					xssfRow.getCell(17).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setMadeIn(xssfRow.getCell(17).toString());
				}
				if (xssfRow.getCell(18) != null) {
					xssfRow.getCell(18).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setSupplierPrice(xssfRow.getCell(18).toString());
				}
				if (xssfRow.getCell(19) != null) {
					xssfRow.getCell(19).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setSupplierCurrency(xssfRow.getCell(18).toString());
				}
				if (xssfRow.getCell(20) != null) {
					xssfRow.getCell(20).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setMarketPrice(xssfRow.getCell(20).toString());
				}
				if (xssfRow.getCell(21) != null) {
					xssfRow.getCell(21).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setMarketCurrency(xssfRow.getCell(21).toString());
				}
				if (xssfRow.getCell(22) != null) {
					xssfRow.getCell(22).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setMeasure(xssfRow.getCell(22).toString());
				}
				if (xssfRow.getCell(23) != null) {
					xssfRow.getCell(23).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setDescription(xssfRow.getCell(23).toString());
				}
			} catch (Exception ex) {
				ex.getStackTrace();
			}
		}
		return item;
	}
private static boolean checkFileTemplet(XSSFRow xssfRow) {
		
		Map<Integer,String> excelMappingMap = new HashMap<Integer,String>();
		excelMappingMap.put(0,"供应商名称");
		excelMappingMap.put(0,"品类名称");
		excelMappingMap.put(1,"品类编号*");
		excelMappingMap.put(2,"品牌编号*");
		excelMappingMap.put(3,"品牌名称");
		excelMappingMap.put(4,"货号*");
		excelMappingMap.put(5,"适应性别*");
		excelMappingMap.put(6,"颜色*");
		excelMappingMap.put(7,"原尺码类型");
		excelMappingMap.put(8,"原尺码值");
		excelMappingMap.put(9,"材质*");
		excelMappingMap.put(10,"产地*");
		excelMappingMap.put(11,"市场价");
		excelMappingMap.put(12,"市场价币种");
		boolean flag = true;
		for(int i=0;i<excelMappingMap.size();i++){
			if (xssfRow.getCell(i) != null) {
				String fieldName = xssfRow.getCell(i).toString();
				if(!excelMappingMap.get(i).equals(fieldName)){
					flag = false;
					break;
				}
			}
		}
		return flag;
	}
}
