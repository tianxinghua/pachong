package com.shangpin.iog.biondini.dao;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by lizhongren on 2018/4/3.
 */
@Getter
@Setter
public class Product implements Serializable {
    private String spuNo;
    private String picUrl;
    private String supplierSpuModel;
}
