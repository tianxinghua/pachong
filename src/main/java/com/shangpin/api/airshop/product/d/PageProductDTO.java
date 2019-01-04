
package com.shangpin.api.airshop.product.d;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by loyalty on 15/9/15.
 * 产品信息
 */
@Setter
@Getter
public class PageProductDTO implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -3125067250395773889L;

	private String Status;
	private String barcode;
	private String brandName;
	private String SpStatus;
	private String Memo;
	private String ProductCode;
	private String productName;
	private String SkuId;
	private String spSkuId;
	
	private String color;//颜色 

	private String size;//尺码 
	
	private String categoryName;
	
	private String sizeClass;
	private String supplierId;
	private Integer pageIndex;
	private Integer pageSize;
	
}

