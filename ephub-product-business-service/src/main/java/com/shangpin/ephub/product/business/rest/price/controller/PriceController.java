package com.shangpin.ephub.product.business.rest.price.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.price.unionselect.dto.PriceQueryDto;
import com.shangpin.ephub.client.product.business.price.dto.PriceDto;
import com.shangpin.ephub.product.business.rest.price.service.PriceService;
import com.shangpin.ephub.product.business.rest.price.vo.ProductPrice;
/**
 * <p>Title: PriceController</p>
 * <p>Description: 供价推送服务接口 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年3月31日 下午12:09:11
 *
 */
import com.shangpin.ephub.response.HubResponse;
@RestController
@RequestMapping("price")
public class PriceController {
	
	@Autowired
	private PriceService priceService;

	@RequestMapping(value = "/save-and-sendmessage")
	public void savePriceRecordAndSendConsumer(@RequestBody PriceDto priceDto) throws Exception{
		priceService.savePriceRecordAndSendConsumer(priceDto);
	}
	
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public HubResponse<?> priceList(@RequestBody PriceQueryDto priceQueryDto){
		ProductPrice price = priceService.priceList(priceQueryDto);
		if(null != price){
			return HubResponse.successResp(price);
		}else{
			return HubResponse.errorResp("查询参数错误或服务发生异常");
		}
	}
}
