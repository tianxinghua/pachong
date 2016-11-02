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
     * @param state 状态1可用，0不可用,null查询所有
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
    
    /**
     * 根据supplierId获取实体
     * @param supplierId
     * @return
     * @throws ServiceException
     */
    public SupplierDTO findBysupplierId(String supplierId) throws ServiceException;
    
    public List<SupplierDTO> hkFindAllByState(String supplier_state);
    
    public List<SupplierDTO> findAll() throws Exception;
    
    public SupplierDTO hkFindBysupplierId(String supplier)throws ServiceException;
}
