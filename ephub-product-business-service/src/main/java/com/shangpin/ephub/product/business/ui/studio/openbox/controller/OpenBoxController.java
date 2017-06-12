package com.shangpin.ephub.product.business.ui.studio.openbox.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.ui.studio.openbox.dto.OpenBoxQuery;
import com.shangpin.ephub.response.HubResponse;
/**
 * <p>Title: OpenBoxController</p>
 * <p>Description: 开箱质检页面的所有调用接口</p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月9日 下午3:47:27
 *
 */
@RestController
@RequestMapping("/open-box")
public class OpenBoxController {

	@RequestMapping(value="/slot-list",method = RequestMethod.POST)
	public HubResponse<?> slotList(@RequestBody OpenBoxQuery openBoxQuery){
		return null;
	}
	
	@RequestMapping(value="/slot-detail",method = RequestMethod.POST)
	public HubResponse<?> slotDetail(@RequestBody String slotNo){
		return null;
	}
	@RequestMapping(value="/slot-detail-check",method = RequestMethod.POST)
	public HubResponse<?> slotDetailCheck(@RequestBody String slotNoSpuId){
		return null;
	}
	@RequestMapping(value="/check-result",method = RequestMethod.POST)
	public HubResponse<?> checkResult(@RequestBody String slotNo){
		return null;
	}
}
