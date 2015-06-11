package com.shangpin.iog.product.dao;


import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.BrandSpDTO;


import java.util.List;

@Mapper
public interface BrandSpMapper extends IBaseDao<BrandSpDTO> {

    /**
     * 获取所有品牌信息
     * @return 供货商列表
     */

    List<BrandSpDTO> findAll();


}