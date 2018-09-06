package com.shangpin.ephub.product.business.rest.picture.service.impl;

import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSkuSupplierMappingGateWay;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPicDto;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPicGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.product.business.rest.picture.service.ISelectHubSpuPicService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowenjun on 2018/9/1.
 */
@Service
@Slf4j
public class SelectHubSpuPicServiceImpl implements ISelectHubSpuPicService{

    @Autowired
    private HubSkuSupplierMappingGateWay hubSkuSupplierMappingGateWay;

    @Autowired
    private HubSpuGateWay hubSpuGateWay;

    @Autowired
    private HubSpuPicGateWay hubSpuPicGateWay;

    @Override
    public List<String> selectHubSpuPic(String spSpuNo) {
        //用sp_sku_no（sp_spu_no拼接001）去hub_sku_supplier_mapping中查sku_no
        HubSkuSupplierMappingCriteriaDto criteria = new HubSkuSupplierMappingCriteriaDto();
        HubSkuSupplierMappingCriteriaDto.Criteria criterionMapping = criteria.createCriteria();
        if (StringUtils.isNotBlank(spSpuNo)){
            String spSkuNo = spSpuNo.concat("001");

            criterionMapping.andSpSkuNoEqualTo(spSkuNo);
        }
        List<HubSkuSupplierMappingDto> hubSkuSupplierMappingDtos = hubSkuSupplierMappingGateWay.selectByCriteria(criteria);
        String skuNo = null;
        if ( null != hubSkuSupplierMappingDtos && !hubSkuSupplierMappingDtos.isEmpty()){
            skuNo = hubSkuSupplierMappingDtos.get(0).getSkuNo();
        }
        log.info("========查询到的sku_no为：" + skuNo + "======mapping集合中的数据：" + hubSkuSupplierMappingDtos);
        //用spu_no（sku_no去掉后三位）去hub_spu查spuId
        HubSpuCriteriaDto hubSpuCriteria = new HubSpuCriteriaDto();
        HubSpuCriteriaDto.Criteria criterionSpu = hubSpuCriteria.createCriteria();
        if (StringUtils.isNotBlank(skuNo)){
            String spuNo = skuNo.substring( 0 , skuNo.length()-3 );
            criterionSpu.andSpuNoEqualTo(spuNo);
        }
        List<HubSpuDto> hubSpuDtos = hubSpuGateWay.selectByCriteria(hubSpuCriteria);
        Long spuId = null;
        if ( null != hubSpuDtos && !hubSpuDtos.isEmpty()){
            spuId = hubSpuDtos.get(0).getSpuId();
        }
        log.info("========查询到的spu_id为：" + spuId + "======hubSpu集合中的数据：" + hubSpuDtos);
        //用spuId去hub_spu_pic查pic_url
        HubSpuPicCriteriaDto hubSpuPicCriteria = new HubSpuPicCriteriaDto();
        HubSpuPicCriteriaDto.Criteria criterionPic = hubSpuPicCriteria.createCriteria();
        if(null != spuId ){
            criterionPic.andSpuIdEqualTo(spuId);
        }
        List<HubSpuPicDto> hubSpuPicDtos = hubSpuPicGateWay.selectByCriteria(hubSpuPicCriteria);
        List<String> picUrlLists = new ArrayList<String>();
        if ( null != hubSpuPicDtos && !hubSpuPicDtos.isEmpty()){
            for (HubSpuPicDto hubSpuPicDto : hubSpuPicDtos) {
                picUrlLists.add(hubSpuPicDto.getSpPicUrl());
            }
        }
        log.info("========获取到的图片集合为：" + picUrlLists);
        log.info("========hub_spu_pic集合中数据为：" + hubSpuPicDtos);
        return picUrlLists;
    }
}
