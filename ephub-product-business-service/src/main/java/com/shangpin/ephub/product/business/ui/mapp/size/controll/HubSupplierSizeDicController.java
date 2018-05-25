package com.shangpin.ephub.product.business.ui.mapp.size.controll;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.data.mysql.enumeration.ConstantProperty;
import com.shangpin.ephub.client.data.mysql.enumeration.InfoState;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskType;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.util.DateTimeUtil;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.common.enumeration.SupplierValueMappingType;
import com.shangpin.ephub.product.business.common.hubDic.size.HubSizeDicService;
import com.shangpin.ephub.product.business.common.supplier.sku.HubSupplierSkuService;
import com.shangpin.ephub.product.business.common.supplier.spu.HubSupplierSpuService;
import com.shangpin.ephub.product.business.rest.gms.dto.SupplierDTO;
import com.shangpin.ephub.product.business.rest.gms.service.SupplierService;
import com.shangpin.ephub.product.business.ui.mapp.size.dto.HubSupplierSizeDicRequestDto;
import com.shangpin.ephub.product.business.ui.mapp.size.dto.HubSupplierSizeDicResponseDto;
import com.shangpin.ephub.product.business.ui.mapp.size.dto.HubSupplierSizeDicResponseWithPageDto;
import com.shangpin.ephub.product.business.ui.task.common.service.TaskImportService;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>HubSpuImportTaskController </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月21日 下午5:25:30
 */
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/hub-supplier-size-dic")
@Slf4j
public class HubSupplierSizeDicController {
	@Autowired
	HubSizeDicService hubSizeDicService;
	@Autowired
	HubSupplierSpuService hubSupplierSpuService;
	@Autowired
	HubSupplierSkuService hubSupplierSkuService;
	@Autowired
	SupplierService supplierService;
	@Autowired
	IShangpinRedis shangpinRedis;
	@RequestMapping(value = "/list",method = RequestMethod.POST)
    public HubResponse selectHubSupplierCateoryList(@RequestBody HubSupplierSizeDicRequestDto hubSupplierSizeDicRequestDto){
		try {
			log.info("尺码list接受到数据:{}",hubSupplierSizeDicRequestDto);
			if(hubSupplierSizeDicRequestDto!=null){
				return getCommonSizeMapp(hubSupplierSizeDicRequestDto);
			}
			return HubResponse.errorResp("传值为空");
		} catch (Exception e) {
			log.error("获取尺码映射列表失败：{}",e);
			return HubResponse.errorResp("获取列表失败");
		}
    }
	
	private HubResponse getCommonSizeMapp(HubSupplierSizeDicRequestDto hubSupplierSizeDicRequestDto) {
		
		String supplierNo = hubSupplierSizeDicRequestDto.getSupplierNo();
		if(StringUtils.isNotBlank(supplierNo)){
			if("quanju".equals(supplierNo)){
				hubSupplierSizeDicRequestDto.setSupplierId("quanju");
			}else{
				SupplierDTO supplierDto = supplierService.getSupplier(supplierNo);
				if(supplierDto!=null){
					hubSupplierSizeDicRequestDto.setSupplierId(supplierDto.getSopUserNo());
				}
				
			}
		}
		
		int total = 0;
		total = hubSizeDicService.countHubSupplierValueMapping(hubSupplierSizeDicRequestDto,
				SupplierValueMappingType.TYPE_SIZE.getIndex());
		log.info("查询总个数："+total);
		if (total > 0) {
			List<HubSupplierValueMappingDto> list = hubSizeDicService
					.getHubSupplierValueMappingBySupplierIdAndType(hubSupplierSizeDicRequestDto,
							SupplierValueMappingType.TYPE_SIZE.getIndex());
			if (list != null && list.size() > 0) {

				HubSupplierSizeDicResponseWithPageDto page = new HubSupplierSizeDicResponseWithPageDto();

				List<HubSupplierSizeDicResponseDto> responseList = new ArrayList<HubSupplierSizeDicResponseDto>();
				for (HubSupplierValueMappingDto dicDto : list) {
					HubSupplierSizeDicResponseDto dic = new HubSupplierSizeDicResponseDto();
					BeanUtils.copyProperties(dicDto, dic);
					if(dicDto.getCreateTime()!=null){
						dic.setCreateTime(DateTimeUtil.getTime(dicDto.getCreateTime()));	
					}
					if(dicDto.getUpdateTime()!=null){
						dic.setUpdateTime(DateTimeUtil.getTime(dicDto.getUpdateTime()));	
					}
					responseList.add(dic);
				}
				page.setList(responseList);
				page.setTotal(total);
				return HubResponse.successResp(page);
			}
		}
		return HubResponse.errorResp("列表页为空");
		
	}

	@RequestMapping(value = "/detail/{id}",method = RequestMethod.POST)
    public HubResponse selectHubSupplierCateoryDetail(@PathVariable("id") Long id){
		try {
			if(id!=null){
				HubSupplierValueMappingDto detail = hubSizeDicService.getHubSupplierValueMappingById(id);
				if(detail!=null){
					List<HubSupplierSizeDicResponseDto> responseList = new ArrayList<HubSupplierSizeDicResponseDto>();
					HubSupplierSizeDicResponseDto dic = new HubSupplierSizeDicResponseDto();
					BeanUtils.copyProperties(detail, dic);
					responseList.add(dic);
					return HubResponse.successResp(responseList);
				}else{
					return HubResponse.errorResp("列表页为空");
				}
			}else{
				return HubResponse.errorResp("传值为空");
			}
		} catch (Exception e) {
			log.error("获取尺码映射列表失败：{}",e);
			return HubResponse.errorResp("获取列表失败");
		}
    }
	
	
	/**
	 * 保存更新尺码映射
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/save",method ={RequestMethod.POST,RequestMethod.GET})
    public HubResponse update(@RequestBody HubSupplierSizeDicRequestDto dto){
	        	
		try {
			log.info("保存尺码接受到数据:{}",dto);
			HubSupplierValueMappingDto hubSupplierValueMappingDto = new HubSupplierValueMappingDto();
			hubSupplierValueMappingDto.setHubSupplierValMappingId(dto.getHubSupplierValMappingId());
			hubSupplierValueMappingDto.setHubVal(dto.getHubVal());
			hubSupplierValueMappingDto.setUpdateTime(new Date());
			hubSupplierValueMappingDto.setUpdateUser(dto.getUpdateUser());
			hubSizeDicService.updateHubSupplierValueMappingByPrimaryKey(hubSupplierValueMappingDto);
			sendTask(dto);
			return HubResponse.successResp("success");
		} catch (Exception e) {
			log.error("保存失败：{}",e);
		}
		return HubResponse.errorResp("保存异常");
    }
	
	@Autowired
	TaskImportService taskImportService;
	@RequestMapping(value = "/refresh",method ={RequestMethod.POST,RequestMethod.GET})
    public HubResponse refresh(@RequestBody HubSupplierSizeDicRequestDto dto){
		
		try {
			log.info("更新和刷新尺码接受到数据:{}",dto);
			update(dto);
			sendTask(dto);
			
			return HubResponse.successResp("success");
		} catch (Exception e) {
			log.error("刷新失败：{}",e);
		}
		return HubResponse.errorResp("刷新异常");
    }
	
	private void sendTask(HubSupplierSizeDicRequestDto dto) throws Exception{
		
		if(StringUtils.isBlank(dto.getSupplierVal())){
			return;
		}
		
		Date date = new Date();
		String taskNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date);
		taskImportService.saveTask(taskNo, "尺码映射:"+dto.getSupplierVal()+"=>"+dto.getHubVal(), dto.getUpdateUser(), TaskType.REFRESH_DIC.getIndex());
		dto.setRefreshDicType(InfoState.RefreshSize.getIndex());
		taskImportService.sendTaskMessage(taskNo,TaskType.REFRESH_DIC.getIndex(),JsonUtil.serialize(dto));
		if(StringUtils.isNotBlank(dto.getSupplierId())){
			shangpinRedis.del(ConstantProperty.REDIS_EPHUB_SUPPLIER_SIZE_MAPPING_KEY+"_"+dto.getSupplierId());	
		}else{
			shangpinRedis.del(ConstantProperty.REDIS_EPHUB_SUPPLIER_COMMON_SIZE_MAPPING_KEY);	
		}
	}

	@RequestMapping(value = "/insert",method ={RequestMethod.POST,RequestMethod.GET})
    public HubResponse insert(@RequestBody HubSupplierSizeDicRequestDto dto){
		
		try {
			boolean flag = false;
			log.info("保存新尺码接受到数据:{}",dto);
			if(StringUtils.isBlank(dto.getSupplierVal())){
				return HubResponse.errorResp("供应商尺码为空");
			}
			HubSupplierValueMappingDto hubSupplierValueMappingDto = new HubSupplierValueMappingDto();
			hubSupplierValueMappingDto.setSupplierVal(dto.getSupplierVal());
			String supplierNo = dto.getSupplierNo();
			if(StringUtils.isNotBlank(supplierNo)){
				if("quanju".equals(supplierNo)){
					hubSupplierValueMappingDto.setSupplierId("quanju");	
				}else{
					SupplierDTO supplierDto = supplierService.getSupplier(supplierNo);
					hubSupplierValueMappingDto.setSupplierId(supplierDto.getSopUserNo());
					dto.setSupplierId(supplierDto.getSopUserNo());
				}
			}
			
			List<HubSupplierValueMappingDto> tempList = hubSizeDicService.getHubSupplierValueMappingBySupplierIdAndSize(hubSupplierValueMappingDto.getSupplierId(),dto.getSupplierVal());
			if(tempList!=null&&tempList.size()>0){
				log.info("该尺码已存在不添加："+dto.getSupplierVal());
				return HubResponse.successResp(null);
			}
			
			hubSupplierValueMappingDto.setHubValType((byte)4);
			hubSupplierValueMappingDto.setCreateTime(new Date());
			hubSupplierValueMappingDto.setUpdateTime(new Date());
			hubSupplierValueMappingDto.setCreateUser(dto.getUpdateUser());
			hubSupplierValueMappingDto.setUpdateUser(dto.getUpdateUser());
			if(dto.getHubVal()!=null){
				hubSupplierValueMappingDto.setHubVal(dto.getHubVal().trim());
				hubSupplierValueMappingDto.setMappingType((byte)1);
				flag = true;
			}else{
				hubSupplierValueMappingDto.setMappingType((byte)0);
			}
			hubSizeDicService.insertHubSupplierValueMapping(hubSupplierValueMappingDto);
			if(flag){
				sendTask(dto);
			}
			return HubResponse.successResp(null);
		} catch (Exception e) {
			log.error("保存失败：{}",e);
		}
		return HubResponse.errorResp("刷新异常");
    }
	
	
	@RequestMapping(value = "/delete/{id}",method ={RequestMethod.POST,RequestMethod.GET})
    public HubResponse delete(@PathVariable("id") Long id){
		
		try {
			log.info("删除尺码接受到数据:{}",id);
			hubSizeDicService.deleteHubSupplierValueMapping(id);
			return HubResponse.successResp(null);
		} catch (Exception e) {
			log.error("保存失败：{}",e);
		}
		return HubResponse.errorResp("刷新异常");
    }
	
}