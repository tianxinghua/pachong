package com.shangpin.ephub.product.business.rest.common.controller;

import com.shangpin.ephub.product.business.service.studio.studio.StudioCommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**

    通用
 */
@RestController
@RequestMapping(value = "/common-studio")
@Slf4j
public class StudioTimeController {
	
	@Autowired
    StudioCommonService studioService;
	

	@RequestMapping(value = "/gettimelag")
	public Integer getTimeLag(@RequestBody Long studioId){

	 int timeLog = 	studioService.getTimeLog(studioId);
	 return timeLog;
	}



	@RequestMapping(value = "/get-timelag-time")
	public String  getTimeLagTime(@RequestBody Long studioId){

		int timeLog = 	studioService.getTimeLog(studioId);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY,(timeLog * -1) );
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(calendar.getTime());

	}
}
