package com.shangpin.iog.tony.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lizhongren on 2015/10/24.
 */
@Setter
@Getter
public class EventDTO {
    private IdDTO _id;
    private int type;
    private DateDTO order_id;
    private ShopDTO shop_id;
    private Additional_info additional_info;

}
