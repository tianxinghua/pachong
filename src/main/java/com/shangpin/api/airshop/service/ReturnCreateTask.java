package com.shangpin.api.airshop.service;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ReturnCreateTask {

	private String Message;
	private String MessageCode;
	private String Successed;
	private List<WayBillTask> WaybillTaskList;

}
