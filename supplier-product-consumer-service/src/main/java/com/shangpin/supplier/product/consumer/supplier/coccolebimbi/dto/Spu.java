package com.shangpin.supplier.product.consumer.supplier.coccolebimbi.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Spu{
	private String spuId;//SPUID  必填
    private String spuName;
    private String gender;//必填
    private String categoryName;//必填
    private String brandName;//必填
    private String seasonName;
    private String material;//材质 必填
    private String productOrigin;//产地
    private String productCode;
    private String color;// 颜色 必填
    private String productDescription;//描述
	private List<Sku> skus;
	private List<String> images;

}
