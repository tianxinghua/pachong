package com.shangpin.ephub.data.mysql.picture.deleted.mapper;

import com.shangpin.ephub.data.mysql.picture.deleted.po.HubSpuPendingPicDeleted;
import com.shangpin.ephub.data.mysql.picture.deleted.po.HubSpuPendingPicDeletedCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
/**
 * <p>Title:HubSpuPendingPicMapper.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月18日 下午2:12:51
 */
@Mapper
public interface HubSpuPendingPicDeletedMapper {
    int countByExample(HubSpuPendingPicDeletedCriteria example);

    int deleteByExample(HubSpuPendingPicDeletedCriteria example);

    int deleteByPrimaryKey(Long spuPendingPicId);

    int insert(HubSpuPendingPicDeleted record);

    int insertSelective(HubSpuPendingPicDeleted record);

    List<HubSpuPendingPicDeleted> selectByExampleWithRowbounds(HubSpuPendingPicDeletedCriteria example, RowBounds rowBounds);

    List<HubSpuPendingPicDeleted> selectByExample(HubSpuPendingPicDeletedCriteria example);

    HubSpuPendingPicDeleted selectByPrimaryKey(Long spuPendingPicId);

    int updateByExampleSelective(@Param("record") HubSpuPendingPicDeleted record, @Param("example") HubSpuPendingPicDeletedCriteria example);

    int updateByExample(@Param("record") HubSpuPendingPicDeleted record, @Param("example") HubSpuPendingPicDeletedCriteria example);

    int updateByPrimaryKeySelective(HubSpuPendingPicDeleted record);

    int updateByPrimaryKey(HubSpuPendingPicDeleted record);
}