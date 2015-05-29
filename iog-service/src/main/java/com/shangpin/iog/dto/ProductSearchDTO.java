package com.shangpin.iog.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 15/5/27.
 */
@Getter
@Setter
public class ProductSearchDTO {
    private String startDate;
    private String endDate;
    private Integer pageIndex;
    private Integer pageSize;
    private String category;
}
