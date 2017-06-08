package com.shangpin.ephub.data.mysql.sku.purchase.service;

import com.shangpin.ephub.data.mysql.sku.purchase.mapper.PurchaseProductRecordMapper;
import com.shangpin.ephub.data.mysql.sku.purchase.po.PurchaseProductCriteria;
import com.shangpin.ephub.data.mysql.sku.purchase.po.PurchaseProductRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by loyalty on 16/12/27.
 */
@Service
@Slf4j
public class PurchaseProductRecordService {

    @Autowired
    private PurchaseProductRecordMapper purchaseProductRecordMapper;

    public List<PurchaseProductRecord> getProductWithPurchase(PurchaseProductCriteria searchItems){
        return  purchaseProductRecordMapper.getProductWithPurchase(searchItems);
    }
}
