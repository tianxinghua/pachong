package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.page.Page;
import com.shangpin.iog.common.utils.excel.AccountsExcelTemplate;
import com.shangpin.iog.dto.ProductDTO;

import java.util.Date;

/**
 * Created by loyalty on 15/5/20.
 * 产品接口
 */
public interface ProductService {

    /**
     * 抓取产品   现抓取使用另外程序 不需要实现
     * @throws ServiceException   自定义异常
     */
    public void  fetchProduct() throws ServiceException;

    /**
     * 从数据库中获取产品信息
     * @param categoryId   品类
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param pageIndex 开始页码
     * @param pageSize  每页显示行数
     * @return
     * @throws ServiceException 自定义异常
     */
    public Page<ProductDTO> findProduct(String categoryId,Date startDate,Date endDate,Integer pageIndex,Integer pageSize) throws ServiceException;

    /**
     * 导出excel产品信息
     * @param categoryId   品类
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param pageIndex 开始页码
     * @param pageSize  每页显示行数
     * @return   excel模板
     * @throws ServiceException
     */
    public AccountsExcelTemplate exportProduct(String categoryId,Date startDate,Date endDate,Integer pageIndex,Integer pageSize) throws ServiceException;
}
