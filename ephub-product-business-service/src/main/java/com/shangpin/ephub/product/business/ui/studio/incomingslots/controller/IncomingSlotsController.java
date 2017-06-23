package com.shangpin.ephub.product.business.ui.studio.incomingslots.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.ui.studio.incomingslots.dto.ConfirmQuery;
import com.shangpin.ephub.product.business.ui.studio.incomingslots.dto.IncomingSlotsQuery;
import com.shangpin.ephub.product.business.ui.studio.incomingslots.service.IncomingSlotsService;
import com.shangpin.ephub.product.business.ui.studio.incomingslots.vo.IncomingSlotsVo;
import com.shangpin.ephub.response.HubResponse;

/**
 * <p>Title: IncomingSlotsController</p>
 * <p>Description: 样品收货页面 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月20日 下午3:28:06
 *
 */
@RestController
@RequestMapping("/incoming-slots")
public class IncomingSlotsController {
	
	@Autowired
	private IncomingSlotsService incomingSlotsService;

	@RequestMapping(value="/list", method= RequestMethod.POST)
	public HubResponse<?> list(@RequestBody IncomingSlotsQuery query){
		IncomingSlotsVo vo = incomingSlotsService.list(query);
		if(null != vo){
			return HubResponse.successResp(vo);
		}else{
			return HubResponse.errorResp("调用接口异常");
		}
	}
	
	@RequestMapping(value="/confirm", method= RequestMethod.POST)
	public HubResponse<?> confirm(@RequestBody ConfirmQuery confirmQuery){
		boolean bool = incomingSlotsService.confirm(confirmQuery);
		if(bool){
			return HubResponse.successResp("");
		}else{
			return HubResponse.errorResp("更新异常。");
		}
	}
	
	
}
