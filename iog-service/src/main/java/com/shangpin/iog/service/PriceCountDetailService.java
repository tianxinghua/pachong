package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.PriceCountDetailDTO;
import com.shangpin.iog.dto.DiscountDTO;
import com.shangpin.iog.service.DiscountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaogenchun on 2015/10/13.
 */
public interface PriceCountDetailService {

	 /**
     * 查询所有可用供应商计算价格的标示(1代表市场价、2代表供货价)
     * @param flag(1代表可用，0代表不可用)
     */
    List<PriceCountDetailDTO> findAllByFlag(String state) throws ServiceException;

}
