package com.shangpin.iog.productweb.schedule;

import java.util.Date;





import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;







public class Task {
	
	private Long id; //主键
	
	private String queryKeyAndValue;//查询条件
	
	private TaskState        state=TaskState.INIT;//任务状态
	
	private Date        createTime=new Date();//任务创建时间
	
	private String handerExpression;//任务处理者表达式
	private String   cronExpression;//Cron表达式
	
	private String           remark;//简要说明
	
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TaskState getState() {
		return state;
	}

	public void setState(TaskState state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getHanderExpression() {
		return handerExpression;
	}

	public void setHanderExpression(String handerExpression) {
		this.handerExpression = handerExpression;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	public String getQueryKeyAndValue() {
		return queryKeyAndValue;
	}

	public void setQueryKeyAndValue(String queryKeyAndValue) {
		this.queryKeyAndValue = queryKeyAndValue;
	}

	/**
	 * 获取任务执行者对象
	 * 
	 * @param context
	 * @return
	 */
	public TaskHanderService getHander(ApplicationContext context){
		
		Object hander = context.getBean(getHanderExpression());
		
		
		if(!TaskHanderService.class.isAssignableFrom(hander.getClass())){
			throw new RuntimeException(getHanderExpression()+"must implements com.guahaoe.system.service.CronHander interface!");
		}
		
		return (TaskHanderService)hander;
	}
	
	/**
	 * 获取任务触发器
	 * @return
	 */
	public Trigger getTrigger(){
		return new CronTrigger(getCronExpression());
	}
}
