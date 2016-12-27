package com.shangpin.asynchronous.task.consumer.productimport.hub.service;

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
import com.shangpin.asynchronous.task.consumer.productimport.hub.dto.HubProductImportDTO;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.client.data.mysql.task.gateway.HubSpuImportTaskGateWay;
import com.shangpin.ephub.client.message.task.product.body.ProductImportTask;
import com.shangpin.ephub.client.product.business.hubproduct.dto.HubProductDto;
import com.shangpin.ephub.client.product.business.hubproduct.gateway.HubProductCheckGateWay;
import com.shangpin.ephub.client.product.business.hubproduct.result.HubProductCheckResult;
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
@SuppressWarnings("rawtypes")
@Service
public class HubProductImportService {

	@Autowired
	HubSpuImportTaskGateWay spuImportGateway;
	@Autowired
	HubProductCheckGateWay HubProductCheckGateWay;
	@Autowired
	TaskService taskService;
	@Autowired
	HubSpuGateWay hubSpuGateWay;
	private static String[] hubKeyTemplate = null;
	static {
		hubKeyTemplate = TaskImportTemplate.getPendingSkuTemplate();
	}

	// 1、更新任务表，把task_state更新成正在处理 2、从ftp下载文件并解析成对象 3、公共类校验hub数据并把校验结果写入excel
	// 4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
	public void handMessage(ProductImportTask task) throws Exception {

		// 1、更新任务表，把task_state更新成正在处理
		taskService.updateHubSpuImportStatusByTaskNo(TaskState.HANDLEING.getIndex(), task.getTaskNo(), null);
		// 2、从ftp下载文件并解析成对象
		List<HubProductImportDTO> listHubProduct = handleHubExcel(task.getTaskFtpFilePath(), task.getTaskNo());
		if(listHubProduct==null){
			return;
		}
		// 3、公共类校验hub数据并把校验结果写入excel
		List<Map<String, String>> result = checkAndsaveHubProduct(task.getTaskNo(), listHubProduct);

		SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String resultFileName = sim.format(new Date());
		File file = new File(resultFileName + ".xls");
		FileOutputStream out = new FileOutputStream(file);
		String[] headers = { "任务编号", "货号", "任务状态", "任务说明" };
		String[] columns = { "taskNo", "spuModel", "taskState", "processInfo" };
		ExportExcelUtils.exportExcel(resultFileName, headers, columns, result, out);
		// 4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
		String path = FTPClientUtil.uploadFile(file, resultFileName + ".xls");
		file.delete();
		out.close();
		int status;
		if (result != null && result.size() > 0) {
			// 部分成功
			status = 2;
		} else {
			// 全部成功
			status = 3;
		}
		taskService.updateHubSpuImportByTaskNo(task.getTaskNo(), path + resultFileName + ".xls", status);
	}

	// 校验数据以及保存到hub表
	private List<Map<String, String>> checkAndsaveHubProduct(String taskNo, List<HubProductImportDTO> listHubProduct) {

		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
	
		for (HubProductImportDTO product : listHubProduct) {

			boolean flag = false;
			// TODO hub商品入库前的公共校验方法
			
			HubProductDto hubProductDto = convertHubImportProduct2HupProduct(product);
			HubProductCheckResult hubProductCheckResult = HubProductCheckGateWay.checkProduct(hubProductDto);
			
			
			Map<String, String> map = new HashMap<String, String>();
			if (hubProductCheckResult.isPassing() == true) {
				
				Long hubSpuId = null;
				List<HubSpuDto> hub = findHubSpuDto(product);
				if (hub != null && hub.size() > 0) {
					hubSpuId = hub.get(0).getSpuId();
					flag = true;
				}
				
				map.put("taskNo", taskNo);
				map.put("spuModel", product.getSpuModel());
				map.put("taskState", "校验成功");
				map.put("processInfo", "校验通过");
				HubSpuDto hubSpuDto = convertHubImportProduct2HupSpu(product);
				if (flag) {
					// 已存在，更新
					hubSpuDto.setSpuId(hubSpuId);
					hubSpuGateWay.updateByPrimaryKeySelective(hubSpuDto);
				} else {
					// 不存在，save
					hubSpuGateWay.insert(hubSpuDto);
				}
			} else {
				map.put("taskNo", taskNo);
				map.put("spuModel", product.getSpuModel());
				map.put("taskState", "校验失败");
				map.put("processInfo", hubProductCheckResult.getResult());
			}
			listMap.add(map);
		}
		return listMap;
	}

	private List<HubSpuDto> findHubSpuDto(HubProductImportDTO product) {
		HubSpuCriteriaDto hubSpuCriteriaDto = new HubSpuCriteriaDto();
		hubSpuCriteriaDto.createCriteria().andBrandNoEqualTo(product.getBrandNo())
				.andSpuModelEqualTo(product.getSpuModel());

		return hubSpuGateWay.selectByCriteria(hubSpuCriteriaDto);
	}

	private HubSpuDto convertHubImportProduct2HupSpu(HubProductImportDTO product) {
		HubSpuDto HubSpuDto = new HubSpuDto();
		BeanUtils.copyProperties(product, HubSpuDto);
		return HubSpuDto;
	}

	private HubProductDto convertHubImportProduct2HupProduct(HubProductImportDTO product) {

		HubProductDto hubProductDto = new HubProductDto();
		BeanUtils.copyProperties(product, hubProductDto);
		return hubProductDto;
	}

	// 解析excel转换为对象
	private List<HubProductImportDTO> handleHubExcel(String taskFtpFilePath, String taskNo) throws Exception {

		InputStream in = FTPClientUtil.downFile(taskFtpFilePath);
		if (in == null) {
			taskService.updateHubSpuImportStatusByTaskNo(TaskState.SOME_SUCCESS.getIndex(), taskNo, "从ftp下载失败");
			return null;
		}
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(in);
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		if (xssfSheet.getRow(0) == null) {
			taskService.updateHubSpuImportStatusByTaskNo(TaskState.SOME_SUCCESS.getIndex(), taskNo, "下载的excel数据为空");
			return null;
		}
		boolean flag = checkFileTemplet(xssfSheet.getRow(0));
		if (!flag) {
			// TODO 上传文件与模板不一致
			taskService.updateHubSpuImportStatusByTaskNo(TaskState.SOME_SUCCESS.getIndex(), taskNo, "上传文件与模板不一致");
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
	@SuppressWarnings("all")
	private static HubProductImportDTO convertSpuDTO(XSSFRow xssfRow) {
		HubProductImportDTO item = null;
		if (xssfRow != null) {
			try {
				item = new HubProductImportDTO();
				String [] hubValueTemplate = item.getHubProductTemplate();
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
		for (int i = 0; i < hubKeyTemplate.length; i++) {
			if (xssfRow.getCell(i) != null) {
				String fieldName = xssfRow.getCell(i).toString();
				if (!hubKeyTemplate[i].equals(fieldName)) {
					flag = false;
					break;
				}
			}
		}
		return flag;
	}

}
