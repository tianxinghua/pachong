package com.shangpin.ephub.data.mysql.modifyDataStatus.state.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.modifyDataStatus.state.service.ModifyDataStatusService;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Administrator on 2017/6/8.
 */
@RestController
@RequestMapping("/modifyDataStatus")
@Slf4j
public class ModifyDataStatusController {
	
	@Autowired
	private ModifyDataStatusService modifyDataStatusService;

    @RequestMapping(value = "/updateStatus")
    public void updateStatus(){
    	log.info("-------start updateStatus---------");
    	try {
    		modifyDataStatusService.updateStatus();
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    	log.info("-------end updateStatus---------");
    }
    
    @RequestMapping(value = "/updatePicStatus")
    public void updatePicStatus(){
    	log.info("-------start updatePicStatus---------");
    	try {
    		modifyDataStatusService.updatePicStatus();
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	log.info("-------end updatePicStatus---------");
    }
    
    @RequestMapping(value = "/updateNewPicStatus")
    public void updateNewPicStatus(){
    	log.info("-------start updateNewPicStatus---------");
    	try {
    		modifyDataStatusService.updateNewPicStatus();
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	log.info("-------end updateNewPicStatus---------");
    }
}
