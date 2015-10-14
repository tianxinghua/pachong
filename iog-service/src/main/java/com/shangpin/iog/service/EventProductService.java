package com.shangpin.iog.service;

import java.util.List;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.EventProductDTO;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;

/**
 * Created by loyalty on 15/6/4.
 * 产品
 */
public interface EventProductService {

    /**
     * 保存单个活动
     * @param spuDTO
     * @throws ServiceException
     */
    public void saveEventProduct(EventProductDTO eventDTO) throws ServiceException;

	public String selectEventIdBySku(String skuNo,String supplierId) throws ServiceException;

}
