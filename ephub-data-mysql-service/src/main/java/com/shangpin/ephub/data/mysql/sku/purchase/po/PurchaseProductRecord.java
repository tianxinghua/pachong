package com.shangpin.ephub.data.mysql.sku.purchase.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/6.
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseProductRecord implements Serializable {

    private static final long serialVersionUID = -1672970955045193907L;

    private String supplierId;//供货商门户编号

    private String spuId; //必须

    private String skuId; //必须

    private String productName="";//产品展示名称 必须

    private String size="";//尺码 必须

    private String color="";//颜色 必须
}
