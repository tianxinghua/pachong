package com.shangpin.ephub.data.mysql.product.controller;

import com.shangpin.ephub.data.mysql.product.common.HubSpuUtil;
import com.shangpin.ephub.data.mysql.product.dto.HubPendingDto;
import com.shangpin.ephub.data.mysql.product.dto.SpuModelDto;
import com.shangpin.ephub.data.mysql.product.dto.SpuPendingAuditDto;
import com.shangpin.ephub.data.mysql.product.service.PengingToHubService;
import com.shangpin.ephub.data.mysql.sku.pending.bean.HubSkuPendingCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.sku.pending.po.HubSkuPending;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by loyalty on 16/12/28.
 */
@RestController
@RequestMapping("/penging-to-hub")
@Slf4j
public class PendingToHubController {

    @Autowired
    PengingToHubService pengingToHubService;



    @RequestMapping(value = "/create-hubspu-and-hubsku")
    public boolean auditPending(@RequestBody SpuModelDto dto){
        try {
            log.debug("spumodeldto = "+ dto.toString());
            pengingToHubService.auditPending(dto);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("audit error . Reason : " + e.getMessage(),e);
            return false;
        }
        return true;
    }


    @RequestMapping(value = "/add-sku-or-skusuppliermapping")
    public boolean addSkuOrSkuSupplierMapping(@RequestBody HubPendingDto dto){
        try {
            log.debug("HubPendingDto = "+ dto.toString());

        } catch (Exception e) {
            e.printStackTrace();
            log.error("audit error . Reason : " + e.getMessage(),e);
            return false;
        }
        return true;
    }






}