package com.shangpin.api.airshop.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**结算单列表返回信息
 * @author wanghua
 *
 */
@Getter
@Setter
public class PurAccountRS implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String total;
	private List<PurAccountContent> purchaseOrderDetails;

}
