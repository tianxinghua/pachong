package com.shangpin.ephub.product.business.service.studio.hubslotpic.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Created by loyalty on 17/7/11.
 */
@Getter
@Setter
public class SlotSpuPic {

    private String brandNo;

    private String spuModel;

    private List<SupplierPic> supplierPic;

}
