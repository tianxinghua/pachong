package com.shangpin.ephub.product.business.service.studio.hubslotpic.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by loyalty on 17/7/11.
 */
@Getter
@Setter
public class SupplierPic {

    String supplierId;
    List<Pic> supplierPic;
}