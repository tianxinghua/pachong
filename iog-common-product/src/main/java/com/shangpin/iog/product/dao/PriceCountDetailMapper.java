package com.shangpin.iog.product.dao;

import java.util.List;

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
     * @param (flag)
     */
     List<PriceCountDetailDTO> findAllOfAvailabled(String flag);

}


