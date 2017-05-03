package com.shangpin.ephub.product.business.ui.mapp.model.dto;

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
public class HubBrandModelDicRequestDto {
	
	private int pageNo;
	private int pageSize;
	private String hubCategoryNo;
	private String hubBrandNo;
	private Long brandModelRuleId;
    /**
     * 货号规则
     */
	private String modelRule;
    /**
     * 货号验证正则
     */
    private String modelRex;

    /**
     * 排除特殊字符正则
     */
	private String excludeRex;
    /**
     * 分隔符
     */
	private String formatSeparator;
	private String createUser;
	private String updateUser;
}
