package com.shangpin.ephub.product.business.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class BrandRequstDto {

	//@JsonIgnore
	public String brandNo;  //单个查询
    public List<String> BrandNos;//编号查询
    public String BrandEnName; //品牌英文名称
    
		
}
