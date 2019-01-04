package com.shangpin.api.airshop.dto.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author 商品列表实体
 *
 */
@Getter
@Setter
public class ProductListRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String itemName;// 商品名称
	
	private String itemCode;//商品编码
	
	
	private String shangpinSKU;
	
	private String brand;//品牌
	
	private String editStatus;//修改状态
	
	private String shelveStatus;//
	private String supplierSKU;//
    
	
	private String image;
	
	private String price;
	
	private String updateTime;
	
    private String color;//颜色 必须

    private String size;//尺码 必须
	
//	private Long spuId;
	private String spuId;
	
	private String categoryName;
}
