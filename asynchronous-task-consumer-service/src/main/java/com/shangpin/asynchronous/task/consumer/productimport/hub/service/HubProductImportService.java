package com.shangpin.asynchronous.task.consumer.productimport.hub.service;

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

import com.shangpin.asynchronous.task.consumer.productimport.hub.dto.HubProductImportDTO;
import com.shangpin.asynchronous.task.consumer.productimport.hub.util.ExportExcelUtils;
import com.shangpin.asynchronous.task.consumer.productimport.hub.util.FTPClientUtil;
import com.shangpin.ephub.client.data.mysql.config.dto.HubSpuImportTaskCriteriaDto;
import com.shangpin.ephub.client.data.mysql.config.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.client.data.mysql.config.dto.HubSpuImportTaskWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.config.gateway.HubSpuImportTaskGateWay;
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
public class HubProductImportService {
	
	@Autowired
	HubSpuImportTaskGateWay spuImportGateway;
	
	//1、更新任务表，把task_state更新成正在处理  2、从ftp下载文件并解析成对象   3、公共类校验hub数据并把校验结果写入excel   4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
	public void handMessage(ProductImportTask task) throws Exception{
		
		//1、更新任务表，把task_state更新成正在处理
		updateHubSpuImportStatusByTaskNo(task.getTaskNo());
		// 2、从ftp下载文件并解析成对象
		List<HubProductImportDTO> listHubProduct = handleHubExcel(task.getTaskFtpFilePath());
		//3、公共类校验hub数据并把校验结果写入excel
		 List<Map<String, String>> result = checkAndsaveHubProduct(task.getTaskNo(),listHubProduct);
		
		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String resultFileName = sim.format(new Date());
		FileOutputStream out = new FileOutputStream(new File(resultFileName+".xlsx"));
		
		String[] headers = { "任务编号", "货号", "任务状态", "任务说明"};
		String[] columns = { "taskNo", "spuModel","taskState", "processInfo"};
		ExportExcelUtils.exportExcel(resultFileName, headers, columns, result,
				out);
		
		//4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
		String path = FTPClientUtil.uploadFile(new File(resultFileName+".xlsx"), resultFileName+".xlsx");
		
		out.close();
		int status;
		if(result!=null&&result.size()>0){
			//部分成功
			status = 2;
		}else{
			//全部成功
			status = 3;
		}
		updateHubSpuImportByTaskNo(task.getTaskNo(),path+resultFileName+".xlsx",status);
	}


	//校验数据以及保存到hub表
	private List<Map<String, String>> checkAndsaveHubProduct(String taskNo,List<HubProductImportDTO> listHubProduct) {

		List<Map<String, String>> listMap = new ArrayList<Map<String,String>>();
		for(HubProductImportDTO product:listHubProduct){
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
	private List<HubProductImportDTO> handleHubExcel(String taskFtpFilePath)  throws Exception {
		
		InputStream in = FTPClientUtil.downFile(taskFtpFilePath);
		if(in==null){
			
		}
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(in);
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		if (xssfSheet.getRow(0) == null) {
			return null;
		}
		boolean flag = checkFileTemplet(xssfSheet.getRow(0));
		if(!flag){
			//TODO 上传文件与模板不一致
			return null;
		}
		List<HubProductImportDTO> listHubProduct = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			HubProductImportDTO product = convertSpuDTO(xssfRow);
			listHubProduct.add(product);
		}
		return listHubProduct;
	}
	private static HubProductImportDTO convertSpuDTO(XSSFRow xssfRow) {
		HubProductImportDTO item = null;
		if (xssfRow != null) {
			try {
				item = new HubProductImportDTO();
				if (xssfRow.getCell(0) != null) {
					xssfRow.getCell(0).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setCategoryName(xssfRow.getCell(0).toString());
				}
				if (xssfRow.getCell(1) != null) {
					xssfRow.getCell(1).setCellType(
							Cell.CELL_TYPE_STRING);
					xssfRow.getCell(2).setCellType(
							Cell.CELL_TYPE_STRING);
					String categoryNo = xssfRow.getCell(1).toString();
					String brandNo = xssfRow.getCell(2).toString();
					item.setCategoryNo(categoryNo);
					item.setBrandNo(brandNo);
				}
				if (xssfRow.getCell(3) != null) {
					xssfRow.getCell(3).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setBrandName(xssfRow.getCell(3).toString());
				}
				if (xssfRow.getCell(4) != null) {
					xssfRow.getCell(4).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setProductCode(xssfRow.getCell(4).toString());
				}
				if (xssfRow.getCell(5) != null) {
					xssfRow.getCell(5).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setGender(xssfRow.getCell(5).toString());
				}
				if (xssfRow.getCell(6) != null) {
					xssfRow.getCell(6).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setColor(xssfRow.getCell(6).toString());
				}
				if (xssfRow.getCell(7) != null) {
					xssfRow.getCell(7).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setSizeType(xssfRow.getCell(7).toString());
				}
				if (xssfRow.getCell(8) != null) {
					xssfRow.getCell(8).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setSize(xssfRow.getCell(8).toString());
				}
				if (xssfRow.getCell(9) != null) {
					xssfRow.getCell(9).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setMaterial(xssfRow.getCell(9).toString());
				}
				if (xssfRow.getCell(10) != null) {
					xssfRow.getCell(10).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setMadeIn(xssfRow.getCell(10).toString());
				}
				if (xssfRow.getCell(11) != null) {
					xssfRow.getCell(11).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setMarketPrice(xssfRow.getCell(11).toString());
				}
				if (xssfRow.getCell(12) != null) {
					xssfRow.getCell(12).setCellType(
							Cell.CELL_TYPE_STRING);
					item.setMarketCurrency(xssfRow.getCell(12).toString());
				}
			} catch (Exception ex) {
				ex.getStackTrace();
			}
		}
		return item;
	}
private static boolean checkFileTemplet(XSSFRow xssfRow) {
		
		Map<Integer,String> map = new HashMap<Integer,String>();
		map.put(0,"品类名称");
		map.put(1,"品类编号*");
		map.put(2,"品牌编号*");
		map.put(3,"品牌名称");
		map.put(4,"货号*");
		map.put(5,"适应性别*");
		map.put(6,"颜色*");
		map.put(7,"原尺码类型");
		map.put(8,"原尺码值");
		map.put(9,"材质*");
		map.put(10,"产地*");
		map.put(11,"市场价");
		map.put(12,"市场价币种");
		boolean flag = true;
		for(int i=0;i<map.size();i++){
			if (xssfRow.getCell(i) != null) {
				String fieldName = xssfRow.getCell(i).toString();
				if(!map.get(i).equals(fieldName)){
					flag = false;
					break;
				}
			}
		}
		return flag;
	}
	private boolean updateHubSpuImportStatusByTaskNo(String taskNo) {
		// TODO Auto-generated method stub
		HubSpuImportTaskWithCriteriaDto dto = new HubSpuImportTaskWithCriteriaDto();
		
		HubSpuImportTaskDto hubSpuImportTaskDto = new HubSpuImportTaskDto();
		//1代表正在处理
		hubSpuImportTaskDto.setTaskState((byte)1);
		dto.setHubSpuImportTask(hubSpuImportTaskDto);
		HubSpuImportTaskCriteriaDto hubSpuImportTaskCriteriaDto = new HubSpuImportTaskCriteriaDto();
		HubSpuImportTaskCriteriaDto.Criteria criteria = hubSpuImportTaskCriteriaDto.createCriteria().andTaskNoEqualTo(taskNo);
		dto.setCriteria(hubSpuImportTaskCriteriaDto);
		int result = spuImportGateway.updateByCriteriaSelective(dto);
		return true;
	}
	private void updateHubSpuImportByTaskNo(String taskNo, String resultFilePath, int status) {
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
}
