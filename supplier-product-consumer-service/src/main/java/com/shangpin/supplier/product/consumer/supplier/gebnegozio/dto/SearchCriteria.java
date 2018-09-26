package com.shangpin.supplier.product.consumer.supplier.gebnegozio.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by zhaowenjun on 2018/9/8.
 */
@Getter
@Setter
public class SearchCriteria {
    private List<String> filterGroups;
    private String pageSize;
    private String currentPage;
}
