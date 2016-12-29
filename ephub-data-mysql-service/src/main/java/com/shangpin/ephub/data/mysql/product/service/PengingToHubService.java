package com.shangpin.ephub.data.mysql.product.service;

import com.shangpin.ephub.data.mysql.product.dto.SpuModelDto;

/**
 * Created by loyalty on 16/12/27.
 */
public interface PengingToHubService {

    /**
     * 审核后产生HUBSPU和hubsku
     * @param spuModelVO
     * @return
     * @throws Exception
     */
    public boolean auditPending(SpuModelDto spuModelVO) throws Exception;


}
