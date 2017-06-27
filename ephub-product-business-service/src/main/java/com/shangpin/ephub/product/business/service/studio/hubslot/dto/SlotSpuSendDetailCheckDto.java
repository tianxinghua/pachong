package com.shangpin.ephub.product.business.service.studio.hubslot.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by loyalty on 17/6/23.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SlotSpuSendDetailCheckDto implements Serializable {

    private Long studioSlotSpuSendDetailId;
    private Long slotSpuSupplierId;
    private String slotNo;
    private String memo;
    private boolean resultSign;
    private String userName;
}
