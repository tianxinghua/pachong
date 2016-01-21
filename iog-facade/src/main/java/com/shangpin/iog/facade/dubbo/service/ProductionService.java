package com.shangpin.iog.facade.dubbo.service;

import com.shangpin.iog.facade.dubbo.dto.ProductDTO;
import com.shangpin.iog.facade.dubbo.dto.ProductUpdateDTO;
import com.shangpin.iog.facade.dubbo.dto.ServiceException;

/**
 * Created by loyalty on 15/9/15.
 * 产品业务
 * 无删除信息业务 删除走SOP页面操作
 */
public interface ProductionService {

    /**
     * 保存推送过来的产品
     * @param productDTO
     * @throws ServiceException
     */
    public Boolean   saveProduct(ProductDTO productDTO) throws ServiceException;

    /**
     * 更新产品信息
     * TODO  此接口需要考虑清楚更新产品那个部分  有些内容不允许修改 现建议不做
     * 因为产品推送过来后 需要人工翻译  当再次修改后 可能人工就不在处理了（？）  可以维护修改时间
     * @param productUpdateDTO
     * @return
     * @throws ServiceException
     */
    public Boolean  updateProduct(ProductUpdateDTO productUpdateDTO) throws ServiceException;


}
