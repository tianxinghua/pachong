package com.shangpin.ephub.data.mysql.dictionary.brand.mapper;

import com.shangpin.ephub.data.mysql.dictionary.brand.po.HubSupplierBrandDic;
import com.shangpin.ephub.data.mysql.dictionary.brand.po.HubSupplierBrandDicCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
/**
 * <p>Title:HubSupplierBrandDicMapper.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月29日 上午10:39:09
 */
@Mapper
public interface HubSupplierBrandDicMapper {
    int countByExample(HubSupplierBrandDicCriteria example);

    int deleteByExample(HubSupplierBrandDicCriteria example);

    int deleteByPrimaryKey(Long supplierBrandDicId);

    int insert(HubSupplierBrandDic record);

    int insertSelective(HubSupplierBrandDic record);

    List<HubSupplierBrandDic> selectByExampleWithRowbounds(HubSupplierBrandDicCriteria example, RowBounds rowBounds);

    List<HubSupplierBrandDic> selectByExample(HubSupplierBrandDicCriteria example);

    HubSupplierBrandDic selectByPrimaryKey(Long supplierBrandDicId);

    int updateByExampleSelective(@Param("record") HubSupplierBrandDic record, @Param("example") HubSupplierBrandDicCriteria example);

    int updateByExample(@Param("record") HubSupplierBrandDic record, @Param("example") HubSupplierBrandDicCriteria example);

    int updateByPrimaryKeySelective(HubSupplierBrandDic record);

    int updateByPrimaryKey(HubSupplierBrandDic record);
}