package com.shangpin.iog.product.dao;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.HubSupplierValueMappingDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface HubSupplierValueMappingMapper extends IBaseDao<HubSupplierValueMappingDTO> {
	
	public int findCountOfSpvalueType(@Param("spvalueType") Integer spvalueType);

     public List<HubSupplierValueMappingDTO>  findListBySpvalueType(@Param("spvalueType") Integer spvalueType);

    public  HubSupplierValueMappingDTO getMappingBySpBrandIdAndSupplierBrandName(@Param("spValueNo") String spValueNo,@Param("supplierValue") String  supplierValue);
}