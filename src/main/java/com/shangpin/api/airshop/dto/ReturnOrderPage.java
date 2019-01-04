package com.shangpin.api.airshop.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 返货列表
 * Created by ZRS on 2016/1/14.
 */
@Setter
@Getter
@ToString
public class ReturnOrderPage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int total;
	private List<ReturnOrder> orderSendReturnList;
}
