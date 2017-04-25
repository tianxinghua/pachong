package com.shangpin.supplier.product.consumer.supplier.emonti.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lizhongren on 2017/4/20.
 */
@Getter
@Setter
public class ProductSkuDTO {
    private String supplierId;//供货商ID  必填
    private String skuId;//必填
    private String productName;
    private String marketPrice;//市场价  三个价格必须有一个 需要和BD沟通 那个价格是算尚品的供货价的价格
    private String salePrice;//销售价格
    private String supplierPrice;//供货商价格
    private String barcode;//条形码

    private String saleCurrency;//币种
    private String productSize;//尺码   必填
    private String stock;//库存  必填   如果库存等于0的 不存
}
