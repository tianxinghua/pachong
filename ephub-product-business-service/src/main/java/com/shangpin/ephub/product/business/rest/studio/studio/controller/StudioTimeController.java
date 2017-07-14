package com.shangpin.ephub.product.business.rest.studio.studio.controller;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuDto;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuService;
import com.shangpin.ephub.product.business.service.studio.studio.StudioCommonService;
import com.shangpin.ephub.product.business.ui.pending.util.JavaUtil;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**

    摄影棚时间操作查询

 */
@RestController
@RequestMapping(value = "/studio-time")
@Slf4j
public class StudioTimeController {
	
	@Autowired
	StudioCommonService studioCommonService;
	
	@RequestMapping(value="/get-time-lag" , method=RequestMethod.POST)
	public int  getTimeLag(@RequestBody Long studioId){
		try {
			return  studioCommonService.getTimeLog(studioId);
		} catch (Exception e) {
			log.error("待拍照导入异常："+e.getMessage(),e);
		}
		return 0;

	}


	@RequestMapping(value="/get-time-lag-time" , method=RequestMethod.POST)
	public String   getTimeLagTime(@RequestBody Long studioId){
		try {
			return  studioCommonService.getTimeLogTime(studioId);
		} catch (Exception e) {
			log.error("待拍照导入异常："+e.getMessage(),e);
		}
		return null;

	}


	

}
