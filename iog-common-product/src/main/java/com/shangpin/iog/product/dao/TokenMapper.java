package com.shangpin.iog.product.dao;

import org.apache.ibatis.annotations.Param;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.TokenDTO;

@Mapper
public interface TokenMapper extends IBaseDao<TokenDTO> {
	
	public Integer save(TokenDTO tokenDTO);
	
	public TokenDTO findToken(@Param("accessToken") String accessToken,@Param("supplierId") String supplierId);
	
	public void updateToken(TokenDTO tokenDTO);
}
