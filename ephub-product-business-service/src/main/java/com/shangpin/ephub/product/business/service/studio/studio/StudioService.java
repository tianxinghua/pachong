package com.shangpin.ephub.product.business.service.studio.studio;

/**
 * Created by loyalty on 17/6/20.
 */
public interface StudioService {
    /**
     * 查询是否是自家的摄影棚
     * @param supplierId
     * @return
     */
    public boolean isOwnerStudio(String supplierId);
}
