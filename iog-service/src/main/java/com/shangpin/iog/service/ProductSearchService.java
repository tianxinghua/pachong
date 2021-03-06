package com.shangpin.iog.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.page.Page;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;

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
     * @param flag same价格未变，diff价格变化
     * @return
     * @throws ServiceException 自定义异常
     */
    public Page<ProductDTO> findProductPageBySupplierAndTime(String supplier,Date startDate,Date endDate,Integer pageIndex,Integer pageSize,String flag) throws ServiceException;


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
     * 获取指定日期内的SKU 仅包含价格和库存
     * @param supplier    供货商编号
     * @param startDate   开始日期
     * @param endDate     结束日期
     * @return
     * @throws ServiceException
     */
    public Map<String,SkuDTO> findStockAndPriceOfSkuObjectMap(String supplier,Date startDate,Date endDate)  throws ServiceException;


    /**
     * 获取产品信息
     * @param supplier  供货商
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param pageIndex 开始页码
     * @param pageSize  每页显示行数
     * @param flag same价格未变，diff价格变化
     * @return  StringBuffer
     * @throws ServiceException
     */
    public StringBuffer exportProduct(String supplier,Date startDate,Date endDate,Integer pageIndex,Integer pageSize,String flag) throws ServiceException;
    
    public StringBuffer dailyUpdatedProduct(String supplier,int day,Date now,Integer pageIndex,Integer pageSize,String flag) throws ServiceException;
    
    public StringBuffer dailyGoodProducts(String[] suppliers,Date startime,Date endtime,Integer pageIndex,Integer pageSize) throws ServiceException;
    
    /**
     * 获取产品id
     * @param supplier  供货商
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return  String
     * @throws ServiceException
     */
    public String exportSkuId(String supplier,Date startDate,Date endDate) throws ServiceException;
    
    public StringBuffer exportDiffProduct(String supplier,Date startDate,Date endDate,Integer pageIndex,Integer pageSize,String flag) throws ServiceException;
    /**
     * 获取价格变化了的产品   用于发邮件
     * @param startDate
     * @param endDate
     * @param pageIndex
     * @param pageSize
     * @param flag
     * @return
     * @throws ServiceException
     */
    public StringBuffer getDiffProduct(Date startDate,Date endDate,Integer pageIndex,Integer pageSize,String flag) throws ServiceException;
    
    /**
     * 获取季节发生变化的产品
     * @param supplier
     * @param startDate
     * @param endDate
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws ServiceException
     */
    public StringBuffer getDiffSeasonProducts(String supplier,Date startDate,Date endDate,Integer pageIndex,Integer pageSize) throws ServiceException;
    /**
     * 获取产品信息,for order
     * @param supplierId  供货商
     * @return ProductDTO (若为空，返回所有)
     */
    public ProductDTO findProductForOrder(String supplierId,String skuId) throws ServiceException;

    /**
     * 获取产品信息 基础信息
     * @param supplierId   供货商
     * @param skuId         供货商SKUID
     * @return
     * @throws ServiceException
     */
    public ProductDTO findProductBySupplierIdAndSkuId(String supplierId,String skuId) throws ServiceException;
    
    public SpuDTO findPartSpuData(String supplierId,String spuId);

	public List<SpuDTO> findPartBySupAndSpuId(String supplierId);
    
	public List<SpuDTO> findpartSpuListBySupplier(String supplierId);
	
	/**
	 * 查询符合ep规则的所有图片名
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	List<ProductDTO> findPicName(String supplier, Date startDate, Date endDate,Integer pageIndex, Integer pageSize);
	
	/**
	 * 根据ep规则导出重点供货商或者KA的产品
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws ServiceException
	 */
	public StringBuffer exportProductByEpRule(String supplier,Date startDate,Date endDate,Integer pageIndex,Integer pageSize) throws ServiceException;
	
	/**
	 * BU根据新的ep规则导出产品
	 * @param bu 选择哪个bu
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public StringBuffer buExportProduct(String bu,String supplier,Date startDate,Date endDate,Integer pageIndex,Integer pageSize) throws ServiceException;
	
	/**
	 * 查找报表导出的产品
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws ServiceException
	 */
	public List<ProductDTO> findReportProduct(String supplier,Date startDate,Date endDate,Integer pageIndex,Integer pageSize) throws ServiceException;
	
	/**
	 * 将产品List转换成Excel
	 * @param products
	 * @param picPath
	 * @return
	 * @throws ServiceException
	 */
	public HSSFWorkbook reportProductToExcel(List<ProductDTO> products,String picPath) throws ServiceException;
	
	/**
	 * 将产品list转换StringBuffer
	 * @param products
	 * @return
	 * @throws ServiceException
	 */
	public StringBuffer reportProductToBuffer(List<ProductDTO> products) throws ServiceException;
	
	/**
	 * 报表导出
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws ServiceException
	 */
	public StringBuffer exportReportProduct(String supplier,Date startDate,Date endDate,Integer pageIndex,Integer pageSize) throws ServiceException;
	/**
	 * 女鞋
	 * @param bu
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws ServiceException
	 */
	public StringBuffer shoeExportProduct(String bu,String supplier, Date startDate,
			Date endDate, Integer pageIndex, Integer pageSize) throws ServiceException;
	/**
	 * 查找所有BU
	 * @return
	 */
	public List<String> findAllBus() throws ServiceException;
	
	/**
	 * 临时导出
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws ServiceException
	 */
	public StringBuffer tempExport(String supplier,Date startDate,Date endDate,Integer pageIndex,Integer pageSize) throws ServiceException;
		
}

