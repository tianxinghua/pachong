package com.shangpin.ep.order.module.exception.mapper;

import com.shangpin.ep.order.module.exception.bean.HubOrderException;
import com.shangpin.ep.order.module.exception.bean.HubOrderExceptionCriteria;
import com.shangpin.ep.order.module.exception.bean.HubOrderExceptionWithBLOBs;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
/**
 * <p>Title:HubOrderExceptionMapper.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月17日 下午4:55:02
 */
@Mapper
public interface HubOrderExceptionMapper {
    int countByExample(HubOrderExceptionCriteria example);

    int deleteByExample(HubOrderExceptionCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(HubOrderExceptionWithBLOBs record);

    int insertSelective(HubOrderExceptionWithBLOBs record);

    List<HubOrderExceptionWithBLOBs> selectByExampleWithBLOBsWithRowbounds(HubOrderExceptionCriteria example, RowBounds rowBounds);

    List<HubOrderExceptionWithBLOBs> selectByExampleWithBLOBs(HubOrderExceptionCriteria example);

    List<HubOrderException> selectByExampleWithRowbounds(HubOrderExceptionCriteria example, RowBounds rowBounds);

    List<HubOrderException> selectByExample(HubOrderExceptionCriteria example);

    HubOrderExceptionWithBLOBs selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") HubOrderExceptionWithBLOBs record, @Param("example") HubOrderExceptionCriteria example);

    int updateByExampleWithBLOBs(@Param("record") HubOrderExceptionWithBLOBs record, @Param("example") HubOrderExceptionCriteria example);

    int updateByExample(@Param("record") HubOrderException record, @Param("example") HubOrderExceptionCriteria example);

    int updateByPrimaryKeySelective(HubOrderExceptionWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(HubOrderExceptionWithBLOBs record);

    int updateByPrimaryKey(HubOrderException record);
    
    /**************************************以上部分为自动生产，如果需要自定义方法的话，请将自定义方法写在下方****************************************************************/
}