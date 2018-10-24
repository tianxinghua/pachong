package com.shangpin.ep.order.module.order.service.impl;


import com.shangpin.ep.order.module.order.bean.TokenDTO;
import com.shangpin.ep.order.module.order.mapper.TokenMapper;
import com.shangpin.ep.order.module.order.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class TokenServiceImpl implements TokenService {
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
