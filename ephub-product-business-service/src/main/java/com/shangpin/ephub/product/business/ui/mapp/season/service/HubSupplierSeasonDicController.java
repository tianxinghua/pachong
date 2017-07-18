package com.shangpin.ephub.product.business.ui.mapp.season.service;

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
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.util.DateTimeUtil;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.common.hubDic.season.HubSeasonDicService;
import com.shangpin.ephub.product.business.common.mapp.hubSupplierValueMapping.HubSupplierValueMappingService;
import com.shangpin.ephub.product.business.rest.gms.dto.SupplierDTO;
import com.shangpin.ephub.product.business.rest.gms.service.SupplierService;
import com.shangpin.ephub.product.business.ui.mapp.season.dto.HubSupplierSeasonDicRequestDto;
import com.shangpin.ephub.product.business.ui.mapp.season.dto.HubSupplierSeasonDicResponseDto;
import com.shangpin.ephub.product.business.ui.mapp.season.dto.HubSupplierSeasonDicResponseWithPageDto;
import com.shangpin.ephub.product.business.ui.task.common.service.TaskImportService;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * HubSpuImportTaskController
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: www.shangpin.com
 * </p>
 * @author zhaogenchun
 * @date 2016年12月21日 下午5:25:30
 */
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/hub-supplier-season-dic")
@Slf4j
public class HubSupplierSeasonDicController {
	@Autowired
	HubSeasonDicService hubSeasonDicService;
	@Autowired
	SupplierService supplierService;
	@Autowired
	TaskImportService taskImportService;
	@Autowired
	IShangpinRedis shangpinRedis;
	@Autowired
	HubSupplierValueMappingService hubSupplierValueMappingService;
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public HubResponse selectHubSupplierSeasonList(
			@RequestBody HubSupplierSeasonDicRequestDto hubSupplierSeasonDicRequestDto) {
		
		try {
			log.info("===季节映射list请求参数：{}",hubSupplierSeasonDicRequestDto);
			String supplierNo = hubSupplierSeasonDicRequestDto.getSupplierNo();
			String supplierId = null;
			if(StringUtils.isNotBlank(supplierNo)){
				SupplierDTO supplierDto = supplierService.getSupplier(supplierNo);
				if(supplierDto==null){
					return HubResponse.errorResp("供应商为空");
				}	
				supplierId = supplierDto.getSopUserNo();
				hubSupplierSeasonDicRequestDto.setSupplierId(supplierId);
			}
			return getHubSeasonDic(hubSupplierSeasonDicRequestDto);
		} catch (Exception e) {
			log.error("获取列表失败：{}", e);
			return HubResponse.errorResp("获取列表失败");
		}
	}

	private HubResponse getHubSeasonDic(HubSupplierSeasonDicRequestDto hubSupplierSeasonDicRequestDto) {
		
		int total = hubSeasonDicService.countHubSeason(hubSupplierSeasonDicRequestDto.getSupplierId(),hubSupplierSeasonDicRequestDto.getSupplierSeason(),hubSupplierSeasonDicRequestDto.getHubMarketTime(),hubSupplierSeasonDicRequestDto.getHubSeason(),hubSupplierSeasonDicRequestDto.getType());
		
		log.info("返回个数："+total);
		if(total>0){
			List<HubSeasonDicDto> list = hubSeasonDicService.getHubSeason(hubSupplierSeasonDicRequestDto.getSupplierId(),hubSupplierSeasonDicRequestDto.getSupplierSeason(),hubSupplierSeasonDicRequestDto.getHubMarketTime(),hubSupplierSeasonDicRequestDto.getHubSeason(),hubSupplierSeasonDicRequestDto.getType(),hubSupplierSeasonDicRequestDto.getPageNo(), hubSupplierSeasonDicRequestDto.getPageSize());
			if (list != null && list.size() > 0) {
				List<HubSupplierSeasonDicResponseDto> responseList = new ArrayList<HubSupplierSeasonDicResponseDto>();
				for (HubSeasonDicDto dicDto : list) {
					HubSupplierSeasonDicResponseDto dic = new HubSupplierSeasonDicResponseDto();
					dic.setSupplierId(dicDto.getSupplierid());
					BeanUtils.copyProperties(dicDto, dic);
					if(dicDto.getCreateTime()!=null){
						dic.setCreateTime(DateTimeUtil.getTime(dicDto.getCreateTime()));						
					}
					if(dicDto.getUpdateTime()!=null){
						dic.setUpdateTime(DateTimeUtil.getTime(dicDto.getUpdateTime()));	
					}
					if(dicDto.getSupplierid()!=null){
						List<HubSupplierValueMappingDto> listMapp = hubSupplierValueMappingService.getHubSupplierValueMappingByTypeAndSupplierId((byte)5,dicDto.getSupplierid());
						if(listMapp!=null&&listMapp.size()>0){
							dic.setSupplierNo(listMapp.get(0).getHubValNo());
						}	
					}
					dic.setSupplierId(dicDto.getSupplierid());
					responseList.add(dic);
				}
				HubSupplierSeasonDicResponseWithPageDto response = new HubSupplierSeasonDicResponseWithPageDto();
				response.setTotal(total);
				response.setList(responseList);
				return HubResponse.successResp(response);
			}
		}
		return HubResponse.errorResp("列表为空");
	}

	@RequestMapping(value = "/updateAndRefresh", method = { RequestMethod.POST, RequestMethod.GET })
	public HubResponse update(@RequestBody HubSupplierSeasonDicRequestDto hubSupplierSeasonDicRequestDto) {
		try {
			log.info("修改参数：{}",hubSupplierSeasonDicRequestDto);
			HubSeasonDicDto dicDto = new HubSeasonDicDto();
			BeanUtils.copyProperties(hubSupplierSeasonDicRequestDto, dicDto);
			dicDto.setUpdateTime(new Date());
			dicDto.setPushState((byte)1);
			dicDto.setMemo(String.valueOf(hubSupplierSeasonDicRequestDto.getFilterFlag()));
			dicDto.setFilterFlag(hubSupplierSeasonDicRequestDto.getFilterFlag());
			hubSeasonDicService.updateHubSeasonDicById(dicDto);
			
			if(hubSupplierSeasonDicRequestDto.getFilterFlag()!=0||hubSupplierSeasonDicRequestDto.getFilterFlag()!=1){
				
				List<HubSeasonDicDto> dic = hubSeasonDicService.getHubSeasonDicById(hubSupplierSeasonDicRequestDto.getSeasonDicId());
				if(dic!=null&&dic.size()>0){
					hubSupplierSeasonDicRequestDto.setSupplierId(dic.get(0).getSupplierid());
					hubSupplierSeasonDicRequestDto.setSupplierSeason(dic.get(0).getSupplierSeason());
				}
				Date date = new Date();
				String taskNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date);
				taskImportService.saveTask(taskNo, "季节映射:"+hubSupplierSeasonDicRequestDto.getSupplierSeason()+"=>"+hubSupplierSeasonDicRequestDto.getHubMarketTime()+"_"
				+hubSupplierSeasonDicRequestDto.getHubSeason(), hubSupplierSeasonDicRequestDto.getUpdateUser(), TaskType.REFRESH_DIC.getIndex());
				hubSupplierSeasonDicRequestDto.setRefreshDicType(InfoState.RefreshSeason.getIndex());
				taskImportService.sendTaskMessage(taskNo,TaskType.REFRESH_DIC.getIndex(),JsonUtil.serialize(hubSupplierSeasonDicRequestDto));
				shangpinRedis.del(ConstantProperty.REDIS_EPHUB_SUPPLIER_SEASON_MAPPING_MAP_KEY+"_"+hubSupplierSeasonDicRequestDto.getSupplierId());
			}
			return HubResponse.successResp(null);
		} catch (Exception e) {
			log.error("刷新失败：{}", e);
		}
		return HubResponse.errorResp("刷新异常");
	}

	@RequestMapping(value = "/detail/{id}", method = RequestMethod.POST)
	public HubResponse selectHubSupplierCateoryDetail(@PathVariable("id") Long id) {
		try {
			if (id != null) {
				HubSeasonDicDto detail = hubSeasonDicService.getSupplierSeasonById(id);
				if (detail != null) {
					List<HubSeasonDicDto> responseList = new ArrayList<HubSeasonDicDto>();
					HubSeasonDicDto dic = new HubSeasonDicDto();
					BeanUtils.copyProperties(detail, dic);
					responseList.add(dic);
					return HubResponse.successResp(responseList);
				} else {
					return HubResponse.errorResp("列表页为空");
				}
			} else {
				return HubResponse.errorResp("传值为空");
			}
		} catch (Exception e) {
			log.error("获取列表失败：{}", e);
			return HubResponse.errorResp("获取列表失败");
		}
	}

	/**
	 * 导出查询商品
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/save", method = { RequestMethod.POST, RequestMethod.GET })
	public HubResponse save(@RequestBody HubSupplierSeasonDicRequestDto hubSupplierSeasonDicRequestDto) {

		try {
			log.info("保存参数：{}",hubSupplierSeasonDicRequestDto);
			HubSeasonDicDto dicDto = new HubSeasonDicDto();
			BeanUtils.copyProperties(hubSupplierSeasonDicRequestDto, dicDto);
			dicDto.setCreateTime(new Date());
			dicDto.setUpdateTime(new Date());
			
			if(StringUtils.isNotBlank(hubSupplierSeasonDicRequestDto.getSupplierNo())){
				SupplierDTO supplierDto = supplierService.getSupplier(hubSupplierSeasonDicRequestDto.getSupplierNo());
				log.info("supplierDto:{}",supplierDto);
				if(supplierDto!=null){
					dicDto.setSupplierid(supplierDto.getSopUserNo());
				}	
			}
			dicDto.setMemo(String.valueOf(hubSupplierSeasonDicRequestDto.getFilterFlag()));
			dicDto.setPushState((byte)1);
			//待处理保存更新
			hubSeasonDicService.saveHubSeason(dicDto);
			return HubResponse.successResp(null);
		} catch (Exception e) {
			log.error("保存失败：{}", e);
		}
		return HubResponse.errorResp("保存异常");
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public HubResponse deleteHubSupplierCateoryDetail(@PathVariable("id") Long id) {
			hubSeasonDicService.deleteHubSeasonById(id);
			return HubResponse.successResp(null);
	}

}
