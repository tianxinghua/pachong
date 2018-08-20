package com.shangpin.ephub.product.business.rest.price.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ephub.client.consumer.price.dto.ProductPriceDTO;
import com.shangpin.ephub.client.consumer.price.gateway.PriceMqGateWay;
import com.shangpin.ephub.client.data.mysql.enumeration.PriceHandleState;
import com.shangpin.ephub.client.data.mysql.enumeration.PriceHandleType;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierPriceChangeRecordDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
import com.shangpin.ephub.client.product.business.gms.result.HubResponseDto;
import com.shangpin.ephub.client.product.business.price.dto.PriceDto;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.rest.gms.dto.CategoryRequestDto;
import com.shangpin.ephub.product.business.rest.gms.dto.FourLevelCategory;
import com.shangpin.ephub.product.business.rest.price.dto.PriceQuery;
import com.shangpin.ephub.product.business.rest.price.service.PriceService;
import com.shangpin.ephub.product.business.rest.price.vo.PriceChangeRecordDto;
import com.shangpin.ephub.product.business.rest.price.vo.ProductPrice;
/**
 * <p>Title: PriceController</p>
 * <p>Description: 供价推送服务接口 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年3月31日 下午12:09:11
 *
 */
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/price")
@Slf4j
public class PriceController {
	
	@Autowired
	private PriceService priceService;
	@Autowired
    PriceMqGateWay priceMqGateWay;
	@Autowired
	private HubSupplierSkuGateWay hubSupplierSkuGateWay;
	@Autowired
	private HubSupplierSpuGateWay hubSupplierSpuGateWay;

	@RequestMapping(value = "/save-and-sendmessage")
	public void savePriceRecordAndSendConsumer(@RequestBody PriceDto priceDto) throws Exception{
		priceService.savePriceRecordAndSendConsumer(priceDto);
	}
	
	@RequestMapping(value = "/save-and-sendmessage-new")
	public void savePriceRecordAndSendConsumerNew(@RequestBody PriceDto priceDto) throws Exception{
		priceService.savePriceRecordAndSendConsumerNew(priceDto);
	}
	//zhicai商品变价
	@RequestMapping(value = "/save-and-sendmessage-zhicai")
	public HubResponse<?> savePriceRecordAndSendConsumerzhicai(@RequestBody com.shangpin.ephub.product.business.rest.price.dto.PriceDto priceDto) throws Exception{
		
		if(StringUtils.isBlank(priceDto.getSupplierSkuNo())||StringUtils.isBlank(priceDto.getMarketPrice())||StringUtils.isBlank(priceDto.getSupplierId())||StringUtils.isBlank(priceDto.getSupplierNo()))
			return HubResponse.errorResp("参数不能为null，全部为必填!");
		
		try {
			HubSupplierSkuCriteriaDto hubSupplierSkuCriteriaDto = new HubSupplierSkuCriteriaDto();
			hubSupplierSkuCriteriaDto.createCriteria().andSupplierSkuNoEqualTo(priceDto.getSupplierSkuNo()).andSupplierIdEqualTo(priceDto.getSupplierId());
			List<HubSupplierSkuDto> hubSupplierSkuDtoList = hubSupplierSkuGateWay.selectByCriteria(hubSupplierSkuCriteriaDto);

			if(hubSupplierSkuDtoList!=null&&hubSupplierSkuDtoList.size()>0) {
				HubSupplierSkuDto hubSupplierSkuDto = hubSupplierSkuDtoList.get(0);
				log.info("hubSupplierSkuDto："+JSONObject.toJSONString(hubSupplierSkuDto)); 
				if(hubSupplierSkuDto.getMarketPrice().compareTo(new BigDecimal(priceDto.getMarketPrice()))==0) {
					return HubResponse.errorResp("价格与数据库市场价一致，不推送!");
				}
				hubSupplierSkuDto.setMarketPrice(new BigDecimal(priceDto.getMarketPrice()));
				//更新数据库的SKU价格
				HubSupplierSkuDto skuDtoTmp  = new HubSupplierSkuDto();
				skuDtoTmp.setSupplierSkuId(hubSupplierSkuDto.getSupplierSkuId());
				skuDtoTmp.setMarketPrice(new BigDecimal(priceDto.getMarketPrice()));
				hubSupplierSkuGateWay.updateByPrimaryKeySelective(skuDtoTmp);

				Long hubSupplierSpuId = hubSupplierSkuDto.getSupplierSpuId();
				HubSupplierSpuDto hubSupplierSpuDto = hubSupplierSpuGateWay.selectByPrimaryKey(hubSupplierSpuId);
				PriceDto dto = new PriceDto();
				dto.setHubSkus(hubSupplierSkuDtoList);
				dto.setHubSpu(hubSupplierSpuDto);
				dto.setSupplierNo(priceDto.getSupplierNo());
				priceService.savePriceRecordAndSendConsumer(dto);
			}
		} catch (Exception e) {
			log.info("变价失败 PriceDto:"+JSONObject.toJSONString(priceDto));
			log.info(e.getMessage());
			return HubResponse.errorResp("变价失败!"+e.getMessage()); 
		}
		return HubResponse.successResp("变价推送成功");
	}
		
	
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public HubResponse<?> priceList(@RequestBody PriceQuery priceQueryDto){
		ProductPrice price = priceService.priceList(priceQueryDto);
		if(null != price){
			return HubResponse.successResp(price);
		}else{
			return HubResponse.errorResp("查询参数错误或服务发生异常");
		}
	}

	@RequestMapping(value="/update-price-handle-status",method=RequestMethod.POST)
	public HubResponse<?> updatePriceHandleStatus(@RequestBody List<PriceChangeRecordDto> priceChangeRecordDtos){
		ObjectMapper mapper = new ObjectMapper();
		try {
			log.info("call price/update-price-handle-status parameter = " +
                    mapper.writeValueAsString(priceChangeRecordDtos));
		} catch (Exception e) {
			log.error("call price/update-price-handle-status error: "+e.getMessage(),e);
		}
		for(PriceChangeRecordDto priceChangeRecordDto:priceChangeRecordDtos) {
			try {
				if(null!=priceChangeRecordDto.getId()&&0!=priceChangeRecordDto.getId()){
                    if("1".equals(priceChangeRecordDto.getSign())){
                        priceService.updateState(priceChangeRecordDto.getId(), PriceHandleState.HANDLED_SUCCESS);
                    } else{
                        priceService.updateState(priceChangeRecordDto.getId(),PriceHandleState.HANDLE_ERROR,priceChangeRecordDto.getMemo());
                    }

                }else{

                        if("1".equals(priceChangeRecordDto.getSign())){
                            priceService.updateState(priceChangeRecordDto.getSupplierId(),priceChangeRecordDto.getSkuNo(),"", PriceHandleState.HANDLED_SUCCESS);
                        } else{
                            priceService.updateState(priceChangeRecordDto.getSupplierId(),priceChangeRecordDto.getSkuNo(),priceChangeRecordDto.getMemo(),PriceHandleState.HANDLE_ERROR);
                        }

                }
			} catch (Exception e) {
				log.error("update price record :" + priceChangeRecordDto.toString() + " state error:" + e.getMessage(),e);
			}
		}


		 return HubResponse.successResp(true);

	}
	
	@RequestMapping(value="/push-price",method=RequestMethod.POST)
	public HubResponse<?> pushPrice(@RequestBody PriceQuery priceQueryDto){
		int startRow = 1;
    	List<HubSupplierPriceChangeRecordDto> pushMqErrorRecordList = new ArrayList<>();
    	try {
	    	priceService.findPushMqErrorRecordList(startRow,pushMqErrorRecordList,priceQueryDto);
	        List<HubSupplierPriceChangeRecordDto> needHandleRecords = priceService.findNeedHandleRecord(pushMqErrorRecordList);
	        for(HubSupplierPriceChangeRecordDto tryDao:needHandleRecords){
	        	try {
		            ProductPriceDTO productPriceDTO = new ProductPriceDTO();
		            this.transObject(tryDao,productPriceDTO);
		            log.info("重推价格消息体："+ JsonUtil.serialize(productPriceDTO));
		            priceMqGateWay.transPrice(productPriceDTO);
		            log.info(productPriceDTO.getSopUserNo()+" "+productPriceDTO.getSupplierSkuNo()+" 发送队列成功。");
	        	} catch (Exception e) {
	        		log.info(e.getMessage());
					e.printStackTrace();
				}
	        }
    	} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			return HubResponse.successResp("推送失败!");
		}
        return HubResponse.successResp("推送成功!");
	}
	private void  transObject(HubSupplierPriceChangeRecordDto sourceObj,ProductPriceDTO targetObj){
        targetObj.setSupplierPriceChangeRecordId(sourceObj.getSupplierPriceChangeRecordId());
        targetObj.setMarketPrice(sourceObj.getMarketPrice().toString());
        targetObj.setPurchasePrice(sourceObj.getSupplyPrice().toString());
        targetObj.setMarketSeason(sourceObj.getMarketSeason());
        targetObj.setMarketYear(sourceObj.getMarketYear());
        targetObj.setSkuNo(sourceObj.getSpSkuNo());
        targetObj.setSupplierSkuNo(sourceObj.getSupplierSkuNo());
        targetObj.setSopUserNo(sourceObj.getSupplierId());
        targetObj.setSupplierNo(sourceObj.getSupplierNo()); 
        targetObj.setPriceHandleType(PriceHandleType.NEW_DEFAULT.getIndex());
    }
}
