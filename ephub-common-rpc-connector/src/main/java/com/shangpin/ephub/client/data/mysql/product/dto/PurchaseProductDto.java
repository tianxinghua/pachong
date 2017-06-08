package com.shangpin.ephub.client.data.mysql.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/6.
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseProductDto implements Serializable {

    private static final long serialVersionUID = -1672970955045193907L;

    private String supplierId;//供货商门户编号
    private List<String> spSkuIds;; //必须
}
