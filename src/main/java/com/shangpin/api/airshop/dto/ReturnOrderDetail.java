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
public class ReturnOrderDetail implements Serializable {

    @JSONField(name = "SopReturnOrderNo")
    private String sopReturnOrderNo;    //返货单号
    @JSONField(name = "SkuNo")
    private String skuNo;    //返货商品的商品Sku编码
    @JSONField(name = "SupplierSkuNo")
    private String supplierSkuNo;    //返货商品的供应商Sku编码
    @JSONField(name = "ProductModel")
    private String productModel;    //返货商品的原厂货号
    @JSONField(name = "Memo")
    private String memo;    //返货描述
    @JSONField(name = "BarCode")
    private String barCode;    //条形码
    @JSONField(name = "WarehouseNo")
    private String warehouseNo;    //仓库编号
    @JSONField(name = "WarehouseName")
    private String warehouseName;    //仓库名称
    @JSONField(name = "ExceptionType")
    private String exceptionType;    //返货原因 0=正常,1=残次,2=无条码,3=订单取消,4=多到货,5=其他,6=采购取消

    private String exceptionDesc;    //返货原因 0=正常,1=残次,2=无条码,3=订单取消,4=多到货,5=其他,6=采购取消
    @JSONField(name = "BrandName")
    private String brandName;
    @JSONField(name = "SopPurchaseOrderNo")
    private String sopPurchaseOrderNo;

    private String size;
    private String productMsg;
}
