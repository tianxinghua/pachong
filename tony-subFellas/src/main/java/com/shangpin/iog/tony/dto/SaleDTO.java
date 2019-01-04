package com.shangpin.iog.tony.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Created by loyalty on 15/9/11.
 */
@Setter
@Getter
public class SaleDTO {

    String  id;
    String name ;
    String  description;
    List<ImageDTO> editorial;
    String start_datetime;
    String  end_datetime;
    List<String> sku_ids;

}
