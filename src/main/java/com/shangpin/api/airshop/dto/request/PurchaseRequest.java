package com.shangpin.api.airshop.dto.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 采购单查询请求参数实体
 * @author qinyingchun
 *
 */
@Getter
@Setter
public class PurchaseRequest extends CommonPurchaseRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String isDoStock;//库存检查标记确认 1 待确认 2 已确认

}
