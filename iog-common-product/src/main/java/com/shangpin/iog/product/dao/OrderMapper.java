package com.shangpin.iog.product.dao;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.OrderDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by huxia on 2015/9/10.
 */
@Mapper
public interface OrderMapper extends IBaseDao<OrderDTO> {


    public List<OrderDTO> findBySupplierIdAndStatus(@Param("supplierId") String supplierId,
                                                    @Param("status") String status) ;



    public int updateOrderStatus(Map<String,String> paraMap);


}
