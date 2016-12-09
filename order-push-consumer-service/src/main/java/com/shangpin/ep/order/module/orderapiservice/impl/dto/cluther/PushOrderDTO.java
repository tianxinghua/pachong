package com.shangpin.ep.order.module.orderapiservice.impl.dto.cluther;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2015/9/28.
 */
@Setter
@Getter
public class PushOrderDTO {
	private String purchaseNo;
    private String orderNo;
    private List<ItemDTO> orderItems;
}
