package com.shangpin.ephub.product.business.ui.studio.studio.controller;

import com.shangpin.ephub.product.business.ui.studio.studio.dto.ReturnSlotQueryDto;
import com.shangpin.ephub.product.business.ui.studio.studio.service.IReturnSlotService;
import com.shangpin.ephub.product.business.ui.studio.studio.vo.SlotSpuSupplierQueryDto;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2017/6/27.
 */
@RestController
@RequestMapping("/returnSlot")
@Slf4j
public class ReturnSlotController {

    @Autowired
    IReturnSlotService iReturnSlotService;
    /*
  * 获取待处理商品列表
  * */
    @RequestMapping(value = "/slotlist",method = RequestMethod.POST)
    public HubResponse<?> getReturnSlotList(@RequestBody ReturnSlotQueryDto queryDto) {
        Long supplierId = queryDto.getSupplierId();
        if(StringUtils.isEmpty(supplierId) ){
            return  HubResponse.errorResp("传入参数不正确");
        }
        return  HubResponse.successResp(iReturnSlotService.getReturnSlotList(supplierId));
    }
}
