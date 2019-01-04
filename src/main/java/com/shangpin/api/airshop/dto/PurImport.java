package com.shangpin.api.airshop.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**结算单列表请求列表
 * @author wanghua
 *
 */
@Getter
@Setter
public class PurImport implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String SopPurchaseOrderDetailNo;
	private String SopPurchaseOrderNo;
	private String SupplierSkuNo;
	private int IsStock;
	private String Memo;
}
