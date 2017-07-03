package com.shangpin.ephub.product.business.rest.studio.hub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuService;
import com.shangpin.ephub.product.business.ui.pending.util.JavaUtil;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;

import lombok.extern.slf4j.Slf4j;

/**

   slotSpu 业务查询

 */
@RestController
@RequestMapping(value = "/hub-slot-spu")
@Slf4j
public class HubSlotSpuController {
	
	@Autowired
	HubSlotSpuService hubSlotSpuService;
	
	@RequestMapping(value="/add-slot-spu" , method=RequestMethod.POST)
	public boolean addSlotSpuAndSupplier(@RequestBody HubSpuPendingDto hubSpuPendingDto){
		try {
			PendingProductDto pendingProductDto = new PendingProductDto();
			JavaUtil.fatherToChild(hubSpuPendingDto, pendingProductDto); 
			return hubSlotSpuService.addSlotSpuAndSupplier(pendingProductDto );
		} catch (Exception e) {
			log.error("待拍照导入异常："+e.getMessage(),e);
		}
		return false;
	}
	

}
