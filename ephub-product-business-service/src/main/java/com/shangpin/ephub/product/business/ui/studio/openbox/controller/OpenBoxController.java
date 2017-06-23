package com.shangpin.ephub.product.business.ui.studio.openbox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.ui.studio.openbox.dto.OpenBoxQuery;
import com.shangpin.ephub.product.business.ui.studio.openbox.service.OpenBoxService;
import com.shangpin.ephub.product.business.ui.studio.openbox.vo.CheckDetailVo;
import com.shangpin.ephub.product.business.ui.studio.openbox.vo.OpenBoxDetailVo;
import com.shangpin.ephub.product.business.ui.studio.openbox.vo.OpenBoxVo;
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
@RequestMapping("/api/airstudio/open-box")
public class OpenBoxController {
	
	@Autowired
	private OpenBoxService openBoxService;

	@RequestMapping(value="/slot-list",method = RequestMethod.POST)
	public HubResponse<?> slotList(@RequestBody OpenBoxQuery openBoxQuery){
		OpenBoxVo openBoxVo = openBoxService.slotList(openBoxQuery); 
		if(null != openBoxVo){
			return HubResponse.successResp(openBoxVo);
		}else{
			return HubResponse.errorResp("调用接口异常");
		}
 	}
	
	@RequestMapping(value="/slot-detail",method = RequestMethod.POST)
	public HubResponse<?> slotDetail(@RequestBody String slotNo){
		OpenBoxDetailVo detailVo = openBoxService.slotDetail(slotNo);
		return HubResponse.successResp(detailVo); 
	}
	@RequestMapping(value="/slot-detail-check",method = RequestMethod.POST)
	public HubResponse<?> slotDetailCheck(@RequestBody String slotNoSpuId){
		boolean result = openBoxService.slotDetailCheck(slotNoSpuId);
		if(result){
			return HubResponse.successResp(result);
		}else{
			return HubResponse.errorResp("扫码质检接口异常");
		}
	}
	@RequestMapping(value="/check-result",method = RequestMethod.POST)
	public HubResponse<?> checkResult(@RequestBody String slotNo){
		CheckDetailVo detailVo = openBoxService.checkResult(slotNo);
		if(null != detailVo){
			return HubResponse.successResp(detailVo);
		}else{
			return HubResponse.errorResp("调用接口异常");
		}
	}
}
