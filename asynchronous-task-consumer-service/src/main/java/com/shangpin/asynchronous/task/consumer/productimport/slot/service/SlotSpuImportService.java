package com.shangpin.asynchronous.task.consumer.productimport.slot.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.shangpin.asynchronous.task.consumer.productimport.common.dto.CheckResultDto;
import com.shangpin.asynchronous.task.consumer.productimport.common.enumeration.TaskState;
import com.shangpin.asynchronous.task.consumer.productimport.common.service.TaskImportService;
import com.shangpin.asynchronous.task.consumer.productimport.slot.dto.HubSlotSpuExcelDto;
import com.shangpin.asynchronous.task.consumer.util.ReflectBeanUtils;
import com.shangpin.asynchronous.task.consumer.util.excel.ReadExcel;
import com.shangpin.ephub.client.data.mysql.enumeration.CatgoryState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuBrandState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuModelState;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.message.task.product.body.Task;
import com.shangpin.ephub.client.product.business.hubpending.spu.dto.HubSpuPendingCheckProperty;
import com.shangpin.ephub.client.product.business.hubpending.spu.gateway.HubPendingSpuCheckGateWay;
import com.shangpin.ephub.client.product.business.studio.gateway.HubSlotSpuTaskGateWay;
import com.shangpin.ephub.client.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title: ImportService</p>
 * <p>Description: 导入服务 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月29日 下午3:03:40
 *
 */
@Service
@Slf4j
public class SlotSpuImportService {
	
	@Autowired
	private TaskImportService taskService;
	@Autowired
	private HubSpuPendingGateWay hubSpuPendingGateWay;
	@Autowired
	private HubSlotSpuTaskGateWay hubSlotSpuTaskGateWay;
	@Autowired
	private HubPendingSpuCheckGateWay hubPendingSpuCheckGateWay;

	public String handMessage(Task task) throws Exception {
		String taskNo = task.getTaskNo();
		JSONObject json = JSONObject.parseObject(task.getData());
		String filePath = json.get("taskFtpFilePath").toString();
		String createUser = json.get("createUser").toString();
		task.setData(filePath);		
		InputStream input = taskService.downFileFromFtp(task);
		
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		List<HubSlotSpuExcelDto> excelDtos = ReadExcel.readExcel(HubSlotSpuExcelDto.class, input, filePath);
		if(CollectionUtils.isNotEmpty(excelDtos)){
			for(HubSlotSpuExcelDto excelDto : excelDtos){
				HubSpuPendingCheckProperty property = myCheckProperty();
				property.setDto(convertDto(excelDto,createUser));
				HubSpuPendingDto pendingDto = hubPendingSpuCheckGateWay.checkSpuProperty(property);
				log.info("校验结果========="+JsonUtil.serialize(pendingDto)); 
				hubSpuPendingGateWay.updateByPrimaryKeySelective(pendingDto);
				CheckResultDto resultDto = checkPendingSpu(taskNo,pendingDto);
				if(TaskState.SUCCESS.equals(resultDto.getTaskState())){
					hubSlotSpuTaskGateWay.add(pendingDto);
				}else{
					log.info(pendingDto.getSpuModel()+" 校验不通过，不调接口"); 
				}
				listMap.add(ReflectBeanUtils.objectToMap(resultDto));
			}
		}
		return taskService.convertExcel(listMap, taskNo);
	}

	private HubSpuPendingCheckProperty myCheckProperty() {
		HubSpuPendingCheckProperty property = new HubSpuPendingCheckProperty();
		property.setHubBrand(true);
		property.setHubCategory(true);
		property.setHubSpuModel(true);
		return property;
	}
	
	
	/**
	 * 做转化
	 * @param excelDto
	 * @param createUser
	 * @return
	 */
	private HubSpuPendingDto convertDto(HubSlotSpuExcelDto excelDto, String createUser){
		HubSpuPendingDto pendingDto =  new HubSpuPendingDto();
		pendingDto.setSupplierSpuId(Long.valueOf(excelDto.getSupplierSpuId()));  
		pendingDto.setSpuPendingId(Long.valueOf(excelDto.getSpuPendingId())); 
		pendingDto.setSupplierId(excelDto.getSupplierId());
		pendingDto.setSupplierNo(excelDto.getSupplierNo());
		pendingDto.setHubCategoryNo(excelDto.getHubCategoryNo());
		pendingDto.setHubBrandNo(excelDto.getHubBrandNo());
		pendingDto.setSpuModel(excelDto.getSpuModel()); 
		pendingDto.setUpdateUser("待拍照导入-"+createUser); 
		return pendingDto;
	}
	/**
	 * 根据校验结果生成结果文件
	 * @param taskNo
	 * @param pendingDdto
	 * @return
	 */
	private CheckResultDto checkPendingSpu(String taskNo, HubSpuPendingDto pendingDdto){
		CheckResultDto resultDto = new CheckResultDto();
		resultDto.setTaskNo(taskNo);
		resultDto.setSpuModel(pendingDdto.getSpuModel());
		StringBuffer buffer = new StringBuffer();
		boolean isPass = true;
		if(StringUtils.isEmpty(pendingDdto.getSpuModelState()) || SpuModelState.VERIFY_FAILED.getIndex() == pendingDdto.getSpuModelState()){
			isPass = false;
			buffer.append(TaskState.MODEL_FAIL).append(TaskState.SIGN);
		}
		if(StringUtils.isEmpty(pendingDdto.getCatgoryState()) || CatgoryState.ENTIRELY_MISMATCHING.getIndex() == pendingDdto.getCatgoryState()){
			isPass = false;
			buffer.append(TaskState.CATGORY_FAIL).append(TaskState.SIGN);
		}
		if(StringUtils.isEmpty(pendingDdto.getSpuBrandState()) || SpuBrandState.UNHANDLED.getIndex() == pendingDdto.getSpuBrandState()){
			isPass = false;
			buffer.append(TaskState.BRAND_FAIL).append(TaskState.SIGN);
		}
		if(isPass){
			resultDto.setTaskState(TaskState.SUCCESS);
		}else{
			resultDto.setTaskState(TaskState.FAIL);
			resultDto.setProcessInfo(buffer.toString()); 
		}
		return resultDto;
	}
	

}
