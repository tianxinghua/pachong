package com.shangpin.ephub.product.business.ui.studio.hub.controller;

import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**

 slotSpu 业务查询

 */
@RestController
@RequestMapping(value = "/hub-slot-spu")
@Slf4j
public class HubSlotSpuUIController {

    @Autowired
    HubSlotSpuService hubSlotSpuService;


}