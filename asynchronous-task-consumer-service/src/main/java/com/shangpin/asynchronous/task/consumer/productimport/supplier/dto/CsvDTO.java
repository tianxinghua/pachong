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
	private String barCode;
	private String size;
	private String proName;
	private String marketPrice;
	private String salePrice;
	private String qty;
	private String made;
	private String desc;
	private String pics;
	private String detailLink;
	private String demo1;
	private String demo2;
	private String demo3;
	private String demo4;
	private String demo5;
	private String demo6;
	private String demo7;
	private String demo8;
	private String demo9;
	private String demo10;
	private String demo11;
	private String demo12;
	private String demo13;

	public String[] getCsvDTO() {
		String [] temp = {"gender","brand","category","spu","barCode","size","proName","marketPrice","salePrice","qty","made","desc","pics","detailLink","demo1",
				"demo2","demo3", "demo4", "demo5","demo6","demo7","demo8","demo9","demo10","demo11","demo12","demo13"};
		return temp;
	}
	
}
