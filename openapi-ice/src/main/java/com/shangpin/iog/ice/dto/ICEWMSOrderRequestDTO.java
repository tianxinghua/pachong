package com.shangpin.iog.ice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by lizhongren on 2015/10/3.
 */
@Getter
@Setter
public class ICEWMSOrderRequestDTO {
    private String WarehouseNo;
    private String SupplierNo; //供货商编号 非门户ID
    private List<String> SkuList;
    private List<String> ProductList;
    private String BeginTime;
    private String EndTime;
    private String FormTypeName;



}
