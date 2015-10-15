package com.shangpin.iog.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.PriceCountDetailDTO;

/**
 * Created by zhaogenchun on 2015/10/13.
 */
@Mapper
public interface PriceCountDetailMapper extends IBaseDao<PriceCountDetailDTO>{

    /**
     * 查询CountPrice表
     * @param (state)
     */
     List<PriceCountDetailDTO> findAllOfAvailabled(@Param("state") String state);

}


