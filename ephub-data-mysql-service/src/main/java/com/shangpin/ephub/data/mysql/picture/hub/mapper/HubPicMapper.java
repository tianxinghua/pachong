package com.shangpin.ephub.data.mysql.picture.hub.mapper;

import com.shangpin.ephub.data.mysql.picture.hub.po.HubPic;
import com.shangpin.ephub.data.mysql.picture.hub.po.HubPicCriteria;
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
public interface HubPicMapper {
    int countByExample(HubPicCriteria example);

    int deleteByExample(HubPicCriteria example);

    int deleteByPrimaryKey(Long picId);

    int insert(HubPic record);

    int insertSelective(HubPic record);

    List<HubPic> selectByExampleWithRowbounds(HubPicCriteria example, RowBounds rowBounds);

    List<HubPic> selectByExample(HubPicCriteria example);

    HubPic selectByPrimaryKey(Long picId);

    int updateByExampleSelective(@Param("record") HubPic record, @Param("example") HubPicCriteria example);

    int updateByExample(@Param("record") HubPic record, @Param("example") HubPicCriteria example);

    int updateByPrimaryKeySelective(HubPic record);

    int updateByPrimaryKey(HubPic record);
}