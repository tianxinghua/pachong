package com.shangpin.iog.product.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.iog.dto.TokenDTO;
import com.shangpin.iog.product.dao.TokenMapper;
import com.shangpin.iog.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService{
	@Autowired
	TokenMapper tokenDAO;

	@Override
	public void saveToken(TokenDTO tokenDTO) throws SQLException {
		tokenDAO.save(tokenDTO);
	}

	@Override
	public TokenDTO findToken(String supplierId) throws SQLException {
		return tokenDAO.findToken(supplierId);
	}

	@Override
	public void refreshToken(TokenDTO tokenDTO) throws SQLException {
		tokenDAO.updateToken(tokenDTO);
	}

}
