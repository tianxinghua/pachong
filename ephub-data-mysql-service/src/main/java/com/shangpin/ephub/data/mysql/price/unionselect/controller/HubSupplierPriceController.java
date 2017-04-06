package com.shangpin.ephub.data.mysql.price.unionselect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.price.unionselect.bean.PriceQueryDto;
import com.shangpin.ephub.data.mysql.price.unionselect.po.HubSupplierPrice;
import com.shangpin.ephub.data.mysql.price.unionselect.service.HubSupplierPriceService;

/**
 * <p>Title: HubSupplierPriceController</p>
 * <p>Description: 供价策略需要联合查询的类 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年4月5日 下午6:36:24
 *
 */
@RestController
@RequestMapping("/hub-supplier-price")
public class HubSupplierPriceController {
	
	@Autowired
	private HubSupplierPriceService hubSupplierPriceService;

	@RequestMapping(value="/select-by-price-query")
	public List<HubSupplierPrice> selectByQuery(@RequestBody PriceQueryDto priceQueryDto){
		return hubSupplierPriceService.selectByPriceQuery(priceQueryDto);
	}
	@RequestMapping(value="/count-by-price-query")
	public int countByQuery(@RequestBody PriceQueryDto priceQueryDto){
		return hubSupplierPriceService.countByQuery(priceQueryDto);
	}
}
