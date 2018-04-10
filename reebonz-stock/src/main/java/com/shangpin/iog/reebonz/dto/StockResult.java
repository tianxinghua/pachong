package com.shangpin.iog.reebonz.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by lizhongren on 2018/4/9.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockResult implements Serializable {

    String supplierSkuNo;
    String size;
    Integer stock;

}
