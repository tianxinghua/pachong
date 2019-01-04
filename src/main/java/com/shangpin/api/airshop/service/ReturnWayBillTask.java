package com.shangpin.api.airshop.service;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReturnWayBillTask {
	  private String Message;

	    //时间戳
	    private String MessageCode;
	    private String Successed;
	    private List<WayBillTask> WaybillTaskList;
}
