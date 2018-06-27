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
	private String marketPrice;
	private String salePrice;
	private String qty;
	private String made;
	private String desc;
	private String pics;
	private String detailLink;

	public String[] getCsvDTO() {
		String [] temp = {"gender","brand","category","spu","productModel","season","material","color","size","proName","marketPrice","salePrice","qty","made","desc","pics","detailLink"};
		return temp;
	}
	
}
