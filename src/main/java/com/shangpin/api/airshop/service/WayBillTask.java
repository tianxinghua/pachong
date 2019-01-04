package com.shangpin.api.airshop.service;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class WayBillTask {

	private String ImportType;
	private String IsPrintInvoices;
	private String LogisticCompanyID;
	private String LogisticCompanyName;
	private String MasterTrackNo;
	private String OrderCount;
	private String OrderProductAmount;
	
	
	private String OrderProductCount;
	private String OrderWeightSum;
	private String OutBoundEndTime;
	private String OutBoundStartTime;
	private String SendTime;
	private String TaskBatchNo;
	private String TaskStatus;
	private String TaskType;
	private String TranshipmentWarehouseID;
	private String WarehouseName;
	private String WaybillTaskID;
}
