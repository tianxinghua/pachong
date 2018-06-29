package com.shangpin.ephub.product.business.rest.price.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ephub.client.consumer.price.dto.ProductPriceDTO;
import com.shangpin.ephub.client.consumer.price.gateway.PriceMqGateWay;
import com.shangpin.ephub.client.data.mysql.enumeration.PriceHandleState;
import com.shangpin.ephub.client.data.mysql.enumeration.PriceHandleType;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierPriceChangeRecordDto;
import com.shangpin.ephub.client.product.business.price.dto.PriceDto;
import com.shangpin.ephub.client.util.JsonUtil;
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

	@RequestMapping(value = "/save-and-sendmessage")
	public void savePriceRecordAndSendConsumer(@RequestBody PriceDto priceDto) throws Exception{
		priceService.savePriceRecordAndSendConsumer(priceDto);
	}
	
	@RequestMapping(value = "/save-and-sendmessage-new")
	public void savePriceRecordAndSendConsumerNew(@RequestBody PriceDto priceDto) throws Exception{
		priceService.savePriceRecordAndSendConsumerNew(priceDto);
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
