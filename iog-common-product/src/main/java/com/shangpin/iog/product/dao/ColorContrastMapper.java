package com.shangpin.iog.product.dao;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dto.ColorContrastDTO;
import com.shangpin.iog.dao.base.Mapper;

import java.util.*;

/**
 * Created by huxia on 2015/6/15.
 */

@Mapper
public interface ColorContrastMapper extends IBaseDao<ColorContrastDTO> {

    List<ColorContrastDTO> findAll() ;

    public int findCount();

}
