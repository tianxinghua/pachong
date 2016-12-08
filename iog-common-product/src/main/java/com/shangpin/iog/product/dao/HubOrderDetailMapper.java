package com.shangpin.iog.product.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.HubOrderDetailDTO;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.OrderDetailDTO;

@Mapper
public interface HubOrderDetailMapper extends IBaseDao<HubOrderDetailDTO> {


    public List<HubOrderDetailDTO> getOrderBySupplierIdAndTime(@Param("supplierId")String supplier, @Param("startDate")Date startDate,
    		@Param("endDate")Date endDate,@Param("CGD")String CGD,@Param("spSkuId")String spSkuId,
    		@Param("supplierSkuId")String supplierSkuId,@Param("orderStatus")String orderStatus,@Param("pushStatus") String pushStatus,RowBounds rowBounds);
    
    /**
     * 通过epMasterOrderNo查询
     * @param epMasterOrderNo
     * @return
     */

	public int getOrderTotalBySupplierIdAndTime(@Param("supplierId") String supplier,@Param("startDate") String startTime,@Param("endDate") String endTime,@Param("CGD") String CGD,
					@Param("spSkuId") String spSkuId,@Param("supplierSkuId") String supplierSkuId,@Param("orderStatus") String orderStatus,@Param("pushStatus") String pushStatus);

}
