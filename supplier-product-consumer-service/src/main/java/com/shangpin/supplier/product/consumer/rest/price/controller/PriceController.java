package com.shangpin.supplier.product.consumer.rest.price.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
import com.shangpin.ephub.response.HubResponse;
import com.shangpin.supplier.product.consumer.rest.price.dto.PriceDto;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/price")
@Slf4j
public class PriceController {
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private HubSupplierSkuGateWay hubSupplierSkuGateWay;
	@Autowired
	private HubSupplierSpuGateWay hubSupplierSpuGateWay;

	@RequestMapping(value = "/save-and-sendmessage")
	public HubResponse<?> savePriceRecordAndSendConsumer(@RequestBody PriceDto priceDto) throws Exception{
		if(StringUtils.isBlank(priceDto.getSupplierSkuNo())||StringUtils.isBlank(priceDto.getMarketPrice())||StringUtils.isBlank(priceDto.getSupplierId())||StringUtils.isBlank(priceDto.getSupplierNo()))
			return HubResponse.errorResp("参数不能为null，全部为必填!");
		
		try {
			HubSupplierSkuCriteriaDto hubSupplierSkuCriteriaDto = new HubSupplierSkuCriteriaDto();
			hubSupplierSkuCriteriaDto.createCriteria().andSupplierSkuNoEqualTo(priceDto.getSupplierSkuNo()).andSupplierIdEqualTo(priceDto.getSupplierId());
			List<HubSupplierSkuDto> hubSupplierSkuDtoList = hubSupplierSkuGateWay.selectByCriteria(hubSupplierSkuCriteriaDto);
			List<HubSupplierSkuDto> hubSupplierSkuDtoListNew = new ArrayList<>();
			if(hubSupplierSkuDtoList!=null&&hubSupplierSkuDtoList.size()>0) {
				HubSupplierSkuDto hubSupplierSkuDto = hubSupplierSkuDtoList.get(0);
				log.info("hubSupplierSkuDto："+JSONObject.toJSONString(hubSupplierSkuDto)); 
				if(hubSupplierSkuDto.getMarketPrice().compareTo(new BigDecimal(priceDto.getMarketPrice()))==0) {
					return HubResponse.errorResp("价格与数据库市场价一致，不推送!");
				}
				hubSupplierSkuDto.setMarketPrice(new BigDecimal(priceDto.getMarketPrice()));
				hubSupplierSkuDtoListNew.add(hubSupplierSkuDto);
				Long hubSupplierSpuId = hubSupplierSkuDto.getSupplierSpuId();
				HubSupplierSpuDto hubSupplierSpuDto = hubSupplierSpuGateWay.selectByPrimaryKey(hubSupplierSpuId);
				supplierProductSaveAndSendToPending.savePriceRecordAndSendConsumer(priceDto.getSupplierNo(), hubSupplierSpuDto, hubSupplierSkuDtoListNew );
			}
		} catch (Exception e) {
			log.info("变价失败 PriceDto:"+JSONObject.toJSONString(priceDto));
			log.info(e.getMessage());
		}
		return HubResponse.successResp("变价推送成功");
	}
}
