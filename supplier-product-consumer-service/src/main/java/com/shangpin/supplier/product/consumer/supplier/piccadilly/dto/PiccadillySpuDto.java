package com.shangpin.supplier.product.consumer.supplier.piccadilly.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PiccadillySpuDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5465101223592615698L;
	
	private String id;
    private String supplierId;//供货商ID 必填
    private String spuId;//SPUID  必填
    private String spuName;
    /**性别分类*/
    private String categoryGender;//必填
    private String categoryId;
    /**大类*/
    private String categoryName;//必填
    private String subCategoryId;
    /**小类*/
    private String subCategoryName;
    private String brandId;
    private String seasonId;//上市季节ID
    private String brandName;//必填
    private String seasonName;
    private String picUrl;//图片地址
    private String material;//材质 必填
    private String productOrigin;//产地
    private String memo;
    private String newseasonId;
    private String newseasonName;
    private String spCategory;
    private String spBrand;
	
	private List<String> pictures;
	
	private List<PiccadillySkuDto> skus;
	
	private String color;
	
	private String productModel;

}
