package com.shangpin.ephub.product.business.rest.common.controller;

import com.shangpin.ephub.client.product.business.size.result.MatchSizeResult;
import com.shangpin.ephub.product.business.rest.size.dto.MatchSizeDto;
import com.shangpin.ephub.product.business.rest.size.service.MatchSizeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**

    通用
 */
@RestController
@RequestMapping(value = "/match-size")
@Slf4j
public class CommonController {
	
	@Autowired
	MatchSizeService matchSizeService;
	
	/**
	 * 匹配尺码类型
	 * @param dto 数据传输对象
	 * @return 校验结果
	 */
	@RequestMapping(value = "/match")
	public MatchSizeResult matchSize(@RequestBody MatchSizeDto dto){
		long start = System.currentTimeMillis();
		MatchSizeResult result = null;
		log.info(CommonController.class.getName()+".matchSize接收到的参数为:{}", dto.toString());
		result = matchSizeService.matchSize(dto);
		log.info(CommonController.class.getName()+"验证结果为{}， 耗时{}milliseconds!", result.toString(), System.currentTimeMillis() - start);
		return result;
	}
}
