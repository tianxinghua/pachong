package com.shangpin.api.airshop.dto;

import java.io.Serializable;

import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**结算单列表请求列表
 * @author wanghua
 *
 */
@Getter
@Setter
public class PurAccountRQ implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String sopUserNo;
	private String updateTimeBegin;
	private String updateTimeEnd;
	private String sopPurchaseOrderNo;
	private String pageIndex="0";
	private String pageSize="0";
	
	public void converDate(){
		if (StringUtils.isEmpty(this.updateTimeBegin)||StringUtils.isEmpty(this.updateTimeEnd)) {
			this.updateTimeBegin="";
			this.updateTimeEnd="";
		}else {
			this.updateTimeBegin=DeliveryOrdersRQ.covertDate(this.updateTimeBegin);
			this.updateTimeEnd=DeliveryOrdersRQ.covertDate(this.updateTimeEnd);
		}
	}

}
