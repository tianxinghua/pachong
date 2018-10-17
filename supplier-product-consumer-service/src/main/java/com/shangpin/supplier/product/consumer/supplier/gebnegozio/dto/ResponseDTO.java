package com.shangpin.supplier.product.consumer.supplier.gebnegozio.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowenjun on 2018/9/8.
 */
@Getter
@Setter
public class ResponseDTO {
    private List<GebnegozioDTO> items = new ArrayList<GebnegozioDTO>();//产品
    private SearchCriteria search_criteria;
    private String total_count;
}