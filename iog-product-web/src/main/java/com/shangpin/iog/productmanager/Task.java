package com.shangpin.iog.productmanager;

import java.util.Date;







import lombok.Getter;
import lombok.Setter;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
@Setter
@Getter
public class Task {
	
	private String supplierId; //主键
	
	private TaskState state=TaskState.INIT;//任务状态
	
	private Date createTime=new Date();//任务创建时间
	
	private String handerExpression;//任务处理者表达式
	private String cronExpression;//Cron表达式

	/**
	 * 获取任务触发器
	 * @return
	 */
	public Trigger getTrigger(){
		return new CronTrigger(getCronExpression());
	}
}
