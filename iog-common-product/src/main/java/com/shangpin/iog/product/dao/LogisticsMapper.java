package com.shangpin.iog.product.dao;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.LogisticsDTO;
import com.shangpin.iog.dto.OrderDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 */
@Mapper
public interface LogisticsMapper extends IBaseDao<LogisticsDTO> {




    /**
     * 获取尚未处理的供货商发货单号
     * @param supplierId  供货商门户编号
     * @return
     */
    public List<String> findNotConfirmSupplierLogisticsNumber(@Param("supplierId") String supplierId,@Param("createTime") String  createTime);

    /**
     * 根据供货商的发货单号获取产品的物流信息列表
     * @param trackNumber
     * @return
     */
    public List<LogisticsDTO>  findPurchaseDetailNoBySupplierAndTrackNumber(@Param("supplierId") String supplierId,@Param("trackNumber") String trackNumber);

    /**
     * 更新尚品的发货单信息信息状态
     * @param logisticsDTO 更新信息对象
     * @return
     */
    public int updateLogisticsStatus(LogisticsDTO logisticsDTO);

    /**
     * 同时更新商品的发货单号信息及更新时间
     * @param map   supplierId trackNumber spInvoice   updateTime
     */
    public void updateMulti(Map<String,Object> map);


}
