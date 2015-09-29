package com.shangpin.iog.product.dao;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.ReturnOrderDTO;

import java.util.Map;

/**
 * Created by sunny on 2015/9/19.
 */
@Mapper
public interface ReturnOrderMapper extends IBaseDao<ReturnOrderDTO> {

    public int updateReturnOrderStatus(Map<String,String> paraMap);


    /**
     * 更新退单信息 任意信息
     * @param paraMap
     * @return
     */
    public int updateReturnOrderMsg(Map<String,String> paraMap);
}
