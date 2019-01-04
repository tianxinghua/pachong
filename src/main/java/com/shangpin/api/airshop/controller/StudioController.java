package com.shangpin.api.airshop.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangpin.api.airshop.config.ImageCDNConfig;
import com.shangpin.api.airshop.dto.UserInfo;
import com.shangpin.api.airshop.dto.dataTables.DataTablesInput;
import com.shangpin.api.airshop.dto.dataTables.DataTablesOutput;
import com.shangpin.api.airshop.dto.request.SlotDefectiveSpu;
import com.shangpin.api.airshop.product.m.ProductManager;
import com.shangpin.api.airshop.product.o.UploadPicResponse;
import com.shangpin.api.airshop.product.v.ProductVO;
import com.shangpin.api.airshop.service.StudioService;
import com.shangpin.api.airshop.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/2.
 */
@RestController
@RequestMapping("/studio")
@Slf4j
@SessionAttributes(Constants.SESSION_USER) //添加到session作用域
public class StudioController {
    @Autowired
    StudioService studioService;


    //region slot comfirming

    /*
    * 获取待处理商品列表
    * */
    @RequestMapping(value = "/spullist")
    public JSONObject getStayProductList(@RequestParam(value = "brandName", required = false ) String brandName,
                                         @RequestParam(value = "categoryName", required = false ) String categoryName,
                                         @RequestParam(value = "seasonName", required = false ) String seasonName,
                                         @RequestParam(value = "sku", required = false ) String sku,
                                         @RequestParam(value = "itemCode", required = false ) String itemCode,
                                         @RequestParam(value = "startTime", required = false ) String startTime,
                                         @RequestParam(value = "endTime", required = false ) String endTime,
                                         @RequestParam(value="pageIndex",defaultValue="1")int pageIndex,
                                         @RequestParam(value="pageSize",defaultValue="50")int pageSize,
                                        @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {


        String supplierId = userInfo.getSopUserNo();
        return studioService.getPendingProductList(supplierId,brandName,categoryName,seasonName,sku,itemCode,startTime,endTime,pageIndex,pageSize);
    }

    /*
     获取供应商已申请批次
     */
    @RequestMapping(value = "/slotlist")
    public JSONObject getSupplierSlotList( @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {
        String supplierId = userInfo.getSopUserNo();
        return  studioService.getSupplierSlotList(supplierId,"0");
    }

    /*
 * 删除商品供货商商品
 * */
    @RequestMapping(value = "/delslotspu")
    public JSONObject delSlotProduct(@RequestParam(value = "ssid", required = true ) String slotSSId
            , @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {
        String supplierId = userInfo.getSopUserNo();
        return studioService.delSlotProduct(supplierId,slotSSId,userInfo.getAccountName());
    }

    /*
   * 获取批次详情
   * */
    @RequestMapping(value = "/slotinfo")
    public JSONObject getSlotInfo(@RequestParam(value = "slotNo", required = true ) String slotNo
            , @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {
        String supplierId = userInfo.getSopUserNo();
        return  studioService.getSlotInfo(supplierId,slotNo);
    }


    /*
 * 批次添加商品
 * */
    @RequestMapping(value = "/addspu")
    public JSONObject addProductIntoSlot(@RequestParam(value = "slotNo", required = true ) String slotNo
            ,@RequestParam(value = "slotSSId", required = true ) String slotSSId
            , @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {
        String supplierId = userInfo.getSopUserNo();
        return studioService.addProductIntoSlot(supplierId, slotNo, slotSSId, userInfo.getAccountName());
    }

    /*
 * 批次删除商品
 * */
    @RequestMapping(value = "/delspu")
    public JSONObject delProductFromSlot(@RequestParam(value = "slotNo", required = true ) String slotNo
            ,@RequestParam(value = "ssid", required = true ) String slotSSId
            ,@RequestParam(value = "id", required = true ) String slotSSDId
            , @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {
        String supplierId = userInfo.getSopUserNo();
        return studioService.delProductFromSlot(supplierId,slotNo,slotSSId,slotSSDId,userInfo.getAccountName());
    }

    /*
* 验证批次，确定能否发货
* */
    @RequestMapping(value = "/checkslot")
    public JSONObject checkProductAndSendSlot(@RequestParam(value = "slotNo", required = false ) String slotNo
            , @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {
        String supplierId = userInfo.getSopUserNo();
        return studioService.checkProductAndSendSlot(supplierId,slotNo);
    }

    @RequestMapping(value = "/slotprint")
    public JSONObject slotPrint(@RequestParam(value = "slotId", required = false ) String slotId
            , @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {
        String supplierId = userInfo.getSopUserNo();
        return studioService.slotPrint(supplierId,slotId);
    }

    /**
     * 申请批次释放
     * @param slotId
     * @param userInfo
     * @return
     */
    @RequestMapping(value = "/releaseslot")
    public JSONObject getReleaseStudioSlot(@RequestParam(value = "slotId", required = false ) String slotId
            , @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo){

        String supplierId = userInfo.getSopUserNo();
        return studioService.getReleaseStudioSlot(supplierId,slotId,userInfo.getAccountName());
    }

    @RequestMapping(value = "/logisticslot")
    public JSONObject insertSlotLogistic(@RequestParam(value = "slotId", required = false ) String slotId
            , @RequestParam(value = "trackName", required = false ) String trackName
            , @RequestParam(value = "trackingNo", required = false ) String trackingNo
            , @RequestParam(value = "memo", required = false ) String memo
            , @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {
        String supplierId = userInfo.getSopUserNo();
        return studioService.insertSlotLogistic(supplierId,slotId,trackName,trackingNo,memo,userInfo.getAccountName());
    }
    @RequestMapping(value = "/slotlogisticinfo")
    public JSONObject getSlotLogisticInfo(@RequestParam(value = "slotId", required = false ) String slotId
            , @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {
        String supplierId = userInfo.getSopUserNo();
        return studioService.getSlotLogisticInfo(supplierId,slotId);
    }


    //endregion

    //region slot application
    /*
  * 获取待处理商品列表
  * */
    @RequestMapping(value = "/studioslotlist")
    public JSONObject getStudioSlotList(@RequestParam(value = "studioId", required = false ) String StudioId,
                                         @RequestParam(value = "startTime", required = false ) String startTime,
                                         @RequestParam(value = "endTime", required = false ) String endTime,
                                         @RequestParam(value="pageIndex",defaultValue="1")int pageIndex,
                                         @RequestParam(value="pageSize",defaultValue="10")int pageSize,
                                         @RequestParam(value = "categoryNos", required = false ) String categoryNos,
                                         @RequestParam(value = "history", required = false ) int isHistory,
                                         @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {
        String supplierId = userInfo.getSopUserNo();
        return studioService.getStudioSlotList(supplierId,StudioId,startTime,endTime,categoryNos,pageIndex,pageSize,isHistory);
    }

    /*
  * 获取待处理商品列表
  * */
    @RequestMapping(value = "/studiolist")
    public JSONObject getStudioList( @RequestParam(value = "categoryNos", required = false ) String categoryNos,
                                     @RequestParam(value = "isHistory", required = false ) String isHistory,
                                     @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {
        return studioService.getStudioList(categoryNos,isHistory);
    }


    /*
* 批次删除商品
* */
    @RequestMapping(value = "/applyslot")
    public JSONObject applySlot(@RequestParam(value = "slotIds") String slotIds
            , @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {
        String supplierId = userInfo.getSopUserNo();
        return studioService.applySlot(supplierId,slotIds,userInfo.getAccountName());
    }
    //endregion


    //region slot returning

    /**
     * 获取未签收的返回单
     * @param userInfo
     * @return
     */
    @RequestMapping(value = "/returnslotlist")
    public JSONObject getReturnSlotList(@RequestParam(value = "studioId", required = false ) String StudioId,
                                        @RequestParam(value = "startTime", required = false ) String startTime,
                                        @RequestParam(value = "endTime", required = false ) String endTime,
                                        @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {

        String supplierId = userInfo.getSopUserNo();
        return studioService.getReturnSlotList(supplierId,StudioId,startTime,endTime);
    }


    /**
     * 签收返货单
     * @param userInfo
     * @return
     */
    @RequestMapping(value = "/receiveslot")
    public JSONObject ReceiveReturnSlot(@RequestParam(value = "slotId") String slotId,
                                        @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {

        String supplierId = userInfo.getSopUserNo();
        return studioService.ReceiveReturnSlot(supplierId,slotId,userInfo.getAccountName());
    }

    /**
     * 获取已签收返回单，进行拣货
     * @param userInfo
     * @return
     */
    @RequestMapping(value = "/receivedslotlist")
    public JSONObject getReceivedSlotList( @RequestParam(value = "studioId", required = false ) String StudioId,
                                           @RequestParam(value = "startTime", required = false ) String startTime,
                                           @RequestParam(value = "endTime", required = false ) String endTime,
                                           @RequestParam(value="pageIndex",defaultValue="1")int pageIndex,
                                           @RequestParam(value="pageSize",defaultValue="50")int pageSize,
                                           @RequestParam(value="status",defaultValue="1") int status,
                                           @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {

        String supplierId = userInfo.getSopUserNo();
        return studioService.getReceivedSlotList(StudioId, startTime, endTime,pageIndex,pageSize,supplierId ,status);
    }

    /**
     * 获取返回单，已经拣货的商品列表
     * @param userInfo
     * @return
     */
    @RequestMapping(value = "/receivedSlotInfo")
    public JSONObject getReceivedSlotInfo(@RequestParam(value = "slotId") String slotId,
                                          @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {

        String supplierId = userInfo.getSopUserNo();
        return studioService.getReceivedSlotInfo(supplierId,slotId);
    }

    /**
     * 扫描拣货
     * @param userInfo
     * @return
     */
    @RequestMapping(value = "/scanProduct")
    public JSONObject addProductFromScan(@RequestParam(value = "slotId") String slotIds,
                                         @RequestParam(value = "barcode") String barcode,
                                          @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {

        String supplierId = userInfo.getSopUserNo();
        return studioService.addProductFromScan(supplierId,slotIds,barcode,userInfo.getAccountName());
    }

    /**
     * 拣货结果确认
     * @param slotIds
     * @param userInfo
     * @return
     */
    @RequestMapping(value = "/confirmSlot")
    public JSONObject confirmSlotInfo(@RequestParam(value = "slotId") String slotIds,
                                         @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo){
        String supplierId = userInfo.getSopUserNo();
        return studioService.confirmSlotInfo(supplierId,slotIds,userInfo.getAccountName());
    }

    @RequestMapping(value = "/adddefective",method = RequestMethod.POST)
    public JSONObject addDefective(@RequestBody SlotDefectiveSpu defectiveSpu, @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo){
        String supplierId = userInfo.getSopUserNo();
        defectiveSpu.setSupplierId(supplierId);
        defectiveSpu.setUserName(userInfo.getAccountName());
        return studioService.SlotDefectiveSpu(defectiveSpu );
    }
    @RequestMapping(value = "/defectivelist")
    public JSONObject getDefectiveList(@RequestParam(value = "studioId", required = false ) String StudioId,
                                       @RequestParam(value = "startTime", required = false ) String startTime,
                                       @RequestParam(value = "endTime", required = false ) String endTime,
                                       @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo){
        String supplierId = userInfo.getSopUserNo();
        return studioService.getDefectiveList(supplierId,StudioId,startTime,endTime);

    }
    @RequestMapping(value = "/deletedefective")
    public JSONObject DeleteDefective(@RequestParam(value = "id") String id, @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo){
        String supplierId = userInfo.getSopUserNo();
        return studioService.deleteDefective(supplierId,id,userInfo.getAccountName());

    }


    //endregion


}
