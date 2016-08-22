package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.ProductDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhongren on 2016/8/18.
 */
public interface ProductReportService {


    /**
     * 获取供货商尚未处理的最新季节的数量
     * @return
     * @throws ServiceException
     */
    public Map<String,Integer> findProductReport() throws ServiceException;

    /**
     * 根据日期获取供货商尚未处理的最新季节的数量
     * @param startDate
     * @param endDate
     * @return
     * @throws ServiceException
     */
    public Map<String,Integer> findProductReport(String startDate,String endDate) throws ServiceException;

    /**
     * 获取未处理的图片
     * @return    key:supplierId-date
     * @throws ServiceException
     */
    public Map<String,String> findPicture(Map<String,String> picMap ,String supplierId ,String startDate,String endDate,String excludeSupplierId) throws ServiceException;

}
