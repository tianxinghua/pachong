package com.shangpin.ephub.data.mysql.supplier.channel.mapper;

import com.shangpin.ephub.data.mysql.supplier.channel.bean.SupplierChannelPic;
import com.shangpin.ephub.data.mysql.supplier.channel.bean.SupplierChannelPicCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

@Mapper
public interface SupplierChannelPicMapper {
    int countByExample(SupplierChannelPicCriteria example);

    int deleteByExample(SupplierChannelPicCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(SupplierChannelPic record);

    int insertSelective(SupplierChannelPic record);

    List<SupplierChannelPic> selectByExampleWithRowbounds(SupplierChannelPicCriteria example, RowBounds rowBounds);

    List<SupplierChannelPic> selectByExample(SupplierChannelPicCriteria example);

    SupplierChannelPic selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SupplierChannelPic record, @Param("example") SupplierChannelPicCriteria example);

    int updateByExample(@Param("record") SupplierChannelPic record, @Param("example") SupplierChannelPicCriteria example);

    int updateByPrimaryKeySelective(SupplierChannelPic record);

    int updateByPrimaryKey(SupplierChannelPic record);

    SupplierChannelPic selectByMap(Map<String,String> map);
}