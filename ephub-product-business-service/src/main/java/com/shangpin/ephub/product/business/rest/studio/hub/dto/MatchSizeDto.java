package com.shangpin.ephub.product.business.rest.studio.hub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

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
	@NotNull(message = "品类编号不可为空")
    private String hubCategoryNo;
    
    /**
     * 尺码值
     */
	@NotNull(message = "尺码不可为空")
    private String size;
}
