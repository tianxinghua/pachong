package com.shangpin.ephub.data.mysql.sku.purchase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ephub.data.mysql.product.dto.HubPendingDto;
import com.shangpin.ephub.data.mysql.product.dto.SpuModelDto;
import com.shangpin.ephub.data.mysql.product.service.PengingToHubService;
import com.shangpin.ephub.data.mysql.sku.purchase.po.PurchaseProductCriteria;
import com.shangpin.ephub.data.mysql.sku.purchase.po.PurchaseProductRecord;
import com.shangpin.ephub.data.mysql.sku.purchase.service.PurchaseProductRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by loyalty on 16/12/28.
 */
@RestController
@RequestMapping("/purchase-product")
@Slf4j
public class PurchaseProductRecordController {

    @Autowired
    PurchaseProductRecordService purchaseProductRecordService;

    @RequestMapping(value = "/get-purchase-product")
    public List<PurchaseProductRecord> getProductWithPurchase(@RequestBody PurchaseProductCriteria querySearch){
        return  purchaseProductRecordService.getProductWithPurchase(querySearch);
    }

}