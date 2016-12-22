package com.shangpin.ephub.product.business.rest.checkHub.dao;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

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
    private String color;
    private String material;
    private String madeIn;
}
