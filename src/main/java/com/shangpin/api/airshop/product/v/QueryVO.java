package com.shangpin.api.airshop.product.v;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class QueryVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6332522587378104937L;
	
	private  Integer pageCode;
	
	private Integer pageIndex;
	
	private Integer pageSize;// add
	
	private String itemName;
	
	private String itemCode;
	
	private String supplierSku;
	
	private String shangpinSKU;
	
	private String brand;
	
	private String barCode;
	
	private String from;
	
	private String to;
	
	private Integer editStatus;
	
	private Integer shelveStatus;
	
	
	
    private String color;//颜色 必须

    private String size;//尺码 必须
    
    private String sizeClass;//尺码下拉分类
	
	private String category;
	private Long supplierSpuId;
	private Long supplierSkuId;

}
