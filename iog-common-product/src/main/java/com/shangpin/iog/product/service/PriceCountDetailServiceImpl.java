package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.PriceCountDetailDTO;
import com.shangpin.iog.product.dao.PriceCountDetailMapper;
import com.shangpin.iog.service.PriceCountDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaogenchun on 2015/10/13.
 */
@Service
public class PriceCountDetailServiceImpl implements PriceCountDetailService{

    @Autowired
    PriceCountDetailMapper countPriceDAO;

	@Override
	public List<PriceCountDetailDTO> findAllByFlag(String state)
			throws ServiceException {
		
		  List<PriceCountDetailDTO> countPriceDTOList = new ArrayList<>();
		  countPriceDTOList = countPriceDAO.findAllOfAvailabled(state);
	      return countPriceDTOList;
	}

}
