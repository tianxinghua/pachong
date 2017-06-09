package com.shangpin.ephub.product.business.rest.studio.hub.controller;

import com.shangpin.ephub.client.product.business.size.result.MatchSizeResult;
import com.shangpin.ephub.product.business.rest.studio.hub.dto.MatchSizeDto;
import com.shangpin.ephub.product.business.rest.studio.hub.service.HubSlotSpuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 匹配尺码类型
 * @author zhaogenchun
 * @date 2017年2月4日 下午1:51:24
 */
@RestController
@RequestMapping(value = "/match-size")
@Slf4j
public class HubSlotSpuController {
	
	@Autowired
	HubSlotSpuService hubSlotSpuService;
	
	/**
	 * 匹配尺码类型
	 * @param dto 数据传输对象
	 * @return 校验结果
	 */
	@RequestMapping(value = "/match")
	public MatchSizeResult matchSize(@RequestBody MatchSizeDto dto){

	}
}
