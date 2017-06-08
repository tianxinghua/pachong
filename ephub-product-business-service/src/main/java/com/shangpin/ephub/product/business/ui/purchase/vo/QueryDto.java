package com.shangpin.ephub.product.business.ui.purchase.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/7.
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryDto implements Serializable {

    private static final long serialVersionUID = -1672970955045193937L;

    private String supplierId;//供货商门户编号
    private String skuId;; //必须


    public List<String> getSkuId() {
        return skuId == null?null:java.util.Arrays.asList(skuId.split(","));
    }


}
