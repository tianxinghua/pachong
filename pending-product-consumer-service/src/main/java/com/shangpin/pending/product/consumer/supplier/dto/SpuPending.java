package com.shangpin.pending.product.consumer.supplier.dto;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 17/1/3.
 */
@Getter
@Setter
public class SpuPending extends HubSpuPendingDto {

    private  String hubSpuNo;//hub spu 编号
}
