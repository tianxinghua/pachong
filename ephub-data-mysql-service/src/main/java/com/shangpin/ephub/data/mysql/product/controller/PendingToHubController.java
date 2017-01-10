package com.shangpin.ephub.data.mysql.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.product.dto.HubPendingDto;
import com.shangpin.ephub.data.mysql.product.dto.SpuModelDto;
import com.shangpin.ephub.data.mysql.product.service.PengingToHubService;

import lombok.extern.slf4j.Slf4j;

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