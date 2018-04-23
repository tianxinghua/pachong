package com.shangpin.ephub.data.schedule.service.stock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.data.schedule.service.product.SpuPendingHandler;

import lombok.extern.slf4j.Slf4j;




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
        log.info("需要清除库存的产品的总数是："+skuDtos.size());
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
        spuPendingHandler.updateStotckState(skuPendingDto.getSpuPendingId(),totalStock);
    }

    private List<HubSupplierSkuDto> findSupplierSkuNoUpdateOutSevenDay(){
        Calendar  calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,-7);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
        Date date = calendar.getTime();
        log.info("今天库存清零的商品是"+format.format(date)+"之前的");

        HubSupplierSkuCriteriaDto criteriaDto = new HubSupplierSkuCriteriaDto();
        criteriaDto.setPageSize(Integer.MAX_VALUE);
        /**
         * 以下2个供应商是暂时不检测的两个供应商
         */
        List<String> values = new ArrayList<String>();
        values.add("2015092801542");//francescomassa
        values.add("2015101501616");//frmoda
        values.add("2016051001890");//reebonz
        values.add("2018041302004");//BAC (reebonz)

		criteriaDto.createCriteria().andLastPullTimeLessThan(date).andSupplierIdNotIn(values ).andStockGreaterThan(0);
		criteriaDto.or(criteriaDto.createCriteria().andLastPullTimeIsNull().andSupplierIdNotIn(values ).andStockGreaterThan(0));
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

	public void updateSupplierStockToPendindStock() {
		hubSkuPendingGateWay.updateStock();
	}

}
