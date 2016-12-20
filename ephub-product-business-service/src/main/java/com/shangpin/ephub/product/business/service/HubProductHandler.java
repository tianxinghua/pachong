package com.shangpin.ephub.product.business.service;

import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.stereotype.Service;

/**
 * Created by loyalty on 16/12/20.
 */
@Service
@EnableDiscoveryClient
@EnableFeignClients("com.shangpin.ephub")
public class HubProductHandler {

    @Autowired
    HubSpuGateWay hubSpuGateWay;

    @Autowired
    HubSkuGateWay hubSkuGateWay;



    /**
     * 判断hubSPU是否存在
     * @param brandNo
     * @param spuModle
     * @param color
     * @return
     */
    public boolean isExistByBrandNoAndProductModelAndColor(String brandNo,String spuModle,String color){
        boolean result = false;
        HubSpuCriteriaDto criteria = new HubSpuCriteriaDto();
        criteria.createCriteria().andBrandNoEqualTo(brandNo).andSpuModelEqualTo(spuModle)
                ;
        if(hubSpuGateWay.countByCriteria(criteria)>0){
            result = true;
        }


        return result;
    }



}
