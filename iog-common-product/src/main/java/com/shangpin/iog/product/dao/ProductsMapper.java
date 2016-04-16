package com.shangpin.iog.product.dao;


import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.ProductOfSpecDTO;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;

@Mapper
public interface ProductsMapper extends IBaseDao<ProductDTO> {


    /**
     * 根据供货商标识和最后修改时间获取产品分页列表
     * @param supplier   供货商ID或NO 唯一标示
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @param rowBounds  rowBounds 对象
     * @return
     */
	List<ProductDTO> findListBySupplierAndLastDate(@Param("supplier") String supplier,
                                                            @Param("startDate") Date startDate,
                                                            @Param("endDate") Date endDate,
                                                            RowBounds rowBounds);

    /**
     * 根据供货商标识和最后修改时间获取产品列表
     * @param supplier   供货商ID或NO 唯一标示
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @return
     */
    List<ProductDTO> findListBySupplierAndLastDate(@Param("supplier") String supplier,
                                                            @Param("startDate") Date startDate,
                                                            @Param("endDate") Date endDate);
    
    /**
     * 根据ep规则查找商品 分页
     * @param supplier
     * @param startDate
     * @param endDate
     * @param rowBounds
     * @return
     */
    List<ProductDTO> findListByEPRegularAndLastDate(@Param("supplier") String supplier,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            RowBounds rowBounds);
    /**
     * 根据ep规则查找商品  不分页
     * @param supplier
     * @param startDate
     * @param endDate
     * @return
     */
    List<ProductDTO> findListByEPRegularAndLastDate(@Param("supplier") String supplier,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
    
    /**
     * 根据供货商标识和修改时间获取所有产品的ID
     * @param supplier   供货商ID或NO 唯一标示
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @return
     */
    List<ProductDTO> findSkuIdbySupplier(@Param("supplier") String supplier,
    							@Param("startDate") Date startDate,
    							@Param("endDate") Date endDate);
    /**
     * 根据供货商id查询价格变化的产品列表
     */
    List<ProductDTO> findDiffListBySupplierAndLastDate(@Param("supplier") String supplier,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
    /**
     * 根据供货商id查询价格变化的产品列表
     * 
     */
    List<ProductDTO> findDiffListBySupplierAndLastDate(@Param("supplier") String supplier,
    		@Param("startDate") Date startDate,
    		@Param("endDate") Date endDate,
    		RowBounds rowBounds);
    
    /**
     * 根据供货商id查询产品列表(supplierId，spuid,skuid,category,brand,size,color,price)
     * @param supplierId skuid
     * @return 返回对应supplierId,skuId的产品，
     */
    ProductDTO findProductOrderDTOList(@Param("supplierId") String supplierId,@Param("skuId") String skuId);

    /**
     * 根据供货商id和skuId 查询产品全方位信息数据
     * @param supplierId
     * @param skuId
     * @return 返回对应supplierId,skuId的产品，
     */
    ProductDTO findProductBySupplierIdAndSkuId(@Param("supplierId") String supplierId,@Param("skuId") String skuId);


    /**
     * 根据供货商id和skuId 查询产品部分信息
     * @param supplierId   供货商ID
     * @param skuId
     * @return 返回对应supplierId,skuId的产品，
     */
    ProductOfSpecDTO findProductOfSpecBySupplierIdAndSkuId(@Param("supplierId") String supplierId, @Param("skuId") String skuId);


    /**
     * 查询所有供货商的产品列表
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param rowBounds
     * @return
     */
    List<ProductDTO> findListOfAllSupplier(@Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            RowBounds rowBounds);
    /**
     * 按照ep规则查询所有图片名
     */
    List<String> findPicNameListByEPRegularAndLastDate(@Param("supplier") String supplier,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
    List<String> findPicNameListByEPRegularAndLastDate(@Param("supplier") String supplier,
    		@Param("startDate") Date startDate,
    		@Param("endDate") Date endDate, RowBounds rowBounds);
}





