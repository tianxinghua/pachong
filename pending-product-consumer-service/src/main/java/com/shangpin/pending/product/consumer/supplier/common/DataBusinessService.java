package com.shangpin.pending.product.consumer.supplier.common;

import com.shangpin.ephub.client.data.mysql.enumeration.StockState;
import com.shangpin.ephub.client.data.mysql.enumeration.SupplierPriceState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.studio.dic.dto.HubDicStudioBrandCriteriaDto;
import com.shangpin.ephub.client.data.mysql.studio.dic.dto.HubDicStudioBrandDto;
import com.shangpin.ephub.client.data.mysql.studio.dic.gateway.HubDicStudioBrandGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lizhongren on 2017/8/24.
 */
@Service
@Slf4j
public class DataBusinessService extends DataServiceHandler {



    @Autowired
    private HubSpuPendingGateWay hubSpuPendingGateWay;

    @Autowired
    private HubSkuPendingGateWay hubSkuPendingGateWay;

    @Autowired
    private HubDicStudioBrandGateway studioBrandGateway;



    public void updateSpuPendingStockAndPriceState(Long  spuPendingId){
        try {
            if(null==spuPendingId) return;
            updateSpuPendingStockAndPriceState(spuPendingId,getStockTotalBySpuPendingId(spuPendingId));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public int getStockTotalBySpuPendingId(Long spuPendingId){

        return hubSkuPendingGateWay.sumStockBySpuPendingId(spuPendingId);

    }

    public void updateSpuPendingStockState(Long spuPendingId,int stockState){

        HubSpuPendingDto spuPendingDto = new HubSpuPendingDto();
        spuPendingDto.setSpuPendingId(spuPendingId);
        if(stockState>0){
            spuPendingDto.setStockState(StockState.HANDLED.getIndex());
        }else if(stockState<=0){
            spuPendingDto.setStockState(StockState.NOSTOCK.getIndex());
        }
        hubSpuPendingGateWay.updateByPrimaryKeySelective(spuPendingDto);

    }




    /**
     * 查询是否需要排查
     * @param spBrandNo
     * @param spCategoryNo
     * @return
     */
    public boolean isNeedShoot(String spBrandNo,String spCategoryNo){

        HubDicStudioBrandCriteriaDto brandAndCategoryCriteria = new HubDicStudioBrandCriteriaDto();
        brandAndCategoryCriteria.createCriteria().andSpBrandNoEqualTo(spBrandNo).andSpCategoryNoEqualTo(spCategoryNo);
        List<HubDicStudioBrandDto> hubDicStudioBrandDtos = studioBrandGateway.selectByCriteria(brandAndCategoryCriteria);
        if(null!=hubDicStudioBrandDtos&&hubDicStudioBrandDtos.size()>0){
            return true;
        }else{
            HubDicStudioBrandCriteriaDto brandCriteria = new HubDicStudioBrandCriteriaDto();
            brandCriteria.createCriteria().andSpBrandNoEqualTo(spBrandNo);
            List<HubDicStudioBrandDto> brandDtos = studioBrandGateway.selectByCriteria(brandCriteria);
            if(null!=brandDtos&&brandDtos.size()>0){
                return true;
            }else{
                return  false;
            }
        }
    }


    public void updateSpuPendingStockAndPriceState(Long spuPendingId,int stockState){

        HubSpuPendingDto spuPendingDto = new HubSpuPendingDto();
        spuPendingDto.setSpuPendingId(spuPendingId);
        if(stockState>0){
            spuPendingDto.setStockState(StockState.HANDLED.getIndex());
        }else if(stockState<=0){
            spuPendingDto.setStockState(StockState.NOSTOCK.getIndex());
        }
        HubSkuPendingCriteriaDto criteria = new HubSkuPendingCriteriaDto();
        List<HubSkuPendingDto> hubSkuPendingDtos = hubSkuPendingGateWay.selectByCriteria(criteria);
        if(null!=hubSkuPendingDtos&&hubSkuPendingDtos.size()>0){
            BigDecimal marketPrice = hubSkuPendingDtos.get(0).getMarketPrice();
            BigDecimal supplyPrice = hubSkuPendingDtos.get(0).getSupplyPrice();
            if(null==marketPrice){
                spuPendingDto.setMarketPriceState(SupplierPriceState.NO_PRICE.getIndex());
            }else{
                spuPendingDto.setMarketPriceState(SupplierPriceState.HAVE_PRICE.getIndex());
            }
            if(null==supplyPrice){
                spuPendingDto.setSupplyPriceState(SupplierPriceState.NO_PRICE.getIndex());
            }else{
                spuPendingDto.setSupplyPriceState(SupplierPriceState.HAVE_PRICE.getIndex());
            }
        }else{
            spuPendingDto.setMarketPriceState(SupplierPriceState.NO_PRICE.getIndex());
            spuPendingDto.setSupplyPriceState(SupplierPriceState.NO_PRICE.getIndex());
        }

        hubSpuPendingGateWay.updateByPrimaryKeySelective(spuPendingDto);

    }







    //	private void updateSpuStockStateForInsertSku(SpuPending hubSpuPending, HubSkuPendingDto hubSkuPending) {
//		boolean isMarketPrice = true, isSupplyPrice = true;
//		if(null==hubSkuPending.getMarketPrice()||hubSkuPending.getMarketPrice().intValue()==0){
//			isMarketPrice = false;
//		}
//		if(null==hubSkuPending.getSupplyPrice()||hubSkuPending.getSupplyPrice().intValue()==0){
//			isSupplyPrice = false;
//		}
//
//		if(null!=hubSpuPending.getStockState()){
//            if(hubSpuPending.getStockState().toString().equals(String.valueOf(StockState.NOSKU.getIndex()))){
//                spuPendingHandler.updateStotckStateAndPriceState(hubSpuPending.getSpuPendingId(),hubSkuPending.getStock(),isMarketPrice,isSupplyPrice);
//            }else if(hubSpuPending.getStockState().toString().equals(String.valueOf(StockState.NOSTOCK.getIndex()))){
//                if(hubSkuPending.getStock()>0){
//                    spuPendingHandler.updateStotckStateAndPriceState(hubSpuPending.getSpuPendingId(),hubSkuPending.getStock(),isMarketPrice,isSupplyPrice);
//                }
//            }else{
//                //原库存标记为有库存
//                if(0==hubSkuPending.getStock()){
//                    int totalStock = 0;
//                    try {
//                        totalStock = dataBusinessService.getStockTotalBySpuPendingId(hubSpuPending.getSpuPendingId());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    if(0==totalStock){
//                        spuPendingHandler.updateStotckStateAndPriceState(hubSpuPending.getSpuPendingId(),totalStock,isMarketPrice,isSupplyPrice);
//                    }
//
//                }
//            }
//        }else{//遗漏  第一次插入SKU  应该默认赋值为NOSKU
//            spuPendingHandler.updateStotckStateAndPriceState(hubSpuPending.getSpuPendingId(),hubSkuPending.getStock(),isMarketPrice,isSupplyPrice);
//
//        }
//	}





}
