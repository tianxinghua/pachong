package com.shangpin.ep.order.module.order.service;

import com.shangpin.ep.order.module.order.bean.TokenDTO;


import java.sql.SQLException;


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
