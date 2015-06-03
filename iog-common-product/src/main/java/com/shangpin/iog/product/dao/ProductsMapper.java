package com.shangpin.iog.product.dao;


import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.SpinnakerProductDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;

@Mapper
public interface ProductsMapper extends IBaseDao<SpinnakerProductDTO> {


    /**
     * 根据品类和最后修改时间获取产品分页列表
     * @param categoryId 品类ID
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @param rowBounds  rowBounds 对象
     * @return
     */
	List<SpinnakerProductDTO> findListByCategoryAndLastDate(@Param("category") String categoryId,
                                                            @Param("startDate") Date startDate,
                                                            @Param("endDate") Date endDate,
                                                            RowBounds rowBounds);

    /**
     * 根据品类和最后修改时间获取产品列表
     * @param categoryId 品类ID
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @return
     */
    List<SpinnakerProductDTO> findListByCategoryAndLastDate(@Param("category") String categoryId,
                                                            @Param("startDate") Date startDate,
                                                            @Param("endDate") Date endDate);
}