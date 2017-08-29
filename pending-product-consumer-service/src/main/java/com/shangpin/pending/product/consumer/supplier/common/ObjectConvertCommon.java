package com.shangpin.pending.product.consumer.supplier.common;

import com.shangpin.ephub.client.data.mysql.enumeration.FilterFlag;
import com.shangpin.ephub.client.data.mysql.enumeration.StockState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.message.pending.body.sku.PendingSku;
import com.shangpin.pending.product.consumer.common.enumeration.DataStatus;
import com.shangpin.pending.product.consumer.common.enumeration.PropertyStatus;
import com.shangpin.pending.product.consumer.common.enumeration.SpuStatus;
import com.shangpin.pending.product.consumer.supplier.dto.SpuPending;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by lizhongren on 2017/2/16.
 */
@Component
public class ObjectConvertCommon {

    public void setSpuPropertyFromHubSpu(SpuPending hubSpuPending, HubSpuDto hubSpuDto) {
        hubSpuPending.setHubBrandNo(hubSpuDto.getBrandNo());
        hubSpuPending.setSpuBrandState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
        hubSpuPending.setHubCategoryNo(hubSpuDto.getCategoryNo());
        hubSpuPending.setCatgoryState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
        hubSpuPending.setHubColorNo(hubSpuDto.getHubColorNo());
        hubSpuPending.setSpuColorState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
        hubSpuPending.setHubSeason(hubSpuDto.getMarketTime() + "_" + hubSpuDto.getSeason());
        hubSpuPending.setSpuSeasonState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
        hubSpuPending.setHubMaterial(hubSpuDto.getMaterial());
        hubSpuPending.setHubOrigin(hubSpuDto.getOrigin());
        hubSpuPending.setOriginState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
        
        if(hubSpuDto.getHubColor()!=null&&hubSpuDto.getHubColor().equals(hubSpuPending.getHubColor())){
        	hubSpuPending.setSpuState(SpuStatus.SPU_WAIT_AUDIT.getIndex().byteValue());
        }else{
        	hubSpuPending.setSpuState(SpuStatus.SPU_WAIT_HANDLE.getIndex().byteValue());
        	hubSpuPending.setAuditDate(new Date());
        	hubSpuPending.setAuditOpinion("再处理：同品牌同货号颜色不一样,hub颜色："+hubSpuDto.getHubColor());
        	hubSpuPending.setMemo("再处理：同品牌同货号颜色不一样，hub颜色："+hubSpuDto.getHubColor());
        	hubSpuPending.setAuditState((byte)0);
        	hubSpuPending.setAuditUser("chenxu");
        }
        hubSpuPending.setIsCurrentSeason(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
        hubSpuPending.setHubSpuNo(hubSpuDto.getSpuNo());
        hubSpuPending.setFilterFlag(FilterFlag.EFFECTIVE.getIndex());
        hubSpuPending.setMaterialState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
        hubSpuPending.setOriginState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
        hubSpuPending.setSpSkuSizeState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
        hubSpuPending.setHubGender(hubSpuDto.getGender());
        hubSpuPending.setSpuGenderState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
        hubSpuPending.setDataState(DataStatus.DATA_STATUS_NORMAL.getIndex().byteValue());
        hubSpuPending.setStockState(StockState.NOSKU.getIndex());
        Date date = new Date();
        hubSpuPending.setCreateTime(date);
        hubSpuPending.setUpdateTime(date);
    }


    /**
     * 将hubSku转换成pendingSku
     * @param hubSku
     * @param pendingSku
     */
    public  void convertHubSkuToPendingSku(HubSupplierSkuDto hubSku, PendingSku pendingSku) throws Exception {
        pendingSku.setSupplierId(hubSku.getSupplierId());
        pendingSku.setSupplierSkuNo(hubSku.getSupplierSkuNo());
        pendingSku.setHubSkuSize(hubSku.getSupplierSkuSize());
        pendingSku.setMarketPrice(hubSku.getMarketPrice());
        pendingSku.setMarketPriceCurrencyorg(hubSku.getMarketPriceCurrencyorg());
        pendingSku.setSalesPrice(hubSku.getSalesPrice());
        pendingSku.setSalesPriceCurrency(hubSku.getSalesPriceCurrency());
        pendingSku.setSkuName(hubSku.getSupplierSkuName());
        pendingSku.setStock(hubSku.getStock());
        pendingSku.setSupplierBarcode(hubSku.getSupplierBarcode());
        pendingSku.setSupplyPrice(hubSku.getSupplyPrice());
        pendingSku.setSupplyPriceCurrency(hubSku.getSupplyPriceCurrency());
    }
}
