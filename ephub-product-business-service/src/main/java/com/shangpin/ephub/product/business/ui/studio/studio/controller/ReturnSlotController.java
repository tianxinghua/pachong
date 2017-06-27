package com.shangpin.ephub.product.business.ui.studio.studio.controller;

import com.shangpin.ephub.product.business.ui.studio.studio.dto.ReturnSlotQueryDto;
import com.shangpin.ephub.product.business.ui.studio.studio.dto.StudioSlotQueryDto;
import com.shangpin.ephub.product.business.ui.studio.studio.service.IReturnSlotService;
import com.shangpin.ephub.product.business.ui.studio.studio.vo.ErrorConent;
import com.shangpin.ephub.product.business.ui.studio.studio.vo.SlotSpuSupplierQueryDto;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Administrator on 2017/6/27.
 */
@RestController
@RequestMapping("/returnslot")
@Slf4j
public class ReturnSlotController {

    @Autowired
    IReturnSlotService iReturnSlotService;
    /*
  * 获取待处理商品列表
  * */
    @RequestMapping(value = "/returnslotlist",method = RequestMethod.POST)
    public HubResponse<?> getReturnSlotList(@RequestBody ReturnSlotQueryDto queryDto) {
        Long supplierId = queryDto.getSupplierId();
        if(StringUtils.isEmpty(supplierId) ){
            return  HubResponse.errorResp("传入参数不正确");
        }
        return  HubResponse.successResp(iReturnSlotService.getReturnSlotList(queryDto));
    }

    /**
     * 接收返回单
     * @param queryDto
     * @return
     */
    @RequestMapping(value = "/receivereturnslot")
    public HubResponse<?> ReceiveReturnSlot(@RequestBody ReturnSlotQueryDto queryDto) {
        try {
            Long supplierId = queryDto.getSupplierId();
            Long id = queryDto.getId();
            if(StringUtils.isEmpty(supplierId)||StringUtils.isEmpty(id) ){
                return  HubResponse.errorResp("传入参数不正确");
            }
           if(iReturnSlotService.ReceiveReturnSlot(supplierId,id,queryDto.getSupplierUser())){
                return HubResponse.successResp(null);
            }else{
               return HubResponse.successResp("fail!");
           }
        }catch (Exception ex){
            return HubResponse.errorResp(ex.getMessage());
        }
    }
}
