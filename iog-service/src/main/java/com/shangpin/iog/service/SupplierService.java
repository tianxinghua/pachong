package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.SupplierDTO;

import java.util.List;

/**
 * Created by loyalty on 15/6/9.
 */
public interface SupplierService {
    /**
     * 根据供货商状态 获取供货商信息
     * @param state 状态
     * @return  供货商列表
     * @throws ServiceException
     */
    public List<SupplierDTO> findByState(String state) throws ServiceException;

    /**
     * 获取所有可用供货商
     * @return 供货商列表
     * @throws ServiceException
     */
    public List<SupplierDTO> findAllWithAvailable() throws ServiceException;
}
