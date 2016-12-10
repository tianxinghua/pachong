package com.shangpin.ep.order.module.supplier.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by lizhongren on 2016/11/29.
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierOfScm {
    private SupplierMsg SupplierInfoApi;
}
