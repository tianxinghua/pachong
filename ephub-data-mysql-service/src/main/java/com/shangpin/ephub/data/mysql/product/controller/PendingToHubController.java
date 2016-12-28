package com.shangpin.ephub.data.mysql.product.controller;

import com.shangpin.ephub.data.mysql.product.dto.SpuPendingAuditDto;
import com.shangpin.ephub.data.mysql.sku.pending.bean.HubSkuPendingCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.sku.pending.po.HubSkuPending;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by loyalty on 16/12/28.
 */
@RestController
@RequestMapping("/penging-to-hub")
public class PendingToHubController {

    @RequestMapping(value = "/create-hubspu-and-hubsku")
    public boolean auditPending(@RequestBody SpuPendingAuditDto dto){
        return false;
    }
}