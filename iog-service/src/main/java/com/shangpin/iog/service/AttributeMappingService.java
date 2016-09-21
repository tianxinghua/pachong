package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.HubSupplierValueMappingDTO;

/**
 * Created by lizhongren on 2016/9/14.
 */
public interface AttributeMappingService {

    /**
     * 保存品牌 ，保存前需要查询 不能重复插入
     * @param dto
     * @throws ServiceException
     */
    public Boolean  saveBrand(HubSupplierValueMappingDTO dto) throws ServiceException;
}
