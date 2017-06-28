package com.shangpin.pending.product.consumer.conf.schedule;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shangpin.pending.product.consumer.service.HubFilterService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:Scheduler.java </p>
 * <p>Description: 调度器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2017年1月18日 上午10:21:08
 */
@Component
@Slf4j
public class Scheduler {
	
	@Autowired
	HubFilterService hubFilterService;
	@Scheduled(cron = "05 0 07 * * ?")
	public void modelTask() {
		try {
			List<String> categoryList = new ArrayList<String>();
			categoryList.add("A01B01");
			categoryList.add("A01B02");
			categoryList.add("A02B01");
			categoryList.add("A02B02");
			categoryList.add("A03B01");
			categoryList.add("A03B02");
			categoryList.add("A11");
			for(String category:categoryList){
				hubFilterService.refreshHubFilter(category);
			}
		} catch (Throwable e) {
			log.info("=======系统扫描同款需要重新推送的数据事件发生异常======",e);
			e.printStackTrace();
		}
	}
}