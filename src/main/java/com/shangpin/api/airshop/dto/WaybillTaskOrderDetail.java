package com.shangpin.api.airshop.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WaybillTaskOrderDetail {
	

	private String MTaskBatchNo;
	private String OrderNo;
	private String OrderProductCount;
	private String OrderProductAmount;
	private String OrderWeightSum;
	private String CTaskBatchNo;
	private String PackageSequenceNumber;
	
	
	private String TrackNo;
	private String SendErrorMsg;
	private String IsRequestSuccess;
	private String PlaneSinglePath;
	private String RequestTimes;
	private String OutboundTime;
	private String SupplierOrderNO;
	private String WaybillTaskOrderDetailID;

}
