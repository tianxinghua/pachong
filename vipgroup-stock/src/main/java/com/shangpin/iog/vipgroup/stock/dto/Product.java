package com.shangpin.iog.vipgroup.stock.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private String productId;
    private String stock;
    private String listPrice;
    private String sellPrice;
    private String wholesale;
}
