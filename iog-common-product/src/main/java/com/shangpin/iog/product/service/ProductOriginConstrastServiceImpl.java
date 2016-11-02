package com.shangpin.iog.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.iog.dto.ProductOriginConstrastDTO;
import com.shangpin.iog.product.dao.ProductOriginConstrastMapper;
import com.shangpin.iog.service.ProductOriginConstrastService;


@Service
public class ProductOriginConstrastServiceImpl implements ProductOriginConstrastService {

	@Autowired
	ProductOriginConstrastMapper ProductOriginConstrastDAO;
	
	@Override
	public List<ProductOriginConstrastDTO> findByRank(int rank) {		
		return ProductOriginConstrastDAO.findByRank(rank);
	}

	
}
