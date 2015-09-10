package com.shangpin.iog.gilt.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by loyalty on 15/9/10.
 */
@Setter
@Getter
public class OrderDTO {
    private List<OrderDetailDTO> order_items;


}
