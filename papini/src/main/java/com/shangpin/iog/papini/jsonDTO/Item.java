package com.shangpin.iog.papini.jsonDTO;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {
    private String item_id;
    private String item_size;    
    private String barcode;
    private String color;
    private String stock;
    private String last_modified;
    private List<String> pictures;
    
}
