package com.shangpin.ephub.price.consumer.conf.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:Scheduler.java </p>
 * <p>Description: 调度器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月18日 上午10:21:08
 */
@Component
@Slf4j
public class Scheduler {
	


	@Scheduled(cron = "00 33 16 * * ?")
	public void pictureTask() {

	}
}