package com.shangpin.ephub.data.mysql.dictionary.material.mapper;

import com.shangpin.ephub.data.mysql.dictionary.material.po.HubMaterialDic;
import com.shangpin.ephub.data.mysql.dictionary.material.po.HubMaterialDicCriteria;
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
public interface HubMaterialDicMapper {
    int countByExample(HubMaterialDicCriteria example);

    int deleteByExample(HubMaterialDicCriteria example);

    int deleteByPrimaryKey(Long materialDicId);

    int insert(HubMaterialDic record);

    int insertSelective(HubMaterialDic record);

    List<HubMaterialDic> selectByExampleWithRowbounds(HubMaterialDicCriteria example, RowBounds rowBounds);

    List<HubMaterialDic> selectByExample(HubMaterialDicCriteria example);

    HubMaterialDic selectByPrimaryKey(Long materialDicId);

    int updateByExampleSelective(@Param("record") HubMaterialDic record, @Param("example") HubMaterialDicCriteria example);

    int updateByExample(@Param("record") HubMaterialDic record, @Param("example") HubMaterialDicCriteria example);

    int updateByPrimaryKeySelective(HubMaterialDic record);

    int updateByPrimaryKey(HubMaterialDic record);
}