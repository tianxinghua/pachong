package com.shangpin.ephub.client.message.productselect.body;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lizhongren on 2016/12/30.
 * 循环套用
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSelect implements Serializable {

    Long id;

    List<ProductSelect> subProduct;

}
