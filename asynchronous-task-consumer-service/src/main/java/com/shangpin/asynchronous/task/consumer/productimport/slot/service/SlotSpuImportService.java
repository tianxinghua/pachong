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
	TaskImportService taskService;
	@Autowired
	HubSpuPendingGateWay hubSpuPendingGateWay;

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
				HubSpuPendingDto pendingDto = convertDto(excelDto,createUser);
				//TODO 校验，返回pendingDto
				hubSpuPendingGateWay.updateByPrimaryKeySelective(pendingDto);
				//TODO 调重任接口
				CheckResultDto resultDto = checkPendingSpu(taskNo,pendingDto);
				listMap.add(ReflectBeanUtils.objectToMap(resultDto));
			}
		}
		return taskService.convertExcel(listMap, taskNo);
	}
	
	/**
	 * 做转化
	 * @param excelDto
	 * @param createUser
	 * @return
	 */
	private HubSpuPendingDto convertDto(HubSlotSpuExcelDto excelDto, String createUser){
		HubSpuPendingDto pendingDto =  new HubSpuPendingDto();
		pendingDto.setSpuPendingId(excelDto.getSpuPendingId()); 
		pendingDto.setSupplierId(excelDto.getSupplierId());
		pendingDto.setSupplierNo(excelDto.getSupplierNo());
		pendingDto.setSpuName(excelDto.getSupplierName());
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
