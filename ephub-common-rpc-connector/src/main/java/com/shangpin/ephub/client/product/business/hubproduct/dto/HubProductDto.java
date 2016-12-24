package com.shangpin.ephub.client.product.business.hubproduct.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubProductDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2215182249308660796L;
	private String supplierNo;
	/**
	 * 货号
	 */
	private String spuModel;
    /**
     * 品牌编号
     */
    private String brandNo;
    /**
     * 类目编号
     */
    private String categoryNo;
    /**
     * 性别
     */
    private String gender;
    /**
     * 颜色
     */
    private String color;
    private String size;
    private String material;
    private String madeIn;
    private String season;
    private String seasonYear;
}
