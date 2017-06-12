package com.shangpin.ephub.product.business.ui.studio.studio.controller;

import com.shangpin.ephub.product.business.ui.studio.studio.vo.StudioQueryDto;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2017/6/8.
 * airshop studio
 */
@RestController
@RequestMapping("/Studio")
@Slf4j
public class StudioController {

    /*
    * 获取待处理商品列表
    * */
    @RequestMapping(value = "/spullist")
    public HubResponse<?> getPendingProductList(@RequestBody StudioQueryDto queryDto) {

        return  null;//studioService.getStayProductList(supplierNo);
    }

    /*
     获取供应商已申请批次
     */
    @RequestMapping(value = "/slotlist")
    public HubResponse<?> getSupplierSlotList(@RequestBody StudioQueryDto queryDto) {
        return  null;//studioService.getSupplierSlotList(supplierNo,status);
    }


    /*
   * 获取批次详情
   * */
    @RequestMapping(value = "/slotinfo")
    public HubResponse<?> getSlotInfo(@RequestBody StudioQueryDto queryDto) {
        return  null;//studioService.getSlotInfo(supplierNo,slotNo);
    }


    /*
 * 批次添加商品
 * */
    @RequestMapping(value = "/addspu")
    public HubResponse<?> addProductIntoSlot(@RequestBody StudioQueryDto queryDto) {

        return null;// studioService.addProductIntoSlot(supplierNo,slotNo,spuNo);
    }

    /*
 * 批次删除商品
 * */
    @RequestMapping(value = "/delspu")
    public HubResponse<?> delProductFromSlot(@RequestBody StudioQueryDto queryDto) {

        return null;//studioService.delProductFromSlot(supplierNo,slotNo,spuNo);
    }

    /*
* 验证批次，确定能否发货
* */
    @RequestMapping(value = "/sendslot")
    public HubResponse<?> checkProductAndSendSlot(@RequestBody StudioQueryDto queryDto) {

        return null;//studioService.checkProductAndSendSlot(supplierNo,slotNo);
    }
}
