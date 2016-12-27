package com.shangpin.ephub.product.business.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.product.business.common.util.HubSpuUtil;

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

    @Autowired
    HubSpuUtil hubSpuUtil;



    public void saveSpuAndSku(HubSpuPendingDto spuPendingDto, List<HubSkuPendingDto> skuPendingDtos){
        //查询是否存在
        if(isExistByBrandNoAndProductModelAndColor(spuPendingDto.getHubBrandNo(),spuPendingDto.getSpuModel(),"")){
            //存在 ，判断SKU是否存在 存在 关联供货商的SKU  不存在  获取新的SKU编号 添加sku消息 记录供应商关系

        }else{

            //不存在 获取spu编号 添加SPU和SKU

        }



    }


    public String getHubSpuNo(){
        //先从redis里取值
       String spuNo =  hubSpuUtil.createHubSpuNo(0L);
       if(StringUtils.isBlank(spuNo)){//redis 出现问题 从数据库中获取最大值


       }
       return spuNo;
    }



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

    /**
     * 获取最大的hubSpuNo
     * @return
     */
    private Long getMaxHubSpuNo(){
        HubSpuCriteriaDto criteria = new HubSpuCriteriaDto();
        return 0L;
    }





}
