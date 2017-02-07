package com.shangpin.ephub.product.business.rest.size.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.rest.size.dto.MatchSizeDto;
import com.shangpin.ephub.product.business.rest.size.result.MatchSizeResult;
import com.shangpin.ephub.product.business.rest.size.service.MatchSizeService;

import lombok.extern.slf4j.Slf4j;

/**
 * 匹配尺码类型
 * @author zhaogenchun
 * @date 2017年2月4日 下午1:51:24
 */
@RestController
@RequestMapping(value = "/match-size")
@Slf4j
public class MatchSizeController {
	
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
		log.info(MatchSizeController.class.getName()+".matchSize接收到的参数为:{}", dto.toString());
		result = matchSizeService.matchSize(dto);
		log.info(MatchSizeController.class.getName()+"验证结果为{}， 耗时{}milliseconds!", result.toString(), System.currentTimeMillis() - start);
		return result;
	}
}
