package com.shangpin.ephub.data.mysql.spu.pendingnohand.mapper;

import com.shangpin.ephub.data.mysql.spu.pendingnohand.po.HubSpuPendingNohandleReason;
import com.shangpin.ephub.data.mysql.spu.pendingnohand.po.HubSpuPendingNohandleReasonCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
@Mapper
public interface HubSpuPendingNohandleReasonMapper {
    int countByExample(HubSpuPendingNohandleReasonCriteria example);

    int deleteByExample(HubSpuPendingNohandleReasonCriteria example);

    int deleteByPrimaryKey(Long spuPendingNohandleReasonId);

    int insert(HubSpuPendingNohandleReason record);

    int insertSelective(HubSpuPendingNohandleReason record);

    List<HubSpuPendingNohandleReason> selectByExampleWithRowbounds(HubSpuPendingNohandleReasonCriteria example, RowBounds rowBounds);

    List<HubSpuPendingNohandleReason> selectByExample(HubSpuPendingNohandleReasonCriteria example);

    HubSpuPendingNohandleReason selectByPrimaryKey(Long spuPendingNohandleReasonId);

    int updateByExampleSelective(@Param("record") HubSpuPendingNohandleReason record, @Param("example") HubSpuPendingNohandleReasonCriteria example);

    int updateByExample(@Param("record") HubSpuPendingNohandleReason record, @Param("example") HubSpuPendingNohandleReasonCriteria example);

    int updateByPrimaryKeySelective(HubSpuPendingNohandleReason record);

    int updateByPrimaryKey(HubSpuPendingNohandleReason record);
}