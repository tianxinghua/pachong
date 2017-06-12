package com.shangpin.ephub.product.business.rest.studio.hub.service;

import com.shangpin.ephub.product.business.rest.studio.hub.dto.StudioBrandDto;
import org.springframework.stereotype.Service;

/**
 * Created by loyalty on 17/6/8.
 * 需要拍照的品牌
 */

public interface StudioBrandService {


    /**
     * 查询是否需要拍照
     * @param studioBrandDto
     * @return  true:需要  false: 不需要
     */
     public boolean  isNeedShoot(StudioBrandDto studioBrandDto);

}
