package com.shangpin.iog.meifeng.dto;

public class SteImg {
	private String productModel;
	private String color;
	private String url1;
	private String url2;
	private String url3;
	private String url4;
	private String url5;
	private String url6;
	private String url7;
	private String url8;
	
	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getUrl1() {
		return url1;
	}

	public void setUrl1(String url1) {
		this.url1 = url1;
	}

	public String getUrl2() {
		return url2;
	}

	public void setUrl2(String url2) {
		this.url2 = url2;
	}

	public String getUrl3() {
		return url3;
	}

	public void setUrl3(String url3) {
		this.url3 = url3;
	}

	public String getUrl4() {
		return url4;
	}

	public void setUrl4(String url4) {
		this.url4 = url4;
	}

	public String getUrl5() {
		return url5;
	}

	public void setUrl5(String url5) {
		this.url5 = url5;
	}

	public String getUrl6() {
		return url6;
	}

	public void setUrl6(String url6) {
		this.url6 = url6;
	}

	public String getUrl7() {
		return url7;
	}

	public void setUrl7(String url7) {
		this.url7 = url7;
	}

	public String getUrl8() {
		return url8;
	}

	public void setUrl8(String url8) {
		this.url8 = url8;
	}

	public String[] getIngArr(){
		return new String[]{url1,url2,url3,url4,url5,url6,url7,url8};
	}
}
