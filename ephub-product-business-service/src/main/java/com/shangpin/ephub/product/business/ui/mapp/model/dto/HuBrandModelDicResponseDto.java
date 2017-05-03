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
public class HuBrandModelDicResponseDto {
	
	private Long brandModelRuleId;
	private String hubBrandNo;
	private String hubCategoryNo;
	private String modelRulel;
    private String modelRex;
	private String excludeRex;
	private String formatSeparator;
	private String updateTime;
	private String createTime;
	private String updateUser;
}
