package com.shangpin.ep.order.module.log.mapper;

import com.shangpin.ep.order.module.log.bean.HubOrderLog;
import com.shangpin.ep.order.module.log.bean.HubOrderLogCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
/**
 * <p>Title:HubOrderLogMapper.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月17日 下午4:55:09
 */
@Mapper
public interface HubOrderLogMapper {
    int countByExample(HubOrderLogCriteria example);

    int deleteByExample(HubOrderLogCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(HubOrderLog record);

    int insertSelective(HubOrderLog record);

    List<HubOrderLog> selectByExampleWithRowbounds(HubOrderLogCriteria example, RowBounds rowBounds);

    List<HubOrderLog> selectByExample(HubOrderLogCriteria example);

    HubOrderLog selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") HubOrderLog record, @Param("example") HubOrderLogCriteria example);

    int updateByExample(@Param("record") HubOrderLog record, @Param("example") HubOrderLogCriteria example);

    int updateByPrimaryKeySelective(HubOrderLog record);

    int updateByPrimaryKey(HubOrderLog record);
    
    /**************************************以上部分为自动生产，如果需要自定义方法的话，请将自定义方法写在下方****************************************************************/
}