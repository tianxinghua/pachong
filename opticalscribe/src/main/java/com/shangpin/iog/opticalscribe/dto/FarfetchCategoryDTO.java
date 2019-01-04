package com.shangpin.iog.opticalscribe.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 14:38 2018/7/16
 * @Description:
 */
@Setter
@Getter
@ToString
public class FarfetchCategoryDTO {

    private String categoryName;

    private String categoryId;

    private String parentCategoryName;

    public FarfetchCategoryDTO(String parentCategoryName,String categoryName, String categoryId) {
        this.categoryName = categoryName;
        this.categoryId = categoryId;
        this.parentCategoryName = parentCategoryName;
    }
}
