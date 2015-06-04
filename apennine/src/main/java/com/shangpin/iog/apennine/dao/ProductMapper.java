package com.shangpin.iog.apennine.dao;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.ApennineProductDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;

/**
 * Created by sunny on 2015/6/3.
 */
@Mapper
public interface ProductMapper extends IBaseDao<ApennineProductDTO> {
    /**
     * 根据品类和最后修改时间获取产品分页列表
     * @param categoryId 品类ID
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @param rowBounds  rowBounds 对象
     * @return
     */
    List<ApennineProductDTO> findListByCategoryAndLastDate(@Param("category")String categoryId,
                                                            @Param("startDate")Date startDate,
                                                            @Param("endDate")Date endDate,
                                                            RowBounds rowBounds);

    /**
     * 根据品类和最后修改时间获取产品列表
     * @param categoryId 品类ID
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @return
     */
    List<ApennineProductDTO> findListByCategoryAndLastDate(@Param("category")String categoryId,
                                                            @Param("startDate")Date startDate,
                                                            @Param("endDate")Date endDate);
}
