package com.shangpin.ephub.product.business.service.supplier;

import com.shangpin.ephub.product.business.service.supplier.dto.SupplierDto;

/**
 * Created by loyalty on 17/7/5.
 */
public interface SupplierService {
    /**
     * 获取供货商信息
     * @param supplierId
     * @return
     */
    public SupplierDto getSupplierBySupplierId(String supplierId);
}
