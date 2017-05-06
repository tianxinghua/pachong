package com.shangpin.ephub.product.business.ui.mapp.season.service;

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
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.product.business.common.hubDic.season.HubSeasonDicService;
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
		
		int total = hubSeasonDicService.countHubSeason(hubSupplierSeasonDicRequestDto.getSupplierSeason(),hubSupplierSeasonDicRequestDto.getHubMarketTime(),hubSupplierSeasonDicRequestDto.getHubSeason(),hubSupplierSeasonDicRequestDto.getType());
		
		log.info("返回个数："+total);
		if(total>0){
			List<HubSeasonDicDto> list = hubSeasonDicService.getHubSeason(hubSupplierSeasonDicRequestDto.getSupplierSeason(),hubSupplierSeasonDicRequestDto.getHubMarketTime(),hubSupplierSeasonDicRequestDto.getHubSeason(),hubSupplierSeasonDicRequestDto.getType(),hubSupplierSeasonDicRequestDto.getPageNo(), hubSupplierSeasonDicRequestDto.getPageSize());
			if (list != null && list.size() > 0) {
				List<HubSupplierSeasonDicResponseDto> responseList = new ArrayList<HubSupplierSeasonDicResponseDto>();
				for (HubSeasonDicDto dicDto : list) {
					HubSupplierSeasonDicResponseDto dic = new HubSupplierSeasonDicResponseDto();
					BeanUtils.copyProperties(dicDto, dic);
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
			HubSeasonDicDto dicDto = new HubSeasonDicDto();
			BeanUtils.copyProperties(hubSupplierSeasonDicRequestDto, dicDto);
			dicDto.setCreateTime(new Date());
			dicDto.setUpdateTime(new Date());
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
