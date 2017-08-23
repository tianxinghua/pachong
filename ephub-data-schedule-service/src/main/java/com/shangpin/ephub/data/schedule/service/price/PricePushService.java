package com.shangpin.ephub.data.schedule.service.price;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.shangpin.ephub.client.consumer.price.dto.ProductPriceDTO;
import com.shangpin.ephub.client.consumer.price.gateway.PriceMqGateWay;
import com.shangpin.ephub.client.data.mysql.enumeration.PriceHandleState;
import com.shangpin.ephub.client.data.mysql.enumeration.PriceHandleType;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierPriceChangeRecordCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierPriceChangeRecordDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierPriceChangeRecordGateWay;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.data.schedule.service.mail.SendMailService;
import com.shangpin.ephub.data.schedule.service.product.ProductPullDataService;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by lizhongren on 2017/5/23.
 */
@Service
@Slf4j
public class PricePushService {
	
	private static final String LINE_BREAK = "<br>";
	
    @Autowired
    PricePushDataService pricePushDataService;
    @Autowired
    PriceMqGateWay priceMqGateWay;
    @Autowired
    ProductPullDataService productPullDataService;
    @Autowired
    HubSupplierPriceChangeRecordGateWay priceChangeRecordGateWay;
    @Autowired
    private SendMailService sendMailService;

    public void handleErrorPush() throws Exception{
    	int startRow = 1;
    	List<HubSupplierPriceChangeRecordDto> pushMqErrorRecordList = new ArrayList<>();
    	pricePushDataService.findPushMqErrorRecordList(startRow,pushMqErrorRecordList);
        List<HubSupplierPriceChangeRecordDto> needHandleRecords = pricePushDataService.findNeedHandleRecord(pushMqErrorRecordList);
        for(HubSupplierPriceChangeRecordDto tryDao:needHandleRecords){
            ProductPriceDTO productPriceDTO = new ProductPriceDTO();
            this.transObject(tryDao,productPriceDTO);
            log.info("重推价格消息体："+ JsonUtil.serialize(productPriceDTO));
            priceMqGateWay.transPrice(productPriceDTO);
            log.info(productPriceDTO.getSopUserNo()+" "+productPriceDTO.getSupplierSkuNo()+" 发送队列成功。");
        }
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
    
    public void priceCheck(){
    	List<HubSupplierValueMappingDto> suppliers = productPullDataService.findAllSupplier();
    	if(CollectionUtils.isNotEmpty(suppliers)){
    		Date start = new Date(new Date().getTime() - 24*1000*60*60);
    		StringBuffer buffer = new StringBuffer();
    		suppliers.forEach(s -> findFailRecords(s,start,buffer)); 
    		String messageText = buffer.toString();
    		if(StringUtils.isNotBlank(messageText)){
    			sendMailService.sendMail("EPHUB推送价格检测", messageText); 
    		}
    	}
    }
    
    public void findFailRecords(HubSupplierValueMappingDto supplierDto, Date start, StringBuffer buffer){
    	try {
    		log.info("开始检测 "+supplierDto.getHubVal()+" 价格推送情况...");
    		HubSupplierPriceChangeRecordCriteriaDto criteria = new HubSupplierPriceChangeRecordCriteriaDto();
    		criteria.setPageNo(1);
    		criteria.setPageSize(1000);
    		criteria.setFields("supplier_sku_no,type,state");
    		criteria.setOrderByClause("create_time desc");
    		criteria.createCriteria().andSupplierIdEqualTo(supplierDto.getSupplierId()).andStateIn(Lists.newArrayList(PriceHandleState.PUSHED_ERROR.getIndex(),PriceHandleState.HANDLE_ERROR.getIndex(),PriceHandleState.PUSHED_OPENAPI_ERROR.getIndex())).andCreateTimeGreaterThanOrEqualTo(start);
    		List<HubSupplierPriceChangeRecordDto> list = priceChangeRecordGateWay.selectByCriteria(criteria );
    		if(CollectionUtils.isNotEmpty(list)){
    			Map<String,HubSupplierPriceChangeRecordDto> maps = Maps.newHashMap();
    			for(HubSupplierPriceChangeRecordDto dto : list){
    				if(!maps.containsKey(dto.getSupplierSkuNo())){
    					maps.put(dto.getSupplierSkuNo(), dto);
    				}
    			}
				buffer.append("供应商"+supplierDto.getHubVal()+"推送失败的sku有：").append(LINE_BREAK);
				maps.values().forEach((dto) -> buffer.append("SUPPLIERSKU："+dto.getSupplierSkuNo()+"，当前状态："+getStateMessage(dto.getState())+"，类型："+getTypeMessage(dto.getType())).append(LINE_BREAK));
    		}
		} catch (Exception e) {
			log.error("检测 "+supplierDto.getHubVal()+" 价格推送情况异常："+e.getMessage(),e); 
		}
    }
    
    private String getStateMessage(Byte state){
    	if(null != state){
    		for(PriceHandleState priceState : PriceHandleState.values()){
    			if(state == priceState.getIndex()){
    				return priceState.getDescription();
    			}
    		}
    	}
    	return "";
    }
    
    private String getTypeMessage(Byte type){
    	if(null != type){
    		for(PriceHandleType priceType : PriceHandleType.values()){
    			if(type == priceType.getIndex()){
    				return priceType.getDescription();
    			}
    		}
    	}
    	return "";
    }
}
