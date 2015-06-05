package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.page.Page;
import com.shangpin.iog.common.utils.excel.AccountsExcelTemplate;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.SpinnakerProductDTO;

import java.util.Date;
import java.util.List;

/**
 * Created by loyalty on 15/5/20.
 * 产品查询导出业务
 */
public interface ProductSearchService {




    /**
     * 获取产品分页信息
     * @param supplier  供货商ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param pageIndex 开始页码
     * @param pageSize  每页显示行数
     * @return
     * @throws ServiceException 自定义异常
     */
    public Page<ProductDTO> findProductPageBySupplierAndTime(String supplier,Date startDate,Date endDate,Integer pageIndex,Integer pageSize) throws ServiceException;


    /**
     * 获取产品分页信息
     * @param supplier
     * @param startDate
     * @param endDate
     * @return
     * @throws ServiceException
     */
    public List<ProductDTO> findProductListBySupplierAndTime(String supplier,Date startDate,Date endDate) throws ServiceException;



    /**
     * 获取产品信息
     * @param supplier  供货商
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param pageIndex 开始页码
     * @param pageSize  每页显示行数
     * @return  StringBuffer
     * @throws ServiceException
     */
    public StringBuffer exportProduct(String supplier,Date startDate,Date endDate,Integer pageIndex,Integer pageSize) throws ServiceException;
}
