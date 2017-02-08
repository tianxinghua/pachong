package com.shangpin.ephub.product.business.rest.size.dto;

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
public class MatchSizeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2215182249308660796L;

    /**
     * 尚品品牌编号
     */
	@NotNull(message = "品牌编号不可为空")
    private String hubBrandNo;
    
    /**
     * 尚品品类编号
     */
    private String hubCategoryNo;
    
    /**
     * 尺码值
     */
    private String size;
}
