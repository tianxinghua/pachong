package com.shangpin.iog.product.dao;


import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.BrandSpDTO;


@Mapper
public interface BrandSpMapper extends IBaseDao<BrandSpDTO> {


    /**
     * 获取品牌数量
     * @return
     */
      public int  findCount();


}