package com.shangpin.ephub.data.mysql.spu.hub.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.shangpin.ephub.data.mysql.spu.hub.po.HubSpu;
import com.shangpin.ephub.data.mysql.spu.hub.po.HubSpuCriteria;
import com.shangpin.ephub.data.mysql.spu.supplier.po.HubSupplierSpuQureyDto;
/**
 * 
 * @author yanxiaobin
 *
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
    
    List<HubSpu> selectByBrand(HubSupplierSpuQureyDto dto);
    
    int count(HubSupplierSpuQureyDto dto);

    HubSpu selectByPrimaryKey(Long spuId);

    int updateByExampleSelective(@Param("record") HubSpu record, @Param("example") HubSpuCriteria example);

    int updateByExample(@Param("record") HubSpu record, @Param("example") HubSpuCriteria example);

    int updateByPrimaryKeySelective(HubSpu record);

    int updateByPrimaryKey(HubSpu record);
    //------------------- 以上部分为自动生产，如果需要自定义方法的话，请将自定义方法写在下方 -------------
    String getMaxSpuNo();
}