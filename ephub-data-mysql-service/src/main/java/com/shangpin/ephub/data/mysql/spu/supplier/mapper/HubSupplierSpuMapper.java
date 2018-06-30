package com.shangpin.ephub.data.mysql.spu.supplier.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.shangpin.ephub.data.mysql.spu.supplier.po.HubSupplierSpu;
import com.shangpin.ephub.data.mysql.spu.supplier.po.HubSupplierSpuCriteria;
import com.shangpin.ephub.data.mysql.spu.supplier.po.HubSupplierSpuQureyDto;
/**
 * <p>Title:HubBrandDicMapper.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月13日 下午2:12:01
 */
@Mapper
public interface HubSupplierSpuMapper {
	
    int countByExample(HubSupplierSpuCriteria example);

    int deleteByExample(HubSupplierSpuCriteria example);

    int deleteByPrimaryKey(Long supplierSpuId);

    int insert(HubSupplierSpu record);

    int insertSelective(HubSupplierSpu record);

    List<HubSupplierSpu> selectByExampleWithRowbounds(HubSupplierSpuCriteria example, RowBounds rowBounds);

    List<HubSupplierSpu> selectByExample(HubSupplierSpuCriteria example);
    
    List<HubSupplierSpu> selectByBrand(HubSupplierSpuQureyDto dto);
    
    int count(HubSupplierSpuQureyDto dto);

    HubSupplierSpu selectByPrimaryKey(Long supplierSpuId);

    int updateByExampleSelective(@Param("record") HubSupplierSpu record, @Param("example") HubSupplierSpuCriteria example);

    int updateByExample(@Param("record") HubSupplierSpu record, @Param("example") HubSupplierSpuCriteria example);

    int updateByPrimaryKeySelective(HubSupplierSpu record);

    int updateByPrimaryKey(HubSupplierSpu record);
}