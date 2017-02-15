package com.shangpin.asynchronous.task.consumer.productimport.hub.service;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.esotericsoftware.minlog.Log;
import com.shangpin.asynchronous.task.consumer.productimport.common.service.TaskImportService;
import com.shangpin.asynchronous.task.consumer.productimport.hub.dto.HubProductImportDTO;
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
import com.shangpin.ephub.client.product.business.model.gateway.HubBrandModelRuleGateWay;
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
	@Autowired
	HubBrandModelRuleGateWay hubBrandModelRuleGateWay;

	private static String[] hubKeyTemplate = null;
	static {
		hubKeyTemplate = HubProductDto.getHubProductTemplate();
	}
	private static String[] hubValueTemplate = null;
	static {
		hubValueTemplate = HubProductDto.getHubProductValueTemplate();
	}
	// 1、更新任务表，把task_state更新成正在处理 2、从ftp下载文件并解析成对象 3、公共类校验hub数据并把校验结果写入excel
	// 4、处理结果的excel上传ftp，更新任务表状态和文件在ftp的路径
	public String handMessage(ProductImportTask task) throws Exception {

		JSONObject json = JSONObject.parseObject(task.getData());
		String filePath = json.get("taskFtpFilePath").toString();
		task.setData(filePath);
		
		InputStream in = taskService.downFileFromFtp(task);
		
		// 2、从ftp下载文件并校验模板，并校验
		XSSFSheet xssfSheet = taskService.checkXlsxExcel(in, task, "hub");
		List<HubProductDto> listHubProduct = excelToObject(xssfSheet);
		// 3、公共类校验hub数据并把校验结果写入excel
		return checkAndsaveHubProduct(task.getTaskNo(), listHubProduct);
	}

	// 校验数据以及保存到hub表
	private String checkAndsaveHubProduct(String taskNo, List<HubProductDto> listHubProduct) throws Exception {

		if (listHubProduct == null) {
			return null;
		}
		// true全部校验成功，
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		for (HubProductDto hubProductDto : listHubProduct) {
			if(hubProductDto==null||StringUtils.isBlank(hubProductDto.getCategoryNo())){
				continue;
			}
			map = new HashMap<String, String>();
			// 校验hub
			map.put("spuModel", hubProductDto.getSpuModel());
			log.info("hub检验参数：{}",hubProductDto);
			HubProductCheckResult hubProductCheckResult = HubProductCheckGateWay.checkProduct(hubProductDto);
			log.info("hub校验返回结果：{}",hubProductCheckResult);
			if (hubProductCheckResult.isPassing() == true) {
				//sizeId,sizeType:sizeValue;spuModel
				String sizeArr = null;
				String spuModel = hubProductCheckResult.getSpuModel();
				String size = null;
				String sizeId = null;
				sizeArr = hubProductCheckResult.getSize();
				if(StringUtils.isNotBlank(sizeArr)&&sizeArr.split(";").length>1){
					size = sizeArr.split(",")[1];
					sizeId = sizeArr.split(",")[0];
				}else{
					size = sizeArr;
				}
				if(!spuModel.equals(hubProductDto.getSpuModel())){
					map.put("spuNewModel", spuModel);	
				}
				hubProductDto.setSpuModel(spuModel);
				HubSpuDto hubSpuDto = convertHubImportProduct2HupSpu(hubProductDto);
				// 查询hubSpu是否存在
				String hubSpuNo = null;
				List<HubSpuDto> hub = findHubSpuDto(hubSpuDto);
				if (hub != null && hub.size() > 0) {
					hubSpuNo = hub.get(0).getSpuNo();
					hubSpuDto.setSpuId(hub.get(0).getSpuId());
					hubSpuDto.setUpdateTime(new Date());
					hubSpuGateWay.updateByPrimaryKeySelective(hubSpuDto);
				} else {
					hubSpuDto.setCreateTime(new Date());
					hubSpuDto.setUpdateTime(new Date());
					hubSpuDto.setDataState((byte) 1);
					hubSpuDto.setSpuSelectState((byte) 0);
					// 调用接口生成spuNo
					hubSpuNo = hubSpuGateWay.getSpuNo();
					hubSpuDto.setSpuNo(hubSpuNo);
					hubSpuDto.setInfoFrom((byte)1);
					Log.info(spuModel+"生成hubSpu:"+hubSpuNo);
					hubSpuGateWay.insert(hubSpuDto);
				}
				
				List<HubSkuDto> hubSkuList = findHubSkuDto(hubSpuNo,size);
				if (hubSkuList != null && hubSkuList.size() > 0) {
					HubSkuWithCriteriaDto HubSkuWithCriteriaDto = new HubSkuWithCriteriaDto();
					HubSkuCriteriaDto HubSkuCriteriaDto = new HubSkuCriteriaDto();
					if(size!=null&&"尺码".equals(hubProductDto.getSpecificationType())){
						HubSkuCriteriaDto.createCriteria().andSpuNoEqualTo(hubSpuNo).andSkuSizeEqualTo(size);	
					}else if("尺码".equals(hubProductDto.getSpecificationType())&&size==null){
						map.put("processInfo", "hub商品已存在,");
						continue;
					}else{
						HubSkuCriteriaDto.createCriteria().andSpuNoEqualTo(hubSpuNo);
					}
					
					HubSkuDto hubSku = new HubSkuDto();
					hubSku.setUpdateTime(new Date());
					HubSkuWithCriteriaDto.setHubSku(hubSku);
					HubSkuWithCriteriaDto.setCriteria(HubSkuCriteriaDto);
					hubSkuGateWay.updateByCriteriaSelective(HubSkuWithCriteriaDto);
				} else {
					HubSkuDto hubSku = new HubSkuDto();
					hubSku.setSkuSize(size);
					hubSku.setSpuNo(hubSpuNo);
					hubSku.setCreateTime(new Date());
					hubSku.setUpdateTime(new Date());
					hubSku.setDataState((byte) 1);
					hubSku.setSkuSizeId(sizeId);
					// 生成skuNo调用接口
					String skuNo = hubSkuGateWay.createSkuNo(hubSpuNo);
					Log.info(hubProductDto.getSpuModel()+"生成hubSku:"+skuNo);
					hubSku.setSkuNo(skuNo);
					hubSkuGateWay.insert(hubSku);
				}

				map.put("taskState", "校验成功");
				map.put("processInfo", "校验通过");

			} else {
				Log.info("hub校验失败");
				map.put("taskState", "校验失败");
				map.put("processInfo", hubProductCheckResult.getResult());
			}
			map.put("taskNo", taskNo);
			map.put("spuModel", hubProductDto.getSpuModel());
			listMap.add(map);
		}
		return taskService.convertExcel(listMap, taskNo);
	}

	private List<HubSkuDto> findHubSkuDto(String hubSpuNo, String skuSize) {
		HubSkuCriteriaDto criteria = new HubSkuCriteriaDto();
		if(skuSize!=null){
			criteria.createCriteria().andSpuNoEqualTo(hubSpuNo).andSkuSizeEqualTo(skuSize);	
		}else{
			criteria.createCriteria().andSpuNoEqualTo(hubSpuNo);
		}
		
		return hubSkuGateWay.selectByCriteria(criteria);
	}

	private List<HubSpuDto> findHubSpuDto(HubSpuDto product) {
		HubSpuCriteriaDto hubSpuCriteriaDto = new HubSpuCriteriaDto();
		hubSpuCriteriaDto.createCriteria().andBrandNoEqualTo(product.getBrandNo())
				.andSpuModelEqualTo(product.getSpuModel());

		return hubSpuGateWay.selectByCriteria(hubSpuCriteriaDto);
	}

	private HubSpuDto convertHubImportProduct2HupSpu(HubProductDto product) {
		HubSpuDto HubSpuDto = new HubSpuDto();
		BeanUtils.copyProperties(product, HubSpuDto);
		return HubSpuDto;
	}


	// 解析excel转换为对象
	private List<HubProductDto> excelToObject(XSSFSheet xssfSheet) throws Exception {

		if (xssfSheet == null) {
			return null;
		}
		List<HubProductDto> listHubProduct = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			HubProductDto product = convertSpuDTO(xssfRow);
			if(product!=null){
				listHubProduct.add(product);	
			}
		}
		return listHubProduct;

	}

	@SuppressWarnings("all")
	private static HubProductDto convertSpuDTO(XSSFRow xssfRow) {
		HubProductDto item = null;
		if (xssfRow != null) {
			try {
				item = new HubProductDto();
//				String[] hubValueTemplate = item.getHubProductValueTemplate();
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
}
