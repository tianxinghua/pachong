package com.shangpin.pending.product.consumer.supplier.common;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.studio.dic.dto.HubDicStudioBrandCriteriaDto;
import com.shangpin.ephub.client.data.mysql.studio.dic.dto.HubDicStudioBrandDto;
import com.shangpin.ephub.client.data.mysql.studio.dic.gateway.HubDicStudioBrandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lizhongren on 2017/2/13.
 */
@Component
public class DataOfPendingServiceHandler {
    @Autowired
    private HubSpuPendingGateWay hubSpuPendingGateWay;

    @Autowired
    private HubSkuPendingGateWay hubSkuPendingGateWay;

    @Autowired
    private HubDicStudioBrandGateway studioBrandGateway;


    public int getStockTotalBySpuPendingId(Long spuPendingId){

       return hubSkuPendingGateWay.sumStockBySpuPendingId(spuPendingId);

    }


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



}
