package com.shangpin.asynchronous.task.consumer.productimport.hub.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.asynchronous.task.consumer.productimport.common.service.TaskImportService;
import com.shangpin.asynchronous.task.consumer.productimport.hub.dto.HubProductImportDTO;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
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
	TaskImportService taskService;
	@Autowired
	HubSpuGateWay hubSpuGateWay;
	@Autowired
	HubSkuGateWay hubSkuGateWay;
	private static String[] hubKeyTemplate = null;
	static {
		hubKeyTemplate = TaskImportTemplate.getPendingSkuTemplate();
	}

	// 1、更新任务表，把task_state更新成正在处理 2、从ftp下载文件并解析成对象 3、公共类校验hub数据并把校验结果写入excel
	// 4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
	public void handMessage(ProductImportTask task) throws Exception {

		// 1、更新任务表，把task_state更新成正在处理
		taskService.updateHubSpuImportStatusByTaskNo(TaskState.HANDLEING.getIndex(), task.getTaskNo(), null);
		
		// 2、从ftp下载文件并校验模板，并校验
		XSSFSheet xssfSheet = taskService.checkExcel(task.getTaskFtpFilePath(),task.getTaskNo(),"hub");
		
		List<HubProductImportDTO> listHubProduct = excelToObject(xssfSheet);
		
		// 3、公共类校验hub数据并把校验结果写入excel
		checkAndsaveHubProduct(task.getTaskNo(), listHubProduct);
		
	}

	// 校验数据以及保存到hub表
	private void checkAndsaveHubProduct(String taskNo, List<HubProductImportDTO> listHubProduct) throws Exception{

		if(listHubProduct==null){
			return;
		}
		//true全部校验成功，
		boolean flag = false;
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
	
		for (HubProductImportDTO product : listHubProduct) {

//			boolean isExist = false;
			Map<String, String> map = new HashMap<String, String>();
			HubProductDto hubProductDto = convertHubImportProduct2HupProduct(product);
			//校验hub
			HubProductCheckResult hubProductCheckResult = HubProductCheckGateWay.checkProduct(hubProductDto);
			if (hubProductCheckResult.isPassing() == true) {
				
				HubSpuDto hubSpuDto = convertHubImportProduct2HupSpu(product);
				//查询hubSpu是否存在
				String hubSpuNo = null;
				List<HubSpuDto> hub = findHubSpuDto(hubSpuDto);
				if (hub != null && hub.size() > 0) {
					hubSpuNo = hub.get(0).getSpuNo();
					hubSpuDto.setSpuId(hub.get(0).getSpuId());
					hubSpuGateWay.updateByPrimaryKeySelective(hubSpuDto);
				}else {
					
					hubSpuDto.setCreateTime(new Date());
					hubSpuDto.setDataState((byte)1);
					hubSpuDto.setSpuSelectState((byte)0);
					//调用接口生成spuNo
					hubSpuNo = hubSpuGateWay.getSpuNo();
					hubSpuDto.setSpuNo(hubSpuNo);
					hubSpuGateWay.insert(hubSpuDto);
				}
				
				List<HubSkuDto> hubSkuList = findHubSkuDto(hubSpuNo,product.getSkuSize());
				if(hubSkuList!=null&&hubSkuList.size()>0){
					HubSkuWithCriteriaDto HubSkuWithCriteriaDto = new HubSkuWithCriteriaDto();
					HubSkuCriteriaDto HubSkuCriteriaDto = new HubSkuCriteriaDto();
					HubSkuCriteriaDto.createCriteria().andSpuNoEqualTo(hubSpuNo);
					HubSkuDto hubSku = new HubSkuDto();
					hubSku.setSkuSize(product.getSkuSize());
					hubSku.setUpdateTime(new Date());
					HubSkuWithCriteriaDto.setHubSku(hubSku);
					HubSkuWithCriteriaDto.setCriteria(HubSkuCriteriaDto);
					hubSkuGateWay.updateByCriteriaSelective(HubSkuWithCriteriaDto);
				}else{
					HubSkuDto hubSku = new HubSkuDto();
					hubSku.setSkuSize(product.getSkuSize());
					hubSku.setSpuNo(hubSpuNo);
					hubSku.setCreateTime(new Date());
					hubSku.setDataState((byte)1);
					//生成skuNo调用接口
					String skuNo = hubSkuGateWay.createSkuNo(hubSpuNo);
					hubSku.setSkuNo(skuNo);
					hubSkuGateWay.insert(hubSku);
				}
				
				map.put("taskNo", taskNo);
				map.put("spuModel", product.getSpuModel());
				map.put("taskState", "校验成功");
				map.put("processInfo", "校验通过");
				
			} else {
				map.put("taskNo", taskNo);
				map.put("spuModel", product.getSpuModel());
				map.put("taskState", "校验失败");
				map.put("processInfo", hubProductCheckResult.getResult());
				flag = false;
			}
			listMap.add(map);
		}
		taskService.convertExcel(listMap,taskNo,flag);
	}

	private List<HubSkuDto> findHubSkuDto(String hubSpuNo, String skuSize) {
		HubSkuCriteriaDto criteria = new HubSkuCriteriaDto();
		criteria.createCriteria().andSpuNoEqualTo(hubSpuNo).andSkuSizeEqualTo(skuSize);
		return hubSkuGateWay.selectByCriteria(criteria);
	}

	private List<HubSpuDto> findHubSpuDto(HubSpuDto product) {
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
	private List<HubProductImportDTO> excelToObject(XSSFSheet xssfSheet) throws Exception {

		if(xssfSheet==null){
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
