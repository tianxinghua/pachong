package com.shangpin.ephub.product.business.ui.studio.hub.service.impl;

import com.shangpin.ephub.product.business.ui.studio.hub.dto.StudioBrandDto;
import com.shangpin.ephub.product.business.ui.studio.hub.service.StudioBrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by loyalty on 17/6/9.
 */
@Service
@Slf4j
public class StudioBrandServiceImpl implements StudioBrandService {


    @Override
    public boolean isNeedShoot(StudioBrandDto studioBrandDto) {
        return true;
    }
}
