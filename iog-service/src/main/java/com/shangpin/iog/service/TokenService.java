package com.shangpin.iog.service;

import java.sql.SQLException;

import com.shangpin.iog.dto.TokenDTO;


/**
 * @author monkey
 *
 */
public interface TokenService {
	 /**
     * 保存token
     */
	public void saveToken(TokenDTO tokenDTO) throws SQLException;
	/**
	 * 查询
	 * @param supplierId
	 */
	public TokenDTO findToken(String supplierId) throws SQLException;
	
	/**
	 * 更新Token
	 */
	public void refreshToken(TokenDTO tokenDTO) throws SQLException;
	
}
