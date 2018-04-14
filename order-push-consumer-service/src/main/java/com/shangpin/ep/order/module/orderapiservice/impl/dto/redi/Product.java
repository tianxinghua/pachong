package com.shangpin.ep.order.module.orderapiservice.impl.dto.redi;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by lizhongren on 2018/4/3.
 */
@Getter
@Setter
public class Product implements Serializable {
    String item_size_id;

    Integer  quantity;
}
