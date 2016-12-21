package com.shangpin.ephub.client.product.business.model.dto;

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
public class BrandModelDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2215182249308660796L;

    /**
     * 尚品品牌编号
     */
    private String hubBrandNo;
    
    /**
     * 尚品品类编号
     */
    private String hubCategoryNo;
    
    /**
     * 品牌型号
     */
    private String brandMode;
}
