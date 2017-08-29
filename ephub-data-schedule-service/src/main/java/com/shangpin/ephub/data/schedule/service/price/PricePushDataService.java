package com.shangpin.ephub.data.schedule.service.price;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.enumeration.PriceHandleState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierPriceChangeRecordCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierPriceChangeRecordDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierPriceChangeRecordGateWay;

/**
 * Created by lizhongren on 2017/5/23.
 */
@Component
public class PricePushDataService {
	
    @Autowired
    HubSupplierPriceChangeRecordGateWay priceChangeRecordGateWay;

    /**
     * 如果多个相同的供货商下的同一个商品 取最新的 老的不再处理
     * @return
     */
    public void  findPushMqErrorRecordList(int startRow,List<HubSupplierPriceChangeRecordDto> recordDtos) throws Exception{
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); 
		c.add(Calendar.DAY_OF_MONTH, -1);
		Date start = sdformat.parse(sdf.format(c.getTime())+" 00:00:00");
		Date end = sdformat.parse(sdf.format(c.getTime())+" 23:59:59");
		List<Byte> list = new ArrayList<>();
		list.add(PriceHandleState.PUSHED_ERROR.getIndex());
		list.add(PriceHandleState.PUSHED_OPENAPI_ERROR.getIndex());
        HubSupplierPriceChangeRecordCriteriaDto criteriaDto = new HubSupplierPriceChangeRecordCriteriaDto();
        criteriaDto.setOrderByClause(" create_time desc ");
        criteriaDto.createCriteria().andStateIn(list).andMarketSeasonIsNotNull().andCreateTimeGreaterThanOrEqualTo(start).andCreateTimeLessThanOrEqualTo(end); 
        criteriaDto.setPageNo(startRow);
        criteriaDto.setPageSize(100);
        List<HubSupplierPriceChangeRecordDto> hubSupplierPriceChangeRecordDtos = priceChangeRecordGateWay.selectByCriteria(criteriaDto);
        Map<String,String> priceChangeMap = new HashMap<>();
        int count = hubSupplierPriceChangeRecordDtos.size();
        for(int i= 0 ;i<hubSupplierPriceChangeRecordDtos.size();i++){
            HubSupplierPriceChangeRecordDto dto = hubSupplierPriceChangeRecordDtos.get(i);
            if(priceChangeMap.containsKey(dto.getSupplierId()+"-"+dto.getSupplierSkuNo())){
                hubSupplierPriceChangeRecordDtos.remove(i);
                i--;
            }else{
                priceChangeMap.put(dto.getSupplierId()+"-"+dto.getSupplierSkuNo(),"");
            }
        }
        recordDtos.addAll(hubSupplierPriceChangeRecordDtos);
        if(count==100){
        	startRow++;
        	findPushMqErrorRecordList(startRow,recordDtos);
        }
    }


    public List<HubSupplierPriceChangeRecordDto>  findNeedHandleRecord(List<HubSupplierPriceChangeRecordDto>  dtos){
        List<HubSupplierPriceChangeRecordDto> needHandles = new ArrayList<>();
        for(HubSupplierPriceChangeRecordDto dto:dtos){
            HubSupplierPriceChangeRecordCriteriaDto criteriaDto = new HubSupplierPriceChangeRecordCriteriaDto();

            criteriaDto.createCriteria().andSupplierIdEqualTo(dto.getSupplierId()).andSupplierSkuNoEqualTo(dto.getSupplierSkuNo()).andMarketSeasonIsNotNull()
            .andCreateTimeGreaterThan(dto.getCreateTime());
            List<HubSupplierPriceChangeRecordDto> hubSupplierPriceChangeRecordDtos = priceChangeRecordGateWay.selectByCriteria(criteriaDto);
            if(null ==hubSupplierPriceChangeRecordDtos || hubSupplierPriceChangeRecordDtos.size()== 0){
            	needHandles.add(dto);
            }
        }
        return needHandles;


    }


}