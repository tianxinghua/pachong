package com.shangpin.ephub.product.business.service.supplier.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by loyalty on 17/7/5.
 */
@Getter
@Setter
public class SupplierDto implements Serializable {
    private String supplierId;
    private String supplierName;
    private String supplierNo;
    private boolean isSupplyPrice;//供价制标记 取hub_supplier_value_mapping表中的mapping_state
    private boolean isStudio;//是否有摄影棚  取hub_supplier_value_mapping表中的 supplier_val_no
    private String supplierRate ;//税率 取hub_supplier_value_mapping表中的 supplier_val

}
