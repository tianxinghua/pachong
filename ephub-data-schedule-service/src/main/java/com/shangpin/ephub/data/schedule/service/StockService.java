package com.shangpin.ephub.data.schedule.service;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lizhongren on 2017/5/6.
 */
@Service
@Slf4j
public class StockService {

    @Autowired
    HubSupplierSkuGateWay hubSupplierSkuGateWay;

    @Autowired
    HubSkuPendingGateWay hubSkuPendingGateWay;

    @Autowired
    SpuPendingHandler spuPendingHandler;

    public void updateStockToZero() throws Exception{

        List<HubSupplierSkuDto> skuDtos = this.findSupplierSkuNoUpdateOutSevenDay();
        for(HubSupplierSkuDto supplierSkuDto:skuDtos){
            if(supplierSkuDto.getStock()>0){
                try {
                    //更新库存为0
                    updateSupplierSkuStock(supplierSkuDto.getSupplierSkuId());
                    //查找HUB_SKU_PENDING ,查找到更新库存为0
                    HubSkuPendingDto skuPendingDto = this.getSkuPending(supplierSkuDto.getSupplierId(),supplierSkuDto.getSupplierSkuNo());
                    if(null!=skuPendingDto){
                        //清库存
                        updateSkuPendingStock(skuPendingDto.getSkuPendingId());
                        //计算HUB_SPU的库存状态
                        updateSpuPendingStockState(skuPendingDto);
                    }
                } catch (Exception e) {
                    log.error("set stock error for supplierSku : " + supplierSkuDto.toString() + " reason :" + e.getMessage(),e);
                }

            }
        }


    }

    private void updateSpuPendingStockState(HubSkuPendingDto skuPendingDto) {
        int totalStock = spuPendingHandler.getStockTotalBySpuPendingId(skuPendingDto.getSpuPendingId());
        if(totalStock>0){
            spuPendingHandler.updateStotckState(skuPendingDto.getSpuPendingId(),totalStock);
        }else{
            spuPendingHandler.updateStotckState(skuPendingDto.getSpuPendingId(),0);
        }
    }

    private List<HubSupplierSkuDto> findSupplierSkuNoUpdateOutSevenDay(){
        Calendar  calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,-7);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
        Date date = calendar.getTime();

        HubSupplierSkuCriteriaDto criteriaDto = new HubSupplierSkuCriteriaDto();
        criteriaDto.createCriteria().andLastPullTimeLessThan(date);
        return  hubSupplierSkuGateWay.selectByCriteria(criteriaDto);
    }

    private HubSkuPendingDto getSkuPending(String supplerId,String supplierSkuNo){
        HubSkuPendingCriteriaDto criteriaDto = new HubSkuPendingCriteriaDto();
        criteriaDto.createCriteria().andSupplierIdEqualTo(supplerId).andSupplierSkuNoEqualTo(supplierSkuNo);

        List<HubSkuPendingDto> hubSkuPendingDtos = hubSkuPendingGateWay.selectByCriteria(criteriaDto);
        if(null!=hubSkuPendingDtos&&hubSkuPendingDtos.size()>0){
            return hubSkuPendingDtos.get(0);
        }else{
            return null;
        }
    }

    private void updateSupplierSkuStock(Long supplierSkuId) throws Exception{
        HubSupplierSkuDto skuDto = new HubSupplierSkuDto();
        skuDto.setSupplierSkuId(supplierSkuId);
        skuDto.setStock(0);
        hubSupplierSkuGateWay.updateByPrimaryKeySelective(skuDto);
    }

    private void updateSkuPendingStock(Long skuPendingId) throws Exception{
        HubSkuPendingDto skuDto = new HubSkuPendingDto();
        skuDto.setSkuPendingId(skuPendingId);
        skuDto.setStock(0);
        hubSkuPendingGateWay.updateByPrimaryKeySelective(skuDto);
    }

}
