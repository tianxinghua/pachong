package com.shangpin.ephub.data.mysql.spu.pending.mapper;

import com.shangpin.ephub.data.mysql.spu.pending.po.HubSpuPending;
import com.shangpin.ephub.data.mysql.spu.pending.po.HubSpuPendingCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
/**
 * <p>Title:HubSpuPendingMapper.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月22日 上午11:40:54
 */
@Mapper
public interface HubSpuPendingMapper {
    int countByExample(HubSpuPendingCriteria example);

    int deleteByExample(HubSpuPendingCriteria example);

    int deleteByPrimaryKey(Long spuPendingId);

    int insert(HubSpuPending record);

    int insertSelective(HubSpuPending record);

    List<HubSpuPending> selectByExampleWithRowbounds(HubSpuPendingCriteria example, RowBounds rowBounds);

    List<HubSpuPending> selectByExample(HubSpuPendingCriteria example);

    HubSpuPending selectByPrimaryKey(Long spuPendingId);

    int updateByExampleSelective(@Param("record") HubSpuPending record, @Param("example") HubSpuPendingCriteria example);

    int updateByExample(@Param("record") HubSpuPending record, @Param("example") HubSpuPendingCriteria example);

    int updateByPrimaryKeySelective(HubSpuPending record);

    int updateByPrimaryKey(HubSpuPending record);


    // 以上部分为自动生产，如果需要自定义方法的话，请将自定义方法写在下方
    int countDistinctBrandNoAndSpuModelByExample(HubSpuPendingCriteria example);
}