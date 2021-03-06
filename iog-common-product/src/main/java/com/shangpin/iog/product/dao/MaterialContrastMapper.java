package com.shangpin.iog.product.dao;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.ColorContrastDTO;
import com.shangpin.iog.dto.MaterialContrastDTO;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * Created by fanhaiying on 2015/6/15.
 */

@Mapper
public interface MaterialContrastMapper extends IBaseDao<MaterialContrastDTO> {
    /**
     * 获取材质信息
     * @return  颜色列表
     * @throws ServiceException
     */
    List<MaterialContrastDTO>  findAll() ;
    
    /**
     * 根据材质级别查找
     * @param rank
     * @return
     */
    public List<MaterialContrastDTO> findByRank(@Param("rank") int rank); 

    public int findCount();

}
