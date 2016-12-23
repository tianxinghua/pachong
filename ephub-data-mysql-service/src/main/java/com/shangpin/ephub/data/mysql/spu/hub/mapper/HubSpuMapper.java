package com.shangpin.ephub.data.mysql.spu.hub.mapper;

import com.shangpin.ephub.data.mysql.spu.hub.po.HubSpu;
import com.shangpin.ephub.data.mysql.spu.hub.po.HubSpuCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
/**
 * <p>Title:HubSpuMapper.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月21日 下午2:39:52
 */
@Mapper
public interface HubSpuMapper {
    int countByExample(HubSpuCriteria example);

    int deleteByExample(HubSpuCriteria example);

    int deleteByPrimaryKey(Long spuId);

    int insert(HubSpu record);

    int insertSelective(HubSpu record);

    List<HubSpu> selectByExampleWithRowbounds(HubSpuCriteria example, RowBounds rowBounds);

    List<HubSpu> selectByExample(HubSpuCriteria example);

    HubSpu selectByPrimaryKey(Long spuId);

    int updateByExampleSelective(@Param("record") HubSpu record, @Param("example") HubSpuCriteria example);

    int updateByExample(@Param("record") HubSpu record, @Param("example") HubSpuCriteria example);

    int updateByPrimaryKeySelective(HubSpu record);

    int updateByPrimaryKey(HubSpu record);

    String getMaxSpuNo();
}