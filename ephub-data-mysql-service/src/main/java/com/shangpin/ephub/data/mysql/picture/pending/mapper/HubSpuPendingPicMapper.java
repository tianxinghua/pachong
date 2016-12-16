package com.shangpin.ephub.data.mysql.picture.pending.mapper;

import com.shangpin.ephub.data.mysql.picture.pending.po.HubSpuPendingPic;
import com.shangpin.ephub.data.mysql.picture.pending.po.HubSpuPendingPicCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
/**
 * <p>Title:HubBrandDicMapper.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月13日 下午2:12:01
 */
@Mapper
public interface HubSpuPendingPicMapper {
    int countByExample(HubSpuPendingPicCriteria example);

    int deleteByExample(HubSpuPendingPicCriteria example);

    int deleteByPrimaryKey(Long spuPicId);

    int insert(HubSpuPendingPic record);

    int insertSelective(HubSpuPendingPic record);

    List<HubSpuPendingPic> selectByExampleWithRowbounds(HubSpuPendingPicCriteria example, RowBounds rowBounds);

    List<HubSpuPendingPic> selectByExample(HubSpuPendingPicCriteria example);

    HubSpuPendingPic selectByPrimaryKey(Long spuPicId);

    int updateByExampleSelective(@Param("record") HubSpuPendingPic record, @Param("example") HubSpuPendingPicCriteria example);

    int updateByExample(@Param("record") HubSpuPendingPic record, @Param("example") HubSpuPendingPicCriteria example);

    int updateByPrimaryKeySelective(HubSpuPendingPic record);

    int updateByPrimaryKey(HubSpuPendingPic record);
}