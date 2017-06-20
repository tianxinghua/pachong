package com.shangpin.ephub.product.business.rest.studio.hub.controller;

import com.shangpin.ephub.product.business.rest.studio.hub.dto.StudioBrandDto;
import com.shangpin.ephub.product.business.service.studio.slotbranddic.SlotBrandDicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**

   需要拍照的品牌 业务查询

 */
@RestController
@RequestMapping(value = "/hub-dic-studio-brand")
@Slf4j
public class HubStudioBrandController {

	@Autowired
	SlotBrandDicService studioBrandService;

	@RequestMapping(value = "/is-need-shoot")
	public boolean  isNeedShoot(@RequestBody StudioBrandDto studioBrandDto){
		log.info("isNeedShoot处理信息：{}",studioBrandDto);
		try {
			return studioBrandService.isNeedShoot(studioBrandDto.getHubBrandNo());
		} catch (Exception e) {
			log.error("studioBrandService.isNeedShoot处理发生异常：{}",e);
			e.printStackTrace();
		}
		return false;
	}


}
