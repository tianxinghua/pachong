package com.shangpin.ephub.data.mysql.price.unionselect.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.price.unionselect.bean.PriceQueryDto;
import com.shangpin.ephub.data.mysql.price.unionselect.mapper.HubSupplierPriceMapper;
import com.shangpin.ephub.data.mysql.price.unionselect.po.HubSupplierPrice;

@Service
public class HubSupplierPriceService {

	@Autowired
	private HubSupplierPriceMapper mapper;
	
	public List<HubSupplierPrice> selectByPriceQuery(PriceQueryDto priceQueryDto){
		return mapper.selectByPriceQuery(priceQueryDto);
	}
	
	public int countByQuery(PriceQueryDto priceQueryDto){
		return mapper.countByQuery(priceQueryDto);
	}
}
