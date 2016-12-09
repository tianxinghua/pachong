package com.shangpin.ep.order.module.mapping.service.impl;

import com.shangpin.ep.order.module.mapping.bean.HubSkuRelation;
import com.shangpin.ep.order.module.mapping.bean.HubSkuRelationCriteria;
import com.shangpin.ep.order.module.mapping.mapper.HubSkuRelationMapper;
import com.shangpin.ep.order.module.mapping.service.IHubSkuRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>Title:HubSkuRelationService.java </p>
 * <p>Description: HUB订单系统商品sku和供货商sku之间的映射关系接口规范实现</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午2:01:31
 */
@Service
public class HubSkuRelationService implements IHubSkuRelationService {
    @Autowired
    HubSkuRelationMapper hubSkuRelationMapper;

    @Override
    public HubSkuRelation getSkuRelationBySupplierIdAndSpSkuNo(String supplierId, String spSkuNo) {
        HubSkuRelationCriteria criteria = new HubSkuRelationCriteria();
        criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSopSkuIdEqualTo(spSkuNo);
        List<HubSkuRelation> hubSkuRelations = hubSkuRelationMapper.selectByExample(criteria);
        if(null!=hubSkuRelations&&hubSkuRelations.size()>0){
            return hubSkuRelations.get(0);
        }
        return null;
    }
}
