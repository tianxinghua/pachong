package com.shangpin.iog.pozzilei.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Sku {


    private String item_id;
    private String item_size;
    private String barcode;
    private String color;
    private String stock;
    private List<String> pictures;

}
