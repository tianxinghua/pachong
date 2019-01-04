package com.shangpin.api.airshop.dto.request;

import java.io.Serializable;

import org.springframework.util.StringUtils;

import com.shangpin.api.airshop.dto.DeliveryOrdersRQ;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonPurchaseRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sopUserNo;
	private String supplierNo;
	private String productModel;
	private String pageIndex="0";
	private String pageSize="0";
	private String sopPurchaseOrderNo;
	private String logisticsOrderNo;
	private String sopDeliverOrderNo;
	private String barCode;
	private String importType;
	private String supplierSkuNo;
	private Object isStock; //库存检查标记 0=未标记 1 = 有货 2 = 缺货
	private Object detailStatus; //采购单状态 1=待处理，2=待发货，3=已发货，待收货，4=待补发，5=已取消，6=已完成,7=缺货
	private String updateTimeBegin;
	private String updateTimeEnd;
	private byte OverTimeStatus; //默认0  1=超过确认时间，2=超过发货时间
	private String brandNo; //品牌名称编号
	private byte mVPSort=0; //是否按MVP排序置顶 1排序，0不排序


	public void converDate(){
		if (StringUtils.isEmpty(this.updateTimeBegin)||StringUtils.isEmpty(this.updateTimeEnd)) {
			this.updateTimeBegin="";
			this.updateTimeEnd="";
		}else {
			this.updateTimeBegin=DeliveryOrdersRQ.covertDate(this.updateTimeBegin);
			this.updateTimeEnd=DeliveryOrdersRQ.covertDate(this.updateTimeEnd);
			
			/*this.updateTimeBegin=DeliveryOrdersRQ.covertDate(this.updateTimeBegin);
			this.updateTimeEnd=DeliveryOrdersRQ.covertDate(this.updateTimeEnd);*/
		}
	}

}
