package com.shangpin.ephub.product.business.ui.studio.studio.controller;

import com.alibaba.fastjson.JSONObject;
import com.shangpin.ephub.client.data.studio.slot.logistic.dto.StudioSlotLogistictTrackDto;
import com.shangpin.ephub.product.business.ui.studio.studio.dto.StudioSlotQueryDto;
import com.shangpin.ephub.product.business.ui.studio.studio.service.IStudioService;
import com.shangpin.ephub.product.business.ui.studio.studio.vo.ErrorConent;
import com.shangpin.ephub.product.business.ui.studio.studio.vo.SlotSpuSupplierQueryDto;
import com.shangpin.ephub.product.business.ui.studio.studio.vo.StudioQueryDto;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
        return  HubResponse.successResp(iStudioService.getPendingProductList(queryDto));
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

    @RequestMapping(value = "/addslotLogistic")
    public HubResponse<?> insertSlotLogistic (@RequestBody StudioQueryDto queryDto){
        String supplierId = queryDto.getSupplierId();
        Long studioSlotId = queryDto.getStudioSlotId();
        String trackName = queryDto.getTrackName();
        String trackingNo = queryDto.getTrackingNo();
        if(StringUtils.isEmpty(supplierId) || studioSlotId ==null|| StringUtils.isEmpty(trackName)|| StringUtils.isEmpty(trackingNo)){
            return  HubResponse.errorResp("传入参数不正确");
        }
       if(iStudioService.insertSlotLogistic(studioSlotId,trackName,trackingNo,queryDto.getCreateUser())){
           return  HubResponse.successResp(null);
       }else {
           return  HubResponse.errorResp("Logistics information added failed!");
       }
    }
    @RequestMapping(value = "/slotlogisticinfo")
    public HubResponse<?> getSlotLogisticInfo (@RequestBody StudioQueryDto queryDto){
        String supplierId = queryDto.getSupplierId();
        Long studioSlotId = queryDto.getStudioSlotId();

        if(StringUtils.isEmpty(supplierId) || studioSlotId ==null){
            return  HubResponse.errorResp("传入参数不正确");
        }
        StudioSlotLogistictTrackDto result = iStudioService.getSlotLogisticInfo(studioSlotId);
        if(result==null){
            return HubResponse.errorResp("Get logistic info is fail!");
        }else {
            return HubResponse.successResp(result);
        }

    }



    @RequestMapping(value = "/studioslotlist")
    public HubResponse<?> getStudioSlotList(@RequestBody StudioSlotQueryDto queryDto) {

        return  HubResponse.successResp(iStudioService.getStudioSlot(queryDto.getStudioId(),queryDto.getStartTime(),
                queryDto.getEndTime(),queryDto.getCategoryNos(),queryDto.getPageIndex(),queryDto.getPageSize()));
    }
    @RequestMapping(value = "/applyslot")
    public HubResponse<?> applySlot(@RequestBody StudioSlotQueryDto upDto) {
        try {
            if( StringUtils.isEmpty(upDto.getStudioSlotIds()) ||
                    StringUtils.isEmpty(upDto.getSupplierId())||
                    StringUtils.isEmpty(upDto.getSupplierUser())){
                return HubResponse.errorResp("参数不正确");
            }
            List<ErrorConent> result = iStudioService.applyUpdateSlot(upDto);
            if (result ==null){
                return HubResponse.successResp(null);
            }else {
                return HubResponse.errorResp(result);
            }
        }catch (Exception ex){
            return HubResponse.errorResp(ex.getMessage());
        }
    }
    @RequestMapping(value = "/studiolist")
    public HubResponse<?> getStudioList( @RequestBody Map<String,String> paramNos) {
        System.out.println(paramNos);
        String categoryNos = paramNos.get("categoryNos");
        List<String> list = null;
            if(!StringUtils.isEmpty(categoryNos)) {
                list=  Arrays.asList(categoryNos.split(",")).stream().collect(Collectors.toList());
            }
        return  HubResponse.successResp(iStudioService.getStudioListByCategory(list));
    }



}
