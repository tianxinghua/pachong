package com.shangpin.ephub.product.business.service.supplier;

import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.product.business.service.supplier.dto.SupplierDto;

import java.util.List;

/**
 * Created by loyalty on 17/7/5.
 */
public interface SupplierInHubService {
    /**
     * 获取供货商信息
     * @param supplierId
     * @return
     */
    public SupplierDto getSupplierBySupplierId(String supplierId);

    /**
     * 获取需要拍照的供货商信息
     * @return
     */
    public List<HubSupplierValueMappingDto> getNeedShootSupplier();

    /**
     * 获取需要拍照的供货商的ID
     * @return
     */
    public List<String>  getNeedShootSupplierId();

    /**
     * 是否需要拍照供货商
     * @param supplierId
     * @return
     */
    public Boolean isShootSupplier(String supplierId);


}
