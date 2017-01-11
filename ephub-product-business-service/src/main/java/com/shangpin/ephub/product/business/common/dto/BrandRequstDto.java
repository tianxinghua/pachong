package com.shangpin.ephub.product.business.common.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BrandRequstDto {

	@JsonIgnore
	public String brandNo;  //单个查询
	@JsonIgnore
    public List<String> brandNos;//编号查询
	@JsonIgnore
    public String brandEnName; //品牌英文名称
	
    @JsonProperty("BrandNo")
	public String getBrandNo() {
		return brandNo;
	}
	 @JsonProperty("BrandNo")
	public void setBrandNo(String brandNo) {
		this.brandNo = brandNo;
	}
	 @JsonProperty("BrandNos")
	public List<String> getBrandNos() {
		return brandNos;
	}
	 @JsonProperty("BrandNos")
	public void setBrandNos(List<String> brandNos) {
		this.brandNos = brandNos;
	}
	 @JsonProperty("BrandEnName")
	public String getBrandEnName() {
		return brandEnName;
	}
	 @JsonProperty("BrandEnName")
	public void setBrandEnName(String brandEnName) {
		this.brandEnName = brandEnName;
	}
    
		
}
