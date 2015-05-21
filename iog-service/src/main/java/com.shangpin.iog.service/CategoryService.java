package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;

/**
 * Created by loyalty on 15/5/20.
 * 品类接口
 */
public interface CategoryService {
    /**
     * 获取品类
     * @throws ServiceException
     */
    public void fetchCategory() throws ServiceException;
}
