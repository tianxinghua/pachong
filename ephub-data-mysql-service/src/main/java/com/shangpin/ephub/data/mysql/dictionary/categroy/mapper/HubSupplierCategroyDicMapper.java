package com.shangpin.ephub.data.mysql.dictionary.categroy.mapper;

import com.shangpin.ephub.data.mysql.dictionary.categroy.po.HubSupplierCategroyDic;
import com.shangpin.ephub.data.mysql.dictionary.categroy.po.HubSupplierCategroyDicCriteria;
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
public interface HubSupplierCategroyDicMapper {
	
    int countByExample(HubSupplierCategroyDicCriteria example);

    int deleteByExample(HubSupplierCategroyDicCriteria example);

    int deleteByPrimaryKey(Long supplierCategoryDicId);

    int insert(HubSupplierCategroyDic record);

    int insertSelective(HubSupplierCategroyDic record);

    List<HubSupplierCategroyDic> selectByExampleWithRowbounds(HubSupplierCategroyDicCriteria example, RowBounds rowBounds);

    List<HubSupplierCategroyDic> selectByExample(HubSupplierCategroyDicCriteria example);

    HubSupplierCategroyDic selectByPrimaryKey(Long supplierCategoryDicId);

    int updateByExampleSelective(@Param("record") HubSupplierCategroyDic record, @Param("example") HubSupplierCategroyDicCriteria example);

    int updateByExample(@Param("record") HubSupplierCategroyDic record, @Param("example") HubSupplierCategroyDicCriteria example);

    int updateByPrimaryKeySelective(HubSupplierCategroyDic record);

    int updateByPrimaryKey(HubSupplierCategroyDic record);
}