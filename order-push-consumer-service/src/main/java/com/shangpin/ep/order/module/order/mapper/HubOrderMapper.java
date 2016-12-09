package com.shangpin.ep.order.module.order.mapper;

import com.shangpin.ep.order.module.order.bean.HubOrder;
import com.shangpin.ep.order.module.order.bean.HubOrderCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
/**
 * <p>Title:HubOrderMapper.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月17日 下午4:55:22
 */
@Mapper
public interface HubOrderMapper {
    int countByExample(HubOrderCriteria example);

    int deleteByExample(HubOrderCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(HubOrder record);

    int insertSelective(HubOrder record);

    List<HubOrder> selectByExampleWithRowbounds(HubOrderCriteria example, RowBounds rowBounds);

    List<HubOrder> selectByExample(HubOrderCriteria example);

    HubOrder selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") HubOrder record, @Param("example") HubOrderCriteria example);

    int updateByExample(@Param("record") HubOrder record, @Param("example") HubOrderCriteria example);

    int updateByPrimaryKeySelective(HubOrder record);

    int updateByPrimaryKey(HubOrder record);
    
    /**************************************以上部分为自动生产，如果需要自定义方法的话，请将自定义方法写在下方****************************************************************/
}