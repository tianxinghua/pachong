package com.shangpin.api.airshop.service;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Invoice {
	private String ImportType;
	private String InvoiceNo;
	private String MasterTrackNo;
	private String OrderProductCount;
	private String OrderWeightSum;
	private String SendTime;
	private String TotalOrderProductAmount;
	private String TotalOrderProductCount;
	private String TotalSupplierPriceAmount;
	private String TaskBatchNo;
	private String TranshipmentWarehouseID;
	private String LogisticCompanyID;
	

}
