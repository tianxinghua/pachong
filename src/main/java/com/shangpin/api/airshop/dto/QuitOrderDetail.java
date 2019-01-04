package com.shangpin.api.airshop.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by ZRS on 2016/1/14.
 */
@Setter
@Getter
@ToString
public class QuitOrderDetail implements Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JSONField(name = "SopSecondReturnOrderNo")
    private String sopSecondReturnOrderNo;
    @JSONField(name = "SkuNo")
    private String skuNo;
    @JSONField(name = "BarCode")
    private String barCode;
    @JSONField(name = "ProductModel")
    private String productModel;
    @JSONField(name = "BrandNo")
    private String brandNo;
    @JSONField(name = "BrandName")
    private String brandName;
    @JSONField(name = "SupplierSkuNo")
    private String supplierSkuNo;
    @JSONField(name = "SopPurchaseOrderNo")
    private String sopPurchaseOrderNo;
    @JSONField(name = "Memo")
    private String memo="";

    private String size;
    private String productMsg;
}
