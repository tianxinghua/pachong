package com.shangpin.ep.order.module.order.mapper;

import com.shangpin.ep.order.module.order.bean.TokenDTO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TokenMapper {
	
	public Integer save(TokenDTO tokenDTO);
	
	public TokenDTO findToken(@Param("supplierId") String supplierId);
	
	public void updateToken(TokenDTO tokenDTO);
}
