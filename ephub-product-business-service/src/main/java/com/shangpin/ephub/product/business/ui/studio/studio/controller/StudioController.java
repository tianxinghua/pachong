package com.shangpin.ephub.product.business.ui.studio.studio.controller;

import com.shangpin.ephub.product.business.ui.studio.studio.service.IStudioService;
import com.shangpin.ephub.product.business.ui.studio.studio.vo.SlotSpuSupplierQueryDto;
import com.shangpin.ephub.product.business.ui.studio.studio.vo.StudioQueryDto;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/6/8.
 * airshop studio
 */
@RestController
@RequestMapping("/studio")
@Slf4j
public class StudioController {

    @Autowired
    private IStudioService iStudioService;
    /*
    * 获取待处理商品列表
    * */
    @RequestMapping(value = "/spullist",method = RequestMethod.POST)
    public HubResponse<?> getPendingProductList(@RequestBody SlotSpuSupplierQueryDto queryDto) {
        String supplierId = queryDto.getSupplierId();
        if(StringUtils.isEmpty(supplierId) ){
            return  HubResponse.errorResp("传入参数不正确");
        }
        //return  HubResponse.successResp(iStudioService.getPendingProductList(supplierId));
        return null;
    }

    /*
     获取供应商已申请批次
     */
    @RequestMapping(value = "/slotlist")
    public HubResponse<?> getSupplierSlotList(@RequestBody StudioQueryDto queryDto) {
        String supplierId = queryDto.getSupplierId();
        if(StringUtils.isEmpty(supplierId) ){
            return  HubResponse.errorResp("传入参数不正确");
        }
        return  HubResponse.successResp(iStudioService.getSupplierSlotList(supplierId));
    }


    /*
   * 获取批次详情
   * */
    @RequestMapping(value = "/slotinfo")
    public HubResponse<?> getSlotInfo(@RequestBody StudioQueryDto queryDto) {
        String supplierId = queryDto.getSupplierId();
        String slotNo = queryDto.getSlotNo();
        if(StringUtils.isEmpty(supplierId) || StringUtils.isEmpty(slotNo)){
            return  HubResponse.errorResp("传入参数不正确");
        }
        return  HubResponse.successResp(iStudioService.getSlotInfo(supplierId,slotNo));
    }


    /*
 * 批次添加商品
 * */
    @RequestMapping(value = "/addspu",method = RequestMethod.POST)
    public HubResponse<?> addProductIntoSlot(@RequestBody StudioQueryDto queryDto) {
//    public HubResponse<?> addProductIntoSlot(@RequestParam("supplierId") String supplierId,
//                                             @RequestParam("slotNo") String slotNo,
//                                             @RequestParam("slotSSIds") List<String> slotSSIds,@RequestParam(value = "createUser",defaultValue = "") String createUser) {

        String supplierId = queryDto.getSupplierId();
        String slotNo = queryDto.getSlotNo();
        String slotSSIds = queryDto.getSlotSSIds();
        if(StringUtils.isEmpty(supplierId) || StringUtils.isEmpty(slotNo) || StringUtils.isEmpty(slotSSIds)){
            return  HubResponse.errorResp("传入参数不正确");
        }
        List<Long> ssids = Arrays.asList(slotSSIds.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        return iStudioService.addProductIntoSlot(supplierId,slotNo, ssids ,queryDto.getCreateUser());
    }

    /*
 * 批次删除商品
 * */
    @RequestMapping(value = "/delspu")
    public HubResponse<?> delProductFromSlot(@RequestBody StudioQueryDto queryDto) {

        String supplierId = queryDto.getSupplierId();
        String slotNo = queryDto.getSlotNo();
        Long slotSSId = queryDto.getSlotSSId();
        Long slotSSDId = queryDto.getSlotSSDId();
        if(StringUtils.isEmpty(supplierId) || StringUtils.isEmpty(slotNo)|| slotSSId==null || slotSSDId==null){
            return  HubResponse.errorResp("传入参数不正确");
        }
        return iStudioService.delProductFromSlot(supplierId,slotNo, slotSSId, slotSSDId ,queryDto.getCreateUser());
    }

    /*
* 验证批次，确定能否发货
* */
    @RequestMapping(value = "/sendslot")
    public HubResponse<?> checkProductAndSendSlot(@RequestBody StudioQueryDto queryDto) {
        String supplierId = queryDto.getSupplierId();
        String slotNo = queryDto.getSlotNo();
        if(StringUtils.isEmpty(supplierId) || StringUtils.isEmpty(slotNo)){
            return  HubResponse.errorResp("传入参数不正确");
        }
        return  iStudioService.checkProductAndSendSlot(supplierId,slotNo);
    }
}
