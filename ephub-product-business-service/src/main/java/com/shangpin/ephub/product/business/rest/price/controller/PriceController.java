package com.shangpin.ephub.product.business.rest.price.controller;

import com.shangpin.ephub.client.data.mysql.enumeration.PriceHandleState;
import com.shangpin.ephub.product.business.rest.price.vo.PriceChangeRecordDto;
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

import java.util.List;

@RestController
@RequestMapping("/price")
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

	@RequestMapping(value="/update-price-handle-status",method=RequestMethod.POST)
	public HubResponse<?> updatePriceHandleStatus(@RequestBody List<PriceChangeRecordDto> priceChangeRecordDtos){

		for(PriceChangeRecordDto priceChangeRecordDto:priceChangeRecordDtos) {
			if(null!=priceChangeRecordDto.getId()&&0!=priceChangeRecordDto.getId()){
				if("1".equals(priceChangeRecordDto.getSign())){
					priceService.updateState(priceChangeRecordDto.getId(), PriceHandleState.HANDLED_SUCCESS);
				} else{
					priceService.updateState(priceChangeRecordDto.getId(),PriceHandleState.HANDLE_ERROR,priceChangeRecordDto.getMemo());
				}

			}else{
				try {
					if("1".equals(priceChangeRecordDto.getSign())){
                        priceService.updateState(priceChangeRecordDto.getSupplierId(),priceChangeRecordDto.getSkuNo(),"", PriceHandleState.HANDLED_SUCCESS);
                    } else{
                        priceService.updateState(priceChangeRecordDto.getSupplierId(),priceChangeRecordDto.getSkuNo(),priceChangeRecordDto.getMemo(),PriceHandleState.HANDLE_ERROR);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}


		 return HubResponse.successResp(true);

	}
}
