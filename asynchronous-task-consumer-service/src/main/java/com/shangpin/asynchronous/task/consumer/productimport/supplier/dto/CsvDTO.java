package com.shangpin.asynchronous.task.consumer.productimport.supplier.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CsvDTO {
//	Gender	brand	category	SPU	barCode	size	proName	市场价	
//	售价	qty	made	desc	pics	detailLink	demo1	demo2	
//	demo3	demo4	demo5	demo6	demo7	demo8	demo9	demo10	demo11	demo12	demo13			

	private String gender;
	private String brand;
	private String category;
	private String spu;
	private String productModel;
    private String season;
	private String material;
	private String color;
	private String size;
	private String proName;
	private String foreignMarketPrice;//国外市场价
	private String domesticMarketPrice;//国内市场价
	private String qty;
	private String made;
	private String desc;
	private String pics;
	private String detailLink;
	private String measurement;
	private String supplierId;
	private String supplierNo;
	private String supplierSkuNo;

	public String[] getCsvDTO() {
		String [] temp = {"gender","brand","category","spu","productModel","season","material","color","size","proName","foreignMarketPrice","domesticMarketPrice","qty","made","desc","pics","detailLink","measurement","supplierId","supplierNo","supplierSkuNo"};
		return temp;
	}
	
}
