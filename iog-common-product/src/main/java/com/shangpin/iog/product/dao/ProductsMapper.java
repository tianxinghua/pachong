package com.shangpin.iog.product.dao;


import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.ProductDTO;
import org.apache.ibatis.annotations.Param;
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
}