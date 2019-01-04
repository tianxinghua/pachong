package com.shangpin.api.airshop.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnWaybillTaskOrderDetail {
	
	  private String Message;

	    //时间戳
	    private String MessageCode;
	    private String Successed;
	    private List<WaybillTaskOrderDetail> WaybillTaskOrderDetailList;

}
