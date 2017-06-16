package com.shangpin.ephub.data.mysql.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ephub.data.mysql.product.common.ConstantProperty;
import com.shangpin.ephub.data.mysql.product.common.HubSpuUtil;
import com.shangpin.ephub.data.mysql.product.dto.HubPendingDto;
import com.shangpin.ephub.data.mysql.product.dto.SpuModelDto;
import com.shangpin.ephub.data.mysql.product.dto.SpuNoTypeDto;
import com.shangpin.ephub.data.mysql.product.service.PengingToHubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by loyalty on 16/12/28.
 */
@RestController
@RequestMapping("/spuno")
@Slf4j
public class SpuNoController {

    @Autowired
    HubSpuUtil hubSpuUtil;



    @RequestMapping(value = "/getspuno")
    public String  getSpuNo(@RequestBody SpuNoTypeDto dto){
        try {
            if(ConstantProperty.SPU_NO_TYPE_FOR_HUB_SPU.equals(dto.getType())){

               return  hubSpuUtil.createHubSpuNo(1L);
            }else {
                return  hubSpuUtil.createSlotSpuNo(1L);
            }


        } catch (Exception e) {

            log.error("getSpuNo error . Reason : " + e.getMessage(),e);

        }
        return "";
    }




    



}