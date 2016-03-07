package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.BrandSpDTO;
import com.shangpin.iog.dto.SupplierDTO;

import java.util.List;

/**
 * Created by huxia on 15/6/9.
 */
public interface BrandSpSearchService {
    /**
     * 获取品牌信息
     * @return  品牌列表
     * @throws ServiceException
     */
    List<BrandSpDTO> findAll() throws ServiceException;

}
